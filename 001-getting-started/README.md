# 001-Getting-Started: Spring AI

The least amount of setup required to get a Spring AI project running against Google Gemini, themed as a FIFA World Cup 2026 fan assistant.

Two blocking request/response endpoints. Its sibling project `001-getting-started-streams` redoes one of them as a streaming response.

> [!Note]
> **Why are these fixtures wrong?** At the time of development, the model's training data predates the 2026 tournament, so it
> invents plausible-looking fixtures, squads, kickoff times, news and even scores sometimes and other times it says I don't have answer. Keep
> this in mind throughout the series. It is exactly the problem we solve with tool calling in `005-tool-calling`, where we ground the model with real
> tournament data.

---

## 1. Project Setup

1. Go to Spring Initializr (https://start.spring.io/).
2. Select the following settings:
    * Project: Maven
    * Language: Java
    * Java Version: 25
3. Add the following dependencies:
    * Spring Web
    * Google GenAI
4. Click Generate to download the project blueprint.

---

## 2. Configuration

### Environment Variable

Add your Google API key to your environment variables before running the application:

```bash
export GOOGLE_AI_API_KEY="your_api_key_here"
```

### Application Properties

Configure `src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: Worldcup2026
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

Spring AI auto-configures a `ChatClient.Builder`; inject it and build the client:

```java
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Worldcup2026Controller {

	private final ChatClient chatClient;

	public Worldcup2026Controller(final ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}

	@GetMapping("/tournament")
	public String getMatches() {
		return chatClient.prompt()
            .user("""
				Give me a general information about Fifa world cup 2026, including list of the full 26-man squads for every team participating in the
				tournament. One player per line formatted like
				'Player Name — Country'
				
				Also give me the latest news about the tournament and any controversial events.
				""")
            .call()
            .content();
	}

	@GetMapping("/matches")
	public String getMatches(final String date) {
		return chatClient
				.prompt()
				.user("What are the " + date + " World Cup 2026 matches for the afternoon, evening and night kickoffs?")
				.call()
				.content();
	}
}
```

Two endpoints, both on the same anonymous `chatClient`:

* `/tournament` sends a fixed prompt asking for general tournament information: the full 26-man squad for every team, one player per line, plus the
  latest news and controversies.
* `/matches` takes a `date` request parameter and asks what's on that day. There's no
  `@RequestParam` annotation; Spring resolves the plain `String` parameter from the query string on its own. There's also no null check, so calling
  `/matches` without `date` sends the literal word
  `null` into the prompt rather than falling back to a default.

Notice `/matches` is plain string concatenation, not a template: nothing here validates or escapes what the caller sends, so a caller-supplied `date`
becomes part of the prompt verbatim. That's a real prompt-injection surface, left deliberately unfixed here. Prompt templates, which close it, arrive
in `002-chat-client`.

---

## 4. Running and Testing

* Start the application,
* Check and run [Requests.http](src/test/resources/Requests.http)

---

## 5. References

* [Spring AI Getting Started](https://docs.spring.io/spring-ai/reference/getting-started.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)

---

## 6. Exercise

Try the [exercise](EXERCISE.md) before opening `002-chat-client`.
