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

@JsonPropertyOrder({
		"name",
		"country",
		"goals",
		"assists",
		"yellowCards",
		"redCards",
		"manOfTheMatchAwards"
})
public record Player(
		@JsonPropertyDescription("The player's full name, e.g., 'Kylian Mbappé'")
		String name,

		@JsonPropertyDescription("The player's country, e.g., 'France'")
		String country,

		@JsonPropertyDescription("Goals scored during the group stage. Null where not yet verified.")
		Integer goals,

		@JsonPropertyDescription("Assists provided during the group stage. Null where not yet verified.")
		Integer assists,

		@JsonPropertyDescription("Yellow cards received during the group stage. Null where not yet verified.")
		Integer yellowCards,

		@JsonPropertyDescription("Red cards received during the group stage. Null where not yet verified.")
		Integer redCards,

		@JsonPropertyDescription("Number of Man of the Match awards received during the group stage")
		int manOfTheMatchAwards
) {

}
