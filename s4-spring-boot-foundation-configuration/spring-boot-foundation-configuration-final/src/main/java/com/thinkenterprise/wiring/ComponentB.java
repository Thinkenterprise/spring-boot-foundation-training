package com.thinkenterprise.wiring;

import org.springframework.stereotype.Component;

@Component//("y")
public class ComponentB implements ArbitraryInterface {
    @Override
    public String getMessage() {
        return "Hello from " + this.getClass().getName();
    }
}
