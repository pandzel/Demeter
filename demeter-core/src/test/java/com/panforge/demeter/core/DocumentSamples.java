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

  public static Document oai_identifier() throws IOException, SAXException {

    return XmlUtils.parseToXml(
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<oai-identifier xmlns=\"http://www.openarchives.org/OAI/2.0/oai-identifier\"\n"
            + "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "      xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai-identifier\n"
            + "      http://www.openarchives.org/OAI/2.0/oai-identifier.xsd\">\n"
            + "    <scheme>oai</scheme> \n"
            + "    <repositoryIdentifier>bespa.org</repositoryIdentifier>    \n"
            + "    <delimiter>:</delimiter> \n"
            + "    <sampleIdentifier>oai:bespa.org:medi99-123</sampleIdentifier>\n"
            + "  </oai-identifier>"
    );
  }

  public static Document eprints() throws IOException, SAXException {
    return XmlUtils.parseToXml(
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<eprints xmlns=\"http://www.openarchives.org/OAI/1.1/eprints\"\n"
            + "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "      xsi:schemaLocation=\"http://www.openarchives.org/OAI/1.1/eprints         \n"
            + "      http://www.openarchives.org/OAI/1.1/eprints.xsd\">\n"
            + "   <content>\n"
            + "     <URL>http://arXiv.org/arXiv_content.htm</URL>\n"
            + "   </content>\n"
            + "   <metadataPolicy>\n"
            + "     <text>Metadata can be used by commercial and non-commercial \n"
            + "           service providers</text>\n"
            + "     <URL>http://arXiv.org/arXiv_metadata_use.htm</URL>\n"
            + "   </metadataPolicy>\n"
            + "   <dataPolicy>\n"
            + "     <text>Full content, i.e. preprints may not be harvested by robots</text>\n"
            + "   </dataPolicy>\n"
            + "   <submissionPolicy>\n"
            + "     <URL>http://arXiv.org/arXiv_submission.htm</URL>\n"
            + "   </submissionPolicy>\n"
            + "</eprints>"
    );
  }
  
  public static Document friends() throws IOException, SAXException {
    return XmlUtils.parseToXml(
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<friends xmlns=\"http://www.openarchives.org/OAI/2.0/friends/\" \n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/friends/\n"
            + "      http://www.openarchives.org/OAI/2.0/friends.xsd\">\n"
            + "    <baseURL>http://oai.east.org/foo/</baseURL>\n"
            + "    <baseURL>http://oai.hq.org/bar/</baseURL>\n"
            + "    <baseURL>http://oai.south.org/repo.cgi</baseURL>\n"
            + "</friends>"
    );
  }
  
  public static Document branding() throws IOException, SAXException {
    return XmlUtils.parseToXml(
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<branding xmlns=\"http://www.openarchives.org/OAI/2.0/branding/\"\n"
            + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "  xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/branding/\n"
            + "                      http://www.openarchives.org/OAI/2.0/branding.xsd\">\n"
            + "  <collectionIcon>\n"
            + "    <url>http://my.site/icon.png</url>\n"
            + "    <link>http://my.site/homepage.html</link>\n"
            + "    <title>MySite(tm)</title>\n"
            + "    <width>88</width>\n"
            + "    <height>31</height>\n"
            + "  </collectionIcon>\n"
            + "  <metadataRendering \n"
            + "    metadataNamespace=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" \n"
            + "    mimeType=\"text/xsl\">http://some.where/DCrender.xsl</metadataRendering>\n"
            + "  <metadataRendering\n"
            + "    metadataNamespace=\"http://another.place/MARC\" \n"
            + "    mimeType=\"text/css\">http://another.place/MARCrender.css</metadataRendering>\n"
            + "</branding>"
    );
  }
  
  public static Document gateway() throws IOException, SAXException {
    return XmlUtils.parseToXml(
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<gateway xmlns=\"http://www.openarchives.org/OAI/2.0/gateway/\"\n"
            + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "  xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/gateway/\n"
            + "                      http://www.openarchives.org/OAI/2.0/gateway.xsd\">\n"
            + "  <source>http://foo.edu/Dienst/</source>\n"
            + "  <gatewayDescription>http://cs-tr.cs.cornell.edu/Dienst/UI/2.0/Describe/ncstrl.cornell/TR94-1418</gatewayDescription>\n"
            + "  <gatewayAdmin>admin@bar.edu</gatewayAdmin>\n"
            + "  <gatewayAdmin>admin-alternative@bar.edu</gatewayAdmin>\n"
            + "  <gatewayURL>http://bar.edu/oai-gateway/2.0/</gatewayURL>\n"
            + "  <gatewayNotes>http://bar.edu/oai-gateway/notes.html</gatewayNotes>\n"
            + "</gateway>"
    );
  }
  
  public static Document provenance() throws IOException, SAXException {
    return XmlUtils.parseToXml(
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<provenance xmlns=\"http://www.openarchives.org/OAI/2.0/provenance\"\n"
            + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "  xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/provenance\n"
            + "                      http://www.openarchives.org/OAI/2.0/provenance.xsd\">\n"
            + "\n"
            + "<originDescription harvestDate=\"2002-02-02T14:10:02Z\" altered=\"true\">\n"
            + "  <baseURL>http://the.oa.org</baseURL>\n"
            + "  <identifier>oai:r2.org:klik001</identifier>\n"
            + "  <datestamp>2002-01-01</datestamp>\n"
            + "  <metadataNamespace>http://www.openarchives.org/OAI/2.0/oai_dc/</metadataNamespace>\n"
            + "  <originDescription harvestDate=\"2002-01-01T11:10:01Z\" altered=\"false\">\n"
            + "    <baseURL>http://some.oa.org</baseURL>\n"
            + "    <identifier>oai:r2.org:klik001</identifier>\n"
            + "    <datestamp>2001-01-01</datestamp>\n"
            + "    <metadataNamespace>http://www.openarchives.org/OAI/2.0/oai_dc/</metadataNamespace>\n"
            + "  </originDescription>\n"
            + "</originDescription>\n"
            + "\n"
            + "</provenance>"
    );
  }
  
  public static Document rights() throws IOException, SAXException {
    return XmlUtils.parseToXml(
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<rights xmlns=\"http://www.openarchives.org/OAI/2.0/rights/\"  \n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
            + "    xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/rights/\n"
            + "                        http://www.openarchives.org/OAI/2.0/rights.xsd\">\n"
            + "    <rightsDefinition>\n"
            + "      <oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\"\n"
            + "        xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n"
            + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "        xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/\n"
            + "                            http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n"
            + "        <dc:title>GNU Free Documentation License, Version 1.2</dc:title>\n"
            + "        <dc:date>2002-11</dc:date>\n"
            + "        <dc:creator>Free Software Foundation, Inc.</dc:creator>\n"
            + "        <dc:description>Copyright (c) 2005 Fred Bloggs.\n"
            + "          Permission is granted to copy, distribute and/or modify this document\n"
            + "          under the terms of the GNU Free Documentation License, Version 1.2\n"
            + "          or any later version published by the Free Software Foundation;\n"
            + "          with no Invariant Sections, no Front-Cover Texts, and no Back-Cover\n"
            + "          Texts.</dc:description>\n"
            + "        <dc:identifier>http://www.gnu.org/licenses/fdl.txt</dc:identifier>\n"
            + "      </oai_dc:dc>\n"
            + "    </rightsDefinition>\n"
            + "  </rights>"
    );
  }
  
  public static Document rightsManifest() throws IOException, SAXException {
    return XmlUtils.parseToXml(
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<rightsManifest\n"
            + "    xmlns=\"http://www.openarchives.org/OAI/2.0/rights/\" \n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
            + "    xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/rights/ \n"
            + "                        http://www.openarchives.org/OAI/2.0/rightsManifest.xsd\"\n"
            + "    appliesTo=\"http://www.openarchives.org/OAI/2.0/entity#metadata\">\n"
            + "    <rights>\n"
            + "      <rightsDefinition>\n"
            + "        <oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\"\n"
            + "          xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n"
            + "          xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "          xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/\n"
            + "                              http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n"
            + "          <dc:title>GNU Free Documentation License, Version 1.2</dc:title>\n"
            + "          <dc:date>2002-11</dc:date>\n"
            + "          <dc:creator>Free Software Foundation, Inc.</dc:creator>\n"
            + "          <dc:description>Copyright (c) 2005 Fred Bloggs.\n"
            + "            Permission is granted to copy, distribute and/or modify this document\n"
            + "            under the terms of the GNU Free Documentation License, Version 1.2\n"
            + "            or any later version published by the Free Software Foundation;\n"
            + "            with no Invariant Sections, no Front-Cover Texts, and no Back-Cover\n"
            + "            Texts.</dc:description>\n"
            + "          <dc:identifier>http://www.gnu.org/licenses/fdl.txt</dc:identifier>\n"
            + "        </oai_dc:dc>\n"
            + "      </rightsDefinition>\n"
            + "    </rights>\n"
            + "    <rights>\n"
            + "      <rightsReference ref=\"http://creativecommons.org/licenses/by-nd/2.0/rdf\"/>\n"
            + "    </rights>\n"
            + "  </rightsManifest>"
    );
  }
}
