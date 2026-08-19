# 006-tool-search: Spring AI

Builds directly on `005-tool-calling`. Same `@Tool` components, same endpoint, same hardcoded 2026 World Cup data; nothing about what the tools do
changes. What changes is how many of them the model actually sees on any given call.

`005-tool-calling` registers all tools as `defaultTools` on every `ChatClient`, so every request carries full tool schemas (name, description, every
parameter's description) regardless of whether the question needs one of them or none at all. That is fine at tools; it stops being fine once a real
integration exposes dozens, and it gets actively worse once several tools have similar names or overlapping descriptions, since the model has to
disambiguate between all of them on every single
call. [Spring AI's Tool Search Tool](https://spring.io/blog/2025/12/11/spring-ai-tool-search-tools-tzolov) fixes this by turning tool selection into a
search step: the model sees one tool, searches an index of the real ones by keyword, and only pulls in the definitions it decides it actually needs
for this question.

Added on top of `005-tool-calling`:

* `ToolSearchToolCallingAdvisor`, wrapping the same tools `worldCupTools` already composes; nothing about the tools themselves changes, only how their
  definitions reach the model
* `LuceneToolIndex`, an in-memory keyword index built into `spring-ai-tool-search-advisor`, you can also use `vector` and `regex` instead of `lucene`
  ToolIndex.
* Explicit `advisorOrder(25)` on the search advisor: its actual default order is `Integer.MIN_VALUE + 300`, which would run it before
  `PiiRedactionAdvisor` (10) entirely, the same latent-ordering trap `MessageChatMemoryAdvisor`
  has carried since `008-chat-memory`

---

## 1. Configuration

```xml

<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-tool-search-advisor</artifactId>
</dependency>
```

```yml
spring.ai.chat.client.tool-search-advisor.enabled: true
```

According to [Spring AI documentation](https://docs.spring.io/spring-ai/reference/api/tools.html#_spring_boot_auto_configuration)

> When enabled, the auto-configuration registers a ToolSearchToolCallingAdvisor.Builder bean that transparently replaces the default
> ToolCallingAdvisor — no code changes needed. It also auto-registers a ToolIndex bean unless the application declares one explicitly.

But in reality, only configures a `LuceneToolIndex` bean. `ToolSearchAdvisorAutoConfiguration#toolCallingAdvisorBuilder` never gets called to create
that builder bean. If you have any luck getting it called please let me know, maybe it's something in this codebase and not Spring AI's. I ended up
creating the advisor by hand, which is not a big deal since I did the same for `[PiiRedactionAdvisor` and `SimpleLoggerAdvisor`.

```java

@Bean
public ToolSearchToolCallingAdvisor toolSearchToolCallingAdvisor(final ToolSearchAdvisorProperties properties, final ToolIndex toolIndex) {
	int maxResults = properties.getMaxResults() == null ? 5 : properties.getMaxResults();

	return ToolSearchToolCallingAdvisor.builder()
			.toolIndex(toolIndex)
			.advisorOrder(properties.getAdvisorOrder())
			.maxResults(maxResults)
			.conversationHistoryEnabled(true)
			.sessionIdKeyName(properties.getSessionIdKeyName())
			.build();
}
```

`defaultTools(worldCupTools.toArray())` is unchanged from `005-tool-calling`: the advisor does not replace tool registration, it replaces what happens
*after* registration. The first time it sees a conversation, it indexes every registered tool's name and description into `LuceneToolIndex` under that
conversation's session ID; from then on, the model gets one tool, a search over that index, instead of all schemas up front.

Order `25` places it after `PiiRedactionAdvisor` (`10`, so redacted text is what reaches tool search and tool calls) and after
`MessageChatMemoryAdvisor`
(`20`, so the memory-aware client still has conversation context available when it searches and calls tools), and before `SimpleLoggerAdvisor`
(`30`, so the logger still sees the final answer).

---

## 2. Source Code

Nothing in the `tool` package changed at all; every `@Tool` class is identical to `005-tool-calling`'s. The only thing worth watching is
`advisor: DEBUG`: where `005-tool-calling`shows the model calling a named tool directly (`get-venue`, `get-fixtures`, ...), this module shows an extra
step first, a call to the search tool itself, then the same named tool call once the model has the real definition in hand. One additional round trip,
in exchange for not shipping every tool's full schema on every single request regardless of whether it is ever used.

> [!Note]
>[Spring AI's own benchmarks](https://spring.io/blog/2025/12/11/spring-ai-tool-search-tools-tzolov) report roughly one extra request per conversation
turn on average, against a 34-64% reduction in total tokens spent across the providers they tested, since a handful of extra search calls still cost
far less than a full tool catalog sent with every message. tools is too small a set to demonstrate that ratio convincingly here; the lesson is the
mechanism and the trade-off it makes, not a token count this module's own tool catalog is big enough to make dramatic.

---

## 3. Running and Testing

* Start the application
* Check and run [Requests.http](src/test/resources/Requests.http)

Watch `advisor: DEBUG` for the extra search-tool call ahead of the named tool call, compared against the identical requests in `005-tool-calling`,
where the named tool call is the first and only one.

---

## 4. References

* [Tool Calling]([https://docs.spring.io/spring-ai/reference/api/tools.html](https://docs.spring.io/spring-ai/reference/api/tools.html#_spring_boot_auto_configuration))
* [Spring AI Tool Search Tool (Tzolov)](https://spring.io/blog/2025/12/11/spring-ai-tool-search-tools-tzolov)
* [Anthropic - Code execution with MCP: Building more efficient agents](https://www.anthropic.com/engineering/advanced-tool-use)
* [Anthropic - Code execution with MCP: Building more efficient agents](https://www.anthropic.com/engineering/code-execution-with-mcp)

---

## 5. Exercise

Try the [exercise](EXERCISE.md) before opening `007-mcp-server` and `007-mcp-client`.
