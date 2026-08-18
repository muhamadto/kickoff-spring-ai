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

package ai.spring.learning.worldcup.data;

import ai.spring.learning.worldcup.model.Fixture;
import ai.spring.learning.worldcup.model.Referee;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Real 2026 FIFA World Cup group-stage fixtures, verified against public sources. Knockout-stage fixtures are not included yet.
 */
@Component
public class FixtureData {

	private static final List<Fixture> FIXTURES = List.of(
			// Group A
			new Fixture("Group Stage A", "Mexico vs South Africa", "11 June 2026", "13:00 local", "Estadio Azteca",
					new Referee("Wilton Sampaio", "Brazil")),
			new Fixture("Group Stage A", "South Korea vs Czech Republic", "11 June 2026", "20:00 local", "Estadio Akron",
					new Referee("Amin Omar", "Egypt")),
			new Fixture("Group Stage A", "Czech Republic vs South Africa", "18 June 2026", "12:00 local", "Mercedes-Benz Stadium",
					new Referee("Tori Penso", "United States")),
			new Fixture("Group Stage A", "Mexico vs South Korea", "18 June 2026", "19:00 local", "Estadio Akron", new Referee("Gustavo Tejera", "Uruguay")),
			new Fixture("Group Stage A", "Czech Republic vs Mexico", "24 June 2026", "19:00 local", "Estadio Azteca",
					new Referee("Yael Falcón", "Argentina")),
			new Fixture("Group Stage A", "South Africa vs South Korea", "24 June 2026", "19:00 local", "Estadio BBVA",
					new Referee("Facundo Tello", "Argentina")),
			// Group B
			new Fixture("Group Stage B", "Canada vs Bosnia and Herzegovina", "12 June 2026", "15:00 local", "BMO Field",
					new Referee("Facundo Tello", "Argentina")),
			new Fixture("Group Stage B", "Qatar vs Switzerland", "13 June 2026", "12:00 local", "Levi's Stadium", new Referee("Saíd Martínez", "Honduras")),
			new Fixture("Group Stage B", "Switzerland vs Bosnia and Herzegovina", "18 June 2026", "12:00 local", "SoFi Stadium",
					new Referee("João Pinheiro", "Portugal")),
			new Fixture("Group Stage B", "Canada vs Qatar", "18 June 2026", "15:00 local", "BC Place", new Referee("Cristián Garay", "Chile")),
			new Fixture("Group Stage B", "Switzerland vs Canada", "24 June 2026", "12:00 local", "BC Place", new Referee("Ramon Abatti", "Brazil")),
			new Fixture("Group Stage B", "Bosnia and Herzegovina vs Qatar", "24 June 2026", "12:00 local", "Lumen Field",
					new Referee("Jesús Valenzuela", "Venezuela")),
			// Group C
			new Fixture("Group Stage C", "Brazil vs Morocco", "13 June 2026", "18:00 local", "MetLife Stadium", new Referee("Slavko Vinčić", "Slovenia")),
			new Fixture("Group Stage C", "Haiti vs Scotland", "13 June 2026", "21:00 local", "Gillette Stadium",
					new Referee("Mustapha Ghorbal", "Algeria")),
			new Fixture("Group Stage C", "Scotland vs Morocco", "19 June 2026", "18:00 local", "Gillette Stadium",
					new Referee("Ilgiz Tantashev", "Uzbekistan")),
			new Fixture("Group Stage C", "Brazil vs Haiti", "19 June 2026", "20:30 local", "Lincoln Financial Field",
					new Referee("Alejandro Hernández Hernández", "Spain")),
			new Fixture("Group Stage C", "Scotland vs Brazil", "24 June 2026", "18:00 local", "Hard Rock Stadium",
					new Referee("César Arturo Ramos", "Mexico")),
			new Fixture("Group Stage C", "Morocco vs Haiti", "24 June 2026", "18:00 local", "Mercedes-Benz Stadium",
					new Referee("Danny Makkelie", "Netherlands")),
			// Group D
			new Fixture("Group Stage D", "United States vs Paraguay", "12 June 2026", "18:00 local", "SoFi Stadium",
					new Referee("Danny Makkelie", "Netherlands")),
			new Fixture("Group Stage D", "Australia vs Türkiye", "13 June 2026", "21:00 local", "BC Place", new Referee("Jesús Valenzuela", "Venezuela")),
			new Fixture("Group Stage D", "United States vs Australia", "19 June 2026", "12:00 local", "Lumen Field",
					new Referee("Felix Zwayer", "Germany")),
			new Fixture("Group Stage D", "Türkiye vs Paraguay", "19 June 2026", "20:00 local", "Levi's Stadium", new Referee("Iván Barton", "El Salvador")),
			new Fixture("Group Stage D", "Türkiye vs United States", "25 June 2026", "19:00 local", "SoFi Stadium",
					new Referee("Mustapha Ghorbal", "Algeria")),
			new Fixture("Group Stage D", "Paraguay vs Australia", "25 June 2026", "19:00 local", "Levi's Stadium", new Referee("Clément Turpin", "France")),
			// Group E
			new Fixture("Group Stage E", "Germany vs Curaçao", "14 June 2026", "12:00 local", "NRG Stadium", new Referee("Jalal Jayed", "Morocco")),
			new Fixture("Group Stage E", "Ivory Coast vs Ecuador", "14 June 2026", "19:00 local", "Lincoln Financial Field",
					new Referee("François Letexier", "France")),
			new Fixture("Group Stage E", "Germany vs Ivory Coast", "20 June 2026", "16:00 local", "BMO Field",
					new Referee("Juan Gabriel Benítez", "Paraguay")),
			new Fixture("Group Stage E", "Ecuador vs Curaçao", "20 June 2026", "19:00 local", "Arrowhead Stadium", new Referee("Ma Ning", "China")),
			new Fixture("Group Stage E", "Curaçao vs Ivory Coast", "25 June 2026", "16:00 local", "Lincoln Financial Field",
					new Referee("Glenn Nyberg", "Sweden")),
			new Fixture("Group Stage E", "Ecuador vs Germany", "25 June 2026", "16:00 local", "MetLife Stadium",
					new Referee("Tori Penso", "United States")),
			// Group F
			new Fixture("Group Stage F", "Netherlands vs Japan", "14 June 2026", "15:00 local", "AT&T Stadium",
					new Referee("Ismail Elfath", "United States")),
			new Fixture("Group Stage F", "Sweden vs Tunisia", "14 June 2026", "20:00 local", "Estadio BBVA", new Referee("Yael Falcón", "Argentina")),
			new Fixture("Group Stage F", "Netherlands vs Sweden", "20 June 2026", "12:00 local", "NRG Stadium", new Referee("Michael Oliver", "England")),
			new Fixture("Group Stage F", "Tunisia vs Japan", "20 June 2026", "22:00 local", "Estadio BBVA", new Referee("István Kovács", "Romania")),
			new Fixture("Group Stage F", "Japan vs Sweden", "25 June 2026", "18:00 local", "AT&T Stadium", new Referee("Iván Barton", "El Salvador")),
			new Fixture("Group Stage F", "Tunisia vs Netherlands", "25 June 2026", "18:00 local", "Arrowhead Stadium",
					new Referee("Katia Itzel García", "Mexico")),
			// Group G
			new Fixture("Group Stage G", "Belgium vs Egypt", "15 June 2026", "12:00 local", "Lumen Field", new Referee("Ramon Abatti", "Brazil")),
			new Fixture("Group Stage G", "Iran vs New Zealand", "15 June 2026", "18:00 local", "SoFi Stadium", new Referee("César Arturo Ramos", "Mexico")),
			new Fixture("Group Stage G", "Belgium vs Iran", "21 June 2026", "12:00 local", "SoFi Stadium", new Referee("Darío Herrera", "Argentina")),
			new Fixture("Group Stage G", "New Zealand vs Egypt", "21 June 2026", "18:00 local", "BC Place",
					new Referee("Omar Al Ali", "United Arab Emirates")),
			new Fixture("Group Stage G", "Egypt vs Iran", "26 June 2026", "20:00 local", "Lumen Field", new Referee("Szymon Marciniak", "Poland")),
			new Fixture("Group Stage G", "New Zealand vs Belgium", "26 June 2026", "20:00 local", "BC Place", new Referee("Adham Makhadmeh", "Jordan")),
			// Group H
			new Fixture("Group Stage H", "Spain vs Cape Verde", "15 June 2026", "12:00 local", "Mercedes-Benz Stadium",
					new Referee("Adham Makhadmeh", "Jordan")),
			new Fixture("Group Stage H", "Saudi Arabia vs Uruguay", "15 June 2026", "18:00 local", "Hard Rock Stadium",
					new Referee("Maurizio Mariani", "Italy")),
			new Fixture("Group Stage H", "Spain vs Saudi Arabia", "21 June 2026", "12:00 local", "Mercedes-Benz Stadium",
					new Referee("Raphael Claus", "Brazil")),
			new Fixture("Group Stage H", "Uruguay vs Cape Verde", "21 June 2026", "18:00 local", "Hard Rock Stadium", new Referee("Espen Eskås", "Norway")),
			new Fixture("Group Stage H", "Cape Verde vs Saudi Arabia", "26 June 2026", "19:00 local", "NRG Stadium",
					new Referee("François Letexier", "France")),
			new Fixture("Group Stage H", "Uruguay vs Spain", "26 June 2026", "18:00 local", "Estadio Akron", new Referee("Ismail Elfath", "United States")),
			// Group I
			new Fixture("Group Stage I", "France vs Senegal", "16 June 2026", "15:00 local", "MetLife Stadium",
					new Referee("Alireza Faghani", "Australia")),
			new Fixture("Group Stage I", "Iraq vs Norway", "16 June 2026", "18:00 local", "Gillette Stadium", new Referee("Pierre Atcho", "Gabon")),
			new Fixture("Group Stage I", "France vs Iraq", "22 June 2026", "17:00 local", "Lincoln Financial Field", new Referee("Drew Fischer", "Canada")),
			new Fixture("Group Stage I", "Norway vs Senegal", "22 June 2026", "20:00 local", "MetLife Stadium", new Referee("Wilton Sampaio", "Brazil")),
			new Fixture("Group Stage I", "Norway vs France", "26 June 2026", "15:00 local", "Gillette Stadium", new Referee("Michael Oliver", "England")),
			new Fixture("Group Stage I", "Senegal vs Iraq", "26 June 2026", "15:00 local", "BMO Field", new Referee("Anthony Taylor", "England")),
			// Group J
			new Fixture("Group Stage J", "Argentina vs Algeria", "16 June 2026", "20:00 local", "Arrowhead Stadium",
					new Referee("Szymon Marciniak", "Poland")),
			new Fixture("Group Stage J", "Austria vs Jordan", "16 June 2026", "21:00 local", "Levi's Stadium", new Referee("Dahane Beida", "Mauritania")),
			new Fixture("Group Stage J", "Argentina vs Austria", "22 June 2026", "12:00 local", "AT&T Stadium", new Referee("Amin Omar", "Egypt")),
			new Fixture("Group Stage J", "Jordan vs Algeria", "22 June 2026", "20:00 local", "Levi's Stadium", new Referee("Slavko Vinčić", "Slovenia")),
			new Fixture("Group Stage J", "Algeria vs Austria", "27 June 2026", "21:00 local", "Arrowhead Stadium",
					new Referee("Ilgiz Tantashev", "Uzbekistan")),
			new Fixture("Group Stage J", "Jordan vs Argentina", "27 June 2026", "21:00 local", "AT&T Stadium", new Referee("István Kovács", "Romania")),
			// Group K
			new Fixture("Group Stage K", "Portugal vs DR Congo", "17 June 2026", "12:00 local", "NRG Stadium",
					new Referee("Abdulrahman Al-Jassim", "Qatar")),
			new Fixture("Group Stage K", "Uzbekistan vs Colombia", "17 June 2026", "20:00 local", "Estadio Azteca",
					new Referee("Anthony Taylor", "England")),
			new Fixture("Group Stage K", "Portugal vs Uzbekistan", "23 June 2026", "12:00 local", "NRG Stadium", new Referee("Jalal Jayed", "Morocco")),
			new Fixture("Group Stage K", "Colombia vs DR Congo", "23 June 2026", "20:00 local", "Estadio Akron", new Referee("Maurizio Mariani", "Italy")),
			new Fixture("Group Stage K", "Colombia vs Portugal", "27 June 2026", "19:30 local", "Hard Rock Stadium",
					new Referee("Alireza Faghani", "Australia")),
			new Fixture("Group Stage K", "DR Congo vs Uzbekistan", "27 June 2026", "19:30 local", "Mercedes-Benz Stadium",
					new Referee("Felix Zwayer", "Germany")),
			// Group L
			new Fixture("Group Stage L", "England vs Croatia", "17 June 2026", "15:00 local", "AT&T Stadium", new Referee("Clément Turpin", "France")),
			new Fixture("Group Stage L", "Ghana vs Panama", "17 June 2026", "19:00 local", "BMO Field", new Referee("Glenn Nyberg", "Sweden")),
			new Fixture("Group Stage L", "England vs Ghana", "23 June 2026", "16:00 local", "Gillette Stadium", new Referee("Saíd Martínez", "Honduras")),
			new Fixture("Group Stage L", "Panama vs Croatia", "23 June 2026", "19:00 local", "BMO Field", new Referee("Pierre Atcho", "Gabon")),
			new Fixture("Group Stage L", "Panama vs England", "27 June 2026", "17:00 local", "MetLife Stadium",
					new Referee("Abdulrahman Al-Jassim", "Qatar")),
			new Fixture("Group Stage L", "Croatia vs Ghana", "27 June 2026", "17:00 local", "Lincoln Financial Field",
					new Referee("Drew Fischer", "Canada")));

	public List<Fixture> fixturesOf(final String date, final String team) {
		return FIXTURES.stream().filter(f -> date == null || date.isBlank() || f.date().equalsIgnoreCase(date.trim()))
				.filter(f -> team == null || team.isBlank() || f.fixture().toLowerCase().contains(team.toLowerCase().trim())).toList();
	}

	public List<String> fixtureNames() {
		return FIXTURES.stream().map(Fixture::fixture).toList();
	}
}
