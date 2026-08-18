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

import ai.spring.learning.worldcup.model.BookingRequest;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The three checks {@code 012-agentic-rag}'s {@code BookingTool} never made, all deterministic, all running before anything gets recorded: are the
 * required fields present, is the fixture real, and has this exact booking already been made in this conversation. None of these need a model's
 * judgement; the answer to each is already knowable from data the code has.
 */
@Component
public class BookingService {

	private static final Logger log = LoggerFactory.getLogger(BookingService.class);

	private final FixtureRegistryService fixtureRegistryService;
	private final Map<String, Set<String>> bookedFixturesByConversation = new ConcurrentHashMap<>();

	public BookingService(final FixtureRegistryService fixtureRegistryService) {
		this.fixtureRegistryService = fixtureRegistryService;
	}

	public String book(final String conversationId, final BookingRequest request) {
		if (request.homeTeam() == null || request.awayTeam() == null || request.date() == null || request.quantity() == null) {
			return "I need both teams, the date and how many tickets before I can book this.";
		}

		if (request.quantity() <= 0) {
			return "I need a valid, positive number of tickets before I can book this.";
		}

		if (!fixtureRegistryService.exists(request.homeTeam(), request.awayTeam())) {
			return "I couldn't find a World Cup 2026 fixture between %s and %s.".formatted(request.homeTeam(), request.awayTeam());
		}

		final String fixtureKey = (Stream.of(request.homeTeam(), request.awayTeam()).sorted().collect(Collectors.joining(" vs ")) + " on "
				+ request.date()).toLowerCase(Locale.ROOT);
		final Set<String> bookedInThisConversation = bookedFixturesByConversation.computeIfAbsent(conversationId, id -> ConcurrentHashMap.newKeySet());
		if (!bookedInThisConversation.add(fixtureKey)) {
			return "You've already booked %s vs %s on %s in this conversation.".formatted(request.homeTeam(), request.awayTeam(), request.date());
		}

		final String reference = "WC26-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
		log
				.info("Booking recorded: {} ticket(s) for {} vs {} on {}, reference {}",
						request.quantity(),
						request.homeTeam(),
						request.awayTeam(),
						request.date(),
						reference);
		return "Booked %d ticket(s) for %s vs %s on %s. Your booking reference is %s."
				.formatted(request.quantity(), request.homeTeam(), request.awayTeam(), request.date(), reference);
	}
}
