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

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// The MCP client is disabled here so the context can start without a running worldcup-mcp-server;
// GenAiConfig's worldCupTools and the controller's mcpSyncClients both take an ObjectProvider and
// degrade to an empty list when no MCP client beans exist, for the same reason.
@SpringBootTest(properties = "spring.ai.mcp.client.enabled=false")
class Worldcup2026ApplicationIT {

	@Test
	void contextLoads() {
	}

}
