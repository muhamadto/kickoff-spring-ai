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

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientBuilderCustomizer;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.ToolCallingAdvisor;
import org.springframework.ai.chat.client.advisor.observation.AdvisorObservationConvention;
import org.springframework.ai.chat.client.observation.ChatClientObservationConvention;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions.Builder;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientBuilderConfigurer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAiConfig {

	private static ChatClient.Builder enrichChatClientBuilder(final ChatClient.Builder builder) {
		return builder
				.defaultSystem("""
						You are a helpful and enthusiastic AI assistant specialized in the FIFA World Cup 2026,
						hosted across the USA, Canada and Mexico. Provide accurate, engaging advice for fans
						following the tournament while remaining professional.
						""")
				// Be cautious about logging sensitive information in production environments. Probably best to create a custom logging advisor and remove the
				// user text and the response text
				.defaultAdvisors(new SimpleLoggerAdvisor());
	}

	private static Builder getGeminiChatOptions(final String model) {
		return GoogleGenAiChatOptions.builder().model(model);
	}

	/**
	 * A {@link ChatClient} created with:
	 * <ul>
	 * <li>ChatClient#create({@link org.springframework.ai.google.genai.GoogleGenAiChatModel.ChatModel}), OR</li>
	 * <li>ChatClient#builder({@link org.springframework.ai.google.genai.GoogleGenAiChatModel.ChatModel})</li>
	 * </ul>
	 * <p>
	 * bypasses the auto-configured {@link ChatClient.Builder}, which means observability and {@link ChatClientBuilderCustomizer} beans are ignored
	 *
	 * @param googleGenAiChatModel
	 * @return {@link ChatClient}
	 */
	@Bean
	public ChatClient geminiFlashGenAiChatClientWithoutObservability(final GoogleGenAiChatModel googleGenAiChatModel) {
		return ChatClient.create(googleGenAiChatModel);
	}

	@Bean
	public ChatClient geminiProChatClient(final GoogleGenAiChatModel googleGenAiChatModel,
			final ChatClientBuilderConfigurer configurer,
			final ObjectProvider<ObservationRegistry> observationRegistry,
			final ObjectProvider<ChatClientObservationConvention> chatClientObservationConvention,
			final ObjectProvider<AdvisorObservationConvention> advisorObservationConvention,
			final ObjectProvider<ToolCallingAdvisor.Builder<?>> toolCallingAdvisorBuilder) {

		final ChatClient.Builder builder = ChatClient
				.builder(googleGenAiChatModel,
						observationRegistry.getIfUnique(() -> ObservationRegistry.NOOP),
						chatClientObservationConvention.getIfUnique(),
						advisorObservationConvention.getIfUnique(),
						toolCallingAdvisorBuilder.getIfAvailable())
				.defaultAdvisors(new SimpleLoggerAdvisor());

		return configurer.configure(builder).build();
	}

	@Bean
	public ChatClient geminiFlashChatClient(final ChatClient.Builder builder, @Value("${spring.ai.google.genai.chat.flash-model}") final String model) {
		return enrichChatClientBuilder(builder).defaultOptions(getGeminiChatOptions(model)).build();
	}

	@Bean
	public ChatClient geminiFlashLiteChatClient(final ChatClient.Builder builder,
			@Value("${spring.ai.google.genai.chat.flash-lite-model}") final String model) {
		return enrichChatClientBuilder(builder).defaultOptions(getGeminiChatOptions(model)).build();
	}
}
