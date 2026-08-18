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
import java.util.List;
import java.util.UUID;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.EntityParamSpec;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Worldcup2026Controller {

	private final ChatClient geminiSearchableToolsChatClient;

	public Worldcup2026Controller(final ChatClient geminiSearchableToolsChatClient) {
		this.geminiSearchableToolsChatClient = geminiSearchableToolsChatClient;
	}

	/**
	 * Tool-calling: {@code get-teams}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/teams")
	public List<String> getTeams(final String group) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u.text("List the teams competing in Group {group} of the World Cup 2026.").param("group", group))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-venue}. Demonstrates default tool-call usage.
	 */
	@GetMapping("/venues")
	public List<Venue> getVenues(final String city) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u.text("What are the details of the World Cup 2026 venue in {city}? Include the host city and country.").param("city", city))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-fixtures}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/fixtures")
	public List<Match> getFixtures(final String team, final String stage) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u.text("Give me {team}'s {stage} fixtures for the World Cup 2026.").param("team", team).param("stage", stage))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling with no matching tool: nothing grounds this one, so the model has to hallucinate. Demonstrates default tool-call usage.
	 */
	@GetMapping("/featured-match")
	public Match getFeaturedMatch(final String stage) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u.text("What is the must-watch World Cup 2026 match of the {stage}?").param("stage", stage))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(Match.class, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-player}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/players")
	public Player getPlayer(final String name) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u
						.text("What are {name}'s stats (goals, assists, cards and Man of the Match awards) during the World Cup 2026 group stage?")
						.param("name", name))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(Player.class, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-match-result}. Demonstrates default tool-call usage.
	 */
	@GetMapping("/matches/result")
	public MatchResult getMatchResult(final String fixture) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u.text("What was the final result of the World Cup 2026 fixture {fixture}?").param("fixture", fixture))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(MatchResult.class, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-match-stats}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/matches/stats")
	public MatchStats getMatchStats(final String fixture) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u
						.text("Give me the statistics (Man of the Match, cards, shots and shots on target) for the World Cup 2026 fixture {fixture}.")
						.param("fixture", fixture))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(MatchStats.class, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-referees}. Demonstrates default tool-call usage.
	 */
	@GetMapping("/referees")
	public List<Referee> getReferees() {
		return geminiSearchableToolsChatClient
				.prompt()
				.user("List the head referees appointed for the World Cup 2026, with their countries.")
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-group-standings}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/groups/standings")
	public List<TeamStanding> getGroupStandings(final String group) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u
						.text("What is the final group-stage standings table for World Cup 2026 Group {group}, ranked by points then goal difference?")
						.param("group", group))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-team-journey}. Demonstrates default tool-call usage.
	 */
	@GetMapping("/teams/journey")
	public List<TeamJourneyMatch> getTeamJourney(final String team) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u
						.text("Describe {team}'s World Cup 2026 journey so far: every fixture played, in chronological order, with results and Man of the Match.")
						.param("team", team))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-goal-leaderboard} / {@code get-assist-leaderboard}. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/players/leaderboard")
	public List<Player> getLeaderboard(final String category) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u.text("List the World Cup 2026 top {category} leaders so far, ranked from highest to lowest.").param("category", category))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-news}. Demonstrates default tool-call usage.
	 */
	@GetMapping("/news")
	public List<TeamNews> getNews(final String team) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u.text("What is the latest real news and notable incidents about {team} at the World Cup 2026?").param("team", team))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Tool-calling: {@code get-controversies}. Left as prose — a controversy is a narrative, not a record. Demonstrates per-call tool-call usage.
	 */
	@GetMapping("/controversies")
	public String getControversies(final String team) {
		return geminiSearchableToolsChatClient
				.prompt()
				.user(u -> u.text("What controversial incidents involved {team} during the World Cup 2026 group stage?").param("team", team))
				.advisors(a -> a.param("search-tool", UUID.randomUUID().toString()))
				.call()
				.content();
	}
}
