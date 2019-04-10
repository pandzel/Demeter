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
package com.panforge.demeter.service;

import com.panforge.demeter.core.api.exception.CannotDisseminateFormatException;
import com.panforge.demeter.core.api.exception.IdDoesNotExistException;
import com.panforge.demeter.core.api.exception.NoMetadataFormatsException;
import com.panforge.demeter.core.api.exception.NoRecordsMatchException;
import com.panforge.demeter.core.api.exception.NoSetHierarchyException;
import com.panforge.demeter.core.content.ContentProvider;
import com.panforge.demeter.core.content.Cursor;
import com.panforge.demeter.core.content.Filter;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.response.elements.Set;
import java.net.URI;

/**
 * Mockup content provider.
 */
public class MockupContentProvider implements ContentProvider {

  private final static MetadataFormat oai_dc = new MetadataFormat(
          "oai_dc",
          "http://www.openarchives.org/OAI/2.0/oai_dc.xsd",
          "http://www.openarchives.org/OAI/2.0/oai_dc/"
  );
  
  private final static Set mainSet = new Set("main", "Main set");

  @Override
  public Cursor<MetadataFormat> listMetadataFormats(URI identifier) throws IdDoesNotExistException, NoMetadataFormatsException {
    return Cursor.of(new MetadataFormat[]{oai_dc});
  }

  @Override
  public Cursor<Set> listSets() throws NoSetHierarchyException {
    return Cursor.of(new Set[]{mainSet});
  }

  @Override
  public Cursor<Header> listHeaders(Filter filter) throws CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
    return Cursor.of(new Header[0]);
  }

  @Override
  public Record readRecord(URI identifier, String metadataPrefix) throws IdDoesNotExistException, CannotDisseminateFormatException {
    throw new IdDoesNotExistException("");
  }
  
}
