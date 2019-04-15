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
import com.panforge.demeter.core.api.ResponseParser;
import com.panforge.demeter.core.content.ContentProvider;
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.model.request.*;
import com.panforge.demeter.core.model.response.*;
import com.panforge.demeter.core.utils.QueryUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Service test
 */
public class ServiceTest {
  private static Config config;
  private static Service service;
  private static ResponseParser respParser;
  private static ContentProvider contentProvider;
  
  public ServiceTest() {
  }
  
  @BeforeClass
  public static void setUpClass() throws URISyntaxException {
    config = new Config();
    config.repositoryName = "Mockup repository";
    contentProvider = new MockupContentProvider().initialize();
    service = new Service(config, contentProvider);
    respParser = new ResponseParser();
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Test
  public void testIdentify() throws Exception {
    IdentifyRequest request = new IdentifyRequest();
    Map<String, String[]> parameters = request.getParameters();
    String responseStr = service.execute(parameters);
    Response response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNotNull("Incomplete response", response.parameters);
    assertEquals("Invalid response type", Verb.Identify.name(), QueryUtils.primeParams(response.parameters).get("verb"));
    
    IdentifyResponse responseObj = (IdentifyResponse)response;
    assertEquals("Invalid identify", config.repositoryName, responseObj.repositoryName);
  }
  
  @Test
  public void testListMetadataFormats() throws Exception {
    ListMetadataFormatsRequest request = new ListMetadataFormatsRequest(null);
    Map<String, String[]> parameters = request.getParameters();
    String responseStr = service.execute(parameters);
    Response response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNotNull("Incomplete response", response.parameters);
    assertEquals("Invalid response type", Verb.ListMetadataFormats.name(), QueryUtils.primeParams(response.parameters).get("verb"));
    
    ListMetadataFormatsResponse responseObj = (ListMetadataFormatsResponse)response;
    assertEquals("Invalid number of formats", contentProvider.listMetadataFormats(null).total(), responseObj.metadataFormats.length);
  }
  
  @Test
  public void testListSets() throws Exception {
    ListSetsRequest request = new ListSetsRequest();
    Map<String, String[]> parameters = request.getParameters();
    String responseStr = service.execute(parameters);
    Response response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNull("Errors received", response.errors);
    assertNotNull("Incomplete response", response.parameters);
    assertEquals("Invalid response type", Verb.ListSets.name(), QueryUtils.primeParams(response.parameters).get("verb"));
    
    ListSetsResponse responseObj = (ListSetsResponse)response;
    assertEquals("Invalid number of formats", contentProvider.listSets().total(), responseObj.listSets.length);
  }
  
  @Test
  public void testListIdentifiers() throws Exception {
    ListIdentifiersRequest request = new ListIdentifiersRequest("oai_dc", null, null, null);
    Map<String, String[]> parameters = request.getParameters();
    String responseStr = service.execute(parameters);
    Response response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNull("Errors received", response.errors);
    assertNotNull("Incomplete response", response.parameters);
    assertEquals("Invalid response type", Verb.ListIdentifiers.name(), QueryUtils.primeParams(response.parameters).get("verb"));
    
    ListIdentifiersResponse responseObj = (ListIdentifiersResponse)response;
    assertEquals("Invalid number of identifiers", contentProvider.listHeaders(null).total(), responseObj.headers.length);
  }
  
  @Test
  public void testListIdentifiersWithToken() throws Exception {
    int auxBatchSize = 3;
    Service auxService = new Service(config, contentProvider, new SimpleTokenManager(), auxBatchSize);
    ListIdentifiersRequest request = new ListIdentifiersRequest("oai_dc", null, null, null);
    Map<String, String[]> parameters = request.getParameters();
    String responseStr = auxService.execute(parameters);
    Response response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNull("Errors received", response.errors);
    assertNotNull("Incomplete response", response.parameters);
    assertEquals("Invalid response type", Verb.ListIdentifiers.name(), QueryUtils.primeParams(response.parameters).get("verb"));
    
    ListIdentifiersResponse responseObj = (ListIdentifiersResponse)response;
    assertTrue("Invalid number of identifiers", responseObj.headers.length <= auxBatchSize);
    assertNotNull("Missing resumptionToken", responseObj.resumptionToken);
    
    request = new ListIdentifiersRequest(responseObj.resumptionToken.value);
    parameters = request.getParameters();
    responseStr = auxService.execute(parameters);
    response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNull("Errors received", response.errors);
    assertNotNull("Incomplete response", response.parameters);
    assertEquals("Invalid response type", Verb.ListIdentifiers.name(), QueryUtils.primeParams(response.parameters).get("verb"));
    
    responseObj = (ListIdentifiersResponse)response;
    assertTrue("Invalid number of identifiers", auxBatchSize + responseObj.headers.length == contentProvider.listHeaders(null).total());
  }
  
  @Test
  public void testListRecords() throws Exception {
    ListRecordsRequest request = new ListRecordsRequest("oai_dc", null, null, null);
    Map<String, String[]> parameters = request.getParameters();
    String responseStr = service.execute(parameters);
    Response response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNull("Errors received", response.errors);
    assertNotNull("Incomplete response", response.parameters);
    assertEquals("Invalid response type", Verb.ListRecords.name(), QueryUtils.primeParams(response.parameters).get("verb"));
    
    ListRecordsResponse responseObj = (ListRecordsResponse)response;
    assertEquals("Invalid number of formats", contentProvider.listHeaders(null).total(), responseObj.records.length);
  }
  
  @Test
  public void testListRecordsWithToken() throws Exception {
    int auxBatchSize = 3;
    Service auxService = new Service(config, contentProvider, new SimpleTokenManager(), auxBatchSize);
    ListRecordsRequest request = new ListRecordsRequest("oai_dc", null, null, null);
    Map<String, String[]> parameters = request.getParameters();
    String responseStr = auxService.execute(parameters);
    Response response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNull("Errors received", response.errors);
    assertNotNull("Incomplete response", response.parameters);
    assertEquals("Invalid response type", Verb.ListRecords.name(), QueryUtils.primeParams(response.parameters).get("verb"));
    
    ListRecordsResponse responseObj = (ListRecordsResponse)response;
    assertTrue("Invalid number of records", responseObj.records.length <= auxBatchSize);
    assertNotNull("Missing resumptionToken", responseObj.resumptionToken);
    
    request = new ListRecordsRequest(responseObj.resumptionToken.value);
    parameters = request.getParameters();
    responseStr = auxService.execute(parameters);
    response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNull("Errors received", response.errors);
    assertNotNull("Incomplete response", response.parameters);
    assertEquals("Invalid response type", Verb.ListRecords.name(), QueryUtils.primeParams(response.parameters).get("verb"));
    
    responseObj = (ListRecordsResponse)response;
    assertTrue("Invalid number of records", auxBatchSize + responseObj.records.length == contentProvider.listHeaders(null).total());
  }
  
  @Test
  public void testGetRecord() throws Exception {
    GetRecordRequest request = new GetRecordRequest(URI.create("001"), "oai_dc");
    Map<String, String[]> parameters = request.getParameters();
    String responseStr = service.execute(parameters);
    Response response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNull("Errors received", response.errors);
    assertNotNull("Incomplete response", response.parameters);
    assertEquals("Invalid response type", Verb.GetRecord.name(), QueryUtils.primeParams(response.parameters).get("verb"));
    
    GetRecordResponse responseObj = (GetRecordResponse)response;
    assertNotNull("Missing record", responseObj.record);
  }
}
