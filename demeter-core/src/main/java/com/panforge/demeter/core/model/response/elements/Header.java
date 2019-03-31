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

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * Header.
 */
public final class Header {
  
  /** identifier */
  public final URI identifier;
  /** date stamp */
  public final OffsetDateTime datestamp;
  /** list of sets */
  public final String[] set;
  /** deleted status */
  public final boolean deleted;

  /**
   * Creates instance of the header.
   * @param identifier identifier
   * @param datestamp datestamp
   * @param set list of set
   * @param deteted deleted status
   */
  public Header(URI identifier, OffsetDateTime datestamp, String[] set, boolean deteted) {
    // TODO: validate arguments
    this.identifier = identifier;
    this.datestamp = datestamp;
    this.set = set;
    this.deleted = deteted;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 13 * hash + Objects.hashCode(this.identifier);
    hash = 13 * hash + Objects.hashCode(this.datestamp);
    hash = 13 * hash + Arrays.deepHashCode(this.set);
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
    final Header other = (Header) obj;
    if (!Objects.equals(this.identifier, other.identifier)) {
      return false;
    }
    if (!Objects.equals(this.datestamp.toLocalDate(), other.datestamp.toLocalDate())) {
      return false;
    }
    if (!Arrays.deepEquals(this.set, other.set)) {
      return false;
    }
    if (this.deleted!=other.deleted) {
      return false;
    }
    return true;
  }
  
}
