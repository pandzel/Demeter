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
package com.panforge.demeter.core.utils;

import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Piotr Andzel
 */
public class QueryUtilsTest {
  
  public QueryUtilsTest() {
  }

  @Test
  public void testParamsToQuery() {
    String inputQuery = "A=1&B=2&A=3";
    Map<String, List<String>> params = QueryUtils.queryToParams(inputQuery);
    String query = QueryUtils.paramsToQuery(params);
    
    assertEquals("Invalid length of query", inputQuery.length(), query.length());
  }

  @Test
  public void testQueryToParams() {
    Map<String, List<String>> params = QueryUtils.queryToParams("A=1&B=2&A=3");
    
    assertEquals("Invalid number of parameters", 2, params.size());
    assertTrue("Missing A", params.get("A")!=null);
    assertTrue("Missing B", params.get("B")!=null);
    assertEquals("Invalid number of valueas for A", 2, params.get("A").size());
    assertEquals("Invalid number of valueas for B", 1, params.get("B").size());
  }
  
  @Test
  public void testRejectKeys() {
    Map<String, List<String>> params = QueryUtils.queryToParams("A=1&B=2&A=3");
    params = QueryUtils.rejectKeys(params, "A");
    
    assertEquals("Invalid number of parameters", 1, params.size());
  }
  
}
