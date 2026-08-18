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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions.Builder;
import org.springframework.ai.mcp.DefaultMcpToolNamePrefixGenerator;
import org.springframework.ai.mcp.McpToolNamePrefixGenerator;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAiConfig {

	private static ChatClient.Builder enrichChatClientBuilder(final ChatClient.Builder builder,
			final PiiRedactionAdvisor piiRedactionAdvisor) {

		final Advisor[] advisors = {piiRedactionAdvisor, new SimpleLoggerAdvisor(30)};

		return builder.defaultSystem("""
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

	@Bean
	public List<Object> worldCupTools(final ObjectProvider<SyncMcpToolCallbackProvider> mcpToolCallbackProvider) {
		return mcpToolCallbackProvider.stream()
				.map(SyncMcpToolCallbackProvider::getToolCallbacks)
				.flatMap(Arrays::stream)
				.filter(Objects::nonNull)
				.map(Object.class::cast)
				.toList();
	}

	/**
	 * By default, if no custom McpToolNamePrefixGenerator bean is provided, the starter uses {@link DefaultMcpToolNamePrefixGenerator} which ensures
	 * unique tool names across all MCP client connections. The default generator:
	 * <ul>
	 * <li>Tracks all existing connections and tool names to ensure uniqueness</li>
	 * <li>Formats tool names by replacing non-alphanumeric characters with underscores (e.g., my-tool becomes my_tool)</li>
	 * <li>When duplicate tool names are detected across different connections, adds a counter prefix (e.g., alt_1_toolName, alt_2_toolName)</li>
	 * <li>Is thread-safe and maintains idempotency - the same combination of (client, server, tool) always gets the same unique name</li>
	 * <li>Ensures the final name doesn’t exceed 64 characters (truncating from the beginning if necessary)</li>
	 * </ul>
	 * <p>
	 * You can use also {@link McpToolNamePrefixGenerator#noPrefix()}, but this will throw {@link IllegalStateException} if multiple MCPs have the tools
	 * with the same name
	 *
	 * @return {@link McpToolNamePrefixGenerator}
	 */
	@Bean
	public McpToolNamePrefixGenerator mcpToolNamePrefixGenerator() {
		return McpToolNamePrefixGenerator.noPrefix();
	}

	@Bean
	public ChatClient geminiToolsAwareChatClient(final ChatClient geminiToolsNotAwareChatClient,
			final List<Object> worldCupTools) {
		return geminiToolsNotAwareChatClient.mutate()
				.defaultTools(worldCupTools)
				.build();
	}

	@Bean
	public ChatClient geminiToolsNotAwareChatClient(final ChatClient.Builder builder,
			@Value("${spring.ai.google.genai.chat.model}") final String model,
			final PiiRedactionAdvisor piiRedactionAdvisor) {
		return enrichChatClientBuilder(builder, piiRedactionAdvisor)
				.defaultOptions(getGeminiChatOptions(model))
				.build();
	}
}
