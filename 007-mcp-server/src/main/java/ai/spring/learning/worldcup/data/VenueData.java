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

import ai.spring.learning.worldcup.model.Venue;
import java.util.Collection;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Real 2026 FIFA World Cup venue data, verified against public sources.
 */
@Component
public class VenueData {

	private static final Map<String, Venue> VENUES =
			Map.ofEntries(Map.entry("mexico city", new Venue("Estadio Azteca", "Mexico City", "Mexico", 80824)),
					Map.entry("new york", new Venue("MetLife Stadium", "New York", "USA", 80663)),
					Map.entry("dallas", new Venue("AT&T Stadium", "Dallas", "USA", 70649)),
					Map.entry("los angeles", new Venue("SoFi Stadium", "Los Angeles", "USA", 70492)),
					Map.entry("kansas city", new Venue("Arrowhead Stadium", "Kansas City", "USA", 69045)),
					Map.entry("san francisco", new Venue("Levi's Stadium", "San Francisco Bay Area", "USA", 68827)),
					Map.entry("houston", new Venue("NRG Stadium", "Houston", "USA", 68777)),
					Map.entry("philadelphia", new Venue("Lincoln Financial Field", "Philadelphia", "USA", 68324)),
					Map.entry("atlanta", new Venue("Mercedes-Benz Stadium", "Atlanta", "USA", 68239)),
					Map.entry("seattle", new Venue("Lumen Field", "Seattle", "USA", 66925)),
					Map.entry("miami", new Venue("Hard Rock Stadium", "Miami", "USA", 64478)),
					Map.entry("boston", new Venue("Gillette Stadium", "Boston", "USA", 64146)),
					Map.entry("vancouver", new Venue("BC Place", "Vancouver", "Canada", 52497)),
					Map.entry("monterrey", new Venue("Estadio BBVA", "Monterrey", "Mexico", 51243)),
					Map.entry("guadalajara", new Venue("Estadio Akron", "Guadalajara", "Mexico", 45664)),
					Map.entry("toronto", new Venue("BMO Field", "Toronto", "Canada", 43036)));

	public Venue venueIn(final String city) {
		return VENUES.getOrDefault(city.toLowerCase().trim(), new Venue("Unknown", city, "Unknown", 0));
	}

	public Collection<Venue> venues() {
		return VENUES.values();
	}
}
