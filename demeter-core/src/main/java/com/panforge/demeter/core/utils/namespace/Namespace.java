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
package com.panforge.demeter.core.utils.namespace;

import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import java.util.Objects;
import org.apache.commons.lang3.Validate;

/**
 * Name space.
 */
public class Namespace {
  /** name space */
  public final String namespace;
  /** schema name */
  public final String schema;

  /**
   * Creates instance of the name space.
   * @param namespace
   * @param schema 
   */
  public Namespace(String namespace, String schema) {
    Validate.notBlank(namespace, "Missing namespace");
    Validate.notBlank(namespace, "Missing schema");
    this.namespace = namespace;
    this.schema = schema;
  }
  
  /**
   * Gets metadata format.
   * @param prefix prefix
   * @return metadata format
   */
  public MetadataFormat toMetadataFormat(String prefix) {
    return new MetadataFormat(prefix, schema, namespace);
  }
  
  /**
   * Gets schema location string.
   * @return schema location string
   */
  public String toSchemaLocation() {
    return String.format("%s %s", namespace, schema);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 17 * hash + Objects.hashCode(this.namespace);
    hash = 17 * hash + Objects.hashCode(this.schema);
    return hash;
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
    final Namespace other = (Namespace) obj;
    if (!Objects.equals(this.namespace, other.namespace)) {
      return false;
    }
    if (!Objects.equals(this.schema, other.schema)) {
      return false;
    }
    return true;
  }
  
  
}
