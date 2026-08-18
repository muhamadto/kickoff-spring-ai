# 016-Evaluation: Spring AI

Builds directly on `015-observability`. No route-shaped change, no new production code at all:
this module is a JUnit test suite, `KnowledgeBaseEvaluationIT`, that scores the real guarded pipeline's answers against Spring AI's own evaluators.

**Test-suite form, not a `/leaderboard/.../evaluate` endpoint.** The original sketch for this module (see `ENDPOINT-JOURNEY.md`) left this an open
question: a real `GET .../evaluate` route, consistent with every other lesson in the series being `curl`-able, or a JUnit suite using the same
evaluators. This module goes with the JUnit suite, because that is how evaluation is actually used in practice:
as a regression gate that runs in CI against a golden set, not a production endpoint a fan would ever call. Making it callable over HTTP would mean
either exposing "did the last answer pass a relevancy check" as a real, permanent part of the app's public surface for a concern that's actually about
testing the app, or building a second, parallel evaluation-only code path just to have something to route to. Neither is worth it for what a test
class already does directly.

**Why an evaluator instead of a golden-answer string match.** A fixed expected string ("Ma Ning is from China") breaks the moment the model rephrases
a correct answer a different way. Spring AI's
`Evaluator` interface scores an answer against context instead of against an exact string:
`RelevancyEvaluator` checks whether the response is actually in line with the retrieved context (catches the model answering something unrelated to
what it retrieved), `FactCheckingEvaluator`
checks whether a claim is supported by a document (catches a claim that contradicts the evidence outright). Both delegate to a real LLM call to make
that judgement, the same way a human reviewer would read the answer against the source rather than diffing strings.

**The judge is not the model being judged.** `Worldcup2026Service` (the pipeline under test) still answers through the real, Gemini-backed
`mainChatClient`, `stepBackChatClient` and
`classificationChatClient`, unchanged from `014-query-optimised-rag`. The two evaluators scoring its output run on Gemini flash-lite instead of the
pro model doing the generating, the same model `classificationChatClient` already uses in production. This matches the evaluation-testing
documentation's own guidance directly: the model doing the judging can, and often should, differ from the model doing the generating, and a cheap,
fast model is genuinely enough for a YES/NO-shaped judgement even when the generation model is much larger. Both evaluators need the same
`GOOGLE_AI_API_KEY` the pipeline under test does, since there's only one model provider in play now.

Added on top of `015-observability`:

* `KnowledgeBaseEvaluationIT`, a real (not mocked) end-to-end test: seeds a small fixture set of facts directly into the Testcontainers PGVector
  instance, then runs each golden question through the actual `Worldcup2026Service.chat(...)`, the same code path `/chat` uses, and scores the result
* `EvaluationTestConfig`, test-scoped only: two extra beans, `RelevancyEvaluator` and `FactCheckingEvaluator`, both built on the same
  auto-configured `ChatClient.Builder` and flash-lite model `GenAiConfig` already uses elsewhere. Nothing in `src/main` changed: evaluation
  infrastructure has no business being part of the running app

---

## 1. Architecture

[![Spring AI Architecture Diagram](./docs/architecture.svg)](./docs/architecture.svg)

---

## 2. Why the Testcontainers PGVector instance needs seeding

Every module since `010-vector-store-rag` has relied on `010-embedding` having already ingested the full generated knowledge base into the *real*
Postgres instance from `docker-compose.yml`. The Testcontainers PGVector instance this test spins up is a fresh, empty container: `010-embedding`'s
ingestion never runs against it. `seedKnowledgeBase()` writes a handful of the exact facts the golden questions need, via the same
`VectorStore.add(...)` call `010-embedding` itself makes, just for a small fixture instead of the full knowledge base.
`@TestInstance(Lifecycle.PER_CLASS)` is what makes a non-static `@BeforeAll` legal here: it needs `@Autowired VectorStore vectorStore`, which only
exists on the test instance, not statically.

---

## 3. The judge model, flash-lite, test-scoped only

`EvaluationTestConfig`, a `@TestConfiguration` in its own file (explicitly `@Import`ed onto
`KnowledgeBaseEvaluationIT`, rather than nested inside it, since Spring Boot's auto-detection of a static nested `@TestConfiguration` only applies
while it *is* nested), builds both evaluators on the same flash-lite model `classificationChatClient` already uses in production:

```java
@Bean
RelevancyEvaluator relevancyEvaluator(final ChatClient.Builder builder,
    @Value("${spring.ai.google.genai.chat.flash-lite-model}") final String model) {
  return RelevancyEvaluator.builder().chatClientBuilder(evaluationChatClientBuilder(builder, model)).build();
}

@Bean
FactCheckingEvaluator factCheckingEvaluator(final ChatClient.Builder builder,
    @Value("${spring.ai.google.genai.chat.flash-lite-model}") final String model) {
  return FactCheckingEvaluator.builder(evaluationChatClientBuilder(builder, model)).build();
}

private static ChatClient.Builder evaluationChatClientBuilder(final ChatClient.Builder builder, final String model) {
  return builder.defaultOptions(GoogleGenAiChatOptions.builder().model(model).temperature(0.0));
}
```

This is entirely separate from `GenAiConfig`, deliberately: nothing here is production wiring, it exists only so this test class has evaluators to
autowire, each with its own bare `ChatClient.Builder`, no advisors, no shared state with `mainChatClient`, `stepBackChatClient` or
`classificationChatClient`. `FactCheckingEvaluator.builder(...)` uses the library's generic fact-checking prompt rather than the
`forBespokeMinicheck(...)` factory: that factory's prompt is tuned for a specific locally-hosted model this module no longer runs, so the generic
prompt is the correct choice for a Gemini-backed judge.

No container pull, no model download: both evaluators reuse the same auto-configured Google GenAI provider the production pipeline already
authenticates with, so nothing extra needs to start before this test can run.

---

## 4. The tests

```java
@ParameterizedTest
@MethodSource("goldenQuestions")
void answerIsRelevantToRetrievedContext(final String question) {
  final List<Document> context = vectorStore.similaritySearch(SearchRequest.builder().query(question).topK(3).build());
  final String answer = worldcup2026Service.chat(question, "016-evaluation-test");

  final EvaluationResponse response = relevancyEvaluator.evaluate(new EvaluationRequest(question, context, answer));

  assertThat(response.isPass()).as(response.getFeedback()).isTrue();
}
```

Four golden questions run through this, each hitting the real `MessageClassificationService`,
`QuestionAnswerAdvisor` and the step-back retrieval `014-query-optimised-rag` added, before the local
`RelevancyEvaluator` scores the result: this is evaluating the actual guarded pipeline's output, not a hand-rolled stand-in for it. Context for the
evaluator itself comes from a plain
`VectorStore.similaritySearch(...)` call against the same store, not from calling either retrieval path directly: `QuestionAnswerAdvisor` is a
`CallAdvisor` with no public search method, only
`adviseCall(ChatClientRequest, CallAdvisorChain)`, and replicating
`StepBackSearchService`'s own query rewrite just for a relevancy check isn't worth it either.

```java
@Test
void factCheckingCatchesAContradictedClaim() {
  final List<Document> context = vectorStore.similaritySearch(SearchRequest.builder().query("What is the seating capacity of Estadio Akron?").topK(1).build());

  final EvaluationResponse response = factCheckingEvaluator.evaluate(new EvaluationRequest(context.getFirst().getText(), List.of(), "Estadio Akron has a seating capacity of 90,000."));

  assertThat(response.isPass()).as(response.getFeedback()).isFalse();
}
```

This one asserts a **failure**: a deliberately wrong claim (90,000, the real figure is 45,664)
should not pass fact-checking against the real retrieved document. An evaluation suite that only ever asserts `isTrue()` hasn't proven the evaluator
can catch anything.

---

## 5. Running

`Worldcup2026Service`'s own generation and deflection calls, and now both evaluators too, need a live Gemini model, so
`KnowledgeBaseEvaluationIT` stays gated behind a real key:

```java
@EnabledIfEnvironmentVariable(named = "GOOGLE_AI_API_KEY", matches = ".+")
```

Without one, the whole class is skipped, not faked: `./mvnw test` still passes,
`Worldcup2026ApplicationIT`'s context-load check still runs, but nothing here pretends to have verified an evaluator result it never actually called.
Set a real key and run it for real (Docker is required, for Postgres only):

```bash
GOOGLE_AI_API_KEY=your-key ./mvnw test -pl 016-evaluation
```
