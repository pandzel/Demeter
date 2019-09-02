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

import com.panforge.demeter.server.elements.QueryResult;
import com.panforge.demeter.server.elements.RecordData;
import java.util.UUID;

/**
 * Records DAO.
 */
public interface RecordsDao {
  /**
   * Lists all records.
   * @param page
   * @return records
   */
  QueryResult<RecordData> listRecords(Integer page);
  
  /**
   * Reads record info.
   * @param id record id
   * @return record info
   */
  RecordData readRecord(UUID id);
  
  /**
   * Creates new record.
   * @param recordData record data
   * @return actual record data
   */
  RecordData createRecord(RecordData recordData);
  
  /**
   * Deletes record.
   * @param id record id
   * @return <code>true</code> if update successful.
   */
  boolean deleteRecord(UUID id);
  
  /**
   * Updates record.
   * @param recordData record data
   * @return <code>true</code> if update successful.
   */
  boolean updateRecord(RecordData recordData);
}
