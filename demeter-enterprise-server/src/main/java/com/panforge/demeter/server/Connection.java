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
package com.panforge.demeter.server;

import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Statement;

/**
 * Connection.
 */
public interface Connection {

  /**
   * Executes CQL query.
   * @param cql query
   * @return result
   */
  ResultSet execute(String cql);

  /**
   * Executes bound statement.
   * @param stmt  statement
   * @return result
   */
  ResultSet execute(Statement stmt);
  
  /**
   * Prepares statement.
   * @param cql query
   * @return statement
   */
  PreparedStatement prepare(String cql);
  
}
