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

import com.panforge.demeter.core.api.Config.Deletion;
import com.panforge.demeter.core.api.Config.Compression;
import com.panforge.demeter.core.model.request.IdentifyRequest;
import com.panforge.demeter.core.model.response.IdentifyResponse;
import static com.panforge.demeter.core.utils.DateTimeUtils.parseTimestamp;
import static com.panforge.demeter.core.utils.nodeiter.NodeIterable.nodes;
import static com.panforge.demeter.core.utils.nodeiter.NodeIterable.stream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import javax.xml.xpath.XPathConstants;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Identify request parser.
 */
class IdentifyParser extends DocParser {

  /**
   * Creates instance of the parser.
   * @param doc the document
   */
  public IdentifyParser(Document doc) {
    super(doc);
  }

  @Override
  public IdentifyResponse parse() {
    IdentifyRequest request = new IdentifyRequest();

    String repositoryName = (String) evaluate("//oai:OAI-PMH/oai:Identify/oai:repositoryName", doc, XPathConstants.STRING);
    String baseURL = (String) evaluate("//oai:OAI-PMH/oai:Identify/oai:baseURL", doc, XPathConstants.STRING);
    String protocolVersion = (String) evaluate("//oai:OAI-PMH/oai:Identify/oai:protocolVersion", doc, XPathConstants.STRING);
    String[] edminEmails = readAdminEmails();
    OffsetDateTime earliestDatestamp = !StringUtils.isBlank((String)evaluate("//oai:OAI-PMH/oai:Identify/oai:earliestDatestamp", doc, XPathConstants.STRING))? parseTimestamp((String) evaluate("//oai:OAI-PMH/oai:Identify/oai:earliestDatestamp", doc, XPathConstants.STRING)): null;
    Deletion deletedRecord = Deletion.parse((String) evaluate("//oai:OAI-PMH/oai:Identify/oai:deletedRecord", doc, XPathConstants.STRING));
    String granularity = (String) evaluate("//oai:OAI-PMH/oai:Identify/oai:granularity", doc, XPathConstants.STRING);
    Compression [] compression = readCompression();

    Document[] descriptions = readDescriptions((NodeList) evaluate("//oai:OAI-PMH/oai:Identify/oai:description", doc, XPathConstants.NODESET));

    return new IdentifyResponse(repositoryName, baseURL, protocolVersion, edminEmails, earliestDatestamp, deletedRecord, granularity, compression, descriptions, readResponseDate(doc), request);
  }

  private Compression [] readCompression() {
    ArrayList<Compression> compression = new ArrayList<>();
    for (Node node : nodes((NodeList) evaluate("//oai:OAI-PMH/oai:Identify/oai:compression", doc, XPathConstants.NODESET))) {
      Compression c = Compression.parse(node.getTextContent());
      compression.add(c);
    }
    return compression.toArray(new Compression[compression.size()]);
  }
  
  private String[] readAdminEmails() {
    ArrayList<String> adminEmails = new ArrayList<>();
    for (Node node : nodes((NodeList) evaluate("//oai:OAI-PMH/oai:Identify/oai:adminEmail", doc, XPathConstants.NODESET))) {
      adminEmails.add(node.getTextContent());
    }
    return adminEmails.toArray(new String[adminEmails.size()]);
  }

  private Document[] readDescriptions(NodeList nodeList) {
    ArrayList<Document> descriptions = new ArrayList<>();
    for (Node node : nodes(nodeList)) {
      Node descriptionNode = stream(node.getChildNodes()).filter(n -> n.getNodeType() == 1).findFirst().orElse(null);
      if (descriptionNode != null) {
        Document descDoc = builder.newDocument();
        Node adopted = descDoc.adoptNode(descriptionNode);
        descDoc.appendChild(adopted);
        descriptions.add(descDoc);
      }
    }
    return descriptions.toArray(new Document[descriptions.size()]);
  }
}
