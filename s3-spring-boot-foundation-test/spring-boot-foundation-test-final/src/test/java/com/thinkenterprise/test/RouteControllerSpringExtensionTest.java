package com.thinkenterprise.test;

import com.thinkenterprise.Application;
import com.thinkenterprise.controller.RouteController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Application.class)
public class RouteControllerSpringExtensionTest {

    @Autowired
    RouteController routeController;

    @Test
    public void testHelloWorldOnController() {
        String result = routeController.helloWorld();
        assertThat(result).isEqualTo("Hello World");
    }
}
