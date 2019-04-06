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
package com.panforge.demeter.core.content;

import com.panforge.demeter.core.api.Config;
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

/**
 * Content provider.
 */
public interface ContentProvider {
  
  /**
   * Gets configuration.
   * @return configuration
   */
  Config getConfig();
  
  /**
   * Lists metadata formats.
   * @param identifier record identifier (optional)
   * @return iterable of metadata formats
   * @throws IdDoesNotExistException if id does not exist
   * @throws NoMetadataFormatsException if no metadata formats
   */
  Cursor<MetadataFormat> listMetadataFormats(URI identifier) throws IdDoesNotExistException, NoMetadataFormatsException;
  
  /**
   * Lists sets.
   * @return iterable of sets
   * @throws NoSetHierarchyException if error iterating sets
   */
  Cursor<Set> listSets() throws NoSetHierarchyException;
  
  /**
   * Lists headers.
   * @param filter filter
   * @return iterable of headers
   * @throws CannotDisseminateFormatException if invalid metadata format
   * @throws NoRecordsMatchException if no records
   * @throws NoSetHierarchyException if set hierarchy not supported
   */
  Cursor<Header> listHeaders(Filter filter) throws CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException;
  
  /**
   * Reads record.
   * @param identifier record identifier
   * @param metadataPrefix metadata prefix
   * @return record
   * @throws IdDoesNotExistException if id does not exist
   * @throws CannotDisseminateFormatException if record not available in a specified format
   */
  Record readRecord(URI identifier, String metadataPrefix) throws IdDoesNotExistException, CannotDisseminateFormatException;
}
