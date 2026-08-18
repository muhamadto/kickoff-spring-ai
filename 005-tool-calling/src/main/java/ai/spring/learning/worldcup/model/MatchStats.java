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

public record MatchStats(
		@JsonPropertyDescription("The fixture as 'Home vs Away', e.g., 'Morocco vs Haiti'")
		String fixture,

		@JsonPropertyDescription("The Man of the Match, e.g., 'Kylian Mbappé'. Empty if not recorded.")
		String manOfTheMatch,

		@JsonPropertyDescription("Home team's yellow cards. Null if not yet verified.")
		Integer homeYellowCards,

		@JsonPropertyDescription("Home team's red cards. Null if not yet verified.")
		Integer homeRedCards,

		@JsonPropertyDescription("Away team's yellow cards. Null if not yet verified.")
		Integer awayYellowCards,

		@JsonPropertyDescription("Away team's red cards. Null if not yet verified.")
		Integer awayRedCards,

		@JsonPropertyDescription("Home team's total shots. Null if not yet verified.")
		Integer homeShots,

		@JsonPropertyDescription("Home team's shots on target. Null if not yet verified.")
		Integer homeShotsOnTarget,

		@JsonPropertyDescription("Away team's total shots. Null if not yet verified.")
		Integer awayShots,

		@JsonPropertyDescription("Away team's shots on target. Null if not yet verified.")
		Integer awayShotsOnTarget
) {

}
