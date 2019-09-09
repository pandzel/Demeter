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

import com.panforge.demeter.server.elements.OperationStatus;
import com.panforge.demeter.server.elements.SetData;
import com.panforge.demeter.server.elements.SetInfo;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.panforge.demeter.server.SetsDao;
import com.panforge.demeter.server.elements.QueryResult;
import org.apache.commons.collections4.KeyValue;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Sets controller.
 */
@RestController
@RequestMapping("/rest")
public class SetController {
  private final Logger LOG = LoggerFactory.getLogger(SetController.class);
  
  private final SetsDao dao;
  
  @Autowired 
  public SetController(SetsDao dao) {
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
  
  @RequestMapping(value = "/sets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<QueryResult<SetData>> list(HttpServletRequest request, @RequestParam(required = false) Integer page) {
    try {
      LOG.debug(String.format("Received request '%s'", request.getQueryString()));
      return new ResponseEntity<>(dao.listSets(page), HttpStatus.OK);
    } catch (Exception ex) {
      LOG.error(String.format("Error processing request '%s'", request.getQueryString()), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @RequestMapping(value = "/sets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SetData> post(HttpServletRequest request, @RequestBody SetData setData) {
    try {
      LOG.debug(String.format("Received request '%s'", request.getQueryString()));
      return new ResponseEntity<>(dao.createSet(setData), HttpStatus.OK);
    } catch (Exception ex) {
      LOG.error(String.format("Error processing request '%s'", request.getQueryString()), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @RequestMapping(value = "/sets/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> put(HttpServletRequest request, @PathVariable UUID id, @RequestBody SetData setData) {
    try {
      setData.id = id;
      LOG.debug(String.format("Received request '%s'", request.getQueryString()));
      boolean success = dao.updateSet(setData);
      OperationStatus opStat = success? new OperationStatus(id): new OperationStatus(id, "Error updating record");
      return new ResponseEntity<>(opStat, HttpStatus.OK);
    } catch (Exception ex) {
      LOG.error(String.format("Error processing request '%s'", request.getQueryString()), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @RequestMapping(value = "/sets/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SetInfo> read(HttpServletRequest request, @PathVariable UUID id) {
    try {
      LOG.debug(String.format("Received request '%s'", request.getQueryString()));
      return new ResponseEntity<>(dao.readSet(id), HttpStatus.OK);
    } catch (Exception ex) {
      LOG.error(String.format("Error processing request '%s'", request.getQueryString()), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @RequestMapping(value = "/sets/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<OperationStatus> delete(HttpServletRequest request, @PathVariable UUID id) {
    try {
      LOG.debug(String.format("Received request '%s'", request.getQueryString()));
      boolean success = dao.deleteSet(id);
      OperationStatus opStat = success? new OperationStatus(id): new OperationStatus(id, "Error deleting record");
      return new ResponseEntity<>(opStat, HttpStatus.OK);
    } catch (Exception ex) {
      LOG.error(String.format("Error processing request '%s'", request.getQueryString()), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @RequestMapping(value = "/sets/{id}/records", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<QueryResult<KeyValue<String,String>>> getRecords(HttpServletRequest request, @PathVariable UUID id, @RequestParam(required = false) Integer page) {
    try {
      LOG.debug(String.format("Received request '%s'", request.getQueryString()));
      QueryResult<KeyValue<String,String>> result = dao.listRecords(id, page);
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception ex) {
      LOG.error(String.format("Error processing request '%s'", request.getQueryString()), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @RequestMapping(value = "/sets/{id}/records/{recordId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> putRecord(HttpServletRequest request, @PathVariable UUID id, @PathVariable UUID recordId) {
    try {
      LOG.debug(String.format("Received request '%s'", request.getQueryString()));
      boolean success = dao.putCollection(id, recordId);
      OperationStatus opStat = success? new OperationStatus(id): new OperationStatus(id, "Error updating record");
      return new ResponseEntity<>(opStat, HttpStatus.OK);
    } catch (Exception ex) {
      LOG.error(String.format("Error processing request '%s'", request.getQueryString()), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @RequestMapping(value = "/sets/{id}/records/{recordId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OperationStatus> deleteRecord(HttpServletRequest request, @PathVariable UUID id, @PathVariable UUID recordId) {
    try {
      LOG.debug(String.format("Received request '%s'", request.getQueryString()));
      boolean success = dao.delCollection(id, recordId);
      OperationStatus opStat = success? new OperationStatus(id): new OperationStatus(id, "Error updating record");
      return new ResponseEntity<>(opStat, HttpStatus.OK);
    } catch (Exception ex) {
      LOG.error(String.format("Error processing request '%s'", request.getQueryString()), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
