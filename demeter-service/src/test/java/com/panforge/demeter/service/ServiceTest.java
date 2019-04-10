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
import com.panforge.demeter.core.api.Context;
import com.panforge.demeter.core.api.RequestParser;
import com.panforge.demeter.core.api.ResponseParser;
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.model.request.IdentifyRequest;
import com.panforge.demeter.core.model.response.Response;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Piotr Andzel
 */
public class ServiceTest {
  private static Context ctx;
  private static Service service;
  private static RequestParser reqParser;
  private static ResponseParser respParser;
  
  public ServiceTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
    Config config = new Config();
    ctx = new Context(config);
    service = new Service(config, new MockupContentProvider());
    reqParser = new RequestParser(ctx);
    respParser = new ResponseParser();
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Test
  public void testConfig() throws Exception {
    IdentifyRequest request = new IdentifyRequest();
    Map<String, String[]> parameters = request.getParameters();
    String responseStr = service.execute(parameters);
    Response response = respParser.parse(responseStr);
    
    assertNotNull("Empty response", response);
    assertNotNull("Incomplete response", response.request);
    assertEquals("Invalid response type", Verb.Identify, response.request.verb);
  }
}
