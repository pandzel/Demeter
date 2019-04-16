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
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.model.request.ListMetadataFormatsRequest;
import com.panforge.demeter.core.model.request.Request;
import com.panforge.demeter.core.model.response.ErrorResponse;
import com.panforge.demeter.core.model.response.ListMetadataFormatsResponse;
import com.panforge.demeter.core.model.response.Response;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
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
 * ListMetadataFormats request parser.
 */
class ListMetadataFormatsParser extends DocParser {
  
  /**
   * Creates instance of the parser.
   * @param doc the document
   */
  public ListMetadataFormatsParser(Document doc) {
    super(doc);
  }

  @Override
  public Response<? extends Request> parse() {
    ErrorInfo[] errors = readErrors(doc);
    if (!ArrayUtils.isEmpty(errors)) {
      return new ErrorResponse(readResponseDate(doc), errors, extractRequest());
    }
    
    ArrayList<MetadataFormat> formats = new ArrayList<>();
    for (Node node : nodes((NodeList) evaluate("//oai:OAI-PMH/oai:ListMetadataFormats/oai:metadataFormat", doc, XPathConstants.NODESET))) {
      formats.add(readMetadataFormat(node));
    }
    
    return new ListMetadataFormatsResponse(formats.toArray(new MetadataFormat[formats.size()]), readResponseDate(doc), readErrors(doc), extractRequest());
  }
  
  private MetadataFormat readMetadataFormat(Node node) {
    return new MetadataFormat(
            (String) evaluate("oai:metadataPrefix", node, XPathConstants.STRING),
            (String) evaluate("oai:schema", node, XPathConstants.STRING),
            (String) evaluate("oai:metadataNamespace", node, XPathConstants.STRING)
    );
  }
  
  private Map<String,String[]> extractRequest() {
    Map<String,String[]> values = new HashMap<>();
    values.put("verb", new String[] { Verb.ListMetadataFormats.name() });
    values.put("identifier", new String[]{ readIdentifierAsString(doc) });
    return values;
  }
  
}
