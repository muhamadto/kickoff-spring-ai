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

package ai.spring.learning.worldcup.booking;

import java.util.Locale;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * The agentic decision this module actually illustrates: not whether to retrieve (retrieval is a fixed {@code QuestionAnswerAdvisor} that always
 * runs, dropping {@code 011-hybrid-search-rag}'s hand-rolled fusion), but whether to take a real-world action on the fan's behalf. Booking is
 * deliberately not a lookup: it has a side effect (recorded here as a log line, since there's no real ticketing system behind this tutorial), which
 * is a materially different kind of decision than anything {@code 005-tool-calling} or {@code 007-mcp-server} exposed as a tool.
 *
 * <p>
 * Deliberately unguarded, matching this module's own honesty about rough edges: nothing here checks that {@code homeTeam} and {@code awayTeam} name a
 * real fixture, nothing parses or validates {@code date}, nothing stops the same booking being recorded twice, and nothing stops the model calling
 * this when the fan was only musing about a match rather than asking to book one. {@code 013-guarded-rag} is where those checks land.
 */
@Component
public class BookingTool {

	private static final Logger log = LoggerFactory.getLogger(BookingTool.class);

	@Tool(description = "Book World Cup 2026 match tickets for the fan. Only call this when the fan has clearly asked to book or attend a "
			+ "specific fixture and you know both teams; do not call it for a general question about a match, only an actual booking request.")
	public String bookMatchTicket(@ToolParam(description = "The home team name") final String homeTeam,
			@ToolParam(description = "The away team name") final String awayTeam,
			@ToolParam(description = "The match date, however the fan phrased it") final String date,
			@ToolParam(description = "Number of tickets") final int quantity) {
		final String reference = "WC26-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
		log.info("Booking recorded: {} ticket(s) for {} vs {} on {}, reference {}. I'm using your stored credit card \uD83D\uDC7B",
				quantity,
				homeTeam,
				awayTeam,
				date,
				reference);

		return "Booked %d ticket(s) for %s vs %s on %s. Your booking reference is %s.".formatted(quantity, homeTeam, awayTeam, date, reference);
	}
}
