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

package ai.spring.learning.worldcup.capability;

import ai.spring.learning.worldcup.data.FixtureData;
import ai.spring.learning.worldcup.data.MatchData;
import ai.spring.learning.worldcup.data.NewsData;
import ai.spring.learning.worldcup.data.PlayerData;
import ai.spring.learning.worldcup.data.RefereeData;
import ai.spring.learning.worldcup.data.StandingsData;
import ai.spring.learning.worldcup.data.TeamData;
import ai.spring.learning.worldcup.data.VenueData;
import ai.spring.learning.worldcup.model.Controversy;
import ai.spring.learning.worldcup.model.Fixture;
import ai.spring.learning.worldcup.model.MatchStats;
import ai.spring.learning.worldcup.model.Player;
import ai.spring.learning.worldcup.model.Referee;
import ai.spring.learning.worldcup.model.TeamJourneyMatch;
import ai.spring.learning.worldcup.model.TeamNews;
import ai.spring.learning.worldcup.model.TeamStanding;
import ai.spring.learning.worldcup.model.Venue;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

/**
 * Tools: the model advises when to call these. This is the full 005-tool-calling tool set, moved here rather than duplicated in 007-mcp-client, so
 * "where did this data come from" has exactly one answer for the whole module: MCP. Same real, verified data as 006, same tool names.
 */
@Component
public class TournamentTools {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

	private final TeamData teams;
	private final VenueData venues;
	private final FixtureData fixtures;
	private final MatchData matches;
	private final RefereeData referees;
	private final StandingsData standings;
	private final PlayerData players;
	private final NewsData news;

	public TournamentTools(final TeamData teams,
			final VenueData venues,
			final FixtureData fixtures,
			final MatchData matches,
			final RefereeData referees,
			final StandingsData standings,
			final PlayerData players,
			final NewsData news) {
		this.teams = teams;
		this.venues = venues;
		this.fixtures = fixtures;
		this.matches = matches;
		this.referees = referees;
		this.standings = standings;
		this.players = players;
		this.news = news;
	}

	@McpTool(name = "get-teams", description = "Get the list of teams competing in the World Cup 2026, optionally filtered by group")
	public List<String> getTeams(
			@McpToolParam(description = "An optional group letter filter, e.g., 'A'. Pass empty string for all 48 teams.") final String group) {
		return teams.teamsIn(group);
	}

	@McpTool(name = "get-venue", description = "Get venue details for a host city of the World Cup 2026")
	public Venue getVenue(
			@McpToolParam(description = "The host city name, e.g., 'Dallas', 'Mexico City', 'Toronto'", required = true) final String city) {
		return venues.venueIn(city);
	}

	@McpTool(name = "get-fixtures", description = "Get the list of World Cup 2026 group-stage fixtures for a specific date or team")
	public List<Fixture> getFixtures(
			@McpToolParam(description = "An optional date filter, e.g., '15 June 2026'. Pass empty string for all fixtures.") final String date,
			@McpToolParam(description = "An optional team filter, e.g., 'Morocco'. Pass empty string for all teams.") final String team) {
		return fixtures.fixturesOf(date, team);
	}

	@McpTool(name = "get-match-result", description = "Get the final result of a played World Cup 2026 match")
	public String getMatchResult(
			@McpToolParam(description = "The fixture as 'Home vs Away', e.g., 'Morocco vs Haiti'", required = true) final String fixture) {
		return matches.resultOf(fixture);
	}

	@McpTool(name = "get-match-stats", description = "Get statistics (Man of the Match, cards, shots) for a played World Cup 2026 match")
	public MatchStats getMatchStats(
			@McpToolParam(description = "The fixture as 'Home vs Away', e.g., 'Morocco vs Haiti'", required = true) final String fixture) {
		return matches.statsOf(fixture);
	}

	@McpTool(name = "get-referees", description = "Get the list of match officials (head referees) appointed for the World Cup 2026")
	public List<Referee> getReferees() {
		return referees.referees();
	}

	@McpTool(name = "get-group-standings",
			description = "Get the final group-stage standings table for a World Cup 2026 group, ranked by points then goal difference")
	public List<TeamStanding> getGroupStandings(@McpToolParam(description = "The group letter, e.g., 'A' or 'L'", required = true) final String group) {
		return standings.standingsOf(group);
	}

	@McpTool(name = "get-team-journey",
			description = """
					Get a team's World Cup 2026 journey: every fixture played so far, in chronological order, with results and Man of the Match.
					The list ends at the team's last recorded match, whether that's the end of the group stage or an eventual elimination.
					""")
	public List<TeamJourneyMatch> getTeamJourney(@McpToolParam(description = "The team name, e.g., 'Morocco'", required = true) final String team) {
		return fixtures
				.fixturesOf("", team)
				.stream()
				.sorted(Comparator.comparing(f -> LocalDate.parse(f.date(), DATE_FORMAT)))
				.map(f -> new TeamJourneyMatch(
						f.stage(),
						f.fixture(),
						f.date(),
						f.venue(),
						matches.resultOf(f.fixture()),
						matches.statsOf(f.fixture()).manOfTheMatch()))
				.toList();
	}

	@McpTool(name = "get-player",
			description = "Get a World Cup 2026 player's stats: goals, assists, cards and Man of the Match awards during the group stage")
	public Player getPlayer(@McpToolParam(description = "The player's full name, e.g., 'Kylian Mbappé'", required = true) final String name) {
		return players.getPlayer(name);
	}

	@McpTool(name = "get-goal-leaderboard",
			description = "Get the World Cup 2026 top goal scorers so far, ranked from highest to lowest. Empty until verified goal data is sourced.")
	public List<Player> getGoalLeaderboard() {
		return players
				.getAllPlayers()
				.stream()
				.filter(p -> p.goals() != null && p.goals() > 0)
				.sorted(Comparator.comparingInt(Player::goals).reversed())
				.toList();
	}

	@McpTool(name = "get-assist-leaderboard",
			description = "Get the World Cup 2026 top assist providers so far, ranked from highest to lowest. Empty until verified assist data is sourced.")
	public List<Player> getAssistLeaderboard() {
		return players
				.getAllPlayers()
				.stream()
				.filter(p -> p.assists() != null && p.assists() > 0)
				.sorted(Comparator.comparingInt(Player::assists).reversed())
				.toList();
	}

	@McpTool(name = "get-news", description = "Get real news stories and notable incidents about a World Cup 2026 team, optionally filtered by team")
	public List<TeamNews> getNews(
			@McpToolParam(description = "An optional team filter, e.g., 'Iran'. Pass empty string for all stories.") final String team) {
		return news.newsOf(team);
	}

	@McpTool(name = "get-controversies",
			description = "Get real controversial incidents from the World Cup 2026 group stage, optionally filtered by team")
	public List<Controversy> getControversies(
			@McpToolParam(description = "An optional team filter, e.g., 'Brazil'. Pass empty string for all controversies.") final String team) {
		return news.controversiesOf(team);
	}
}
