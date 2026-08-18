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
import ai.spring.learning.worldcup.model.MessageClassification;
import ai.spring.learning.worldcup.repository.ChatHistoryRepository;
import ai.spring.learning.worldcup.service.query.StepBackQueryService;
import ai.spring.learning.worldcup.service.query.StepBackSearchService;
import java.util.Optional;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

/**
 * The control flow {@code 012-agentic-rag} left to the model: whether a message is even worth processing, whether it's a genuine booking request, and
 * whether that booking is actually valid, are all decided here, in plain Java, before {@code BookingService} ever records anything or generation ever
 * runs. {@link MessageClassificationService} decides both scope and booking intent in a single call: only {@link MessageClassification#IN_SCOPE}
 * reaches the step-back-augmented RAG chat, and only {@link MessageClassification#BOOKING_REQUEST} reaches the booking path.
 *
 * <p>
 * What's new in this module is what happens to a message that reaches the general chat path: {@code StepBackQueryService} generates a broader version
 * of the question, {@code StepBackSearchService} retrieves against it, and the result is injected into the system prompt, not the user message, so
 * {@code QuestionAnswerAdvisor}'s own retrieval (still bound to the fan's raw question) is completely unaffected. Both retrieval passes reach
 * {@code mainChatClient}; neither one knows the other exists.
 *
 * <p>
 * {@code chatHistoryRepository} records every request and answer, regardless of which branch produced it: {@code chat_history} is an audit log of
 * what was asked and how the system responded, not only of the requests that reached generation.
 */
@Service
public class Worldcup2026Service {

	private static final String RELATED_BUT_OUT_OF_SCOPE_MESSAGE =
			"I can answer World Cup 2026 questions and book match tickets, but I can't help with that.";
	private static final String NOT_RELATED_MESSAGE =
			"I can only help with FIFA World Cup 2026 questions and match ticket bookings. Ask me something about the tournament.";
	private static final String PROBABLE_SECURITY_PROBLEM_MESSAGE = "I can't help with that request.";

	private static final String BASE_SYSTEM_PROMPT = """
			You are a helpful and enthusiastic AI assistant specialized in the FIFA World Cup 2026,
			hosted across the USA, Canada and Mexico. Provide accurate, engaging advice for fans
			following the tournament while remaining professional. Answer only from the context
			provided to you; say so plainly if the context does not cover the question.
			""";

	private final ChatClient mainChatClient;
	private final ChatClient classificationChatClient;
	private final MessageClassificationService messageClassificationService;
	private final BookingService bookingService;
	private final StepBackQueryService stepBackQueryService;
	private final StepBackSearchService stepBackSearchService;
	private final ChatHistoryRepository chatHistoryRepository;

	public Worldcup2026Service(final ChatClient mainChatClient,
			final ChatClient classificationChatClient,
			final MessageClassificationService messageClassificationService,
			final BookingService bookingService,
			final StepBackQueryService stepBackQueryService,
			final StepBackSearchService stepBackSearchService,
			final ChatHistoryRepository chatHistoryRepository) {
		this.mainChatClient = mainChatClient;
		this.classificationChatClient = classificationChatClient;
		this.messageClassificationService = messageClassificationService;
		this.bookingService = bookingService;
		this.stepBackQueryService = stepBackQueryService;
		this.stepBackSearchService = stepBackSearchService;
		this.chatHistoryRepository = chatHistoryRepository;
	}

	private static String systemPrompt(final String stepBackQuery, final Optional<String> stepBackContext) {
		return stepBackContext.map(s -> BASE_SYSTEM_PROMPT + """
				
				Additional background, retrieved using a broader version of the question ("%s"):
				---------------------
				%s
				---------------------
				""".formatted(stepBackQuery, s)).orElse(BASE_SYSTEM_PROMPT);
	}

	public String chat(final String question, final String conversationId) {
		final String answer = respond(question, conversationId);
		chatHistoryRepository.record(conversationId, "user", question);
		chatHistoryRepository.record(conversationId, "assistant", answer);
		return answer;
	}

	private String respond(final String question, final String conversationId) {
		return switch (messageClassificationService.classify(question)) {
			case RELATED_BUT_OUT_OF_SCOPE -> RELATED_BUT_OUT_OF_SCOPE_MESSAGE;
			case NOT_RELATED -> NOT_RELATED_MESSAGE;
			case PROBABLE_SECURITY_PROBLEM -> PROBABLE_SECURITY_PROBLEM_MESSAGE;
			case BOOKING_REQUEST -> bookingService.book(conversationId, extractBookingRequest(question));
			case IN_SCOPE -> respondInScope(question, conversationId);
		};
	}

	private String respondInScope(final String question, final String conversationId) {
		final String stepBackQuery = stepBackQueryService.generateStepBackQuery(question);
		final Optional<String> stepBackContext = stepBackSearchService.search(stepBackQuery);

		return mainChatClient.prompt()
				.system(systemPrompt(stepBackQuery, stepBackContext))
				.user(question)
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
				.call()
				.content();
	}

	private BookingRequest extractBookingRequest(final String question) {
		return classificationChatClient.prompt().system("""
				Extract a World Cup 2026 ticket booking request from the fan's message: the home
				team, the away team, the match date and the number of tickets. Use null for any
				field the message doesn't clearly state; never guess a value.
				""").user(question).call().entity(BookingRequest.class);
	}
}
