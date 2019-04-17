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
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * ListMetadataFormats request.
 */
public final class ListMetadataFormatsRequest extends Request {

  /** record identifier (optional) */
  private URI identifier;

  /**
   * Creates instance of the request.
   * @param identifier identifier
   */
  public ListMetadataFormatsRequest(URI identifier) {
    super(Verb.ListMetadataFormats);
    this.identifier = identifier;
  }

  /**
   * Creates instance of the request.
   */
  ListMetadataFormatsRequest() {
    super(Verb.ListMetadataFormats);
  }

  /**
   * Gets identifier.
   * @return identifier
   */
  public URI getIdentifier() {
    return identifier;
  }

  /**
   * Creates request from parameters.
   * @param params parameters
   * @return request
   * @throws BadArgumentException if creation fails
   */
  public static ListMetadataFormatsRequest create(Map<String, String[]> params) throws BadArgumentException {
    ListMetadataFormatsRequest request = new ListMetadataFormatsRequest();
    ParamProcessor
            .with("identifier", v -> {
              if (v!=null) {
                try {
                  request.identifier = URI.create(v);
                } catch (IllegalArgumentException|NullPointerException ex) {
                  throw new BadArgumentException(String.format("Invalid identifier format: %s", v));
                }
              }
            })
            .build().execute(params);
    return request;
  }
  
  @Override
  public Map<String, String[]> getParameters() {
    Map<String,String[]> parameters = new HashMap<>();
    parameters.put("verb", new String[]{ verb.name() });
    if (identifier!=null) {
      parameters.put("identifier", new String[]{ identifier.toASCIIString() });
    }
    return parameters;
  }
}
