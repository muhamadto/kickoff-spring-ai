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

package ai.spring.learning.worldcup.converter;

import org.apache.logging.log4j.util.Strings;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.converter.ResponseTextCleaner;
import org.springframework.core.ParameterizedTypeReference;
import tools.jackson.databind.json.JsonMapper;

public class WorldCup2026BeanOutputConverter<T> extends org.springframework.ai.converter.BeanOutputConverter<@NonNull T> {

	public WorldCup2026BeanOutputConverter(Class<T> clazz) {
		this(clazz, null, null);
	}

	/**
	 * Constructor to initialize with the target type's class, a custom JSON mapper, and a line endings normalizer to ensure consistent line endings on
	 * any platform.
	 *
	 * @param clazz      The target type's class.
	 * @param jsonMapper Custom JSON mapper for JSON operations. endings.
	 */
	public WorldCup2026BeanOutputConverter(final Class<T> clazz, @Nullable final JsonMapper jsonMapper) {
		super(clazz, jsonMapper);
	}

	/**
	 * Constructor to initialize with the target type's class, a custom JSON mapper, and a custom text cleaner.
	 *
	 * @param clazz       The target type's class.
	 * @param jsonMapper  Custom JSON mapper for JSON operations.
	 * @param textCleaner Custom text cleaner for preprocessing responses.
	 */
	public WorldCup2026BeanOutputConverter(final Class<T> clazz,
			@Nullable final JsonMapper jsonMapper,
			@Nullable final ResponseTextCleaner textCleaner) {
		super(clazz, jsonMapper, textCleaner);
	}

	/**
	 * Constructor to initialize with the target class type reference.
	 *
	 * @param typeRef The target class type reference.
	 */
	public WorldCup2026BeanOutputConverter(final ParameterizedTypeReference<T> typeRef) {
		super(typeRef);
	}

	/**
	 * Constructor to initialize with the target class type reference, a custom JSON mapper, and a line endings normalizer to ensure consistent line
	 * endings on any platform.
	 *
	 * @param typeRef    The target class type reference.
	 * @param jsonMapper Custom JSON mapper for JSON operations. endings.
	 */
	public WorldCup2026BeanOutputConverter(final ParameterizedTypeReference<T> typeRef, @Nullable final JsonMapper jsonMapper) {
		super(typeRef, jsonMapper);
	}

	/**
	 * Constructor to initialize with the target class type reference, a custom JSON mapper, and a custom text cleaner.
	 *
	 * @param typeRef     The target class type reference.
	 * @param jsonMapper  Custom JSON mapper for JSON operations.
	 * @param textCleaner Custom text cleaner for preprocessing responses.
	 */
	public WorldCup2026BeanOutputConverter(final ParameterizedTypeReference<T> typeRef,
			@Nullable final JsonMapper jsonMapper,
			@Nullable final ResponseTextCleaner textCleaner) {
		super(typeRef, jsonMapper, textCleaner);
	}

	@Override
	public String getFormat() {
		return Strings.EMPTY;
	}
}
