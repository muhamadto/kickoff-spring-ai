# Exercise: Build It Yourself Before Opening [016-evaluation](../016-evaluation)

Every test in this series so far either checks plumbing (does the app start, does a request return 200) or, at most, that a response contains an
expected substring. Neither actually proves the model's *answer* was good: a fixed expected string breaks the moment the model rephrases a correct
answer differently, and nothing so far checks whether an answer is actually supported by what was retrieved for it. Before you look at
`016-evaluation`, try to build a real evaluation test yourself, using Spring AI's own `Evaluator` interface.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`016-evaluation` sub-module.

---

## The Challenge

### Task 1 - Pick a judge that isn't the model being judged

Stand up a local model, via Testcontainers, to score answers produced by your real, Gemini-backed pipeline. Don't reuse Gemini as the judge.

Think about: why would using the same model to both answer and judge its own answer be a weaker test than using a different, smaller model to judge
it?

### Task 2 - Write a relevancy check against real retrieval

Pick a handful of golden questions your knowledge base genuinely covers. For each, run the actual question through `Worldcup2026Service.chat(...)`,
the same code path `/chat` uses, retrieve the context yourself with a plain `VectorStore.similaritySearch(...)` call, and score the answer against
that context with `RelevancyEvaluator`.

Think about: `QuestionAnswerAdvisor` has no public search method to call directly, so your test needs its own retrieval call anyway. Since the advisor
itself is just a plain dense search internally now, does that make this test's retrieval genuinely equivalent, or still an approximation of what
actually runs in production?

### Task 3 - Prove the evaluator can actually fail something

Write a second test that feeds `FactCheckingEvaluator` a claim you know contradicts a real, retrieved document (a made-up capacity number for a venue
you have the real figure for, for instance), and assert that it does *not* pass.

Think about: what does an evaluation suite that only ever asserts success actually prove? Why is a deliberately-failing test just as important as the
passing ones?

### Task 4 - Decide the test's shape

Is this a JUnit suite, or a real `/evaluate` HTTP endpoint the app exposes? Write down your reasoning either way before you look at how
`016-evaluation` answers the same question.

Think about: who would actually call an evaluation endpoint in production, and how often? Does that match how the rest of this app's routes get used?

---

## Helping Material

* [Spring AI - Evaluation Testing](https://docs.spring.io/spring-ai/reference/api/testing.html)

---

## Done?

Open [016-evaluation](../016-evaluation) and compare. Pay attention to why it stays a test suite, not a route, and to how `KnowledgeBaseEvaluationIT`
seeds its own small fixture into an empty Testcontainers PGVector instance rather than depending on `010-embedding`'s full ingestion run.
