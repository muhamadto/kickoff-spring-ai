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

package ai.spring.learning.worldcup;

import ai.spring.learning.worldcup.advisor.PiiRedactionAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions.Builder;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAiConfig {

	/**
	 * No booking tool here, unlike 012-agentic-rag: whether to book is decided by {@code Worldcup2026Service} before this client is ever called, so
	 * this client only ever sees a question that has already been judged not to be a booking request.
	 */
	private static ChatClient.Builder enrichChatClientBuilder(final ChatClient.Builder builder,
			final QuestionAnswerAdvisor questionAnswerAdvisor,
			final MessageChatMemoryAdvisor messageChatMemoryAdvisor) {
		return builder.defaultAdvisors(new PiiRedactionAdvisor(10),
				messageChatMemoryAdvisor, // order 20
				questionAnswerAdvisor, // order 25
				new SimpleLoggerAdvisor(30));
	}

	private static Builder getGeminiChatOptions(final String model) {
		return GoogleGenAiChatOptions.builder().model(model);
	}

	/**
	 * The pro model runs the final answer: {@code Worldcup2026Service} uses this client for that last step, after step-back retrieval has already run.
	 */
	@Bean
	public ChatClient mainChatClient(final ChatClient.Builder builder,
			@Value("${spring.ai.google.genai.chat.model}") final String model,
			final QuestionAnswerAdvisor questionAnswerAdvisor,
			final MessageChatMemoryAdvisor messageChatMemoryAdvisor) {
		return enrichChatClientBuilder(builder, questionAnswerAdvisor, messageChatMemoryAdvisor).defaultOptions(getGeminiChatOptions(model)).build();
	}

	/**
	 * Deliberately bare: no {@link PiiRedactionAdvisor}, no memory, no {@link SimpleLoggerAdvisor}. None of the classification or extraction calls that
	 * run before generation are part of the fan's conversation, so none of them should be written to it. Shared by {@code MessageClassificationService}
	 * and {@code Worldcup2026Service}'s extraction call, each supplying its own system prompt per call. Kept on the flash-lite model since both are
	 * cheap: classification runs before every request, extraction only for the booking-request subset.
	 */
	@Bean
	public ChatClient classificationChatClient(final ChatClient.Builder builder,
			@Value("${spring.ai.google.genai.chat.flash-lite-model}") final String model) {
		return builder.defaultOptions(getGeminiChatOptions(model)).build();
	}

	/**
	 * Also bare, also no advisors: a step-back rewrite isn't part of the fan's conversation either. Flash, not flash-lite: rewriting a question into a
	 * genuinely useful broader one benefits from more capability than the one-word classification and extraction calls need.
	 */
	@Bean
	public ChatClient stepBackChatClient(final ChatClient.Builder builder, @Value("${spring.ai.google.genai.chat.flash-model}") final String model) {
		return builder.defaultOptions(getGeminiChatOptions(model)).build();
	}

	/**
	 * Order 20: after {@link PiiRedactionAdvisor} (10) so memory never stores raw PII, same fix carried over from 010-vector-store-rag,
	 * 011-hybrid-search-rag and 012-agentic-rag.
	 */
	@Bean
	public MessageChatMemoryAdvisor messageChatMemoryAdvisor(final ChatMemoryRepository chatMemoryRepository) {
		final var chatMemory = MessageWindowChatMemory.builder().chatMemoryRepository(chatMemoryRepository).maxMessages(20).build();

		return MessageChatMemoryAdvisor.builder(chatMemory).order(20).build();
	}

	/**
	 * Order 25: back to {@code 010-vector-store-rag}'s plain dense retrieval rather than {@code 011-hybrid-search-rag}'s hand-rolled fusion, dropped
	 * from {@code 012-agentic-rag} onward. This module adds a second, separate retrieval (step-back search, in {@code query.StepBackSearchService})
	 * rather than touching this advisor at all: it keeps retrieving against the fan's raw question, regardless of what else the pipeline does around
	 * it.
	 */
	@Bean
	public QuestionAnswerAdvisor questionAnswerAdvisor(final VectorStore vectorStore) {
		return QuestionAnswerAdvisor.builder(vectorStore).order(25).build();
	}
}
