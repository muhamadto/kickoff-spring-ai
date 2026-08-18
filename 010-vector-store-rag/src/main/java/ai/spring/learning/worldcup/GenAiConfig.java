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
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions.Builder;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAiConfig {

	private static ChatClient.Builder enrichChatClientBuilder(final ChatClient.Builder builder,
			final QuestionAnswerAdvisor questionAnswerAdvisor,
			final MessageChatMemoryAdvisor messageChatMemoryAdvisor) {

		final Advisor[] advisors = {
				new PiiRedactionAdvisor(10),
				messageChatMemoryAdvisor, // order 20
				questionAnswerAdvisor, // order 25
				SimpleLoggerAdvisor.builder().order(30).build()
		};

		return builder
				.defaultSystem("""
						You are a helpful and enthusiastic AI assistant specialized in the FIFA World Cup 2026,
						hosted across the USA, Canada and Mexico. Provide accurate, engaging advice for fans
						following the tournament while remaining professional. Answer only from the context
						provided to you; say so plainly if the context does not cover the question.
						""")
				.defaultAdvisors(advisors);
	}

	private static Builder getGeminiChatOptions(final String model) {
		return GoogleGenAiChatOptions.builder().model(model);
	}

	/**
	 * Order 20: after {@link PiiRedactionAdvisor} (10) so memory never stores raw PII, same fix carried over from 008-chat-memory.
	 */
	@Bean
	public MessageChatMemoryAdvisor messageChatMemoryAdvisor(final JdbcChatMemoryRepository jdbcChatMemoryRepository) {
		final var chatMemory = MessageWindowChatMemory.builder().chatMemoryRepository(jdbcChatMemoryRepository).maxMessages(20).build();

		return MessageChatMemoryAdvisor.builder(chatMemory)
				.order(20)
				.build();
	}

	/**
	 * Order 25: after {@link MessageChatMemoryAdvisor} (20) and before {@link SimpleLoggerAdvisor} (30).
	 */
	@Bean
	public QuestionAnswerAdvisor questionAnswerAdvisor(final VectorStore vectorStore) {
		return QuestionAnswerAdvisor.builder(vectorStore)
				.order(25)
				.build();
	}

	@Bean
	public ChatClient geminiChatClient(final ChatClient.Builder builder,
			@Value("${spring.ai.google.genai.chat.model}") final String model,
			final MessageChatMemoryAdvisor messageChatMemoryAdvisor,
			final QuestionAnswerAdvisor questionAnswerAdvisor) {
		return enrichChatClientBuilder(builder, questionAnswerAdvisor, messageChatMemoryAdvisor)
				.defaultOptions(getGeminiChatOptions(model))
				.build();
	}
}
