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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Real 2026 FIFA World Cup group draw data, verified against public sources.
 */
@Component
public class TeamData {

	private static final Map<String, List<String>> GROUPS = new LinkedHashMap<>();

	static {
		GROUPS.put("A", List.of("Mexico", "South Africa", "South Korea", "Czech Republic"));
		GROUPS.put("B", List.of("Canada", "Bosnia and Herzegovina", "Qatar", "Switzerland"));
		GROUPS.put("C", List.of("Brazil", "Morocco", "Haiti", "Scotland"));
		GROUPS.put("D", List.of("United States", "Paraguay", "Australia", "Türkiye"));
		GROUPS.put("E", List.of("Germany", "Curaçao", "Ivory Coast", "Ecuador"));
		GROUPS.put("F", List.of("Netherlands", "Japan", "Sweden", "Tunisia"));
		GROUPS.put("G", List.of("Belgium", "Egypt", "Iran", "New Zealand"));
		GROUPS.put("H", List.of("Spain", "Cape Verde", "Saudi Arabia", "Uruguay"));
		GROUPS.put("I", List.of("France", "Senegal", "Iraq", "Norway"));
		GROUPS.put("J", List.of("Argentina", "Algeria", "Austria", "Jordan"));
		GROUPS.put("K", List.of("Portugal", "DR Congo", "Uzbekistan", "Colombia"));
		GROUPS.put("L", List.of("England", "Croatia", "Ghana", "Panama"));
	}

	public List<String> teamsIn(final String group) {
		if (group == null || group.isBlank()) {
			return GROUPS.values().stream().flatMap(List::stream).toList();
		}
		return GROUPS.getOrDefault(group.toUpperCase().trim(), List.of());
	}

	public List<String> groups() {
		return List.copyOf(GROUPS.keySet());
	}
}
