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
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.panforge.demeter.server.SetsDao;
import com.panforge.demeter.server.elements.QueryResult;
import java.util.List;
import java.util.stream.StreamSupport;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

/**
 * Sets DAO service.
 */
@Service
public class SetsDaoService implements SetsDao {
  private final Logger LOG = LoggerFactory.getLogger(SetsDaoService.class);
  private final long PAGE_SIZE = 50;
  
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
  public QueryResult<SetData> listSets(Integer page) {
    Row one = conn.execute("select counter from counter where table_name = 'sets'").one();
    long total = one != null ? one.getLong("counter") : 0;
    
    ResultSet rs = conn.execute("select * from sets");
    
    QueryResult<SetData> queryResult = new QueryResult<>();
    queryResult.total = new Long(total);
    queryResult.page = page!=null? page: 0L;
    queryResult.pageSize = PAGE_SIZE;
    queryResult.data = StreamSupport.stream(rs.spliterator(), false)
            .skip(page!=null? page * PAGE_SIZE: 0)
            .limit(PAGE_SIZE)
            .map(row -> {
              SetData setData = new SetData();
              readRow(setData, row);
              return setData;
            })
            .collect(Collectors.toList());

    return queryResult;
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
    
    boolean success = result!=null && result.getExecutionInfo().getErrors().isEmpty();
    if (success) {
      conn.execute("update counter set counter = counter+1 where table_name = 'sets'");
    }
    
    return success? fromDatabase: null;
  }

  @Override
  public boolean deleteSet(UUID id) {
    ResultSet colSet = conn.execute(conn.prepare("select * from collections where setId = ?").bind(id));
    StreamSupport.stream(colSet.spliterator(), false).forEach(row -> {
      UUID recordId = row.getUuid("recordId");
      conn.execute(conn.prepare("delete from collections where recordId = ? and setId = ?").bind(recordId, id));
    });
    
    PreparedStatement stmt = conn.prepare("delete from sets where id = ?");
    BoundStatement bound = stmt.bind(id);
    ResultSet result = conn.execute(bound);
    
    boolean success = result!=null && result.getExecutionInfo().getErrors().isEmpty();
    if (success) {
      conn.execute("update counter set counter = counter-1 where table_name = 'sets'");
    }
    
    return success;
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
    
    boolean success = result!=null && result.getExecutionInfo().getErrors().isEmpty();
    
    return success;
  }

  @Override
  public QueryResult<KeyValue<String, String>> listRecords(UUID setId, Integer page) {
    long total = conn.execute(conn.prepare("select count(*) from collections where setId = ?").bind(setId)).one().getLong(0);
    ResultSet rows = conn.execute(conn.prepare("select recordId from collections where setId = ?").bind(setId));
    
    List<KeyValue<String, String>> records = StreamSupport.stream(rows.spliterator(), false)
            .skip(page!=null? page * PAGE_SIZE: 0)
            .limit(PAGE_SIZE)
            .map(row -> {
              UUID recordId = row.getUuid(0);
              Row recordRow = conn.execute(conn.prepare("select title from records where id = ?").bind(recordId)).one();
              if (recordRow==null) return null;
              String title = recordRow.getString(0);
              return new DefaultKeyValue<String,String>(recordId.toString(), title);
            })
            .filter(kv -> kv!=null)
            .collect(Collectors.toList());
    
    QueryResult<KeyValue<String, String>> result = new QueryResult<>();
    result.page = page!=null? page.longValue(): 0;
    result.pageSize = PAGE_SIZE;
    result.total = total;
    result.data = records;
    
    return result;
  }

  @Override
  public boolean putCollection(UUID setId, UUID recordId) {
    ResultSet result = conn.execute(conn.prepare("insert into collections (setId, recordId) values (?, ?)").bind(setId, recordId));
    boolean success = result!=null && result.getExecutionInfo().getErrors().isEmpty();
    return success;
  }

  @Override
  public boolean delCollection(UUID setId, UUID recordId) {
    ResultSet result = conn.execute(conn.prepare("delete from collections where setId = ? and recordId = ?").bind(setId, recordId));
    boolean success = result!=null && result.getExecutionInfo().getErrors().isEmpty();
    return success;
  }
  
  private void readRow(SetData setData, Row row) {
    setData.id = row.getUuid("id");
    setData.setSpec = row.getString("setSpec");
    setData.setName = row.getString("setName");
  }
}
