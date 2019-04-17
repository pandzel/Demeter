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
import static com.panforge.demeter.core.utils.DateTimeUtils.parseRequestTimestamp;
import java.util.HashMap;
import org.apache.commons.lang3.Validate;

/**
 * ListRecords request.
 */
public class ListRecordsRequest extends RequestWithToken {

  /** starting date (optional) */
  private OffsetDateTime from;
  /** ending date (optional) */
  private OffsetDateTime until;
  /** metadata prefix (mandatory) */
  private String metadataPrefix;
  /** set (optional) */
  private String set;

  /**
   * Creates instance of the request.
   * @param resumptionToken resumption token
   * @return instance of the request
   */
  public static ListRecordsRequest resume(String resumptionToken) {
    return new ListRecordsRequest(resumptionToken);
  }
  
  /**
   * Creates instance of the request.
   * @param resumptionToken resumption token
   */
  ListRecordsRequest(String resumptionToken) {
    super(Verb.ListRecords);
    this.resumptionToken = resumptionToken;
    Validate.notEmpty(resumptionToken, "Missing resumption token");
  }

  /**
   * Creates instance of the request.
   * @param metadataPrefix the metadata prefix (mandatory)
   * @param from the 'from' date (optional)
   * @param until the 'until' date (optional)
   * @param set the set (optional)
   */
  public ListRecordsRequest(String metadataPrefix, OffsetDateTime from, OffsetDateTime until, String set) {
    super(Verb.ListRecords);
    this.from = from;
    this.until = until;
    this.metadataPrefix = metadataPrefix;
    this.set = set;
    Validate.notNull(metadataPrefix, "Missing metadata prefix");
  }

  /**
   * Creates instance of the request.
   */
  ListRecordsRequest() {
    super(Verb.ListRecords);
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
  public static ListRecordsRequest create(Map<String, String[]> params) throws BadArgumentException {
    ListRecordsRequest request = new ListRecordsRequest();
    if (params.containsKey("resumptionToken")) {
      ParamProcessor
              .with("resumptionToken", v -> {
                if (v != null) {
                  request.resumptionToken = v;
                }
              })
              .build().execute(params);
    } else {
      ParamProcessor
              .with("from", v -> {
                if (v != null) {
                  request.from = parseRequestTimestamp(v);
                }
              })
              .with("until", v -> {
                if (v != null) {
                  request.until = parseRequestTimestamp(v);
                }
              })
              .with("metadataPrefix", v -> {
                if (v == null) {
                  throw new BadArgumentException(String.format("Missing metadataPrefix"));
                }
                request.metadataPrefix = v;
              })
              .with("set", v -> {
                if (v != null) {
                  request.set = v;
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
      parameters.put("until", new String[]{ until.format(DateTimeFormatter.ISO_DATE_TIME) });
    }
    if (metadataPrefix!=null) {
      parameters.put("metadataPrefix", new String[]{ metadataPrefix });
    }
    if (set!=null) {
      parameters.put("set", new String[]{ set });
    }
    return parameters;
  }
}
