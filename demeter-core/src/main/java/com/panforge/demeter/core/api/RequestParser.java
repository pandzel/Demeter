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
package com.panforge.demeter.core.api;

import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.api.exception.BadArgumentException;
import com.panforge.demeter.core.api.exception.BadVerbException;
import com.panforge.demeter.core.model.request.GetRecordRequest;
import com.panforge.demeter.core.model.request.IdentifyRequest;
import com.panforge.demeter.core.model.request.ListIdentifiersRequest;
import com.panforge.demeter.core.model.request.ListMetadataFormatsRequest;
import com.panforge.demeter.core.model.request.ListRecordsRequest;
import com.panforge.demeter.core.model.request.ListSetsRequest;
import com.panforge.demeter.core.model.request.Request;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.panforge.demeter.core.utils.QueryUtils.queryToParams;
import static com.panforge.demeter.core.utils.QueryUtils.rejectKeys;
import org.apache.commons.lang3.Validate;

/**
 * Request parser.
 * <p>
 * It is used to parse query strings into the {@link Request}.
 */
public class RequestParser {
  private final Context CTX;

  /**
   * Creates instance of the parser.
   * @param CTX application context
   */
  public RequestParser(Context CTX) {
    Validate.notNull(CTX, "Missing context");
    this.CTX = CTX;
  }
  
  
  /**
   * Parses query.
   * @param query query string
   * @return an instance of the request
   * @throws BadVerbException if parsing fails
   * @throws BadArgumentException if parsing fails
   */
  public Request parse(String query) throws BadVerbException, BadArgumentException {
    Validate.notNull(query, "Missing query");
    return parse(queryToParams(query));
  }
  
  /**
   * Parses query.
   * @param params query parameters
   * @return an instance of the request
   * @throws BadVerbException if parsing fails
   * @throws BadArgumentException if parsing fails
   */
  public Request parse(Map<String,List<String>> params) throws BadVerbException, BadArgumentException {
    Validate.notNull(params, "Missing parameters");
    switch (getVerb(params)) {
      case GetRecord:
        return GetRecordRequest.create(reduceParams(params));
      case Identify:
        return IdentifyRequest.create(reduceParams(params));
      case ListIdentifiers:
        return ListIdentifiersRequest.create(reduceParams(params));
      case ListMetadataFormats:
        return ListMetadataFormatsRequest.create(reduceParams(params));
      case ListRecords:
        return ListRecordsRequest.create(reduceParams(params));
      case ListSets:
        return ListSetsRequest.create(reduceParams(params));
    }
    throw new BadVerbException(String.format("Missing verb."));
  }
  
  private Map<String,List<String>> reduceParams(Map<String,List<String>> params) {
    return rejectKeys(params, "verb");
  }
  
  private Verb getVerb(Map<String,List<String>> params) throws BadVerbException {
    List<String> values = params.get("verb");
    if (values==null || values.isEmpty()) {
      throw new BadVerbException(String.format("Missing verb."));
    }
    if (values.size()>1) {
      throw new BadVerbException(String.format("To many verbs: %s.", values.stream().collect(Collectors.joining(", "))));
    }
    Verb verb = Verb.parse(values.get(0));
    if (verb==null) {
      throw new BadVerbException(String.format("Unrecognized verb: %s.", values.get(0)));
    }
    return verb;
  }
}
