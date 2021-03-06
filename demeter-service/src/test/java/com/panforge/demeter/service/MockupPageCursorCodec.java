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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panforge.demeter.core.content.PageCursorCodec;
import java.io.IOException;

/**
 *
 * @author Piotr Andzel
 */
public class MockupPageCursorCodec implements PageCursorCodec<MockupPageCursor> {
  private static final ObjectMapper MAPPER = new ObjectMapper();
  static {
    MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Override
  public String toString(MockupPageCursor pageCursor) {
    try {
      return MAPPER.writeValueAsString(pageCursor);
    } catch (JsonProcessingException ex) {
      return "";
    }
  }

  @Override
  public MockupPageCursor fromString(String pageCursorStr) {
    try {
      return MAPPER.readValue(pageCursorStr, MockupPageCursor.class);
    } catch (IOException ex) {
      return null;
    }
  }
}
