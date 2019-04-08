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

import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.utils.nodeiter.NodeIterable;
import com.panforge.demeter.core.utils.parser.SimpleNamespaceContext;
import com.panforge.demeter.server.MetaDescriptor;
import com.panforge.demeter.server.MetaProcessor;
import java.io.File;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * OAI-DC processor bean
 */
@Service
public class OaiDcProcessorBean implements MetaProcessor {
  private static final Logger LOG = LoggerFactory.getLogger(OaiDcProcessorBean.class);

  private final static XPathFactory xPathFactory = XPathFactory.newInstance();
  private final static XPath xPath = xPathFactory.newXPath();

  static {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    xPath.setNamespaceContext(new SimpleNamespaceContext()
            .add("oai", "http://www.openarchives.org/OAI/2.0/")
            .add("dc", "http://purl.org/dc/elements/1.1/")
            .add("dct", "http://purl.org/dc/terms/")
            .add("dcmiBox", "http://dublincore.org/documents/2000/07/11/dcmi-box/")
            .add("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
            .add("ows", "http://www.opengis.net/ows")
    );
  }

  private final static MetadataFormat oai_dc = new MetadataFormat(
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
    return oai_dc;
  }

  @Override
  public boolean interrogate(File file, Document doc) {
    try {
      return (Boolean) xPath.evaluate("count(//dc:identifier)>0", doc, XPathConstants.BOOLEAN);
    } catch (XPathExpressionException ex) {
      return false;
    }
  }

  @Override
  public MetaDescriptor descriptor(File file, Document doc) {
    try {
      return new MetaDescriptor(this, file, URI.create((String) xPath.evaluate("//dc:identifier", doc, XPathConstants.STRING)), oai_dc, OffsetDateTime.MAX);
    } catch (XPathExpressionException ex) {
      return null;
    }
  }

  @Override
  public Document adopt(File file, Document doc) {
    try {
      // get parent node for identifier and list all its children
      Node descNode = (Node)xPath.evaluate("//dc:identifier", doc, XPathConstants.NODE);
      NodeList dcNodes = (NodeList)xPath.evaluate("*", descNode.getParentNode(), XPathConstants.NODESET);
      
      // collect all namespaces and prefixes
      HashMap<String,String> ndUris = new HashMap<>();
      NodeIterable.stream(dcNodes).forEach(nd->{
        ndUris.putAll(collectNamespaces(nd));
      });
      ndUris.put("oai_dc", "http://www.openarchives.org/OAI/2.0/");
      
      // create new document
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.newDocument();
      
      // create top-level element
      Element oaiDc = document.createElementNS("http://www.openarchives.org/OAI/2.0/", "dc");
      ndUris.entrySet().forEach(e->{
        oaiDc.setAttribute("xmlns:"+e.getKey(), e.getValue());
      });
      document.appendChild(oaiDc);

      // addopt all relevant nodes from the input document to the new document
      NodeIterable.stream(dcNodes).forEach(nd->{
        nd = document.adoptNode(nd);
        oaiDc.appendChild(nd);
      });
      
      return document;
      
    } catch (ParserConfigurationException|DOMException|XPathExpressionException ex) {
      return null;
    }
  }

  private Map<String,String> collectNamespaces(Node nd) {
    HashMap<String,String> ndUris = new HashMap<>();
    ndUris.put(nd.getPrefix(), nd.getNamespaceURI());
    
    NodeIterable.stream(nd.getChildNodes()).forEach(chNd->ndUris.putAll(collectNamespaces(chNd)));
    
    return ndUris;
  }
}
