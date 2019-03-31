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

import java.util.Iterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * NodeList iterable.
 */
class NodeListIterable implements NodeIterable {
  private final NodeList nodeList;

  /**
   * Creates instance of the iterable.
   * @param nodeList list of nodes
   */
  public NodeListIterable(NodeList nodeList) {
    this.nodeList = nodeList;
  }

  @Override
  public Iterator<Node> iterator() {
    return new NodeListIterator();
  }
  
  private class NodeListIterator implements Iterator<Node> {
    private int index = 0;

    @Override
    public boolean hasNext() {
      return nodeList!=null && index < nodeList.getLength();
    }

    @Override
    public Node next() {
      return nodeList.item(index++);
    }
    
  }
}
