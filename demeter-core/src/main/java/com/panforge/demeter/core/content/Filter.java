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
package com.panforge.demeter.core.content;

import java.time.OffsetDateTime;

/**
 * Filter.
 */
public final class Filter {
  public final OffsetDateTime from;
  public final OffsetDateTime until;
  public final String metadataPrefix;
  public final String set;

  /**
   * Creates instance of the filter.
   * @param from from timestamp
   * @param until until timestamp
   * @param metadataPrefix metadata prefix
   * @param set set
   */
  public Filter(OffsetDateTime from, OffsetDateTime until, String metadataPrefix, String set) {
    this.from = from;
    this.until = until;
    this.metadataPrefix = metadataPrefix;
    this.set = set;
  }
  
}
