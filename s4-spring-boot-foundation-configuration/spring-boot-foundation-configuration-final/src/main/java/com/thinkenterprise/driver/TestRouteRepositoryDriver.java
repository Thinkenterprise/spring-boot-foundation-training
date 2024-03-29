/*
 * Copyright (C) 2016 Thinkenterprise
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * @author Rafael Kansy
 * @author Michael Schaefer
 */

package com.thinkenterprise.driver;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.thinkenterprise.domain.route.Route;

import jakarta.annotation.PostConstruct;

public class TestRouteRepositoryDriver implements RouteRepositoryDriver {

	@Value("${route.count}")
    private short routeCount;

    private List<Route> routeList = new ArrayList<>();
    private List<String> flightNumberList = new ArrayList<>();
    private List<String> departureList = new ArrayList<>();
    private List<String> destinationList = new ArrayList<>();

    public TestRouteRepositoryDriver() {
        flightNumberList.add("LH7902");
        flightNumberList.add("LH1602");

        departureList.add("MUC");
        departureList.add("FRA");

        destinationList.add("MUC");
        destinationList.add("FRA");
    }

    @PostConstruct
    public void initRoutesList() {
        for (short i = 0; i < routeCount; i++) {
            String flightNumber = flightNumberList.get(i);
            String departure = departureList.get(i);
            String destination = destinationList.get(i);

            Route route = new Route(flightNumber, departure, destination);
            route.addScheduledDaily();
            route.setDepartureTime(LocalTime.of(9 - i, 30));
            route.setArrivalTime(LocalTime.of(14 - i, 0));
            routeList.add(route);
        }
    }

    @Override
    public List<Route> getRouteList() {
        return routeList;
    }
}
