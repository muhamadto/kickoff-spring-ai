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

import ai.spring.learning.worldcup.advisor.PiiRedactionAdvisor;
import ai.spring.learning.worldcup.tool.FixtureTool;
import ai.spring.learning.worldcup.tool.GroupStandingsTool;
import ai.spring.learning.worldcup.tool.LeaderboardTool;
import ai.spring.learning.worldcup.tool.MatchTool;
import ai.spring.learning.worldcup.tool.PlayerTool;
import ai.spring.learning.worldcup.tool.RefereeTool;
import ai.spring.learning.worldcup.tool.TeamJourneyTool;
import ai.spring.learning.worldcup.tool.TeamTool;
import ai.spring.learning.worldcup.tool.TournamentTool;
import ai.spring.learning.worldcup.tool.VenueTool;
import java.util.List;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAiConfig {

	private static ChatClient.Builder enrichChatClientBuilder(final ChatClient.Builder builder,
			final PiiRedactionAdvisor piiRedactionAdvisor) {

		final Advisor[] advisors = {piiRedactionAdvisor, new SimpleLoggerAdvisor(30)};

		return builder.defaultSystem("""
						You are a helpful and enthusiastic AI assistant specialized in the FIFA World Cup 2026,
						hosted across the USA, Canada and Mexico. Provide accurate, engaging advice for fans
						following the tournament while remaining professional.
						""")
				.defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
				.defaultAdvisors(advisors);
	}

	private static Builder getGeminiChatOptions(final String model) {
		return GoogleGenAiChatOptions.builder().model(model);
	}

	@Bean
	public PiiRedactionAdvisor piiRedactionAdvisor() {
		return new PiiRedactionAdvisor(10);
	}

	@Bean
	public List<Object> worldCupTools(final VenueTool venueTool,
			final FixtureTool fixtureTool,
			final MatchTool matchTool,
			final TeamTool teamTool,
			final GroupStandingsTool groupStandingsTool,
			final RefereeTool refereeTool,
			final TeamJourneyTool teamJourneyTool,
			final TournamentTool tournamentTool,
			final PlayerTool playerTool,
			final LeaderboardTool leaderboardTool) {
		return List
				.of(venueTool,
						fixtureTool,
						matchTool,
						teamTool,
						groupStandingsTool,
						refereeTool,
						teamJourneyTool,
						tournamentTool,
						playerTool,
						leaderboardTool);
	}

	@Bean
	public ChatClient geminiToolsAwareChatClient(final ChatClient geminiToolsNotAwareChatClient,
			final List<Object> worldCupTools) {
		return geminiToolsNotAwareChatClient.mutate()
				.defaultTools(worldCupTools)
				.build();
	}

	@Bean
	public ChatClient geminiToolsNotAwareChatClient(final ChatClient.Builder builder,
			@Value("${spring.ai.google.genai.chat.model}") final String model,
			final PiiRedactionAdvisor piiRedactionAdvisor) {
		return enrichChatClientBuilder(builder, piiRedactionAdvisor)
				.defaultOptions(getGeminiChatOptions(model))
				.build();
	}
}
