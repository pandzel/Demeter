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
import com.panforge.demeter.core.content.Filter;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;
import static com.panforge.demeter.core.utils.DateTimeUtils.parseRequestTimestamp;
import static com.panforge.demeter.core.utils.QueryUtils.trimParams;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.lang3.Validate;

/**
 * ListIdentifiers request.
 */
public final class ListIdentifiersRequest extends RequestWithToken {

  private OffsetDateTime from;
  private OffsetDateTime until;
  private String metadataPrefix;
  private String set;
  
  /**
   * Creates instance of the request.
   * @param resumptionToken resumption token
   */
  public ListIdentifiersRequest(String resumptionToken) {
    super(Verb.ListIdentifiers);
    this.resumptionToken = resumptionToken;
    Validate.notEmpty(resumptionToken, "Missing resumption token");
  }

  /**
   * Creates instance of the request.
   * @param metadataPrefix the metadata prefix
   * @param from the 'from' date
   * @param until the 'until' date
   * @param set the set
   */
  public ListIdentifiersRequest(String metadataPrefix, OffsetDateTime from, OffsetDateTime until, String set) {
    super(Verb.ListIdentifiers);
    this.from = from;
    this.until = until;
    this.metadataPrefix = metadataPrefix;
    this.set = set;
    Validate.notNull(metadataPrefix, "Missing metadata prefix");
  }

  /**
   * Creates instance of the request.
   */
  ListIdentifiersRequest() {
    super(Verb.ListIdentifiers);
  }

  /**
   * Gets filter.
   * @return filter
   */
  public Filter getFilter() {
    return new Filter(from, until, metadataPrefix, set);
  }
  
  /**
   * Gets 'from' date.
   * @return 'from' date
   */
  public OffsetDateTime getFrom() {
    return from;
  }

  /**
   * Gets 'until' date.
   * @return 'until' date
   */
  public OffsetDateTime getUntil() {
    return until;
  }

  /**
   * Gets metadata prefix.
   * @return metadata prefix
   */
  public String getMetadataPrefix() {
    return metadataPrefix;
  }

  /**
   * Gets set.
   * @return set
   */
  public String getSet() {
    return set;
  }

  /**
   * Creates request from parameters.
   * @param params parameters
   * @return request
   * @throws BadArgumentException if creation fails
   */
  public static ListIdentifiersRequest create(Map<String, String[]> params) throws BadArgumentException {
    params = trimParams(params);
    ListIdentifiersRequest request = new ListIdentifiersRequest();
    if (params.containsKey("resumptionToken")) {
      ParamProcessor
              .with("resumptionToken", v -> {
                if (v != null) {
                  if (v.length > 1) {
                    throw new BadArgumentException(String.format("Illegal number of resumption tokens: %s", Arrays.stream(v).collect(Collectors.joining(", "))));
                  }
                  request.resumptionToken = v[0];
                }
              })
              .build().execute(params);
    } else {
      ParamProcessor
              .with("from", v -> {
                if (v != null) {
                  if (v.length > 1) {
                    throw new BadArgumentException(String.format("Illegal number of identifiers: %s", Arrays.stream(v).collect(Collectors.joining(", "))));
                  }
                  request.from = parseRequestTimestamp(v[0]);
                }
              })
              .with("until", v -> {
                if (v != null) {
                  if (v.length > 1) {
                    throw new BadArgumentException(String.format("Illegal number of identifiers: %s", Arrays.stream(v).collect(Collectors.joining(", "))));
                  }
                  request.until = parseRequestTimestamp(v[0]);
                }
              })
              .with("metadataPrefix", v -> {
                if (v == null) {
                  throw new BadArgumentException(String.format("Missing metadataPrefix"));
                }
                if (v.length > 1) {
                  throw new BadArgumentException(String.format("Illegal number of identifiers: %s", Arrays.stream(v).collect(Collectors.joining(", "))));
                }
                request.metadataPrefix = v[0];
              })
              .with("set", v -> {
                if (v != null) {
                  if (v.length > 1) {
                    throw new BadArgumentException(String.format("Illegal number of identifiers: %s", Arrays.stream(v).collect(Collectors.joining(", "))));
                  }
                  request.set = v[0];
                }
              })
              .build().execute(params);
    }
    return request;
  }

  @Override
  public Map<String, String[]> getParameters() {
    Map<String,String[]> parameters = new HashMap<>();
    parameters.put("verb", new String[]{ verb.name() });
    if (resumptionToken!=null) {
      parameters.put("resumptionToken", new String[]{ resumptionToken });
    }
    if (from!=null) {
      parameters.put("from", new String[]{ from.format(DateTimeFormatter.ISO_DATE_TIME) });
    }
    if (until!=null) {
      parameters.put("from", new String[]{ until.format(DateTimeFormatter.ISO_DATE_TIME) });
    }
    if (metadataPrefix!=null) {
      parameters.put("from", new String[]{ metadataPrefix });
    }
    if (set!=null) {
      parameters.put("from", new String[]{ set });
    }
    return parameters;
  }
}
