# Exercise: Build It Yourself Before Opening [011-hybrid-search-rag](../011-hybrid-search-rag)

`/chat` grounds every answer through vector similarity search alone. Embeddings represent meaning, not exact tokens: a fan asking about a specific
player or venue by name can get an answer that's topically close but factually wrong, because proper nouns often embed worse than plain keyword
matching would. Before you look at `011-hybrid-search-rag`, try to fix this yourself. The [
`QuestionAnswerAdvisor`](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html)
and [Postgres full-text search](https://www.postgresql.org/docs/current/textsearch.html)
documentation cover everything below.

Work on a copy of this project. When you're done (or stuck), compare your solution with the
`011-hybrid-search-rag` sub-module.

---

## The Challenge

### Task 1 - Reproduce the failure

Ask `/chat` a question naming an exact player, team, or stadium, one your knowledge base genuinely covers. Then ask a near-miss variant (a similar
player from a different team, a misspelled stadium name). Does the retrieved context still name the right entity? Try a few questions until you find
one where vector search alone gets confidently, plausibly wrong.

Think about: why does an embedding model conflate two different proper nouns that a keyword search never would?

### Task 2 - Add full-text search alongside your vector store

Postgres already has full-text search built in: a `tsvector` column and a GIN index, both queryable with plain SQL, no new datastore. Add a generated
column to the same table
`010-embedding` populated (`GENERATED ALWAYS AS (to_tsvector('english', content)) STORED` keeps it in sync automatically), and index it.

Think about: why a generated column instead of a trigger, or a manual backfill after every ingestion run? What happens to the column's value if a
row's `content` never changes?

### Task 3 - Query both, separately

Write two queries against the same question: `VectorStore.similaritySearch(...)` for dense retrieval, and a raw `ts_rank`-ordered SQL query (via
`JdbcTemplate`) for sparse retrieval. Run both for the same failing question from Task 1 and compare the two ranked lists side by side. Which one
actually contains the right entity this time?

Think about: `QuestionAnswerAdvisor` only exposes the vector half of this. What interface would you need to implement instead to run a second,
independent query?

### Task 4 - Merge the two ranked lists

Implement Reciprocal Rank Fusion: for each document, sum `1 / (k + rank)` across every list it appears in, then sort by the fused score. A document
ranked somewhere in *both* lists should usually outrank one ranked first in only one of them.

Think about: what happens to a document that only the keyword search found, and never appeared in vector search at all? Should RRF still be able to
surface it?

### Task 5 - Wire it into the advisor chain

Write your own `CallAdvisor` that runs Tasks 3 and 4, then injects the fused context into the prompt the same way `QuestionAnswerAdvisor` does, and
register it in the same advisor slot, replacing
`QuestionAnswerAdvisor` rather than running alongside it.

Think about: does your advisor need to run before or after `PiiRedactionAdvisor`? What should it search on if the fan's raw message contained PII, the
redacted question or the original?

---

## Done?

Open [011-hybrid-search-rag](../011-hybrid-search-rag) and compare. Pay attention to
`FullTextSearchSchemaInitializer` (an idempotent, `IF NOT EXISTS` migration that's safe to run on every startup) and `HybridSearchAdvisor`'s
Reciprocal Rank Fusion implementation.
