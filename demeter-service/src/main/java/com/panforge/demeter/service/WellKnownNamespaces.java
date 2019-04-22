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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Well known names paces.
 */
public class WellKnownNamespaces {
  private static final Logger LOGGER = LoggerFactory.getLogger(WellKnownNamespaces.class);
  private static final ObjectMapper MAPPER = new ObjectMapper();
  public static final WellKnownNamespaces INSTANCE = new WellKnownNamespaces();
  static {
    try {
      INSTANCE.load();
    } catch (IOException ex) {
      LOGGER.error(String.format("Error reading well known namespaces."), ex);
    }
  }
  
  private Map<String, Namespace> namespaces = Collections.emptyMap();
  
  /**
   * Find name space by name or schema.
   * @param nameOrSchema
   * @return name space or <code>null</code>
   */
  public Namespace find(String nameOrSchema) {
    return namespaces.get(nameOrSchema);
  }
  
  /**
   * Builds schema location string for given names or schemas
   * @param namesOrSchemas names or schemas
   * @return schema location string
   */
  public String buildSchemaLocation(String...namesOrSchemas) {
    return Arrays.stream(namesOrSchemas)
            .map(nameOrSchema->find(nameOrSchema))
            .filter(ObjectUtils::allNotNull)
            .distinct()
            .map(Namespace::toSchemaLocation)
            .collect(Collectors.joining(" "));
  }

  /**
   * Loads name spaces from 'well-known-namespaces.json'
   * @throws java.io.IOException if unable to load definition file
   */
  public void load() throws IOException {
    Map<String, Namespace> buffer = new HashMap<>();
    try (InputStream inp = Thread.currentThread().getContextClassLoader().getResourceAsStream("well-known-namespaces.json");) {
      NSArray nsa = MAPPER.readValue(inp, NSArray.class);
      nsa.stream().map(NS::toNamespace).forEach(n->{
        buffer.put(n.namespace, n);
        buffer.put(n.schema, n);
      });
    }
    namespaces = Collections.unmodifiableMap(buffer);
  }
}
