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

import com.panforge.demeter.core.api.exception.BadResumptionTokenException;
import com.panforge.demeter.core.content.PageCursor;
import com.panforge.demeter.core.model.ResumptionToken;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Piotr Andzel
 */
public class SimpleTokenManagerTest {
  private static final long expiration = 1000;
  private static SimpleTokenManager<MockupPageCursor> tm;
  
  public SimpleTokenManagerTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
    tm = new SimpleTokenManager<>(new MockupPageCursorCodec(), expiration);
  }
  
  @AfterClass
  public static void tearDownClass() {
  }

  @Test
  public void testToken() throws BadResumptionTokenException {
    ResumptionToken token = tm.put(new MockupPageCursor());
    assertNotNull("No token", token);
    assertNotNull("Empty token value", token.value);
    MockupPageCursor pageCursor = tm.pull(token.value);
    assertNotNull("No content received", pageCursor);
  }
  
  @Test(expected = com.panforge.demeter.core.api.exception.BadResumptionTokenException.class)
  public void testExpiration() throws BadResumptionTokenException, InterruptedException {
    ResumptionToken token = tm.put(new MockupPageCursor());
    assertNotNull("No token", token);
    assertNotNull("Empty token value", token.value);
    Thread.sleep(expiration+10);
    MockupPageCursor pageCursor = tm.pull(token.value);
  }
  
}
