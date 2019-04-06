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
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;

/**
 * GetRecord request.
 */
public final class GetRecordRequest extends Request {
  /** record identifier*/
  private URI identifier;
  /** metadata prefix */
  private String metadataPrefix;

  /**
   * Creates instance of the request.
   * @param identifier identifier
   * @param metadataPrefix metadata prefix
   */
  public GetRecordRequest(URI identifier, String metadataPrefix) {
    super(Verb.GetRecord);
    this.identifier = identifier;
    this.metadataPrefix = metadataPrefix;
    Validate.notNull(identifier, "Missing identifier");
    Validate.notEmpty(metadataPrefix, "Missing metadata prefix");
  }

  /**
   * Creates instance of the request.
   */
  GetRecordRequest() {
    super(Verb.GetRecord);
  }

  /**
   * Gets identifier.
   * @return identifier
   */
  public URI getIdentifier() {
    return identifier;
  }

  /**
   * Gets metadata prefix.
   * @return metadata prefix
   */
  public String getMetadataPrefix() {
    return metadataPrefix;
  }
  
  /**
   * Creates request from parameters.
   * @param params parameters
   * @return request
   * @throws BadArgumentException if invalid parameters
   */
  public static GetRecordRequest create(Map<String,String[]> params) throws BadArgumentException {
    params = trimParams(params);
    GetRecordRequest request = new GetRecordRequest();
    ParamProcessor
            .with("identifier", v -> {
              if (v==null) {
                throw new BadArgumentException(String.format("Missing identifier"));
              }
              if (v.length>1) {
                 throw new BadArgumentException(String.format("Illegal number of identifiers: %s", Arrays.stream(v).collect(Collectors.joining(", "))));
              }
              try {
                request.identifier = URI.create(v[0]);
              } catch (IllegalArgumentException|NullPointerException ex) {
                throw new BadArgumentException(String.format("Invalid identifier format: %s", v[0]));
              }
            })
            .with("metadataPrefix", v -> {
              if (v==null) {
                throw new BadArgumentException(String.format("Missing metadataPrefix"));
              }
              if (v.length>1) {
                 throw new BadArgumentException(String.format("Illegal number of metadata formats: %s", Arrays.stream(v).collect(Collectors.joining(", "))));
              }
              request.metadataPrefix = v[0];
            })
            .build().execute(params);
    return request;
  }
  @Override
  public Map<String, String[]> getParameters() {
    Map<String,String[]> parameters = new HashMap<>();
    parameters.put("verb", new String[]{ verb.name() });
    if (metadataPrefix!=null) {
      parameters.put("metadataPrefix", new String[]{ metadataPrefix });
    }
    if (identifier!=null) {
      parameters.put("identifier", new String[]{ identifier.toASCIIString() });
    }
    return parameters;
  }
}
