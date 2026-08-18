# 001-Getting-Started-Streams: Spring AI

The same World Cup 2026 fan assistant as `001-getting-started`, streaming its answer token by token instead of blocking until the full response is
ready. The route here is `/matches`, but the prompt it sends is the same fixed, general-information one as `001-getting-started`'s
`/tournament` endpoint (full squads for every team plus the latest news and controversies), not
`001-getting-started`'s own date-scoped `/matches` prompt. Same route name across the two modules, different content, worth knowing before you go
comparing `curl` output.

Streaming matters for AI endpoints because full responses can take many seconds to generate. With a stream, the fan sees the first line while the
model is still writing the last one.

> [!Note]
> This submodule illustrates the streaming programming model for completeness. From here on, the tutorial focuses on Spring AI with the
> synchronous (blocking) programming model and will not cover streams again.

> [!Note]
> **Why are these fixtures wrong?** Same caveat as `001-getting-started`: the model's training data predates the 2026 tournament, so it streams
> confidently invented squads, news and controversies. We fix this with tool calling in `005-tool-calling`.

---

## 1. Project Setup

1. Go to Spring Initializr (https://start.spring.io/).
2. Select the following settings:
    * Project: Maven
    * Language: Java
    * Java Version: 25
3. Add the following dependencies:
    * Spring Reactive Web (WebFlux, instead of Spring Web)
    * Google GenAI
4. Click Generate to download the project blueprint.

---

## 2. Configuration

### Environment Variable

```bash
export GOOGLE_AI_API_KEY="your_api_key_here"
```

### Application Properties

```yaml
spring:
  application:
    name: Worldcup2026-streams
  ai:
    google:
      genai:
        api-key: ${GOOGLE_AI_API_KEY}
        chat:
          model: gemini-3.5-flash
```

---

## 3. Source Code

### Controller

Three differences from the blocking version: the return type is `Flux<String>`, the mapping produces
`text/event-stream`, and the call chain uses `.stream()` instead of `.call()`:

```java
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class Worldcup2026Controller {

	private final ChatClient chatClient;

	public Worldcup2026Controller(final ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}

	@GetMapping(value = "/matches", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> getMatches() {
		return chatClient.prompt()
				.user("""
						Give me a general information about Fifa world cup 2026, including list of the full 26-man squads for every team participating in the
						tournament. One player per line formatted like
						'Player Name — Country'
						
						Also give me the latest news about the tournament and any controversial events.
						""")
				.stream()
				.content();
	}
}
```

---

## 4. Running and Testing

* Start the application, then test the endpoint. Use the `-N` (no-buffer) flag to see the tokens arriving in real time:
* Check and run [Requests.http](src/test/resources/Requests.http)

> [!Warning]
> **Why Gemini Can Look Like It Is Not Streaming?** Gemini streams in a few large chunks. For a short answer that is often a single chunk, so a
> streamed response can look identical to a blocking one. Other providers (OpenAI, Ollama) stream per token. The long, multi-team squad-list-plus-news
> prompt above gives Gemini enough output to show visible chunking.

---

## 5. References

* [Spring AI Getting Started with streaming responses]([https://docs.spring.io/spring-ai/reference/getting-started.html](https://docs.spring.io/spring-ai/reference/api/chatclient.html#_streaming_responses))
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)

---
