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
 * Set.
 */
public final class Set {
  
  /** set specification */
  public final String setSpec;
  /** set name */
  public final String setName;

  /**
   * Creates instance of the set.
   * @param setSpec set specification
   * @param setName set name
   */
  public Set(String setSpec, String setName) {
    Validate.notBlank(setSpec, "Missing set specification");
    Validate.notBlank(setName, "Missing set name");
    this.setSpec = setSpec;
    this.setName = setName;
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
    final Set other = (Set) obj;
    if (!Objects.equals(this.setSpec, other.setSpec)) {
      return false;
    }
    if (!Objects.equals(this.setName, other.setName)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 83 * hash + Objects.hashCode(this.setSpec);
    hash = 83 * hash + Objects.hashCode(this.setName);
    return hash;
  }
  
}
