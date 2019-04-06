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

import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.api.exception.BadArgumentException;
import com.panforge.demeter.core.api.exception.BadVerbException;
import com.panforge.demeter.core.model.request.ListIdentifiersRequest;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.ListIdentifiersResponse;
import com.panforge.demeter.core.utils.DateTimeUtils;
import static com.panforge.demeter.core.utils.nodeiter.NodeIterable.nodes;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * ListIdentifiers request parser.
 */
class ListIdentifiersParser extends DocParser {

  /**
   * Creates instance of the parser.
   * @param doc the document
   */
  public ListIdentifiersParser(Document doc) {
    super(doc);
  }

  @Override
  public ListIdentifiersResponse parse() throws BadVerbException, BadArgumentException {
    ArrayList<Header> headers = new ArrayList<>();
    for (Node node : nodes((NodeList) evaluate("//oai:OAI-PMH/oai:ListIdentifiers/oai:header", doc, XPathConstants.NODESET))) {
      headers.add(readHeader(node));
    }
    String requestResumptionToken = readRequestResumptionToken(doc);
    ListIdentifiersRequest request = requestResumptionToken!=null
            ? new ListIdentifiersRequest(requestResumptionToken)
            : extractRequest();
    ResumptionToken resumptionToken = readResponseResumptionToken(doc);
    return new ListIdentifiersResponse(headers.toArray(new Header[headers.size()]), resumptionToken, readResponseDate(doc), request);
  }
  
  private ListIdentifiersRequest extractRequest() throws BadVerbException, BadArgumentException {
    Map<String,String[]> values = new HashMap<>();
    values.put("metadataPrefix", new String[]{ readMetadataPrefix(doc) });
    values.put("from", new String[]{ readFromAsString(doc) });
    values.put("until", new String[]{ readUntilAsString(doc) });
    values.put("set", new String[]{ readSet(doc) });
    return ListIdentifiersRequest.create(values);
  }

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
