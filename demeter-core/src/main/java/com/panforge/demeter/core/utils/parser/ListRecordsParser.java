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
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.model.response.ListRecordsResponse;
import com.panforge.demeter.core.model.response.elements.Record;
import static com.panforge.demeter.core.utils.nodeiter.NodeIterable.nodes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * ListRecords request parser.
 */
class ListRecordsParser extends DocParser {
  
  /**
   * Creates instance of the parser.
   * @param doc the document
   */
  public ListRecordsParser(Document doc) {
    super(doc);
  }

  @Override
  public ListRecordsResponse parse() {
    ArrayList<Record> records = new ArrayList<>();
    for (Node node : nodes((NodeList) evaluate("//oai:OAI-PMH/oai:ListRecords/oai:record", doc, XPathConstants.NODESET))) {
      records.add(readRecord(node));
    }
    ResumptionToken resumptionToken = readResponseResumptionToken(doc);
    return new ListRecordsResponse(records.toArray(new Record[records.size()]), resumptionToken, readResponseDate(doc), readErrors(doc), extractRequest());
  }
  
  private Map<String,String[]> extractRequest() {
    Map<String,String[]> values = new HashMap<>();
    values.put("verb", new String[] { Verb.ListRecords.name() });
    values.put("resumptionToken", new String[]{ readRequestResumptionToken(doc) });
    values.put("metadataPrefix", new String[]{ readMetadataPrefix(doc) });
    values.put("from", new String[]{ readFromAsString(doc) });
    values.put("until", new String[]{ readUntilAsString(doc) });
    values.put("set", new String[]{ readSet(doc) });
    return values;
  }

}
