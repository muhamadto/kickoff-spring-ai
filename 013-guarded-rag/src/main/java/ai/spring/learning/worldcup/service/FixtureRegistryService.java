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

package ai.spring.learning.worldcup.service;

import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Deterministic, no LLM call: a small, known-real subset of the World Cup 2026 group stage, enough to validate a booking request against. A real
 * system would check this against the same source of truth the knowledge base was generated from; this tutorial hardcodes a handful of fixtures that
 * are genuinely in {@code 010-embedding}'s knowledge base rather than invent placeholder data.
 */
@Component
public class FixtureRegistryService {

	private static final Set<String> KNOWN_FIXTURES =
			Set.of("morocco vs haiti", "mexico vs south africa", "south korea vs czech republic", "brazil vs morocco", "united states vs paraguay");

	private static String normalise(final String homeTeam, final String awayTeam) {
		return "%s vs %s".formatted(homeTeam, awayTeam).toLowerCase(Locale.ROOT);
	}

	public boolean exists(final String homeTeam, final String awayTeam) {
		if (homeTeam == null || awayTeam == null) {
			return false;
		}
		return KNOWN_FIXTURES.contains(normalise(homeTeam, awayTeam)) || KNOWN_FIXTURES.contains(normalise(awayTeam, homeTeam));
	}
}
