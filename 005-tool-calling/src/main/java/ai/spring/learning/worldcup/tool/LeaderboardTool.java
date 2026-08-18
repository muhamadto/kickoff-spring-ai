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

package ai.spring.learning.worldcup.tool;

import ai.spring.learning.worldcup.model.Player;
import java.util.Comparator;
import java.util.List;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * Composes {@link PlayerTool} rather than holding its own data, so the goal and assist leaderboards stay in sync with player stats.
 */
@Component
public class LeaderboardTool {

	private final PlayerTool playerTool;

	public LeaderboardTool(final PlayerTool playerTool) {
		this.playerTool = playerTool;
	}

	@Tool(name = "get-goal-leaderboard",
			description = "Get the World Cup 2026 top goal scorers so far, ranked from highest to lowest. Empty until verified goal data is sourced.")
	public List<Player> getGoalLeaderboard() {
		return playerTool
				.getAllPlayers()
				.stream()
				.filter(p -> p.goals() != null && p.goals() > 0)
				.sorted(Comparator.comparingInt(Player::goals).reversed())
				.toList();
	}

	@Tool(name = "get-assist-leaderboard",
			description = "Get the World Cup 2026 top assist providers so far, ranked from highest to lowest. Empty until verified assist data is sourced.")
	public List<Player> getAssistLeaderboard() {
		return playerTool
				.getAllPlayers()
				.stream()
				.filter(p -> p.assists() != null && p.assists() > 0)
				.sorted(Comparator.comparingInt(Player::assists).reversed())
				.toList();
	}
}
