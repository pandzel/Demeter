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
package com.panforge.demeter.core.model.request;

import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.utils.QueryUtils;
import java.util.Map;

/**
 * Base of all requests..
 */
public abstract class Request {
  /** the verb */
  public final Verb verb;

  /**
   * Creates instance of the request.
   * @param verb the verb identifying type of the request
   */
  Request(Verb verb) {
    this.verb = verb;
  }
  
  /**
   * Gets parameters.
   * @return map of parameters
   */
  public abstract Map<String,String[]> getParameters();
  
  @Override
  public String toString() {
    return QueryUtils.paramsToQuery(getParameters());
  }
}
