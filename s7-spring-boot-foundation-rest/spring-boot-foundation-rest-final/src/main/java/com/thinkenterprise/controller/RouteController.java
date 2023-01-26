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

package com.thinkenterprise.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.thinkenterprise.domain.core.Problem;
import com.thinkenterprise.domain.route.Route;
import com.thinkenterprise.domain.route.RouteNotFoundException;
import com.thinkenterprise.service.RouteService;

import jakarta.persistence.PersistenceException;


@RestController
@RequestMapping("routes")
public class RouteController {

    @Autowired
    private RouteService service;

  
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Route>> getAll() {
		Iterable<Route> result = service.findAll();
		return new ResponseEntity<Iterable<Route>>(service.findAll(),HttpStatus.OK);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Route> get(@PathVariable(value = "id") Long id) {
		if(id==110000L) throw new RouteNotFoundException();
		if(id==120000L) throw new PersistenceException("Route Get Persistence Exception");
		return new ResponseEntity<Route>(service.findById(id),HttpStatus.OK); 
	 }
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
		service.deleteById(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);   	
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> put(@Validated @RequestBody Route entity) {		
		service.save(entity);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);    
	}	
	
	@RequestMapping(method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Route> post(@Validated @RequestBody Route entity) {	
	    return new ResponseEntity<Route>(service.save(entity),HttpStatus.CREATED);
	}	
	
	 @RequestMapping(value="search",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<Iterable<Route>> findByDeparture(@RequestParam(value = "departure") String departure) {
		 return new ResponseEntity<Iterable<Route>>(service.findAll(),HttpStatus.OK);
	 }
	 
	 @ExceptionHandler(value = RouteNotFoundException.class)
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 public Problem exception(RouteNotFoundException exception) {
	     return new Problem(URI.create("http://thinkenterprise.com"), 
	    		            "Route not found", 
	    		            HttpStatus.BAD_REQUEST, 
	    		            exception.getMessage());
	 }
	 	 
}
