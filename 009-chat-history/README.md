# 009-Chat-History: Spring AI

Builds directly on `008-chat-memory`. `/chat?message=&conversationId=` is unchanged. New: `GET /chat/{conversationId}`, a plain JSON transcript of
everything that conversation ID has ever been asked and answered, read straight from Postgres, no LLM call.

This starts a second arc. `001`-`007` were about making the model's answers better: clients, structured output, advisors, memory. `008`-`015` are
about operating that system responsibly: durable history for audit here, then retrieval and its variants, then observability, then evaluation.

**Chat history is not chat memory, despite the name overlap.** `MessageChatMemoryAdvisor`'s JDBC-backed `ChatMemory` (since `008-chat-memory`,
persisted in the same Postgres instance) shapes what the model sees on the *next* call and is windowed to the last 20 messages. It exists to make
follow-up questions work.
`chat_history` in this module exists for a different job entirely: a durable, append-only record of every question and answer for a conversation ID,
kept regardless of what memory still holds, and never read back into a prompt. If memory evicts a message, the fan's follow-up questions stop being
able to reference it; the audit record in `chat_history` doesn't care, it was never in the business of feeding the model anything.

`Worldcup2026Service.chat(...)` records every request-response pair: this module has only one path to generation (`008-chat-memory`'s memory-aware
chat), so every recorded turn is an ordinary generated answer, but the wrapping shape (compute the answer, then record it, then return it) is what
every later module in the series carries forward untouched, however many more response paths they add.

Added on top of `008-chat-memory`:

* `chat_history` table (`ChatHistorySchemaInitializer`, an idempotent `IF NOT EXISTS` migration), on the same Postgres instance, no new datastore
* `ChatHistoryRepository`, a plain `JdbcTemplate` wrapper: `record(...)` and
  `findByConversationId(...)`
* `GET /chat/{conversationId}`, returning `List<ChatHistoryEntry>` as JSON
* `Worldcup2026Service`, a new thin layer between the controller and `geminiMemoryAwareChatClient`: the controller no longer talks to a `ChatClient`
  directly, it delegates to the service, which wraps the answer with the record-then-return pattern above

---

## 1. Configuration

No new dependency: `JdbcTemplate` and the `postgresql` driver are already on the classpath from
`008-chat-memory`.

```sql
CREATE TABLE IF NOT EXISTS chat_history (
    id BIGSERIAL PRIMARY KEY,
    conversation_id VARCHAR(255) NOT NULL,
    role VARCHAR(32) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS chat_history_conversation_id_idx ON chat_history (conversation_id);
```

`ChatHistorySchemaInitializer` runs this at startup: safe to run every time, a no-op once the table and index exist.

---

## 2. Source Code

`Worldcup2026Service` separates deciding the answer from recording it:

```java
public String chat(final String message, final String conversationId) {
  final String answer = respond(message, conversationId);
  chatHistoryRepository.record(conversationId, "user", message);
  chatHistoryRepository.record(conversationId, "assistant", answer);
  return answer;
}

private String respond(final String message, final String conversationId) {
  return geminiMemoryAwareChatClient.prompt().user(message)
      .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
      .call().content();
}
```

`respond(...)` is exactly `008-chat-memory`'s own `/chat` logic; `chat(...)` wraps it with two inserts that run regardless of what `respond(...)`
returns.

The new endpoint does no LLM work at all:

```java
@GetMapping("/chat/{conversationId}")
public List<ChatHistoryEntry> history(@PathVariable final String conversationId) {
  return chatHistoryRepository.findByConversationId(conversationId);
}
```

```java
public List<ChatHistoryEntry> findByConversationId(final String conversationId) {
  return jdbcTemplate.query(
      "SELECT conversation_id, role, content, created_at FROM chat_history WHERE conversation_id = ? ORDER BY created_at ASC, id ASC",
      (rs, rowNum) -> new ChatHistoryEntry(rs.getString("conversation_id"), rs.getString("role"), rs.getString("content"),
          rs.getTimestamp("created_at").toInstant()),
      conversationId);
}
```

---

## 3. Running and Testing

Same as `008-chat-memory`: Postgres must be running first.

```bash
docker compose up -d postgres                        # from the repository root
cd 009-chat-history && ./mvnw spring-boot:run         # this module
```

```bash
curl "http://localhost:8080/chat?message=Who scored for Morocco against Haiti?&conversationId=009"
curl "http://localhost:8080/chat/009"
```

The second call returns the full transcript for conversation `009` as JSON, straight from Postgres, with no model involved: run it again after enough
further turns would have pushed this exchange out of the window-limited chat memory, and the transcript is still there.

---

## 4. Exercise

Try the [exercise](EXERCISE.md) before opening `010-embedding` and `010-vector-store-rag`.
