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
package com.panforge.demeter.demeter.http.client;

import java.time.OffsetDateTime;

/**
 * Crawl context.
 */
public class CrawlContext {
  /** 'from' time */
  public final OffsetDateTime from;
  /** 'until' time */
  public final OffsetDateTime until;
  /** set spec */
  public final String set;

  /**
   * Creates instance of the crawl context.
   * @param from 'from' time
   * @param until 'until' time
   * @param set set spec
   */
  public CrawlContext(OffsetDateTime from, OffsetDateTime until, String set) {
    this.from = from;
    this.until = until;
    this.set = set;
  }
}
