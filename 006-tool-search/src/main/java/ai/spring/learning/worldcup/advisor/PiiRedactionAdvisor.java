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

package ai.spring.learning.worldcup.advisor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

public class PiiRedactionAdvisor implements CallAdvisor {

	private static final Pattern EMAIL = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
	private static final Pattern PHONE =
			Pattern.compile("(?<!\\d)(?:\\+\\d{1,3}[-.\\s]?\\(?\\d{1,4}\\)?(?:[-.\\s]?\\d{2,4}){1,4}|\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4})(?!\\d)");

	private final int order;

	public PiiRedactionAdvisor(final int order) {
		this.order = order;
	}

	@NonNull
	@Override
	public ChatClientResponse adviseCall(@NonNull final ChatClientRequest request, @NonNull final CallAdvisorChain chain) {
		final Prompt redactedPrompt = redactUserMessages(request.prompt());
		final ChatClientRequest updatedRequest = request.mutate().prompt(redactedPrompt).build();

		return chain.nextCall(updatedRequest);
	}

	private Prompt redactUserMessages(final Prompt prompt) {
		final List<Message> redacted = new ArrayList<>();

		for (final Message message : prompt.getInstructions()) {
			if (message.getMessageType() == org.springframework.ai.chat.messages.MessageType.USER) {
				redacted.add(new UserMessage(redact(message.getText())));
			} else {
				redacted.add(message);
			}
		}

		return new Prompt(redacted, prompt.getOptions());
	}

	private String redact(final String text) {
		String result = text;
		final Matcher emailMatcher = EMAIL.matcher(result);
		result = emailMatcher.replaceAll("[EMAIL_REDACTED]");
		final Matcher phoneMatcher = PHONE.matcher(result);
		result = phoneMatcher.replaceAll("[PHONE_REDACTED]");
		return result;
	}

	@NonNull
	@Override
	public String getName() {
		return "PiiRedactionAdvisor";
	}

	@Override
	public int getOrder() {
		return this.order;
	}
}
