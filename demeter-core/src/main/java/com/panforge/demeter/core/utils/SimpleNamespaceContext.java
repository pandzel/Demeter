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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;

/**
 * Simple namespace context.
 */
public class SimpleNamespaceContext implements NamespaceContext {
  private final Map<String, String> prefixToUri = new HashMap<>();
  private final Map<String, String> uriToPrefix = new HashMap<>();

  @Override
  public String getNamespaceURI(String prefix) {
    return prefixToUri.get(prefix);
  }

  @Override
  public String getPrefix(String namespaceURI) {
    return uriToPrefix.get(namespaceURI);
  }

  @Override
  public Iterator getPrefixes(String namespaceURI) {
    return prefixToUri.entrySet().stream()
            .filter(e->e.getValue().equals(namespaceURI))
            .map(Map.Entry<String, String>::getKey).iterator();
  }

  public SimpleNamespaceContext add(String prefix, String namespaceURI) {
    prefixToUri.put(prefix, namespaceURI);
    uriToPrefix.put(namespaceURI, prefix);
    return this;
  }
}
