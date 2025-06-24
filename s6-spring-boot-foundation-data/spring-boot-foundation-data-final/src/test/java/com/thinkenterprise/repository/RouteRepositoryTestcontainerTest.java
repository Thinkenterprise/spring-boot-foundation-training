package com.thinkenterprise.repository;

import com.thinkenterprise.domain.route.Route;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class RouteRepositoryTestcontainerTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:12-alpine");

    @Autowired
    private RouteRepository routeRepository;

    @Test
    void containerStartingAndRunning() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void create() {
        long expected = routeRepository.count() + 1;

        Route entity = new Route("LH400", "MUC", "NYC");
        routeRepository.save(entity);

        long actual = routeRepository.count();
        Assertions.assertEquals(expected, actual);
    }
}
