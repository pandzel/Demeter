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

  public static Document oai_marc() throws IOException, SAXException {
    return XmlUtils.parseToXml(
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<oai_marc\n"
            + "    status=\"n\" type=\"a\" level=\"m\" encLvl=\"7\" \n"
            + "    catForm=\"a\" xmlns=\"http://www.openarchives.org/OAI/1.1/oai_marc\"\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
            + "    xsi:schemaLocation=\"http://www.openarchives.org/OAI/1.1/oai_marc \n"
            + "                    http://www.openarchives.org/OAI/1.1/oai_marc.xsd\">\n"
            + "<fixfield id=\"1\">\"   90178038 \"</fixfield>\n"
            + "<fixfield id=\"3\">\"DLC\"</fixfield>\n"
            + "<fixfield id=\"5\">\"19900814092959.1\"</fixfield>\n"
            + "<fixfield id=\"8\">\"900724s1974    po af         000 0 fre  \"</fixfield>\n"
            + "<varfield id=\"10\" i1=\" \" i2=\" \">\n"
            + " <subfield label=\"a\">90178038</subfield>\n"
            + "</varfield>\n"
            + "<varfield id=\"40\" i1=\" \" i2=\" \">\n"
            + " <subfield label=\"a\">DLC</subfield>\n"
            + " <subfield label=\"c\">DLC</subfield>\n"
            + "</varfield>\n"
            + "<varfield id=\"50\" i1=\"0\" i2=\"0\">\n"
            + " <subfield label=\"a\">MLCL 90/00723 (Q)</subfield>\n"
            + "</varfield>\n"
            + " <varfield id=\"100\" i1=\"1\" i2=\"0\">\n"
            + " <subfield label=\"a\">Berthou, P. Y.</subfield>\n"
            + " <subfield label=\"q\">(Pierre Yves)</subfield>\n"
            + "</varfield>\n"
            + " <varfield id=\"245\" i1=\"1\" i2=\"3\">\n"
            + " <subfield label=\"a\">Le C&#x00E9;nomanien de l\"Estr&#x00E9;madure \n"
            + "                     portugaise /</subfield>\n"
            + " <subfield label=\"c\">par Pierre-Yves Berthou.</subfield>\n"
            + "</varfield>\n"
            + "<varfield id=\"260\" i1=\"0\" i2=\" \">\n"
            + " <subfield label=\"a\">Lisboa :</subfield>\n"
            + " <subfield label=\"b\">Minist&#x00E9;rio da Economia, Secretaria de Estado\n"
            + "           da Ind&#x00FA;stria, Direc&#x00E7;&#x00E3;o-Geral de Minas e \n"
            + "           Servi&#x00E7;os Geol&#x00F3;gicos, Servi&#x00E7;os \n"
            + "           Geol&#x00F3;gicos de Portugal,</subfield>\n"
            + " <subfield label=\"c\">1973 [i.e. 1974]</subfield>\n"
            + "</varfield>\n"
            + "<varfield id=\"300\" i1=\" \" i2=\" \">\n"
            + " <subfield label=\"a\">168 p., 67 leaves of plates :</subfield>\n"
            + " <subfield label=\"b\">ill. ;</subfield>\n"
            + " <subfield label=\"c\">33 cm.</subfield>\n"
            + "</varfield>\n"
            + "<varfield id=\"490\" i1=\"1\" i2=\" \">\n"
            + " <subfield label=\"a\">Mem&#x00F3;ria ;</subfield>\n"
            + " <subfield label=\"v\">no. 23 (Nova s&#x00E9;r.)</subfield>\n"
            + "</varfield>\n"
            + "<varfield id=\"653\" i1=\"0\" i2=\" \">\n"
            + " <subfield label=\"a\">Stratigraphic geology;</subfield>\n"
            + " <subfield label=\"a\">Cenomanian deposits;</subfield>\n"
            + " <subfield label=\"a\">Portugal</subfield>\n"
            + "</varfield>\n"
            + "<varfield id=\"830\" i1=\" \" i2=\"0\">\n"
            + " <subfield label=\"a\">Mem&#x00F3;ria (Servi&#x00E7;os Geol&#x00F3;gicos \n"
            + "           de Portugal) ;</subfield>\n"
            + " <subfield label=\"v\">nova s&#x00E9;r. no. 23.</subfield>\n"
            + "</varfield>\n"
            + "</oai_marc>"
    );
  }
  
  public static Document marc21() throws IOException, SAXException {
    return XmlUtils.parseToXml(
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<record xmlns=\"http://www.loc.gov/MARC21/slim\"\n"
            + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
            + "  xsi:schemaLocation=\"http://www.loc.gov/MARC21/slim \n"
            + "  http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd\"\n"
            + "  type=\"Bibliographic\">\n"
            + "  <leader>01427cem  22003731  4500</leader>\n"
            + "<controlfield tag=\"001\">   99448814 </controlfield>\n"
            + "<controlfield tag=\"003\">DLC</controlfield>\n"
            + "<controlfield tag=\"005\">20020812092159.0</controlfield>\n"
            + "<controlfield tag=\"007\">aj aanzn</controlfield>\n"
            + "<controlfield tag=\"007\">cr |||||||||||</controlfield>\n"
            + "<controlfield tag=\"008\">020710s1862    nyu       a     0   eng  </controlfield>\n"
            + "<datafield tag=\"035\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">(DLC)12848717</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"010\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">   99448814 </subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"040\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">DLC</subfield>\n"
            + "<subfield code=\"c\">DLC</subfield>\n"
            + "<subfield code=\"d\">DLC</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"050\" ind1=\"0\" ind2=\"0\">\n"
            + "<subfield code=\"a\">G3914.C3S5 1862</subfield>\n"
            + "<subfield code=\"b\">.E3</subfield>\n"
            + "<subfield code=\"u\">CW 369</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"052\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">3914</subfield>\n"
            + "<subfield code=\"b\">C3</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"072\" ind1=\" \" ind2=\"7\">\n"
            + "<subfield code=\"a\">S5</subfield>\n"
            + "<subfield code=\"2\">lcg</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"110\" ind1=\"2\" ind2=\" \">\n"
            + "<subfield code=\"a\">E. &amp; G.W. Blunt (Firm).</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"245\" ind1=\"1\" ind2=\"0\">\n"
            + "<subfield code=\"a\">E. &amp; G. W. Blunt's map of Charleston and vicinity.</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"260\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">[New York],</subfield>\n"
            + "<subfield code=\"c\">1862.</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"300\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">1 map,</subfield>\n"
            + "<subfield code=\"c\">46 x 52 cm.</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"507\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">Scale ca. 1:151,000.</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"510\" ind1=\"4\" ind2=\" \">\n"
            + "<subfield code=\"a\">LC Civil War Maps (2nd ed.),</subfield>\n"
            + "<subfield code=\"c\">369</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"500\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">Indicates fortifications, roads, railroads, towns, street pattern of Charleston, drainage, vegetation, and ship channels.</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"500\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">Description derived from published bibliography.</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"530\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">Available also through the Library of Congress web site as raster image.</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"651\" ind1=\" \" ind2=\"0\">\n"
            + "<subfield code=\"a\">Charleston Region (S.C.)</subfield>\n"
            + "<subfield code=\"x\">History</subfield>\n"
            + "<subfield code=\"y\">Civil War, 1861-1865</subfield>\n"
            + "<subfield code=\"v\">Maps.</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"752\" ind1=\" \" ind2=\" \">\n"
            + "<subfield code=\"a\">United States</subfield>\n"
            + "<subfield code=\"b\">South Carolina</subfield>\n"
            + "<subfield code=\"d\">Charleston Region.</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"852\" ind1=\"0\" ind2=\" \">\n"
            + "<subfield code=\"a\">Library of Congress</subfield>\n"
            + "<subfield code=\"b\">Geography and Map Division</subfield>\n"
            + "<subfield code=\"e\">Washington, D.C. 20540-4650</subfield>\n"
            + "<subfield code=\"n\">dcu</subfield>\n"
            + "</datafield>\n"
            + "<datafield tag=\"856\" ind1=\"4\" ind2=\"1\">\n"
            + "<subfield code=\"3\">copy 1</subfield>\n"
            + "<subfield code=\"d\">g3914c</subfield>\n"
            + "<subfield code=\"f\">cw0369000</subfield>\n"
            + "<subfield code=\"u\">http://hdl.loc.gov/loc.gmd/g3914c.cw0369000</subfield>\n"
            + "</datafield>\n"
            + "</record>"
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
