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

import com.panforge.demeter.core.utils.nodeiter.NodeIterable;
import java.util.function.UnaryOperator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * XML traverser.
 */
class XmlTraverser {
  
  /**
   * Traverses document.
   * @param doc document
   */
  public void traverse(Document doc, UnaryOperator<Node> nodeFun) {
    Element topElement = doc.getDocumentElement();
    NodeIterable.stream(topElement.getChildNodes()).forEach(nd->{ traverse(nd, nodeFun); });
  }
  
  /**
   * Traverses document with default xmlns:xsi substitution.
   * @param doc document
   */
  public void traverse(Document doc) {
    traverse(doc, node->{
      if (node instanceof Element) {
        Element element = (Element)node;
        Node xmlnsXsiNode = element.getAttributes().getNamedItem("xmlns:xsi");
        if (xmlnsXsiNode!=null) {
          element.getAttributes().removeNamedItem("xmlns:xsi");
          element.setAttribute("xmlns:__xsi__", "http://www.w3.org/2001/XMLSchema-instance");
        }
      }
      return node;
    });
  }
  
  /**
   * Updates document.
   * @param xml xml document
   * @return updated xml document
   */
  public String update(String xml) {
    return xml.replaceAll("xmlns:__xsi__", "xmlns:xsi");
  }
  
  private void traverse(Node node, UnaryOperator<Node> nodeFun) {
    nodeFun.apply(node);
    NodeIterable.stream(node.getChildNodes()).forEach(nd->{ traverse(nd, nodeFun); });
  }
  
}
