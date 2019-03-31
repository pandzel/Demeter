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
package com.panforge.demeter.core.model.response;

import com.panforge.demeter.core.model.request.Request;
import java.time.OffsetDateTime;

/**
 * Base of all response types..
 * @param <REQ> the request type
 */
public abstract class Response<REQ extends Request> {
  
  /** response date */
  public final OffsetDateTime responseDate;
  /** request */
  public final REQ request;

  /**
   * Creates instance of the response.
   * @param responseDate response date
   * @param request instance of the request
   */
  public Response(OffsetDateTime responseDate, REQ request) {
    this.responseDate = responseDate;
    this.request = request;
  }
  
  
}
