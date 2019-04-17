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
import com.panforge.demeter.core.model.request.Request;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Base of all response types..
 * @param <REQ> the request type
 */
public abstract class Response<REQ extends Request> {
  
  /** request parameters */
  public final Map<String, String[]> parameters;
  /** response date */
  public final OffsetDateTime responseDate;
  /** errors */
  public final ErrorInfo[] errors;

  /**
   * Creates instance of the response.
   * @param parameters request parameters
   * @param responseDate response date
   * @param errors errors
   */
  public Response(Map<String, String[]> parameters, OffsetDateTime responseDate, ErrorInfo[] errors) {
    this.parameters = Collections.unmodifiableMap(parameters);
    this.responseDate = responseDate;
    this.errors = errors;
  }

  /**
   * Creates instance of the response.
   * @param parameters request parameters
   * @param responseDate response date
   */
  public Response(Map<String, String[]> parameters, OffsetDateTime responseDate) {
    this(parameters, responseDate, null);
  }
  
  /**
   * Gets first parameter value by name.
   * @param name parameter name.
   * @return parameter value or <code>null</code>
   */
  public String getParameter(String name) {
    String [] values = parameters.get(name);
    return values!=null? Arrays.stream(values).filter(v -> !StringUtils.isBlank(v)).findFirst().orElse(null): null;
  }
  
}
