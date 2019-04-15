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
package com.panforge.demeter.core.utils.parser;

import com.panforge.demeter.core.model.ErrorCode;
import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.api.exception.BadVerbException;
import com.panforge.demeter.core.model.request.Request;
import com.panforge.demeter.core.model.response.Response;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.utils.DateTimeUtils;
import com.panforge.demeter.core.utils.nodeiter.NodeIterable;
import static com.panforge.demeter.core.utils.nodeiter.NodeIterable.nodes;
import static com.panforge.demeter.core.utils.nodeiter.NodeIterable.stream;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Document parser.
 */
public class DocParser {
  protected final static XPathFactory xPathFactory = XPathFactory.newInstance();
  protected final static XPath xPath = xPathFactory.newXPath();
  protected final static DocumentBuilder builder;
  static {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      builder = factory.newDocumentBuilder();
      xPath.setNamespaceContext(new SimpleNamespaceContext().add("oai", "http://www.openarchives.org/OAI/2.0/"));
    } catch (ParserConfigurationException ex) {
      throw new RuntimeException("Error creating document builder.", ex);
    }
  }
  
  protected final Document doc;

  /**
   * Creates instance of the parser.
   * @param doc the document
   */
  public DocParser(Document doc) {
    this.doc = doc;
  }
  
  /**
   * Parses XML response document.
   * @return response as an object
   * @throws BadVerbException if parsing fails
   */
  public Response<? extends Request> parse() throws BadVerbException {
    DocParser parser = getInstance(readVerb(doc));
    return parser.parse();
  }

  /**
   * Gets corresponding to the verb parser instance
   * @param verb the verb
   * @return parser instance
   */
  protected final DocParser getInstance(Verb verb) {
    switch (verb) {
      case ListSets:
        return new ListSetsParser(doc);
      case ListRecords:
        return new ListRecordsParser(doc);
      case ListMetadataFormats:
        return new ListMetadataFormatsParser(doc);
      case ListIdentifiers:
        return new ListIdentifiersParser(doc);
      case Identify:
        return new IdentifyParser(doc);
      case GetRecord:
        return new GetRecordParser(doc);
    }
    return null;
  }
  
  /**
   * Reads 'from' property.
   * @param doc the document
   * @return the 'from' property
   */
  protected String readFromAsString(Document doc) {
    String text = StringUtils.trimToNull((String)evaluate("//oai:OAI-PMH/oai:request/@from", doc, XPathConstants.STRING));
    return text;
  }
  
  /**
   * Reads 'from' property.
   * @param doc the document
   * @return the 'from' property
   */
  protected OffsetDateTime readFrom(Document doc) {
    String text = readFromAsString(doc);
    return text!=null? OffsetDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME): null;
  }
  
  /**
   * Reads 'until' property.
   * @param doc the document
   * @return the 'until' property
   */
  protected String readUntilAsString(Document doc) {
    String text = StringUtils.trimToNull((String)evaluate("//oai:OAI-PMH/oai:request/@until", doc, XPathConstants.STRING));
    return text;
  }
  
  /**
   * Reads 'until' property.
   * @param doc the document
   * @return the 'until' property
   */
  protected OffsetDateTime readUntil(Document doc) {
    String text = readUntilAsString(doc);
    return text!=null? OffsetDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME): null;
  }

  /**
   * Reads 'set' property.
   * @param doc the document
   * @return the 'set' property
   */
  protected String readSet(Document doc) {
    String set = StringUtils.trimToNull((String)evaluate("//oai:OAI-PMH/oai:request/@set", doc, XPathConstants.STRING));
    return set;
  }

  /**
   * Reads 'metadataPrefix' property.
   * @param doc the document
   * @return the 'metadataPrefix' property
   */
  protected String readMetadataPrefix(Document doc) {
    String metadataPrefix = StringUtils.trimToNull((String)evaluate("//oai:OAI-PMH/oai:request/@metadataPrefix", doc, XPathConstants.STRING));
    return metadataPrefix;
  }

  protected String readIdentifierAsString(Document doc) {
    String sIdentifier = StringUtils.trimToNull((String)evaluate("//oai:OAI-PMH/oai:request/@identifier", doc, XPathConstants.STRING));
    return sIdentifier;
  }
  
  /**
   * Reads 'identifier' property.
   * @param doc the document
   * @return the 'identifier' property
   */
  protected URI readIdentifier(Document doc) {
    String sIdentifier = readIdentifierAsString(doc);
    return sIdentifier!=null? URI.create(sIdentifier): null;
  }
  
  /**
   * Reads 'error' information.
   * @param doc the document
   * @return the 'error' information
   */
  protected ErrorInfo [] readErrors(Document doc) {
    NodeList ndErrors = (NodeList)evaluate("//oai:OAI-PMH/oai:error", doc, XPathConstants.NODESET);
    if (ndErrors == null || ndErrors.getLength()==0) {
      return null;
    }
    ArrayList<ErrorInfo> infos = new ArrayList<>();
    NodeIterable.stream(ndErrors).forEach(nd->{
      ErrorCode code = ErrorCode.parse((String)evaluate("@code", nd, XPathConstants.STRING));
      String message = (String)evaluate(".", nd, XPathConstants.STRING);
      ErrorInfo info = new ErrorInfo(code, message);
      infos.add(info);
    });
    return infos.toArray(new ErrorInfo[infos.size()]);
  }
  
  /**
   * Reads 'responseDate' property.
   * @param doc the document
   * @return the 'responseDate' property
   */
  protected OffsetDateTime readResponseDate(Document doc) {
    String text = StringUtils.trimToNull((String)evaluate("//oai:OAI-PMH/oai:responseDate", doc, XPathConstants.STRING));
    return text!=null? OffsetDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME): null;
  }
  
  /**
   * Reads 'resumptionToken' property.
   * This property represents resumption token required for the next consecutive request.
   * @param doc the document
   * @return the 'resumptionToken' property
   */
  protected ResumptionToken readResponseResumptionToken(Document doc) {
    Node ndResumptionToken = (Node)evaluate("//oai:resumptionToken", doc, XPathConstants.NODE);
    if (ndResumptionToken==null) {
      return null;
    }

    ResumptionToken resumptionToken = new ResumptionToken(
      (String)evaluate(".", ndResumptionToken, XPathConstants.STRING),
      !StringUtils.isBlank((String)evaluate("@expirationDate", ndResumptionToken, XPathConstants.STRING))? DateTimeUtils.parseTimestamp((String)evaluate("@expirationDate", ndResumptionToken, XPathConstants.STRING)): null,
      NumberUtils.toLong((String)evaluate("@completeListSize", ndResumptionToken, XPathConstants.STRING), 0),
      NumberUtils.toLong((String)evaluate("@cursor", ndResumptionToken, XPathConstants.STRING), 0)
    );

    return resumptionToken;
  }
  
  /**
   * Reads 'resumptionToken' property.<p>
   * This property represents resumption token used to obtain current response.
   * @param doc the document
   * @return the 'resumptionToken' property
   */
  protected String readRequestResumptionToken(Document doc) {
    String resumptionToken = StringUtils.trimToNull((String)evaluate("//oai:OAI-PMH/oai:request/@resumptionToken", doc, XPathConstants.STRING));
    return resumptionToken;
  }
  
  /**
   * Reads 'verb' property.
   * @param doc document
   * @return the 'verb' property
   * @throws BadVerbException if reading verb fails
   */
  protected final Verb readVerb(Document doc) throws BadVerbException {
    String sVerb = (String)evaluate("//oai:OAI-PMH/oai:request/@verb", doc, XPathConstants.STRING);
    Verb verb = Verb.parse(sVerb);
    if (verb==null)
      throw new BadVerbException(String.format("Error reading verb: '%s'", StringUtils.trimToEmpty(sVerb)));
    return verb;
  }
  
  /**
   * Safely evaluates XPath expressions.
   * @param expression XPath expression
   * @param item item to evaluate
   * @param returnType return type
   * @return result
   */
  protected final Object evaluate(String expression, Object item, QName returnType) {
    try {
      return xPath.evaluate(expression, item, returnType);
    } catch (XPathExpressionException ex) {
      throw new RuntimeException(String.format("Invalid XPath expression: '%s'", expression), ex);
    }
  }
  
  /**
   * Reads record.
   * @param node the node
   * @return the record
   */
  protected Record readRecord(Node node) {
    Header header = readHeader((Node)evaluate("oai:header", node, XPathConstants.NODE));

    Document metadata = null;
    Node ndMetadata = (Node)evaluate("oai:metadata", node, XPathConstants.NODE);
    if (ndMetadata!=null) {
      Node metadataDocumentNode = stream(ndMetadata.getChildNodes()).filter(n->n.getNodeType()==1).findFirst().orElse(null);
      if (metadataDocumentNode!=null) {
        metadata = builder.newDocument();
        Node adopted = metadata.adoptNode(metadataDocumentNode);
        metadata.appendChild(adopted);
      }
    }

    Document about = null;
    Node ndAbout = (Node)evaluate("oai:about", node, XPathConstants.NODE);
    if (ndAbout!=null) {
      Node aboutDocumentNode = stream(ndAbout.getChildNodes()).filter(n->n.getNodeType()==1).findFirst().orElse(null);
      if (aboutDocumentNode!=null) {
        about = builder.newDocument();
        Node adopted = about.adoptNode(aboutDocumentNode);
        about.appendChild(adopted);
      }
    }

    return new Record(header, metadata, about);
  }

  /**
   * Reads header.
   * @param node the node
   * @return the header
   */
  private Header readHeader(Node node) {
      return new Header(
              URI.create((String) evaluate("oai:identifier", node, XPathConstants.STRING)),
              DateTimeUtils.parseTimestamp((String) evaluate("oai:datestamp", node, XPathConstants.STRING)),
              parseSet((NodeList) evaluate("oai:setSpec", node, XPathConstants.NODESET)),
              "deleted".equalsIgnoreCase((String)evaluate("@status", node, XPathConstants.STRING))
      );
  }

  private String[] parseSet(NodeList nodeSet) {
    ArrayList<String> sets = new ArrayList<>();
    for (Node node : nodes(nodeSet)) {
      sets.add(node.getTextContent());
    }
    return sets.toArray(new String[sets.size()]);
  }
}
