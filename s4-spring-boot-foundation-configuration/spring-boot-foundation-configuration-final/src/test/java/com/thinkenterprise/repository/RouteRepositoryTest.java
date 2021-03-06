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

package com.thinkenterprise.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.repository.RouteRepository;

@SpringBootTest
@ActiveProfiles("test")
public class RouteRepositoryTest {

    @Autowired
    private RouteRepository repository;

    @Test
    public void findByDeparture() throws Exception {
        Iterable<Route> actual = repository.findByDeparture("FRA");
        Assertions.assertNotNull(actual.iterator().hasNext());
       
    }

    @Test
    public void findAll() throws Exception {
        Iterable<Route> actual = repository.findAll();
  
    }
}