# 010-Embedding: Spring AI

> [!note]
> Most likely when embedding you will be using a cloud hosted vector store provided by Gemini, Bedrock, ... etc rather than spinning and maintaining
one yourself. However, it's still good to learn how it works.

A standalone embedding job, not a fan-facing application like every other module in this series. Real embedding pipelines are normally offline batch
work, decoupled from the low-latency serving path, so this module and `010-vector-store-rag` mirror the split `007-mcp-server`/`007-mcp-client`
already established: one module populates a datastore (offline), the other serves fans from it. This one reads the World Cup 2026 knowledge base,
embeds it into PGVector, and exits. It has no controller and no REST surface.

* The pgvector extension, enabled on the Postgres instance.
* A knowledge base markdown document covering every category of World Cup 2026 data this series has used: teams, venues, fixtures, results,
  statistics, referees, standings, squads, leaderboards, news and controversies.
* Local ONNX embeddings via `spring-ai-starter-model-transformers`, not a hosted provider
* A single `CommandLineRunner`: chunks the knowledge base into sections, embed it, then exit

---

## 1. Configuration

```xml

<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-model-transformers</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.ai</groupId>
<artifactId>spring-ai-starter-vector-store-pgvector</artifactId>
</dependency>
```

```yaml
spring:
  application:
    name: Worldcup2026Embedding
  ai:
    vectorstore:
      pgvector:
        initialize-schema: true
        table-name: world_cup_knowledge
  datasource:
    url: jdbc:postgresql://localhost:5432/worldcup
    username: worldcup
    password: worldcup
```

`initialize-schema: true` creates the pgvector extension and table on startup if they do not already exist. `010-vector-store-rag` reads from the same
table name, so both modules must agree on it.

<details>
<summary>Using Gemini instead of a local model</summary>

Google GenAI embeddings are a separate Spring AI artifact from the chat starter used everywhere else in this series, the change is just a few lines of
config, nothing more from what this model already has:

* add `spring-ai-starter-model-google-genai-embedding`,
* configure `spring.ai.google.genai.embedding.*` (an API key and `gemini-embedding-001` as the model) in `application.yaml`, and it autoconfigures its
  own `GoogleGenAiTextEmbeddingModel` bean in place of the local one.

> [!Caution]
> `gemini-embedding-001` defaults to 3072 dimensions, against `all-MiniLM-L6-v2`'s 384, and a
> pgvector column's dimension is fixed at `initialize-schema` time. If `world_cup_knowledge` already
> exists at the other dimension, switching models means there is a need for a new table. Otherwise, drop and re-initialise the existing table, not
just a config change.

</details>

---

## 2. Source Code

`Worldcup2026Application` implements `CommandLineRunner`. This app is a kinda task runner job, not a microservice, so there is no controller or config
class for the lesson to live in instead.

```java

@Override
public void run(final String... args) throws IOException {
	final Long existing = jdbcTemplate.queryForObject("SELECT count(*) FROM " + tableName, Long.class);
	if (existing != null && existing > 0) {
		log.info("Knowledge base table '%s' already has %d chunks, skipping ingestion".formatted(tableName, existing));
		return;
	}

	final String text = StreamUtils.copyToString(knowledgeBase.getInputStream(), StandardCharsets.UTF_8);
	final List<Document> sections = splitIntoSections(text);
	final List<Document> chunks = new TokenTextSplitter().apply(sections);

	vectorStore.add(chunks);
	log.info("Ingested %d chunks from %d sections of the World Cup 2026 knowledge base".formatted(chunks.size(), sections.size()));
}
```

The row-count check makes restarts idempotent: re-running this app does not re-embed (and re-pay the compute cost of embedding) the same content every
time. The markdown is split into one `Document` per `##` section first (so each chunk keeps its topic as metadata), then each section is further split
by `TokenTextSplitter` into embedding-sized pieces.

---

## 3. Running and Testing

Postgres must be up first:

```bash
docker compose up -d postgres
./mvnw spring-boot:run
```

Watch the log for the ingested chunk count, then check the table directly if you want to see the result:

```bash
psql -h localhost -U worldcup -d worldcup -c "SELECT count(*) FROM world_cup_knowledge;"
```

Run it again: the log now reports the table already has rows and skips ingestion entirely.

---

# 4. References

* [Embeddings Model API](https://docs.spring.io/spring-ai/reference/api/embeddings.html)
* [Spring Embedding Config and Vector Stores](https://docs.spring.io/spring-ai/reference/api/vectordbs.html)
* [AI for Java Developers by Dan Vega](https://www.youtube.com/watch?v=FzLABAppJfM)
