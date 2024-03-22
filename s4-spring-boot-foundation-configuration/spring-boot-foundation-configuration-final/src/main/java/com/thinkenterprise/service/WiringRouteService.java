package com.thinkenterprise.service;

import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.repository.RouteRepository;

import java.util.List;

public class WiringRouteService {

    private final RouteRepository routeRepository;

    public WiringRouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> listRoutes() {
        return routeRepository.findAll();
    }
}
