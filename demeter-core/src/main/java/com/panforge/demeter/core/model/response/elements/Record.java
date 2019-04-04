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
package com.panforge.demeter.core.model.response.elements;

import org.apache.commons.lang3.Validate;
import org.w3c.dom.Document;

/**
 * Record.
 */
public final class Record {
  
  /** header */
  public final Header header;
  /** metadata */
  public final Document metadata;
  /** about information */
  public final Document about;

  /**
   * Creates instance of the record.
   * @param header header
   * @param metadata metadata
   * @param about about information
   */
  public Record(Header header, Document metadata, Document about) {
    Validate.notNull(header, "Missing header");
    this.header = header;
    this.metadata = metadata;
    this.about = about;
  }
  
}
