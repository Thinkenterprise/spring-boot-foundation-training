package com.thinkenterprise.controller;

import com.thinkenterprise.wiring.ArbitraryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nwiring/")
public class WiringByNameController {

    @Autowired
    private ArbitraryInterface componentA;

    @Autowired
    private ArbitraryInterface componentB;

    @RequestMapping("/a")
    public String myRoutes() {
        return componentA.getMessage();
    }

    @RequestMapping("/b")
    public String otherRoutes() {
        return componentB.getMessage();
    }
}
