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
              
              setData.id = row.getUuid("id");
              setData.setSpec = row.getString("setSpec");
              setData.setName = row.getString("setName");
              
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
    
    setInfo.id = row.getUuid("id");
    setInfo.setSpec = row.getString("setSpec");
    setInfo.setName = row.getString("setName");
    
    // TODO: read set descriptions
    
    return setInfo;
  }
}
