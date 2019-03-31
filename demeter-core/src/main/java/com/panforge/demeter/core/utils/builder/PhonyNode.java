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

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Phony node.
 */
class PhonyNode extends DocNode {
  
  final Document document;
  final Node parent;

  public PhonyNode(Document document, Node parent, Node element) {
    super(element);
    this.document = document;
    this.parent = parent;
  }

  @Override
  public DocNode addDocument(Document doc) {
    return this;
  }

  @Override
  public DocNode attr(String name, Supplier<String> supplier, BooleanSupplier cond) {
    return this;
  }

  @Override
  public DocNode attr(String name, Supplier<String> supplier) {
    return this;
  }

  @Override
  public DocNode attr(String name, String value, BooleanSupplier cond) {
    return this;
  }

  @Override
  public DocNode attr(String name, String value) {
    return this;
  }

  @Override
  public <T> DocNode child(T arg, BiConsumer<DocNode, T> fun, BooleanSupplier cond) {
    return new PhonyNode(doc(), element, doc().createElement("N" + UUID.randomUUID().toString().replaceAll("[^\\p{Alnum}]", "")));
  }

  @Override
  public <T> DocNode child(T arg, BiConsumer<DocNode, T> fun) {
    return new PhonyNode(doc(), element, doc().createElement("N" + UUID.randomUUID().toString().replaceAll("[^\\p{Alnum}]", "")));
  }

  @Override
  public DocNode child(String name, BooleanSupplier cond) {
    return new PhonyNode(doc(), element, doc().createElement("N" + UUID.randomUUID().toString().replaceAll("[^\\p{Alnum}]", "")));
  }

  @Override
  public DocNode child(String name) {
    return new PhonyNode(doc(), element, doc().createElement("N" + UUID.randomUUID().toString().replaceAll("[^\\p{Alnum}]", "")));
  }

  @Override
  public <T> DocNode forEach(Stream<T> stream, BiConsumer<DocNode, T> consumer) {
    return this;
  }

  @Override
  public DocNode value(Supplier<String> supplier) {
    return this;
  }

  @Override
  public DocNode value(String text) {
    return this;
  }

  @Override
  protected Document doc() {
    return document;
  }

  @Override
  protected Node parent() {
    return parent;
  }
  
}
