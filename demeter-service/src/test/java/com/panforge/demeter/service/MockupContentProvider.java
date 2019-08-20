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
package com.panforge.demeter.service;

import com.panforge.demeter.core.api.exception.CannotDisseminateFormatException;
import com.panforge.demeter.core.api.exception.IdDoesNotExistException;
import com.panforge.demeter.core.api.exception.NoMetadataFormatsException;
import com.panforge.demeter.core.api.exception.NoRecordsMatchException;
import com.panforge.demeter.core.api.exception.NoSetHierarchyException;
import com.panforge.demeter.core.content.ContentProvider;
import com.panforge.demeter.core.content.Filter;
import com.panforge.demeter.core.content.Page;
import com.panforge.demeter.core.content.PageCursor;
import com.panforge.demeter.core.content.StreamingIterable;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.response.elements.Set;
import com.panforge.demeter.core.utils.XmlUtils;
import com.panforge.demeter.core.utils.nodeiter.NodeIterable;
import com.panforge.demeter.core.utils.SimpleNamespaceContext;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Mockup content provider.
 */
public class MockupContentProvider implements ContentProvider<MockupPageCursor> {

  private final static XPathFactory XPATH_FACTORY = XPathFactory.newInstance();
  private final static XPath XPATH = XPATH_FACTORY.newXPath();

  static {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    XPATH.setNamespaceContext(new SimpleNamespaceContext()
            .add("oai", "http://www.openarchives.org/OAI/2.0/")
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

  private final static Set MAIN_SET = new Set("main", "Main set", null);
  
  private final List<MetaDescriptor> descriptors = new ArrayList<>();
  
  ContentProvider<MockupPageCursor> initialize() throws URISyntaxException {
    File root = new File(Thread.currentThread().getContextClassLoader().getResource("content").toURI());
    File[] files = root.listFiles();
    Stream.of(files).map(file -> descriptor(file, parseToXml(file))).forEach(desc->descriptors.add(desc));
    return this;
  }

  @Override
  public StreamingIterable<MetadataFormat> listMetadataFormats(URI identifier) throws IdDoesNotExistException, NoMetadataFormatsException {
    return Page.of(Arrays.asList(new MetadataFormat[]{OAI_DC}));
  }

  @Override
  public Page<Set,MockupPageCursor> listSets(MockupPageCursor pageCursor, int pageSize) throws NoSetHierarchyException {
    return Page.of(Arrays.asList(new Set[]{MAIN_SET}));
  }

  @Override
  public Page<Header,MockupPageCursor> listHeaders(Filter filter, MockupPageCursor pageCursor, int pageSize) throws CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
    MockupPageCursor nextPageCursor = pageCursor==null? pageSize < descriptors.size()? new MockupPageCursor(pageSize): null: pageCursor.cursor() + pageSize < descriptors.size()? new MockupPageCursor(pageCursor.cursor() + pageSize): null;
    Page<Header,MockupPageCursor> page = Page.of(descriptors.stream().skip(pageCursor!=null? pageCursor.cursor(): 0).limit(pageSize).map(MetaDescriptor::toHeader), descriptors.size(), nextPageCursor);
    return page;
  }

  @Override
  public Record readRecord(URI identifier, String metadataPrefix) throws IdDoesNotExistException, CannotDisseminateFormatException {
    MetaDescriptor selected = descriptors.stream().filter(desc->desc.uri.equals(identifier)).findFirst().orElse(null);
    if (selected==null) {
      throw new IdDoesNotExistException("");
    }
    Document doc = parseToXml(selected.source);
    doc = adopt(selected.source, doc);
    return new Record(selected.toHeader(), doc, null);
  }

  private Map<String,String> collectNamespaces(Node nd) {
    HashMap<String,String> ndUris = new HashMap<>();
    ndUris.put(nd.getPrefix(), nd.getNamespaceURI());
    
    NodeIterable.stream(nd.getChildNodes()).forEach(chNd->ndUris.putAll(collectNamespaces(chNd)));
    
    return ndUris;
  }
  
  private Document adopt(File file, Document doc) {
    try {
      // get parent node for identifier and list all its children
      Node descNode = (Node)XPATH.evaluate("//dc:identifier", doc, XPathConstants.NODE);
      NodeList dcNodes = (NodeList)XPATH.evaluate("*", descNode.getParentNode(), XPathConstants.NODESET);
      
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

  private MetaDescriptor descriptor(File file, Document doc) {
    try {
      return new MetaDescriptor(file, URI.create((String) XPATH.evaluate("//dc:identifier", doc, XPathConstants.STRING)), OAI_DC, OffsetDateTime.MAX);
    } catch (XPathExpressionException ex) {
      return null;
    }
  }

  private Document parseToXml(File file) {
    try {
      return XmlUtils.parseToXml(file);
    } catch (IOException | SAXException ex) {
      return null;
    }
  }

  private static class MetaDescriptor {

    public final File source;
    public final URI uri;
    public final MetadataFormat format;
    public final OffsetDateTime datestamp;

    /**
     * Creates instance of the descriptor.
     *
     * @param source metadata source
     * @param uri metadata URI
     * @param format metadata format
     * @param datestamp datestamp
     */
    public MetaDescriptor(File source, URI uri, MetadataFormat format, OffsetDateTime datestamp) {
      this.source = source;
      this.uri = uri;
      this.format = format;
      this.datestamp = datestamp;
    }

    /**
     * Gets header.
     *
     * @return header.
     */
    public Header toHeader() {
      return new Header(uri, datestamp, new String[]{}, false);
    }
  }
}
