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
package com.panforge.demeter.core;

import com.panforge.demeter.core.utils.XmlUtils;
import java.io.IOException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Document samples.
 */
public class DocumentSamples {
  
  public static Document oai_dc() throws IOException, SAXException {
    
    return XmlUtils.parseToXml(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">"
            + "<dc:identifier>ID0001</dc:identifier>"
            + "<dc:title>Sample OAI DC document</dc:title>"
            + "<dc:publisher>Piotr Andzel</dc:publisher>"
            + "</oai_dc:dc>"
    );
  }
  
  public static Document rfc_1807() throws IOException, SAXException {

    return XmlUtils.parseToXml(
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<rfc1807 xmlns=\"http://info.internet.isi.edu:80/in-notes/rfc/files/rfc1807.txt\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://info.internet.isi.edu:80/in-notes/rfc/files/rfc1807.txt http://www.openarchives.org/OAI/1.1/rfc1807.xsd\">"
            + "<bib-version>v2</bib-version>"
            + "<id>ID0002</id>"
            + "<entry>January 1, 1999</entry>"
            + "<title>Sample RFC 1807 document</title>"
            + "<author>Piotr Andzel</author>"
            + "<date>March 30, 1999</date>"
            + "</rfc1807>"
    );
  }
}
