# Exercise: Build It Yourself Before Opening [010-embedding](../010-embedding) & [010-vector-store-rag](../010-vector-store-rag)

Every grounding technique so far, tool calling and MCP, answers by calling a function and getting back a precise, structured result: a score, a
standings table, a squad list. Retrieval-augmented generation (RAG) answers a different kind of question: one where the useful "data" is prose, not a
record, news, controversies, narrative recaps, and a vector store is what lets you search that prose by meaning instead of exact keyword match. Before
you look at `010-embedding` and
`010-vector-store-rag`, try to build both halves yourself. The
[vector databases documentation](https://docs.spring.io/spring-ai/reference/api/vectordbs.html)
and [retrieval-augmented generation documentation](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html)
cover everything below.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`010-embedding` and `010-vector-store-rag` sub-modules.

---

## The Challenge

### Task 1 - Stand up a vector store

Enable the pgvector extension on the Postgres service already in your `docker-compose.yml` (added in `008-chat-memory` for chat memory). Add
`spring-ai-starter-vector-store-pgvector` to a new module and configure `spring.datasource.*` plus `spring.ai.vectorstore.pgvector.*` to point at it.

Think about: what does `initialize-schema: true` actually create for you? What happens if two separate applications point at the same table name?

### Task 2 - Pick an embedding model, then work around a real gap

Try wiring up Google GenAI embeddings the way you'd expect, `spring.ai.google.genai.embedding.*`
properties, the `spring-ai-starter-model-google-genai` dependency you already have. Watch it fail to produce an `EmbeddingModel` bean. Then switch to
`spring-ai-starter-model-transformers` instead.

Think about: why would an autoconfiguration module fully wire and document a bean that its own dependency doesn't actually ship? What does local,
ONNX-based embedding cost you compared to a hosted model, in accuracy, latency and operational simplicity?

### Task 3 - Build a knowledge base and ingest it, offline

Write a markdown document covering categories this series' data already has, teams, venues, fixtures, results, news, controversies, but as prose, not
records. In a separate module (no controller, no REST surface), split it into sections, chunk each section, and embed it into your vector store with a
`CommandLineRunner`. Make re-running it a no-op once the table has rows.

Think about: why does this app not have a `@RestController`? What would go wrong if embedding ran inside the fan-facing app on every request instead
of as a separate, offline step?

### Task 4 - Ground a chat client with retrieval instead of tools

In your fan-facing app, wire a `QuestionAnswerAdvisor` backed by the same vector store as a
`defaultAdvisor`. Ask it something your knowledge base covers, then something it doesn't.

Think about: what does the advisor actually inject into the prompt? How is this different from a
`@Tool` the model chooses to call?

### Task 5 - Collapse, don't accumulate

Every endpoint since `003-structured-output` has accumulated forward. This time, replace all of them with a single
`GET /chat?question=&conversationId=`, and carry chat memory and chat history forward from `008-chat-memory` and `009-chat-history` onto it. Unlike
every earlier module, keep the response as plain prose (`.call().content()`) rather than a typed `entity(Class)` call: a RAG-grounded conversational
answer is closer to `/controversies`' narrative territory than to a structured record, and a rigid schema would flatten it. `009-chat-history` already
revisited the single-endpoint shape once, adding `GET /chat/{conversationId}` alongside `/chat` for history read-back; that's a second endpoint next
to the collapsed one, not a reason to accumulate again, and every RAG variant that follows keeps both.

Think about: why does collapsing the endpoint surface make sense specifically for this lesson, when every earlier module kept accumulating instead?
Why does a typed, schema-validated response stop making sense once the answer is retrieval-grounded prose instead of a fact lookup?

---

## Helping Material

### Embeddings

* [Spring AI - Embeddings Model API](https://docs.spring.io/spring-ai/reference/api/embeddings.html)
* [Spring AI - Embedding Config and Vector Stores](https://docs.spring.io/spring-ai/reference/api/vectordbs.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)

### RAG

* [Spring AI - Retrieval Augmented Generation](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)

---

## Done?

Open [010-embedding](../010-embedding) and [010-vector-store-rag](../010-vector-store-rag) and compare. Pay attention to why they are two separate
modules rather than one: real embedding pipelines are normally offline, decoupled from the low-latency serving path, the same reasoning behind
splitting
`007-mcp-server` from `007-mcp-client`.
