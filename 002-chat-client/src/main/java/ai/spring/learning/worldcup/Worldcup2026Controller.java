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

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Worldcup2026Controller {

	private final ChatClient geminiProChatClient;
	private final ChatClient geminiFlashChatClient;
	private final ChatClient geminiFlashLiteChatClient;

	/**
	 * Demonstrates usage of multiple {@link ChatClient}.
	 */
	public Worldcup2026Controller(final ChatClient geminiProChatClient,
			final ChatClient geminiFlashChatClient,
			final ChatClient geminiFlashLiteChatClient) {
		this.geminiProChatClient = geminiProChatClient;
		this.geminiFlashChatClient = geminiFlashChatClient;
		this.geminiFlashLiteChatClient = geminiFlashLiteChatClient;
	}

	@GetMapping("/tournament")
	public String getTournament() {
		return geminiFlashChatClient.prompt()
				.user("""
						Give me a general information about Fifa world cup 2026, including list of the full 26-man squads for every team participating in the
						tournament. One player per line formatted like
						'Player Name — Country'
						
						Also give me the latest news about the tournament and any controversial events.
						""")
				.call()
				.content();
	}

	@GetMapping("/matches")
	public String getMatches(final String date, final String stage) {
		if (stage != null && !stage.isBlank()) {
			return geminiProChatClient
					.prompt()
					.user(u -> u.text("What World Cup 2026 matches are on during the {stage}?").param("stage", stage).metadata("messageId", "msg-123"))
					.call()
					.content();
		}
		return geminiProChatClient
				.prompt()
				.user(u -> u.text("What are the {date} World Cup 2026 matches for the afternoon, evening and night kickoffs?").param("date", date))
				.call()
				.content();
	}

	/**
	 * Demonstrates {@link ChatClient} variant and per-prompt options overriding the client defaults. The yaml pins temperature to 0.0 for deterministic
	 * answers, but storytelling wants creativity, so this prompt raises it.
	 *
	 * @param team
	 * @return 200 OK
	 */
	@GetMapping("/story")
	public String getStory(final String team) {
		return geminiFlashLiteChatClient
				.prompt()
				.options(GoogleGenAiChatOptions.builder().temperature(0.6))
				.user(u -> u.text("Tell me a short story about {team} at the World Cup 2026.").param("team", team))
				.call()
				.content();
	}
}
