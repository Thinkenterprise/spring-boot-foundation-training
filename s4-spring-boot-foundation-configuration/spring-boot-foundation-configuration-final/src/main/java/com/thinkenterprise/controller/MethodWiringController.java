package com.thinkenterprise.controller;

import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Nicht Best Practice!!!
@RestController
@RequestMapping("/mwiring/")
public class MethodWiringController {

    private RouteService routeService;

    @RequestMapping("/routes")
    public List<Route> myRoutes() {
        return routeService.findAll();
    }

    @Autowired
    public void setRouteService(RouteService routeService) {
        this.routeService = routeService;
    }
}
