package com.thinkenterprise.wiring;

import org.springframework.stereotype.Component;

@Component//("x")
public class ComponentA implements ArbitraryInterface {
    @Override
    public String getMessage() {
        return "Hello from " + this.getClass().getName();
    }
}
