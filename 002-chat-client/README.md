# 002-Chat-Client: Spring AI

Builds directly on `001-getting-started`. Add two new chat clients to demonstrate how to configure and operate different chat clients concurrently

Added on top of `001-getting-started`:

* A `GenAiConfig` class defining named `ChatClient` beans (`geminiProChatClient`,`geminiFlashChatClient`, `geminiFlashLiteChatClient`)
* `defaultSystem`: a shared system prompt applied to every call on the flash and flash-lite clients (the pro client is built manually and skips it,
  see section 1 below)
* `defaultOptions`: pinning a model per client via custom yaml properties
* Prompt templates with `{placeholders}`, `.param(...)` and `.metadata(...)`
* Per-prompt options overriding client defaults: `/story` raises the temperature from the yaml's `0.0` to `0.6`
* `SimpleLoggerAdvisor` plus `DEBUG` logging to see exactly what is sent to the model

> [!Note]
> **A note on grounding here.** `google-search-retrieval` is on for this module (see section 1),
> so the model can run a live search before answering instead of relying only on its training
> data, and these fixtures are likely accurate as a result. Search grounding is the model's own
> judgement call, not a guaranteed lookup against a source you control, and nothing in the plain
> text response tells you whether it actually searched. `005-tool-calling` switches
> `google-search-retrieval` back off so the contrast with real tool calling is clean.

---

## 1. Configuration

`application.yaml` grows from one model to three models, plus generation defaults, search grounding and advisor logging. Only `model` is a Spring AI
property; `flash-model` and `flash-lite-model` are our own custom properties, consumed by `@Value` in `GenAiConfig`:

```yaml
spring:
  application:
    name: Worldcup2026
  ai:
    google:
      genai:
        api-key: ${GOOGLE_AI_API_KEY}
        chat:
          model: gemini-3.1-pro-preview
          flash-model: gemini-3.5-flash
          flash-lite-model: gemini-3.1-flash-lite
          temperature: 0.0
          google-search-retrieval: true
          include-server-side-tool-invocations: true
          thinking-budget: 8192

logging:
  level:
    org:
      springframework:
        ai:
          chat:
            client:
              advisor: DEBUG
```

`google-search-retrieval` and `include-server-side-tool-invocations` turn on Gemini's own live search grounding for every client from this module
onward through `008-chat-memory`. The model can issue a search before answering instead of relying only on training data; see the note above for what
that does and does not guarantee.

The `advisor: DEBUG` logging pairs with `SimpleLoggerAdvisor` so every request and response is printed. **Be careful with sensitive data if you carry
this into production.**

See `GenAiConfig.java` for the full bean wiring. Three things to notice:

1. `geminiProChatClient` builds a client manually via `ChatClient.builder(...)`, wiring the observability beans explicitly and passing the builder
   through `ChatClientBuilderConfigurer`. It only gets a `SimpleLoggerAdvisor`, not the shared system prompt below, a deliberate contrast with the
   other two clients, to show that a manually-built client opts out of whatever defaults you don't wire in yourself.
2. `geminiFlashChatClient` and `geminiFlashLiteChatClient` take the auto-configured `ChatClient.Builder` and pin their model via `defaultOptions`,
   sharing a common `defaultSystem` prompt that frames every answer as World Cup 2026 fan guidance.
3. `geminiFlashGenAiChatClientWithoutObservability` exists as a warning: `ChatClient.create(model)` bypasses the auto-configured builder entirely, so
   you lose metrics. Prefer injecting the autoconfigured `ChatClient.Builder`.

> [!Caution]
> A ChatClient created with:
> * ChatClient#create (GoogleGenAiChatModel.ChatModel), or
> * ChatClient#builder (GoogleGenAiChatModel.ChatModel)
>
> bypasses the autoconfigured ChatClient.Builder, which
means [observability and ChatClientBuilderCustomizer beans are ignored](https://docs.spring.io/spring-ai/reference/api/chatclient.html#_chatclients_for_different_model_types)
---

## 2. Source Code

`/matches` from `001-getting-started` stays on the same route and gains a prompt template, on the flagship (`geminiProChatClient`) model, closing the
string-concatenation gap from `001-getting-started`:

```java

@GetMapping("/matches")
public String getMatches(final String date, final String stage) {
	if (stage != null && !stage.isBlank()) {
		return geminiProChatClient.prompt()
				.user(u -> u.text("What World Cup 2026 matches are on during the {stage}?")
						.param("stage", stage)
						.metadata("messageId", "msg-123"))
				.call()
				.content();
	}
	return geminiProChatClient.prompt()
			.user(u -> u.text("What are the {date} World Cup 2026 matches for the afternoon, evening and night kickoffs?")
					.param("date", date))
			.call()
			.content();
}
```

A fixed, parameter-free endpoint on the flash model, asking for general tournament information:

```java

@GetMapping("/tournament")
public String getTournament() {
	return geminiFlashChatClient.prompt()
			.user("""
					Give me a general information about Fifa world cup 2026, including list of the full 26-man squads for every team participating in the
					tournament. One player per line formatted like
					'Player Name — Country'
					
					Also give me the latest news about the tournament and any controversial events.
					""")
			.call()
			.content();
}
```

And a storytelling endpoint that shows per-prompt options overriding the defaults. The yaml pins
`temperature: 0.0` for deterministic answers, but a story should be creative, so this one call raises it to `0.6`. Hit it twice with the same team:
unlike the other endpoints, you get a noticeably different story each time.

```java

@GetMapping("/story")
public String getStory(final String team) {
	return geminiFlashLiteChatClient.prompt()
			.options(GoogleGenAiChatOptions.builder()
					.temperature(0.6))
			.user(u -> u.text("Tell me a short story about {team} at the World Cup 2026.")
					.param("team", team))
			.call()
			.content();
}
```

---

## 3. Running and Testing

* Start the application
* Check and run [Requests.http](src/test/resources/Requests.http)

Watch the application log while calling. The `SimpleLoggerAdvisor` output shows the system prompt (where one applies), the rendered template, and the
model's raw response.

---

## 4. References

* [Spring AI Getting Started](https://docs.spring.io/spring-ai/reference/getting-started.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)

---

## 5. Exercise

Try the [exercise](EXERCISE.md) before opening `003-structured-output`.
