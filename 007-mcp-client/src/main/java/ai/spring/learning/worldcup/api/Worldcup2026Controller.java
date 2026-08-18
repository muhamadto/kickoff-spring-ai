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

import ai.spring.learning.worldcup.model.Match;
import ai.spring.learning.worldcup.model.MatchResult;
import ai.spring.learning.worldcup.model.MatchStats;
import ai.spring.learning.worldcup.model.Player;
import ai.spring.learning.worldcup.model.Referee;
import ai.spring.learning.worldcup.model.TeamJourneyMatch;
import ai.spring.learning.worldcup.model.TeamNews;
import ai.spring.learning.worldcup.model.TeamStanding;
import ai.spring.learning.worldcup.model.Venue;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.EntityParamSpec;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Every {@link ChatClient} here is grounded via MCP, with native structured output and schema validation on for every call. Endpoints are grouped by
 * which MCP capability they demonstrate: tools, resources, prompts, then completions.
 */
@RestController
public class Worldcup2026Controller {

	private final ChatClient geminiToolsAwareChatClient;
	private final ChatClient geminiToolsNotAwareChatClient;

	private final List<McpSyncClient> mcpSyncClients;

	private final List<Object> worldCupTools;

	public Worldcup2026Controller(final ChatClient geminiToolsAwareChatClient,
			final ChatClient geminiToolsNotAwareChatClient,
			final ObjectProvider<List<McpSyncClient>> mcpSyncClients,
			final List<Object> worldCupTools) {
		this.geminiToolsAwareChatClient = geminiToolsAwareChatClient;
		this.geminiToolsNotAwareChatClient = geminiToolsNotAwareChatClient;

		this.mcpSyncClients = mcpSyncClients.getIfAvailable(List::of);

		this.worldCupTools = worldCupTools;
	}

	// ==================== Tools: plain prompts, the model decides when to call one ====================

	/**
	 * Tool-calling: {@code get-teams}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/teams")
	public List<String> getTeams(final String group) {
		return geminiToolsNotAwareChatClient
				.prompt()
				.user(u -> u.text("List the teams competing in Group {group} of the World Cup 2026.").param("group", group))
				.tools(this.worldCupTools)
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-venue}. Demonstrates default tool-call usage.
	 */
	@GetMapping("/venues")
	public List<Venue> getVenues(final String city) {
		return geminiToolsAwareChatClient
				.prompt()
				.user(u -> u.text("What are the details of the World Cup 2026 venue in {city}? Include the host city and country.").param("city", city))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-fixtures}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/fixtures")
	public List<Match> getFixtures(final String team, final String stage) {
		return geminiToolsNotAwareChatClient
				.prompt()
				.user(u -> u.text("Give me {team}'s {stage} fixtures for the World Cup 2026.").param("team", team).param("stage", stage))
				.tools(this.worldCupTools)
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling with no matching tool: nothing grounds this one, so the model has to hallucinate. Demonstrates default tool-call usage.
	 */
	@GetMapping("/featured-match")
	public Match getFeaturedMatch(final String stage) {
		return geminiToolsAwareChatClient
				.prompt()
				.user(u -> u.text("What is the must-watch World Cup 2026 match of the {stage}?").param("stage", stage))
				.call()
				.entity(Match.class, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-player}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/players")
	public Player getPlayer(final String name) {
		return geminiToolsNotAwareChatClient
				.prompt()
				.user(u -> u
						.text("What are {name}'s stats (goals, assists, cards and Man of the Match awards) during the World Cup 2026 group stage?")
						.param("name", name))
				.tools(this.worldCupTools)
				.call()
				.entity(Player.class, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-match-result}. Demonstrates default tool-call usage.
	 */
	@GetMapping("/matches/result")
	public MatchResult getMatchResult(final String fixture) {
		return geminiToolsAwareChatClient
				.prompt()
				.user(u -> u.text("What was the final result of the World Cup 2026 fixture {fixture}?").param("fixture", fixture))
				.call()
				.entity(MatchResult.class, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-match-stats}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/matches/stats")
	public MatchStats getMatchStats(final String fixture) {
		return geminiToolsNotAwareChatClient
				.prompt()
				.user(u -> u
						.text("Give me the statistics (Man of the Match, cards, shots and shots on target) for the World Cup 2026 fixture {fixture}.")
						.param("fixture", fixture))
				.tools(this.worldCupTools)
				.call()
				.entity(MatchStats.class, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-referees}. Demonstrates default tool-call usage.
	 */
	@GetMapping("/referees")
	public List<Referee> getReferees() {
		return geminiToolsAwareChatClient
				.prompt()
				.user("List the head referees appointed for the World Cup 2026, with their countries.")
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-group-standings}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/groups/standings")
	public List<TeamStanding> getGroupStandings(final String group) {
		return geminiToolsNotAwareChatClient
				.prompt()
				.user(u -> u
						.text("What is the final group-stage standings table for World Cup 2026 Group {group}, ranked by points then goal difference?")
						.param("group", group))
				.tools(this.worldCupTools)
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-team-journey}. Demonstrates default tool-call usage.
	 */
	@GetMapping("/teams/journey")
	public List<TeamJourneyMatch> getTeamJourney(final String team) {
		return geminiToolsAwareChatClient
				.prompt()
				.user(u -> u
						.text("Describe {team}'s World Cup 2026 journey so far: every fixture played, in chronological order, with results and Man of the Match.")
						.param("team", team))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-goal-leaderboard} / {@code get-assist-leaderboard}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/players/leaderboard")
	public List<Player> getLeaderboard(final String category) {
		return geminiToolsNotAwareChatClient
				.prompt()
				.user(u -> u.text("List the World Cup 2026 top {category} leaders so far, ranked from highest to lowest.").param("category", category))
				.tools(this.worldCupTools)
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-news}. Demonstrates default tool-call usage.
	 */
	@GetMapping("/news")
	public List<TeamNews> getNews(final String team) {
		return geminiToolsAwareChatClient
				.prompt()
				.user(u -> u.text("What is the latest real news and notable incidents about {team} at the World Cup 2026?").param("team", team))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-controversies}. Left as prose — a controversy is a narrative, not a record. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/controversies")
	public String getControversies(final String team) {
		return geminiToolsNotAwareChatClient
				.prompt()
				.user(u -> u.text("What controversial incidents involved {team} during the World Cup 2026 group stage?").param("team", team))
				.tools(this.worldCupTools)
				.call()
				.content();
	}

	// ==================== Resources: the application decides when to fetch these, not the model ====================

	/**
	 * Resource: {@code worldcup://teams/{team}/squad}.
	 */
	@GetMapping("/squad")
	public String getSquad(final String team) {
		final McpSchema.ReadResourceResult resource =
				mcpSyncClients.getFirst().readResource(new McpSchema.ReadResourceRequest("worldcup://teams/" + team + "/squad"));

		final String squad = ((McpSchema.TextResourceContents) resource.contents().getFirst()).text();

		return geminiToolsNotAwareChatClient.prompt().user(u -> u.text("""
				Using only this squad information:
				
				{squad}
				
				Introduce {team}'s key players to a new fan.""").param("squad", squad).param("team", team)).call().content();
	}

	/**
	 * Resource: {@code worldcup://fixtures/{date}}.
	 */
	@GetMapping("/schedule")
	public String getSchedule(final String date) {
		final McpSchema.ReadResourceResult resource =
				mcpSyncClients.getFirst().readResource(new McpSchema.ReadResourceRequest("worldcup://fixtures/" + date));

		final String fixtures = ((McpSchema.TextResourceContents) resource.contents().getFirst()).text();

		return geminiToolsNotAwareChatClient.prompt().user(u -> u.text("""
				Using only this fixture information:
				
				{fixtures}
				
				Summarise the {date} schedule for a fan in a couple of sentences.""").param("fixtures", fixtures).param("date", date)).call().content();
	}

	/**
	 * Resource: {@code worldcup://stadiums}. No URI variables, and returned as-is — a resource doesn't have to go through the model at all.
	 */
	@GetMapping("/stadiums")
	public String getStadiums() {
		final McpSchema.ReadResourceResult resource = mcpSyncClients.getFirst().readResource(new McpSchema.ReadResourceRequest("worldcup://stadiums"));

		return ((McpSchema.TextResourceContents) resource.contents().getFirst()).text();
	}

	/**
	 * Resource: {@code worldcup://groups/{group}/standings}.
	 */
	@GetMapping("/standings")
	public String getStandingsResource(final String group) {
		final McpSchema.ReadResourceResult resource =
				mcpSyncClients.getFirst().readResource(new McpSchema.ReadResourceRequest("worldcup://groups/" + group + "/standings"));

		return ((McpSchema.TextResourceContents) resource.contents().getFirst()).text();
	}

	// ==================== Prompts: templates owned by the server ====================

	/**
	 * Prompt: {@code match-recap}.
	 */
	@GetMapping("/recap")
	public String getRecap(final String fixture) {
		final McpSchema.GetPromptResult prompt =
				mcpSyncClients.getFirst().getPrompt(new McpSchema.GetPromptRequest("match-recap", Map.of("fixture", fixture)));

		final String text = ((McpSchema.TextContent) prompt.messages().getFirst().content()).text();

		return geminiToolsNotAwareChatClient.prompt().user(text).call().content();
	}

	// ==================== Completions: autocomplete over small known sets, no LLM call ====================

	/**
	 * Completion for the {@code match-recap} prompt's {@code fixture} argument.
	 */
	@GetMapping("/autocomplete/fixtures")
	public List<String> autocompleteFixtures(final String prefix) {
		final McpSchema.CompleteResult result = mcpSyncClients
				.getFirst()
				.completeCompletion(new McpSchema.CompleteRequest(
						new McpSchema.PromptReference("match-recap"),
						new McpSchema.CompleteRequest.CompleteArgument("fixture", prefix)));

		return result.completion().values();
	}

	/**
	 * Completion for the {@code worldcup://teams/{team}/squad} resource's {@code team} URI variable.
	 */
	@GetMapping("/autocomplete/squad-teams")
	public List<String> autocompleteSquadTeams(final String prefix) {
		final McpSchema.CompleteResult result = mcpSyncClients
				.getFirst()
				.completeCompletion(new McpSchema.CompleteRequest(
						new McpSchema.ResourceReference("worldcup://teams/{team}/squad"),
						new McpSchema.CompleteRequest.CompleteArgument("team", prefix)));

		return result.completion().values();
	}
}
