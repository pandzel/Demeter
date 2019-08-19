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

import com.panforge.demeter.core.api.exception.ProtocolException;
import com.panforge.demeter.core.content.PageCursor;
import com.panforge.demeter.core.model.ResumptionToken;
import java.util.function.Supplier;

/**
 * Token manager.
 * @param <PC> type of page cursor
 */
public interface TokenManager<PC extends PageCursor> {

  /**
   * Invoke action for a token.
   * @param token resumption token
   * @return response
   * @throws ProtocolException if invoking action fails
   */
  String invoke(String token) throws ProtocolException;
  
  /**
   * Registers a supplier for a new token.
   * @param supplier action to be invoked with the next call with a token
   * @param completeListSize complete list size
   * @param cursor cursor position
   * @return resumption token.
   */
  ResumptionToken register(Supplier<String> supplier, long completeListSize, long cursor);
}
