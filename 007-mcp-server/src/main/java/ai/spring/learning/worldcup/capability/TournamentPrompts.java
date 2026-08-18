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

package ai.spring.learning.worldcup.capability;

import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import java.util.List;
import org.springframework.ai.mcp.annotation.McpArg;
import org.springframework.ai.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Component;

/**
 * Prompts: reusable templates the server owns. Earlier modules kept templates inside the client application (002); publishing them here means every
 * connected client shares one version.
 */
@Component
public class TournamentPrompts {

	private static GetPromptResult result(final String description, final String text) {
		return new GetPromptResult(description, List.of(new PromptMessage(Role.USER, new TextContent(text))));
	}

	@McpPrompt(name = "match-recap", description = "The World Cup 2026 match recap prompt")
	public GetPromptResult matchRecap(
			@McpArg(name = "fixture", description = "The fixture as 'Home vs Away', e.g., 'Morocco vs Haiti'", required = true) final String fixture) {
		final String text = """
				Give me a full recap of the World Cup 2026 match %s: the final score, tournament stage, venue, \
				standout performances, and the key moments that decided it.""".formatted(fixture);
		return result("Match recap for " + fixture, text);
	}
}
