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

package ai.spring.learning.worldcup.repository;

import ai.spring.learning.worldcup.model.ChatHistoryEntry;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatHistoryRepository {

	private final JdbcTemplate jdbcTemplate;

	public ChatHistoryRepository(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void record(final String conversationId, final String role, final String content) {
		jdbcTemplate.update("INSERT INTO chat_history (conversation_id, role, content) VALUES (?, ?, ?)", conversationId, role, content);
	}

	public List<ChatHistoryEntry> findByConversationId(final String conversationId) {
		return jdbcTemplate
				.query("SELECT conversation_id, role, content, created_at FROM chat_history WHERE conversation_id = ? ORDER BY created_at ASC, id ASC",
						(rs, rowNum) -> new ChatHistoryEntry(
								rs.getString("conversation_id"),
								rs.getString("role"),
								rs.getString("content"),
								rs.getTimestamp("created_at").toInstant()),
						conversationId);
	}
}
