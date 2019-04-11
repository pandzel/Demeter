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
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
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
    assertNotNull("Incomplete response", response.request);
    assertEquals("Invalid response type", Verb.Identify, response.request.verb);
    
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
    assertNotNull("Incomplete response", response.request);
    assertEquals("Invalid response type", Verb.ListMetadataFormats, response.request.verb);
    
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
    assertNotNull("Incomplete response", response.request);
    assertEquals("Invalid response type", Verb.ListSets, response.request.verb);
    
    ListSetsResponse responseObj = (ListSetsResponse)response;
    assertEquals("Invalid number of formats", contentProvider.listSets().total(), responseObj.listSets.length);
  }
  
  @Test
  public void testListHeaders() throws Exception {
    ListIdentifiersRequest request = new ListIdentifiersRequest("oai_dc", null, null, null);
    Map<String, String[]> parameters = request.getParameters();
    String responseStr = service.execute(parameters);
    Response response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNotNull("Incomplete response", response.request);
    assertEquals("Invalid response type", Verb.ListIdentifiers, response.request.verb);
    
    ListIdentifiersResponse responseObj = (ListIdentifiersResponse)response;
    assertEquals("Invalid number of formats", contentProvider.listHeaders(null).total(), responseObj.headers.length);
  }
}
