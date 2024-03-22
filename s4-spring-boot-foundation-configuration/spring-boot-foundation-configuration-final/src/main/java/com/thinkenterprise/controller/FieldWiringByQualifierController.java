package com.thinkenterprise.controller;

import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.service.WiringRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fqwiring/")
public class FieldWiringByQualifierController {

    @Autowired
    @Qualifier("myRouteService")
    private WiringRouteService myRouteService;

    @Autowired
    @Qualifier("otherRouteService")
    private WiringRouteService otherRouteService;

    @RequestMapping("/my")
    public List<Route> myRoutes() {
        return myRouteService.listRoutes();
    }

    @RequestMapping("/other")
    public List<Route> otherRoutes() {
        return otherRouteService.listRoutes();
    }


}
