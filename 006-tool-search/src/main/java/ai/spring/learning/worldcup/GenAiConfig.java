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
import org.springframework.ai.chat.client.advisor.toolsearch.ToolSearchToolCallingAdvisor;
import org.springframework.ai.chat.client.advisor.toolsearch.autoconfigure.ToolSearchAdvisorProperties;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions.Builder;
import org.springframework.ai.tool.toolsearch.ToolIndex;
import org.springframework.ai.tool.toolsearch.index.lucene.LuceneToolIndex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAiConfig {

	private static ChatClient.Builder enrichChatClientBuilder(final ChatClient.Builder builder,
			final List<Object> worldCupTools,
			final ToolSearchToolCallingAdvisor toolSearchToolCallingAdvisor) {
		return builder
				.defaultSystem("""
						You are a helpful and enthusiastic AI assistant specialized in the FIFA World Cup 2026,
						hosted across the USA, Canada and Mexico. Provide accurate, engaging advice for fans
						following the tournament while remaining professional.
						""")
				.defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
				.defaultAdvisors(new PiiRedactionAdvisor(10), toolSearchToolCallingAdvisor, new SimpleLoggerAdvisor(30))
				.defaultTools(worldCupTools);
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

	/**
	 * Ten {@code @Tool} classes means ten full tool schemas in every request today, regardless of what the fan actually asked.
	 * {@link ToolSearchToolCallingAdvisor} replaces that: the model sees one tool, a search over {@link LuceneToolIndex}'s in-memory keyword index of
	 * the same ten tools, and only pulls in the definitions it decides it actually needs for this question. Order {@code 25}: after
	 * {@link PiiRedactionAdvisor} ({@code 10}) so redacted text is what reaches tool search and tool calls, before {@link SimpleLoggerAdvisor}
	 * ({@code 30}) so the logger still sees the final answer.
	 */
	@Bean
	public ToolSearchToolCallingAdvisor toolSearchToolCallingAdvisor(final ToolSearchAdvisorProperties properties, final ToolIndex toolIndex) {
		int maxResults = properties.getMaxResults() == null ? 5 : properties.getMaxResults();

		return ToolSearchToolCallingAdvisor.builder()
				.toolIndex(toolIndex)
				.advisorOrder(properties.getAdvisorOrder())
				.maxResults(maxResults)
				.conversationHistoryEnabled(true)
				.sessionIdKeyName(properties.getSessionIdKeyName())
				.build();
	}

	@Bean
	public ChatClient geminiSearchableToolsChatClient(final ChatClient.Builder builder,
			@Value("${spring.ai.google.genai.chat.model}") final String model,
			final List<Object> worldCupTools,
			final ToolSearchToolCallingAdvisor toolSearchToolCallingAdvisor) {
		return enrichChatClientBuilder(builder, worldCupTools, toolSearchToolCallingAdvisor)
				.defaultOptions(getGeminiChatOptions(model))
				.build();
	}
}
