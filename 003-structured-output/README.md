# 003-Structured-Output: Spring AI

Builds directly on `002-chat-client`. Every endpoint here returns a typed Java record instead of prose.

Added on top of 002:

* A domain model (`Match`, `Venue`, `Player`, ... etc): records annotated with `@JsonPropertyDescription` so the model knows what each field means
* `entity(...)`: mapping a response into a record
* `responseEntity(...)`: keeping the raw `ChatResponse` alongside the mapped entity
* `ParameterizedTypeReference`: returning collections like `List<Venue>`
* Native (provider-side e.g. Gemini) structured output via `EntityParamSpec::useProviderStructuredOutput`, also enabled client-wide on the client in
  `GenAiConfig`
* Schema validation with retries via `EntityParamSpec::validateSchema`
* A custom `WorldCup2026BeanOutputConverter`, Spring AI has a [bug](https://github.com/spring-projects/spring-ai/issues/4426#issuecomment-4676955751)
  where the formatting instructions of the built in `BeanOutputConverter`
  get sent to the model even when native structured output is enabled. This could cost a lot of tokens unnecessarily.

> [!Note]
> **A note on grounding here.** `google-search-retrieval` is on for this module,
> so the model can run a live search before answering instead of relying only on its training
> data, and these fixtures are likely accurate as a result. Search grounding is the model's own
> judgement call, not a guaranteed lookup against a source you control, and nothing in the plain
> text response tells you whether it actually searched. `005-tool-calling` switches
> `google-search-retrieval` back off so the contrast with real tool calling is clean.

---

## 1. Configuration

`application.yaml` dropped the fixed `temperature` value (no endpoint here needs one overridden per call).

```yaml
spring:
  application:
    name: Worldcup2026
  ai:
    google:
      genai:
        api-key: ${GOOGLE_AI_API_KEY}
        chat:
          model: gemini-3.1-flash-lite
          google-search-retrieval: true
          include-server-side-tool-invocations: true
          thinking-budget: 8192
```

`GenAiConfig` now uses two chat client:

* `nativeStructuredOutputEnabledGeminiChatClient`
* `nativeStructuredOutputDisabledGeminiChatClient`

```java

@Bean
public ChatClient nativeStructuredOutputEnabledGeminiChatClient(final ChatClient nativeStructuredOutputDisabledGeminiChatClient) {
	return nativeStructuredOutputDisabledGeminiChatClient.mutate()
			.defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
			.build();
}

@Bean
public ChatClient nativeStructuredOutputDisabledGeminiChatClient(final ChatClient.Builder builder,
		@Value("${spring.ai.google.genai.chat.model}") final String model) {
	return enrichChatClientBuilder(builder)
			.defaultOptions(getGeminiChatOptions(model)).build();
}
```

This means every endpoint that runs on `nativeStructuredOutputEnabledGeminiChatClient` (`/matches/stats`,`/groups/standings`) has structured output
natively without needing to pass it per call, while the other client stays on the appended-format-instructions default unless a specific call opts in
with
`EntityParamSpec::useProviderStructuredOutput` or `.advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)`. Same feature, three ways to reach it,
see section 2.

---

## 2. Source Code

### The domain model

New `model` package. a model like `Player`

```java
public record Player(
		@JsonPropertyDescription("The player's full name, e.g., 'Kylian Mbappé'")
		String name,

		@JsonPropertyDescription("The player's country, e.g., 'France'")
		String country,

		@JsonPropertyDescription("Goals scored during the group stage. Null where not yet verified.")
		Integer goals,

		@JsonPropertyDescription("Assists provided during the group stage. Null where not yet verified.")
		Integer assists,

		@JsonPropertyDescription("Yellow cards received during the group stage. Null where not yet verified.")
		Integer yellowCards,

		@JsonPropertyDescription("Red cards received during the group stage. Null where not yet verified.")
		Integer redCards,

		@JsonPropertyDescription("Number of Man of the Match awards received during the group stage")
		int manOfTheMatchAwards
) {

}
```

gets converted to a schema like the following and sent to the model with instruction in the prompt to return Json

```json
{
  "$schema" : "https://json-schema.org/draft/2020-12/schema",
  "type" : "object",
  "properties" : {
    "name" : {
      "type" : "string",
      "description" : "The player's full name, e.g., 'Kylian Mbappé'"
    },
    "country" : {
      "type" : "string",
      "description" : "The player's country, e.g., 'France'"
    },
    "goals" : {
      "type" : "integer",
      "format" : "int32",
      "description" : "Goals scored during the group stage. Null where not yet verified."
    },
    "assists" : {
      "type" : "integer",
      "format" : "int32",
      "description" : "Assists provided during the group stage. Null where not yet verified."
    },
    "yellowCards" : {
      "type" : "integer",
      "format" : "int32",
      "description" : "Yellow cards received during the group stage. Null where not yet verified."
    },
    "redCards" : {
      "type" : "integer",
      "format" : "int32",
      "description" : "Red cards received during the group stage. Null where not yet verified."
    },
    "manOfTheMatchAwards" : {
      "type" : "integer",
      "format" : "int32",
      "description" : "Number of Man of the Match awards received during the group stage"
    }
  },
  "required" : [ "name", "country", "goals", "assists", "yellowCards", "redCards", "manOfTheMatchAwards" ],
  "additionalProperties" : false
}
```

an example instruction, `BeabOutputConverter` formatter

```text
Your response should be in JSON format.
Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.
Do not include markdown code blocks in your response.
Remove the ```json markdown from the output.
```

Follow the same pattern: flat records, every field described for the model via `@JsonPropertyDescription`

### The new endpoints

Each demonstrates a distinct technique on a distinct question. The `Worldcup2026Controller` documents each endpoint and what it does. The endpoints
mix:

#### Different return types

* `entity(Class)`
* `entity(ParameterizedTypeReference)` for collections
* `responseEntity(Class)`
* `responseEntity(ParameterizedTypeReference)` for collections

#### Per call advisor to enable native structured output via

* `org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec#advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)`

#### Entity parameter specifications for structured outputs and validation:

* `EntityParamSpec::validateSchema` Validates model output against the provided schema
* `EntityParamSpec::useProviderStructuredOutput` Allows chat clients without global native structured output enabled to request provider-native
  structured output.

---

## 3. Running and Testing

* Start the application
* Check and run [Requests.http](src/test/resources/Requests.http)

Compare any of these with the questions answered as prose on `002-chat-client`: same model, similar questions, different contract. The
`SimpleLoggerAdvisor` output shows the schema instructions Spring AI appends to your prompt.

---

## 4. References

* [Spring AI - Structured Output Converter](https://docs.spring.io/spring-ai/reference/api/structured-output-converter.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)

---

## 5. Exercise

Try the [exercise](EXERCISE.md) before opening `004-advisors`.
