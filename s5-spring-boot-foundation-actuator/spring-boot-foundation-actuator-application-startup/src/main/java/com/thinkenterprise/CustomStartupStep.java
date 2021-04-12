/*
 * Copyright (C) 2018 Thinkenterprise
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * @author Siegfried Steiner
 */

package com.thinkenterprise;

import java.util.function.Supplier;

import org.springframework.core.metrics.StartupStep;

/**
 * A dummy {@link StartupStep} implementation for inspecting the Spring Boot
 * application startup mechanism.
 */
class CustomStartupStep implements StartupStep {

	private final CustomTags TAGS = new CustomTags();

	private String name;

	public CustomStartupStep( String name ) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getId() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getParentId() {
		return -1L;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StartupStep tag( String key, String value ) {
		System.out.println( "[##### <" + key + "=" + value + "> #####]" );
		return new CustomStartupStep( key + "=" + value );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StartupStep tag( String key, Supplier<String> value ) {
		System.out.println( "[##### <" + key + "=" + value.get() + "> #####]" );
		return new CustomStartupStep( key + "=" + value.get() );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tags getTags() {
		return TAGS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void end() {}

	// ---------------------------------------------------------------------

}