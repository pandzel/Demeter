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
package com.panforge.demeter.server;

import com.panforge.demeter.core.content.Filter;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import java.io.File;
import java.net.URI;
import java.time.OffsetDateTime;
import org.apache.commons.lang3.Validate;

/**
 * Metadata descriptor.
 */
public class MetaDescriptor {
  public final MetaProcessor mp;
  public final File source;
  public final URI uri;
  public final MetadataFormat format;
  public final OffsetDateTime datestamp;

  /**
   * Creates instance of the descriptor.
   * @param mp metadata processor
   * @param source metadata source
   * @param uri metadata URI
   * @param format metadata format
   * @param datestamp datestamp
   */
  public MetaDescriptor(MetaProcessor mp, File source, URI uri, MetadataFormat format, OffsetDateTime datestamp) {
    Validate.notNull(mp, "Missing metadata processor");
    Validate.notNull(source, "Missing source");
    Validate.notNull(uri, "Missing metadata URI");
    Validate.notNull(format, "Missing metadata format");
    Validate.notNull(datestamp, "Missing datestamp");
    
    this.mp = mp;
    this.source = source;
    this.uri = uri;
    this.format = format;
    this.datestamp = datestamp;
  }
  
  /**
   * Checks if descriptor matches filter
   * @param filter filter
   * @return <code>true</code> if descriptor matches filter
   */
  public boolean matches(Filter filter) {
    if (filter==null) return true;
    if (filter.from!=null && datestamp.isBefore(filter.from)) return false;
    if (filter.until!=null && datestamp.isAfter(filter.until)) return false;
    if (filter.metadataPrefix!=null && !format.metadataPrefix.equals(filter.metadataPrefix)) return false;
    return true;
  }
  
  /**
   * Gets header.
   * @return header.
   */
  public Header toHeader() {
    return new Header(uri, datestamp, new String[]{}, false);
  }
}
