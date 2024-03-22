package com.thinkenterprise.wiring;

import com.thinkenterprise.repository.RouteRepository;
import com.thinkenterprise.service.WiringRouteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean//("my")
    public WiringRouteService myRouteService(RouteRepository routeRepository) {
        return new WiringRouteService(routeRepository);
    }

    @Bean//("other")
    public WiringRouteService otherRouteService(RouteRepository routeRepository) {
        return new WiringRouteService(routeRepository);
    }

}
