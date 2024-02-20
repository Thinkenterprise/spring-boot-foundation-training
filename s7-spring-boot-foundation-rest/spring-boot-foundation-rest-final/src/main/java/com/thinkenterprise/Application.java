/*
 * Copyright (C) 2016 Thinkenterprise
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * @author Rafael Kansy
 * @author Michael Schaefer
 */

package com.thinkenterprise;


import com.thinkenterprise.domain.route.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootApplication
public class Application implements CommandLineRunner {
	
	public final static Logger logger = LoggerFactory.getLogger(Application.class);
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		RestClient restClient = RestClient.create("http://localhost:8080");

		ResponseEntity<Route> route = restClient.get()
				.uri("/routes/101")
				.retrieve()
				.toEntity(Route.class);

		logger.info("### Response Status {}", route.getStatusCode());


		restClient.get()
				.uri("/routes/110000")
				.retrieve()
				.onStatus(httpStatusCode -> httpStatusCode.isSameCodeAs(BAD_REQUEST), ((request, response) -> {
					logger.error("### Response Status {}", response.getStatusCode());
				}))
				.toEntity(Route.class);
	}
        
}
