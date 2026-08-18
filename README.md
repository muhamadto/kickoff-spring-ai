# Spring AI Tutorial: World Cup 2026 Fan Assistant

A step-by-step tutorial for building a Spring AI application against Google Gemini, themed as a FIFA World Cup 2026 fan assistant. Each
module builds directly on the one before it.

This tutorial does not teach AI concept. It assumes you already are familiar with

* Java
* AI concepts, including
    01. Models
    02. Tokens
    03. Tools calling
    04. MCPs
    05. A2A
    06. Vector stores
    07. Embedding
    08. Evaluation
    09. Observability and time series
    10. RAG

---

## Prerequisites

* Java 25
* A Google AI API key (`export GOOGLE_AI_API_KEY="..."`)
* Docker (for Postgres/pgvector, see `docker-compose.yml`)
* Understanding of AI concepts

## Modules

| Module                        | Topic                                                                                                                                        | README                                          |
|-------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------|
| `001-getting-started`         | Single blocking request/response call                                                                                                        | [README](001-getting-started/README.md)         |
| `001-getting-started-streams` | Same endpoint as a streaming response                                                                                                        | [README](001-getting-started-streams/README.md) |
| `002-chat-client`             | Named `ChatClient` beans per model model, prompt templates, per-prompt options                                                               | [README](002-chat-client/README.md)             |
| `003-structured-output`       | Typed responses: `entity(...)`, `responseEntity(...)`, native structured output, schema validation                                           | [README](003-structured-output/README.md)       |
| `004-advisors`                | The Advisors API: a `PiiRedactionAdvisor` as a cross-cutting `defaultAdvisor`                                                                | [README](004-advisors/README.md)                |
| `008-chat-memory`             | Chat memory across multiple turns                                                                                                            | [README](008-chat-memory/README.md)             |
| `009-chat-history`            | A durable, Postgres-backed audit log of every question and answer, separate from JDBC-backed chat memory                                     | [README](009-chat-history/README.md)            |
| `005-tool-calling`            | Tool calling to ground the model with real tournament data, one `@Tool` class per concern                                                    | [README](005-tool-calling/README.md)            |
| `006-tool-search`             | The model searches an index of its own tools instead of receiving every schema upfront                                                       | [README](006-tool-search/README.md)             |
| `007-mcp-server`              | A standalone MCP server publishing tournament tools, resources, prompts and completions                                                      | [README](007-mcp-server/README.md)              |
| `007-mcp-client`              | The full fan assistant consuming all four MCP capabilities                                                                                  | [README](007-mcp-client/README.md)              |
| `010-embedding`               | Standalone job: embeds the World Cup 2026 knowledge base into PGVector, then exits                                                           | [README](010-embedding/README.md)               |
| `010-vector-store-rag`        | Retrieval-augmented generation over that knowledge base, collapsed to one `/chat` endpoint                                                   | [README](010-vector-store-rag/README.md)        |
| `011-hybrid-search-rag`       | Blends vector similarity search with Postgres full-text search, merged with Reciprocal Rank Fusion                                           | [README](011-hybrid-search-rag/README.md)       |
| `012-agentic-rag`             | The model decides on its own whether to book match tickets — a real, unguarded, side-effecting tool call                                     | [README](012-agentic-rag/README.md)             |
| `013-guarded-rag`             | Deterministic classification, intent and validation gates wrap booking and retrieval, replacing the model's judgement with code              | [README](013-guarded-rag/README.md)             |
| `014-query-optimised-rag`     | Step-back prompting: a model-generated broader query retrieves a second, independent context alongside the fan's exact question              | [README](014-query-optimised-rag/README.md)     |
| `015-observability`           | Micrometer tracing and metrics turn a multi-hop AI request into one inspectable trace                                                        | [README](015-observability/README.md)           |
| `016-evaluation`              | A real integration test scores the guarded pipeline's answers with `RelevancyEvaluator`/`FactCheckingEvaluator` against a local Ollama judge | [README](016-evaluation/README.md)              |

---

## How to Use

Start at `001-getting-started`. Each module has an `EXERCISE.md` that challenges you to build the next step yourself before reading its source.