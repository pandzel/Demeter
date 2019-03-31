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
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XML utilities.
 */
public class XmlUtils {
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
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.parse(data);
    } catch (ParserConfigurationException ex) {
      throw new Error("Error obtaining XML parser.", ex);
    }
  }
  
}
