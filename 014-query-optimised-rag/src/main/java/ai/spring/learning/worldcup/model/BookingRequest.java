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

import ai.spring.learning.worldcup.service.BookingService;

/**
 * Extracted from the fan's message via structured output, not hand-parsed. Fields are nullable on purpose: a field the fan's message didn't clearly
 * state comes back {@code null} rather than guessed, so {@link BookingService} can tell "missing" apart from "provided."
 */
public record BookingRequest(String homeTeam, String awayTeam, String date, Integer quantity) {

}
