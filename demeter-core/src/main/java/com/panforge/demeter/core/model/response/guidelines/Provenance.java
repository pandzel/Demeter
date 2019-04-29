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
import java.net.URL;
import java.time.OffsetDateTime;
import org.apache.commons.lang3.Validate;

/**
 * Provenance.
 */
public final class Provenance {
  /** harvest date */
  public final OffsetDateTime harvestDate;
  /** altered indicator */
  public final boolean altered;
  /** base URL */
  public final URL baseUrl;
  /** identifier */
  public final URI identifier;
  /** date stamp */
  public final OffsetDateTime datestamp;
  /** metadata namespace */
  public final URI metadataNamespace;
  /** origin description */
  public final Provenance originDescription;

  /**
   * Creates instance of 'provenance' section.
   * @param harvestDate harvest date (mandatory)
   * @param altered <code>true</code> if altered
   * @param baseUrl base URL (optional)
   * @param identifier identifier (mandatory)
   * @param datestamp date stamp (mandatory)
   * @param metadataNamespace metadata namespace (mandatory)
   * @param originDescription  origin description (optional)
   */
  public Provenance(OffsetDateTime harvestDate, boolean altered, URL baseUrl, URI identifier, OffsetDateTime datestamp, URI metadataNamespace, Provenance originDescription) {
    Validate.notNull(harvestDate, "Missing harvest date");
    Validate.notNull(baseUrl, "Missing base URL");
    Validate.notNull(identifier, "Missing identifier.");
    Validate.notNull(datestamp, "Missing datestamp.");
    Validate.notNull(metadataNamespace, "Missing metadataNamespace.");
    this.harvestDate = harvestDate;
    this.altered = altered;
    this.baseUrl = baseUrl;
    this.identifier = identifier;
    this.datestamp = datestamp;
    this.metadataNamespace = metadataNamespace;
    this.originDescription = originDescription;
  }

  @Override
  public String toString() {
    return "Provenance{" + "harvestDate=" + harvestDate + ", altered=" + altered + ", baseUrl=" + baseUrl + ", identifier=" + identifier + ", datestamp=" + datestamp + ", metadataNamespace=" + metadataNamespace + ", originDescription=" + originDescription + '}';
  }
}
