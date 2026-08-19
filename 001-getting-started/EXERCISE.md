# Exercise: Build It Yourself Before Opening [002-chat-client](../002-chat-client)

You have a working fan assistant with a single, anonymous `ChatClient`. Before you look at
`002-chat-client`, try to evolve this project on your own. Everything below is achievable with
the [Spring AI reference documentation](https://docs.spring.io/spring-ai/reference/api/chatclient.html)
and what you already know from this module.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`002-chat-client` sub-module.

---

## The Challenge

Your single `ChatClient` uses one model for everything. Real applications juggle trade-offs: the flagship model is smart but slow and expensive; the
lite model is fast and cheap but shallower. A fixtures lookup doesn't need the same firepower as tactical analysis.

### Task 1: Multiple named clients

Create a `@Configuration` class that defines three named `ChatClient` beans, one each for
`gemini-3.1-pro-preview`, `gemini-3.5-flash` and `gemini-3.1-flash-lite`, and inject them into the controller by name.

Think about:

* Only one `spring.ai.google.genai.chat.model` property exists. How do you configure the other two models? (Hint: nothing stops you from defining your
  own properties and reading them with
  `@Value`.)
* What is the difference between `ChatClient.create(model)`, `ChatClient.builder(model)` and injecting the auto-configured `ChatClient.Builder`? Which
  one keeps Spring Boot's observability and customisers? Try to find out what you silently lose with the first two.

### Task 2: A shared personality

Give every client the same system prompt so all answers speak as a World Cup 2026 fan guide, without repeating the text in every endpoint.

Think about: where do defaults like this belong, the controller, the prompt, or the client definition?

### Task 3: Prompt template endpoints

`/matches` already accepts an optional `?date=` query parameter, but via plain string concatenation (see this module's README for why that's a
problem). Fix it: switch to a template with a placeholder, `.user(u -> u.text("...{date}...").param("date", date))`. Then extend the same route to
also accept `?stage=...`, asking *"What World Cup 2026 matches are on during the {stage}?"* the same way, on a different model model, when `stage` is
supplied instead of `date`.

Think about: why is `.user(u -> u.text("...{stage}...").param("stage", stage))` better than
`"..." + stage + "..."`? What happens with prompt injection in each case?

### Task 4: Route by cost

Add `GET /tournament` (no parameters, a fixed prompt asking for general tournament information:
full squads, latest news, controversies) and serve it from the flash model. Keep `/matches` on the flagship model.

### Task 5: Temperature

The yaml pins `temperature: 0.0` so answers are deterministic. Add `GET /story?team=...` that tells a short story about a team, and make that one
endpoint creative by overriding the temperature per prompt (try `0.9`).

Verify it worked: call `/story?team=Morocco` twice. Then call `/venues?city=Dallas` twice. Which endpoint gives you a different answer each time, and
why?

### Task 6: See what you're sending

Wire up logging so you can see the actual request hitting the model: system prompt, rendered template, and all. (Hint: Spring AI ships a
`SimpleLoggerAdvisor`, and advisor logging has its own logger category you need to set to `DEBUG`.)

Think about: why might you not want this advisor, as-is, in production?

---

## Helping Material

* [Spring AI Getting Started](https://docs.spring.io/spring-ai/reference/getting-started.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)

---

## Done?

Open [002-chat-client](../002-chat-client) and compare. Pay attention to `GenAiConfig`, including the one bean that exists purely as a warning about
what `ChatClient.create()` costs you.