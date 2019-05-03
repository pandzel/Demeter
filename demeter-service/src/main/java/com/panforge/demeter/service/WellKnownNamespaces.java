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
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Well known names paces.
 */
public class WellKnownNamespaces {
  private static final Logger LOGGER = LoggerFactory.getLogger(WellKnownNamespaces.class);
  private static final ObjectMapper MAPPER = new ObjectMapper();
  public static final WellKnownNamespaces INSTANCE = new WellKnownNamespaces();

  public final List<Namespace> NSLIST;
  public final Map<String, Namespace> NSMAP;

  /**
   * Creates instance of the well known namespaces.
   */
  public WellKnownNamespaces() {
    try (InputStream inp = Thread.currentThread().getContextClassLoader().getResourceAsStream("well-known-namespaces.json");) {
      NSArray nsa = MAPPER.readValue(inp, NSArray.class);
      NSLIST = Collections.unmodifiableList(nsa.stream().map(NS::toNamespace).collect(Collectors.toList()));
      
      HashMap<String, Namespace> nsmap = new HashMap<>();
      NSLIST.stream().forEach(ns->{
        if (!StringUtils.isBlank(ns.namespace)) {
          nsmap.put(ns.namespace, ns);
        }
        if (!StringUtils.isBlank(ns.schema)) {
          nsmap.put(ns.schema, ns);
        }
      });
      
      NSMAP = Collections.unmodifiableMap(nsmap);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }
}
