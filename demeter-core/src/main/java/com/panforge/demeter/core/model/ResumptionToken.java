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
package com.panforge.demeter.core.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import org.apache.commons.lang3.Validate;

/**
 * Resumption token.
 */
public final class ResumptionToken {
  public final String value;
  public final OffsetDateTime expirationDate;
  public final Long completeListSize;
  public final Long cursor;

  /**
   * Creates instance of the resumption token.
   * @param value value of the token
   * @param expirationDate expiration date
   * @param completeListSize complete list size
   * @param cursor cursor position
   */
  public ResumptionToken(String value, OffsetDateTime expirationDate, Long completeListSize, Long cursor) {
    Validate.notEmpty(value, "Missing token value");
    Validate.notNull(expirationDate, "Missing token expiration date");
    this.value = value;
    this.expirationDate = expirationDate;
    this.completeListSize = completeListSize;
    this.cursor = cursor;
  }
  
  /**
   * Checks if token expired.
   * @return <code>true</code> if token expired.
   */
  public boolean expired() {
    return OffsetDateTime.now().isAfter(expirationDate);
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
    final ResumptionToken other = (ResumptionToken) obj;
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    if (!Objects.equals(this.expirationDate, other.expirationDate)) {
      return false;
    }
    if (!Objects.equals(this.completeListSize, other.completeListSize)) {
      return false;
    }
    if (!Objects.equals(this.cursor, other.cursor)) {
      return false;
    }
    return true;
  }
}
