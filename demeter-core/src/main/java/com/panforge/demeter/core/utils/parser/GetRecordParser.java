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

import com.panforge.demeter.core.api.exception.BadArgumentException;
import com.panforge.demeter.core.api.exception.BadVerbException;
import com.panforge.demeter.core.model.request.GetRecordRequest;
import com.panforge.demeter.core.model.response.GetRecordResponse;
import com.panforge.demeter.core.model.response.elements.Record;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * GetRecord request parser.
 */
class GetRecordParser extends DocParser {
  
  /**
   * Creates instance of the parser.
   * @param doc the document
   */
  public GetRecordParser(Document doc) {
    super(doc);
  }

  @Override
  public GetRecordResponse parse() throws BadArgumentException {
    GetRecordRequest request = extractRequest();
    Node ndRecord = (Node)evaluate("//oai:OAI-PMH/oai:GetRecord/oai:record", doc, XPathConstants.NODE);
    if (ndRecord!=null) {
      Record record = readRecord(ndRecord);
      return new GetRecordResponse(record, readResponseDate(doc), readErrors(doc), request);
    }
    return null;
  }
  
  private GetRecordRequest extractRequest() throws BadArgumentException {
    Map<String,String[]> values = new HashMap<>();
    values.put("identifier", new String[] { readIdentifierAsString(doc) });
    values.put("metadataPrefix", new String[] { readMetadataPrefix(doc) });
    return GetRecordRequest.create(values);
  }
  
}
