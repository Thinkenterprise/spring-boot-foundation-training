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

package com.thinkenterprise.service;

import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.domain.route.SomethingGoesWrongException;
import com.thinkenterprise.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class RouteService {
   
	@Autowired
    private RouteRepository routeRepository;

	@Transactional(readOnly = true)
    public List<Route> findAll() {
        return routeRepository.findAll();
    }

   
    
    @Transactional(rollbackFor = SomethingGoesWrongException.class)
    public void unitOfWorkWithException() {
    	
    	Route routeFirst = new Route("LH7933D","BER","MUC");
    	Route routeRecond = new Route(null,"DUS","MUC");
    	Route routeThird = new Route("LH7935D","CGN","MUC");
    	
    	try {
    		routeRepository.save(routeFirst);
        	routeRepository.save(routeRecond);
        	routeRepository.save(routeThird);
		} catch (DataAccessException e) {
			throw new SomethingGoesWrongException("SomethingGoesWrongException");
		}
    	
    }
    
    @Transactional
    public void unitOfWork() {
    	
    	Route routeFirst = new Route("LH7933D","BER","MUC");
    	Route routeRecond = new Route(null,"DUS","MUC");
    	Route routeThird = new Route("LH7935D","CGN","MUC");
    	
    	routeRepository.save(routeFirst);
    	routeRepository.save(routeRecond);
    	routeRepository.save(routeThird);
    	
    }
    
    
    
    
}
