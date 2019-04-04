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

import com.panforge.demeter.core.content.ContentProvider;
import com.panforge.demeter.core.content.Cursor;
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
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

/**
 * Service.
 */
public class Service {
  protected final ContentProvider repo;
  protected final TokenManager tokenManager;
  
  protected final Context ctx;
  protected final RequestParser parser;
  protected final ResponseFactory factory;
  protected final long batchSize;

  /**
   * Creates instance of the service.
   * @param repo repository
   * @param tokenManager token manager
   * @param batchSize batch size
   */
  public Service(ContentProvider repo, TokenManager tokenManager, long batchSize) {
    this.repo = repo;
    this.tokenManager = tokenManager;
    this.batchSize = batchSize;
    
    this.ctx = new Context(repo.getConfig());
    this.parser = new RequestParser(ctx);
    this.factory = new ResponseFactory(ctx);
  }
  
  /**
   * Executes query.
   * @param query query
   * @return response
   */
  public String execute(String query) {
    Map<String, List<String>> params = QueryUtils.queryToParams(query);
    try {
      Request request = parser.parse(params);
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
          return factory.createErrorResponse(OffsetDateTime.now(), params, new ErrorInfo[]{new ErrorInfo(ErrorCode.badArgument, "Error parsing request.")});
      }
    } catch (ProtocolException pex) {
      return factory.createErrorResponse(OffsetDateTime.now(), params, pex.infos);
    }
  }
  
  private String createIdentifyResponse(IdentifyRequest request) {
    IdentifyResponse idetifyResponse = IdentifyResponse.createFromConfig(repo.getConfig(), null, OffsetDateTime.now(), request);
    return factory.createIdentifyResponse(idetifyResponse);
  }
  
  private String createListMetadataFormatsResponse(ListMetadataFormatsRequest request) throws IdDoesNotExistException, NoMetadataFormatsException {
    try (Cursor<MetadataFormat> metadataFormats = repo.listMetadataFormats(request.getIdentifier());) {
      MetadataFormat[] metadataFormatsArray = StreamSupport.stream(metadataFormats.spliterator(), false).toArray(MetadataFormat[]::new);
      ListMetadataFormatsResponse metadataFormatsResponse = new ListMetadataFormatsResponse(metadataFormatsArray, OffsetDateTime.now(), request);
      return factory.createListMetadataFormatsResponse(metadataFormatsResponse);
    }
  }
  
  private String createGetRecordResponse(GetRecordRequest request) throws IdDoesNotExistException, CannotDisseminateFormatException {
    Record record = repo.readRecord(request.getIdentifier(), request.getMetadataPrefix());
    if (record==null) {
      throw new IdDoesNotExistException(String.format("Unknown record id: '%s'", request.getIdentifier()));
    }
    GetRecordResponse getRecordResponse = new GetRecordResponse(record, OffsetDateTime.now(), request);
    return factory.createGetRecordResponse(getRecordResponse);
  }
  
  private String createListSetsResponse(ListSetsRequest request) throws BadResumptionTokenException, NoSetHierarchyException {
    try (Cursor<Set> listSets = repo.listSets();) {
      Spliterator<Set> spliterator = listSets.spliterator();
      return createListSetsSupplier(request, spliterator, listSets.total(), 0).get();
    }
  }
  
  private Supplier<String> createListSetsSupplier(ListSetsRequest request, Spliterator<Set> spliterator, long completeListSize, long cursor) {
    Set[] setArray = StreamSupport.stream(spliterator, false).limit(batchSize).toArray(Set[]::new);
    return () -> { 
      ResumptionToken resumptionToken = null;
      if (spliterator.tryAdvance(set->{})) {
        Supplier<String> supplier = createListSetsSupplier(request, spliterator, completeListSize, cursor+setArray.length);
        resumptionToken = tokenManager.register(supplier, completeListSize, cursor+setArray.length);
      }
      ListSetsResponse response = new ListSetsResponse(setArray, resumptionToken, OffsetDateTime.now(), request);
      return factory.createListSetsResponse(response); 
    };
  }
  
  private String createListIdentifiersResponse(ListIdentifiersRequest request) throws BadResumptionTokenException, CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
    // TODO: use request criteria
    try (Cursor<Header> headers = repo.listHeaders(request.getFilter());) {
      Spliterator<Header> spliterator = headers.spliterator();
      return createListIdentifiersSupplier(request, spliterator, headers.total(), 0).get();
    }
  }
  
  private Supplier<String> createListIdentifiersSupplier(ListIdentifiersRequest request, Spliterator<Header> spliterator, long completeListSize, long cursor) {
    Header[] headerArray = StreamSupport.stream(spliterator, false).limit(batchSize).toArray(Header[]::new);
    return () -> { 
      ResumptionToken resumptionToken = null;
      if (spliterator.tryAdvance(set->{})) {
        Supplier<String> supplier = createListIdentifiersSupplier(request, spliterator, completeListSize, cursor+headerArray.length);
        resumptionToken = tokenManager.register(supplier, completeListSize, cursor+headerArray.length);
      }
      ListIdentifiersResponse response = new ListIdentifiersResponse(headerArray, resumptionToken, OffsetDateTime.now(), request);
      return factory.createListIdentifiersResponse(response); 
    };
  }
  
  private String createListRecordsResponse(ListRecordsRequest request) throws BadResumptionTokenException, CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
    try {
      if (!StreamSupport.stream(repo.listMetadataFormats(null).spliterator(), false).map(f->f.metadataPrefix).anyMatch(p->p.equals(request.getMetadataPrefix()))) {
        throw new CannotDisseminateFormatException(String.format("Invalid metadata format prefix: '%s'", request.getMetadataPrefix()));
      }
    } catch (NoMetadataFormatsException|IdDoesNotExistException ex) {
        throw new CannotDisseminateFormatException(String.format("Invalid metadata format prefix: '%s'", request.getMetadataPrefix()), ex);
    }
    try (Cursor<Header> headers = repo.listHeaders(request.getFilter());) {
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
      return createListRecordsSupplier(request, spliterator, headers.total(), 0).get();
    }
  }
  
  private Supplier<String> createListRecordsSupplier(ListRecordsRequest request, Spliterator<Record> spliterator, long completeListSize, long cursor) {
    Record[] headerArray = StreamSupport.stream(spliterator, false).limit(batchSize).toArray(Record[]::new);
    return () -> { 
      ResumptionToken resumptionToken = null;
      if (spliterator.tryAdvance(record->{})) {
        Supplier<String> supplier = createListRecordsSupplier(request, spliterator, completeListSize, cursor+headerArray.length);
        resumptionToken = tokenManager.register(supplier, completeListSize, cursor+headerArray.length);
      }
      ListRecordsResponse response = new ListRecordsResponse(headerArray, resumptionToken, OffsetDateTime.now(), request);
      return factory.createListRecordsResponse(response); 
    };
  }
}
