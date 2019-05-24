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
package com.panforge.demeter.server.beans;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.panforge.demeter.core.api.exception.CannotDisseminateFormatException;
import com.panforge.demeter.core.api.exception.IdDoesNotExistException;
import com.panforge.demeter.core.api.exception.NoMetadataFormatsException;
import com.panforge.demeter.core.api.exception.NoRecordsMatchException;
import com.panforge.demeter.core.api.exception.NoSetHierarchyException;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.response.elements.Set;
import java.net.URI;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import com.panforge.demeter.core.content.Cursor;
import com.panforge.demeter.server.MetaProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;
import com.panforge.demeter.core.content.ContentProvider;
import com.panforge.demeter.core.content.Filter;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Repository bean.
 */
@Service
public class ContentProviderBean implements ContentProvider {

  private static final Logger LOG = LoggerFactory.getLogger(ContentProviderBean.class);

  @Autowired
  private MetaProcessorService metadataProcessorService;

  private CqlSession session;

  @PostConstruct
  public void construct() {
    session = CqlSession.builder().withKeyspace(CqlIdentifier.fromCql("demeter")).build();
    LOG.info(String.format("%s created.", this.getClass().getSimpleName()));
  }

  @PreDestroy
  public void destroy() {
    if (session != null) {
      session.close();
    }
    LOG.info(String.format("%s destroyed.", this.getClass().getSimpleName()));
  }

  @Override
  public Cursor<MetadataFormat> listMetadataFormats(URI uri) throws IdDoesNotExistException, NoMetadataFormatsException {
    List<MetadataFormat> formats = metadataProcessorService.listMetadataFormats().stream().collect(Collectors.toList());
    if (formats.isEmpty()) {
      throw new NoMetadataFormatsException(uri == null ? String.format("No metadata formats") : String.format("No metadata formats for: %s", uri));
    }
    return Cursor.of(formats);
  }

  @Override
  public Cursor<Set> listSets() throws NoSetHierarchyException {
    ResultSet rs = session.execute("select * from sets");
    int total = rs.getAvailableWithoutFetching();
    return Cursor.of(StreamSupport.stream(rs.spliterator(), false).map(row -> {
      String setSpec = row.getString("setSpec");
      String setName = row.getString("setName");
      return new Set(setSpec, setName, null);
    }), total);
  }

  private Cursor<UUID> listSetsIdsFor(String recordId) {
    ResultSet rs = session.execute("select setId from records_sets where recordId = " + recordId);
    int total = rs.getAvailableWithoutFetching();
    return Cursor.of(StreamSupport.stream(rs.spliterator(), false).map(row -> row.getUuid("setId")), total);
  }

  private Cursor<Set> listSetsFor(String recordId) {
    ResultSet rs = session.execute("select * from sets where id in (" + listSetsIdsFor(recordId).createStream().map(UUID::toString).collect(Collectors.joining(",")) + ")");
    int total = rs.getAvailableWithoutFetching();
    return Cursor.of(StreamSupport.stream(rs.spliterator(), false).map(row -> {
      String setSpec = row.getString("setSpec");
      String setName = row.getString("setName");
      return new Set(setSpec, setName, null);
    }), total);
  }

  @Override
  public Cursor<Header> listHeaders(Filter filter) throws CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
    try {
      if (!StreamSupport.stream(listMetadataFormats(null).spliterator(), false).map(f -> f.metadataPrefix).anyMatch(p -> p.equals(filter.metadataPrefix))) {
        throw new CannotDisseminateFormatException(String.format("Invalid metadata format prefix: '%s'", filter.metadataPrefix));
      }
    } catch (NoMetadataFormatsException | IdDoesNotExistException ex) {
      throw new CannotDisseminateFormatException(String.format("Invalid metadata format prefix: '%s'", filter.metadataPrefix), ex);
    }
    ResultSet rs = session.execute("select id, identifier, date from records");
    int total = rs.getAvailableWithoutFetching();
    return Cursor.of(StreamSupport.stream(rs.spliterator(), false).map(row -> {
      UUID id = row.getUuid("id");
      String identifier = row.getString("identifier");
      LocalDate localDate = row.getLocalDate("date");
      List<String> sets = listSetsFor(id.toString()).createStream().map(s->s.setSpec).collect(Collectors.toList());
      String [] setsArray = !sets.isEmpty()? sets.toArray(new String[sets.size()]): null;
      return new Header(URI.create(identifier), OffsetDateTime.ofInstant(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()), setsArray, false);
    }), total);
  }

  @Override
  public Record readRecord(URI identifier, String metadataPrefix) throws IdDoesNotExistException, CannotDisseminateFormatException {
    throw new IdDoesNotExistException(identifier.toString());
  }
}
