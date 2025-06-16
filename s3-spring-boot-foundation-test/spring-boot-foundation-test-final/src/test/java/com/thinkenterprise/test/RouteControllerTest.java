package com.thinkenterprise.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.client.RestClient;

import com.thinkenterprise.controller.RouteController;
import com.thinkenterprise.service.RouteService;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class RouteControllerTest {
	
	@Autowired
	RouteController routeController;

	@LocalServerPort
	int port;

	@MockitoBean
	RouteService routeService;

	@TestBean
	private RestClient restClient;

	@Autowired
	private MockMvcTester mockMvcTester;

	public static RestClient restClient() {
		return RestClient.create("http://localhost:" + 8080);
	}

	@BeforeEach
	void setUp() {
		when(routeService.getHelloWorld()).thenReturn("Hello World");
	}
	
	@Test
	public void testHelloWorld() {
		String result = restClient.get().uri("/helloWorld").retrieve().body(String.class);
		assertThat(result).isEqualTo("Hello World");
	}

	@Test
	public void testHelloWorldWithMVCMock() {
		var result = mockMvcTester.get().uri("/").exchange();
		assertThat(result).hasStatusOk();
		assertThat(result).hasViewName("index");
	}
	
	@Test
	public void testHelloWorldOnController() {
		String result = routeController.helloWorld();
		assertThat(result).isEqualTo("Hello World");
	}
}
