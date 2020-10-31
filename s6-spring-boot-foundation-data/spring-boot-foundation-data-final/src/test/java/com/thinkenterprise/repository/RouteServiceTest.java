/*
 * Copyright (C) 2019 Thinkenterprise
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
 * @author Michael Schaefer
 */

package com.thinkenterprise.repository;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.domain.route.SomethingGoesWrongException;
import com.thinkenterprise.service.RouteService;

@SpringBootTest
public class RouteServiceTest {

  
    @Autowired
    private RouteService routeService;
    
    
    @Test
    public void unitofWorkWithException()  {
       
    	try {
    		routeService.unitOfWorkWithException();
		} catch (SomethingGoesWrongException e) {
			
		}
    	
    	List<Route> routes =routeService.findAll();
    	Assertions.assertTrue(routes.size()==3);
    	
    }
    
    @Test
    public void unitofWork()  {
       
    	try {
    		routeService.unitOfWork();
		} catch (DataAccessException e) {
			
		}
    	
    	List<Route> routes =routeService.findAll();
    	Assertions.assertTrue(routes.size()==3);
    	
    }

    
}