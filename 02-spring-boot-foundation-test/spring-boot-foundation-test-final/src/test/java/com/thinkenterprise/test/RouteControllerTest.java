package com.thinkenterprise.test;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.thinkenterprise.controller.RouteController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RouteControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private RouteController routeController;

	
	@Test
	public void testHelloWorld() {
		
		String result = testRestTemplate.getForObject("/helloWorld", String.class);
		Assert.assertTrue(result.contentEquals("Hello World"));

	}
	
	@Test
	public void testHelloWorldOnController() {
		String result = routeController.helloWorld();
		Assert.assertTrue(result.contentEquals("Hello World"));
		
	}
	
	
}
