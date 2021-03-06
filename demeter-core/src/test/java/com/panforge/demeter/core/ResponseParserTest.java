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
package com.panforge.demeter.core;

import com.panforge.demeter.core.api.Config;
import com.panforge.demeter.core.api.ResponseFactory;
import com.panforge.demeter.core.api.Context;
import com.panforge.demeter.core.api.ResponseParser;
import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.model.request.GetRecordRequest;
import com.panforge.demeter.core.model.request.IdentifyRequest;
import com.panforge.demeter.core.model.request.ListMetadataFormatsRequest;
import com.panforge.demeter.core.model.request.ListSetsRequest;
import com.panforge.demeter.core.model.request.ListIdentifiersRequest;
import com.panforge.demeter.core.model.request.ListRecordsRequest;
import com.panforge.demeter.core.model.response.GetRecordResponse;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.IdentifyResponse;
import com.panforge.demeter.core.model.response.ListIdentifiersResponse;
import com.panforge.demeter.core.model.response.ListMetadataFormatsResponse;
import com.panforge.demeter.core.model.response.ListRecordsResponse;
import com.panforge.demeter.core.model.response.ListSetsResponse;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.response.elements.Set;
import static com.panforge.demeter.core.DocumentSamples.*;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

/**
 * Parse response unit test.
 */
public class ResponseParserTest {
  private static Config config;
  private static Context ctx;
  private static ResponseFactory f;
  private static ResponseParser parser;
  
  @BeforeClass
  public static void initClass() throws ParserConfigurationException {
    config = new Config();
    config.repositoryName = "Sample";
    config.baseURL = "http://localhost/oaipmh";
    config.protocolVersion = "2.0";
    config.adminEmail = new String[] { "somebody@company.com", "anubody@company.com" };
    config.deletedRecord = Config.Deletion.Persistent;
    config.granularity = "YYYY-MM-DDThh:mm:ssZ";
    config.earliestDatestamp = OffsetDateTime.now();
    config.compression = Config.Compression.values();
    
    ctx = new Context(config);
    f = new ResponseFactory(ctx);
    
    parser = new ResponseParser();
  }

  @Test
  public void testCreateGetRecordResponse() throws Exception {
    GetRecordRequest request = new GetRecordRequest(URI.create("identifier"), "oai");
    
    Header header = new Header(request.getIdentifier(), OffsetDateTime.now(), new String[] { "music" }, false);
    
    Record record = new Record(header, oai_dc(), new Document[]{rfc_1807()});
    
    GetRecordResponse response = new GetRecordResponse(request.getParameters(), OffsetDateTime.now(), record);
    
    String rsp = f.createGetRecordResponse(response);
    GetRecordResponse parsed = (GetRecordResponse)parser.parse(rsp);
    
    assertNotNull("No parsed response", parsed);
    assertNotNull("No record", parsed.record);
    
    assertNotNull("No header", parsed.record.header);
    assertNotNull("No metadata", parsed.record.metadata);
    assertNotNull("No about", parsed.record.about);
    assertTrue("No about", parsed.record.about.length > 0);
  }
  
  @Test
  public void testCreateListRecordsResponse() throws Exception {
    ListRecordsRequest request = new ListRecordsRequest("oai", null, null, null);
    
    Header header = new Header(URI.create("identifier"), OffsetDateTime.now(), new String[] { "music" }, false);
    
    Record record = new Record(header, oai_dc(), new Document[]{rfc_1807()});
    
    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);
    
    ListRecordsResponse response = new ListRecordsResponse(request.getParameters(), OffsetDateTime.now(), new Record[] { record }, resumptionToken);
    
    String rsp = f.createListRecordsResponse(response);
    
    ListRecordsResponse parsed = (ListRecordsResponse)parser.parse(rsp);
    assertNotNull("No parsed response", parsed);
    assertNotNull("No records", parsed.records);
    assertTrue("No records", parsed.records.length > 0);
    assertNotNull("No header", parsed.records[0].header);
    assertNotNull("No metadata", parsed.records[0].metadata);
    assertNotNull("No about", parsed.records[0].about);
    assertTrue("No about", parsed.records[0].about.length > 0);
  }
  
  @Test
  public void testIdentifyResponse() throws Exception {
    IdentifyRequest request = new IdentifyRequest();
    Document [] descriptions = new Document[] {};
    
    IdentifyResponse response = IdentifyResponse.createFromConfig(request.getParameters(), OffsetDateTime.now(), config, descriptions);
    
    String rsp = f.createIdentifyResponse(response);
    
    IdentifyResponse parsed = (IdentifyResponse)parser.parse(rsp);
    assertNotNull("No parsed response", parsed);
    assertEquals("Different repositoryName", parsed.repositoryName, response.repositoryName);
    assertEquals("Different baseURL", parsed.baseURL, response.baseURL);
    assertEquals("Different protocolVersion", parsed.protocolVersion, response.protocolVersion);
    assertArrayEquals("Different compression", parsed.compression, response.compression);
    assertEquals("Different deletedRecord", parsed.deletedRecord, response.deletedRecord);
    assertEquals("Different earliestDatestamp", parsed.earliestDatestamp.format(DateTimeFormatter.ISO_DATE), response.earliestDatestamp.format(DateTimeFormatter.ISO_DATE));
    assertEquals("Different granularity", parsed.granularity, response.granularity);
    assertTrue("Different adminEmail", Arrays.deepEquals(parsed.adminEmail, response.adminEmail));
    assertNotNull("No descriptions", parsed.descriptions);
    // TODO: provide identify description test
    // assertEquals("Invalid number of descriptions.", 1, parsed.descriptions.length);
  }
  
  @Test
  public void testCreateListIdentifiersResponse() throws Exception {
    ListIdentifiersRequest request = new ListIdentifiersRequest("oai", null, null, null);
    
    Header header = new Header(URI.create("identifier"), OffsetDateTime.now(), new String[] { "music" }, false);
    
    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);
    
    ListIdentifiersResponse response = new ListIdentifiersResponse(request.getParameters(), OffsetDateTime.now(), new Header[] { header }, resumptionToken);
    
    String rsp = f.createListIdentifiersResponse(response);
    
    ListIdentifiersResponse parsed = (ListIdentifiersResponse)parser.parse(rsp);
    assertNotNull("No parsed response", parsed);
    assertNotNull("No parsed response", parsed.headers);
    assertTrue("Invalid number of headers", parsed.headers.length==response.headers.length);
    assertEquals("Different header", response.headers[0], parsed.headers[0]);
    assertNotNull("No parsed resumption token", parsed.resumptionToken);
    assertEquals("Different resumption token", resumptionToken, parsed.resumptionToken);
  }
  
  @Test
  public void testCreateListMetadataFormatsResponse() throws Exception {
    ListMetadataFormatsRequest request = new ListMetadataFormatsRequest(null);
    
    MetadataFormat fmt = new MetadataFormat(
            "oai_dc",
            "http://www.openarchives.org/OAI/2.0/oai_dc.xsd",
            "http://www.openarchives.org/OAI/2.0/oai_dc/"
    );
    
    ListMetadataFormatsResponse response = new ListMetadataFormatsResponse(request.getParameters(), OffsetDateTime.now(), new MetadataFormat[]{fmt});
    
    String rsp = f.createListMetadataFormatsResponse(response);
    
    ListMetadataFormatsResponse parsed = (ListMetadataFormatsResponse)parser.parse(rsp);
    assertNotNull("No parsed response", parsed);
    assertNotNull("No parsed formats", parsed.metadataFormats);
    assertTrue("Invalid number of formats", parsed.metadataFormats.length==response.metadataFormats.length);
    assertEquals("Different format", response.metadataFormats[0], parsed.metadataFormats[0]);
  }
  
  @Test
  public void testListSetsResponse() throws Exception  {
    
    Set set = new Set("music", "Music (set)", new Document[] { oai_dc() });
    
    ListSetsRequest request = new ListSetsRequest();
    
    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);
    
    ListSetsResponse response = new ListSetsResponse(request.getParameters(), OffsetDateTime.now(), new Set[]{set}, resumptionToken);
    
    String rsp = f.createListSetsResponse(response);
    
    ListSetsResponse parsed = (ListSetsResponse)parser.parse(rsp);
    
    assertNotNull("No parsed response", parsed);
    assertNotNull("No parsed set", parsed.listSets);
    assertTrue("Invalid number of sets", parsed.listSets.length==response.listSets.length);
    assertEquals("Different set", response.listSets[0], parsed.listSets[0]);
    assertNotNull("No parsed resumption token", parsed.resumptionToken);
    assertEquals("Different resumption token", resumptionToken, parsed.resumptionToken);
    assertNotNull("Missing set description", parsed.listSets[0].descriptions);
    assertTrue("Missing set description", parsed.listSets[0].descriptions.length > 0);
  }
  
}
