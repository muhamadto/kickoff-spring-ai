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

public record Fixture(
		@JsonPropertyDescription("The tournament stage, e.g., 'Group Stage', 'Round of 32'")
		String stage,

		@JsonPropertyDescription("The fixture as 'Home vs Away', e.g., 'Morocco vs Spain'")
		String fixture,

		@JsonPropertyDescription("The date of the match, e.g., '15 July 2026'")
		String date,

		@JsonPropertyDescription("The kickoff time, e.g., '18:00 local'")
		String kickoff,

		@JsonPropertyDescription("The stadium name, e.g., 'AT&T Stadium'")
		String venue,

		@JsonPropertyDescription("The head referee officiating the match")
		Referee referee
) {

}
