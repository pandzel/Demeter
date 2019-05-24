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

import com.datastax.oss.driver.api.core.cql.Row;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import org.w3c.dom.Document;

/**
 * Metadata processor.
 */
public interface MetaProcessor {

  /**
   * Interrogates document if can be processed by the processor.
   * @param row row of data
   * @return <code>true</code> if can be processed by the processor
   */
  boolean interrogate(Row row);
  
  /**
   * Adopts document.
   * @param row row of data
   * @return adopted document
   */
  Document adopt(Row row);
  
  /**
   * Gets supported metadata format.
   * @return supported metadata format
   */
  MetadataFormat format();
}
