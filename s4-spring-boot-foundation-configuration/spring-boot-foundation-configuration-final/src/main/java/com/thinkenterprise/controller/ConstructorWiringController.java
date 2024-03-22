package com.thinkenterprise.controller;

import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.service.RouteService;
import com.thinkenterprise.service.WiringRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cwiring/")
public class ConstructorWiringController {

    private final RouteService routeService;
    private final WiringRouteService wiringRouteService;

    @Autowired // Theoretisch nicht nötig, aber der Lesbarkeit wegen sinnvoll.
    public ConstructorWiringController(RouteService routeService, @Qualifier("myRouteService") WiringRouteService wiringRouteService) {
        this.routeService = routeService;
        this.wiringRouteService = wiringRouteService;
    }

    @RequestMapping("/routes")
    public List<Route> myRoutes() {
        return routeService.findAll();
    }

    @RequestMapping("/wiring")
    public List<Route> wiringRoutes() {
        return wiringRouteService.listRoutes();
    }

}
