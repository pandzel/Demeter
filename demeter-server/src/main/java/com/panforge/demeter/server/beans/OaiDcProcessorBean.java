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
import com.panforge.demeter.core.utils.SimpleNamespaceContext;
import com.panforge.demeter.core.utils.XmlUtils;
import com.panforge.demeter.server.MetaDescriptor;
import com.panforge.demeter.server.MetaProcessor;
import com.panforge.demeter.core.utils.namespace.NamespaceUtils;
import com.panforge.demeter.core.utils.namespace.Namespaces;
import java.io.File;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.parsers.DocumentBuilderFactory;
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
  public boolean interrogate(File file, Document doc) {
    try {
      return (Boolean) XPATH.evaluate("count(//dc:identifier)>0", doc, XPathConstants.BOOLEAN);
    } catch (XPathExpressionException ex) {
      throw new Error("Error interrogating file.", ex);
    }
  }

  @Override
  public MetaDescriptor descriptor(File file, Document doc) {
    try {
      OffsetDateTime fileTimestamp = OffsetDateTime.ofInstant(new Date(file.lastModified()).toInstant(), ZoneId.systemDefault());
      return new MetaDescriptor(this, file, URI.create((String) XPATH.evaluate("//dc:identifier", doc, XPathConstants.STRING)), OAI_DC, fileTimestamp);
    } catch (XPathExpressionException ex) {
      throw new Error("Error reading metadata descriptor.", ex);
    }
  }

  @Override
  public Document adopt(File file, Document doc) {
    try {
      // get 'identifier' node; it must be one because 'interrogate()' has already deceted it
      Node descNode = (Node)XPATH.evaluate("//dc:identifier", doc, XPathConstants.NODE);
      if (descNode==null) {
        throw new IllegalStateException(String.format("Expected identifier missing."));
      }
      
      // get all child nodes of the parent of the 'identifier' node (including 'identifier' node)
      NodeList dcNodes = (NodeList)XPATH.evaluate("*", descNode.getParentNode(), XPathConstants.NODESET);
      
      // collect all legitimate namespaces for each sibling node of 'identifier' node; include OAI_DC
      Map<String, String> ndUris = NamespaceUtils.collectNamespaces(NodeIterable.stream(dcNodes))
              .entrySet().stream()
              .filter(e->Namespaces.NSMAP.containsKey(e.getValue()))
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      ndUris.put(OAI_DC.metadataPrefix, OAI_DC.metadataNamespace);
      
      
      // find all schema locations for the legitimate URIs
      String schemaLocation = NamespaceUtils.generateSchemaLocation(ndUris.values());
      
      // create new document
      Document document = XmlUtils.newDocument();
      
      // create document element
      Element oaiDc = document.createElement(String.format("%s:dc", OAI_DC.metadataPrefix));
      ndUris.entrySet().forEach(e->oaiDc.setAttribute("xmlns:"+e.getKey(), e.getValue()));
      oaiDc.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      oaiDc.setAttribute("xsi:schemaLocation", schemaLocation);
      document.appendChild(oaiDc);

      // adopt each node from the input document to the new document
      NodeIterable.stream(dcNodes).forEach(nd->{
        nd = document.adoptNode(nd);
        oaiDc.appendChild(nd);
      });
      
      // remove nodes which are not withing legitimate namespaces
      NamespaceUtils.sanitize(document.getDocumentElement());      
      
      return document;
      
    } catch (DOMException ex) {
      // ignore
      return null;
    } catch (XPathExpressionException ex) {
      throw new Error("Error adopting node.", ex);
    }
  }
}
