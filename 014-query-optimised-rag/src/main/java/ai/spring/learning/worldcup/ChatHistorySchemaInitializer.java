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

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Idempotent migration, safe to run on every startup: {@code IF NOT EXISTS} throughout. {@code chat_history} is a plain audit log, deliberately
 * separate from the JDBC-backed {@code ChatMemory}: memory shapes what the model sees on the next call and is windowed to a fixed message count; this
 * table is a durable record of every request and answer, kept regardless of what memory still holds, and never read back into a prompt.
 */
@Component
public class ChatHistorySchemaInitializer implements ApplicationRunner {

	private final JdbcTemplate jdbcTemplate;

	public ChatHistorySchemaInitializer(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void run(final ApplicationArguments args) {
		this.jdbcTemplate.execute("""
				CREATE TABLE IF NOT EXISTS chat_history (
				    id BIGSERIAL PRIMARY KEY,
				    conversation_id VARCHAR(255) NOT NULL,
				    role VARCHAR(32) NOT NULL,
				    content TEXT NOT NULL,
				    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
				)
				""");
		jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS chat_history_conversation_id_idx ON chat_history (conversation_id)");
	}
}
