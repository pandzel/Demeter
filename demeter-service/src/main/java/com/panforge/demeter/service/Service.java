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
package com.panforge.demeter.service;

import com.panforge.demeter.core.api.Config;
import com.panforge.demeter.core.content.ContentProvider;
import com.panforge.demeter.core.api.Context;
import com.panforge.demeter.core.api.RequestParser;
import com.panforge.demeter.core.api.ResponseFactory;
import com.panforge.demeter.core.model.ErrorCode;
import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.api.exception.ProtocolException;
import com.panforge.demeter.core.api.exception.BadResumptionTokenException;
import com.panforge.demeter.core.api.exception.CannotDisseminateFormatException;
import com.panforge.demeter.core.api.exception.IdDoesNotExistException;
import com.panforge.demeter.core.api.exception.NoMetadataFormatsException;
import com.panforge.demeter.core.api.exception.NoRecordsMatchException;
import com.panforge.demeter.core.api.exception.NoSetHierarchyException;
import com.panforge.demeter.core.content.Page;
import com.panforge.demeter.core.content.PageCursor;
import com.panforge.demeter.core.content.StreamingIterable;
import com.panforge.demeter.core.model.request.GetRecordRequest;
import com.panforge.demeter.core.model.request.IdentifyRequest;
import com.panforge.demeter.core.model.request.ListMetadataFormatsRequest;
import com.panforge.demeter.core.model.request.ListSetsRequest;
import com.panforge.demeter.core.model.request.ListIdentifiersRequest;
import com.panforge.demeter.core.model.request.ListRecordsRequest;
import com.panforge.demeter.core.model.request.Request;
import com.panforge.demeter.core.model.response.GetRecordResponse;
import com.panforge.demeter.core.model.response.IdentifyResponse;
import com.panforge.demeter.core.model.response.ListSetsResponse;
import com.panforge.demeter.core.model.response.ListIdentifiersResponse;
import com.panforge.demeter.core.model.response.ListMetadataFormatsResponse;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.response.elements.Set;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.utils.QueryUtils;
import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.model.response.ListRecordsResponse;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service.
 * @param <PC> page cursor type
 */
public class Service<PC extends PageCursor> {
  protected static final Logger LOG = LoggerFactory.getLogger(Service.class);
  public static final int DEFAULT_BATCH_SIZE = 10;
  private final ContentProvider<PC> repo;
  private final TokenManager<PC> tokenManager;
  
  private final Context ctx;
  private final RequestParser parser;
  private final ResponseFactory factory;
  private final int pageSize;

  /**
   * Creates instance of the service.
   * @param config configuration
   * @param repo repository
   * @param tokenManager token manager
   * @param pageSize batch size
   */
  public Service(Config config, ContentProvider<PC> repo, TokenManager<PC> tokenManager, int pageSize) {
    this.repo = repo;
    this.tokenManager = tokenManager;
    this.pageSize = pageSize;
    
    Validate.notNull(config, "Missing configuration");
    Validate.notNull(repo, "Missing content provider");
    Validate.notNull(tokenManager, "Missing token manager");
    
    this.ctx = new Context(config);
    this.parser = new RequestParser();
    this.factory = new ResponseFactory(ctx);
  }

  /**
   * Creates instance of the service.
   * @param config configuration
   * @param repo repository
   * @param tokenManager token manager
   */
  public Service(Config config, ContentProvider<PC> repo, TokenManager<PC> tokenManager) {
    this(config, repo, tokenManager, DEFAULT_BATCH_SIZE);
  }

  /**
   * Executes OAI-PMH request.
   * @param query HTTP query
   * @return response
   */
  public String execute(String query) {
    Map<String, String[]> parameters = QueryUtils.queryToParams(query);
    return execute(parameters);
  }
  
  /**   
   * Executes OAI-PMH request.
   * @param parameters HTTP parameters
   * @return response
   */
  public String execute(Map<String, String[]> parameters) {
    try {
      Request request = parser.parse(parameters);
      switch (request.verb) {
        case Identify:
          return createIdentifyResponse((IdentifyRequest) request);

        case ListMetadataFormats:
          return createListMetadataFormatsResponse((ListMetadataFormatsRequest) request);
          
        case GetRecord:
          return createGetRecordResponse((GetRecordRequest) request);

        case ListSets:
          return createListSetsResponse((ListSetsRequest)request);
          
        case ListIdentifiers:
          return createListIdentifiersResponse((ListIdentifiersRequest)request);
          
        case ListRecords:
          return createListRecordsResponse((ListRecordsRequest)request);
          
        default:
          return factory.createErrorResponse(OffsetDateTime.now(), parameters, new ErrorInfo[]{new ErrorInfo(ErrorCode.badArgument, "Error parsing request.")});
      }
    } catch (ProtocolException pex) {
      return factory.createErrorResponse(OffsetDateTime.now(), parameters, pex.infos);
    }
  }
  
  private String createIdentifyResponse(IdentifyRequest request) {
    IdentifyResponse idetifyResponse = IdentifyResponse.createFromConfig(request.getParameters(), OffsetDateTime.now(), ctx.config, null);
    return factory.createIdentifyResponse(idetifyResponse);
  }
  
  private String createListMetadataFormatsResponse(ListMetadataFormatsRequest request) throws IdDoesNotExistException, NoMetadataFormatsException {
    StreamingIterable<MetadataFormat> metadataFormats = repo.listMetadataFormats(request.getIdentifier());
    MetadataFormat[] metadataFormatsArray = StreamSupport.stream(metadataFormats.spliterator(), false).toArray(MetadataFormat[]::new);
    ListMetadataFormatsResponse metadataFormatsResponse = new ListMetadataFormatsResponse(request.getParameters(), OffsetDateTime.now(), metadataFormatsArray);
    return factory.createListMetadataFormatsResponse(metadataFormatsResponse);
  }
  
  private String createGetRecordResponse(GetRecordRequest request) throws IdDoesNotExistException, CannotDisseminateFormatException {
    Record record = repo.readRecord(request.getIdentifier(), request.getMetadataPrefix());
    GetRecordResponse getRecordResponse = new GetRecordResponse(request.getParameters(), OffsetDateTime.now(), record);
    return factory.createGetRecordResponse(getRecordResponse);
  }
  
  private String createListSetsResponse(ListSetsRequest request) throws BadResumptionTokenException, NoSetHierarchyException {
    PC pageCursor = request.getResumptionToken()!=null? tokenManager.pull(request.getResumptionToken()): null;
    try (Page<Set,PC> listSets = repo.listSets(pageCursor, pageSize);) {
      PC nextPageCursor = listSets.nextPageCursor();
      ResumptionToken resumptionToken = nextPageCursor!=null? tokenManager.put(nextPageCursor, listSets.total()): null;
      Set[] setArray = StreamSupport.stream(listSets.spliterator(), false).toArray(Set[]::new);
      ListSetsResponse response = new ListSetsResponse(request.getParameters(), OffsetDateTime.now(), setArray, resumptionToken);
      return factory.createListSetsResponse(response); 
    }
  }
  
  private String createListIdentifiersResponse(ListIdentifiersRequest request) throws BadResumptionTokenException, CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
    PC pageCursor = request.getResumptionToken()!=null? tokenManager.pull(request.getResumptionToken()): null;
    try (Page<Header, PC> headers = repo.listHeaders(request.getFilter(), pageCursor, pageSize);) {
      PC nextPageCursor = headers.nextPageCursor();
      ResumptionToken resumptionToken = nextPageCursor!=null? tokenManager.put(nextPageCursor, headers.total()): null;
      Header[] headerArray = StreamSupport.stream(headers.spliterator(), false).toArray(Header[]::new);
      ListIdentifiersResponse response = new ListIdentifiersResponse(request.getParameters(), OffsetDateTime.now(), headerArray, resumptionToken);
      return factory.createListIdentifiersResponse(response); 
    }
  }
  
  private String createListRecordsResponse(ListRecordsRequest request) throws BadResumptionTokenException, CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
    PC pageCursor = request.getResumptionToken()!=null? tokenManager.pull(request.getResumptionToken()): null;
    try (Page<Header, PC> headers = repo.listHeaders(request.getFilter(), pageCursor, pageSize);) {
      PC nextPageCursor = headers.nextPageCursor();
      ResumptionToken resumptionToken = nextPageCursor!=null? tokenManager.put(nextPageCursor, headers.total()): null;
      Record[] recordArray = StreamSupport.stream(headers.spliterator(), false).map(h -> {
        if (!h.deleted) {
          try {
            return repo.readRecord(h.identifier, request.getMetadataPrefix()); 
          } catch (ProtocolException ex) {
            LOG.debug(String.format("Error reading record: %s", h.identifier), ex);
            return null;
          }
        } else {
          Record rec = new Record(h, null, null);
          return rec;
        }
      }).filter(r->r!=null).toArray(Record[]::new);
      ListRecordsResponse response = new ListRecordsResponse(request.getParameters(), OffsetDateTime.now(), recordArray, resumptionToken);
      return factory.createListRecordsResponse(response); 
    }
  }
}
