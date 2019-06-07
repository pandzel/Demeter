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

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.panforge.demeter.server.Connection;
import com.panforge.demeter.server.elements.SetData;
import com.panforge.demeter.server.elements.SetInfo;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.panforge.demeter.server.SetsDao;

/**
 * Sets DAO service.
 */
@Service
public class SetsDaoService implements SetsDao {
  private final Logger LOG = LoggerFactory.getLogger(SetsDaoService.class);
  
  private Connection conn;
  
  /**
   * Creates instance of the DAO service
   * @param conn connection
   */
  @Autowired
  public SetsDaoService(Connection conn) {
    this.conn = conn;
  }
  
  @Override
  public List<SetData> listSets() {
    ResultSet rs = conn.execute("select * from sets");
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
    PreparedStatement stmt = conn.prepare("select * from sets where id = ?");
    BoundStatement bound = stmt.bind(id);
    
    ResultSet rs = conn.execute(bound);
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
    
    PreparedStatement stmt = conn.prepare("insert into sets (id, setSpec, setName) values (?, ?, ?)");
    BoundStatement bound = stmt.bind(fromDatabase.id, fromDatabase.setSpec, fromDatabase.setName);
    ResultSet result = conn.execute(bound);
    return result!=null && result.getExecutionInfo().getErrors().isEmpty()? fromDatabase: null;
  }

  @Override
  public boolean deleteSet(UUID id) {
    PreparedStatement stmt = conn.prepare("delete from sets where id = ?");
    BoundStatement bound = stmt.bind(id);
    ResultSet result = conn.execute(bound);
    return result!=null && result.getExecutionInfo().getErrors().isEmpty();
  }

  @Override
  public boolean updateSet(SetData setData) {

    SetData fromDatabase = new SetData();
    
    fromDatabase.id = setData.id!=null? setData.id: UUID.randomUUID();
    fromDatabase.setSpec = StringUtils.trimToEmpty(setData.setSpec);
    fromDatabase.setName = StringUtils.trimToEmpty(setData.setName);

    PreparedStatement updateStmt = conn.prepare("update sets set setSpec = ?, setName = ? where id = ?");
    BoundStatement updateBound = updateStmt.bind(fromDatabase.setSpec, fromDatabase.setName, fromDatabase.id);
    ResultSet result = conn.execute(updateBound);
    
    return result!=null && result.getExecutionInfo().getErrors().isEmpty();
  }
  
  private void readRow(SetData setData, Row row) {
    setData.id = row.getUuid("id");
    setData.setSpec = row.getString("setSpec");
    setData.setName = row.getString("setName");
  }
}
