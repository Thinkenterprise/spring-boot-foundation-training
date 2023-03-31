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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.thinkenterprise.domain.route.Route;

@SpringBootApplication
public class Application implements CommandLineRunner {
	
	public final static Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Autowired RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		
		
		ResponseEntity<Route> routeEntity;
		
		try {
			routeEntity = this.restTemplate.getForEntity("/routes/{id}", Route.class, "101");
		} catch (HttpClientErrorException e) {
			ProblemDetail problemDetail= e.getResponseBodyAs(ProblemDetail.class);
			logger.info(problemDetail.getTitle());
		}
		
		
	}
        
}
