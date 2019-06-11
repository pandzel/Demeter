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
package com.panforge.demeter.server.beans;

import com.panforge.demeter.server.Connection;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Connection service.
 */
@Service
public class ConnectionService implements Connection {
  private final Logger LOG = LoggerFactory.getLogger(ConnectionService.class);

  private CqlSession session;
  
  @PostConstruct
  public void construct() {
    session = CqlSession.builder().withKeyspace(CqlIdentifier.fromCql("demeter")).build();
    LOG.info(String.format("%s created.", this.getClass().getSimpleName()));
  }
  
  @PreDestroy
  public void destroy() {
    if (session != null) {
      session.close();
    }
    LOG.info(String.format("%s destroyed.", this.getClass().getSimpleName()));
  }
  
  @Override
  public ResultSet execute(String cql) {
    return session.execute(cql);
  }

  @Override
  public ResultSet execute(BoundStatement bound) {
    return session.execute(bound);
  }
  
  @Override
  public PreparedStatement prepare(String cql) {
    return session.prepare(cql);
  }
}
