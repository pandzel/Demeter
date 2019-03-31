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
package com.panforge.demeter.core.utils.nodeiter;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Node iterable root interface.
 */
public interface NodeIterable extends Iterable<Node> {
  
  /**
   * Creates stream of nodes.
   * @param nodeList list of nodes
   * @return stream of nodes
   */
  static Stream<Node> stream(NodeList nodeList) {
    return StreamSupport.stream(nodes(nodeList).spliterator(), false);
  }
  
  /**
   * Creates stream of nodes.
   * @param nodeList list of nodes
   * @return stream of nodes
   */
  static Stream<Node> stream(NamedNodeMap nodeList) {
    return StreamSupport.stream(nodes(nodeList).spliterator(), false);
  }
  
  /**
   * Creates iterable of nodes.
   * @param nodeList list of nodes
   * @return iterable of nodes
   */
  static NodeIterable nodes(NodeList nodeList) {
    return new NodeListIterable(nodeList);
  }
  
  /**
   * Creates iterable of nodes.
   * @param nodeList list of nodes
   * @return iterable of nodes
   */
  static NodeIterable nodes(NamedNodeMap nodeList) {
    return new NamedNodeMapIterable(nodeList);
  }
  
}
