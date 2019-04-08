/*
 * Copyright 2019 Piotr Andzel.
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
 */
package com.panforge.demeter.server.rest;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAI controller
 */
@RestController
public class OaiController {
  private final Logger LOG = LoggerFactory.getLogger(OaiController.class);
  
  @Autowired
  private com.panforge.demeter.service.Service service;
  
  @PostConstruct
  public void construct() {
    LOG.info(String.format("%s created.", this.getClass().getSimpleName()));
  }
  
  @PreDestroy
  public void destroy() {
    LOG.info(String.format("%s destroyed.", this.getClass().getSimpleName()));
  }
  
  @RequestMapping(value = "/oai", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
  public ResponseEntity<String> execute(HttpServletRequest request) {
    try {
      Map<String, String[]> parameters = request.getParameterMap();
      return new ResponseEntity<>(service.execute(parameters), HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
