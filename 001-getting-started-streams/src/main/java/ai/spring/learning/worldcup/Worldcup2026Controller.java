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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class Worldcup2026Controller {

	private final ChatClient chatClient;

	public Worldcup2026Controller(final ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}

	/**
	 * The same endpoint as 001-getting-started, but delivered as a token stream instead of a blocking response.
	 *
	 * @return 200 OK
	 */
	@GetMapping(value = "/matches", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> getMatches() {
		return chatClient.prompt().user("""
				Give me a general information about Fifa world cup 2026, including list of the full 26-man squads for every team participating in the
				tournament. One player per line formatted like
				'Player Name — Country'
				
				Also give me the latest news about the tournament and any controversial events.
				""").stream().content();
	}
}
