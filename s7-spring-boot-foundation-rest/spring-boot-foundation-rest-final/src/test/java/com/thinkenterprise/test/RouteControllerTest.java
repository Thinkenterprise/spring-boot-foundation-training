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

package com.thinkenterprise.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkenterprise.domain.route.Flight;
import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.repository.RouteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8080"})
public class RouteControllerTest {

    @Autowired
    private RouteRepository routeRepository;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:8080");
    }

    @Test
    public void post() {

        Route route = new Route("LH2345", "DUS", "BER");

        Flight flight = new Flight(120.45, LocalDate.of(2015, 9, 23));
        route.addFlight(flight);

        flight = new Flight(111.45, LocalDate.of(2015, 9, 24));
        route.addFlight(flight);

        ResponseEntity<Route> result = restClient.post()
                .uri("/routes")
                .contentType(APPLICATION_JSON)
                .body(route)
                .retrieve()
                .toEntity(Route.class);

        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals("LH2345", result.getBody().getFlightNumber());

    }

    @Test
    public void postValidated() {

        Route route = new Route(null, "DUS", "BER");

        Flight flight = new Flight(120.45, LocalDate.of(2015, 9, 23));
        route.addFlight(flight);

        flight = new Flight(111.45, LocalDate.of(2015, 9, 24));
        route.addFlight(flight);

        restClient.post()
                .uri("/routes")
                .contentType(APPLICATION_JSON)
                .body(route)
                .retrieve()
                .onStatus(httpStatusCode -> !httpStatusCode.isSameCodeAs(BAD_REQUEST),
                        (request, response) -> fail())
                .onStatus(httpStatusCode -> httpStatusCode.isSameCodeAs(BAD_REQUEST),
                        (request, response) -> assertTrue(response.getStatusCode().isSameCodeAs(BAD_REQUEST)))
                .toEntity(Route.class);
    }

    @Test
    public void update() {

        Route route = new Route("LH2345", "DUS", "MUC");
        route.setId(101L);

        restClient.put()
                .uri("/routes")
                .contentType(APPLICATION_JSON)
                .body(route)
                .retrieve()
                .toBodilessEntity();

        Route changedRoute = routeRepository.findById(101L).get();

        Assertions.assertEquals(route.getFlightNumber(), changedRoute.getFlightNumber());
    }

    @Test
    public void delete() {

        restClient.delete().uri("/routes/103").retrieve().toBodilessEntity();

        var optional = routeRepository.findById(103L);

        assertTrue(optional.isEmpty());
    }

    @Test
    public void get() {

        ResponseEntity<Route> routeEntity = restClient.get()
                .uri("/routes/101")
                .retrieve()
                .toEntity(Route.class);

        Assertions.assertEquals(HttpStatus.OK, routeEntity.getStatusCode());
        Assertions.assertNotNull(routeEntity.getBody());
        Assertions.assertEquals(101L, routeEntity.getBody().getId().longValue());

    }


    @Test
    public void getNotFound() {
        restClient.get()
                .uri("/routes/110000")
                .exchange(((clientRequest, clientResponse) -> {
                    assertTrue(clientResponse.getStatusCode().isSameCodeAs(BAD_REQUEST));
                    var objectMapper = new ObjectMapper();
                    return objectMapper.readValue(clientResponse.getBody(), ProblemDetail.class);
                }));
    }

    @Test
    public void getPersistenceException() throws Exception {
        restClient.get()
                .uri("/routes/120000")
                .exchange(((clientRequest, clientResponse) -> {
                    assertTrue(clientResponse.getStatusCode().isSameCodeAs(BAD_REQUEST));
                    var objectMapper = new ObjectMapper();
                    return objectMapper.readValue(clientResponse.getBody(), ProblemDetail.class);
                }));
    }

    @Test
    public void getAll() {
        ResponseEntity<Iterable<Route>> routeResponse  = restClient.get()
                .uri("/routes")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});

        Assertions.assertEquals(HttpStatus.OK, routeResponse.getStatusCode());
        Assertions.assertNotNull(routeResponse.getBody());
        Assertions.assertNotNull(routeResponse.getBody().iterator().hasNext());
    }

}