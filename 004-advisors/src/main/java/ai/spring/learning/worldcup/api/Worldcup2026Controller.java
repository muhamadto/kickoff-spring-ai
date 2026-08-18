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

import ai.spring.learning.worldcup.advisor.PiiRedactionAdvisor;
import ai.spring.learning.worldcup.model.Player;
import ai.spring.learning.worldcup.model.TeamNews;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.EntityParamSpec;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Worldcup2026Controller {

	private final ChatClient geminiPiiRedactionAwareChatClient;
	private final ChatClient geminiPiiRedactionNotAwareChatClient;

	private final PiiRedactionAdvisor piiRedactionAdvisor;

	/**
	 * Demonstrates usage of multiple chat clients and an advisor
	 */
	public Worldcup2026Controller(final ChatClient geminiPiiRedactionAwareChatClient,
			final ChatClient geminiPiiRedactionNotAwareChatClient,
			final PiiRedactionAdvisor piiRedactionAdvisor) {
		this.geminiPiiRedactionAwareChatClient = geminiPiiRedactionAwareChatClient;
		this.geminiPiiRedactionNotAwareChatClient = geminiPiiRedactionNotAwareChatClient;

		this.piiRedactionAdvisor = piiRedactionAdvisor;
	}

	/**
	 * Demonstrate per call advisor usage
	 *
	 * @param name the player's full name
	 * @return 200 OK
	 */
	@GetMapping("/players")
	public Player getPlayer(final String name) {
		return geminiPiiRedactionNotAwareChatClient
				.prompt()
				.user(u -> u
						.text("What are {name}'s stats (goals, assists, cards and Man of the Match awards) during the World Cup 2026 group stage?")
						.param("name", name))
				.advisors(piiRedactionAdvisor)
				.call()
				.entity(Player.class, EntityParamSpec::validateSchema);
	}


	/**
	 * Demonstrate default advisor usage
	 *
	 * @param team the team name
	 * @return 200 OK
	 */
	@GetMapping("/news")
	public List<TeamNews> getNews(final String team) {
		return geminiPiiRedactionAwareChatClient
				.prompt()
				.user(u -> u.text("What is the latest real news and notable incidents about {team} at the World Cup 2026?").param("team", team))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				});
	}

}
