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

import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.model.request.Request;
import com.panforge.demeter.core.model.response.ErrorResponse;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.ListIdentifiersResponse;
import com.panforge.demeter.core.model.response.Response;
import static com.panforge.demeter.core.utils.nodeiter.NodeIterable.nodes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import org.apache.commons.lang3.ArrayUtils;
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
  public Response<? extends Request> parse() {
    ErrorInfo[] errors = readErrors(doc);
    if (!ArrayUtils.isEmpty(errors)) {
      return new ErrorResponse(readResponseDate(doc), errors, extractRequest());
    }
    
    ArrayList<Header> headers = new ArrayList<>();
    for (Node node : nodes((NodeList) evaluate("//oai:OAI-PMH/oai:ListIdentifiers/oai:header", doc, XPathConstants.NODESET))) {
      headers.add(readHeader(node));
    }
    ResumptionToken resumptionToken = readResponseResumptionToken(doc);
    
    return new ListIdentifiersResponse(headers.toArray(new Header[headers.size()]), resumptionToken, readResponseDate(doc), readErrors(doc), extractRequest());
  }
  
  private Map<String,String[]> extractRequest() {
    Map<String,String[]> values = new HashMap<>();
    values.put("verb", new String[] { Verb.ListIdentifiers.name() });
    values.put("resumptionToken", new String[]{ readRequestResumptionToken(doc) });
    values.put("metadataPrefix", new String[]{ readMetadataPrefix(doc) });
    values.put("from", new String[]{ readFromAsString(doc) });
    values.put("until", new String[]{ readUntilAsString(doc) });
    values.put("set", new String[]{ readSet(doc) });
    return values;
  }

}
