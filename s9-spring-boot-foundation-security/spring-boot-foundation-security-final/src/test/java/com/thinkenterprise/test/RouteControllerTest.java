package com.thinkenterprise.test;


import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.thinkenterprise.domain.route.Route;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class RouteControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void getAll_Authorized() throws IOException {

		final ResponseEntity<List<Route>> routeResponse =
				testRestTemplate.exchange("/routes",
						HttpMethod.GET, null, new ParameterizedTypeReference<List<Route>>(){});

		Assertions.assertEquals (HttpStatus.OK, routeResponse.getStatusCode());
		Assertions.assertNotNull (routeResponse.getBody());

	}

	@Test
	public void getAll_Unauthorized() {

		testRestTemplate = testRestTemplate.withBasicAuth("user2", "something");

		final ResponseEntity<List<Route>> routeResponse =
				testRestTemplate.exchange("/routes",
						HttpMethod.GET, null, new ParameterizedTypeReference<List<Route>>(){});

		Assertions.assertEquals (HttpStatus.UNAUTHORIZED, routeResponse.getStatusCode());
	}

	@Test
	public void getAll_Authorized2() throws IOException {

		final ResponseEntity<List<Route>> routeResponse = testRestTemplate.exchange("/routes", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Route>>() {
				});

		Assertions.assertEquals(HttpStatus.OK, routeResponse.getStatusCode());
		Assertions.assertNotNull(routeResponse.getBody());

	}

}