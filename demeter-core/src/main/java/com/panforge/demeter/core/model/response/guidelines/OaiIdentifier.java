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

import java.net.URI;
import org.apache.commons.lang3.Validate;

/**
 * OAI identifier.
 */
public final class OaiIdentifier {
  /** scheme */
  public final String scheme;
  /** repository identifier */
  public final URI repositoryIdentifier;
  /** delimiter */
  public final String delimiter;
  /** sample implementation */
  public final URI sampleIdentifier;

  /**
   * Creates instance of the OAI identifier.
   * @param scheme scheme (mandatory)
   * @param repositoryIdentifier repository identifier (mandatory)
   * @param delimiter delimiter (mandatory)
   * @param sampleIdentifier  sample identifier (mandatory)
   */
  public OaiIdentifier(String scheme, URI repositoryIdentifier, String delimiter, URI sampleIdentifier) {
    Validate.notBlank(scheme, "Missing scheme");
    Validate.notNull(repositoryIdentifier, "Missiing repository identifier");
    Validate.notBlank(delimiter, "Missing delimiter");
    Validate.notNull(sampleIdentifier, "Missing sample identifier.");
    this.scheme = scheme;
    this.repositoryIdentifier = repositoryIdentifier;
    this.delimiter = delimiter;
    this.sampleIdentifier = sampleIdentifier;
  }

  @Override
  public String toString() {
    return "OaiIdentifier{" + "scheme=" + scheme + ", repositoryIdentifier=" + repositoryIdentifier + ", delimiter=" + delimiter + ", sampleIdentifier=" + sampleIdentifier + '}';
  }
}
