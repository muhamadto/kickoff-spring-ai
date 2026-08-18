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
import ai.spring.learning.worldcup.data.TeamData;
import java.util.List;
import org.springframework.ai.mcp.annotation.McpComplete;
import org.springframework.stereotype.Component;

/**
 * Completions: autocomplete for prompt arguments and resource URI template variables. They shine on small, finite, known sets, which a tournament
 * hands you for free.
 */
@Component
public class TournamentCompletions {

	private final TeamData teams;
	private final FixtureData fixtures;

	public TournamentCompletions(final TeamData teams, final FixtureData fixtures) {
		this.teams = teams;
		this.fixtures = fixtures;
	}

	private static List<String> startingWith(final List<String> candidates, final String prefix) {
		final String p = prefix == null ? "" : prefix.toLowerCase().trim();
		return candidates.stream().filter(c -> c.toLowerCase().startsWith(p)).limit(10).toList();
	}

	@McpComplete(prompt = "match-recap")
	public List<String> completeRecapFixture(final String prefix) {
		return startingWith(fixtures.fixtureNames(), prefix);
	}

	@McpComplete(uri = "worldcup://teams/{team}/squad")
	public List<String> completeSquadTeam(final String prefix) {
		return startingWith(teams.teamsIn(""), prefix);
	}
}
