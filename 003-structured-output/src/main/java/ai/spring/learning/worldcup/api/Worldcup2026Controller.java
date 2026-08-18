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

import ai.spring.learning.worldcup.GenAiConfig;
import ai.spring.learning.worldcup.converter.WorldCup2026BeanOutputConverter;
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
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.EntityParamSpec;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Worldcup2026Controller {

	private final ChatClient nativeStructuredOutputEnabledGeminiChatClient;
	private final ChatClient nativeStructuredOutputDisabledGeminiChatClient;

	/**
	 * Demonstrates usage of multiple chat clients.
	 */
	public Worldcup2026Controller(final ChatClient nativeStructuredOutputEnabledGeminiChatClient,
			final ChatClient nativeStructuredOutputDisabledGeminiChatClient) {
		this.nativeStructuredOutputEnabledGeminiChatClient = nativeStructuredOutputEnabledGeminiChatClient;
		this.nativeStructuredOutputDisabledGeminiChatClient = nativeStructuredOutputDisabledGeminiChatClient;
	}

	/**
	 * Demonstrates entity(ParameterizedTypeReference) on a plain scalar list, not a record collection.
	 *
	 * @return 200 OK
	 */
	@GetMapping("/teams")
	public List<String> getTeams(final String group) {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt()
				.user(u -> u.text("List the teams competing in Group {group} of the World Cup 2026.").param("group", group)).call()
				.entity(new ParameterizedTypeReference<>() {
				});
	}

	/**
	 * Demonstrates entity(ParameterizedTypeReference) with native structured output (per-call, via useProviderStructuredOutput on this client only): a
	 * collection, constrained by the provider's own schema instead of appended instructions.
	 *
	 * @return 200 OK
	 */
	@GetMapping("/venues")
	public List<Venue> getVenues(final String city) {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt()
				.user(u -> u.text("What are the details of the World Cup 2026 venue in {city}? Include the host city and country.").param("city", city))
				.call().entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::useProviderStructuredOutput);
	}

	/**
	 * Demonstrates responseEntity(ParameterizedTypeReference): the raw {@link ChatResponse} plus a collection.
	 *
	 * @return 200 OK
	 */
	@GetMapping("/fixtures")
	public ResponseEntity<ChatResponse, List<Match>> getFixtures(final String team, final String stage) {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt()
				.user(u -> u.text("Give me {team}'s {stage} fixtures for the World Cup 2026.").param("team", team).param("stage", stage)).call()
				.responseEntity(new ParameterizedTypeReference<>() {
				});
	}

	/**
	 * Demonstrates responseEntity(Class): the mapped entity plus the raw {@link ChatResponse} (model, token usage, finish reason) that entity() throws
	 * away. Runs on a client without the global native-structured-output default (see {@code /groups/standings} below), so the metadata here reflects a
	 * plain format-instruction call.
	 *
	 * @return 200 OK
	 */
	@GetMapping("/featured-match")
	public ResponseEntity<ChatResponse, Match> getFeaturedMatch(final String stage) {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt()
				.user(u -> u.text("What is the must-watch World Cup 2026 match of the {stage}?").param("stage", stage)).call().responseEntity(Match.class);
	}

	/**
	 * Demonstrates entity(Class) plus schema validation (up to 3 retries after the initial call, 4 attempts total): the baseline single-record
	 * technique, with client-side validation and retries layered on top for the shape most likely to come back malformed.
	 *
	 * @return 200 OK
	 */
	@GetMapping("/players")
	public Player getPlayer(final String name) {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt().user(
				u -> u.text("What are {name}'s stats (goals, assists, cards and Man of the Match awards) during the World Cup 2026 group stage?")
						.param("name", name)).call().entity(Player.class, EntityParamSpec::validateSchema);
	}

	/**
	 * Demonstrates entity(Class) with a custom converter. The custome converter lacks the formating instructions.
	 *
	 * @return 500 Internal Server Error
	 */
	@GetMapping("/matches/result")
	public MatchResult getMatchResult(final String fixture) {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt()
				.user(u -> u.text("What was the final result of the World Cup 2026 fixture {fixture}?").param("fixture", fixture))
//        .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT) // remove comment to resolve the 500 http error code
				.call().entity(new WorldCup2026BeanOutputConverter<>(MatchResult.class));
	}

	/**
	 * Demonstrates entity(Class) with schema validation AND a custom converter together. Runs on the chat client, which carries global native
	 * structured output by default (see {@link GenAiConfig})
	 *
	 * @return 200 OK
	 */
	@GetMapping("/matches/stats")
	public MatchStats getMatchStats(final String fixture) {
		return nativeStructuredOutputEnabledGeminiChatClient.prompt()
				.user(
						u -> u.text("Give me the statistics (Man of the Match, cards, shots and shots on target) for the World Cup 2026 fixture {fixture}.")
								.param("fixture", fixture))
				.call()
				.entity(new WorldCup2026BeanOutputConverter<>(MatchStats.class), EntityParamSpec::validateSchema);
	}

	/**
	 * Demonstrates entity(ParameterizedTypeReference) with schema validation: the technique from {@code /players} applied to a collection instead of a
	 * single record.
	 *
	 * @return 200 OK
	 */
	@GetMapping("/referees")
	public List<Referee> getReferees() {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt()
				.user("List the head referees appointed for the World Cup 2026, with their countries.")
				.call()
				.entity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::validateSchema);
	}

	/**
	 * Demonstrates entity(Class) with schema validation failures
	 *
	 * @return 500 Internal Server Error
	 */
	@GetMapping("/referee")
	public Referee getReferee(final String name) {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt()
				.user(u -> u.text("What country is {name}, a head referee appointed for the World Cup 2026, from?").param("name", name)).call()
				.entity(new WorldCup2026BeanOutputConverter<>(Referee.class), EntityParamSpec::validateSchema);
	}

	/**
	 * Demonstrates responseEntity(ParameterizedTypeReference) with a custom converter and schema validation
	 *
	 * @return 200 OK
	 */
	@GetMapping("/groups/standings")
	public ResponseEntity<ChatResponse, List<TeamStanding>> getGroupStandings(final String group) {
		return nativeStructuredOutputEnabledGeminiChatClient.prompt().user(
				u -> u.text("What is the final group-stage standings table for World Cup 2026 Group {group}, ranked by points then goal difference?")
						.param("group", group)).call().responseEntity(new WorldCup2026BeanOutputConverter<>(new ParameterizedTypeReference<>() {
		}), EntityParamSpec::validateSchema);
	}

	/**
	 * Demonstrates responseEntity(ParameterizedTypeReference) with schema validation: ordering is part of this record list's contract, and validation
	 * catches a chronologically scrambled response before it reaches the caller.
	 *
	 * @return 200 OK
	 */
	@GetMapping("/teams/journey")
	public ResponseEntity<ChatResponse, List<TeamJourneyMatch>> getTeamJourney(final String team) {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt().user(u -> u.text(
						"Describe {team}'s World Cup 2026 journey so far: every fixture played, in chronological order, with results and Man of the Match.")
				.param("team", team)).call().responseEntity(new ParameterizedTypeReference<>() {
		}, EntityParamSpec::validateSchema);
	}

	/**
	 * Demonstrates entity(ParameterizedTypeReference) with a custom converter, standalone: no global native structured output involved here (contrast
	 * with {@code /groups/standings} above) — ranking order is simply an implicit contract format instructions communicate poorly. Enable commented out
	 * code to resolve the 500 http error code.
	 *
	 * @return 500 Internal Server Error
	 */
	@GetMapping("/players/leaderboard")
	public List<Player> getLeaderboard(final String category) {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt()
				.user(u -> u.text("List the World Cup 2026 top {category} leaders so far, ranked from highest to lowest.").param("category", category)).call()
				.entity(new WorldCup2026BeanOutputConverter<>(new ParameterizedTypeReference<>() {
				}) /*, EntityParamSpec::useProviderStructuredOutput*/);// remove comment to resolve the 500 http error code
	}

	/**
	 * Demonstrates responseEntity(ParameterizedTypeReference) with native structured output (per-call, via useProviderStructuredOutput on this client
	 * only, same as /venues above): metadata alongside a provider-constrained collection, useful for spotting when the model truncates a longer story
	 * list.
	 *
	 * @return 200 OK
	 */
	@GetMapping("/news")
	public ResponseEntity<ChatResponse, List<TeamNews>> getNews(final String team) {
		return nativeStructuredOutputDisabledGeminiChatClient.prompt()
				.user(u -> u.text("What is the latest real news and notable incidents about {team} at the World Cup 2026?").param("team", team)).call()
				.responseEntity(new ParameterizedTypeReference<>() {
				}, EntityParamSpec::useProviderStructuredOutput);
	}
}
