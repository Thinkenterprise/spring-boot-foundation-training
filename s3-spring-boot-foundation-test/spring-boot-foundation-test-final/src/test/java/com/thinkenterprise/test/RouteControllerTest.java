package com.thinkenterprise.test;

import com.thinkenterprise.controller.RouteController;
import com.thinkenterprise.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class RouteControllerTest {

    private static final String HELLO_WORLD = "Hello World";

    @Autowired
    RouteController routeController;

    @LocalServerPort
    int port;

    @MockitoBean
    RouteService routeService;

    private RestTestClient restClient;

    @Autowired
    private MockMvcTester mockMvcTester;

    @BeforeEach
    void setUp() {
        restClient = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        when(routeService.getHelloWorld()).thenReturn(HELLO_WORLD);
    }

    @Test
    public void testHelloWorld() {
        restClient.get().uri("/helloWorld")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(HELLO_WORLD);
    }

    @Test
    public void testHelloWorldMockMvcTester() {
        var result = mockMvcTester.get().uri("/helloWorld").exchange();
        assertThat(result).hasStatusOk().bodyText().contains("Hello World");
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
