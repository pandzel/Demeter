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

import com.panforge.demeter.core.api.Config;
import com.panforge.demeter.core.api.ResponseFactory;
import com.panforge.demeter.core.api.Context;
import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.model.request.GetRecordRequest;
import com.panforge.demeter.core.model.request.IdentifyRequest;
import com.panforge.demeter.core.model.request.ListIdentifiersRequest;
import com.panforge.demeter.core.model.request.ListMetadataFormatsRequest;
import com.panforge.demeter.core.model.request.ListRecordsRequest;
import com.panforge.demeter.core.model.request.ListSetsRequest;
import com.panforge.demeter.core.model.response.GetRecordResponse;
import com.panforge.demeter.core.model.response.IdentifyResponse;
import com.panforge.demeter.core.model.response.ListIdentifiersResponse;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.ListMetadataFormatsResponse;
import com.panforge.demeter.core.model.response.ListRecordsResponse;
import com.panforge.demeter.core.model.response.ListSetsResponse;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.response.elements.Set;
import static com.panforge.demeter.core.DocumentSamples.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * ResponseFactory test
 */
public class ResponseFactoryTest {

  private static Config config;
  private static Context ctx;
  private static ResponseFactory f;
  private static DocumentBuilder builder;
  private static XPath xpath;
  private static Validator validator;

  @BeforeClass
  public static void initClass() throws ParserConfigurationException, SAXException, IOException {
    config = new Config();
    config.repositoryName = "Sample";
    config.baseURL = "http://localhost/oaipmh";
    config.protocolVersion = "2.0";
    config.adminEmail = new String[]{"somebody@company.com", "anubody@company.com"};
    config.deletedRecord = Config.Deletion.Persistent;
    config.granularity = "YYYY-MM-DDThh:mm:ssZ";
    config.earliestDatestamp = OffsetDateTime.now();
    config.compression = Config.Compression.values();

    ctx = new Context(config);
    f = new ResponseFactory(ctx);

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(false);
    builder = factory.newDocumentBuilder();

    XPathFactory xfactory = XPathFactory.newInstance();
    xpath = xfactory.newXPath();

    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = schemaFactory.newSchema(new Source[]{
      new StreamSource(new URL("http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd").openStream()),
      new StreamSource(new URL("http://www.openarchives.org/OAI/2.0/oai_dc.xsd").openStream()),
      new StreamSource(new URL("http://dublincore.org/schemas/xmls/qdc/2008/02/11/dc.xsd").openStream()),
      new StreamSource(new URL("http://www.openarchives.org/OAI/1.1/rfc1807.xsd").openStream()),
      new StreamSource(new URL("http://www.openarchives.org/OAI/1.1/oai_marc.xsd").openStream()),
      new StreamSource(new URL("http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd").openStream()),
      
      new StreamSource(new URL("http://www.openarchives.org/OAI/2.0/oai-identifier.xsd").openStream()),
      new StreamSource(new URL("http://www.openarchives.org/OAI/2.0/rightsManifest.xsd").openStream()),
      new StreamSource(new URL("http://www.openarchives.org/OAI/1.1/eprints.xsd").openStream()),
      new StreamSource(new URL("http://www.openarchives.org/OAI/2.0/friends.xsd").openStream()),
      new StreamSource(new URL("http://www.openarchives.org/OAI/2.0/branding.xsd").openStream()),
      new StreamSource(new URL("http://www.openarchives.org/OAI/2.0/gateway.xsd").openStream()),
      new StreamSource(new URL("http://www.openarchives.org/OAI/2.0/provenance.xsd").openStream()),
      new StreamSource(new URL("http://www.openarchives.org/OAI/2.0/rights.xsd").openStream()),
    });

    validator = schema.newValidator();
  }

  @Test
  public void testCreateListSetsResponse() throws Exception {

    Set set = new Set("music", "Music (set)", new Document[]{branding(), rightsManifest()});

    ListSetsRequest request = new ListSetsRequest();

    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);

    ListSetsResponse response = new ListSetsResponse(request.getParameters(), OffsetDateTime.now(), new Set[]{set}, resumptionToken);

    String rsp = f.createListSetsResponse(response);

    System.out.println(String.format("%s", rsp));

    assertNotNull("Null response", rsp);
    assertTrue("Invalid response by schema", validate(rsp));

    Document doc = parse(rsp);
    assertNotNull("Null document", doc);
    assertTrue("No root element", test(doc, "count(//OAI-PMH)=1"));
    assertTrue("No response date", test(doc, "count(//OAI-PMH/responseDate)=1"));
    assertTrue("No request", test(doc, "count(//OAI-PMH/request)=1"));
    assertTrue("Invalid verb", test(doc, "//OAI-PMH/request/@verb='ListSets'"));
    assertTrue("No content node", test(doc, "count(//OAI-PMH/ListSets)=1"));
    assertTrue("No set node", test(doc, "count(//OAI-PMH/ListSets/set)>0"));
    assertTrue("No setSpec node", test(doc, "count(//OAI-PMH/ListSets/set[1]/setSpec)=1"));
    assertTrue("No setName node", test(doc, "count(//OAI-PMH/ListSets/set[1]/setName)=1"));
    assertTrue("Invalid setSpec value", test(doc, "//OAI-PMH/ListSets/set[1]/setSpec='music'"));
    assertTrue("Invalid setName value", test(doc, "//OAI-PMH/ListSets/set[1]/setName='Music (set)'"));
    assertTrue("No resumptionToken", test(doc, "count(//OAI-PMH/ListSets/resumptionToken)=1"));
    assertTrue("Invalid resumptionToken", test(doc, "//OAI-PMH/ListSets/resumptionToken='token'"));
    assertTrue("Invalid resumptionToken expiration date", test(doc, "//OAI-PMH/ListSets/resumptionToken/@expirationDate='" + response.resumptionToken.expirationDate.format(DateTimeFormatter.ISO_DATE_TIME) + "'"));
    assertTrue("Invalid cursor value", test(doc, "//OAI-PMH/ListSets/resumptionToken/@cursor='0'"));
    assertTrue("Invalid completeListSize value", test(doc, "//OAI-PMH/ListSets/resumptionToken/@completeListSize='300'"));
  }

  @Test
  public void testCreateListSetsResponseWithResumptionToken() throws Exception {

    Set set = new Set("music", "Music (set)", new Document[]{oai_dc()});

    ListSetsRequest request = ListSetsRequest.resume("token");

    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);

    ListSetsResponse response = new ListSetsResponse(request.getParameters(), OffsetDateTime.now(), new Set[]{set}, resumptionToken);

    String rsp = f.createListSetsResponse(response);

    System.out.println(String.format("%s", rsp));

    assertNotNull("Null response", rsp);
    assertTrue("Invalid response by schema", validate(rsp));

    Document doc = parse(rsp);
    assertNotNull("Null document", doc);
    assertTrue("No root element", test(doc, "count(//OAI-PMH)=1"));
    assertTrue("No response date", test(doc, "count(//OAI-PMH/responseDate)=1"));
    assertTrue("No request", test(doc, "count(//OAI-PMH/request)=1"));
    assertTrue("Invalid verb", test(doc, "//OAI-PMH/request/@verb='ListSets'"));
    assertTrue("Invalid resumtion token", test(doc, "//OAI-PMH/request/@resumptionToken='token'"));
    assertTrue("No content node", test(doc, "count(//OAI-PMH/ListSets)=1"));
    assertTrue("No set node", test(doc, "count(//OAI-PMH/ListSets/set)>0"));
    assertTrue("No setSpec node", test(doc, "count(//OAI-PMH/ListSets/set[1]/setSpec)=1"));
    assertTrue("No setName node", test(doc, "count(//OAI-PMH/ListSets/set[1]/setName)=1"));
    assertTrue("Invalid setSpec value", test(doc, "//OAI-PMH/ListSets/set[1]/setSpec='music'"));
    assertTrue("Invalid setName value", test(doc, "//OAI-PMH/ListSets/set[1]/setName='Music (set)'"));
    assertTrue("No resumptionToken", test(doc, "count(//OAI-PMH/ListSets/resumptionToken)=1"));
    assertTrue("Invalid resumptionToken", test(doc, "//OAI-PMH/ListSets/resumptionToken=''"));
    assertTrue("Invalid resumptionToken expiration date", test(doc, "//OAI-PMH/ListSets/resumptionToken/@expirationDate='" + response.resumptionToken.expirationDate.format(DateTimeFormatter.ISO_DATE_TIME) + "'"));
    assertTrue("Invalid cursor value", test(doc, "//OAI-PMH/ListSets/resumptionToken/@cursor='0'"));
    assertTrue("Invalid completeListSize value", test(doc, "//OAI-PMH/ListSets/resumptionToken/@completeListSize='300'"));
  }

  @Test
  public void testCreateListMetadataFormatsResponse() throws Exception {
    ListMetadataFormatsRequest request = new ListMetadataFormatsRequest(null);

    MetadataFormat fmt = new MetadataFormat(
            "oai_dc",
            "http://www.openarchives.org/OAI/2.0/oai_dc.xsd",
            "http://www.openarchives.org/OAI/2.0/oai_dc/"
    );

    ListMetadataFormatsResponse response = new ListMetadataFormatsResponse(request.getParameters(), OffsetDateTime.now(), new MetadataFormat[]{fmt});

    String rsp = f.createListMetadataFormatsResponse(response);
    System.out.println(String.format("%s", rsp));

    assertNotNull("Null response", rsp);
    assertTrue("Invalid response by schema", validate(rsp));

    Document doc = parse(rsp);
    assertNotNull("Null document", doc);
    assertTrue("No root element", test(doc, "count(//OAI-PMH)=1"));
    assertTrue("No response date", test(doc, "count(//OAI-PMH/responseDate)=1"));
    assertTrue("No request", test(doc, "count(//OAI-PMH/request)=1"));
    assertTrue("Invalid verb", test(doc, "//OAI-PMH/request/@verb='ListMetadataFormats'"));
    assertTrue("No content node", test(doc, "count(//OAI-PMH/ListMetadataFormats)=1"));
    assertTrue("No metadataFormat node", test(doc, "count(//OAI-PMH/ListMetadataFormats/metadataFormat)>0"));
    assertTrue("No metadataPrefix node", test(doc, "count(//OAI-PMH/ListMetadataFormats/metadataFormat[1]/metadataPrefix)=1"));
    assertTrue("No schema node", test(doc, "count(//OAI-PMH/ListMetadataFormats/metadataFormat[1]/schema)=1"));
    assertTrue("No metadataNamespace node", test(doc, "count(//OAI-PMH/ListMetadataFormats/metadataFormat[1]/metadataNamespace)=1"));
    assertTrue("Invalid metadataPrefix value", test(doc, "//OAI-PMH/ListMetadataFormats/metadataFormat[1]/metadataPrefix='oai_dc'"));
    assertTrue("Invalid schema value", test(doc, "//OAI-PMH/ListMetadataFormats/metadataFormat[1]/schema='http://www.openarchives.org/OAI/2.0/oai_dc.xsd'"));
    assertTrue("Invalid metadataNamespace value", test(doc, "//OAI-PMH/ListMetadataFormats/metadataFormat[1]/metadataNamespace='http://www.openarchives.org/OAI/2.0/oai_dc/'"));
  }

  @Test
  public void testIdentifyResponse() throws Exception {
    IdentifyRequest request = new IdentifyRequest();
    Document[] descriptions = new Document[]{oai_identifier(), rightsManifest(), eprints(), friends(), branding(), gateway()};

    IdentifyResponse response = IdentifyResponse.createFromConfig(request.getParameters(), OffsetDateTime.now(), config, descriptions);

    String rsp = f.createIdentifyResponse(response);
    System.out.println(String.format("%s", rsp));

    assertNotNull("Null response", rsp);
    assertTrue("Invalid response by schema", validate(rsp));

    Document doc = parse(rsp);
    assertNotNull("Null document", doc);
    assertTrue("No root element", test(doc, "count(//OAI-PMH)=1"));
    assertTrue("No response date", test(doc, "count(//OAI-PMH/responseDate)=1"));
    assertTrue("No request", test(doc, "count(//OAI-PMH/request)=1"));
    assertTrue("Invalid verb", test(doc, "//OAI-PMH/request/@verb='Identify'"));
    assertTrue("No content node", test(doc, "count(//OAI-PMH/Identify)=1"));
    assertTrue("No repositoryName node", test(doc, "count(//OAI-PMH/Identify/repositoryName)=1"));
    assertTrue("No baseURL node", test(doc, "count(//OAI-PMH/Identify/baseURL)=1"));
    assertTrue("No protocolVersion node", test(doc, "count(//OAI-PMH/Identify/protocolVersion)=1"));
    assertTrue("No adminEmail nodes", test(doc, "count(//OAI-PMH/Identify/adminEmail)>0"));
    assertTrue("No earliestDatestamp node", test(doc, "count(//OAI-PMH/Identify/earliestDatestamp)=1"));
    assertTrue("No deletedRecord node", test(doc, "count(//OAI-PMH/Identify/deletedRecord)=1"));
    assertTrue("No granularity node", test(doc, "count(//OAI-PMH/Identify/granularity)=1"));
    assertTrue("No compression node", test(doc, "count(//OAI-PMH/Identify/compression)>0"));
    // TODO: provide Identify description test
    // assertTrue("No description node", test(doc, "count(//OAI-PMH/Identify/description)>0"));
  }

  @Test
  public void testListIdentifiersResponse() throws Exception {
    ListIdentifiersRequest request = new ListIdentifiersRequest("oai", null, null, null);

    Header header = new Header(URI.create("identifier"), OffsetDateTime.now(), new String[]{"music"}, false);

    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);

    ListIdentifiersResponse response = new ListIdentifiersResponse(request.getParameters(), OffsetDateTime.now(), new Header[]{header}, resumptionToken);

    String rsp = f.createListIdentifiersResponse(response);
    System.out.println(String.format("%s", rsp));

    assertNotNull("Null response", rsp);
    assertTrue("Invalid response by schema", validate(rsp));

    Document doc = parse(rsp);
    assertNotNull("Null document", doc);
    assertTrue("No root element", test(doc, "count(//OAI-PMH)=1"));
    assertTrue("No response date", test(doc, "count(//OAI-PMH/responseDate)=1"));
    assertTrue("No request", test(doc, "count(//OAI-PMH/request)=1"));
    assertTrue("Invalid verb", test(doc, "//OAI-PMH/request/@verb='ListIdentifiers'"));
    assertTrue("No ListIdentifiers node", test(doc, "count(//OAI-PMH/ListIdentifiers)=1"));
    assertTrue("No header nodes", test(doc, "count(//OAI-PMH/ListIdentifiers/header)>0"));
    assertTrue("No identifier node", test(doc, "count(//OAI-PMH/ListIdentifiers/header[1]/identifier)=1"));
    assertTrue("Invalid identifier value", test(doc, "//OAI-PMH/ListIdentifiers/header[1]/identifier='identifier'"));
    assertTrue("No datestamp node", test(doc, "count(//OAI-PMH/ListIdentifiers/header[1]/datestamp)=1"));
    assertTrue("No setSpec nodes", test(doc, "count(//OAI-PMH/ListIdentifiers/header[1]/setSpec)>0"));
    assertTrue("Invalid setSpec value", test(doc, "//OAI-PMH/ListIdentifiers/header[1]/setSpec='music'"));
    assertTrue("No resumptionToken", test(doc, "count(//OAI-PMH/ListIdentifiers/resumptionToken)=1"));
    assertTrue("Invalid resumptionToken", test(doc, "//OAI-PMH/ListIdentifiers/resumptionToken='token'"));
    assertTrue("Invalid resumptionToken expiration date", test(doc, "//OAI-PMH/ListIdentifiers/resumptionToken/@expirationDate='" + response.resumptionToken.expirationDate.format(DateTimeFormatter.ISO_DATE_TIME) + "'"));
    assertTrue("Invalid cursor value", test(doc, "//OAI-PMH/ListIdentifiers/resumptionToken/@cursor='0'"));
    assertTrue("Invalid completeListSize value", test(doc, "//OAI-PMH/ListIdentifiers/resumptionToken/@completeListSize='300'"));
  }

  @Test
  public void testListIdentifiersResponseWithResumptionToken() throws Exception {
    ListIdentifiersRequest request = ListIdentifiersRequest.resume("token");

    Header header = new Header(URI.create("identifier"), OffsetDateTime.now(), new String[]{"music"}, false);

    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);

    ListIdentifiersResponse response = new ListIdentifiersResponse(request.getParameters(), OffsetDateTime.now(), new Header[]{header}, resumptionToken);

    String rsp = f.createListIdentifiersResponse(response);
    System.out.println(String.format("%s", rsp));

    assertNotNull("Null response", rsp);
    assertTrue("Invalid response by schema", validate(rsp));

    Document doc = parse(rsp);
    assertNotNull("Null document", doc);
    assertTrue("No root element", test(doc, "count(//OAI-PMH)=1"));
    assertTrue("No response date", test(doc, "count(//OAI-PMH/responseDate)=1"));
    assertTrue("No request", test(doc, "count(//OAI-PMH/request)=1"));
    assertTrue("Invalid verb", test(doc, "//OAI-PMH/request/@verb='ListIdentifiers'"));
    assertTrue("Invalid resumtion token", test(doc, "//OAI-PMH/request/@resumptionToken='token'"));
    assertTrue("No ListIdentifiers node", test(doc, "count(//OAI-PMH/ListIdentifiers)=1"));
    assertTrue("No header nodes", test(doc, "count(//OAI-PMH/ListIdentifiers/header)>0"));
    assertTrue("No identifier node", test(doc, "count(//OAI-PMH/ListIdentifiers/header[1]/identifier)=1"));
    assertTrue("Invalid identifier value", test(doc, "//OAI-PMH/ListIdentifiers/header[1]/identifier='identifier'"));
    assertTrue("No datestamp node", test(doc, "count(//OAI-PMH/ListIdentifiers/header[1]/datestamp)=1"));
    assertTrue("No setSpec nodes", test(doc, "count(//OAI-PMH/ListIdentifiers/header[1]/setSpec)>0"));
    assertTrue("Invalid setSpec value", test(doc, "//OAI-PMH/ListIdentifiers/header[1]/setSpec='music'"));
    assertTrue("No resumptionToken", test(doc, "count(//OAI-PMH/ListIdentifiers/resumptionToken)=1"));
    assertTrue("Invalid resumptionToken", test(doc, "//OAI-PMH/ListIdentifiers/resumptionToken=''"));
    assertTrue("Invalid resumptionToken expiration date", test(doc, "//OAI-PMH/ListIdentifiers/resumptionToken/@expirationDate='" + response.resumptionToken.expirationDate.format(DateTimeFormatter.ISO_DATE_TIME) + "'"));
    assertTrue("Invalid cursor value", test(doc, "//OAI-PMH/ListIdentifiers/resumptionToken/@cursor='0'"));
    assertTrue("Invalid completeListSize value", test(doc, "//OAI-PMH/ListIdentifiers/resumptionToken/@completeListSize='300'"));
  }

  @Test
  public void testListRecordsResponse() throws Exception {
    ListRecordsRequest request = new ListRecordsRequest("oai", null, null, null);

    Header header = new Header(URI.create("identifier"), OffsetDateTime.now(), new String[]{"music"}, false);

    Record record = new Record(header, oai_dc(), new Document[]{rfc_1807()});

    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);

    ListRecordsResponse response = new ListRecordsResponse(request.getParameters(), OffsetDateTime.now(), new Record[]{record}, resumptionToken);

    String rsp = f.createListRecordsResponse(response);
    System.out.println(String.format("%s", rsp));

    assertNotNull("Null response", rsp);
    assertTrue("Invalid response by schema", validate(rsp));

    Document doc = parse(rsp);
    assertNotNull("Null document", doc);
    assertTrue("No root element", test(doc, "count(//OAI-PMH)=1"));
    assertTrue("No response date", test(doc, "count(//OAI-PMH/responseDate)=1"));
    assertTrue("No request", test(doc, "count(//OAI-PMH/request)=1"));
    assertTrue("Invalid verb", test(doc, "//OAI-PMH/request/@verb='ListRecords'"));
    assertTrue("No ListRecords node", test(doc, "count(//OAI-PMH/ListRecords)=1"));
    assertTrue("No record nodes", test(doc, "count(//OAI-PMH/ListRecords/record)>0"));
    assertTrue("No header node", test(doc, "count(//OAI-PMH/ListRecords/record[1]/header)=1"));
    assertTrue("No metadata node", test(doc, "count(//OAI-PMH/ListRecords/record[1]/metadata)=1"));
    assertTrue("No metadata content", test(doc, "count(//OAI-PMH/ListRecords/record[1]/metadata/dc)=1"));
    assertTrue("No about node", test(doc, "count(//OAI-PMH/ListRecords/record[1]/about)=1"));
    assertTrue("No about content", test(doc, "count(//OAI-PMH/ListRecords/record[1]/about/rfc1807)=1"));

    assertTrue("No resumptionToken", test(doc, "count(//OAI-PMH/ListRecords/resumptionToken)=1"));
    assertTrue("Invalid resumptionToken", test(doc, "//OAI-PMH/ListRecords/resumptionToken='token'"));
    assertTrue("Invalid resumptionToken expiration date", test(doc, "//OAI-PMH/ListRecords/resumptionToken/@expirationDate='" + response.resumptionToken.expirationDate.format(DateTimeFormatter.ISO_DATE_TIME) + "'"));
    assertTrue("Invalid cursor value", test(doc, "//OAI-PMH/ListRecords/resumptionToken/@cursor='0'"));
    assertTrue("Invalid completeListSize value", test(doc, "//OAI-PMH/ListRecords/resumptionToken/@completeListSize='300'"));
  }

  @Test
  public void testListRecordsResponseWithResumptionToken() throws Exception {
    ListRecordsRequest request = ListRecordsRequest.resume("token");

    Header header = new Header(URI.create("identifier"), OffsetDateTime.now(), new String[]{"music"}, false);

    Record record = new Record(header, oai_dc(), new Document[]{rfc_1807()});

    ResumptionToken resumptionToken = new ResumptionToken("token", OffsetDateTime.now(), 300L, 0L);

    ListRecordsResponse response = new ListRecordsResponse(request.getParameters(), OffsetDateTime.now(), new Record[]{record}, resumptionToken);

    String rsp = f.createListRecordsResponse(response);
    System.out.println(String.format("%s", rsp));

    assertNotNull("Null response", rsp);
    assertTrue("Invalid response by schema", validate(rsp));

    Document doc = parse(rsp);
    assertNotNull("Null document", doc);
    assertTrue("No root element", test(doc, "count(//OAI-PMH)=1"));
    assertTrue("No response date", test(doc, "count(//OAI-PMH/responseDate)=1"));
    assertTrue("No request", test(doc, "count(//OAI-PMH/request)=1"));
    assertTrue("Invalid verb", test(doc, "//OAI-PMH/request/@verb='ListRecords'"));
    assertTrue("Invalid resumtion token", test(doc, "//OAI-PMH/request/@resumptionToken='token'"));
    assertTrue("No ListRecords node", test(doc, "count(//OAI-PMH/ListRecords)=1"));
    assertTrue("No record nodes", test(doc, "count(//OAI-PMH/ListRecords/record)>0"));
    assertTrue("No header node", test(doc, "count(//OAI-PMH/ListRecords/record[1]/header)=1"));
    assertTrue("No metadata node", test(doc, "count(//OAI-PMH/ListRecords/record[1]/metadata)=1"));
    assertTrue("No metadata content", test(doc, "count(//OAI-PMH/ListRecords/record[1]/metadata/dc)=1"));
    assertTrue("No about node", test(doc, "count(//OAI-PMH/ListRecords/record[1]/about)=1"));
    assertTrue("No about content", test(doc, "count(//OAI-PMH/ListRecords/record[1]/about/rfc1807)=1"));

    assertTrue("No resumptionToken", test(doc, "count(//OAI-PMH/ListRecords/resumptionToken)=1"));
    assertTrue("Invalid resumptionToken", test(doc, "//OAI-PMH/ListRecords/resumptionToken=''"));
    assertTrue("Invalid resumptionToken expiration date", test(doc, "//OAI-PMH/ListRecords/resumptionToken/@expirationDate='" + response.resumptionToken.expirationDate.format(DateTimeFormatter.ISO_DATE_TIME) + "'"));
    assertTrue("Invalid cursor value", test(doc, "//OAI-PMH/ListRecords/resumptionToken/@cursor='0'"));
    assertTrue("Invalid completeListSize value", test(doc, "//OAI-PMH/ListRecords/resumptionToken/@completeListSize='300'"));
  }

  @Test
  public void testGetRecordResponseAsOaiDc() throws Exception {
    GetRecordRequest request = new GetRecordRequest(URI.create("identifier"), "oai_dc");

    Header header = new Header(request.getIdentifier(), OffsetDateTime.now(), new String[]{"music"}, false);

    Record record = new Record(header, oai_dc(), new Document[]{provenance(), rights()});

    GetRecordResponse response = new GetRecordResponse(request.getParameters(), OffsetDateTime.now(), record);

    String rsp = f.createGetRecordResponse(response);
    System.out.println(String.format("%s", rsp));

    assertNotNull("Null response", rsp);
    assertTrue("Invalid response by schema", validate(rsp));

    Document doc = parse(rsp);
    assertNotNull("Null document", doc);
    assertTrue("No root element", test(doc, "count(//OAI-PMH)=1"));
    assertTrue("No response date", test(doc, "count(//OAI-PMH/responseDate)=1"));
    assertTrue("No request", test(doc, "count(//OAI-PMH/request)=1"));
    assertTrue("Invalid verb", test(doc, "//OAI-PMH/request/@verb='GetRecord'"));
    assertTrue("No GetRecord node", test(doc, "count(//OAI-PMH/GetRecord)=1"));
    assertTrue("No record nodes", test(doc, "count(//OAI-PMH/GetRecord/record)=1"));
    assertTrue("No header node", test(doc, "count(//OAI-PMH/GetRecord/record/header)=1"));
    assertTrue("No metadata node", test(doc, "count(//OAI-PMH/GetRecord/record/metadata)=1"));
    assertTrue("No metadata content", test(doc, "count(//OAI-PMH/GetRecord/record/metadata/dc)=1"));
    assertTrue("No about node", test(doc, "count(//OAI-PMH/GetRecord/record/about)>0"));
  }

  @Test
  public void testGetRecordResponseAsRfc1807() throws Exception {
    GetRecordRequest request = new GetRecordRequest(URI.create("identifier"), "rfc1807");

    Header header = new Header(request.getIdentifier(), OffsetDateTime.now(), new String[]{"music"}, false);

    Record record = new Record(header, rfc_1807(), new Document[]{provenance(), rights()});

    GetRecordResponse response = new GetRecordResponse(request.getParameters(), OffsetDateTime.now(), record);

    String rsp = f.createGetRecordResponse(response);
    System.out.println(String.format("%s", rsp));

    assertNotNull("Null response", rsp);
    assertTrue("Invalid response by schema", validate(rsp));

    Document doc = parse(rsp);
    assertNotNull("Null document", doc);
    assertTrue("No root element", test(doc, "count(//OAI-PMH)=1"));
    assertTrue("No response date", test(doc, "count(//OAI-PMH/responseDate)=1"));
    assertTrue("No request", test(doc, "count(//OAI-PMH/request)=1"));
    assertTrue("Invalid verb", test(doc, "//OAI-PMH/request/@verb='GetRecord'"));
    assertTrue("No GetRecord node", test(doc, "count(//OAI-PMH/GetRecord)=1"));
    assertTrue("No record nodes", test(doc, "count(//OAI-PMH/GetRecord/record)=1"));
    assertTrue("No header node", test(doc, "count(//OAI-PMH/GetRecord/record/header)=1"));
    assertTrue("No metadata node", test(doc, "count(//OAI-PMH/GetRecord/record/metadata)=1"));
    assertTrue("No metadata content", test(doc, "count(//OAI-PMH/GetRecord/record/metadata/rfc1807)=1"));
    assertTrue("No about node", test(doc, "count(//OAI-PMH/GetRecord/record/about)>0"));
  }

  private Document parse(String xml) throws IOException, SAXException {
    try (InputStream xmlStream = new ByteArrayInputStream(xml.getBytes("UTF-8"))) {
      return builder.parse(xmlStream);
    }
  }

  private boolean test(Document doc, String expr) throws XPathExpressionException {
    return (Boolean) xpath.evaluate(expr, doc, XPathConstants.BOOLEAN);
  }

  private boolean validate(String xml) {
    try {
      validator.validate(new StreamSource(new ByteArrayInputStream(xml.getBytes("UTF-8"))));

      return true;
    } catch (SAXException | IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}
