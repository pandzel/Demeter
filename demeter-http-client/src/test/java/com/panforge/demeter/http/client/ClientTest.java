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
package com.panforge.demeter.http.client;

import com.panforge.demeter.core.model.response.ListSetsResponse;
import com.panforge.demeter.core.model.response.GetRecordResponse;
import com.panforge.demeter.core.model.response.IdentifyResponse;
import com.panforge.demeter.core.model.response.ListIdentifiersResponse;
import com.panforge.demeter.core.model.response.ListRecordsResponse;
import com.panforge.demeter.core.model.response.ListMetadataFormatsResponse;
import com.panforge.demeter.core.api.Config;
import com.panforge.demeter.core.api.Context;
import java.net.URL;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import com.panforge.demeter.core.api.ResponseFactory;
import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.model.request.GetRecordRequest;
import com.panforge.demeter.core.model.request.IdentifyRequest;
import com.panforge.demeter.core.model.request.ListIdentifiersRequest;
import com.panforge.demeter.core.model.request.ListMetadataFormatsRequest;
import com.panforge.demeter.core.model.request.ListRecordsRequest;
import com.panforge.demeter.core.model.request.ListSetsRequest;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.response.elements.Set;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.OffsetDateTime;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * OaiPmh client test.
 */
public class ClientTest {
  
  private static final int MOCK_SERVER_PORT = 1080;
  private static ClientAndServer mockServer;
  private static ResponseFactory responseFactory;
  private static Config config;
  private static DocumentBuilder builder;
  
  private Client client;

  @BeforeClass
  public static void setUpClass() throws Exception {
    mockServer = startClientAndServer(MOCK_SERVER_PORT);
    
    config = new Config();
    
    Context context = new Context(config);
    responseFactory = new ResponseFactory(context);
    
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(false);
    builder = factory.newDocumentBuilder();
    
    mockServer.when(HttpRequest.request().withQueryStringParameter("verb", Verb.ListMetadataFormats.name()))
            .respond(HttpResponse.response().withBody(createListMetadataFormatsResponse()));
    mockServer.when(HttpRequest.request().withQueryStringParameter("verb", Verb.ListSets.name()))
            .respond(HttpResponse.response().withBody(createListSetsResponse()));
    mockServer.when(HttpRequest.request().withQueryStringParameter("verb", Verb.Identify.name()))
            .respond(HttpResponse.response().withBody(createIdentifyResponse()));
    mockServer.when(HttpRequest.request().withQueryStringParameter("verb", Verb.ListIdentifiers.name()))
            .respond(HttpResponse.response().withBody(createListIdentifiersResponse()));
    mockServer.when(HttpRequest.request().withQueryStringParameter("verb", Verb.ListRecords.name()))
            .respond(HttpResponse.response().withBody(createListRecordsResponse()));
    mockServer.when(HttpRequest.request().withQueryStringParameter("verb", Verb.GetRecord.name()))
            .respond(HttpResponse.response().withBody(createGetRecordResponse()));
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    mockServer.stop();
  }
  
  @Before
  public void setUp() throws Exception {
    client = new Client(new URL(String.format("http://localhost:%d", MOCK_SERVER_PORT)), true);
  }
  
  @Test
  public void testListMetadataFormats() throws Exception {
    ListMetadataFormatsRequest request = new ListMetadataFormatsRequest(null);
    ListMetadataFormatsResponse response = (ListMetadataFormatsResponse) client.execute(request);
    
    assertNotNull("No response received.", response);
    assertEquals("Invalid verb", Verb.ListMetadataFormats, response.request.verb);
  }
  
  @Test
  public void testListSetsFormats() throws Exception {
    ListSetsRequest request = new ListSetsRequest();
    ListSetsResponse response = (ListSetsResponse) client.execute(request);
    
    assertNotNull("No response received.", response);
    assertEquals("Invalid verb", Verb.ListSets, response.request.verb);
  }
  
  @Test
  public void testListIdentifiers() throws Exception {
    ListIdentifiersRequest request = new ListIdentifiersRequest("oai_dc", null, null, null);
    ListIdentifiersResponse response = (ListIdentifiersResponse) client.execute(request);
    
    assertNotNull("No response received.", response);
    assertEquals("Invalid verb", Verb.ListIdentifiers, response.request.verb);
  }
  
  @Test
  public void testListRecords() throws Exception {
    ListRecordsRequest request = new ListRecordsRequest("oai_dc", null, null, null);
    ListRecordsResponse response = (ListRecordsResponse) client.execute(request);
    
    assertNotNull("No response received.", response);
    assertEquals("Invalid verb", Verb.ListRecords, response.request.verb);
  }
  
  @Test
  public void testGetRecord() throws Exception {
    GetRecordRequest request = new GetRecordRequest(URI.create("identifier"), "oai");
    GetRecordResponse response = (GetRecordResponse) client.execute(request);
    
    assertNotNull("No response received.", response);
    assertEquals("Invalid verb", Verb.GetRecord, response.request.verb);
  }
  
  @Test
  public void testIdentify() throws Exception {
    IdentifyRequest request = new IdentifyRequest();
    IdentifyResponse response = (IdentifyResponse) client.execute(request);
    
    assertNotNull("No response received.", response);
    assertEquals("Invalid verb", Verb.Identify, response.request.verb);
  }
 
  private static String createListMetadataFormatsResponse() throws Exception {
    ListMetadataFormatsRequest request = new ListMetadataFormatsRequest(null);
    
    MetadataFormat fmt = new MetadataFormat(
            "oai_dc",
            "http://www.openarchives.org/OAI/2.0/oai_dc.xsd",
            "http://www.openarchives.org/OAI/2.0/oai_dc/"
    );
    
    ListMetadataFormatsResponse response = new ListMetadataFormatsResponse(new MetadataFormat[]{fmt}, OffsetDateTime.now(), null, request);
    
    return responseFactory.createListMetadataFormatsResponse(response);
  }
  
  private static String createListSetsResponse() throws Exception {
    Set set = new Set("music", "Music (set)");
    
    ListSetsRequest request = new ListSetsRequest();
    
    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);
    
    ListSetsResponse response = new ListSetsResponse(new Set[]{set}, resumptionToken, OffsetDateTime.now(), null, request);
    
    return responseFactory.createListSetsResponse(response);
  }
  
  private static String createIdentifyResponse() throws Exception {
    IdentifyRequest request = new IdentifyRequest();
    Document [] descriptions = new Document[] { parse(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                    + "<oai-identifier>"
                    + "<scheme>oai</scheme>"
                    + "<repositoryIdentifier>lcoa1.loc.gov</repositoryIdentifier>"
                    + "<delimiter>:</delimiter>"
                    + "<sampleIdentifier>oai:lcoa1.loc.gov:loc.music/musdi.002</sampleIdentifier>"
                    + "</oai-identifier>"
    )};
    
    IdentifyResponse response = IdentifyResponse.createFromConfig(config, descriptions, OffsetDateTime.now(), null, request);
    
    return responseFactory.createIdentifyResponse(response);
  }
  
  private static String createListIdentifiersResponse() throws Exception {
    ListIdentifiersRequest request = new ListIdentifiersRequest("oai", null, null, null);
    
    Header header = new Header(URI.create("identifier"), OffsetDateTime.now(), new String[] { "music" }, false);
    
    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);
    
    ListIdentifiersResponse response = new ListIdentifiersResponse(new Header[] { header }, resumptionToken, OffsetDateTime.now(), null, request);
    
    return responseFactory.createListIdentifiersResponse(response);
  }
  
  private static String createListRecordsResponse() throws Exception {
    ListRecordsRequest request = new ListRecordsRequest("oai", null, null, null);
    
    Header header = new Header(URI.create("identifier"), OffsetDateTime.now(), new String[] { "music" }, false);
    
    Document about = parse(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                    + "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">"
                    + "<dc:publisher>Los Alamos arXiv</dc:publisher>"
                    + "<dc:rights>Metadata may be used without restrictions as long as the oai identifier remains attached to it.</dc:rights>"
                    + "</oai_dc:dc>"
    );
    
    Document metadata = parse(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                    + "<rfc1807 xmlns=\"http://info.internet.isi.edu:80/in-notes/rfc/files/rfc1807.txt\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://info.internet.isi.edu:80/in-notes/rfc/files/rfc1807.txt http://www.openarchives.org/OAI/1.1/rfc1807.xsd\">"
                    + "<bib-version>v2</bib-version>"
                    + "<id>hep-th/9901001</id>"
                    + "<entry>January 1, 1999</entry>"
                    + "<title>Investigations of Radioactivity</title>"
                    + "<author>Ernest Rutherford</author>"
                    + "<date>March 30, 1999</date>"
                    + "</rfc1807>"
    );
    
    Record record = new Record(header, metadata, about);
    
    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);
    
    ListRecordsResponse response = new ListRecordsResponse(new Record[] { record }, resumptionToken, OffsetDateTime.now(), null, request);
    
    return responseFactory.createListRecordsResponse(response);
  }
  
  private static String createGetRecordResponse() throws Exception {
    GetRecordRequest request = new GetRecordRequest(URI.create("identifier"), "oai");
    
    Header header = new Header(request.getIdentifier(), OffsetDateTime.now(), new String[] { "music" }, false);
    
    Document about = parse(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                    + "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">"
                    + "<dc:publisher>Los Alamos arXiv</dc:publisher>"
                    + "<dc:rights>Metadata may be used without restrictions as long as the oai identifier remains attached to it.</dc:rights>"
                    + "</oai_dc:dc>"
    );
    
    Document metadata = parse(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                    + "<rfc1807 xmlns=\"http://info.internet.isi.edu:80/in-notes/rfc/files/rfc1807.txt\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://info.internet.isi.edu:80/in-notes/rfc/files/rfc1807.txt http://www.openarchives.org/OAI/1.1/rfc1807.xsd\">"
                    + "<bib-version>v2</bib-version>"
                    + "<id>hep-th/9901001</id>"
                    + "<entry>January 1, 1999</entry>"
                    + "<title>Investigations of Radioactivity</title>"
                    + "<author>Ernest Rutherford</author>"
                    + "<date>March 30, 1999</date>"
                    + "</rfc1807>"
    );
    
    Record record = new Record(header, metadata, about);
    
    GetRecordResponse response = new GetRecordResponse(record, OffsetDateTime.now(), null, request);
    
    return responseFactory.createGetRecordResponse(response);
  }
  
  private static Document parse(String xml) throws IOException, SAXException {
    try (InputStream xmlStream = new ByteArrayInputStream(xml.getBytes("UTF-8"))) {
      return builder.parse(xmlStream);
    }
  }
}
