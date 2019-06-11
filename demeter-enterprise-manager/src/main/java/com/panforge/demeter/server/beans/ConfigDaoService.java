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

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.panforge.demeter.core.api.Config;
import com.panforge.demeter.server.ConfigDao;
import com.panforge.demeter.server.Connection;
import com.panforge.demeter.server.elements.ConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Config dao service.
 */
@Service
public class ConfigDaoService implements ConfigDao {
  private final Logger LOG = LoggerFactory.getLogger(ConfigDaoService.class);
  
  private Connection conn;
  
  /**
   * Creates instance of the DAO service
   * @param conn connection
   */
  @Autowired
  public ConfigDaoService(Connection conn) {
    this.conn = conn;
  }

  @Override
  public ConfigData loadConfig() {
    ConfigData configData = createDefaultConfigData();
    ResultSet rs = conn.execute("SELECT * FROM config where ID = 0");
    Row one = rs.one();
    if (one!=null) {
      configData.repositoryName = one.getString("repositoryName");
      configData.baseURL = one.getString("baseURL");
      configData.adminEmail = one.getList("adminEmail", String.class).stream().toArray(String[]::new);
    }
    return configData;
  }
  
  private ConfigData createDefaultConfigData() {
    Config config = new Config();
    ConfigData configData = new ConfigData();
    configData.repositoryName = config.repositoryName;
    configData.baseURL = config.baseURL;
    configData.adminEmail = config.adminEmail;
    return configData;
  }
}
