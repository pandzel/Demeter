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

import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.model.request.ListIdentifiersRequest;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * ListIdentifiers response.
 */
public final class ListIdentifiersResponse extends Response<ListIdentifiersRequest> {
  
  /** headers */
  public final Header[] headers;
  /** resumption token */
  public final ResumptionToken resumptionToken;

  /**
   * Creates instance of the response.
   * @param headers headers
   * @param resumptionToken resumption token
   * @param responseDate response date
   * @param errors errors
   * @param parameters request parameters
   */
  public ListIdentifiersResponse(Header[] headers, ResumptionToken resumptionToken, OffsetDateTime responseDate, ErrorInfo[] errors, Map<String, String[]> parameters) {
    super(responseDate, errors, parameters);
    this.headers = headers;
    this.resumptionToken = resumptionToken;
  }
  
}
