/*
 * Licensed to Muhammad Hamadto 2026
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.spring.learning.worldcup;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StreamUtils;

/**
 * Not a microservice like every other module: this app does one thing, embed the World Cup 2026 knowledge base into PGVector, then exits. Splits the
 * markdown into one {@link Document} per {@code ##} section, further chunks each section with {@link TokenTextSplitter}, and skips ingestion if the
 * table already has rows, so restarting this app does not re-embed (and re-pay for) the same content every time.
 */
@SpringBootApplication
public class Worldcup2026Application implements CommandLineRunner {

	private static final Log log = LogFactory.getLog(Worldcup2026Application.class);

	private final VectorStore vectorStore;
	private final JdbcTemplate jdbcTemplate;
	private final Resource knowledgeBase;
	private final String tableName;

	public Worldcup2026Application(final VectorStore vectorStore,
			final JdbcTemplate jdbcTemplate,
			final ResourceLoader resourceLoader,
			@Value("${spring.ai.vectorstore.pgvector.table-name}") final String tableName) {
		this.vectorStore = vectorStore;
		this.jdbcTemplate = jdbcTemplate;
		this.knowledgeBase = resourceLoader.getResource("classpath:knowledge/world-cup-2026.md");
		this.tableName = tableName;
	}

	public static void main(final String[] args) {
		SpringApplication.run(Worldcup2026Application.class, args);
	}

	private static List<Document> splitIntoSections(final String text) {
		final List<Document> sections = new ArrayList<>();
		final StringBuilder currentBody = new StringBuilder();

		String currentHeading = "World Cup 2026 Knowledge Base";
		for (final String line : text.split("\n")) {
			if (line.startsWith("## ")) {
				addSection(sections, currentHeading, currentBody);
				currentHeading = line.substring(3).trim();
				currentBody.setLength(0);
			} else {
				currentBody.append(line).append('\n');
			}
		}
		addSection(sections, currentHeading, currentBody);

		return sections;
	}

	private static void addSection(final List<Document> sections, final String heading, final StringBuilder body) {
		final String content = body.toString().trim();
		if (!content.isEmpty()) {
			sections.add(new Document(content, Map.of("section", heading)));
		}
	}

	@Override
	public void run(final String... args) throws IOException {
		final Long existing = jdbcTemplate.queryForObject("SELECT count(*) FROM " + tableName, Long.class);
		if (existing != null && existing > 0) {
			log.info("Knowledge base table '%s' already has %d chunks, skipping ingestion".formatted(tableName, existing));
			return;
		}

		final String text = StreamUtils.copyToString(knowledgeBase.getInputStream(), StandardCharsets.UTF_8);
		final List<Document> sections = splitIntoSections(text);
		final List<Document> chunks = TokenTextSplitter.builder().build().apply(sections);

		vectorStore.add(chunks);
		log.info("Ingested %d chunks from %d sections of the World Cup 2026 knowledge base".formatted(chunks.size(), sections.size()));
	}
}
