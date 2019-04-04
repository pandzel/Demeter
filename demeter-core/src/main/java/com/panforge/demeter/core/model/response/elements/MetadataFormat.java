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
package com.panforge.demeter.core.model.response.elements;

import java.util.Objects;
import org.apache.commons.lang3.Validate;

/**
 * Metadata format.
 */
public final class MetadataFormat {
  
  /** metadata prefix */
  public final String metadataPrefix;
  /** schema name */
  public final String schema;
  /** namespace */
  public final String metadataNamespace;

  /**
   * Creates instance of the metadata format.
   * @param metadataPrefix metadata prefix
   * @param schema schema name
   * @param metadataNamespace namespace
   */
  public MetadataFormat(String metadataPrefix, String schema, String metadataNamespace) {
    Validate.notBlank(metadataPrefix, "Missing metadata prefix");
    Validate.notBlank(schema, "Missing schema");
    Validate.notBlank(metadataNamespace, "Missing metadata namespace");
    this.metadataPrefix = metadataPrefix;
    this.schema = schema;
    this.metadataNamespace = metadataNamespace;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MetadataFormat other = (MetadataFormat) obj;
    if (!Objects.equals(this.metadataPrefix, other.metadataPrefix)) {
      return false;
    }
    if (!Objects.equals(this.schema, other.schema)) {
      return false;
    }
    if (!Objects.equals(this.metadataNamespace, other.metadataNamespace)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 19 * hash + Objects.hashCode(this.metadataPrefix);
    hash = 19 * hash + Objects.hashCode(this.schema);
    hash = 19 * hash + Objects.hashCode(this.metadataNamespace);
    return hash;
  }
  
}
