# Reciprocal Rank Fusion (RRF) & Result Fusion

The core of `HybridSearchAdvisor` is the `fuse()` method, which orchestrates the merging of dense (semantic) and sparse (keyword) search results into
a single context block.

## The Problem RRF Solves

Single-retrieval RAG pipelines suffer from complementary weaknesses:

* **The Vector "Semantic" Blind Spot:** Dense embeddings compress sentences into high-dimensional vector spaces. While excellent for intent and
  paraphrased concepts, they struggle with exact string matching. Proper nouns (e.g., player names like "Mbappé" or "Lamine Yamal", stadium names, or
  exact match scores) can be diluted by semantically similar but factually irrelevant "noise".
* **The Lexical "Keyword" Blind Spot:** Full-text search (`ts_rank`) excels at finding exact tokens, but completely fails when a user asks a
  conceptual question using different terminology than the target document.
* **The Score Normalisation Problem:** Cosine similarity yields scores bounded between 0 and 1, whereas PostgreSQL's `ts_rank` outputs unbounded float
  values depending on term frequency. You cannot simply sum or average these scores, as doing so would heavily bias results towards whichever
  algorithm outputs larger raw numbers.

## Mathematical Foundation

Reciprocal Rank Fusion sidesteps raw score normalisation entirely. Instead of comparing arbitrary relevance scores, RRF evaluates documents based
strictly on their relative rank positions across multiple retrieval lists.

The score for document $d$ is computed as:

$$RRF\_Score (d) = \sum_{m \in M} \frac{1}{k + r_m (d)}$$

Where:

* $M$ is the set of retrieval methods (in our case, Dense and Sparse).
* $r_m (d)$ is the 1-based rank index of document $d$ in retrieval method $m$.
* $k$ is a smoothing constant (`reciprocalRankFusionK`, typically set to 60). It prevents high-ranking documents in small result sets from
  overwhelmingly dominating the score.

## How fuse () Executes Step-by-Step

```text
           [ User Query ]
                 │
   ┌─────────────┴─────────────┐
   ▼                           ▼
[ Dense Results ]       [ Sparse Results ]
(PGVector Similarity)    (Postgres Full-Text)
   │                           │
   │ Rank 1: Doc A             │ Rank 1: Doc B
   │ Rank 2: Doc B             │ Rank 2: Doc A
   │ Rank 3: Doc C             │ Rank 3: Doc D
   └─────────────┬─────────────┘
                 ▼
        [ fuse() Processing ]
                 │
   1. Compute RRF scores: 1 / (60 + rank)
   2. Sum scores across both lists
   3. Deduplicate content by ID
   4. Sort descending & limit to fusedTopK
                 │
                 ▼
  [ Consolidated Prompt Context ]