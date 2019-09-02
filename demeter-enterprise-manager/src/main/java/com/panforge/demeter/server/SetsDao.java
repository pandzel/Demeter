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
import com.panforge.demeter.server.elements.SetData;
import com.panforge.demeter.server.elements.SetInfo;
import java.util.UUID;

/**
 * Sets DAO.
 */
public interface SetsDao {
  
  /**
   * Sets all sets data.
   * @param page page number
   * @return sets data
   */
  QueryResult<SetData> listSets(Integer page);
  
  /**
   * Reads set info.
   * @param id set id
   * @return set info
   */
  SetInfo readSet(UUID id);
  
  /**
   * Creates new set.
   * @param setData set data
   * @return actual set data
   */
  SetData createSet(SetData setData);
  
  /**
   * Deletes set.
   * @param id set id
   * @return <code>true</code> if update successful.
   */
  boolean deleteSet(UUID id);
  
  /**
   * Updates set.
   * @param setData
   * @return <code>true</code> if update successful.
   */
  boolean updateSet(SetData setData);
}
