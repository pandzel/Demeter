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

import com.panforge.demeter.server.ConfigDao;
import com.panforge.demeter.server.elements.ConfigData;
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
 * Config controller.
 */
@RestController
@RequestMapping("/rest")
public class ConfigController {
  private final Logger LOG = LoggerFactory.getLogger(ConfigController.class);
  
  private final ConfigDao dao;
  
  @Autowired 
  public ConfigController(ConfigDao dao) {
    this.dao = dao;
  }
  
  @PostConstruct
  public void construct() {
    LOG.info(String.format("%s created.", this.getClass().getSimpleName()));
  }
  
  @PreDestroy
  public void destroy() {
    LOG.info(String.format("%s destroyed.", this.getClass().getSimpleName()));
  }
  
  @RequestMapping(value = "/config", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ConfigData> list(HttpServletRequest request) {
    try {
      LOG.debug(String.format("Received request '%s'", request.getQueryString()));
      return new ResponseEntity<>(dao.loadConfig(), HttpStatus.OK);
    } catch (Exception ex) {
      LOG.error(String.format("Error processing request '%s'", request.getQueryString()), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
}
