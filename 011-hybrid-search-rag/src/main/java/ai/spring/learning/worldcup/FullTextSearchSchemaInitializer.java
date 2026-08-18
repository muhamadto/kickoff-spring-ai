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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * No new datastore: {@code 010-embedding} already wrote {@code content} into this same PGVector table. Postgres has full-text search built in
 * (generated {@code tsvector} column, GIN index, {@code ts_rank}), so hybrid search needs one small, idempotent schema addition here rather than a
 * second search infrastructure. Safe to run on every startup; {@code IF NOT EXISTS} makes it a no-op once the column and index exist.
 */
@Component
public class FullTextSearchSchemaInitializer implements ApplicationRunner {

	private final JdbcTemplate jdbcTemplate;
	private final String tableName;
	private final String fullTextColumn;
	private final String fullTextIndex;

	public FullTextSearchSchemaInitializer(final JdbcTemplate jdbcTemplate,
			@Value("${spring.ai.vectorstore.pgvector.table-name}") final String tableName,
			@Value("${worldcup.hybrid-search.full-text-column}") final String fullTextColumn,
			@Value("${worldcup.hybrid-search.full-text-index}") final String fullTextIndex) {
		this.jdbcTemplate = jdbcTemplate;
		this.tableName = tableName;
		this.fullTextColumn = fullTextColumn;
		this.fullTextIndex = fullTextIndex;
	}

	@Override
	public void run(final ApplicationArguments args) {
		jdbcTemplate
				.execute("ALTER TABLE %s ADD COLUMN IF NOT EXISTS %s tsvector GENERATED ALWAYS AS (to_tsvector('english', content)) STORED"
						.formatted(tableName, fullTextColumn));
		jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS %s ON %s USING GIN (%s)".formatted(fullTextIndex, tableName, fullTextColumn));
	}
}
