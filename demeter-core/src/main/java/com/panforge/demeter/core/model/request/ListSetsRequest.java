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

import com.panforge.demeter.core.utils.ParamProcessor;
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.api.exception.BadArgumentException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.Validate;

/**
 * ListSets request.
 */
public final class ListSetsRequest extends RequestWithToken {

  /**
   * Creates instance of the request.
   * @param resumptionToken resumption token
   * @return instance of the request
   */
  public static ListSetsRequest resume(String resumptionToken) {
    return new ListSetsRequest(resumptionToken);
  }

  /**
   * Creates instance of the request.
   * @param resumptionToken resumption token
   */
  ListSetsRequest(String resumptionToken) {
    super(Verb.ListSets);
    this.resumptionToken = resumptionToken;
    Validate.notEmpty(resumptionToken, "Missing resumption token");
  }
  
  /**
   * Creates instance of the request.
   */
  public ListSetsRequest() {
    super(Verb.ListSets);
  }

  /**
   * Creates request from parameters.
   * @param params parameters
   * @return request
   * @throws BadArgumentException if creation fails
   */
  public static ListSetsRequest create(Map<String, String[]> params) throws BadArgumentException {
    ListSetsRequest request = new ListSetsRequest();
    ParamProcessor
            .with("resumptionToken", v -> {
              if (v!=null) {
                request.resumptionToken = v;
              }
            })
            .build().execute(params);
    return request;
  }

  @Override
  public Map<String, String[]> getParameters() {
    Map<String,String[]> parameters = new HashMap<>();
    parameters.put("verb", new String[]{ verb.name() });
    if (resumptionToken!=null) {
      parameters.put("resumptionToken", new String[]{ resumptionToken });
    }
    return parameters;
  }
}
