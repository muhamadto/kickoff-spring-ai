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

import static org.assertj.core.api.Assertions.assertThat;

import ai.spring.learning.worldcup.service.Worldcup2026Service;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Real evaluation, against the real guarded pipeline: every {@code @Test} here goes through {@link Worldcup2026Service#chat(String, String)}, the
 * same code path {@code /chat} uses, then scores the result with {@link RelevancyEvaluator} or {@link FactCheckingEvaluator} instead of a
 * golden-answer string match, since a fixed expected string would break the moment the model rephrases a correct answer. Context for the evaluators
 * comes straight from {@link VectorStore#similaritySearch(SearchRequest)} call running against the test-scoped vector database.
 *
 * <p>
 * {@link Worldcup2026Service} answers through the real, Gemini-backed {@code mainChatClient}, {@code stepBackChatClient} and
 * {@code classificationChatClient} (that's the production pipeline being evaluated, unchanged since {@code 014-query-optimised-rag}). The evaluators
 * scoring its output run on Gemini flash-lite instead of the pro model doing the generating: a cheap, fast model is plenty for a YES/NO-shaped
 * judgement. {@code @EnabledIfEnvironmentVariable} below gates the whole class on {@code GOOGLE_AI_API_KEY}, since this project uses Gemini
 * exclusively, if you choose another model family change the gate accordingly.
 *
 * <p>
 * The Testcontainers PGVector instance starts empty ({@code 010-embedding}'s ingestion never runs against it), so {@link #seedKnowledgeBase()} writes
 * a handful of the exact facts the golden questions below need directly via {@code VectorStore.add(...)}, the same call 010-embedding itself makes,
 * just for a small fixture instead of the full generated knowledge base.
 */
@SpringBootTest
@Import(EvaluationTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledIfEnvironmentVariable(named = "GOOGLE_AI_API_KEY", matches = ".+")
class KnowledgeBaseEvaluationIT {

	static final PostgreSQLContainer postgres =
			new PostgreSQLContainer(DockerImageName.parse("pgvector/pgvector:pg17").asCompatibleSubstituteFor("postgres"));

	static {
		postgres.start();
	}

	@Autowired
	private VectorStore vectorStore;

	@Autowired
	private Worldcup2026Service worldcup2026Service;

	@Autowired
	private RelevancyEvaluator relevancyEvaluator;

	@Autowired
	private FactCheckingEvaluator factCheckingEvaluator;

	@DynamicPropertySource
	static void configureContainers(final DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	private static Stream<Arguments> goldenQuestions() {
		return Stream
				.of(Arguments.of("Which country is the referee Ma Ning from?"),
						Arguments.of("Which country is the referee Alireza Faghani from?"),
						Arguments.of("What is the seating capacity of Estadio Akron?"),
						Arguments.of("How many goals and assists does Kylian Mbappé have?"));
	}

	@BeforeAll
	void seedKnowledgeBase() {
		vectorStore
				.add(List
						.of(new Document("Ma Ning (China) served as a head referee at the FIFA World Cup 2026."),
								new Document("Alireza Faghani (Australia) served as a head referee at the FIFA World Cup 2026."),
								new Document("Estadio Akron has a seating capacity of 45,664 and hosted FIFA World Cup 2026 matches."),
								new Document("Kylian Mbappé recorded 10 goals, 4 assists and 1 Man of the Match award during the FIFA World Cup 2026 group stage.")));
	}

	@ParameterizedTest
	@MethodSource("goldenQuestions")
	void answerIsRelevantToRetrievedContext(final String question) {
		final SearchRequest searchRequest = SearchRequest.builder()
				.query(question)
				.topK(3)
				.build();

		final List<Document> context = vectorStore.similaritySearch(searchRequest);

		assertThat(context)
				.as("Expected retrieval to find context for: " + question)
				.isNotEmpty();

		final String answer = worldcup2026Service.chat(question, "016-evaluation-test");

		final EvaluationRequest evaluationRequest = new EvaluationRequest(question, context, answer);

		final EvaluationResponse response = relevancyEvaluator.evaluate(evaluationRequest);

		assertThat(response.isPass()).
				as(response.getFeedback())
				.isTrue();
	}

	@Test
	void factCheckingCatchesAContradictedClaim() {
		final SearchRequest searchRequest = SearchRequest.builder()
				.query("What is the seating capacity of Estadio Akron?")
				.topK(1)
				.build();

		final List<Document> context = vectorStore.similaritySearch(searchRequest);

		assertThat(context)
				.as("Expected retrieval to find Estadio Akron's capacity")
				.isNotEmpty();

		final String answer = worldcup2026Service.chat(context.getFirst().getText(), "016-evaluation-test");

		final EvaluationRequest evaluationRequest =
				new EvaluationRequest("Estadio Akron has a seating capacity of 90,000.", List.of(), answer);

		final EvaluationResponse response = factCheckingEvaluator.evaluate(evaluationRequest);

		assertThat(response.isPass())
				.as(response.getFeedback())
				.isFalse();
	}
}
