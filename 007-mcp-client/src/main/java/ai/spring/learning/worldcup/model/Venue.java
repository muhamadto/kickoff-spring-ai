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

public record Venue(
		@JsonPropertyDescription("The stadium name, e.g., 'Estadio Azteca'")
		String name,

		@JsonPropertyDescription("The host city, e.g., 'Mexico City'")
		String city,

		@JsonPropertyDescription("The host country, e.g., 'Mexico'")
		String country,

		@JsonPropertyDescription("Seating capacity, e.g., 87000")
		int capacity
) {

}
