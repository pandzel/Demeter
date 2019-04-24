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
import java.net.URL;
import java.time.OffsetDateTime;
import org.apache.commons.lang3.Validate;

/**
 * Provenance.
 */
public final class Provenance {
  public final OffsetDateTime harvestDate;
  public final boolean altered;
  public final URL baseUrl;
  public final URI identifier;
  public final OffsetDateTime datestamp;
  public final URI metadataNamespace;
  public final Provenance originDescription;

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
}
