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
      
      oaiPmh.setAttribute("xmlns:oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/");
      oaiPmh.setAttribute("xmlns:rights", "http://www.openarchives.org/OAI/2.0/rights/");
      oaiPmh.setAttribute("xmlns:provenance", "http://www.openarchives.org/OAI/2.0/provenance");
      oaiPmh.setAttribute("xmlns:friends", "http://www.openarchives.org/OAI/2.0/friends/");
      oaiPmh.setAttribute("xmlns:branding", "http://www.openarchives.org/OAI/2.0/branding/");
      oaiPmh.setAttribute("xmlns:gateway", "http://www.openarchives.org/OAI/2.0/gateway/");
      oaiPmh.setAttribute("xmlns:eprints", "http://www.openarchives.org/OAI/1.1/eprints");
      oaiPmh.setAttribute("xmlns:rfc1807", "http://info.internet.isi.edu:80/in-notes/rfc/files/rfc1807.txt");
      oaiPmh.setAttribute("xmlns:marc21", "http://www.loc.gov/MARC21/slim");
      oaiPmh.setAttribute("xmlns:oai_marc", "http://www.openarchives.org/OAI/1.1/oai_marc");
      
      oaiPmh.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      oaiPmh.setAttribute("xsi:schemaLocation", 
                  "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd "
                + "http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd"
                + "http://www.openarchives.org/OAI/2.0/rights/ http://www.openarchives.org/OAI/2.0/rightsManifest.xsd "
                + "http://www.openarchives.org/OAI/2.0/provenance http://www.openarchives.org/OAI/2.0/provenance.xsd "
                + "http://www.openarchives.org/OAI/2.0/friends/ http://www.openarchives.org/OAI/2.0/friends.xsd "
                + "http://www.openarchives.org/OAI/2.0/branding/ http://www.openarchives.org/OAI/2.0/branding.xsd "
                + "http://www.openarchives.org/OAI/2.0/gateway/ http://www.openarchives.org/OAI/2.0/gateway.xsd "
                + "http://www.openarchives.org/OAI/1.1/eprints http://www.openarchives.org/OAI/1.1/eprints.xsd "
                + "http://info.internet.isi.edu:80/in-notes/rfc/files/rfc1807.txt http://www.openarchives.org/OAI/1.1/rfc1807.xsd "
                + "http://www.loc.gov/MARC21/slim http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd "
                + "http://www.openarchives.org/OAI/1.1/oai_marc http://www.openarchives.org/OAI/1.1/oai_marc.xsd"
      );

      return new DocNode(oaiPmh);
    } catch (ParserConfigurationException ex) {
      throw new RuntimeException("Error building document.", ex);
    }
  }


}
