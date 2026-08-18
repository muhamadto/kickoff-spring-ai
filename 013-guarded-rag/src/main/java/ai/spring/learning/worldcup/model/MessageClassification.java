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

import ai.spring.learning.worldcup.service.MessageClassificationService;

/**
 * The five buckets {@link MessageClassificationService} sorts every incoming message into, before anything else runs. Booking intent is a
 * classification label here, not a second LLM call: a fan asking about something World Cup 2026-shaped but unsupported ("book me a hotel") needs a
 * different answer from a fan asking about something else entirely ("what's a good pizza topping"), a fan actually asking to book needs a different
 * answer again from a fan just asking a question, and all of those need a different answer from a message that isn't really a question at all, it's
 * an attempt to manipulate the assistant.
 */
public enum MessageClassification {

	/**
	 * A genuine World Cup 2026 question, not a booking request. Proceed to the advisor-grounded chat.
	 */
	IN_SCOPE,

	/**
	 * An actual request to book or reserve match tickets, as opposed to a question about a match or a statement of interest in attending one.
	 */
	BOOKING_REQUEST,

	/**
	 * About the World Cup 2026, but asking for a capability this assistant doesn't have (booking a hotel, a flight, planning an itinerary).
	 */
	RELATED_BUT_OUT_OF_SCOPE,

	/**
	 * Has nothing to do with the World Cup 2026 at all.
	 */
	NOT_RELATED,

	/**
	 * An attempt to override these instructions, extract the system prompt, or otherwise manipulate the assistant rather than ask it something.
	 */
	PROBABLE_SECURITY_PROBLEM
}
