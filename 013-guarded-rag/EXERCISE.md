# Exercise: Build It Yourself Before Opening [014-query-optimised-rag](../014-query-optimised-rag)

`Worldcup2026Service` decides whether a message is worth processing and whether a booking is valid, all before generation runs, but once a message
clears every gate and reaches the ordinary chat path, retrieval still only ever searches against the fan's exact words. A specific question ("What was
the final result of Morocco vs Haiti?") retrieves well because it's exact; a broader one ("How did Morocco do overall?") can miss useful background a
wider search would have surfaced. Before you look at `014-query-optimised-rag`, try to fix this yourself using
[step-back prompting](https://www.langchain.com/blog/query-transformations), a query-transformation technique from Zheng et al., "Take a Step Back:
Evoking Reasoning via Abstraction in Large Language Models."

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`014-query-optimised-rag` sub-module.

---

## The Challenge

### Task 1 - Reproduce the gap

Ask `/chat` a broad, general question about a team's overall tournament performance, one your knowledge base covers piecemeal across several fixtures
rather than in one chunk. Compare the context `QuestionAnswerAdvisor` retrieves for that question against what it retrieves for a narrow, specific one
about a single match. Is the broad question's retrieved context actually useful, or does it miss context a differently-phrased, more general query
would have found?

Think about: why would asking a *more* specific question ever retrieve *less* useful context than a broader one?

### Task 2 - Generate a broader version of the question

Add a bare, memory-free `ChatClient` (no advisors: this call isn't part of the fan's conversation)
and a service that asks it to rewrite the fan's question into one broader question whose answer would supply useful background for the original. Try
it on the question from Task 1.

Think about: which model model does this call actually need? Is it closer to the one-word classification calls `Worldcup2026Service` already makes, or
to the call that generates the fan's final answer?

### Task 3 - Retrieve on the broader question too, without touching the advisor

Write a second retrieval path that runs the same dense vector search `QuestionAnswerAdvisor` already does, but against your step-back query instead of
the fan's raw question. Decide deliberately whether to extract shared code from the advisor, or duplicate the retrieval call in a new, separate class.

Think about: `QuestionAnswerAdvisor` already works correctly. What would sharing code with it cost you if you got the extraction wrong? What do you
actually save by duplicating a few lines instead?

### Task 4 - Inject the extra context without disturbing the fan's original retrieval

Get both retrieval results, the advisor's and your new one, into the same generation call. Decide where the step-back context goes: into the same user
message the fan's question already occupies, or somewhere else.

Think about: `QuestionAnswerAdvisor` decides what to search for by reading the *user* message. If your step-back context lands there too, what happens
to the advisor's own retrieval?

### Task 5 - Decide when this runs

Should step-back retrieval happen for every message that reaches the general chat path, or only some? Should it run before or after
`MessageClassificationService` decides a message is a booking request?

Think about: does a booking request benefit from a broader, abstracted version of itself at all?

---

## Done?

Open [014-query-optimised-rag](../014-query-optimised-rag) and compare. Pay attention to where the step-back context actually lands in the prompt, and
why `QuestionAnswerAdvisor` needed no changes at all to keep working exactly as before.
