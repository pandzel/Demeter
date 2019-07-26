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
import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.api.exception.BadResumptionTokenException;
import com.panforge.demeter.core.api.exception.CannotDisseminateFormatException;
import com.panforge.demeter.core.api.exception.IdDoesNotExistException;
import com.panforge.demeter.core.api.exception.NoMetadataFormatsException;
import com.panforge.demeter.core.api.exception.NoRecordsMatchException;
import com.panforge.demeter.core.api.exception.NoSetHierarchyException;
import com.panforge.demeter.core.content.Page;
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
import com.panforge.demeter.core.model.response.ListMetadataFormatsResponse;
import com.panforge.demeter.core.model.response.ListSetsResponse;
import com.panforge.demeter.core.model.response.ListIdentifiersResponse;
import com.panforge.demeter.core.model.response.ListRecordsResponse;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.response.elements.Set;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.utils.QueryUtils;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.Validate;

/**
 * Service.
 */
public class Service {
  public static final int DEFAULT_BATCH_SIZE = 10;
  private final ContentProvider repo;
  private final TokenManager tokenManager;
  
  private final Context ctx;
  private final RequestParser parser;
  private final ResponseFactory factory;
  private final int batchSize;

  /**
   * Creates instance of the service.
   * @param config configuration
   * @param repo repository
   * @param tokenManager token manager
   * @param batchSize batch size
   */
  public Service(Config config, ContentProvider repo, TokenManager tokenManager, int batchSize) {
    this.repo = repo;
    this.tokenManager = tokenManager;
    this.batchSize = batchSize;
    
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
  public Service(Config config, ContentProvider repo, TokenManager tokenManager) {
    this(config, repo, tokenManager, DEFAULT_BATCH_SIZE);
  }

  /**
   * Creates instance of the service.
   * @param config configuration
   * @param repo repository
   */
  public Service(Config config, ContentProvider repo) {
    this(config, repo, new SimpleTokenManager());
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
          ListSetsRequest listSetsRequest = (ListSetsRequest)request;
          if (listSetsRequest.getResumptionToken()==null) {
            return createListSetsResponse(listSetsRequest);
          } else {
            return tokenManager.invoke(listSetsRequest.getResumptionToken());
          }
          
        case ListIdentifiers:
          ListIdentifiersRequest listIdentifiersRequest = (ListIdentifiersRequest)request;
          if (listIdentifiersRequest.getResumptionToken()==null) {
            return createListIdentifiersResponse(listIdentifiersRequest);
          } else {
            return tokenManager.invoke(listIdentifiersRequest.getResumptionToken());
          }
          
        case ListRecords:
          ListRecordsRequest listRecordsRequest = (ListRecordsRequest)request;
          if (listRecordsRequest.getResumptionToken()==null) {
            return createListRecordsResponse(listRecordsRequest);
          } else {
            return tokenManager.invoke(listRecordsRequest.getResumptionToken());
          }
          
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
    try (Page<Set> listSets = repo.listSets();) {
      Spliterator<Set> spliterator = listSets.spliterator();
      return createListSetsSupplier(request, new ArrayList<>(), spliterator, listSets.total(), 0).get();
    }
  }
  
  private Supplier<String> createListSetsSupplier(ListSetsRequest request, List<Set> bufferedSets, Spliterator<Set> spliterator, long completeListSize, long cursor) {
    Set[] setArray = Stream.concat(bufferedSets.stream(), StreamSupport.stream(spliterator, false)).limit(batchSize).toArray(Set[]::new);
    return () -> { 
      ResumptionToken resumptionToken = null;
      ArrayList<Set> prefetchedSets = new ArrayList<>();
      if (spliterator.tryAdvance(set->{ prefetchedSets.add(set); })) {
        Supplier<String> supplier = createListSetsSupplier(request, prefetchedSets, spliterator, completeListSize, cursor+setArray.length);
        resumptionToken = tokenManager.register(supplier, completeListSize, cursor+setArray.length);
      }
      ListSetsResponse response = new ListSetsResponse(request.getParameters(), OffsetDateTime.now(), setArray, resumptionToken);
      return factory.createListSetsResponse(response); 
    };
  }
  
  private String createListIdentifiersResponse(ListIdentifiersRequest request) throws BadResumptionTokenException, CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
    try (Page<Header> headers = repo.listHeaders(request.getFilter());) {
      Spliterator<Header> spliterator = headers.spliterator();
      return createListIdentifiersSupplier(request, new ArrayList<>(), spliterator, headers.total(), 0).get();
    }
  }
  
  private Supplier<String> createListIdentifiersSupplier(ListIdentifiersRequest request, List<Header> bufferedHeaders, Spliterator<Header> spliterator, long completeListSize, long cursor) {
    Header[] headerArray = Stream.concat(bufferedHeaders.stream(), StreamSupport.stream(spliterator, false)).limit(batchSize).toArray(Header[]::new);
    return () -> { 
      ResumptionToken resumptionToken = null;
      ArrayList<Header> prefetchedHeaders = new ArrayList<>();
      if (spliterator.tryAdvance(header->{ prefetchedHeaders.add(header); })) {
        Supplier<String> supplier = createListIdentifiersSupplier(request, prefetchedHeaders, spliterator, completeListSize, cursor+headerArray.length);
        resumptionToken = tokenManager.register(supplier, completeListSize, cursor+headerArray.length);
      }
      ListIdentifiersResponse response = new ListIdentifiersResponse(request.getParameters(), OffsetDateTime.now(), headerArray, resumptionToken);
      return factory.createListIdentifiersResponse(response); 
    };
  }
  
  private String createListRecordsResponse(ListRecordsRequest request) throws BadResumptionTokenException, CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
    try (Page<Header> headers = repo.listHeaders(request.getFilter());) {
      Spliterator<Record> spliterator = StreamSupport.stream(headers.spliterator(), false)
              .map(h->{ 
                if (!h.deleted) {
                  try {
                    return repo.readRecord(h.identifier, request.getMetadataPrefix()); 
                  } catch (ProtocolException ex) {
                    return null;
                  }
                } else {
                  Record rec = new Record(h, null, null);
                  return rec;
                }
              })
              .filter(r->r!=null)
              .spliterator();
      return createListRecordsSupplier(request, new ArrayList<>(), spliterator, headers.total(), 0).get();
    }
  }
  
  private Supplier<String> createListRecordsSupplier(ListRecordsRequest request, List<Record> bufferedRecords, Spliterator<Record> spliterator, long completeListSize, long cursor) {
    Record[] headerArray = Stream.concat(bufferedRecords.stream(), StreamSupport.stream(spliterator, false)) .limit(batchSize).toArray(Record[]::new);
    return () -> { 
      ResumptionToken resumptionToken = null;
      ArrayList<Record> prefetchedRecords = new ArrayList<>();
      if (spliterator.tryAdvance(record->{ prefetchedRecords.add(record); })) {
        Supplier<String> supplier = createListRecordsSupplier(request, prefetchedRecords, spliterator, completeListSize, cursor+headerArray.length);
        resumptionToken = tokenManager.register(supplier, completeListSize, cursor+headerArray.length);
      }
      ListRecordsResponse response = new ListRecordsResponse(request.getParameters(), OffsetDateTime.now(), headerArray, resumptionToken);
      return factory.createListRecordsResponse(response); 
    };
  }
}
