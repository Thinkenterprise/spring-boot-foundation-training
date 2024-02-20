package com.thinkenterprise.test;


import com.thinkenterprise.domain.route.Route;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.List;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class RouteControllerTest {

	private RestClient restClient;

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		restClient = RestClient.create("http://localhost:"+port);
	}

	@Test
	public void getAll_Authorized() {

		ResponseEntity<List<Route>> routeResponse = restClient.get()
				.uri("/routes")
				.headers(httpHeaders -> httpHeaders.setBasicAuth("user", "password"))
				.retrieve()
				.toEntity(new ParameterizedTypeReference<>() {
				});

		Assertions.assertEquals (HttpStatus.OK, routeResponse.getStatusCode());
		Assertions.assertNotNull (routeResponse.getBody());

	}

	@Test
	public void getAll_Unauthorized() {
		restClient.get()
				.uri("/routes")
				.headers(httpHeaders -> httpHeaders.setBasicAuth("user2", "wrong_password"))
				.exchange((clientRequest, clientResponse) -> {
					assertThat(clientResponse.getStatusCode()).isEqualTo(UNAUTHORIZED);
					return empty();
				});

	}
}