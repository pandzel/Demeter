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
package com.panforge.demeter.core.utils.builder;

import com.panforge.demeter.core.utils.XmlUtils;
import com.panforge.demeter.core.utils.namespace.NamespaceUtils;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Document node.
 */
public class DocNode {
  
  protected final Node element;

  /**
   * Creates document node.
   * @param element element
   */
  public DocNode(Node element) {
    this.element = element;
  }

  /**
   * Indicates building of a node is complete.
   * @return parent node
   */
  public DocNode done() {
    return new DocNode(parent());
  }

  /**
   * Adds named child.
   * @param name name of the child
   * @return child node
   */
  public DocNode child(String name) {
    return child(name, DocBuilder.TRUE);
  }

  /**
   * Adds named child if condition is fulfilled.
   * @param name name of the child
   * @param cond condiction expression
   * @return child node
   */
  public DocNode child(String name, BooleanSupplier cond) {
    if (StringUtils.isBlank(name) || !cond.getAsBoolean()) {
      return new PhonyNode(doc(), element, doc().createElement("N" + UUID.randomUUID().toString().replaceAll("[^\\p{Alnum}]", "")));
    }
    Node child = doc().createElementNS("http://www.openarchives.org/OAI/2.0/", name);
    element.appendChild(child);
    return new DocNode(child);
  }

  /**
   * Adds named child with content supplier function.
   * @param <T> type of the data
   * @param arg data
   * @param contentSupplier content supplier
   * @return child node
   */
  public <T> DocNode child(T arg, BiConsumer<DocNode, T> contentSupplier) {
    return child(arg, contentSupplier, DocBuilder.TRUE);
  }

  /**
   * Adds named child with content supplier function if condition is fulfilled.
   * @param <T> type of the data
   * @param arg data
   * @param contentSupplier content supplier
   * @param cond condition expression
   * @return child node
   */
  public <T> DocNode child(T arg, BiConsumer<DocNode, T> contentSupplier, BooleanSupplier cond) {
    if (arg == null || contentSupplier == null || !cond.getAsBoolean()) {
      return new PhonyNode(doc(), element, doc().createElement("N" + UUID.randomUUID().toString().replaceAll("[^\\p{Alnum}]", "")));
    }
    contentSupplier.accept(this, arg);
    return this;
  }

  /**
   * Adds textual value to the node.
   * @param text the value
   * @return current node
   */
  public DocNode value(String text) {
    return value(() -> text);
  }

  /**
   * Adds textual value to the node.
   * @param supplier the value supplier
   * @return current node
   */
  public DocNode value(Supplier<String> supplier) {
    if (supplier != null) {
      String value = supplier.get();
      if (value != null) {
        Text textNode = doc().createTextNode(value);
        element.appendChild(textNode);
      }
    }
    return this;
  }

  /**
   * Adds attribute.
   * @param name attribute name
   * @param value attribute value
   * @return current node
   */
  public DocNode attr(String name, String value) {
    return attr(name, value, DocBuilder.TRUE);
  }

  /**
   * Adds attribute if condition is fulfilled.
   * @param name attribute name
   * @param value attribute value
   * @param cond condition expression
   * @return current node
   */
  public DocNode attr(String name, String value, BooleanSupplier cond) {
    return attr(name, () -> value, cond);
  }

  /**
   * Adds attribute.
   * @param name attribute name
   * @param supplier attribute value supplier
   * @return current node
   */
  public DocNode attr(String name, Supplier<String> supplier) {
    return attr(name, supplier, DocBuilder.TRUE);
  }

  /**
   * Adds attribute if condition is fulfilled.
   * @param name attribute name
   * @param supplier attribute value supplier
   * @param cond condition expression
   * @return current node
   */
  public DocNode attr(String name, Supplier<String> supplier, BooleanSupplier cond) {
    if (!StringUtils.isBlank(name) && cond.getAsBoolean() && supplier != null) {
      Attr attrNode = doc().createAttribute(name);
      attrNode.setValue(StringUtils.defaultIfBlank(supplier.get(), ""));
      ((Element) element).setAttributeNode(attrNode);
    }
    return this;
  }

  /**
   * Process stream of data.
   * @param <T> type of data
   * @param stream stream of data
   * @param consumer single data information processing function
   * @return current node
   */
  public <T> DocNode forEach(Stream<T> stream, BiConsumer<DocNode, T> consumer) {
    if (stream != null) {
      stream.forEach((data) -> consumer.accept(this, data));
    }
    return this;
  }

  /**
   * Adds an entire document.
   * @param doc document to add
   * @return current node
   */
  public DocNode addDocument(Document doc) {
    if (doc.getFirstChild() != null) {
      Node docElement = doc.getDocumentElement();
      NamespaceUtils.sanitize(docElement);
      element.appendChild(doc().adoptNode(docElement));
    }
    return this;
  }

  /**
   * Ends building document.
   * @return document as XML string
   */
  public String end() {
    return XmlUtils.formatToString(doc());
  }

  /**
   * Gets current document.
   * @return document
   */
  protected Document doc() {
    return element.getOwnerDocument();
  }

  /**
   * Gets parent node.
   * @return parent node
   */
  protected Node parent() {
    return element.getParentNode();
  }
  
}
