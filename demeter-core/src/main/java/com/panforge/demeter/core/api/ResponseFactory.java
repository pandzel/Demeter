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

import com.panforge.demeter.core.api.Config.Compression;
import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.utils.builder.DocBuilder;
import com.panforge.demeter.core.utils.builder.DocNode;
import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.model.request.Request;
import com.panforge.demeter.core.model.response.GetRecordResponse;
import com.panforge.demeter.core.model.response.IdentifyResponse;
import com.panforge.demeter.core.model.response.ListIdentifiersResponse;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.ListMetadataFormatsResponse;
import com.panforge.demeter.core.model.response.ListRecordsResponse;
import com.panforge.demeter.core.model.response.ListSetsResponse;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.response.Response;
import com.panforge.demeter.core.utils.QueryUtils;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.w3c.dom.Document;

/**
 * Response factory.
 * <p>
 * Converts response object into the corresponding XML response.
 */
public class ResponseFactory {

  private final Context CTX;

  /**
   * Creates instance of the factory.
   *
   * @param CTX context
   */
  public ResponseFactory(Context CTX) {
    this.CTX = CTX;
  }

  /**
   * Creates error response.
   * @param responseDate response date
   * @param reqParams request parameters
   * @param errors errors
   * @return response response
   */
  public String createErrorResponse(OffsetDateTime responseDate, Map<String, String[]> reqParams, ErrorInfo [] errors) {
    Verb verb = Arrays.stream(reqParams.get("verb")).map(s->Verb.parse(s)).filter(v->v!=null).findFirst().orElse(null);
    reqParams = QueryUtils.rejectKeys(reqParams, "verb");
    return new DocBuilder().begin()
            .child("responseDate").value(responseDate.format(DateTimeFormatter.ISO_DATE_TIME)).done()
            .child("request").attr("verb", ()->verb!=null? verb.name(): null, ()->verb!=null).forEach(reqParams != null ? QueryUtils.paramsToList(reqParams).stream() : null, (n, e) -> n.attr(e[0], e[1])).value(CTX.config.baseURL).done()
            .forEach(Arrays.stream(errors!=null? errors: new ErrorInfo[0]), (nd,error)->{
              nd.child("error", () -> error != null).attr("code", error != null ? error.errorCode.name() : "").value(error != null ? error.message : "");
            })
            .end();
  }

  /**
   * Creates GetRecord response.
   *
   * @param response GetRecord response object
   * @return GetRecord XML string
   */
  public String createGetRecordResponse(GetRecordResponse response) {
    Record record = response.record;
    Header header = record.header;

    return createHeader(response, response.parameters)
            .child("GetRecord")
            .child("record")
            .child("header")
            .attr("status", "deleted", () -> header.deleted)
            .child("identifier").value(header.identifier.toASCIIString()).done()
            .child("datestamp").value(header.datestamp.format(DateTimeFormatter.ISO_DATE)).done()
            .forEach(Stream.of(header.set != null ? header.set : new String[]{}), (hnd, set) -> {
              hnd.child("setSpec").value(set).done();
            })
            .done()
            .child("metadata", () -> record.metadata != null && record.metadata.getFirstChild() != null).addDocument(record.metadata).done()
            .forEach(Stream.of(record.about != null ? record.about : new Document[]{}), (hnd, about) -> {
              hnd.child("about").addDocument(about).done();
            })
            .done()
            .done()
            .end();
  }

  /**
   * Creates ListRecords response.
   *
   * @param response ListRecords response object
   * @return ListRecords XML string
   */
  public String createListRecordsResponse(ListRecordsResponse response) {
    return createHeader(response, response.parameters)
            .child("ListRecords")
            .forEach(Stream.of(response.records != null ? response.records : new Record[]{}), (nd, record) -> {
              Header header = record.header;
              nd.child("record")
                      .child("header")
                      .attr("status", "deleted", () -> header.deleted)
                      .child("identifier").value(header.identifier.toASCIIString()).done()
                      .child("datestamp").value(header.datestamp.format(DateTimeFormatter.ISO_DATE)).done()
                      .forEach(Stream.of(header.set != null ? header.set : new String[]{}), (hnd, set) -> {
                        hnd.child("setSpec").value(set).done();
                      })
                      .done()
                      .child("metadata", () -> record.metadata != null && record.metadata.getFirstChild() != null).addDocument(record.metadata).done()
                      .forEach(Stream.of(record.about != null ? record.about : new Document[]{}), (hnd, about) -> {
                        hnd.child("about").addDocument(about).done();
                      })
                      .done()
                      .done()
                      .done();
            })
            .child(response.resumptionToken, (nd, resumptionToken) -> appendResumptionToken(nd, resumptionToken, response.getParameter("resumptionToken") == null))
            .end();
  }

  /**
   * Creates ListIdentifiers response.
   *
   * @param response ListIdentifiers response object
   * @return ListIdentifiers XML string
   */
  public String createListIdentifiersResponse(ListIdentifiersResponse response) {
    return createHeader(response, response.parameters)
            .child("ListIdentifiers")
            .forEach(Stream.of(response.headers != null ? response.headers : new Header[]{}), (nd, header) -> {
              nd.child("header")
                      .attr("status", "deleted", () -> header.deleted)
                      .child("identifier").value(header.identifier.toASCIIString()).done()
                      .child("datestamp").value(header.datestamp.format(DateTimeFormatter.ISO_DATE)).done()
                      .forEach(Stream.of(header.set != null ? header.set : new String[]{}), (hnd, set) -> {
                        hnd.child("setSpec").value(set);
                      })
                      .done()
                      .done();
            })
            .child(response.resumptionToken, (nd, resumptionToken) -> appendResumptionToken(nd, resumptionToken, response.getParameter("resumptionToken") == null))
            .end();
  }

  /**
   * Creates Identify response.
   *
   * @param response Identify response object
   * @return Identify XML string
   */
  public String createIdentifyResponse(IdentifyResponse response) {
    return createHeader(response, response.parameters)
            .child("Identify")
            .child("repositoryName").value(response.repositoryName).done()
            .child("baseURL").value(response.baseURL).done()
            .child("protocolVersion").value(response.protocolVersion).done()
            .forEach(Stream.of(response.adminEmail != null ? response.adminEmail : new String[]{}), (nd, v) -> {
              nd.child("adminEmail").value(v).done();
            })
            .child("earliestDatestamp").value(response.earliestDatestamp != null ? response.earliestDatestamp.format(DateTimeFormatter.ISO_DATE_TIME) : "").done()
            .child("deletedRecord").value(response.deletedRecord != null ? response.deletedRecord.name() : "").done()
            .child("granularity").value(response.granularity).done()
            .forEach(Stream.of(response.compression != null ? response.compression : new Compression[]{}), (nd, v) -> {
              nd.child("compression").value(v.name()).done();
            })
            .forEach(Stream.of(response.descriptions != null ? response.descriptions : new Document[]{}).filter(doc->doc!=null), (nd, doc) -> nd.child("description").addDocument(doc))
            .end();
  }

  /**
   * Creates ListSets response.
   *
   * @param response ListSets response object
   * @return ListSets XML string
   */
  public String createListSetsResponse(ListSetsResponse response) {
    return createHeader(response, response.parameters)
            .child("ListSets", () -> response.listSets != null).forEach(Stream.of(response.listSets), (node, set) -> {
              node
                .child("set")
                .child("setSpec").value(set.setSpec).done()
                .child("setName").value(set.setName).done()
                // TODO: generate set descriptions node
                .done()
                .child(response.resumptionToken, (nd, resumptionToken) -> appendResumptionToken(nd, resumptionToken, response.getParameter("resumptionToken") == null));
            }).done()
            .end();
  }

  /**
   * Creates ListMetadataFormats response.
   *
   * @param response ListMetadataFormats response object
   * @return ListMetadataFormats XML string
   */
  public String createListMetadataFormatsResponse(ListMetadataFormatsResponse response) {
    return createHeader(response, response.parameters)
            .child("ListMetadataFormats", () -> response.metadataFormats != null).forEach(Stream.of(response.metadataFormats), (node, fmt) -> {
              node
                .child("metadataFormat")
                .child("metadataPrefix").value(fmt.metadataPrefix).done()
                .child("schema").value(fmt.schema).done()
                .child("metadataNamespace").value(fmt.metadataNamespace).done()
                .done();
            }).done()
            .end();
  }

  private DocNode createHeader(Response<? extends Request> response, Map<String, String[]> reqParams) {
    return new DocBuilder().begin()
            .child("responseDate").value(response.responseDate.format(DateTimeFormatter.ISO_DATE_TIME)).done()
            .child("request").attr("verb", response.getParameter("verb")).forEach(reqParams != null ? reqParams.entrySet().stream() : new HashMap<String,String[]>().entrySet().stream(), (n, e) -> Arrays.stream(e.getValue()).forEach(val->n.attr(e.getKey(), val))).value(CTX.config.baseURL).done();
  }

  private void appendResumptionToken(DocNode parent, ResumptionToken resumptionToken, boolean printValue) {
    parent.child("resumptionToken", () -> resumptionToken != null)
            .attr("expirationDate", () -> resumptionToken.expirationDate.format(DateTimeFormatter.ISO_DATE_TIME), () -> resumptionToken.expirationDate != null)
            .attr("completeListSize", Long.toString(resumptionToken.completeListSize))
            .attr("cursor", Long.toString(resumptionToken.cursor))
            .value(printValue ? resumptionToken.value : null).done();
  }
}
