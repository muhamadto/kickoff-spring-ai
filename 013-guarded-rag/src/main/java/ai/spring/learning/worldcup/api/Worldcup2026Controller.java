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

package ai.spring.learning.worldcup.api;

import ai.spring.learning.worldcup.model.ChatHistoryEntry;
import ai.spring.learning.worldcup.repository.ChatHistoryRepository;
import ai.spring.learning.worldcup.service.BookingService;
import ai.spring.learning.worldcup.service.Worldcup2026Service;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Same route as every RAG module since {@code 010-vector-store-rag}: {@code /chat} does not change contract. What's different from
 * {@code 012-agentic-rag} is what's on the other side of it: the controller no longer talks to a {@link ChatClient} directly at all, it delegates to
 * {@link Worldcup2026Service}, which decides whether a message is a booking request, and whether that booking is valid, in plain Java before
 * {@link BookingService} ever records anything. A question that isn't a booking request falls straight through to the same advisor-grounded RAG chat
 * as always. {@code /chat/{conversationId}} is new: a plain JSON transcript read straight from Postgres, no LLM call.
 */
@RestController
public class Worldcup2026Controller {

	private final Worldcup2026Service worldcup2026Service;
	private final ChatHistoryRepository chatHistoryRepository;

	public Worldcup2026Controller(final Worldcup2026Service worldcup2026Service, final ChatHistoryRepository chatHistoryRepository) {
		this.worldcup2026Service = worldcup2026Service;
		this.chatHistoryRepository = chatHistoryRepository;
	}

	@GetMapping("/chat")
	public String chat(final String question, final String conversationId) {
		return worldcup2026Service.chat(question, conversationId);
	}

	@GetMapping("/chat/{conversationId}")
	public List<ChatHistoryEntry> history(@PathVariable final String conversationId) {
		return chatHistoryRepository.findByConversationId(conversationId);
	}
}
