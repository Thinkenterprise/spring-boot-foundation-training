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

package com.thinkenterprise.config;

import com.thinkenterprise.driver.RouteRepositoryDriver;
import com.thinkenterprise.driver.TestRouteRepositoryDriver;
import com.thinkenterprise.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
@ConditionalOnClass(RouteRepository.class)
public class TestConfiguration {

    // Nur zur Demonstration der Enable bzw. Scan Funktion für ConfigurationProperties in der Application.java
    @Autowired
    private RouteProperties routeProperties;

    @Bean
    public RouteRepositoryDriver testRouteRepositoryDriver() {
        return new TestRouteRepositoryDriver();
    }
}
