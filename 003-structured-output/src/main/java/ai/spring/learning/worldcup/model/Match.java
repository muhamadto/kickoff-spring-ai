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

package ai.spring.learning.worldcup.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonPropertyOrder({
		"stage",
		"fixture",
		"venue",
		"keyPlayers",
		"watchFor"
})
public record Match(
		@JsonPropertyDescription("The tournament stage of the match, e.g., 'Group Stage', 'Round of 32', 'Quarter-final'")
		String stage,

		@JsonPropertyDescription("The fixture as 'Home vs Away', e.g., 'Morocco vs Spain'")
		String fixture,

		@JsonPropertyDescription("The stadium and host city, e.g., 'Estadio Azteca, Mexico City'")
		String venue,

		@JsonPropertyDescription("The key players to watch in this match, e.g., ['Achraf Hakimi', 'Lamine Yamal']")
		List<String> keyPlayers,

		@JsonPropertyDescription("Storylines or tactical points to watch for, e.g., ['High press vs build-up play', 'Rematch of the 2022 round-of-16 penalty shootout']")
		List<String> watchFor
) {

}
