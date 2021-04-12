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
 * @author Michael Schaefer
 */

package com.thinkenterprise;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.core.metrics.StartupStep;
import org.springframework.core.metrics.jfr.FlightRecorderApplicationStartup;

@SpringBootApplication(exclude = {
		H2ConsoleAutoConfiguration.class
}, scanBasePackageClasses = {
		Application.class
})
public class Application {

	public static void main( String[] args ) {
		SpringApplication springApplication = new SpringApplication( Application.class );
		// springApplication.setApplicationStartup( new FlightRecorderApplicationStartup() ); // Activate JDK Mission Control (JMC), see "start-with-jfr-profiling.sh"
		// springApplication.setApplicationStartup( new BufferingApplicationStartup( 1000 ) ); // As Actuator POST-Request(http://localhost:8080/actuator/startup), see "./request-lifecycle-events.sh"
		springApplication.setApplicationStartup( new CustomApplicationStartup() ); // Custom ApplicationStartup for understanding the framework
		springApplication.run( args );
	}

	// -------------------------------------------------------------------------

	/**
	 * A dummy {@link ApplicationStartup} implementation for inspecting the
	 * Spring Boot application startup mechanism.
	 */
	private static class CustomApplicationStartup implements ApplicationStartup {

		@Override
		public StartupStep start( String name ) {
			System.out.println( "[##### Start <" + name + "> #####]" );
			return new CustomStartupStep( name );
		}
	}

	// -------------------------------------------------------------------------

	/**
	 * A dummy {@link StartupStep} implementation for inspecting the Spring Boot
	 * application startup mechanism.
	 */
	private static class CustomStartupStep implements StartupStep {

		private final CustomTags TAGS = new CustomTags();

		private String name;

		public CustomStartupStep( String name ) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public long getId() {
			return 0;
		}

		@Override
		public Long getParentId() {
			return -1L;
		}

		@Override
		public StartupStep tag( String key, String value ) {
			System.out.println( "[##### <" + key + "=" + value + "> #####]" );
			return new CustomStartupStep( key + "=" + value );
		}

		@Override
		public StartupStep tag( String key, Supplier<String> value ) {
			System.out.println( "[##### <" + key + "=" + value.get() + "> #####]" );
			return new CustomStartupStep( key + "=" + value.get() );
		}

		@Override
		public Tags getTags() {
			return TAGS;
		}

		@Override
		public void end() {}

		// ---------------------------------------------------------------------

		private static class CustomTags implements StartupStep.Tags {

			@Override
			public Iterator<StartupStep.Tag> iterator() {
				return Collections.emptyIterator();
			}
		}
	}
}
