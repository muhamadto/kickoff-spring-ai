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

package ai.spring.learning.worldcup.query;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

/**
 * Step-back prompting (Zheng et al., "Take a Step Back": abstract a specific question into a broader one before retrieving): given the fan's actual
 * question, ask the model for a more general question whose answer would supply useful background, then hand that off to
 * {@link StepBackSearchService} for a second, separate retrieval. Runs on {@code stepBackChatClient}, the flash model, bare (no advisors): this is a
 * narrow, single-purpose rewrite, not part of the fan's conversation.
 */
@Component
public class StepBackQueryService {

	private final ChatClient stepBackChatClient;

	public StepBackQueryService(final ChatClient stepBackChatClient) {
		this.stepBackChatClient = stepBackChatClient;
	}

	public String generateStepBackQuery(final String question) {
		return stepBackChatClient.prompt().system("""
				You help retrieval find better context for a FIFA World Cup 2026 fan assistant. Given
				the fan's specific question, write one broader, more general question whose answer
				would supply useful background for answering the specific one. Reply with only the
				broader question, nothing else.
				""").user(question).call().content();
	}
}
