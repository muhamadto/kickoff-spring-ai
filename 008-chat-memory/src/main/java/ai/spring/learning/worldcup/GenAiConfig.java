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
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAiConfig {

	private static ChatClient.Builder enrichChatClientBuilder(final ChatClient.Builder builder,
			final PiiRedactionAdvisor piiRedactionAdvisor) {

		final Advisor[] advisors = {piiRedactionAdvisor, new SimpleLoggerAdvisor(30)};

		return builder
				.defaultSystem("""
						You are a helpful and enthusiastic AI assistant specialized in the FIFA World Cup 2026,
						hosted across the USA, Canada and Mexico. Provide accurate, engaging advice for fans
						following the tournament while remaining professional.
						""")
				.defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
				.defaultAdvisors(advisors);
	}

	private static Builder getGeminiChatOptions(final String model) {
		return GoogleGenAiChatOptions.builder().model(model);
	}

	@Bean
	public PiiRedactionAdvisor piiRedactionAdvisor() {
		return new PiiRedactionAdvisor(10);
	}

	/**
	 * {@link ChatMemoryRepository} is autoconfigured as we imported the
	 * {@code org.springframework.ai:spring-ai-starter-model-chat-memory-repository-jdbc} dependency
	 *
	 * @param chatMemoryRepository
	 * @return
	 */
	@Bean
	public MessageChatMemoryAdvisor messageChatMemoryAdvisor(final ChatMemoryRepository chatMemoryRepository) {
		final var chatMemory = MessageWindowChatMemory.builder()
				.chatMemoryRepository(chatMemoryRepository)
				.maxMessages(20)
				.build();

		return MessageChatMemoryAdvisor.builder(chatMemory)
				.order(20)
				.build();
	}

	@Bean
	public ChatClient geminiMemoryAwareChatClient(final ChatClient geminiMemoryNotAwareChatClient,
			final MessageChatMemoryAdvisor messageChatMemoryAdvisor) {
		return geminiMemoryNotAwareChatClient.mutate()
				.defaultAdvisors(messageChatMemoryAdvisor)
				.build();
	}

	@Bean
	public ChatClient geminiMemoryNotAwareChatClient(final ChatClient.Builder builder,
			@Value("${spring.ai.google.genai.chat.model}") final String model,
			final PiiRedactionAdvisor piiRedactionAdvisor) {
		return enrichChatClientBuilder(builder, piiRedactionAdvisor)
				.defaultOptions(getGeminiChatOptions(model))
				.build();
	}
}
