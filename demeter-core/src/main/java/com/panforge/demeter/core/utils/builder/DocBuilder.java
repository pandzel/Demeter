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

import java.util.function.BooleanSupplier;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Document builder.
 */
public class DocBuilder {

  public static final BooleanSupplier FALSE = () -> false;
  public static final BooleanSupplier TRUE = () -> true;

  /**
   * Begins building the document node.
   * @return document node
   */
  public DocNode begin() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.newDocument();

      Element oaiPmh = document.createElementNS("http://www.openarchives.org/OAI/2.0/", "OAI-PMH");
      document.appendChild(oaiPmh);

      oaiPmh.setAttribute("xmlns", "http://www.openarchives.org/OAI/2.0/");
      oaiPmh.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      oaiPmh.setAttribute("xsi:schemaLocation", "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");

      return new DocNode(oaiPmh);
    } catch (ParserConfigurationException ex) {
      throw new RuntimeException("Error building document.", ex);
    }
  }


}
