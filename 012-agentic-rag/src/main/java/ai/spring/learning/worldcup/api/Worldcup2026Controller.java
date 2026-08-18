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
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Same route as {@code 010-vector-store-rag} and {@code 011-hybrid-search-rag}, back to {@code 010}'s plain {@code QuestionAnswerAdvisor}-grounded
 * retrieval rather than {@code 011}'s hand-rolled fusion (see {@code GenAiConfig}). What's new is {@code BookingTool}: the model can now call
 * {@code bookMatchTicket} on top of the usual grounded answer, its own decision, whenever it judges a fan's message is a genuine booking request,
 * served by a single {@code geminiChatClient}. {@code /chat/{conversationId}} is new: a plain JSON transcript read straight from Postgres, no LLM
 * call.
 */
@RestController
public class Worldcup2026Controller {

	private final ChatClient geminiChatClient;
	private final ChatHistoryRepository chatHistoryRepository;

	public Worldcup2026Controller(final ChatClient geminiChatClient, final ChatHistoryRepository chatHistoryRepository) {
		this.geminiChatClient = geminiChatClient;
		this.chatHistoryRepository = chatHistoryRepository;
	}

	@GetMapping("/chat")
	public String chat(final String question, final String conversationId) {
		final String answer = geminiChatClient
				.prompt()
				.user(question)
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
				.call()
				.content();
		chatHistoryRepository.record(conversationId, "user", question);
		chatHistoryRepository.record(conversationId, "assistant", answer);
		return answer;
	}

	@GetMapping("/chat/{conversationId}")
	public List<ChatHistoryEntry> history(@PathVariable final String conversationId) {
		return chatHistoryRepository.findByConversationId(conversationId);
	}
}
