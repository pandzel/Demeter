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
import static com.panforge.demeter.core.utils.QueryUtils.trimParams;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;

/**
 * ListSets request.
 */
public final class ListSetsRequest extends RequestWithToken {

  /**
   * Creates instance of the request.
   * @param resumptionToken resumption token
   */
  public ListSetsRequest(String resumptionToken) {
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
  public static ListSetsRequest create(Map<String, List<String>> params) throws BadArgumentException {
    params = trimParams(params);
    ListSetsRequest request = new ListSetsRequest();
    ParamProcessor
            .with("resumptionToken", v -> {
              if (v!=null) {
                if (v.size() > 1) {
                  throw new BadArgumentException(String.format("Illegal number of resumption tokens: %s", v.stream().collect(Collectors.joining(", "))));
                }
                request.resumptionToken = v.get(0);
              }
            })
            .build().execute(params);
    return request;
  }

  @Override
  public Map<String, List<String>> getParameters() {
    Map<String,List<String>> parameters = new HashMap<>();
    parameters.put("verb", Arrays.asList(new String[]{ verb.name() }));
    if (resumptionToken!=null) {
      parameters.put("resumptionToken", Arrays.asList(new String[]{ resumptionToken }));
    }
    return parameters;
  }
}
