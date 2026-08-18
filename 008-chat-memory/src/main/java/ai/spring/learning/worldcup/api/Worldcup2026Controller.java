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

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Worldcup2026Controller {

	private final ChatClient geminiMemoryAwareChatClient;

	public Worldcup2026Controller(final ChatClient geminiMemoryAwareChatClient) {
		this.geminiMemoryAwareChatClient = geminiMemoryAwareChatClient;
	}

	/**
	 * Demonstrates default chat memory storage.
	 *
	 * @param conversationId
	 * @param message
	 */
	@GetMapping("/chat")
	public String chat(final String conversationId, final String message) {
		return geminiMemoryAwareChatClient
				.prompt()
				.user(message)
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
				.call()
				.content();
	}

}
