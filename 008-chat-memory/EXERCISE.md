# Exercise: Build It Yourself Before Opening [009-chat-history](../009-chat-history)

Every module so far answers a fan's question, or explains why it can't, and that's the end of it:
nothing durable records what was actually asked and said. `MessageChatMemoryAdvisor`'s JDBC-backed memory shapes what the model sees on the *next*
call, but it's windowed to a fixed message count; once a message falls out of the window, there's no way to answer "what did we actually discuss in
this conversation" at all. Before you look at `009-chat-history`, try to add a durable, queryable record yourself.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`009-chat-history` sub-module.

---

## The Challenge

### Task 1 - Separate two things that sound alike

Write down, in your own words, what `MessageChatMemoryAdvisor`'s chat memory is actually *for*
(hint: it's read back into the next prompt) versus what a durable history record would be for (hint:
it's read by something other than the model). Are they the same data with different storage, or two genuinely different concerns that happen to both
involve "what was said"?

Think about: if you evicted every message from chat memory right now, should an audit log of the conversation still exist?

### Task 2 - Add a table and a repository

Add a `chat_history` table to the same Postgres instance chat memory's JDBC repository already uses, with columns for conversation ID, role, content
and a timestamp. Write a small
`JdbcTemplate`-backed repository with a method to append one entry and one to read a whole conversation back in order.

Think about: does this table need its own datastore, or does reusing Postgres (already on the classpath, already running) actually cost you anything?

### Task 3 - Record every outcome, not just the successful ones

Wire your repository into whatever handles `/chat` today (introduce a thin service layer between the controller and the `ChatClient` if you don't have
one already) so that every call records both the question and the answer, computed first and recorded after, unconditionally. This module only has one
response path, but structure the wrapping so it wouldn't care how many branches fed it.

Think about: what would an audit log that only recorded "successful" generated answers actually be useful for? What questions could it never answer?

### Task 4 - Expose it as its own endpoint

Add a `GET /chat/{conversationId}` route that reads the transcript back as JSON, with no LLM call involved at all.

Think about: why does this deserve its own route instead of being folded into what `/chat` already returns? What's different about the question each
one answers?

---

## Done?

Open [009-chat-history](../009-chat-history) and compare. Pay attention to how `Worldcup2026Service`
separates *deciding* the answer from *recording* it, and to `ChatHistorySchemaInitializer`'s idempotent, `IF NOT EXISTS`-guarded migration, safe to
run on every startup.
