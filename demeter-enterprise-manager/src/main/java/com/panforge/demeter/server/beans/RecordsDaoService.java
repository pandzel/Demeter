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
import com.panforge.demeter.server.RecordsDao;
import com.panforge.demeter.server.elements.QueryResult;
import com.panforge.demeter.server.elements.RecordData;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Records DAO service.
 */
@Service
public class RecordsDaoService implements RecordsDao {
  private final Logger LOG = LoggerFactory.getLogger(RecordsDaoService.class);
  private final long PAGE_SIZE = 50;
  
  private Connection conn;
  
  /**
   * Creates instance of the DAO service
   * @param conn connection
   */
  @Autowired
  public RecordsDaoService(Connection conn) {
    this.conn = conn;
  }
  
  @Override
  public QueryResult<RecordData> listRecords(Integer page) {
    Row one = conn.execute("select counter from counter where table_name = 'records'").one();
    long total = one != null ? one.getLong("counter") : 0;
    
    ResultSet rs = conn.execute("select * from records");
    
    QueryResult<RecordData> queryResult = new QueryResult<>();
    queryResult.total = new Long(total);
    queryResult.page = page!=null? page: 0L;
    queryResult.pageSize = PAGE_SIZE;
    queryResult.data = StreamSupport.stream(rs.spliterator(), false)
            .skip(page!=null? page * PAGE_SIZE: 0)
            .limit(PAGE_SIZE)
            .map(row -> {
              RecordData recordData = new RecordData();
              readRow(recordData, row);
              return recordData;
            })
            .collect(Collectors.toList());

    return queryResult;
  }
  
  @Override
  public RecordData readRecord(UUID id) {
    PreparedStatement stmt = conn.prepare("select * from records where id = ?");
    BoundStatement bound = stmt.bind(id);
    
    ResultSet rs = conn.execute(bound);
    Row row = rs.one();
    
    if (row==null) return null;
    
    RecordData recordInfo = new RecordData();
    readRow(recordInfo, row);
    
    return recordInfo;
  }

  @Override
  public RecordData createRecord(RecordData recordData) {

    RecordData fromDatabase = new RecordData();
    
    fromDatabase.id = UUID.randomUUID();
    fromDatabase.title = StringUtils.trimToEmpty(recordData.title);
    fromDatabase.creator = StringUtils.trimToEmpty(recordData.creator);
    fromDatabase.subject = StringUtils.trimToEmpty(recordData.subject);
    fromDatabase.description = StringUtils.trimToEmpty(recordData.description);
    fromDatabase.publisher = StringUtils.trimToEmpty(recordData.publisher);
    fromDatabase.contributor = StringUtils.trimToEmpty(recordData.contributor);
    fromDatabase.date = recordData.date;
    fromDatabase.format = StringUtils.trimToEmpty(recordData.format);
    fromDatabase.identifier = StringUtils.trimToEmpty(recordData.identifier);
    fromDatabase.source = StringUtils.trimToEmpty(recordData.source);
    fromDatabase.language = StringUtils.trimToEmpty(recordData.language);
    fromDatabase.relation = StringUtils.trimToEmpty(recordData.relation);
    fromDatabase.coverage = StringUtils.trimToEmpty(recordData.coverage);
    fromDatabase.rights = StringUtils.trimToEmpty(recordData.rights);
    
    PreparedStatement stmt = conn.prepare("insert into records (id, title, creator, subject, description, publisher, contributor, date, format, identifier, source, language, relation, coverage, rights) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    BoundStatement bound = stmt.bind(
      fromDatabase.id,
      fromDatabase.title, fromDatabase.creator, fromDatabase.subject, fromDatabase.description, fromDatabase.publisher, 
      fromDatabase.contributor, fromDatabase.date, fromDatabase.format, fromDatabase.identifier, fromDatabase.source, 
      fromDatabase.language, fromDatabase.relation, fromDatabase.coverage, fromDatabase.rights
    );
    ResultSet result = conn.execute(bound);
    
    boolean success = result!=null && result.getExecutionInfo().getErrors().isEmpty();
    if (success) {
      conn.execute("update counter set counter = counter+1 where table_name = 'records'");
    }
    
    return success? fromDatabase: null;
  }

  @Override
  public boolean deleteRecord(UUID id) {
    conn.execute(conn.prepare("delete from collections where recordId = ?").bind(id));
    
    PreparedStatement stmt = conn.prepare("delete from records where id = ?");
    BoundStatement bound = stmt.bind(id);
    ResultSet result = conn.execute(bound);
    
    boolean success = result!=null && result.getExecutionInfo().getErrors().isEmpty();
    if (success) {
      conn.execute("update counter set counter = counter-1 where table_name = 'records'");
    }
    
    return success;
  }

  @Override
  public boolean updateRecord(RecordData recordData) {

    RecordData fromDatabase = new RecordData();
    
    fromDatabase.id = recordData.id!=null? recordData.id: UUID.randomUUID();
    fromDatabase.title = StringUtils.trimToEmpty(recordData.title);
    fromDatabase.creator = StringUtils.trimToEmpty(recordData.creator);
    fromDatabase.subject = StringUtils.trimToEmpty(recordData.subject);
    fromDatabase.description = StringUtils.trimToEmpty(recordData.description);
    fromDatabase.publisher = StringUtils.trimToEmpty(recordData.publisher);
    fromDatabase.contributor = StringUtils.trimToEmpty(recordData.contributor);
    fromDatabase.date = recordData.date;
    fromDatabase.format = StringUtils.trimToEmpty(recordData.format);
    fromDatabase.identifier = StringUtils.trimToEmpty(recordData.identifier);
    fromDatabase.source = StringUtils.trimToEmpty(recordData.source);
    fromDatabase.language = StringUtils.trimToEmpty(recordData.language);
    fromDatabase.relation = StringUtils.trimToEmpty(recordData.relation);
    fromDatabase.coverage = StringUtils.trimToEmpty(recordData.coverage);
    fromDatabase.rights = StringUtils.trimToEmpty(recordData.rights);

    PreparedStatement updateStmt = conn.prepare(
            "update records set title = ?, creator = ?, subject = ?, description = ?, publisher = ?, contributor = ?, date = ?, format = ?, identifier = ?, source = ?, language = ?, relation = ?, coverage = ?, rights = ? where id = ?"
    );
    BoundStatement updateBound = updateStmt.bind(
            fromDatabase.title, fromDatabase.creator, fromDatabase.subject, fromDatabase.description, fromDatabase.publisher, 
            fromDatabase.contributor, fromDatabase.date, fromDatabase.format, fromDatabase.identifier, fromDatabase.source, 
            fromDatabase.language, fromDatabase.relation, fromDatabase.coverage, fromDatabase.rights, fromDatabase.id
    );
    ResultSet result = conn.execute(updateBound);
    
    boolean success = result!=null && result.getExecutionInfo().getErrors().isEmpty();
    
    return success;
  }

  @Override
  public QueryResult<KeyValue<String, String>> listSets(UUID recordId, Integer page) {
    long total = conn.execute(conn.prepare("select count(*) from collections where recordId = ?").bind(recordId)).one().getLong(0);
    ResultSet rows = conn.execute(conn.prepare("select setId from collections where recordId = ?").bind(recordId));
    
    List<KeyValue<String, String>> records = StreamSupport.stream(rows.spliterator(), false)
            .skip(page!=null? page * PAGE_SIZE: 0)
            .limit(PAGE_SIZE)
            .map(row -> {
              UUID setId = row.getUuid(0);
              Row setRow = conn.execute(conn.prepare("select setName from sets where id = ?").bind(setId)).one();
              if (setRow==null) return null;
              String setName = setRow.getString(0);
              return new DefaultKeyValue<String,String>(setId.toString(), setName);
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
  
  private void readRow(RecordData recordData, Row row) {
    recordData.id = row.getUuid("id");
    recordData.title = row.getString("title");
    recordData.creator = row.getString("creator");
    recordData.subject = row.getString("subject");
    recordData.description = row.getString("description");
    recordData.publisher = row.getString("publisher");
    recordData.contributor = row.getString("contributor");
    recordData.date = row.getLocalDate("date");
    recordData.format = row.getString("format");
    recordData.identifier = row.getString("identifier");
    recordData.source = row.getString("source");
    recordData.language = row.getString("language");
    recordData.relation = row.getString("relation");
    recordData.coverage = row.getString("coverage");
    recordData.rights = row.getString("rights");
  }
  
}
