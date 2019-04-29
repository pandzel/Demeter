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
package com.panforge.demeter.core.model.response.guidelines;

import org.apache.commons.lang3.Validate;

/**
 * About record information.
 */
public final class About {
  /** provenance */
  public final Provenance provenance;
  /** rights */
  public final Rights rights;

  /**
   * Creates instance of the 'About' section.
   * @param provenance provenance
   * @param rights rights
   */
  public About(Provenance provenance, Rights rights) {
    Validate.isTrue(provenance!=null || rights!=null, "Either provenance or rights (or both) must be present.");
    this.provenance = provenance;
    this.rights = rights;
  }

  @Override
  public String toString() {
    return "About{" + "provenance=" + provenance + ", rights=" + rights + '}';
  }
  
}
