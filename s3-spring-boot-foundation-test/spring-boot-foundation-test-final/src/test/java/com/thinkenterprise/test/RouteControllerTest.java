package com.thinkenterprise.test;

import com.thinkenterprise.controller.RouteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RouteControllerTest {
	
	@Autowired
	private RouteController routeController;

	@LocalServerPort
	int port;

	private RestClient restClient;

	@BeforeEach
	void setUp() {
		restClient = RestClient.create("http://localhost:" + port);
	}
	
	@Test
	public void testHelloWorld() {
		String result = restClient.get().uri("/helloWorld").retrieve().body(String.class);
		assertThat(result).isEqualTo("Hello World");
	}
	
	@Test
	public void testHelloWorldOnController() {
		String result = routeController.helloWorld();
		assertThat(result).isEqualTo("Hello World");
	}
}
