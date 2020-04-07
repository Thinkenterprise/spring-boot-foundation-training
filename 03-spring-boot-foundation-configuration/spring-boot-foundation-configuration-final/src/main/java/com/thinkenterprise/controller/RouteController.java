package com.thinkenterprise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.service.RouteService;

@RestController
public class RouteController {
	
	@Autowired
	private RouteService routeService;
	
	@RequestMapping("/routes")
	public List<Route> routes() {
		return routeService.findAll();
	}


}
