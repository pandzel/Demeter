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
package com.panforge.demeter.service;

import com.panforge.demeter.core.api.exception.BadResumptionTokenException;
import com.panforge.demeter.core.content.PageCursor;
import com.panforge.demeter.core.model.ResumptionToken;

/**
 * Token manager.
 * @param <PC> type of page cursor
 */
public interface TokenManager<PC extends PageCursor> {
  /**
   * Stores page cursor.
   * @param pageCursor page cursor
   * @param total total records
   * @return resumption token
   */
  ResumptionToken put(PC pageCursor, long total);
  
  /**
   * Retrieves page cursor.
   * @param tokenId token id
   * @return page cursor
   * @throws BadResumptionTokenException if invalid token
   */
  PC pull(String tokenId) throws BadResumptionTokenException;
}
