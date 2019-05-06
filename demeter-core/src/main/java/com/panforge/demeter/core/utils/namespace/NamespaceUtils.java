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
package com.panforge.demeter.core.utils.namespace;

import com.panforge.demeter.core.utils.nodeiter.NodeIterable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

/**
 * Namespace utility class.
 */
public class NamespaceUtils {
  
  /**
   * Sanitizes node (deep)
   * <p>
   * Removes all element nodes which ar not within legitimate namespace.
   * @param nd node to sanitize
   */
  public static void sanitize(Node nd) {
    boolean defaultNamespaceValid = checkDefaultNamespaceUri(nd);
    List<Node> nodesToRemove = NodeIterable.stream(nd.getChildNodes())
            .filter(n->n.getNodeType()==Node.ELEMENT_NODE)
            .filter(n->!(n.getNamespaceURI()!=null? Namespaces.NSMAP.containsKey(n.getNamespaceURI()): defaultNamespaceValid))
            .collect(Collectors.toList());
    
    nodesToRemove.forEach(n->nd.removeChild(n));
    
    NodeIterable.stream(nd.getChildNodes())
            .filter(n->n.getNodeType()==1)
            .forEach(n->sanitize(n));
  }

  /**
   * Generates schema location.
   * @param uris namespace URI's
   * @return schema location
   */
  public static String generateSchemaLocation(Collection<String> uris) {
    return uris.stream()
            .map(uri -> Namespaces.NSMAP.get(uri))
            .filter(ns -> ns != null && !StringUtils.isBlank(ns.namespace) && !StringUtils.isBlank(ns.schema))
            .map(Namespace::toSchemaLocation)
            .collect(Collectors.joining(" "));
  }

  /**
   * Collects namespaces from multiple nodes of the same document.
   * @param nodes nodes
   * @return map of namespace prefixes to namespace URI's.
   */
  public static Map<String, String> collectNamespaces(Collection<Node> nodes) {
    return collectNamespaces(nodes.stream());
  }

  /**
   * Collects namespaces from multiple nodes of the same document.
   * @param nodes nodes
   * @return map of namespace prefixes to namespace URI's.
   */
  public static Map<String, String> collectNamespaces(Stream<Node> nodes) {
    return nodes
            .flatMap(nd -> collectNamespaces(nd).entrySet().stream())
            .distinct()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * Collects namespaces from a single node (deep).
   * @param nd document node
   * @return map of namespace prefixes to namespace URI's.
   */
  public static Map<String, String> collectNamespaces(Node nd) {
    HashMap<String, String> ndUris = new HashMap<>();

    if (!StringUtils.isBlank(nd.getPrefix()) && !StringUtils.isBlank(nd.getNamespaceURI())) {
      ndUris.put(nd.getPrefix(), nd.getNamespaceURI());
    }

    NodeIterable.stream(nd.getChildNodes()).forEach(chNd -> ndUris.putAll(collectNamespaces(chNd)));

    return ndUris;
  }
  
  private static boolean checkDefaultNamespaceUri(Node nd) {
    boolean defaultNamespaceValid = false;
    Node defaultNamespaceNode = nd.getAttributes().getNamedItem("xmlns");
    if (defaultNamespaceNode!=null) {
      String defaultNamespace = defaultNamespaceNode.getNodeValue();
      defaultNamespaceValid = Namespaces.NSMAP.containsKey(defaultNamespace);
    }
    return defaultNamespaceValid;
  }
}
