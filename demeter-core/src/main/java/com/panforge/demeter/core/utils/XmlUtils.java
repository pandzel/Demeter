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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XML utilities.
 */
public class XmlUtils {
  private static final DocumentBuilder builder;
  static {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      builder = factory.newDocumentBuilder();
    } catch (ParserConfigurationException ex) {
      throw new FactoryConfigurationError(ex);
    }
  }
  
  /**
   * Creates new document.
   * @return document
   */
  public static Document newDocument() {
    return builder.newDocument();
  }
  
  /**
   * Parses string containing xml into a document.
   * @param xml XML string
   * @return document
   * @throws IOException if error reading document
   * @throws SAXException if error parsing document
   */
  public static Document parseToXml(String xml) throws IOException, SAXException {
    try (InputStream xmlStream = new ByteArrayInputStream(xml.getBytes("UTF-8"))) {
      return parseToXml(xmlStream);
    } catch (UnsupportedEncodingException ex) {
      throw new Error("Error decoding string.", ex);
    }
  }
  
  /**
   * Parses file containing xml into a document.
   * @param file XML file
   * @return document
   * @throws IOException if error reading document
   * @throws SAXException if error parsing document
   */
  public static Document parseToXml(File file) throws IOException, SAXException {
    try (InputStream xmlStream = new FileInputStream(file)) {
      return parseToXml(xmlStream);
    }
  }

  /**
   * Parses stream containing xml into a document.
   * @param xmlStream XML stream
   * @return document
   * @throws IOException if error reading document
   * @throws SAXException if error parsing document
   */
  public static Document parseToXml(InputStream xmlStream) throws IOException, SAXException {
    try (InputStream data = new UnicodeBOMInputStream(xmlStream)) {
      return builder.parse(data);
    }
  }
  
  /**
   * Formats document to string.
   * @param document document
   * @return string representation of the document
   */
  public static String formatToString(Document document) {
    try {
      XmlTraverser traverser = new XmlTraverser();
      
      traverser.traverse(document);
      
      TransformerFactory factory = TransformerFactory.newInstance();
      factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            
      Transformer transformer = factory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      Writer out = new StringWriter();
      transformer.transform(new DOMSource(document), new StreamResult(out));
      
      String xml = out.toString();
      
      xml = traverser.update(xml);
      
      return xml;
    } catch (TransformerException ex) {
      throw new TransformerFactoryConfigurationError(ex);
    }
  }
}
