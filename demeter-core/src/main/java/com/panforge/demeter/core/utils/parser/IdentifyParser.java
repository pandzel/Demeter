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
import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.model.request.IdentifyRequest;
import com.panforge.demeter.core.model.request.Request;
import com.panforge.demeter.core.model.response.ErrorResponse;
import com.panforge.demeter.core.model.response.IdentifyResponse;
import com.panforge.demeter.core.model.response.Response;
import com.panforge.demeter.core.model.response.guidelines.RepositoryDescription;
import static com.panforge.demeter.core.utils.DateTimeUtils.parseTimestamp;
import static com.panforge.demeter.core.utils.nodeiter.NodeIterable.nodes;
import static com.panforge.demeter.core.utils.nodeiter.NodeIterable.stream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import org.apache.commons.lang3.ArrayUtils;
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
  public Response<? extends Request> parse() {
    ErrorInfo[] errors = readErrors(doc);
    if (!ArrayUtils.isEmpty(errors)) {
      return new ErrorResponse(extractRequest(), readResponseDate(doc), errors);
    }
    
    IdentifyRequest request = new IdentifyRequest();

    String repositoryName = (String) evaluate("//oai:OAI-PMH/oai:Identify/oai:repositoryName", doc, XPathConstants.STRING);
    String baseURL = (String) evaluate("//oai:OAI-PMH/oai:Identify/oai:baseURL", doc, XPathConstants.STRING);
    String protocolVersion = (String) evaluate("//oai:OAI-PMH/oai:Identify/oai:protocolVersion", doc, XPathConstants.STRING);
    String[] edminEmails = readAdminEmails();
    OffsetDateTime earliestDatestamp = !StringUtils.isBlank((String)evaluate("//oai:OAI-PMH/oai:Identify/oai:earliestDatestamp", doc, XPathConstants.STRING))? parseTimestamp((String) evaluate("//oai:OAI-PMH/oai:Identify/oai:earliestDatestamp", doc, XPathConstants.STRING)): null;
    Deletion deletedRecord = Deletion.parse((String) evaluate("//oai:OAI-PMH/oai:Identify/oai:deletedRecord", doc, XPathConstants.STRING));
    String granularity = (String) evaluate("//oai:OAI-PMH/oai:Identify/oai:granularity", doc, XPathConstants.STRING);
    Compression [] compression = readCompression();

    RepositoryDescription[] descriptions = readDescriptions((NodeList) evaluate("//oai:OAI-PMH/oai:Identify/oai:description", doc, XPathConstants.NODESET));

    return new IdentifyResponse(extractRequest(), readResponseDate(doc), repositoryName, baseURL, protocolVersion, edminEmails, earliestDatestamp, deletedRecord, granularity, compression, descriptions);
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

  private RepositoryDescription[] readDescriptions(NodeList nodeList) {
    ArrayList<RepositoryDescription> descriptions = new ArrayList<>();
    for (Node node : nodes(nodeList)) {
      Node descriptionNode = stream(node.getChildNodes()).filter(n -> n.getNodeType() == 1).findFirst().orElse(null);
      if (descriptionNode != null) {
        Document descDoc = BUILDER.newDocument();
        Node adopted = descDoc.adoptNode(descriptionNode);
        descDoc.appendChild(adopted);
        RepositoryDescription description = createDescription(doc);
        if (description!=null) {
          descriptions.add(description);
        }
      }
    }
    return descriptions.toArray(new RepositoryDescription[descriptions.size()]);
  }
  
  private RepositoryDescription createDescription(Document doc) {
    // TODO: create description from document
    return null;
  }
  
  private Map<String,String[]> extractRequest() {
    Map<String,String[]> values = new HashMap<>();
    values.put("verb", new String[] { Verb.Identify.name() });
    return values;
  }
}
