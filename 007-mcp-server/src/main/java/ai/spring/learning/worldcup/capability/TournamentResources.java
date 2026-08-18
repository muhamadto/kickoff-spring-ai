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
import ai.spring.learning.worldcup.data.PlayerData;
import ai.spring.learning.worldcup.data.StandingsData;
import ai.spring.learning.worldcup.data.VenueData;
import ai.spring.learning.worldcup.model.Player;
import ai.spring.learning.worldcup.model.TeamStanding;
import ai.spring.learning.worldcup.model.Venue;
import java.util.stream.Collectors;
import org.springframework.ai.mcp.annotation.McpResource;
import org.springframework.stereotype.Component;

/**
 * Resources: the user (or client application) decides when to attach these as context. Same dataset as the tools, opposite control model.
 */
@Component
public class TournamentResources {

	private final FixtureData fixtures;
	private final PlayerData players;
	private final VenueData venues;
	private final StandingsData standings;

	public TournamentResources(final FixtureData fixtures, final PlayerData players, final VenueData venues, final StandingsData standings) {
		this.fixtures = fixtures;
		this.players = players;
		this.venues = venues;
		this.standings = standings;
	}

	private static String describe(final Venue v) {
		return "%s, %s, %s (capacity %,d)".formatted(v.name(), v.city(), v.country(), v.capacity());
	}

	private static String describe(final TeamStanding s) {
		return "%s  P%d W%d D%d L%d  GD%+d  Pts%d".formatted(s.team(), s.played(), s.won(), s.drawn(), s.lost(), s.goalDifference(), s.points());
	}

	@McpResource(uri = "worldcup://fixtures/{date}", name = "fixtures-by-date",
			description = "The World Cup 2026 fixtures for a date, e.g., worldcup://fixtures/15 June 2026", mimeType = "text/plain")
	public String fixturesByDate(final String date) {
		final var matches = fixtures.fixturesOf(date, "");
		if (matches.isEmpty()) {
			return "No World Cup 2026 fixtures on " + date + ".";
		}
		return matches.stream().map(f -> "%s: %s at %s".formatted(f.stage(), f.fixture(), f.venue())).collect(Collectors.joining("\n"));
	}

	@McpResource(uri = "worldcup://teams/{team}/squad", name = "team-squad",
			description = "The full squad of a World Cup 2026 team, e.g., worldcup://teams/Morocco/squad", mimeType = "text/plain")
	public String teamSquad(final String team) {
		final var squad = players.playersOf(team);
		if (squad.isEmpty()) {
			return "No squad information for " + team + ".";
		}
		return team + " squad:\n" + squad.stream().map(Player::name).collect(Collectors.joining("\n"));
	}

	@McpResource(uri = "worldcup://stadiums", name = "stadiums", description = "The World Cup 2026 stadium directory", mimeType = "text/plain")
	public String stadiums() {
		return venues.venues().stream().map(TournamentResources::describe).collect(Collectors.joining("\n"));
	}

	@McpResource(uri = "worldcup://groups/{group}/standings", name = "group-standings",
			description = "Final group-stage standings for a group, e.g., worldcup://groups/B/standings", mimeType = "text/plain")
	public String groupStandings(final String group) {
		final var table = standings.standingsOf(group);
		if (table.isEmpty()) {
			return "No standings recorded for group " + group + ".";
		}
		return "Group " + group.toUpperCase() + ":\n" + table.stream().map(TournamentResources::describe).collect(Collectors.joining("\n"));
	}
}
