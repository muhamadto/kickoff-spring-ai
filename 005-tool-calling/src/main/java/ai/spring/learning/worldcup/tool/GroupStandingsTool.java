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

import ai.spring.learning.worldcup.model.TeamStanding;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * Real 2026 FIFA World Cup group-stage standings, computed from verified results.
 */
@Component
public class GroupStandingsTool {

	private static final Map<String, List<TeamStanding>> GROUP_STANDINGS = new LinkedHashMap<>();

	static {
		GROUP_STANDINGS.put("A", List.of(new TeamStanding("Mexico", 3, 3, 0, 0, 6, 0, 6, 9), new TeamStanding("South Africa", 3, 1, 1, 1, 2, 3, -1, 4),
				new TeamStanding("South Korea", 3, 1, 0, 2, 2, 3, -1, 3), new TeamStanding("Czech Republic", 3, 0, 1, 2, 2, 6, -4, 1)));
		GROUP_STANDINGS.put("B", List.of(new TeamStanding("Switzerland", 3, 2, 1, 0, 7, 3, 4, 7), new TeamStanding("Canada", 3, 1, 1, 1, 8, 3, 5, 4),
				new TeamStanding("Bosnia and Herzegovina", 3, 1, 1, 1, 5, 6, -1, 4), new TeamStanding("Qatar", 3, 0, 1, 2, 2, 10, -8, 1)));
		GROUP_STANDINGS.put("C", List.of(new TeamStanding("Brazil", 3, 2, 1, 0, 7, 1, 6, 7), new TeamStanding("Morocco", 3, 2, 1, 0, 6, 3, 3, 7),
				new TeamStanding("Scotland", 3, 1, 0, 2, 1, 4, -3, 3), new TeamStanding("Haiti", 3, 0, 0, 3, 2, 8, -6, 0)));
		GROUP_STANDINGS.put("D", List.of(new TeamStanding("United States", 3, 2, 0, 1, 8, 4, 4, 6), new TeamStanding("Australia", 3, 1, 1, 1, 2, 2, 0, 4),
				new TeamStanding("Paraguay", 3, 1, 1, 1, 2, 4, -2, 4), new TeamStanding("Türkiye", 3, 1, 0, 2, 3, 5, -2, 3)));
		GROUP_STANDINGS.put("E", List.of(new TeamStanding("Germany", 3, 2, 0, 1, 10, 4, 6, 6), new TeamStanding("Ivory Coast", 3, 2, 0, 1, 4, 2, 2, 6),
				new TeamStanding("Ecuador", 3, 1, 1, 1, 2, 2, 0, 4), new TeamStanding("Curaçao", 3, 0, 1, 2, 1, 9, -8, 1)));
		GROUP_STANDINGS.put("F", List.of(new TeamStanding("Japan", 3, 1, 2, 0, 7, 3, 4, 5), new TeamStanding("Netherlands", 3, 1, 1, 1, 8, 6, 2, 4),
				new TeamStanding("Sweden", 3, 1, 1, 1, 7, 7, 0, 4), new TeamStanding("Tunisia", 3, 1, 0, 2, 4, 10, -6, 3)));
		GROUP_STANDINGS.put("G", List.of(new TeamStanding("Belgium", 3, 1, 2, 0, 6, 2, 4, 5), new TeamStanding("Egypt", 3, 1, 2, 0, 5, 3, 2, 5),
				new TeamStanding("Iran", 3, 0, 3, 0, 3, 3, 0, 3), new TeamStanding("New Zealand", 3, 0, 1, 2, 4, 10, -6, 1)));
		GROUP_STANDINGS.put("H", List.of(new TeamStanding("Spain", 3, 2, 1, 0, 5, 0, 5, 7), new TeamStanding("Cape Verde", 3, 0, 3, 0, 2, 2, 0, 3),
				new TeamStanding("Uruguay", 3, 0, 2, 1, 3, 4, -1, 2), new TeamStanding("Saudi Arabia", 3, 0, 2, 1, 1, 5, -4, 2)));
		GROUP_STANDINGS.put("I", List.of(new TeamStanding("France", 3, 3, 0, 0, 10, 2, 8, 9), new TeamStanding("Norway", 3, 2, 0, 1, 8, 7, 1, 6),
				new TeamStanding("Senegal", 3, 1, 0, 2, 8, 6, 2, 3), new TeamStanding("Iraq", 3, 0, 0, 3, 1, 12, -11, 0)));
		GROUP_STANDINGS.put("J", List.of(new TeamStanding("Argentina", 3, 3, 0, 0, 8, 1, 7, 9), new TeamStanding("Austria", 3, 1, 1, 1, 6, 6, 0, 4),
				new TeamStanding("Algeria", 3, 1, 1, 1, 5, 7, -2, 4), new TeamStanding("Jordan", 3, 0, 0, 3, 3, 8, -5, 0)));
		GROUP_STANDINGS.put("K", List.of(new TeamStanding("Colombia", 3, 2, 1, 0, 4, 1, 3, 7), new TeamStanding("Portugal", 3, 1, 2, 0, 6, 1, 5, 5),
				new TeamStanding("DR Congo", 3, 1, 1, 1, 4, 3, 1, 4), new TeamStanding("Uzbekistan", 3, 0, 0, 3, 2, 11, -9, 0)));
		GROUP_STANDINGS.put("L", List.of(new TeamStanding("England", 3, 2, 1, 0, 6, 2, 4, 7), new TeamStanding("Croatia", 3, 2, 0, 1, 5, 5, 0, 6),
				new TeamStanding("Ghana", 3, 1, 1, 1, 2, 2, 0, 4), new TeamStanding("Panama", 3, 0, 0, 3, 0, 4, -4, 0)));
	}

	@Tool(name = "get-group-standings",
			description = "Get the final group-stage standings table for a World Cup 2026 group, ranked by points then goal difference")
	public List<TeamStanding> getGroupStandings(@ToolParam(description = "The group letter, e.g., 'A' or 'L'", required = true) String group) {
		return GROUP_STANDINGS.getOrDefault(group.toUpperCase().trim(), List.of());
	}
}
