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

package ai.spring.learning.worldcup.service;

import ai.spring.learning.worldcup.repository.ChatHistoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

/**
 * {@code chat(...)} records every request and answer to {@code chatHistoryRepository}, then returns it: a durable audit log, separate from
 * {@code MessageChatMemoryAdvisor}'s windowed memory, which shapes what the model sees on the next call but is never read back for anything else.
 */
@Service
public class Worldcup2026Service {

	private final ChatClient geminiMemoryAwareChatClient;
	private final ChatHistoryRepository chatHistoryRepository;

	public Worldcup2026Service(final ChatClient geminiMemoryAwareChatClient, final ChatHistoryRepository chatHistoryRepository) {
		this.geminiMemoryAwareChatClient = geminiMemoryAwareChatClient;
		this.chatHistoryRepository = chatHistoryRepository;
	}

	public String chat(final String message, final String conversationId) {
		final String answer = respond(message, conversationId);
		chatHistoryRepository.record(conversationId, "user", message);
		chatHistoryRepository.record(conversationId, "assistant", answer);
		return answer;
	}

	private String respond(final String message, final String conversationId) {
		return geminiMemoryAwareChatClient
				.prompt()
				.user(message)
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
				.call()
				.content();
	}
}
