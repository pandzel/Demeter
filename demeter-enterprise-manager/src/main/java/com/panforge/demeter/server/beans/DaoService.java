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

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.panforge.demeter.server.Dao;
import com.panforge.demeter.server.elements.SetData;
import com.panforge.demeter.server.elements.SetInfo;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * DAO service.
 */
@Service
public class DaoService implements Dao {
  private final Logger LOG = LoggerFactory.getLogger(DaoService.class);

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
  public CqlSession getSession() {
    return session;
  }
  
  @Override
  public List<SetData> listSets() {
    ResultSet rs = session.execute("select * from sets");
    return rs.all().stream()
            .map(row -> {
              SetData setData = new SetData();
              readRow(setData, row);
              return setData;
            })
            .collect(Collectors.toList());
  }
  
  @Override
  public SetInfo readSet(UUID id) {
    ResultSet rs = session.execute("select * from sets where id = "+id.toString());
    Row row = rs.one();
    
    if (row==null) return null;
    
    SetInfo setInfo = new SetInfo();
    readRow(setInfo, row);
    
    // TODO: read set descriptions
    
    return setInfo;
  }

  @Override
  public SetData createSet(SetData setData) {

    SetData fromDatabase = new SetData();
    
    fromDatabase.id = UUID.randomUUID();
    fromDatabase.setSpec = StringUtils.trimToEmpty(setData.setSpec);
    fromDatabase.setName = StringUtils.trimToEmpty(setData.setName);
    
    PreparedStatement stmt = session.prepare("insert into sets (id, setSpec, setName) values (?, ?, ?)");
    BoundStatement bound = stmt.bind(fromDatabase.id, fromDatabase.setSpec, fromDatabase.setName);
    ResultSet result = session.execute(bound);
    return result!=null && result.getExecutionInfo().getErrors().isEmpty()? fromDatabase: null;
  }

  @Override
  public boolean deleteSet(UUID id) {
    PreparedStatement stmt = session.prepare("delete from sets where id = ?");
    BoundStatement bound = stmt.bind(id);
    ResultSet result = session.execute(bound);
    return result!=null && result.getExecutionInfo().getErrors().isEmpty();
  }

  @Override
  public boolean updateSet(SetData setData) {

    SetData fromDatabase = new SetData();
    
    fromDatabase.id = setData.id!=null? setData.id: UUID.randomUUID();
    fromDatabase.setSpec = StringUtils.trimToEmpty(setData.setSpec);
    fromDatabase.setName = StringUtils.trimToEmpty(setData.setName);

    PreparedStatement updateStmt = session.prepare("update sets set setSpec = ?, setName = ? where id = ?");
    BoundStatement updateBound = updateStmt.bind(fromDatabase.setSpec, fromDatabase.setName, fromDatabase.id);
    ResultSet result = session.execute(updateBound);
    
    return result!=null && result.getExecutionInfo().getErrors().isEmpty();
  }
  
  private void readRow(SetData setData, Row row) {
    setData.id = row.getUuid("id");
    setData.setSpec = row.getString("setSpec");
    setData.setName = row.getString("setName");
  }
}
