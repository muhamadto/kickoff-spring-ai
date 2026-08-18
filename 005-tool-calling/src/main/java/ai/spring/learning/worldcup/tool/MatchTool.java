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

import ai.spring.learning.worldcup.model.MatchStats;
import java.util.Map;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * Real 2026 FIFA World Cup group-stage results and match statistics, verified against public sources and Wikipedia match-report sections. Shots and
 * shots on target are null for every match: no source publishes them for this tournament's group stage. Cards are null only for the matches where no
 * source reported them either (Bosnia and Herzegovina vs Qatar, Morocco vs Haiti, Paraguay vs Australia, Germany vs Ivory Coast); every other card
 * count is a verified real number, including 0. Man of the Match is empty where no source named one.
 */
@Component
public class MatchTool {

	private static final Map<String, String> RESULTS = Map.ofEntries(Map.entry("mexico vs south africa", "Mexico 2-0 South Africa (FT)"),
			Map.entry("south korea vs czech republic", "South Korea 2-1 Czech Republic (FT)"),
			Map.entry("czech republic vs south africa", "Czech Republic 1-1 South Africa (FT)"),
			Map.entry("mexico vs south korea", "Mexico 1-0 South Korea (FT)"), Map.entry("czech republic vs mexico", "Czech Republic 0-3 Mexico (FT)"),
			Map.entry("south africa vs south korea", "South Africa 1-0 South Korea (FT)"),
			Map.entry("canada vs bosnia and herzegovina", "Canada 1-1 Bosnia and Herzegovina (FT)"),
			Map.entry("qatar vs switzerland", "Qatar 1-1 Switzerland (FT)"),
			Map.entry("switzerland vs bosnia and herzegovina", "Switzerland 4-1 Bosnia and Herzegovina (FT)"),
			Map.entry("canada vs qatar", "Canada 6-0 Qatar (FT)"), Map.entry("switzerland vs canada", "Switzerland 2-1 Canada (FT)"),
			Map.entry("bosnia and herzegovina vs qatar", "Bosnia and Herzegovina 3-1 Qatar (FT)"),
			Map.entry("brazil vs morocco", "Brazil 1-1 Morocco (FT)"), Map.entry("haiti vs scotland", "Haiti 0-1 Scotland (FT)"),
			Map.entry("scotland vs morocco", "Scotland 0-1 Morocco (FT)"), Map.entry("brazil vs haiti", "Brazil 3-0 Haiti (FT)"),
			Map.entry("scotland vs brazil", "Scotland 0-3 Brazil (FT)"), Map.entry("morocco vs haiti", "Morocco 4-2 Haiti (FT)"),
			Map.entry("united states vs paraguay", "United States 4-1 Paraguay (FT)"), Map.entry("australia vs türkiye", "Australia 2-0 Türkiye (FT)"),
			Map.entry("united states vs australia", "United States 2-0 Australia (FT)"), Map.entry("türkiye vs paraguay", "Türkiye 0-1 Paraguay (FT)"),
			Map.entry("türkiye vs united states", "Türkiye 3-2 United States (FT)"), Map.entry("paraguay vs australia", "Paraguay 0-0 Australia (FT)"),
			Map.entry("germany vs curaçao", "Germany 7-1 Curaçao (FT)"), Map.entry("ivory coast vs ecuador", "Ivory Coast 1-0 Ecuador (FT)"),
			Map.entry("germany vs ivory coast", "Germany 2-1 Ivory Coast (FT)"), Map.entry("ecuador vs curaçao", "Ecuador 0-0 Curaçao (FT)"),
			Map.entry("curaçao vs ivory coast", "Curaçao 0-2 Ivory Coast (FT)"), Map.entry("ecuador vs germany", "Ecuador 2-1 Germany (FT)"),
			Map.entry("netherlands vs japan", "Netherlands 2-2 Japan (FT)"), Map.entry("sweden vs tunisia", "Sweden 5-1 Tunisia (FT)"),
			Map.entry("netherlands vs sweden", "Netherlands 5-1 Sweden (FT)"), Map.entry("tunisia vs japan", "Tunisia 0-4 Japan (FT)"),
			Map.entry("japan vs sweden", "Japan 1-1 Sweden (FT)"), Map.entry("tunisia vs netherlands", "Tunisia 1-3 Netherlands (FT)"),
			Map.entry("belgium vs egypt", "Belgium 1-1 Egypt (FT)"), Map.entry("iran vs new zealand", "Iran 2-2 New Zealand (FT)"),
			Map.entry("belgium vs iran", "Belgium 0-0 Iran (FT)"), Map.entry("new zealand vs egypt", "New Zealand 1-3 Egypt (FT)"),
			Map.entry("egypt vs iran", "Egypt 1-1 Iran (FT)"), Map.entry("new zealand vs belgium", "New Zealand 1-5 Belgium (FT)"),
			Map.entry("spain vs cape verde", "Spain 0-0 Cape Verde (FT)"), Map.entry("saudi arabia vs uruguay", "Saudi Arabia 1-1 Uruguay (FT)"),
			Map.entry("spain vs saudi arabia", "Spain 4-0 Saudi Arabia (FT)"), Map.entry("uruguay vs cape verde", "Uruguay 2-2 Cape Verde (FT)"),
			Map.entry("cape verde vs saudi arabia", "Cape Verde 0-0 Saudi Arabia (FT)"), Map.entry("uruguay vs spain", "Uruguay 0-1 Spain (FT)"),
			Map.entry("france vs senegal", "France 3-1 Senegal (FT)"), Map.entry("iraq vs norway", "Iraq 1-4 Norway (FT)"),
			Map.entry("france vs iraq", "France 3-0 Iraq (FT)"), Map.entry("norway vs senegal", "Norway 3-2 Senegal (FT)"),
			Map.entry("norway vs france", "Norway 1-4 France (FT)"), Map.entry("senegal vs iraq", "Senegal 5-0 Iraq (FT)"),
			Map.entry("argentina vs algeria", "Argentina 3-0 Algeria (FT)"), Map.entry("austria vs jordan", "Austria 3-1 Jordan (FT)"),
			Map.entry("argentina vs austria", "Argentina 2-0 Austria (FT)"), Map.entry("jordan vs algeria", "Jordan 1-2 Algeria (FT)"),
			Map.entry("algeria vs austria", "Algeria 3-3 Austria (FT)"), Map.entry("jordan vs argentina", "Jordan 1-3 Argentina (FT)"),
			Map.entry("portugal vs dr congo", "Portugal 1-1 DR Congo (FT)"), Map.entry("uzbekistan vs colombia", "Uzbekistan 1-3 Colombia (FT)"),
			Map.entry("portugal vs uzbekistan", "Portugal 5-0 Uzbekistan (FT)"), Map.entry("colombia vs dr congo", "Colombia 1-0 DR Congo (FT)"),
			Map.entry("colombia vs portugal", "Colombia 0-0 Portugal (FT)"), Map.entry("dr congo vs uzbekistan", "DR Congo 3-1 Uzbekistan (FT)"),
			Map.entry("england vs croatia", "England 4-2 Croatia (FT)"), Map.entry("ghana vs panama", "Ghana 1-0 Panama (FT)"),
			Map.entry("england vs ghana", "England 0-0 Ghana (FT)"), Map.entry("panama vs croatia", "Panama 0-1 Croatia (FT)"),
			Map.entry("panama vs england", "Panama 0-2 England (FT)"), Map.entry("croatia vs ghana", "Croatia 2-1 Ghana (FT)"));

	private static final Map<String, MatchStats> STATS = Map.ofEntries(
			// Group A
			Map.entry("mexico vs south africa", new MatchStats("Mexico vs South Africa", "Julián Quiñones", 1, 1, 1, 2, null, null, null, null)),
			Map.entry("south korea vs czech republic",
					new MatchStats("South Korea vs Czech Republic", "Hwang In-beom", 1, 0, 0, 0, null, null, null, null)),
			Map.entry("czech republic vs south africa",
					new MatchStats("Czech Republic vs South Africa", "Ladislav Krejčí", 1, 0, 3, 0, null, null, null, null)),
			Map.entry("mexico vs south korea", new MatchStats("Mexico vs South Korea", "Luis Romo", 0, 0, 2, 0, null, null, null, null)),
			Map.entry("czech republic vs mexico", new MatchStats("Czech Republic vs Mexico", "Mateo Chávez", 0, 0, 1, 0, null, null, null, null)),
			Map.entry("south africa vs south korea", new MatchStats("South Africa vs South Korea", "", 1, 0, 0, 0, null, null, null, null)),
			// Group B
			Map.entry("canada vs bosnia and herzegovina",
					new MatchStats("Canada vs Bosnia and Herzegovina", "Ismaël Koné", 1, 0, 3, 0, null, null, null, null)),
			Map.entry("qatar vs switzerland", new MatchStats("Qatar vs Switzerland", "Mahmud Abunada", 2, 0, 1, 0, null, null, null, null)),
			Map.entry("switzerland vs bosnia and herzegovina",
					new MatchStats("Switzerland vs Bosnia and Herzegovina", "Johan Manzambi", 1, 0, 2, 1, null, null, null, null)),
			Map.entry("canada vs qatar", new MatchStats("Canada vs Qatar", "Jonathan David", 1, 0, 1, 2, null, null, null, null)),
			Map.entry("switzerland vs canada", new MatchStats("Switzerland vs Canada", "Johan Manzambi", 1, 0, 2, 0, null, null, null, null)),
			Map.entry("bosnia and herzegovina vs qatar",
					new MatchStats("Bosnia and Herzegovina vs Qatar", "", null, null, null, null, null, null, null, null)),
			// Group C
			Map.entry("brazil vs morocco", new MatchStats("Brazil vs Morocco", "Vinícius Júnior", 2, 0, 0, 0, null, null, null, null)),
			Map.entry("haiti vs scotland", new MatchStats("Haiti vs Scotland", "John McGinn", 1, 0, 2, 0, null, null, null, null)),
			Map.entry("scotland vs morocco", new MatchStats("Scotland vs Morocco", "Ismael Saibari", 1, 0, 1, 0, null, null, null, null)),
			Map.entry("brazil vs haiti", new MatchStats("Brazil vs Haiti", "Vinícius Júnior", 1, 0, 3, 0, null, null, null, null)),
			Map.entry("scotland vs brazil", new MatchStats("Scotland vs Brazil", "Vinícius Júnior", 1, 0, 2, 0, null, null, null, null)),
			Map.entry("morocco vs haiti", new MatchStats("Morocco vs Haiti", "", null, null, null, null, null, null, null, null)),
			// Group D
			Map.entry("united states vs paraguay", new MatchStats("United States vs Paraguay", "Folarin Balogun", 1, 0, 4, 0, null, null, null, null)),
			Map.entry("australia vs türkiye", new MatchStats("Australia vs Türkiye", "Nestory Irankunda", 0, 0, 1, 0, null, null, null, null)),
			Map.entry("united states vs australia", new MatchStats("United States vs Australia", "Folarin Balogun", 2, 0, 2, 0, null, null, null, null)),
			Map.entry("türkiye vs paraguay", new MatchStats("Türkiye vs Paraguay", "Matías Galarza", 1, 0, 2, 1, null, null, null, null)),
			Map.entry("türkiye vs united states", new MatchStats("Türkiye vs United States", "Arda Güler", 0, 0, 1, 0, null, null, null, null)),
			Map.entry("paraguay vs australia", new MatchStats("Paraguay vs Australia", "", null, null, null, null, null, null, null, null)),
			// Group E
			Map.entry("germany vs curaçao", new MatchStats("Germany vs Curaçao", "Kai Havertz", 0, 0, 0, 0, null, null, null, null)),
			Map.entry("ivory coast vs ecuador", new MatchStats("Ivory Coast vs Ecuador", "Yan Diomande", 3, 0, 1, 0, null, null, null, null)),
			Map.entry("germany vs ivory coast", new MatchStats("Germany vs Ivory Coast", "Deniz Undav", null, null, null, null, null, null, null, null)),
			Map.entry("ecuador vs curaçao", new MatchStats("Ecuador vs Curaçao", "Eloy Room", 1, 0, 5, 0, null, null, null, null)),
			Map.entry("curaçao vs ivory coast", new MatchStats("Curaçao vs Ivory Coast", "Nicolas Pépé", 2, 0, 1, 0, null, null, null, null)),
			Map.entry("ecuador vs germany", new MatchStats("Ecuador vs Germany", "Nilson Angulo", 3, 0, 1, 0, null, null, null, null)),
			// Group F
			Map.entry("netherlands vs japan", new MatchStats("Netherlands vs Japan", "Virgil van Dijk", 2, 0, 0, 0, null, null, null, null)),
			Map.entry("sweden vs tunisia", new MatchStats("Sweden vs Tunisia", "Alexander Isak", 0, 0, 1, 0, null, null, null, null)),
			Map.entry("netherlands vs sweden", new MatchStats("Netherlands vs Sweden", "Cody Gakpo", 0, 0, 2, 0, null, null, null, null)),
			Map.entry("tunisia vs japan", new MatchStats("Tunisia vs Japan", "Ayase Ueda", 1, 0, 0, 0, null, null, null, null)),
			Map.entry("japan vs sweden", new MatchStats("Japan vs Sweden", "Anthony Elanga", 1, 0, 2, 0, null, null, null, null)),
			Map.entry("tunisia vs netherlands", new MatchStats("Tunisia vs Netherlands", "", 0, 0, 0, 0, null, null, null, null)),
			// Group G
			Map.entry("belgium vs egypt", new MatchStats("Belgium vs Egypt", "Emam Ashour", 1, 0, 3, 0, null, null, null, null)),
			Map.entry("iran vs new zealand", new MatchStats("Iran vs New Zealand", "Ramin Rezaeian", 0, 0, 0, 0, null, null, null, null)),
			Map.entry("belgium vs iran", new MatchStats("Belgium vs Iran", "Alireza Beiranvand", 1, 1, 1, 0, null, null, null, null)),
			Map.entry("new zealand vs egypt", new MatchStats("New Zealand vs Egypt", "Mohamed Salah", 2, 0, 1, 0, null, null, null, null)),
			Map.entry("egypt vs iran", new MatchStats("Egypt vs Iran", "Ramin Rezaeian", 1, 0, 5, 0, null, null, null, null)),
			Map.entry("new zealand vs belgium", new MatchStats("New Zealand vs Belgium", "Leandro Trossard", 2, 0, 0, 0, null, null, null, null)),
			// Group H
			Map.entry("spain vs cape verde", new MatchStats("Spain vs Cape Verde", "Vozinha", 1, 0, 1, 0, null, null, null, null)),
			Map.entry("saudi arabia vs uruguay", new MatchStats("Saudi Arabia vs Uruguay", "Federico Valverde", 1, 0, 0, 0, null, null, null, null)),
			Map.entry("spain vs saudi arabia", new MatchStats("Spain vs Saudi Arabia", "Mikel Oyarzabal", 0, 0, 2, 0, null, null, null, null)),
			Map.entry("uruguay vs cape verde", new MatchStats("Uruguay vs Cape Verde", "Kevin Pina", 2, 0, 3, 0, null, null, null, null)),
			Map.entry("cape verde vs saudi arabia", new MatchStats("Cape Verde vs Saudi Arabia", "Deroy Duarte", 1, 0, 4, 0, null, null, null, null)),
			Map.entry("uruguay vs spain", new MatchStats("Uruguay vs Spain", "Álex Baena", 3, 1, 1, 0, null, null, null, null)),
			// Group I
			Map.entry("france vs senegal", new MatchStats("France vs Senegal", "Michael Olise", 0, 0, 0, 0, null, null, null, null)),
			Map.entry("iraq vs norway", new MatchStats("Iraq vs Norway", "Erling Haaland", 1, 0, 0, 0, null, null, null, null)),
			Map.entry("france vs iraq", new MatchStats("France vs Iraq", "Kylian Mbappé", 0, 0, 1, 0, null, null, null, null)),
			Map.entry("norway vs senegal", new MatchStats("Norway vs Senegal", "Erling Haaland", 0, 0, 0, 0, null, null, null, null)),
			Map.entry("norway vs france", new MatchStats("Norway vs France", "Ousmane Dembélé", 1, 0, 1, 0, null, null, null, null)),
			Map.entry("senegal vs iraq", new MatchStats("Senegal vs Iraq", "", 1, 0, 3, 1, null, null, null, null)),
			// Group J
			Map.entry("argentina vs algeria", new MatchStats("Argentina vs Algeria", "Lionel Messi", 0, 0, 0, 0, null, null, null, null)),
			Map.entry("austria vs jordan", new MatchStats("Austria vs Jordan", "Ali Olwan", 1, 0, 0, 0, null, null, null, null)),
			Map.entry("argentina vs austria", new MatchStats("Argentina vs Austria", "Lionel Messi", 1, 0, 3, 0, null, null, null, null)),
			Map.entry("jordan vs algeria", new MatchStats("Jordan vs Algeria", "Ibrahim Maza", 1, 0, 1, 0, null, null, null, null)),
			Map.entry("algeria vs austria", new MatchStats("Algeria vs Austria", "Riyad Mahrez", 0, 0, 1, 0, null, null, null, null)),
			Map.entry("jordan vs argentina", new MatchStats("Jordan vs Argentina", "", 3, 0, 1, 0, null, null, null, null)),
			// Group K
			Map.entry("portugal vs dr congo", new MatchStats("Portugal vs DR Congo", "João Neves", 1, 0, 1, 0, null, null, null, null)),
			Map.entry("uzbekistan vs colombia", new MatchStats("Uzbekistan vs Colombia", "Luis Díaz", 1, 0, 1, 0, null, null, null, null)),
			Map.entry("portugal vs uzbekistan", new MatchStats("Portugal vs Uzbekistan", "Cristiano Ronaldo", 1, 0, 1, 0, null, null, null, null)),
			Map.entry("colombia vs dr congo", new MatchStats("Colombia vs DR Congo", "Daniel Muñoz", 1, 0, 1, 0, null, null, null, null)),
			Map.entry("colombia vs portugal", new MatchStats("Colombia vs Portugal", "Diogo Costa", 1, 0, 0, 0, null, null, null, null)),
			Map.entry("dr congo vs uzbekistan", new MatchStats("DR Congo vs Uzbekistan", "", 3, 0, 3, 0, null, null, null, null)),
			// Group L
			Map.entry("england vs croatia", new MatchStats("England vs Croatia", "Harry Kane", 0, 0, 0, 0, null, null, null, null)),
			Map.entry("ghana vs panama", new MatchStats("Ghana vs Panama", "Antoine Semenyo", 1, 0, 0, 0, null, null, null, null)),
			Map.entry("england vs ghana", new MatchStats("England vs Ghana", "Jude Bellingham", 1, 0, 1, 0, null, null, null, null)),
			Map.entry("panama vs croatia", new MatchStats("Panama vs Croatia", "Cristian Martínez", 1, 0, 0, 0, null, null, null, null)),
			Map.entry("panama vs england", new MatchStats("Panama vs England", "Jude Bellingham", 2, 0, 1, 0, null, null, null, null)),
			Map.entry("croatia vs ghana", new MatchStats("Croatia vs Ghana", "Petar Sučić", 1, 0, 1, 0, null, null, null, null)));

	@Tool(name = "get-match-result", description = "Get the final result of a played World Cup 2026 match")
	public String getMatchResult(@ToolParam(description = "The fixture as 'Home vs Away', e.g., 'Morocco vs Haiti'", required = true) String fixture) {
		return RESULTS.getOrDefault(fixture.toLowerCase().trim(),
				"No final result recorded for '" + fixture + "'. The match may not have been played yet.");
	}

	@Tool(name = "get-match-stats", description = "Get statistics (Man of the Match, cards, shots) for a played World Cup 2026 match")
	public MatchStats getMatchStats(
			@ToolParam(description = "The fixture as 'Home vs Away', e.g., 'Morocco vs Haiti'", required = true) String fixture) {
		return STATS.getOrDefault(fixture.toLowerCase().trim(), new MatchStats(fixture, "", null, null, null, null, null, null, null, null));
	}
}
