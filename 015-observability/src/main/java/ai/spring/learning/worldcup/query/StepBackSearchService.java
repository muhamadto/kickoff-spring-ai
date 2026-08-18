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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Plain dense vector search against the step-back query {@link StepBackQueryService} generates, the same technique {@code QuestionAnswerAdvisor} runs
 * against the fan's raw question, just against a different query string. A genuinely separate search from the one the advisor runs automatically:
 * this service exists so the two never touch each other's code. Dropped the hand-rolled dense-plus-sparse-plus-Reciprocal-Rank-Fusion logic this
 * class used before {@code 012-agentic-rag} removed it from the advisor, for the same reason: {@code 011-hybrid-search-rag} already teaches that
 * technique on its own, and a real deployment would normally get it from its hosted vector store rather than duplicating it here too.
 */
@Component
public class StepBackSearchService {

	private final VectorStore vectorStore;
	private final int topK;

	public StepBackSearchService(final VectorStore vectorStore, @Value("${worldcup.step-back.top-k}") final int topK) {
		this.vectorStore = vectorStore;
		this.topK = topK;
	}

	public Optional<String> search(final String query) {
		final List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder().query(query).topK(topK).build());
		if (documents.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(documents.stream().map(Document::getText).collect(Collectors.joining("\n\n---\n\n")));
	}
}
