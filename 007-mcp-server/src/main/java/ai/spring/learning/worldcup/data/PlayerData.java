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

import ai.spring.learning.worldcup.model.Player;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Full 2026 FIFA World Cup squads for all 48 teams, sourced from Wikipedia's "2026 FIFA World Cup squads" article. Goals, assists, cards and Man of
 * the Match awards are only populated for players verified against {@link MatchData}'s group-stage data (the goal/assist leaderboard and Man of the
 * Match winners, exposed via {@code TournamentTools}); every other player keeps goals/assists/cards null and Man of the Match awards at 0, since no
 * verified per-player stat exists for them. Squad data is split across several {@code addGroupX} methods (one per World Cup group) rather than a
 * single map literal, because a single static initializer for ~1,250 entries would exceed the JVM's 64KB bytecode-per-method limit.
 */
@Component
public class PlayerData {

	private static final Map<String, Player> PLAYERS = buildPlayers();

	private static Map<String, Player> buildPlayers() {
		Map<String, Player> players = new HashMap<>();
		addCuratedStats(players);
		addGroupA(players);
		addGroupB(players);
		addGroupC(players);
		addGroupE(players);
		addGroupF(players);
		addGroupG(players);
		addGroupH(players);
		addGroupI(players);
		addGroupJ(players);
		addGroupK(players);
		addGroupL(players);
		return Map.copyOf(players);
	}

	private static void put(Map<String, Player> players, Player player) {
		Player existing = players.putIfAbsent(player.name().toLowerCase(), player);
		if (existing != null) {
			throw new IllegalStateException("Duplicate player key '%s': %s vs %s".formatted(player.name().toLowerCase(), existing, player));
		}
	}

	/**
	 * Man of the Match winners and the goal/assist leaderboard, with real verified stats.
	 */
	private static void addCuratedStats(Map<String, Player> players) {
		put(players, new Player("Julián Quiñones", "Mexico", 4, 1, null, null, 1));
		put(players, new Player("Hwang In-beom", "South Korea", null, null, null, null, 1));
		put(players, new Player("Ladislav Krejčí", "Czech Republic", null, null, null, null, 1));
		put(players, new Player("Luis Romo", "Mexico", null, null, null, null, 1));
		put(players, new Player("Mateo Chávez", "Mexico", null, null, null, null, 1));
		put(players, new Player("Ismaël Koné", "Canada", null, null, null, null, 1));
		put(players, new Player("Mahmud Abunada", "Qatar", null, null, null, null, 1));
		put(players, new Player("Johan Manzambi", "Switzerland", null, null, null, null, 2));
		put(players, new Player("Jonathan David", "Canada", null, null, null, null, 1));
		put(players, new Player("Vinícius Júnior", "Brazil", 4, 1, null, null, 3));
		put(players, new Player("John McGinn", "Scotland", null, null, null, null, 1));
		put(players, new Player("Ismael Saibari", "Morocco", null, null, null, null, 1));
		put(players, new Player("Folarin Balogun", "United States", null, null, null, null, 2));
		put(players, new Player("Nestory Irankunda", "Australia", null, null, null, null, 1));
		put(players, new Player("Matías Galarza", "Paraguay", null, null, null, null, 1));
		put(players, new Player("Arda Güler", "Türkiye", null, null, null, null, 1));
		put(players, new Player("Kai Havertz", "Germany", null, null, null, null, 1));
		put(players, new Player("Yan Diomande", "Ivory Coast", null, null, null, null, 1));
		put(players, new Player("Deniz Undav", "Germany", null, null, null, null, 1));
		put(players, new Player("Eloy Room", "Curaçao", null, null, null, null, 1));
		put(players, new Player("Nicolas Pépé", "Ivory Coast", null, null, null, null, 1));
		put(players, new Player("Nilson Angulo", "Ecuador", null, null, null, null, 1));
		put(players, new Player("Virgil van Dijk", "Netherlands", null, null, null, null, 1));
		put(players, new Player("Alexander Isak", "Sweden", null, null, null, null, 1));
		put(players, new Player("Cody Gakpo", "Netherlands", null, null, null, null, 1));
		put(players, new Player("Ayase Ueda", "Japan", null, null, null, null, 1));
		put(players, new Player("Anthony Elanga", "Sweden", null, null, null, null, 1));
		put(players, new Player("Emam Ashour", "Egypt", null, null, null, null, 1));
		put(players, new Player("Ramin Rezaeian", "Iran", null, null, null, null, 2));
		put(players, new Player("Alireza Beiranvand", "Iran", null, null, null, null, 1));
		put(players, new Player("Mohamed Salah", "Egypt", null, null, null, null, 1));
		put(players, new Player("Leandro Trossard", "Belgium", null, null, null, null, 1));
		put(players, new Player("Vozinha", "Cape Verde", null, null, null, null, 1));
		put(players, new Player("Federico Valverde", "Uruguay", null, null, null, null, 1));
		put(players, new Player("Mikel Oyarzabal", "Spain", 5, 1, null, null, 1));
		put(players, new Player("Kevin Pina", "Cape Verde", null, null, null, null, 1));
		put(players, new Player("Deroy Duarte", "Cape Verde", null, null, null, null, 1));
		put(players, new Player("Álex Baena", "Spain", null, null, null, null, 1));
		put(players, new Player("Michael Olise", "France", null, null, null, null, 1));
		put(players, new Player("Erling Haaland", "Norway", 7, 0, null, null, 2));
		put(players, new Player("Kylian Mbappé", "France", 10, 4, null, null, 1));
		put(players, new Player("Ousmane Dembélé", "France", 6, 2, null, null, 1));
		put(players, new Player("Lionel Messi", "Argentina", 8, 4, null, null, 2));
		put(players, new Player("Ali Olwan", "Jordan", null, null, null, null, 1));
		put(players, new Player("Ibrahim Maza", "Algeria", null, null, null, null, 1));
		put(players, new Player("Riyad Mahrez", "Algeria", null, null, null, null, 1));
		put(players, new Player("João Neves", "Portugal", null, null, null, null, 1));
		put(players, new Player("Luis Díaz", "Colombia", null, null, null, null, 1));
		put(players, new Player("Cristiano Ronaldo", "Portugal", null, null, null, null, 1));
		put(players, new Player("Daniel Muñoz", "Colombia", null, null, null, null, 1));
		put(players, new Player("Diogo Costa", "Portugal", null, null, null, null, 1));
		put(players, new Player("Harry Kane", "England", 6, 1, null, null, 1));
		put(players, new Player("Antoine Semenyo", "Ghana", null, null, null, null, 1));
		put(players, new Player("Jude Bellingham", "England", 7, 1, null, null, 2));
		put(players, new Player("Cristian Martínez", "Panama", null, null, null, null, 1));
		put(players, new Player("Petar Sučić", "Croatia", null, null, null, null, 1));
		put(players, new Player("Ismaïla Sarr", "Senegal", 4, 1, null, null, 0));
	}

	/**
	 * Group A: Mexico, South Africa, South Korea, Czech Republic.
	 */
	private static void addGroupA(Map<String, Player> players) {
		put(players, new Player("Raúl Rangel", "Mexico", null, null, null, null, 0));
		put(players, new Player("Jorge Sánchez", "Mexico", null, null, null, null, 0));
		put(players, new Player("César Montes", "Mexico", null, null, null, null, 0));
		put(players, new Player("Edson Álvarez", "Mexico", null, null, null, null, 0));
		put(players, new Player("Johan Vásquez", "Mexico", null, null, null, null, 0));
		put(players, new Player("Érik Lira", "Mexico", null, null, null, null, 0));
		put(players, new Player("Álvaro Fidalgo", "Mexico", null, null, null, null, 0));
		put(players, new Player("Raúl Jiménez", "Mexico", null, null, null, null, 0));
		put(players, new Player("Alexis Vega", "Mexico", null, null, null, null, 0));
		put(players, new Player("Santiago Giménez", "Mexico", null, null, null, null, 0));
		put(players, new Player("Carlos Acevedo", "Mexico", null, null, null, null, 0));
		put(players, new Player("Guillermo Ochoa", "Mexico", null, null, null, null, 0));
		put(players, new Player("Armando González", "Mexico", null, null, null, null, 0));
		put(players, new Player("Israel Reyes", "Mexico", null, null, null, null, 0));
		put(players, new Player("Orbelín Pineda", "Mexico", null, null, null, null, 0));
		put(players, new Player("Obed Vargas", "Mexico", null, null, null, null, 0));
		put(players, new Player("Gilberto Mora", "Mexico", null, null, null, null, 0));
		put(players, new Player("César Huerta", "Mexico", null, null, null, null, 0));
		put(players, new Player("Guillermo Martínez", "Mexico", null, null, null, null, 0));
		put(players, new Player("Jesús Gallardo", "Mexico", null, null, null, null, 0));
		put(players, new Player("Luis Chávez", "Mexico", null, null, null, null, 0));
		put(players, new Player("Roberto Alvarado", "Mexico", null, null, null, null, 0));
		put(players, new Player("Brian Gutiérrez", "Mexico", null, null, null, null, 0));

		put(players, new Player("Ronwen Williams", "South Africa", null, null, null, null, 0));
		put(players, new Player("Thabang Matuludi", "South Africa", null, null, null, null, 0));
		put(players, new Player("Khulumani Ndamane", "South Africa", null, null, null, null, 0));
		put(players, new Player("Teboho Mokoena", "South Africa", null, null, null, null, 0));
		put(players, new Player("Thalente Mbatha", "South Africa", null, null, null, null, 0));
		put(players, new Player("Aubrey Modiba", "South Africa", null, null, null, null, 0));
		put(players, new Player("Oswin Appollis", "South Africa", null, null, null, null, 0));
		put(players, new Player("Tshepang Moremi", "South Africa", null, null, null, null, 0));
		put(players, new Player("Lyle Foster", "South Africa", null, null, null, null, 0));
		put(players, new Player("Relebohile Mofokeng", "South Africa", null, null, null, null, 0));
		put(players, new Player("Themba Zwane", "South Africa", null, null, null, null, 0));
		put(players, new Player("Thapelo Maseko", "South Africa", null, null, null, null, 0));
		put(players, new Player("Sphephelo Sithole", "South Africa", null, null, null, null, 0));
		put(players, new Player("Mbekezeli Mbokazi", "South Africa", null, null, null, null, 0));
		put(players, new Player("Iqraam Rayners", "South Africa", null, null, null, null, 0));
		put(players, new Player("Sipho Chaine", "South Africa", null, null, null, null, 0));
		put(players, new Player("Evidence Makgopa", "South Africa", null, null, null, null, 0));
		put(players, new Player("Samukele Kabini", "South Africa", null, null, null, null, 0));
		put(players, new Player("Nkosinathi Sibisi", "South Africa", null, null, null, null, 0));
		put(players, new Player("Khuliso Mudau", "South Africa", null, null, null, null, 0));
		put(players, new Player("Ime Okon", "South Africa", null, null, null, null, 0));
		put(players, new Player("Ricardo Goss", "South Africa", null, null, null, null, 0));
		put(players, new Player("Jayden Adams", "South Africa", null, null, null, null, 0));
		put(players, new Player("Olwethu Makhanya", "South Africa", null, null, null, null, 0));
		put(players, new Player("Kamogelo Sebelebele", "South Africa", null, null, null, null, 0));
		put(players, new Player("Bradley Cross", "South Africa", null, null, null, null, 0));

		put(players, new Player("Kim Seung-gyu", "South Korea", null, null, null, null, 0));
		put(players, new Player("Lee Han-beom", "South Korea", null, null, null, null, 0));
		put(players, new Player("Lee Gi-hyuk", "South Korea", null, null, null, null, 0));
		put(players, new Player("Kim Min-jae", "South Korea", null, null, null, null, 0));
		put(players, new Player("Kim Tae-hyeon", "South Korea", null, null, null, null, 0));
		put(players, new Player("Son Heung-min", "South Korea", null, null, null, null, 0));
		put(players, new Player("Paik Seung-ho", "South Korea", null, null, null, null, 0));
		put(players, new Player("Cho Gue-sung", "South Korea", null, null, null, null, 0));
		put(players, new Player("Lee Jae-sung", "South Korea", null, null, null, null, 0));
		put(players, new Player("Hwang Hee-chan", "South Korea", null, null, null, null, 0));
		put(players, new Player("Song Bum-keun", "South Korea", null, null, null, null, 0));
		put(players, new Player("Lee Tae-seok", "South Korea", null, null, null, null, 0));
		put(players, new Player("Cho Wi-je", "South Korea", null, null, null, null, 0));
		put(players, new Player("Kim Moon-hwan", "South Korea", null, null, null, null, 0));
		put(players, new Player("Park Jin-seob", "South Korea", null, null, null, null, 0));
		put(players, new Player("Bae Jun-ho", "South Korea", null, null, null, null, 0));
		put(players, new Player("Oh Hyeon-gyu", "South Korea", null, null, null, null, 0));
		put(players, new Player("Lee Kang-in", "South Korea", null, null, null, null, 0));
		put(players, new Player("Yang Hyun-jun", "South Korea", null, null, null, null, 0));
		put(players, new Player("Jo Hyeon-woo", "South Korea", null, null, null, null, 0));
		put(players, new Player("Seol Young-woo", "South Korea", null, null, null, null, 0));
		put(players, new Player("Jens Castrop", "South Korea", null, null, null, null, 0));
		put(players, new Player("Kim Jin-gyu", "South Korea", null, null, null, null, 0));
		put(players, new Player("Eom Ji-sung", "South Korea", null, null, null, null, 0));
		put(players, new Player("Lee Dong-gyeong", "South Korea", null, null, null, null, 0));

		put(players, new Player("Matěj Kovář", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("David Zima", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Tomáš Holeš", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Robin Hranáč", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Vladimír Coufal", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Štěpán Chaloupek", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Vladimír Darida", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Adam Hložek", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Patrik Schick", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Jan Kuchta", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Lukáš Červ", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Mojmír Chytil", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("David Jurásek", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Pavel Šulc", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Jindřich Staněk", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Lukáš Provod", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Michal Sadílek", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Tomáš Chorý", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Jaroslav Zelený", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("David Douděra", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Tomáš Souček", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Lukáš Horníček", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Alexandr Sojka", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Hugo Sochůrek", "Czech Republic", null, null, null, null, 0));
		put(players, new Player("Denis Višinský", "Czech Republic", null, null, null, null, 0));
	}

	/**
	 * Group B: Canada, Bosnia and Herzegovina, Qatar, Switzerland.
	 */
	private static void addGroupB(Map<String, Player> players) {
		put(players, new Player("Dayne St. Clair", "Canada", null, null, null, null, 0));
		put(players, new Player("Alistair Johnston", "Canada", null, null, null, null, 0));
		put(players, new Player("Alfie Jones", "Canada", null, null, null, null, 0));
		put(players, new Player("Luc de Fougerolles", "Canada", null, null, null, null, 0));
		put(players, new Player("Joel Waterman", "Canada", null, null, null, null, 0));
		put(players, new Player("Mathieu Choinière", "Canada", null, null, null, null, 0));
		put(players, new Player("Stephen Eustáquio", "Canada", null, null, null, null, 0));
		put(players, new Player("Cyle Larin", "Canada", null, null, null, null, 0));
		put(players, new Player("Liam Millar", "Canada", null, null, null, null, 0));
		put(players, new Player("Tani Oluwaseyi", "Canada", null, null, null, null, 0));
		put(players, new Player("Derek Cornelius", "Canada", null, null, null, null, 0));
		put(players, new Player("Jacob Shaffelburg", "Canada", null, null, null, null, 0));
		put(players, new Player("Moïse Bombito", "Canada", null, null, null, null, 0));
		put(players, new Player("Maxime Crépeau", "Canada", null, null, null, null, 0));
		put(players, new Player("Tajon Buchanan", "Canada", null, null, null, null, 0));
		put(players, new Player("Owen Goodman", "Canada", null, null, null, null, 0));
		put(players, new Player("Alphonso Davies", "Canada", null, null, null, null, 0));
		put(players, new Player("Ali Ahmed", "Canada", null, null, null, null, 0));
		put(players, new Player("Jonathan Osorio", "Canada", null, null, null, null, 0));
		put(players, new Player("Richie Laryea", "Canada", null, null, null, null, 0));
		put(players, new Player("Niko Sigur", "Canada", null, null, null, null, 0));
		put(players, new Player("Promise David", "Canada", null, null, null, null, 0));
		put(players, new Player("Nathan Saliba", "Canada", null, null, null, null, 0));
		put(players, new Player("Jayden Nelson", "Canada", null, null, null, null, 0));

		put(players, new Player("Nikola Vasilj", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Nihad Mujakić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Dennis Hadžikadunić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Tarik Muharemović", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Sead Kolašinac", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Benjamin Tahirović", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Amar Dedić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Armin Gigović", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Samed Baždar", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Ermedin Demirović", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Edin Džeko", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Mladen Jurkas", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Ivan Bašić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Ivan Šunjić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Amar Memić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Amir Hadžiahmetović", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Dženis Burnić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Nikola Katić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Kerim Alajbegović", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Esmir Bajraktarević", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Stjepan Radeljić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Martin Zlomislić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Haris Tabaković", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Arjan Malić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Jovo Lukić", "Bosnia and Herzegovina", null, null, null, null, 0));
		put(players, new Player("Ermin Mahmić", "Bosnia and Herzegovina", null, null, null, null, 0));

		put(players, new Player("Pedro Miguel", "Qatar", null, null, null, null, 0));
		put(players, new Player("Lucas Mendes", "Qatar", null, null, null, null, 0));
		put(players, new Player("Issa Laye", "Qatar", null, null, null, null, 0));
		put(players, new Player("Jassem Gaber", "Qatar", null, null, null, null, 0));
		put(players, new Player("Abdulaziz Hatem", "Qatar", null, null, null, null, 0));
		put(players, new Player("Ahmed Alaaeldin", "Qatar", null, null, null, null, 0));
		put(players, new Player("Edmilson Junior", "Qatar", null, null, null, null, 0));
		put(players, new Player("Mohammed Muntari", "Qatar", null, null, null, null, 0));
		put(players, new Player("Hassan Al-Haydos", "Qatar", null, null, null, null, 0));
		put(players, new Player("Akram Afif", "Qatar", null, null, null, null, 0));
		put(players, new Player("Karim Boudiaf", "Qatar", null, null, null, null, 0));
		put(players, new Player("Ayoub Al-Oui", "Qatar", null, null, null, null, 0));
		put(players, new Player("Homam Ahmed", "Qatar", null, null, null, null, 0));
		put(players, new Player("Yusuf Abdurisag", "Qatar", null, null, null, null, 0));
		put(players, new Player("Boualem Khoukhi", "Qatar", null, null, null, null, 0));
		put(players, new Player("Ahmed Al-Ganehi", "Qatar", null, null, null, null, 0));
		put(players, new Player("Sultan Al-Brake", "Qatar", null, null, null, null, 0));
		put(players, new Player("Almoez Ali", "Qatar", null, null, null, null, 0));
		put(players, new Player("Ahmed Fathy", "Qatar", null, null, null, null, 0));
		put(players, new Player("Salah Zakaria", "Qatar", null, null, null, null, 0));
		put(players, new Player("Meshaal Barsham", "Qatar", null, null, null, null, 0));
		put(players, new Player("Assim Madibo", "Qatar", null, null, null, null, 0));
		put(players, new Player("Tahsin Jamshid", "Qatar", null, null, null, null, 0));
		put(players, new Player("Al-Hashmi Al-Hussain", "Qatar", null, null, null, null, 0));
		put(players, new Player("Mohamed Manai", "Qatar", null, null, null, null, 0));

		put(players, new Player("Gregor Kobel", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Miro Muheim", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Silvan Widmer", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Nico Elvedi", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Manuel Akanji", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Denis Zakaria", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Breel Embolo", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Remo Freuler", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Granit Xhaka", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Dan Ndoye", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Yvon Mvogo", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Ricardo Rodriguez", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Ardon Jashari", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Djibril Sow", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Christian Fassnacht", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Rubén Vargas", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Eray Cömert", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Noah Okafor", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Michel Aebischer", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Marvin Keller", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Fabian Rieder", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Zeki Amdouni", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Aurèle Amenda", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Luca Jaquez", "Switzerland", null, null, null, null, 0));
		put(players, new Player("Cedric Itten", "Switzerland", null, null, null, null, 0));
	}

	/**
	 * Group C: Brazil, Morocco, Haiti, Scotland.
	 */
	private static void addGroupC(Map<String, Player> players) {
		put(players, new Player("Alisson", "Brazil", null, null, null, null, 0));
		put(players, new Player("Éderson Silva", "Brazil", null, null, null, null, 0));
		put(players, new Player("Gabriel Magalhães", "Brazil", null, null, null, null, 0));
		put(players, new Player("Marquinhos", "Brazil", null, null, null, null, 0));
		put(players, new Player("Casemiro", "Brazil", null, null, null, null, 0));
		put(players, new Player("Alex Sandro", "Brazil", null, null, null, null, 0));
		put(players, new Player("Bruno Guimarães", "Brazil", null, null, null, null, 0));
		put(players, new Player("Matheus Cunha", "Brazil", null, null, null, null, 0));
		put(players, new Player("Neymar", "Brazil", null, null, null, null, 0));
		put(players, new Player("Raphinha", "Brazil", null, null, null, null, 0));
		put(players, new Player("Weverton", "Brazil", null, null, null, null, 0));
		put(players, new Player("Danilo Luiz", "Brazil", null, null, null, null, 0));
		put(players, new Player("Bremer", "Brazil", null, null, null, null, 0));
		put(players, new Player("Léo Pereira", "Brazil", null, null, null, null, 0));
		put(players, new Player("Douglas Santos", "Brazil", null, null, null, null, 0));
		put(players, new Player("Fabinho", "Brazil", null, null, null, null, 0));
		put(players, new Player("Danilo Santos", "Brazil", null, null, null, null, 0));
		put(players, new Player("Endrick", "Brazil", null, null, null, null, 0));
		put(players, new Player("Lucas Paquetá", "Brazil", null, null, null, null, 0));
		put(players, new Player("Luiz Henrique", "Brazil", null, null, null, null, 0));
		put(players, new Player("Gabriel Martinelli", "Brazil", null, null, null, null, 0));
		put(players, new Player("Ederson Moraes", "Brazil", null, null, null, null, 0));
		put(players, new Player("Roger Ibañez", "Brazil", null, null, null, null, 0));
		put(players, new Player("Igor Thiago", "Brazil", null, null, null, null, 0));
		put(players, new Player("Rayan", "Brazil", null, null, null, null, 0));

		put(players, new Player("Yassine Bounou", "Morocco", null, null, null, null, 0));
		put(players, new Player("Achraf Hakimi", "Morocco", null, null, null, null, 0));
		put(players, new Player("Noussair Mazraoui", "Morocco", null, null, null, null, 0));
		put(players, new Player("Sofyan Amrabat", "Morocco", null, null, null, null, 0));
		put(players, new Player("Marwane Saâdane", "Morocco", null, null, null, null, 0));
		put(players, new Player("Ayyoub Bouaddi", "Morocco", null, null, null, null, 0));
		put(players, new Player("Chemsdine Talbi", "Morocco", null, null, null, null, 0));
		put(players, new Player("Azzedine Ounahi", "Morocco", null, null, null, null, 0));
		put(players, new Player("Soufiane Rahimi", "Morocco", null, null, null, null, 0));
		put(players, new Player("Brahim Díaz", "Morocco", null, null, null, null, 0));
		put(players, new Player("Munir Mohamedi", "Morocco", null, null, null, null, 0));
		put(players, new Player("Zakaria El Ouahdi", "Morocco", null, null, null, null, 0));
		put(players, new Player("Issa Diop", "Morocco", null, null, null, null, 0));
		put(players, new Player("Samir El Mourabet", "Morocco", null, null, null, null, 0));
		put(players, new Player("Gessime Yassine", "Morocco", null, null, null, null, 0));
		put(players, new Player("Amine Sbaïf", "Morocco", null, null, null, null, 0));
		put(players, new Player("Chadi Riad", "Morocco", null, null, null, null, 0));
		put(players, new Player("Youssef Belammari", "Morocco", null, null, null, null, 0));
		put(players, new Player("Ayoub El Kaabi", "Morocco", null, null, null, null, 0));
		put(players, new Player("Ayoube Amaimouni", "Morocco", null, null, null, null, 0));
		put(players, new Player("Ahmed Reda Tagnaouti", "Morocco", null, null, null, null, 0));
		put(players, new Player("Bilal El Khannouss", "Morocco", null, null, null, null, 0));
		put(players, new Player("Neil El Aynaoui", "Morocco", null, null, null, null, 0));
		put(players, new Player("Redouane Halhal", "Morocco", null, null, null, null, 0));
		put(players, new Player("Anass Salah-Eddine", "Morocco", null, null, null, null, 0));

		put(players, new Player("Johny Placide", "Haiti", null, null, null, null, 0));
		put(players, new Player("Carlens Arcus", "Haiti", null, null, null, null, 0));
		put(players, new Player("Keeto Thermoncy", "Haiti", null, null, null, null, 0));
		put(players, new Player("Ricardo Adé", "Haiti", null, null, null, null, 0));
		put(players, new Player("Hannes Delcroix", "Haiti", null, null, null, null, 0));
		put(players, new Player("Carl Sainté", "Haiti", null, null, null, null, 0));
		put(players, new Player("Derrick Etienne Jr.", "Haiti", null, null, null, null, 0));
		put(players, new Player("Martin Expérience", "Haiti", null, null, null, null, 0));
		put(players, new Player("Duckens Nazon", "Haiti", null, null, null, null, 0));
		put(players, new Player("Jean-Ricner Bellegarde", "Haiti", null, null, null, null, 0));
		put(players, new Player("Louicius Deedson", "Haiti", null, null, null, null, 0));
		put(players, new Player("Alexandre Pierre", "Haiti", null, null, null, null, 0));
		put(players, new Player("Duke Lacroix", "Haiti", null, null, null, null, 0));
		put(players, new Player("Garven Metusala", "Haiti", null, null, null, null, 0));
		put(players, new Player("Ruben Providence", "Haiti", null, null, null, null, 0));
		put(players, new Player("Lenny Joseph", "Haiti", null, null, null, null, 0));
		put(players, new Player("Danley Jean Jacques", "Haiti", null, null, null, null, 0));
		put(players, new Player("Wilson Isidor", "Haiti", null, null, null, null, 0));
		put(players, new Player("Yassin Fortuné", "Haiti", null, null, null, null, 0));
		put(players, new Player("Frantzdy Pierrot", "Haiti", null, null, null, null, 0));
		put(players, new Player("Josué Casimir", "Haiti", null, null, null, null, 0));
		put(players, new Player("Jean-Kévin Duverne", "Haiti", null, null, null, null, 0));
		put(players, new Player("Josué Duverger", "Haiti", null, null, null, null, 0));
		put(players, new Player("Wilguens Paugain", "Haiti", null, null, null, null, 0));
		put(players, new Player("Dominique Simon", "Haiti", null, null, null, null, 0));
		put(players, new Player("Woodensky Pierre", "Haiti", null, null, null, null, 0));

		put(players, new Player("Angus Gunn", "Scotland", null, null, null, null, 0));
		put(players, new Player("Aaron Hickey", "Scotland", null, null, null, null, 0));
		put(players, new Player("Andy Robertson", "Scotland", null, null, null, null, 0));
		put(players, new Player("Scott McTominay", "Scotland", null, null, null, null, 0));
		put(players, new Player("Grant Hanley", "Scotland", null, null, null, null, 0));
		put(players, new Player("Kieran Tierney", "Scotland", null, null, null, null, 0));
		put(players, new Player("Tyler Fletcher", "Scotland", null, null, null, null, 0));
		put(players, new Player("Lyndon Dykes", "Scotland", null, null, null, null, 0));
		put(players, new Player("Ché Adams", "Scotland", null, null, null, null, 0));
		put(players, new Player("Ryan Christie", "Scotland", null, null, null, null, 0));
		put(players, new Player("Liam Kelly", "Scotland", null, null, null, null, 0));
		put(players, new Player("Jack Hendry", "Scotland", null, null, null, null, 0));
		put(players, new Player("Ross Stewart", "Scotland", null, null, null, null, 0));
		put(players, new Player("John Souttar", "Scotland", null, null, null, null, 0));
		put(players, new Player("Dominic Hyam", "Scotland", null, null, null, null, 0));
		put(players, new Player("Ben Gannon-Doak", "Scotland", null, null, null, null, 0));
		put(players, new Player("George Hirst", "Scotland", null, null, null, null, 0));
		put(players, new Player("Lewis Ferguson", "Scotland", null, null, null, null, 0));
		put(players, new Player("Lawrence Shankland", "Scotland", null, null, null, null, 0));
		put(players, new Player("Craig Gordon", "Scotland", null, null, null, null, 0));
		put(players, new Player("Nathan Patterson", "Scotland", null, null, null, null, 0));
		put(players, new Player("Kenny McLean", "Scotland", null, null, null, null, 0));
		put(players, new Player("Anthony Ralston", "Scotland", null, null, null, null, 0));
		put(players, new Player("Findlay Curtis", "Scotland", null, null, null, null, 0));
		put(players, new Player("Scott McKenna", "Scotland", null, null, null, null, 0));
	}

	/**
	 * Group E: Germany, Curaçao, Ivory Coast, Ecuador.
	 */
	private static void addGroupE(Map<String, Player> players) {
		put(players, new Player("Manuel Neuer", "Germany", null, null, null, null, 0));
		put(players, new Player("Antonio Rüdiger", "Germany", null, null, null, null, 0));
		put(players, new Player("Waldemar Anton", "Germany", null, null, null, null, 0));
		put(players, new Player("Jonathan Tah", "Germany", null, null, null, null, 0));
		put(players, new Player("Aleksandar Pavlović", "Germany", null, null, null, null, 0));
		put(players, new Player("Joshua Kimmich", "Germany", null, null, null, null, 0));
		put(players, new Player("Leon Goretzka", "Germany", null, null, null, null, 0));
		put(players, new Player("Jamie Leweling", "Germany", null, null, null, null, 0));
		put(players, new Player("Jamal Musiala", "Germany", null, null, null, null, 0));
		put(players, new Player("Nick Woltemade", "Germany", null, null, null, null, 0));
		put(players, new Player("Oliver Baumann", "Germany", null, null, null, null, 0));
		put(players, new Player("Pascal Groß", "Germany", null, null, null, null, 0));
		put(players, new Player("Maximilian Beier", "Germany", null, null, null, null, 0));
		put(players, new Player("Nico Schlotterbeck", "Germany", null, null, null, null, 0));
		put(players, new Player("Angelo Stiller", "Germany", null, null, null, null, 0));
		put(players, new Player("Florian Wirtz", "Germany", null, null, null, null, 0));
		put(players, new Player("Nathaniel Brown", "Germany", null, null, null, null, 0));
		put(players, new Player("Leroy Sané", "Germany", null, null, null, null, 0));
		put(players, new Player("Nadiem Amiri", "Germany", null, null, null, null, 0));
		put(players, new Player("Alexander Nübel", "Germany", null, null, null, null, 0));
		put(players, new Player("David Raum", "Germany", null, null, null, null, 0));
		put(players, new Player("Felix Nmecha", "Germany", null, null, null, null, 0));
		put(players, new Player("Malick Thiaw", "Germany", null, null, null, null, 0));
		put(players, new Player("Assan Ouédraogo", "Germany", null, null, null, null, 0));

		put(players, new Player("Shurandy Sambo", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Juriën Gaari", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Roshon van Eijma", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Sherel Floranus", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Godfried Roemeratoe", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Juninho Bacuna", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Livano Comenencia", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Jürgen Locadia", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Leandro Bacuna", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Jeremy Antonisse", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Sontje Hansen", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Tyrese Noslin", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Kenji Gorré", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Ar'jany Martha", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Jearl Margaritha", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Brandley Kuwas", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Armando Obispo", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Gervane Kastaneer", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Joshua Brenet", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Tahith Chong", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Kevin Felida", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Riechedly Bazoer", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Deveron Fonville", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Tyrick Bodak", "Curaçao", null, null, null, null, 0));
		put(players, new Player("Trevor Doornbusch", "Curaçao", null, null, null, null, 0));

		put(players, new Player("Yahia Fofana", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Ousmane Diomande", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Ghislain Konan", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Jean Michaël Seri", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Wilfried Singo", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Seko Fofana", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Odilon Kossounou", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Franck Kessié", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Ange-Yoan Bonny", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Simon Adingra", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Elye Wahi", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Christopher Opéri", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Oumar Diakité", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Amad Diallo", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Mohamed Koné", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Guéla Doué", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Ibrahim Sangaré", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Emmanuel Agbadou", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Evan Ndicka", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Evann Guessand", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Alban Lafont", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Bazoumana Touré", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Parfait Guiagon", "Ivory Coast", null, null, null, null, 0));
		put(players, new Player("Christ Inao Oulaï", "Ivory Coast", null, null, null, null, 0));

		put(players, new Player("Hernán Galíndez", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Félix Torres", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Piero Hincapié", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Joel Ordóñez", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Jordy Alcívar", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Willian Pacho", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Pervis Estupiñán", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Anthony Valencia", "Ecuador", null, null, null, null, 0));
		put(players, new Player("John Yeboah", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Kendry Páez", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Kevin Rodríguez", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Moisés Ramírez", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Enner Valencia", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Alan Minda", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Pedro Vite", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Jordy Caicedo", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Ángelo Preciado", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Denil Castillo", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Gonzalo Plata", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Alan Franco", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Gonzalo Valle", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Moisés Caicedo", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Jeremy Arévalo", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Jackson Porozo", "Ecuador", null, null, null, null, 0));
		put(players, new Player("Yaimar Medina", "Ecuador", null, null, null, null, 0));
	}

	/**
	 * Group F: Netherlands, Japan, Sweden, Tunisia.
	 */
	private static void addGroupF(Map<String, Player> players) {
		put(players, new Player("Bart Verbruggen", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Lutsharel Geertruida", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Marten de Roon", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Nathan Aké", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Jan Paul van Hecke", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Justin Kluivert", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Ryan Gravenberch", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Wout Weghorst", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Memphis Depay", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Mats Wieffer", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Robin Roefs", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Tijjani Reijnders", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Micky van de Ven", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Guus Til", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Noa Lang", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Donyell Malen", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Brian Brobbey", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Teun Koopmeiners", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Frenkie de Jong", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Denzel Dumfries", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Mark Flekken", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Crysencio Summerville", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Jorrel Hato", "Netherlands", null, null, null, null, 0));
		put(players, new Player("Quinten Timber", "Netherlands", null, null, null, null, 0));

		put(players, new Player("Zion Suzuki", "Japan", null, null, null, null, 0));
		put(players, new Player("Yukinari Sugawara", "Japan", null, null, null, null, 0));
		put(players, new Player("Shōgo Taniguchi", "Japan", null, null, null, null, 0));
		put(players, new Player("Kō Itakura", "Japan", null, null, null, null, 0));
		put(players, new Player("Yūto Nagatomo", "Japan", null, null, null, null, 0));
		put(players, new Player("Shūto Machino", "Japan", null, null, null, null, 0));
		put(players, new Player("Ao Tanaka", "Japan", null, null, null, null, 0));
		put(players, new Player("Takefusa Kubo", "Japan", null, null, null, null, 0));
		put(players, new Player("Keisuke Gotō", "Japan", null, null, null, null, 0));
		put(players, new Player("Ritsu Dōan", "Japan", null, null, null, null, 0));
		put(players, new Player("Daizen Maeda", "Japan", null, null, null, null, 0));
		put(players, new Player("Keisuke Ōsako", "Japan", null, null, null, null, 0));
		put(players, new Player("Keito Nakamura", "Japan", null, null, null, null, 0));
		put(players, new Player("Junya Itō", "Japan", null, null, null, null, 0));
		put(players, new Player("Daichi Kamada", "Japan", null, null, null, null, 0));
		put(players, new Player("Tsuyoshi Watanabe", "Japan", null, null, null, null, 0));
		put(players, new Player("Yuito Suzuki", "Japan", null, null, null, null, 0));
		put(players, new Player("Kōki Ogawa", "Japan", null, null, null, null, 0));
		put(players, new Player("Ayumu Seko", "Japan", null, null, null, null, 0));
		put(players, new Player("Hiroki Itō", "Japan", null, null, null, null, 0));
		put(players, new Player("Takehiro Tomiyasu", "Japan", null, null, null, null, 0));
		put(players, new Player("Tomoki Hayakawa", "Japan", null, null, null, null, 0));
		put(players, new Player("Kaishū Sano", "Japan", null, null, null, null, 0));
		put(players, new Player("Junnosuke Suzuki", "Japan", null, null, null, null, 0));
		put(players, new Player("Kento Shiogai", "Japan", null, null, null, null, 0));

		put(players, new Player("Jacob Widell Zetterström", "Sweden", null, null, null, null, 0));
		put(players, new Player("Gustaf Lagerbielke", "Sweden", null, null, null, null, 0));
		put(players, new Player("Victor Lindelöf", "Sweden", null, null, null, null, 0));
		put(players, new Player("Isak Hien", "Sweden", null, null, null, null, 0));
		put(players, new Player("Gabriel Gudmundsson", "Sweden", null, null, null, null, 0));
		put(players, new Player("Herman Johansson", "Sweden", null, null, null, null, 0));
		put(players, new Player("Lucas Bergvall", "Sweden", null, null, null, null, 0));
		put(players, new Player("Daniel Svensson", "Sweden", null, null, null, null, 0));
		put(players, new Player("Benjamin Nygren", "Sweden", null, null, null, null, 0));
		put(players, new Player("Viktor Johansson", "Sweden", null, null, null, null, 0));
		put(players, new Player("Ken Sema", "Sweden", null, null, null, null, 0));
		put(players, new Player("Hjalmar Ekdal", "Sweden", null, null, null, null, 0));
		put(players, new Player("Carl Starfelt", "Sweden", null, null, null, null, 0));
		put(players, new Player("Jesper Karlström", "Sweden", null, null, null, null, 0));
		put(players, new Player("Viktor Gyökeres", "Sweden", null, null, null, null, 0));
		put(players, new Player("Yasin Ayari", "Sweden", null, null, null, null, 0));
		put(players, new Player("Mattias Svanberg", "Sweden", null, null, null, null, 0));
		put(players, new Player("Eric Smith", "Sweden", null, null, null, null, 0));
		put(players, new Player("Alexander Bernhardsson", "Sweden", null, null, null, null, 0));
		put(players, new Player("Besfort Zeneli", "Sweden", null, null, null, null, 0));
		put(players, new Player("Kristoffer Nordfeldt", "Sweden", null, null, null, null, 0));
		put(players, new Player("Elliot Stroud", "Sweden", null, null, null, null, 0));
		put(players, new Player("Gustaf Nilsson", "Sweden", null, null, null, null, 0));
		put(players, new Player("Taha Ali", "Sweden", null, null, null, null, 0));

		put(players, new Player("Mouhib Chamakh", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Ali Abdi", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Montassar Talbi", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Omar Rekik", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Adem Arous", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Dylan Bronn", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Elias Achouri", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Elias Saad", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Hazem Mastouri", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Hannibal Mejbri", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Ismaël Gharbi", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Mortadha Ben Ouanes", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Rani Khedira", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Khalil Ayari", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Mohamed Belhadj Mahmoud", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Aymen Dahmen", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Ellyes Skhiri", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Rayan Elloumi", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Firas Chaouat", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Yan Valery", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Mohamed Amine Ben Hamida", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Sabri Ben Hessen", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Moutaz Neffati", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Raed Chikhaoui", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Anis Ben Slimane", "Tunisia", null, null, null, null, 0));
		put(players, new Player("Sebastian Tounekti", "Tunisia", null, null, null, null, 0));
	}

	/**
	 * Group G: Belgium, Egypt, Iran, New Zealand.
	 */
	private static void addGroupG(Map<String, Player> players) {
		put(players, new Player("Thibaut Courtois", "Belgium", null, null, null, null, 0));
		put(players, new Player("Zeno Debast", "Belgium", null, null, null, null, 0));
		put(players, new Player("Arthur Theate", "Belgium", null, null, null, null, 0));
		put(players, new Player("Brandon Mechele", "Belgium", null, null, null, null, 0));
		put(players, new Player("Maxim De Cuyper", "Belgium", null, null, null, null, 0));
		put(players, new Player("Axel Witsel", "Belgium", null, null, null, null, 0));
		put(players, new Player("Kevin De Bruyne", "Belgium", null, null, null, null, 0));
		put(players, new Player("Youri Tielemans", "Belgium", null, null, null, null, 0));
		put(players, new Player("Romelu Lukaku", "Belgium", null, null, null, null, 0));
		put(players, new Player("Jérémy Doku", "Belgium", null, null, null, null, 0));
		put(players, new Player("Senne Lammens", "Belgium", null, null, null, null, 0));
		put(players, new Player("Mike Penders", "Belgium", null, null, null, null, 0));
		put(players, new Player("Dodi Lukébakio", "Belgium", null, null, null, null, 0));
		put(players, new Player("Thomas Meunier", "Belgium", null, null, null, null, 0));
		put(players, new Player("Koni De Winter", "Belgium", null, null, null, null, 0));
		put(players, new Player("Charles De Ketelaere", "Belgium", null, null, null, null, 0));
		put(players, new Player("Joaquin Seys", "Belgium", null, null, null, null, 0));
		put(players, new Player("Diego Moreira", "Belgium", null, null, null, null, 0));
		put(players, new Player("Hans Vanaken", "Belgium", null, null, null, null, 0));
		put(players, new Player("Timothy Castagne", "Belgium", null, null, null, null, 0));
		put(players, new Player("Alexis Saelemaekers", "Belgium", null, null, null, null, 0));
		put(players, new Player("Nicolas Raskin", "Belgium", null, null, null, null, 0));
		put(players, new Player("Amadou Onana", "Belgium", null, null, null, null, 0));
		put(players, new Player("Nathan Ngoy", "Belgium", null, null, null, null, 0));
		put(players, new Player("Matias Fernandez-Pardo", "Belgium", null, null, null, null, 0));

		put(players, new Player("Mohamed El Shenawy", "Egypt", null, null, null, null, 0));
		put(players, new Player("Yasser Ibrahim", "Egypt", null, null, null, null, 0));
		put(players, new Player("Mohamed Hany", "Egypt", null, null, null, null, 0));
		put(players, new Player("Hossam Abdelmaguid", "Egypt", null, null, null, null, 0));
		put(players, new Player("Ramy Rabia", "Egypt", null, null, null, null, 0));
		put(players, new Player("Mohamed Abdelmonem", "Egypt", null, null, null, null, 0));
		put(players, new Player("Trézéguet", "Egypt", null, null, null, null, 0));
		put(players, new Player("Hamza Abdelkarim", "Egypt", null, null, null, null, 0));
		put(players, new Player("Mostafa Ziko", "Egypt", null, null, null, null, 0));
		put(players, new Player("Haissem Hassan", "Egypt", null, null, null, null, 0));
		put(players, new Player("Ahmed Fatouh", "Egypt", null, null, null, null, 0));
		put(players, new Player("Hamdy Fathy", "Egypt", null, null, null, null, 0));
		put(players, new Player("Karim Hafez", "Egypt", null, null, null, null, 0));
		put(players, new Player("El Mahdy Soliman", "Egypt", null, null, null, null, 0));
		put(players, new Player("Mohanad Lasheen", "Egypt", null, null, null, null, 0));
		put(players, new Player("Nabil Emad", "Egypt", null, null, null, null, 0));
		put(players, new Player("Marwan Attia", "Egypt", null, null, null, null, 0));
		put(players, new Player("Ibrahim Adel", "Egypt", null, null, null, null, 0));
		put(players, new Player("Mahmoud Saber", "Egypt", null, null, null, null, 0));
		put(players, new Player("Omar Marmoush", "Egypt", null, null, null, null, 0));
		put(players, new Player("Mostafa Shobeir", "Egypt", null, null, null, null, 0));
		put(players, new Player("Tarek Alaa", "Egypt", null, null, null, null, 0));
		put(players, new Player("Zizo", "Egypt", null, null, null, null, 0));
		put(players, new Player("Mohamed Alaa", "Egypt", null, null, null, null, 0));

		put(players, new Player("Saleh Hardani", "Iran", null, null, null, null, 0));
		put(players, new Player("Ehsan Hajsafi", "Iran", null, null, null, null, 0));
		put(players, new Player("Shojae Khalilzadeh", "Iran", null, null, null, null, 0));
		put(players, new Player("Milad Mohammadi", "Iran", null, null, null, null, 0));
		put(players, new Player("Saeid Ezatolahi", "Iran", null, null, null, null, 0));
		put(players, new Player("Alireza Jahanbakhsh", "Iran", null, null, null, null, 0));
		put(players, new Player("Mohammad Mohebi", "Iran", null, null, null, null, 0));
		put(players, new Player("Mehdi Taremi", "Iran", null, null, null, null, 0));
		put(players, new Player("Mehdi Ghayedi", "Iran", null, null, null, null, 0));
		put(players, new Player("Ali Alipour", "Iran", null, null, null, null, 0));
		put(players, new Player("Payam Niazmand", "Iran", null, null, null, null, 0));
		put(players, new Player("Hossein Kanaanizadegan", "Iran", null, null, null, null, 0));
		put(players, new Player("Saman Ghoddos", "Iran", null, null, null, null, 0));
		put(players, new Player("Rouzbeh Cheshmi", "Iran", null, null, null, null, 0));
		put(players, new Player("Mahdi Torabi", "Iran", null, null, null, null, 0));
		put(players, new Player("Arya Yousefi", "Iran", null, null, null, null, 0));
		put(players, new Player("Amirhossein Hosseinzadeh", "Iran", null, null, null, null, 0));
		put(players, new Player("Ali Nemati", "Iran", null, null, null, null, 0));
		put(players, new Player("Shahriyar Moghanlou", "Iran", null, null, null, null, 0));
		put(players, new Player("Mohammad Ghorbani", "Iran", null, null, null, null, 0));
		put(players, new Player("Hossein Hosseini", "Iran", null, null, null, null, 0));
		put(players, new Player("Dennis Eckert", "Iran", null, null, null, null, 0));
		put(players, new Player("Danial Eiri", "Iran", null, null, null, null, 0));
		put(players, new Player("Amirmohammad Razzaghinia", "Iran", null, null, null, null, 0));

		put(players, new Player("Max Crocombe", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Tim Payne", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Francis de Vries", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Tyler Bindon", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Michael Boxall", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Joe Bell", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Logan Rogerson", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Marko Stamenić", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Chris Wood", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Sarpreet Singh", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Elijah Just", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Alex Paulsen", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Liberato Cacace", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Alex Rufer", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Nando Pijnaker", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Finn Surman", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Kosta Barbarouses", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Ben Waine", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Ben Old", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Callum McCowatt", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Jesse Randall", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Michael Woud", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Ryan Thomas", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Callan Elliot", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Lachlan Bayliss", "New Zealand", null, null, null, null, 0));
		put(players, new Player("Tommy Smith", "New Zealand", null, null, null, null, 0));
	}

	/**
	 * Group H: Spain, Cape Verde, Saudi Arabia, Uruguay.
	 */
	private static void addGroupH(Map<String, Player> players) {
		put(players, new Player("David Raya", "Spain", null, null, null, null, 0));
		put(players, new Player("Unai Simón", "Spain", null, null, null, null, 0));
		put(players, new Player("Joan Garcia", "Spain", null, null, null, null, 0));
		put(players, new Player("Marc Pubill", "Spain", null, null, null, null, 0));
		put(players, new Player("Álex Grimaldo", "Spain", null, null, null, null, 0));
		put(players, new Player("Eric García", "Spain", null, null, null, null, 0));
		put(players, new Player("Marcos Llorente", "Spain", null, null, null, null, 0));
		put(players, new Player("Pedro Porro", "Spain", null, null, null, null, 0));
		put(players, new Player("Aymeric Laporte", "Spain", null, null, null, null, 0));
		put(players, new Player("Pau Cubarsí", "Spain", null, null, null, null, 0));
		put(players, new Player("Marc Cucurella", "Spain", null, null, null, null, 0));
		put(players, new Player("Mikel Merino", "Spain", null, null, null, null, 0));
		put(players, new Player("Fabián Ruiz", "Spain", null, null, null, null, 0));
		put(players, new Player("Gavi", "Spain", null, null, null, null, 0));
		put(players, new Player("Rodri", "Spain", null, null, null, null, 0));
		put(players, new Player("Martín Zubimendi", "Spain", null, null, null, null, 0));
		put(players, new Player("Pedri", "Spain", null, null, null, null, 0));
		put(players, new Player("Ferran Torres", "Spain", null, null, null, null, 0));
		put(players, new Player("Dani Olmo", "Spain", null, null, null, null, 0));
		put(players, new Player("Yéremy Pino", "Spain", null, null, null, null, 0));
		put(players, new Player("Nico Williams", "Spain", null, null, null, null, 0));
		put(players, new Player("Lamine Yamal", "Spain", null, null, null, null, 0));
		put(players, new Player("Víctor Muñoz", "Spain", null, null, null, null, 0));
		put(players, new Player("Borja Iglesias", "Spain", null, null, null, null, 0));

		put(players, new Player("Márcio Rosa", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("CJ dos Santos", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Stopira", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Diney", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Pico Lopes", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Logan Costa", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Sidny Lopes Cabral", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Steven Moreira", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Wagner Pina", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Kelvin Pires", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Jovane Cabral", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("João Paulo", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Jamiro Monteiro", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Garry Rodrigues", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Laros Duarte", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Yannick Semedo", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Willy Semedo", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Telmo Arcanjo", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Hélio Varela", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Nuno da Costa", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Gilson Benchimol", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Dailon Livramento", "Cape Verde", null, null, null, null, 0));
		put(players, new Player("Ryan Mendes", "Cape Verde", null, null, null, null, 0));

		put(players, new Player("Nawaf Al-Aqidi", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Mohammed Al-Owais", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Ahmed Al-Kassar", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Ali Majrashi", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Ali Lajami", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Abdulelah Al-Amri", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Hassan Al-Tambakti", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Saud Abdulhamid", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Nawaf Boushal", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Hassan Kadesh", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Moteb Al-Harbi", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Jehad Thakri", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Mohammed Abu Al-Shamat", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Nasser Al-Dawsari", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Musab Al-Juwayr", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Abdullah Al-Khaibari", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Ziyad Al-Johani", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Alaa Al-Hejji", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Mohamed Kanno", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Ayman Yahya", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Firas Al-Buraikan", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Salem Al-Dawsari", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Saleh Al-Shehri", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Khalid Al-Ghannam", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Abdullah Al-Hamdan", "Saudi Arabia", null, null, null, null, 0));
		put(players, new Player("Sultan Mandash", "Saudi Arabia", null, null, null, null, 0));

		put(players, new Player("Sergio Rochet", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Santiago Mele", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Fernando Muslera", "Uruguay", null, null, null, null, 0));
		put(players, new Player("José María Giménez", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Sebastián Cáceres", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Ronald Araújo", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Guillermo Varela", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Mathías Olivera", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Matías Viña", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Santiago Bueno", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Rodrigo Bentancur", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Manuel Ugarte", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Nicolás de la Cruz", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Giorgian de Arrascaeta", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Agustín Canobbio", "Uruguay", null, null, null, null, 0));
		// Uruguay's midfielder Emiliano Martínez is omitted: he shares an exact name with
		// Argentina's goalkeeper (added in Group J), and player lookup here is by name only.
		put(players, new Player("Maximiliano Araújo", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Joaquín Piquerez", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Juan Manuel Sanabria", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Rodrigo Zalazar", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Darwin Núñez", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Facundo Pellistri", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Brian Rodríguez", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Rodrigo Aguirre", "Uruguay", null, null, null, null, 0));
		put(players, new Player("Federico Viñas", "Uruguay", null, null, null, null, 0));
	}

	/**
	 * Group I: France, Senegal, Iraq, Norway.
	 */
	private static void addGroupI(Map<String, Player> players) {
		put(players, new Player("Brice Samba", "France", null, null, null, null, 0));
		put(players, new Player("Malo Gusto", "France", null, null, null, null, 0));
		put(players, new Player("Lucas Digne", "France", null, null, null, null, 0));
		put(players, new Player("Dayot Upamecano", "France", null, null, null, null, 0));
		put(players, new Player("Jules Koundé", "France", null, null, null, null, 0));
		put(players, new Player("Manu Koné", "France", null, null, null, null, 0));
		put(players, new Player("Aurélien Tchouaméni", "France", null, null, null, null, 0));
		put(players, new Player("Marcus Thuram", "France", null, null, null, null, 0));
		put(players, new Player("Bradley Barcola", "France", null, null, null, null, 0));
		put(players, new Player("N'Golo Kanté", "France", null, null, null, null, 0));
		put(players, new Player("Adrien Rabiot", "France", null, null, null, null, 0));
		put(players, new Player("Ibrahima Konaté", "France", null, null, null, null, 0));
		put(players, new Player("Mike Maignan", "France", null, null, null, null, 0));
		put(players, new Player("William Saliba", "France", null, null, null, null, 0));
		put(players, new Player("Warren Zaïre-Emery", "France", null, null, null, null, 0));
		put(players, new Player("Théo Hernandez", "France", null, null, null, null, 0));
		put(players, new Player("Désiré Doué", "France", null, null, null, null, 0));
		put(players, new Player("Lucas Hernandez", "France", null, null, null, null, 0));
		put(players, new Player("Jean-Philippe Mateta", "France", null, null, null, null, 0));
		put(players, new Player("Robin Risser", "France", null, null, null, null, 0));
		put(players, new Player("Rayan Cherki", "France", null, null, null, null, 0));
		put(players, new Player("Maghnes Akliouche", "France", null, null, null, null, 0));
		put(players, new Player("Maxence Lacroix", "France", null, null, null, null, 0));

		put(players, new Player("Yehvann Diouf", "Senegal", null, null, null, null, 0));
		put(players, new Player("Mamadou Sarr", "Senegal", null, null, null, null, 0));
		put(players, new Player("Kalidou Koulibaly", "Senegal", null, null, null, null, 0));
		put(players, new Player("Abdoulaye Seck", "Senegal", null, null, null, null, 0));
		put(players, new Player("Idrissa Gueye", "Senegal", null, null, null, null, 0));
		put(players, new Player("Pathé Ciss", "Senegal", null, null, null, null, 0));
		put(players, new Player("Assane Diao", "Senegal", null, null, null, null, 0));
		put(players, new Player("Lamine Camara", "Senegal", null, null, null, null, 0));
		put(players, new Player("Bamba Dieng", "Senegal", null, null, null, null, 0));
		put(players, new Player("Sadio Mané", "Senegal", null, null, null, null, 0));
		put(players, new Player("Nicolas Jackson", "Senegal", null, null, null, null, 0));
		put(players, new Player("Cherif Ndiaye", "Senegal", null, null, null, null, 0));
		put(players, new Player("Iliman Ndiaye", "Senegal", null, null, null, null, 0));
		put(players, new Player("Ismail Jakobs", "Senegal", null, null, null, null, 0));
		put(players, new Player("Krépin Diatta", "Senegal", null, null, null, null, 0));
		put(players, new Player("Édouard Mendy", "Senegal", null, null, null, null, 0));
		put(players, new Player("Pape Matar Sarr", "Senegal", null, null, null, null, 0));
		put(players, new Player("Moussa Niakhaté", "Senegal", null, null, null, null, 0));
		put(players, new Player("Ibrahim Mbaye", "Senegal", null, null, null, null, 0));
		put(players, new Player("Habib Diarra", "Senegal", null, null, null, null, 0));
		put(players, new Player("Bara Sapoko Ndiaye", "Senegal", null, null, null, null, 0));
		put(players, new Player("Mory Diaw", "Senegal", null, null, null, null, 0));
		put(players, new Player("Antoine Mendy", "Senegal", null, null, null, null, 0));
		put(players, new Player("El Hadji Malick Diouf", "Senegal", null, null, null, null, 0));
		put(players, new Player("Pape Gueye", "Senegal", null, null, null, null, 0));

		put(players, new Player("Fahad Talib", "Iraq", null, null, null, null, 0));
		put(players, new Player("Rebin Sulaka", "Iraq", null, null, null, null, 0));
		put(players, new Player("Hussein Ali", "Iraq", null, null, null, null, 0));
		put(players, new Player("Zaid Tahseen", "Iraq", null, null, null, null, 0));
		put(players, new Player("Akam Hashim", "Iraq", null, null, null, null, 0));
		put(players, new Player("Manaf Younis", "Iraq", null, null, null, null, 0));
		put(players, new Player("Youssef Amyn", "Iraq", null, null, null, null, 0));
		put(players, new Player("Ibrahim Bayesh", "Iraq", null, null, null, null, 0));
		put(players, new Player("Ali Al-Hamadi", "Iraq", null, null, null, null, 0));
		put(players, new Player("Mohanad Ali", "Iraq", null, null, null, null, 0));
		put(players, new Player("Ahmed Qasem", "Iraq", null, null, null, null, 0));
		put(players, new Player("Jalal Hassan", "Iraq", null, null, null, null, 0));
		put(players, new Player("Ali Yousif", "Iraq", null, null, null, null, 0));
		put(players, new Player("Zidane Iqbal", "Iraq", null, null, null, null, 0));
		put(players, new Player("Ahmed Maknzi", "Iraq", null, null, null, null, 0));
		put(players, new Player("Amir Al-Ammari", "Iraq", null, null, null, null, 0));
		put(players, new Player("Ali Jasim", "Iraq", null, null, null, null, 0));
		put(players, new Player("Aymen Hussein", "Iraq", null, null, null, null, 0));
		put(players, new Player("Kevin Yakob", "Iraq", null, null, null, null, 0));
		put(players, new Player("Aimar Sher", "Iraq", null, null, null, null, 0));
		put(players, new Player("Marko Farji", "Iraq", null, null, null, null, 0));
		put(players, new Player("Ahmed Basil", "Iraq", null, null, null, null, 0));
		put(players, new Player("Merchas Doski", "Iraq", null, null, null, null, 0));
		put(players, new Player("Zaid Ismail", "Iraq", null, null, null, null, 0));
		put(players, new Player("Mustafa Saadoon", "Iraq", null, null, null, null, 0));
		put(players, new Player("Frans Putros", "Iraq", null, null, null, null, 0));

		put(players, new Player("Ørjan Nyland", "Norway", null, null, null, null, 0));
		put(players, new Player("Morten Thorsby", "Norway", null, null, null, null, 0));
		put(players, new Player("Kristoffer Ajer", "Norway", null, null, null, null, 0));
		put(players, new Player("Leo Østigård", "Norway", null, null, null, null, 0));
		put(players, new Player("David Møller Wolfe", "Norway", null, null, null, null, 0));
		put(players, new Player("Patrick Berg", "Norway", null, null, null, null, 0));
		put(players, new Player("Alexander Sørloth", "Norway", null, null, null, null, 0));
		put(players, new Player("Sander Berge", "Norway", null, null, null, null, 0));
		put(players, new Player("Martin Ødegaard", "Norway", null, null, null, null, 0));
		put(players, new Player("Jørgen Strand Larsen", "Norway", null, null, null, null, 0));
		put(players, new Player("Sander Tangvik", "Norway", null, null, null, null, 0));
		put(players, new Player("Egil Selvik", "Norway", null, null, null, null, 0));
		put(players, new Player("Fredrik Aursnes", "Norway", null, null, null, null, 0));
		put(players, new Player("Fredrik André Bjørkan", "Norway", null, null, null, null, 0));
		put(players, new Player("Marcus Holmgren Pedersen", "Norway", null, null, null, null, 0));
		put(players, new Player("Torbjørn Heggem", "Norway", null, null, null, null, 0));
		put(players, new Player("Kristian Thorstvedt", "Norway", null, null, null, null, 0));
		put(players, new Player("Thelo Aasgaard", "Norway", null, null, null, null, 0));
		put(players, new Player("Antonio Nusa", "Norway", null, null, null, null, 0));
		put(players, new Player("Andreas Schjelderup", "Norway", null, null, null, null, 0));
		put(players, new Player("Oscar Bobb", "Norway", null, null, null, null, 0));
		put(players, new Player("Jens Petter Hauge", "Norway", null, null, null, null, 0));
		put(players, new Player("Sondre Langås", "Norway", null, null, null, null, 0));
		put(players, new Player("Henrik Falchener", "Norway", null, null, null, null, 0));
		put(players, new Player("Julian Ryerson", "Norway", null, null, null, null, 0));
	}

	/**
	 * Group J: Argentina, Algeria, Austria, Jordan.
	 */
	private static void addGroupJ(Map<String, Player> players) {
		put(players, new Player("Juan Musso", "Argentina", null, null, null, null, 0));
		put(players, new Player("Marcos Senesi", "Argentina", null, null, null, null, 0));
		put(players, new Player("Nicolás Tagliafico", "Argentina", null, null, null, null, 0));
		put(players, new Player("Gonzalo Montiel", "Argentina", null, null, null, null, 0));
		put(players, new Player("Leandro Paredes", "Argentina", null, null, null, null, 0));
		put(players, new Player("Lisandro Martínez", "Argentina", null, null, null, null, 0));
		put(players, new Player("Rodrigo De Paul", "Argentina", null, null, null, null, 0));
		put(players, new Player("Valentín Barco", "Argentina", null, null, null, null, 0));
		put(players, new Player("Julián Alvarez", "Argentina", null, null, null, null, 0));
		put(players, new Player("Giovani Lo Celso", "Argentina", null, null, null, null, 0));
		put(players, new Player("Gerónimo Rulli", "Argentina", null, null, null, null, 0));
		put(players, new Player("Cristian Romero", "Argentina", null, null, null, null, 0));
		put(players, new Player("Exequiel Palacios", "Argentina", null, null, null, null, 0));
		put(players, new Player("Nicolás González", "Argentina", null, null, null, null, 0));
		put(players, new Player("Thiago Almada", "Argentina", null, null, null, null, 0));
		put(players, new Player("Giuliano Simeone", "Argentina", null, null, null, null, 0));
		put(players, new Player("Nico Paz", "Argentina", null, null, null, null, 0));
		put(players, new Player("Nicolás Otamendi", "Argentina", null, null, null, null, 0));
		put(players, new Player("Alexis Mac Allister", "Argentina", null, null, null, null, 0));
		put(players, new Player("José Manuel López", "Argentina", null, null, null, null, 0));
		put(players, new Player("Lautaro Martínez", "Argentina", null, null, null, null, 0));
		put(players, new Player("Emiliano Martínez", "Argentina", null, null, null, null, 0));
		put(players, new Player("Enzo Fernández", "Argentina", null, null, null, null, 0));
		put(players, new Player("Facundo Medina", "Argentina", null, null, null, null, 0));
		put(players, new Player("Nahuel Molina", "Argentina", null, null, null, null, 0));

		put(players, new Player("Melvin Mastil", "Algeria", null, null, null, null, 0));
		put(players, new Player("Aïssa Mandi", "Algeria", null, null, null, null, 0));
		put(players, new Player("Achref Abada", "Algeria", null, null, null, null, 0));
		put(players, new Player("Mohamed Amine Tougai", "Algeria", null, null, null, null, 0));
		put(players, new Player("Zineddine Belaïd", "Algeria", null, null, null, null, 0));
		put(players, new Player("Ramiz Zerrouki", "Algeria", null, null, null, null, 0));
		put(players, new Player("Houssem Aouar", "Algeria", null, null, null, null, 0));
		put(players, new Player("Amine Gouiri", "Algeria", null, null, null, null, 0));
		put(players, new Player("Farès Chaïbi", "Algeria", null, null, null, null, 0));
		put(players, new Player("Anis Hadj Moussa", "Algeria", null, null, null, null, 0));
		put(players, new Player("Nadhir Benbouali", "Algeria", null, null, null, null, 0));
		put(players, new Player("Jaouen Hadjam", "Algeria", null, null, null, null, 0));
		put(players, new Player("Hicham Boudaoui", "Algeria", null, null, null, null, 0));
		put(players, new Player("Rayan Aït-Nouri", "Algeria", null, null, null, null, 0));
		put(players, new Player("Oussama Benbot", "Algeria", null, null, null, null, 0));
		put(players, new Player("Rafik Belghali", "Algeria", null, null, null, null, 0));
		put(players, new Player("Mohamed Amoura", "Algeria", null, null, null, null, 0));
		put(players, new Player("Nabil Bentaleb", "Algeria", null, null, null, null, 0));
		put(players, new Player("Adil Boulbina", "Algeria", null, null, null, null, 0));
		put(players, new Player("Ramy Bensebaini", "Algeria", null, null, null, null, 0));
		put(players, new Player("Luca Zidane", "Algeria", null, null, null, null, 0));
		put(players, new Player("Yacine Titraoui", "Algeria", null, null, null, null, 0));
		put(players, new Player("Farès Ghedjemis", "Algeria", null, null, null, null, 0));
		put(players, new Player("Samir Chergui", "Algeria", null, null, null, null, 0));

		put(players, new Player("Alexander Schlager", "Austria", null, null, null, null, 0));
		put(players, new Player("David Affengruber", "Austria", null, null, null, null, 0));
		put(players, new Player("Kevin Danso", "Austria", null, null, null, null, 0));
		put(players, new Player("Xaver Schlager", "Austria", null, null, null, null, 0));
		put(players, new Player("Stefan Posch", "Austria", null, null, null, null, 0));
		put(players, new Player("Nicolas Seiwald", "Austria", null, null, null, null, 0));
		put(players, new Player("Marko Arnautović", "Austria", null, null, null, null, 0));
		put(players, new Player("David Alaba", "Austria", null, null, null, null, 0));
		put(players, new Player("Marcel Sabitzer", "Austria", null, null, null, null, 0));
		put(players, new Player("Florian Grillitsch", "Austria", null, null, null, null, 0));
		put(players, new Player("Michael Gregoritsch", "Austria", null, null, null, null, 0));
		put(players, new Player("Florian Wiegele", "Austria", null, null, null, null, 0));
		put(players, new Player("Patrick Pentz", "Austria", null, null, null, null, 0));
		put(players, new Player("Saša Kalajdžić", "Austria", null, null, null, null, 0));
		put(players, new Player("Philipp Lienhart", "Austria", null, null, null, null, 0));
		put(players, new Player("Phillipp Mwene", "Austria", null, null, null, null, 0));
		put(players, new Player("Carney Chukwuemeka", "Austria", null, null, null, null, 0));
		put(players, new Player("Romano Schmid", "Austria", null, null, null, null, 0));
		put(players, new Player("Dejan Ljubičić", "Austria", null, null, null, null, 0));
		put(players, new Player("Konrad Laimer", "Austria", null, null, null, null, 0));
		put(players, new Player("Patrick Wimmer", "Austria", null, null, null, null, 0));
		put(players, new Player("Alexander Prass", "Austria", null, null, null, null, 0));
		put(players, new Player("Marco Friedl", "Austria", null, null, null, null, 0));
		put(players, new Player("Paul Wanner", "Austria", null, null, null, null, 0));
		put(players, new Player("Michael Svoboda", "Austria", null, null, null, null, 0));
		put(players, new Player("Alessandro Schöpf", "Austria", null, null, null, null, 0));

		put(players, new Player("Yazeed Abulaila", "Jordan", null, null, null, null, 0));
		put(players, new Player("Mohammad Abu Hashish", "Jordan", null, null, null, null, 0));
		put(players, new Player("Abdallah Nasib", "Jordan", null, null, null, null, 0));
		put(players, new Player("Husam Abu Dahab", "Jordan", null, null, null, null, 0));
		put(players, new Player("Yazan Al-Arab", "Jordan", null, null, null, null, 0));
		put(players, new Player("Amer Jamous", "Jordan", null, null, null, null, 0));
		put(players, new Player("Mohammad Abu Zrayq", "Jordan", null, null, null, null, 0));
		put(players, new Player("Noor Al-Rawabdeh", "Jordan", null, null, null, null, 0));
		put(players, new Player("Musa Al-Taamari", "Jordan", null, null, null, null, 0));
		put(players, new Player("Odeh Al-Fakhouri", "Jordan", null, null, null, null, 0));
		put(players, new Player("Nour Bani Attiah", "Jordan", null, null, null, null, 0));
		put(players, new Player("Mahmoud Al-Mardi", "Jordan", null, null, null, null, 0));
		put(players, new Player("Rajaei Ayed", "Jordan", null, null, null, null, 0));
		put(players, new Player("Ibrahim Sadeh", "Jordan", null, null, null, null, 0));
		put(players, new Player("Mo Abualnadi", "Jordan", null, null, null, null, 0));
		put(players, new Player("Salim Obaid", "Jordan", null, null, null, null, 0));
		put(players, new Player("Mohammad Taha", "Jordan", null, null, null, null, 0));
		put(players, new Player("Saed Al-Rosan", "Jordan", null, null, null, null, 0));
		put(players, new Player("Mohannad Abu Taha", "Jordan", null, null, null, null, 0));
		put(players, new Player("Nizar Al-Rashdan", "Jordan", null, null, null, null, 0));
		put(players, new Player("Abdallah Al-Fakhouri", "Jordan", null, null, null, null, 0));
		put(players, new Player("Ihsan Haddad", "Jordan", null, null, null, null, 0));
		put(players, new Player("Ali Azaizeh", "Jordan", null, null, null, null, 0));
		put(players, new Player("Mohammad Al-Dawoud", "Jordan", null, null, null, null, 0));
		put(players, new Player("Anas Badawi", "Jordan", null, null, null, null, 0));
	}

	/**
	 * Group K: Portugal, DR Congo, Uzbekistan, Colombia.
	 */
	private static void addGroupK(Map<String, Player> players) {
		put(players, new Player("Nélson Semedo", "Portugal", null, null, null, null, 0));
		put(players, new Player("Rúben Dias", "Portugal", null, null, null, null, 0));
		put(players, new Player("Tomás Araújo", "Portugal", null, null, null, null, 0));
		put(players, new Player("Diogo Dalot", "Portugal", null, null, null, null, 0));
		put(players, new Player("Matheus Nunes", "Portugal", null, null, null, null, 0));
		put(players, new Player("Bruno Fernandes", "Portugal", null, null, null, null, 0));
		put(players, new Player("Gonçalo Ramos", "Portugal", null, null, null, null, 0));
		put(players, new Player("Bernardo Silva", "Portugal", null, null, null, null, 0));
		put(players, new Player("João Félix", "Portugal", null, null, null, null, 0));
		put(players, new Player("José Sá", "Portugal", null, null, null, null, 0));
		put(players, new Player("Renato Veiga", "Portugal", null, null, null, null, 0));
		put(players, new Player("Gonçalo Inácio", "Portugal", null, null, null, null, 0));
		put(players, new Player("Francisco Trincão", "Portugal", null, null, null, null, 0));
		put(players, new Player("Rafael Leão", "Portugal", null, null, null, null, 0));
		put(players, new Player("Pedro Neto", "Portugal", null, null, null, null, 0));
		put(players, new Player("Gonçalo Guedes", "Portugal", null, null, null, null, 0));
		put(players, new Player("João Cancelo", "Portugal", null, null, null, null, 0));
		put(players, new Player("Rúben Neves", "Portugal", null, null, null, null, 0));
		put(players, new Player("Rui Silva", "Portugal", null, null, null, null, 0));
		put(players, new Player("Vitinha", "Portugal", null, null, null, null, 0));
		put(players, new Player("Samú Costa", "Portugal", null, null, null, null, 0));
		put(players, new Player("Nuno Mendes", "Portugal", null, null, null, null, 0));
		put(players, new Player("Francisco Conceição", "Portugal", null, null, null, null, 0));
		// Diogo Jota died in a car accident in July 2025 and did not take part in the tournament;
		// Portugal's squad named a symbolic 27th "+1" spot in his memory.
		put(players, new Player("Diogo Jota", "Portugal", null, null, null, null, 0));

		put(players, new Player("Lionel Mpasi", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Aaron Wan-Bissaka", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Steve Kapuadi", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Axel Tuanzebe", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Dylan Batubinsika", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Ngal'ayel Mukau", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Nathanaël Mbuku", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Samuel Moutoussamy", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Brian Cipenga", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Théo Bongonda", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Gaël Kakuta", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Joris Kayembe", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Meschak Elia", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Noah Sadiki", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Aaron Tshibola", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Timothy Fayulu", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Cédric Bakambu", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Charles Pickel", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Fiston Mayele", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Yoane Wissa", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Matthieu Epolo", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Chancel Mbemba", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Simon Banza", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Gédéon Kalulu", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Edo Kayembe", "DR Congo", null, null, null, null, 0));
		put(players, new Player("Arthur Masuaku", "DR Congo", null, null, null, null, 0));

		put(players, new Player("Utkir Yusupov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Abdukodir Khusanov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Khojiakbar Alijonov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Farrukh Sayfiev", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Rustam Ashurmatov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Akmal Mozgovoy", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Otabek Shukurov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Jamshid Iskanderov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Odiljon Hamrobekov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Ruslanbek Jiyanov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Oston Urunov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Abduvohid Nematov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Sherzod Nasrullaev", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Eldor Shomurodov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Umar Eshmurodov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Botirali Ergashev", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Dostonbek Khamdamov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Abdulla Abdullaev", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Azizjon Ganiev", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Azizbek Amonov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Igor Sergeev", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Abbosbek Fayzullaev", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Sherzod Esanov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Bekhruz Karimov", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Avazbek Ulmasaliev", "Uzbekistan", null, null, null, null, 0));
		put(players, new Player("Jakhongir Urozov", "Uzbekistan", null, null, null, null, 0));

		put(players, new Player("David Ospina", "Colombia", null, null, null, null, 0));
		put(players, new Player("Jhon Lucumí", "Colombia", null, null, null, null, 0));
		put(players, new Player("Santiago Arias", "Colombia", null, null, null, null, 0));
		put(players, new Player("Kevin Castaño", "Colombia", null, null, null, null, 0));
		put(players, new Player("Richard Ríos", "Colombia", null, null, null, null, 0));
		put(players, new Player("Jorge Carrascal", "Colombia", null, null, null, null, 0));
		put(players, new Player("Jhon Córdoba", "Colombia", null, null, null, null, 0));
		put(players, new Player("James Rodríguez", "Colombia", null, null, null, null, 0));
		put(players, new Player("Jhon Arias", "Colombia", null, null, null, null, 0));
		put(players, new Player("Camilo Vargas", "Colombia", null, null, null, null, 0));
		put(players, new Player("Yerry Mina", "Colombia", null, null, null, null, 0));
		put(players, new Player("Gustavo Puerta", "Colombia", null, null, null, null, 0));
		put(players, new Player("Juan Portilla", "Colombia", null, null, null, null, 0));
		put(players, new Player("Jefferson Lerma", "Colombia", null, null, null, null, 0));
		put(players, new Player("Johan Mojica", "Colombia", null, null, null, null, 0));
		put(players, new Player("Willer Ditta", "Colombia", null, null, null, null, 0));
		put(players, new Player("Cucho Hernández", "Colombia", null, null, null, null, 0));
		put(players, new Player("Juan Fernando Quintero", "Colombia", null, null, null, null, 0));
		put(players, new Player("Jaminton Campaz", "Colombia", null, null, null, null, 0));
		put(players, new Player("Deiver Machado", "Colombia", null, null, null, null, 0));
		put(players, new Player("Davinson Sánchez", "Colombia", null, null, null, null, 0));
		put(players, new Player("Álvaro Montero", "Colombia", null, null, null, null, 0));
		put(players, new Player("Luis Suárez", "Colombia", null, null, null, null, 0));
		put(players, new Player("Andrés Gómez", "Colombia", null, null, null, null, 0));
	}

	/**
	 * Group L: England, Croatia, Ghana, Panama.
	 */
	private static void addGroupL(Map<String, Player> players) {
		put(players, new Player("Jordan Pickford", "England", null, null, null, null, 0));
		put(players, new Player("Ezri Konsa", "England", null, null, null, null, 0));
		put(players, new Player("Nico O'Reilly", "England", null, null, null, null, 0));
		put(players, new Player("Declan Rice", "England", null, null, null, null, 0));
		put(players, new Player("John Stones", "England", null, null, null, null, 0));
		put(players, new Player("Marc Guéhi", "England", null, null, null, null, 0));
		put(players, new Player("Bukayo Saka", "England", null, null, null, null, 0));
		put(players, new Player("Elliot Anderson", "England", null, null, null, null, 0));
		put(players, new Player("Marcus Rashford", "England", null, null, null, null, 0));
		put(players, new Player("Trevoh Chalobah", "England", null, null, null, null, 0));
		put(players, new Player("Dean Henderson", "England", null, null, null, null, 0));
		put(players, new Player("Jordan Henderson", "England", null, null, null, null, 0));
		put(players, new Player("Dan Burn", "England", null, null, null, null, 0));
		put(players, new Player("Kobbie Mainoo", "England", null, null, null, null, 0));
		put(players, new Player("Morgan Rogers", "England", null, null, null, null, 0));
		put(players, new Player("Anthony Gordon", "England", null, null, null, null, 0));
		put(players, new Player("Ollie Watkins", "England", null, null, null, null, 0));
		put(players, new Player("Noni Madueke", "England", null, null, null, null, 0));
		put(players, new Player("Eberechi Eze", "England", null, null, null, null, 0));
		put(players, new Player("Ivan Toney", "England", null, null, null, null, 0));
		put(players, new Player("James Trafford", "England", null, null, null, null, 0));
		put(players, new Player("Reece James", "England", null, null, null, null, 0));
		put(players, new Player("Djed Spence", "England", null, null, null, null, 0));
		put(players, new Player("Jarell Quansah", "England", null, null, null, null, 0));

		put(players, new Player("Dominik Livaković", "Croatia", null, null, null, null, 0));
		put(players, new Player("Josip Stanišić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Marin Pongračić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Joško Gvardiol", "Croatia", null, null, null, null, 0));
		put(players, new Player("Duje Ćaleta-Car", "Croatia", null, null, null, null, 0));
		put(players, new Player("Josip Šutalo", "Croatia", null, null, null, null, 0));
		put(players, new Player("Nikola Moro", "Croatia", null, null, null, null, 0));
		put(players, new Player("Mateo Kovačić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Andrej Kramarić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Luka Modrić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Ante Budimir", "Croatia", null, null, null, null, 0));
		put(players, new Player("Ivor Pandur", "Croatia", null, null, null, null, 0));
		put(players, new Player("Nikola Vlašić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Ivan Perišić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Mario Pašalić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Martin Baturina", "Croatia", null, null, null, null, 0));
		put(players, new Player("Kristijan Jakić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Toni Fruk", "Croatia", null, null, null, null, 0));
		put(players, new Player("Igor Matanović", "Croatia", null, null, null, null, 0));
		put(players, new Player("Luka Sučić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Luka Vušković", "Croatia", null, null, null, null, 0));
		put(players, new Player("Dominik Kotarski", "Croatia", null, null, null, null, 0));
		put(players, new Player("Marco Pašalić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Martin Erlić", "Croatia", null, null, null, null, 0));
		put(players, new Player("Petar Musa", "Croatia", null, null, null, null, 0));

		put(players, new Player("Lawrence Ati-Zigi", "Ghana", null, null, null, null, 0));
		put(players, new Player("Alidu Seidu", "Ghana", null, null, null, null, 0));
		put(players, new Player("Caleb Yirenkyi", "Ghana", null, null, null, null, 0));
		put(players, new Player("Jonas Adjetey", "Ghana", null, null, null, null, 0));
		put(players, new Player("Thomas Partey", "Ghana", null, null, null, null, 0));
		put(players, new Player("Abdul Mumin", "Ghana", null, null, null, null, 0));
		put(players, new Player("Abdul Fatawu", "Ghana", null, null, null, null, 0));
		put(players, new Player("Kwasi Sibo", "Ghana", null, null, null, null, 0));
		put(players, new Player("Jordan Ayew", "Ghana", null, null, null, null, 0));
		put(players, new Player("Brandon Thomas-Asante", "Ghana", null, null, null, null, 0));
		put(players, new Player("Joseph Anang", "Ghana", null, null, null, null, 0));
		put(players, new Player("Christopher Bonsu Baah", "Ghana", null, null, null, null, 0));
		put(players, new Player("Gideon Mensah", "Ghana", null, null, null, null, 0));
		put(players, new Player("Elisha Owusu", "Ghana", null, null, null, null, 0));
		put(players, new Player("Benjamin Asare", "Ghana", null, null, null, null, 0));
		put(players, new Player("Abdul Rahman Baba", "Ghana", null, null, null, null, 0));
		put(players, new Player("Jerome Opoku", "Ghana", null, null, null, null, 0));
		put(players, new Player("Iñaki Williams", "Ghana", null, null, null, null, 0));
		put(players, new Player("Augustine Boakye", "Ghana", null, null, null, null, 0));
		put(players, new Player("Kojo Peprah Oppong", "Ghana", null, null, null, null, 0));
		put(players, new Player("Kamaldeen Sulemana", "Ghana", null, null, null, null, 0));
		put(players, new Player("Derrick Luckassen", "Ghana", null, null, null, null, 0));
		put(players, new Player("Ernest Nuamah", "Ghana", null, null, null, null, 0));
		put(players, new Player("Prince Kwabena Adu", "Ghana", null, null, null, null, 0));
		put(players, new Player("Marvin Senaya", "Ghana", null, null, null, null, 0));

		put(players, new Player("Luis Mejía", "Panama", null, null, null, null, 0));
		put(players, new Player("César Blackman", "Panama", null, null, null, null, 0));
		put(players, new Player("José Córdoba", "Panama", null, null, null, null, 0));
		put(players, new Player("Fidel Escobar", "Panama", null, null, null, null, 0));
		put(players, new Player("Edgardo Fariña", "Panama", null, null, null, null, 0));
		put(players, new Player("José Luis Rodríguez", "Panama", null, null, null, null, 0));
		put(players, new Player("Adalberto Carrasquilla", "Panama", null, null, null, null, 0));
		put(players, new Player("Tomás Rodríguez", "Panama", null, null, null, null, 0));
		put(players, new Player("Ismael Díaz", "Panama", null, null, null, null, 0));
		put(players, new Player("Yoel Bárcenas", "Panama", null, null, null, null, 0));
		put(players, new Player("César Samudio", "Panama", null, null, null, null, 0));
		put(players, new Player("Jiovany Ramos", "Panama", null, null, null, null, 0));
		put(players, new Player("Carlos Harvey", "Panama", null, null, null, null, 0));
		put(players, new Player("Eric Davis", "Panama", null, null, null, null, 0));
		put(players, new Player("Andrés Andrade", "Panama", null, null, null, null, 0));
		put(players, new Player("José Fajardo", "Panama", null, null, null, null, 0));
		put(players, new Player("Cecilio Waterman", "Panama", null, null, null, null, 0));
		put(players, new Player("Alberto Quintero", "Panama", null, null, null, null, 0));
		put(players, new Player("Aníbal Godoy", "Panama", null, null, null, null, 0));
		put(players, new Player("César Yanis", "Panama", null, null, null, null, 0));
		put(players, new Player("Orlando Mosquera", "Panama", null, null, null, null, 0));
		put(players, new Player("Michael Amir Murillo", "Panama", null, null, null, null, 0));
		put(players, new Player("Azarias Londoño", "Panama", null, null, null, null, 0));
		put(players, new Player("Roderick Miller", "Panama", null, null, null, null, 0));
		put(players, new Player("Jorge Gutiérrez", "Panama", null, null, null, null, 0));
	}

	public Player getPlayer(final String name) {
		return PLAYERS.getOrDefault(name.toLowerCase().trim(), new Player(name, "Unknown", null, null, null, null, 0));
	}

	public Collection<Player> playersOf(final String team) {
		return PLAYERS.values().stream().filter(p -> p.country().equalsIgnoreCase(team.trim())).toList();
	}

	public Collection<Player> getAllPlayers() {
		return PLAYERS.values();
	}
}
