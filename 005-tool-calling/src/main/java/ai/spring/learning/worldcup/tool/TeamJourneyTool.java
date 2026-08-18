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

package ai.spring.learning.worldcup.tool;

import ai.spring.learning.worldcup.model.TeamJourneyMatch;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * Composes {@link FixtureTool} and {@link MatchTool} rather than holding its own data, so a team's journey stays in sync with the fixture, result and
 * stats tables. Knockout-stage fixtures are not sourced yet, so the journey currently ends at each team's last group-stage match.
 */
@Component
public class TeamJourneyTool {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

	private final FixtureTool fixtureTool;
	private final MatchTool matchTool;

	public TeamJourneyTool(final FixtureTool fixtureTool, final MatchTool matchTool) {
		this.fixtureTool = fixtureTool;
		this.matchTool = matchTool;
	}

	@Tool(name = "get-team-journey",
			description = "Get a team's World Cup 2026 journey: every fixture played so far, in chronological order, with results and Man of the Match. The list ends at the team's last recorded match, whether that's the end of the group stage or an eventual elimination.")
	public List<TeamJourneyMatch> getTeamJourney(@ToolParam(description = "The team name, e.g., 'Morocco'", required = true) String team) {
		return fixtureTool
				.getFixtures("", team)
				.stream()
				.sorted(Comparator.comparing(f -> LocalDate.parse(f.date(), DATE_FORMAT)))
				.map(f -> new TeamJourneyMatch(
						f.stage(),
						f.fixture(),
						f.date(),
						f.venue(),
						matchTool.getMatchResult(f.fixture()),
						matchTool.getMatchStats(f.fixture()).manOfTheMatch()))
				.toList();
	}
}
