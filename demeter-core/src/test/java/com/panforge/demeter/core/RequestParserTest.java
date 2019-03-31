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
import com.panforge.demeter.core.api.Context;
import com.panforge.demeter.core.api.RequestParser;
import com.panforge.demeter.core.api.exception.ProtocolException;
import com.panforge.demeter.core.model.Verb;
import com.panforge.demeter.core.model.request.GetRecordRequest;
import com.panforge.demeter.core.model.request.IdentifyRequest;
import com.panforge.demeter.core.model.request.ListIdentifiersRequest;
import com.panforge.demeter.core.model.request.ListMetadataFormatsRequest;
import com.panforge.demeter.core.model.request.ListRecordsRequest;
import com.panforge.demeter.core.model.request.ListSetsRequest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * RequestParser test
 */
public class RequestParserTest {
  private static Config config;
  private static Context ctx;
  private static RequestParser p;

  @BeforeClass
  public static void initialize() {
    config = new Config();
    config.baseURL = "http://localhost/oaipmh";
    ctx = new Context(config);
    p = new RequestParser(ctx);
  }
  
  @Test(expected = ProtocolException.class)
  public void testMissingVerbException() throws ProtocolException {
    p.parse("val=3");
  }
  
  @Test(expected = ProtocolException.class)
  public void testToManyVerbsException() throws ProtocolException {
    p.parse("verb=a&verb=b");
  }
  
  @Test(expected = ProtocolException.class)
  public void testInvalidVerbException() throws ProtocolException {
    p.parse("verb=a");
  }
  
  @Test
  public void testGetRecord() throws ProtocolException {
    GetRecordRequest request = (GetRecordRequest)p.parse("verb=GetRecord&identifier=urn:isbn:096139210x&metadataPrefix=pfx");
    assertEquals("Invalid result", Verb.GetRecord, request.verb);
    assertEquals("Invalid identifier", "urn:isbn:096139210x", request.getIdentifier().toASCIIString());
    assertEquals("Invalid metadataPrefix", "pfx", request.getMetadataPrefix());
  }
  
  @Test(expected = ProtocolException.class)
  public void testGetRecordExceptionMissingIdentifier() throws ProtocolException {
    p.parse("verb=GetRecord&metadataPrefix=pfx");
  }
  
  @Test(expected = ProtocolException.class)
  public void testGetRecordExceptionInvalidIdentifier() throws ProtocolException {
    p.parse("verb=GetRecord&metadataPrefix=pfx&identifier={}");
  }
  
  @Test
  public void testIdentify() throws ProtocolException {
    IdentifyRequest request = (IdentifyRequest)p.parse("verb=Identify");
    assertEquals("Invalid result", Verb.Identify, request.verb);
  }
  
  @Test
  public void testListIdentifiers() throws ProtocolException {
    ListIdentifiersRequest request = (ListIdentifiersRequest)p.parse("verb=ListIdentifiers&metadataPrefix=pfx&from=2001-04-14&until=2019-01-03");
    assertEquals("Invalid result", Verb.ListIdentifiers, request.verb);
    assertEquals("Invalid metadataPrefix", "pfx", request.getMetadataPrefix());
    assertEquals("Invalid from", OffsetDateTime.of(2001, 4, 14, 0, 0, 0, 0, ZoneOffset.UTC), request.getFrom());
    assertEquals("Invalid until", OffsetDateTime.of(2019, 1, 3, 0, 0, 0, 0, ZoneOffset.UTC), request.getUntil());
    
    request = (ListIdentifiersRequest)p.parse("verb=ListIdentifiers&resumptionToken=token");
    assertEquals("Invalid resumptionToken", "token", request.getResumptionToken());
  }
  
  @Test(expected = ProtocolException.class)
  public void testListIdentifiersExceptionExtraResumptionToken() throws ProtocolException {
    p.parse("verb=ListIdentifiers&metadataPrefix=pfx&from=2001-04-14&until=2019-01-03&resumptionToken=token");
  }
  
  @Test(expected = ProtocolException.class)
  public void testListIdentifiersExceptionMissingPrefix() throws ProtocolException {
    p.parse("verb=ListIdentifiers&from=2001-04-14&until=2019-01-03");
  }
  
  @Test
  public void testListRecords() throws ProtocolException {
    ListRecordsRequest request = (ListRecordsRequest)p.parse("verb=ListRecords&metadataPrefix=pfx&from=2001-04-14&until=2019-01-03");
    assertEquals("Invalid result", Verb.ListRecords, request.verb);
    assertEquals("Invalid metadataPrefix", "pfx", request.getMetadataPrefix());
    assertEquals("Invalid from", OffsetDateTime.of(2001, 4, 14, 0, 0, 0, 0, ZoneOffset.UTC), request.getFrom());
    assertEquals("Invalid until", OffsetDateTime.of(2019, 1, 3, 0, 0, 0, 0, ZoneOffset.UTC), request.getUntil());
    
    request = (ListRecordsRequest)p.parse("verb=ListRecords&resumptionToken=token");
    assertEquals("Invalid resumptionToken", "token", request.getResumptionToken());
  }
  
  @Test(expected = ProtocolException.class)
  public void testListRecordsExceptionExtraToken() throws ProtocolException {
    p.parse("verb=ListRecords&metadataPrefix=pfx&from=2001-04-14&until=2019-01-03&resumptionToken=token");
  }
  
  @Test(expected = ProtocolException.class)
  public void testListRecordsExceptionMissingPrefix() throws ProtocolException {
    p.parse("verb=ListRecords&from=2001-04-14&until=2019-01-03");
  }
  
  @Test
  public void testListMetadataFormats() throws ProtocolException {
    ListMetadataFormatsRequest request = (ListMetadataFormatsRequest)p.parse("verb=ListMetadataFormats&identifier=urn:isbn:096139210x");
    assertEquals("Invalid result", Verb.ListMetadataFormats, request.verb);
    assertEquals("Invalid identifier", "urn:isbn:096139210x", request.getIdentifier().toASCIIString());
  }
  
  @Test(expected = ProtocolException.class)
  public void testListMetadataFormatsExceptionInvalidId() throws ProtocolException {
    p.parse("verb=ListMetadataFormats&identifier={}");
  }
  
  @Test
  public void testListSets() throws ProtocolException {
    ListSetsRequest request = (ListSetsRequest)p.parse("verb=ListSets&resumptionToken=token");
    assertEquals("Invalid result", Verb.ListSets, request.verb);
    assertEquals("Invalid resumptionToken", "token", request.getResumptionToken());
  }
}
