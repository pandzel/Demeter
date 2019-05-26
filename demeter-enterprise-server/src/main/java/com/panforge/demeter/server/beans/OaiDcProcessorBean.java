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
package com.panforge.demeter.server.beans;

import com.datastax.oss.driver.api.core.cql.Row;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.utils.SimpleNamespaceContext;
import com.panforge.demeter.core.utils.XmlUtils;
import com.panforge.demeter.core.utils.namespace.NamespaceUtils;
import com.panforge.demeter.server.MetaProcessor;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * OAI-DC processor bean
 */
@Service
public class OaiDcProcessorBean implements MetaProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(OaiDcProcessorBean.class);

  private final static XPathFactory XPATH_FACTORY = XPathFactory.newInstance();
  private final static XPath XPATH = XPATH_FACTORY.newXPath();

  static {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    XPATH.setNamespaceContext(new SimpleNamespaceContext()
            .add("oai_dc", "http://www.openarchives.org/OAI/2.0/")
            .add("dc", "http://purl.org/dc/elements/1.1/")
            .add("dct", "http://purl.org/dc/terms/")
            .add("dcmiBox", "http://dublincore.org/documents/2000/07/11/dcmi-box/")
            .add("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
            .add("ows", "http://www.opengis.net/ows")
    );
  }

  private final static MetadataFormat OAI_DC = new MetadataFormat(
          "oai_dc",
          "http://www.openarchives.org/OAI/2.0/oai_dc.xsd",
          "http://www.openarchives.org/OAI/2.0/oai_dc/"
  );

  @PostConstruct
  public void construct() {
    LOG.info(String.format("%s created.", this.getClass().getSimpleName()));
  }

  @PreDestroy
  public void destroy() {
    LOG.info(String.format("%s destroyed.", this.getClass().getSimpleName()));
  }

  @Override
  public MetadataFormat format() {
    return OAI_DC;
  }
  
  @Override
  public boolean interrogate(Row row) {
    return !StringUtils.isBlank(row.getString("identifier")) && !StringUtils.isBlank(row.getString("title"));
  }

  @Override
  public Document adopt(Row row) {

    // create new document
    final Document document = XmlUtils.newDocument();

    // create document element
    final Element oaiDc = document.createElement("oai_dc:dc");
    oaiDc.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    oaiDc.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
    oaiDc.setAttribute("xmlns:oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/");
    oaiDc.setAttribute("xsi:schemaLocation", "http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
    document.appendChild(oaiDc);
    
    row.getColumnDefinitions().forEach(cd->{
      String name = cd.getName().asCql(true);
      if (!name.endsWith("id")) {
        Object value = row.getObject(name);
        if (value!=null) {
          Element dcElement = document.createElementNS("http://purl.org/dc/elements/1.1/", name);
          dcElement.setPrefix("dc");
          dcElement.setTextContent(value.toString());
          oaiDc.appendChild(dcElement);
        }
      }
    });

    // remove nodes which are not withing legitimate namespaces
    NamespaceUtils.sanitize(document.getDocumentElement());      
    
    return document;
  }
}
