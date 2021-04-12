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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.core.metrics.jfr.FlightRecorderApplicationStartup;

@SuppressWarnings("unused")
@SpringBootApplication(exclude = {
		H2ConsoleAutoConfiguration.class
}, scanBasePackageClasses = {
		Application.class
})
public class Application {

	public static void main( String[] args ) {
		SpringApplication springApplication = new SpringApplication( Application.class );
		springApplication.setApplicationStartup( new FlightRecorderApplicationStartup() ); // Activate JDK Mission Control (JMC), see "start-with-jfr-profiling.sh"
		// springApplication.setApplicationStartup( new BufferingApplicationStartup( 1000 ) ); // As Actuator POST-Request(http://localhost:8080/actuator/startup), see "./request-lifecycle-events.sh"
		// springApplication.setApplicationStartup( new CustomApplicationStartup() ); // Custom ApplicationStartup for understanding the framework
		springApplication.run( args );
	}
}
