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

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Deliberately separate from {@code GenAiConfig}: nothing here is production wiring, it exists only so {@link KnowledgeBaseEvaluationIT} has a
 * {@link RelevancyEvaluator} and {@link FactCheckingEvaluator} to autowire. Both judges run on the same Gemini flash-lite model the production
 * pipeline already uses for classification: cheap and fast is plenty for a YES/NO-shaped judgement, even one scoring the pro model's own output.
 */
@TestConfiguration
class EvaluationTestConfig {

	@Bean
	RelevancyEvaluator relevancyEvaluator(final ChatClient.Builder builder,
			@Value("${spring.ai.google.genai.chat.flash-lite-model}") final String model) {
		return RelevancyEvaluator.builder().chatClientBuilder(evaluationChatClientBuilder(builder, model)).build();
	}

	@Bean
	FactCheckingEvaluator factCheckingEvaluator(final ChatClient.Builder builder,
			@Value("${spring.ai.google.genai.chat.flash-lite-model}") final String model) {
		return FactCheckingEvaluator.builder(evaluationChatClientBuilder(builder, model)).build();
	}

	private static ChatClient.Builder evaluationChatClientBuilder(final ChatClient.Builder builder, final String model) {
		return builder.defaultOptions(GoogleGenAiChatOptions.builder().model(model).temperature(0.0));
	}
}
