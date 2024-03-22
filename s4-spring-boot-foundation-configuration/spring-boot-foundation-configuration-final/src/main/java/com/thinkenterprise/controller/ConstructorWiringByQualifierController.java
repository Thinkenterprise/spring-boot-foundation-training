package com.thinkenterprise.controller;

import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.service.WiringRouteService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cqwiring/")
public class ConstructorWiringByQualifierController {

    private final WiringRouteService myRouteService;
    private final WiringRouteService otherRouteService;

    public ConstructorWiringByQualifierController(@Qualifier("myRouteService") WiringRouteService myRouteService,
                                                  @Qualifier("otherRouteService") WiringRouteService otherRouteService) {
        this.myRouteService = myRouteService;
        this.otherRouteService = otherRouteService;
    }

    @RequestMapping("/my")
    public List<Route> myRoutes() {
        return myRouteService.listRoutes();
    }

    @RequestMapping("/other")
    public List<Route> otherRoutes() {
        return otherRouteService.listRoutes();
    }
}
