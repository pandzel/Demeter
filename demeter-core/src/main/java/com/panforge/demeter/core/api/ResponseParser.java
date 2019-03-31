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
package com.panforge.demeter.core.api;

import com.panforge.demeter.core.api.exception.BadArgumentException;
import com.panforge.demeter.core.api.exception.BadVerbException;
import com.panforge.demeter.core.model.response.Response;
import com.panforge.demeter.core.utils.XmlUtils;
import com.panforge.demeter.core.utils.parser.DocParser;
import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Response parser.
 * <p>
 * Converts XML response string into response object.
 */
public class ResponseParser {

  /**
   * Parses response.
   *
   * @param xml XML response
   * @return response response object.
   * @throws BadVerbException if parsing fails
   * @throws BadArgumentException if parsing fails
   */
  public Response parse(String xml) throws BadArgumentException, BadVerbException {
    Document doc = parseToXml(xml);
    DocParser docParser = new DocParser(doc);
    return docParser.parse();
  }

  /**
   * Parses response.
   *
   * @param xmlStream content stream
   * @return response response object.
   * @throws BadVerbException if parsing fails
   * @throws BadArgumentException if parsing fails
   */
  public Response parse(InputStream xmlStream) throws BadArgumentException, BadVerbException {
    Document doc = parseToXml(xmlStream);
    DocParser docParser = new DocParser(doc);
    return docParser.parse();
  }

  private Document parseToXml(String xml) throws BadArgumentException {
    try {
      return XmlUtils.parseToXml(xml);
    } catch (IOException | SAXException ex) {
      throw new BadArgumentException(String.format("Error parsing xml: %s", xml), ex);
    }
  }

  private Document parseToXml(InputStream xmlStream) throws BadArgumentException {
    try {
      return XmlUtils.parseToXml(xmlStream);
    } catch (IOException | SAXException ex) {
      throw new BadArgumentException(String.format("Error parsing xml"), ex);
    }
  }
}
