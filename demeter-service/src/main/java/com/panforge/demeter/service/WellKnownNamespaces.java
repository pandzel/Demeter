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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Well known names paces.
 */
public class WellKnownNamespaces {
  private static final ObjectMapper MAPPER = new ObjectMapper();
  
  private final Map<String, Namespace> namespaces = new HashMap<>();

  /**
   * Loads name spaces from 'well-known-namespaces.json'
   * @throws java.io.IOException if unable to load definition file
   */
  public void load() throws IOException {
    try (InputStream inp = Thread.currentThread().getContextClassLoader().getResourceAsStream("well-known-namespaces.json");) {
      Namespaces ns = MAPPER.readValue(inp, Namespaces.class);
      ns.stream().forEach(n->{
        namespaces.put(n.namespace, n);
        namespaces.put(n.schema, n);
      });
    }
  }
}
