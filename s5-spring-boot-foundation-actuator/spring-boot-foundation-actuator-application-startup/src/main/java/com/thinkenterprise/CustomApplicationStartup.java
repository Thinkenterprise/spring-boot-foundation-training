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

import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.core.metrics.StartupStep;

/**
 * A dummy {@link ApplicationStartup} implementation for inspecting the Spring
 * Boot application startup mechanism.
 */
class CustomApplicationStartup implements ApplicationStartup {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StartupStep start( String name ) {
		System.out.println( "[##### Start <" + name + "> #####]" );
		return new CustomStartupStep( name );
	}
}