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

package com.thinkenterprise.controller.exception;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.thinkenterprise.domain.core.Problem;
import com.thinkenterprise.domain.route.RouteNotFoundException;

import jakarta.persistence.PersistenceException;

@ControllerAdvice
public class PersistenceControllerAdvice extends ResponseEntityExceptionHandler {
    

	@ExceptionHandler(value = PersistenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail exception(PersistenceException exception) {
		
		        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		        problemDetail.setType(URI.create("http://thinkenterprise.com/PersistenceException"));
		        problemDetail.setTitle( "General Persistence Exception");
		        problemDetail.setDetail(exception.getMessage());
		        return problemDetail;
		 }

}
