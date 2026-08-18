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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Runs dense (vector similarity, via {@link VectorStore}) and sparse (Postgres full-text, via {@code ts_rank}) retrieval over the same question, and
 * merges the two ranked lists with Reciprocal Rank Fusion: {@code score(doc) = sum(1 / (reciprocalRankFusionK + rank))} across every list the
 * document appears in. Fixes what vector-only retrieval gets wrong: embeddings represent meaning, not exact tokens, so a proper noun like a player or
 * stadium name can retrieve a topically close but factually wrong chunk. Keyword search nails exact tokens; fusing the two catches what either would
 * miss alone.
 */
public class HybridSearchAdvisor implements CallAdvisor {

	private static final Logger log = LoggerFactory.getLogger(HybridSearchAdvisor.class);
	private final VectorStore vectorStore;
	private final JdbcTemplate jdbcTemplate;
	private final String tableName;
	private final String fullTextColumn;
	private final int denseTopK;
	private final int sparseTopK;
	private final int fusedTopK;
	private final int reciprocalRankFusionK;
	private final int order;

	public HybridSearchAdvisor(final VectorStore vectorStore,
			final JdbcTemplate jdbcTemplate,
			final String tableName,
			final String fullTextColumn,
			final int denseTopK,
			final int sparseTopK,
			final int fusedTopK,
			final int reciprocalRankFusionK,
			final int order) {
		this.vectorStore = vectorStore;
		this.jdbcTemplate = jdbcTemplate;
		this.tableName = tableName;
		this.fullTextColumn = fullTextColumn;
		this.denseTopK = denseTopK;
		this.sparseTopK = sparseTopK;
		this.fusedTopK = fusedTopK;
		this.reciprocalRankFusionK = reciprocalRankFusionK;
		this.order = order;
	}

	private static String lastUserQuestion(final Prompt prompt) {
		String question = null;
		for (final Message message : prompt.getInstructions()) {
			if (message.getMessageType() == MessageType.USER) {
				question = message.getText();
			}
		}
		return question;
	}

	private static Prompt augment(final Prompt original, final String question, final String context) {
		final List<Message> messages = new ArrayList<>();
		for (final Message message : original.getInstructions()) {
			if (message.getMessageType() == MessageType.USER) {
				messages.add(new UserMessage("""
						%s
						
						Context information is below, retrieved by combining vector similarity search and keyword search:
						---------------------
						%s
						---------------------
						Given the context and not prior knowledge, answer the question. If the answer is not in the context, say so plainly.
						""".formatted(question, context)));
			} else {
				messages.add(message);
			}
		}
		return new Prompt(messages, original.getOptions());
	}

	@Override
	public ChatClientResponse adviseCall(final ChatClientRequest request, final CallAdvisorChain chain) {
		final String question = lastUserQuestion(request.prompt());
		if (question == null || question.isBlank()) {
			return chain.nextCall(request);
		}

		final List<RankedDocument> dense = denseResults(question);
		final List<RankedDocument> sparse = sparseResults(question);
		final String context = fuse(dense, sparse);

		final ChatClientRequest augmentedRequest = request.mutate().prompt(augment(request.prompt(), question, context)).build();
		return chain.nextCall(augmentedRequest);
	}

	private List<RankedDocument> denseResults(final String question) {
		final List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder().query(question).topK(denseTopK).build());
		final List<RankedDocument> ranked = new ArrayList<>();
		for (int i = 0; i < documents.size(); i++) {
			ranked.add(new RankedDocument(documents.get(i).getId(), documents.get(i).getText(), i + 1));
		}
		return ranked;
	}

	private List<RankedDocument> sparseResults(final String question) {
		// Convert "Morocco squad players" -> "Morocco | squad | players"
		final String orQuery = Arrays.stream(question.split("\\W+"))
				.filter(w -> !w.isBlank())
				.collect(Collectors.joining(" | "));

		final String sql =
				"SELECT id, content FROM %s WHERE %s @@ to_tsquery('english', ?) ORDER BY ts_rank(%s, to_tsquery('english', ?)) DESC LIMIT ?"
						.formatted(tableName, fullTextColumn, fullTextColumn);

		return jdbcTemplate
				.query(sql, (rs, rowNum) -> new RankedDocument(rs.getString("id"), rs.getString("content"), rowNum + 1), orQuery, orQuery, sparseTopK);
	}

	/**
	 * Merges and re-ranks results from dense (vector) and sparse (keyword) search using
	 * <strong>Reciprocal Rank Fusion (RRF)</strong>, returning a single, unified context string for the prompt.
	 *
	 * <h3>Why Reciprocal Rank Fusion?</h3>
	 * <ul>
	 *   <li><b>Fixes the Vector Blind Spot:</b> Dense embeddings capture semantic intent and conceptual
	 *       meaning, but compress exact strings. They struggle with proper nouns (e.g., "Lamine Yamal",
	 *       "Estadio Azteca") and exact terms, often retrieving semantically close but factually incorrect noise.</li>
	 *   <li><b>Fixes the Keyword Blind Spot:</b> Lexical full-text search ({@code ts_rank}) nails exact token
	 *       matches, but fails when a query uses phrasing or synonyms not explicitly written in the chunk.</li>
	 *   <li><b>Solves the Score Incommensurability Problem:</b> Cosine similarity scores (bounded between 0 and 1)
	 *       and Postgres {@code ts_rank} scores (unbounded floats) live on different mathematical scales and cannot
	 *       be directly added or normalized. RRF bypasses score calibration by converting raw scores into ordinal
	 *       ranks, scoring each document via:
	 *
	 *       <pre>
	 *        <b>RRF_Score(d) = &sum;<sub>m &isin; M</sub> [ 1 / (k + r<sub>m</sub>(d)) ]</b>
	 *       </pre>
	 *       where,
	 *       <ul>
	 *         <li><i>M</i> is the set of retrieval methods (in our case, Dense and Sparse).</li>
	 *         <li><i>r<sub>m</sub>(d)</i> is the 1-based rank index of document <i>d</i> in retrieval method <i>m</i>.</li>
	 *         <li><i>k</i> is a smoothing constant ({@code reciprocalRankFusionK}, typically set to 60). It prevents
	 *             high-ranking documents in small result sets from overwhelmingly dominating the score.</li>
	 *       </ul>
	 * </ul>
	 *
	 * <h3>Execution Steps:</h3>
	 * <ol>
	 *   <li>Iterate over dense and sparse ranked document lists, calculating rank-based score contributions.</li>
	 *   <li>Accumulate RRF scores in {@code scoreById}. Documents appearing in <i>both</i> lists receive score
	 *       boosts from both paradigms and naturally float to the top.</li>
	 *   <li>Maintain a map of document content in {@code contentById} to eliminate duplicates across lists.</li>
	 *   <li>Sort document IDs by their combined RRF score in descending order and limit to {@code fusedTopK}.</li>
	 *   <li>Join the selected text contents with Markdown horizontal rule separators ({@code ---}).</li>
	 * </ol>
	 *
	 * @param dense  List of documents retrieved via vector similarity search, ordered by rank (1-based).
	 * @param sparse List of documents retrieved via full-text keyword search, ordered by rank (1-based).
	 * @return A aggregated, Markdown-separated string of top context documents ready for prompt injection.
	 */
	private String fuse(final List<RankedDocument> dense, final List<RankedDocument> sparse) {
		final Map<String, Double> scoreById = new HashMap<>();
		final Map<String, String> contentById = new HashMap<>();

		for (final RankedDocument document : dense) {
			scoreById.merge(document.id(), 1.0 / (reciprocalRankFusionK + document.rank()), Double::sum);
			contentById.putIfAbsent(document.id(), document.content());
		}
		for (final RankedDocument document : sparse) {
			scoreById.merge(document.id(), 1.0 / (reciprocalRankFusionK + document.rank()), Double::sum);
			contentById.putIfAbsent(document.id(), document.content());
		}

		return scoreById
				.entrySet()
				.stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.limit(fusedTopK)
				.map(entry -> contentById.get(entry.getKey()))
				.collect(Collectors.joining("\n\n---\n\n"));
	}

	@Override
	public String getName() {
		return "HybridSearchAdvisor";
	}

	@Override
	public int getOrder() {
		return order;
	}

	private record RankedDocument(String id, String content, int rank) {

	}
}
