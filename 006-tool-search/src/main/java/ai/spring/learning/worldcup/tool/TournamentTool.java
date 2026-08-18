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

import ai.spring.learning.worldcup.model.Controversy;
import ai.spring.learning.worldcup.model.TeamNews;
import java.util.List;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * Real, verified team news and controversial incidents from the 2026 FIFA World Cup group stage. News is cross-checked against Al Jazeera, NPR, ESPN,
 * FIFA, NBC, Sky Sports and this module's own verified fixtures/results/standings where applicable. Controversies are cross-checked across
 * Wikipedia's "2026 FIFA World Cup officiating controversies" page, Al Jazeera and Sports Illustrated; descriptions are kept to the high-level facts
 * every source agreed on, since sources diverged on some fine detail (exact scorelines, wording of VAR margins).
 */
@Component
public class TournamentTool {

	private static final List<TeamNews> NEWS = List.of(new TeamNews("Iran", "Iran denied overnight stays in the USA between matches",
					"US policy meant Iran's squad could not stay in the country between fixtures: they arrived only around 24 hours before kickoff and left immediately after, basing themselves in Tijuana, Mexico (moved from an earlier Tucson plan to shorten travel) instead of enjoying a normal training camp. Several staff were barred entry, and winger Mehdi Torabi's single-entry visa expired after the opening match and had to be reissued as multi-entry. The US eased the rule partway through the group stage to allow entry two days before a match."),
			new TeamNews("Egypt", "FIFA ordered Egypt to strip the seven stars from their shirt",
					"Before the tournament, FIFA ordered Egypt to remove the seven stars on their kit marking their African Cup of Nations titles, since World Cup jersey stars are reserved for World Cup wins only (with a historical exception for Uruguay), and to change their squad numbers from gold to white for legibility. This was a pre-tournament equipment ruling, not an in-match kit change."),
			new TeamNews("Curaçao", "Curaçao became the smallest nation ever to reach a men's World Cup",
					"Making their debut, Curaçao (population about 156,000) became the smallest nation by population and land area ever to qualify for a men's World Cup, opening their campaign against Germany in Group E. They were the first Concacaf debutant since Panama in 2018, with a squad built heavily around diaspora players and coached by Dick Advocaat."),
			new TeamNews("Cape Verde", "Cape Verde's fairytale run made them the smallest nation to reach the knockouts",
					"Cape Verde (population about 525,000) drew Spain 0-0, drew Uruguay 2-2 and drew Saudi Arabia 0-0 to finish second in Group H unbeaten, becoming the smallest nation ever to reach the World Cup knockout stage."),
			new TeamNews("Ecuador", "Ecuador stunned Germany in the final group match",
					"Ecuador beat Germany 2-1 in their final Group E fixture, a result that ranked among the group stage's biggest upsets given Germany's pedigree."),
			new TeamNews("Australia", "Australia's win over Türkiye was one of the group stage's quiet upsets",
					"Australia beat pre-tournament dark horse Türkiye 2-0 in Group D, a result commentators flagged as underrated given Türkiye's form heading into the tournament."),
			new TeamNews("South Africa", "South Africa advanced from the group stage as Group A's second-placed team",
					"South Africa drew Czech Republic 1-1 and then beat South Korea 1-0 to finish second in Group A behind Mexico, advancing to the knockout stage."),
			new TeamNews("Türkiye", "Türkiye, tipped as a dark horse, exited in the group stage",
					"Türkiye entered the tournament as a fashionable dark-horse pick but finished bottom of Group D, exiting at the group stage after losing to Australia and the United States either side of a win over Paraguay."),
			new TeamNews("Uruguay", "Uruguay underperformed relative to expectations in the group stage",
					"Uruguay drew all three group games including a 2-2 draw with debutants Cape Verde, a result widely cited as a low point, and finished third in Group H."),
			new TeamNews("Egypt", "Mohamed Salah battled a hamstring strain during the group stage",
					"Egypt's Mohamed Salah suffered a hamstring strain during the group stage, adding to the injury concerns clubs and fans tracked through the tournament."),
			new TeamNews("Brazil", "Brazil entered as the most injury-hit major squad and lost Raphinha mid-tournament",
					"Brazil went into the tournament already missing four regulars to injury, then lost Raphinha to a hamstring strain during the group stage."),
			new TeamNews("Jordan", "Jordan made their first-ever World Cup appearance",
					"Jordan qualified for their first-ever men's World Cup as part of the tournament's 48-team expansion, finishing bottom of Group J and exiting at the group stage."),
			new TeamNews("Uzbekistan", "Uzbekistan made their first-ever World Cup appearance",
					"Uzbekistan qualified for their first-ever men's World Cup as part of the tournament's 48-team expansion, finishing bottom of Group K and exiting at the group stage."));

	private static final List<Controversy> CONTROVERSIES = List.of(new Controversy("Egypt vs Iran",
					"Shoja Khalilzadeh's stoppage-time equaliser for Iran was disallowed by VAR for offside, upheld on review. The decision helped eliminate Iran from the group. Egyptian players also protested a possible handball in the build-up."),
			new Controversy("England vs Ghana",
					"Two separate penalty-area incidents involving Ghana's Prince Kwabena Adu went unpunished with no VAR intervention. Ghana manager Carlos Queiroz said afterwards that \"VAR went for a coffee.\" No action was taken."),
			new Controversy("Argentina vs Algeria",
					"A studs-up challenge by Lionel Messi on Algeria captain Aïssa Mandi went uncarded, with VAR declining to intervene. The Algerian football federation filed an official complaint with FIFA."),
			new Controversy("Mexico vs South Africa",
					"The tournament's opening match produced three red cards, a World Cup first for an opener: South Africa's Themba Zwane was dismissed for a raised arm, alongside two denial-of-a-goal-scoring-opportunity dismissals. South Africa coach Hugo Broos criticised the officiating and was given a three-match touchline ban, which he appealed."),
			new Controversy("Canada vs Qatar",
					"Qatar's Assim Madibo was sent off for a challenge that broke Canadian midfielder Ismaël Koné's leg. FIFA's disciplinary committee extended his automatic one-match ban to five matches, a sanction some commentators considered excessive."),
			new Controversy("Brazil vs Scotland",
					"A Vinícius Júnior goal was disallowed after VAR review found a foul in the build-up on Scotland's Jack Hendry. Brazil's federation later called for more consistent application of VAR intervention standards."),
			new Controversy("Ecuador vs Germany",
					"Leroy Sané's early goal stood despite an uncarded high boot by Germany's Aleksandar Pavlović on Ecuador's Pedro Vite earlier in the same passage of play; VAR did not intervene. Ecuador went on to win the match 2-1."),
			new Controversy("Argentina vs Austria",
					"Lionel Messi's opening goal followed a challenge by Alexis Mac Allister on Austria's Xaver Schlager that VAR reviewed but did not act on. Austria manager Ralf Rangnick criticised the inconsistency of VAR decisions in the tournament."),
			new Controversy("Colombia vs Portugal",
					"A stoppage-time header from Colombia's Davinson Sánchez was ruled offside by the narrowest of margins on VAR review, denying Colombia a win in a match that finished 0-0."));

	@Tool(name = "get-news", description = "Get real news stories and notable incidents about a World Cup 2026 team, optionally filtered by team")
	public List<TeamNews> getTeamNews(
			@ToolParam(description = "An optional team filter, e.g., 'Iran'. Pass empty string for all stories.") String team) {
		return NEWS.stream().filter(n -> team == null || team.isBlank() || n.team().equalsIgnoreCase(team.trim())).toList();
	}

	@Tool(name = "get-controversies", description = "Get real controversial incidents from the World Cup 2026 group stage, optionally filtered by team")
	public List<Controversy> getControversies(
			@ToolParam(description = "An optional team filter, e.g., 'Brazil'. Pass empty string for all controversies.") String team) {
		return CONTROVERSIES.stream().filter(c -> team == null || team.isBlank() || c.fixture().toLowerCase().contains(team.toLowerCase().trim()))
				.toList();
	}
}
