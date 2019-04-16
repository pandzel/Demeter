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
import com.panforge.demeter.core.model.request.ListSetsRequest;
import com.panforge.demeter.core.model.response.ListSetsResponse;
import com.panforge.demeter.core.model.response.elements.Set;
import static com.panforge.demeter.core.utils.nodeiter.NodeIterable.nodes;
import java.util.ArrayList;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * ListSets request parser.
 */
class ListSetsParser extends DocParser {

  /**
   * Creates instance of the parser.
   * @param doc the document
   */
  public ListSetsParser(Document doc) {
    super(doc);
  }

  @Override
  public ListSetsResponse parse() {
    ArrayList<Set> sets = new ArrayList<>();
    for (Node node : nodes((NodeList)evaluate("//oai:OAI-PMH/oai:ListSets/oai:set", doc, XPathConstants.NODESET))) {
      sets.add(readSet(node));
    }
    String rt = readRequestResumptionToken(doc);
    ListSetsRequest request = rt!=null? ListSetsRequest.resume(readRequestResumptionToken(doc)): new ListSetsRequest();
    ResumptionToken resumptionToken = readResponseResumptionToken(doc);
    return new ListSetsResponse(sets.toArray(new Set[sets.size()]), resumptionToken, readResponseDate(doc), readErrors(doc), request.getParameters());
  }

  private Set readSet(Node node) {
    return new Set(
            (String) evaluate("oai:setSpec", node, XPathConstants.STRING),
            (String) evaluate("oai:setName", node, XPathConstants.STRING)
    );
  }

}
