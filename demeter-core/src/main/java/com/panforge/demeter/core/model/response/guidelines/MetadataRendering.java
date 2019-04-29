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
 * Metadata rendering.
 */
public final class MetadataRendering {
  /** metadata namespace */
  public final String metadataNamespace;
  /** mime type */
  public final String mimeType;

  /**
   * Creates instance of 'metadata rendering' section.
   * @param metadataNamespace metadata namespaces (mandatory)
   * @param mimeType mime type (mandatory)
   */
  public MetadataRendering(String metadataNamespace, String mimeType) {
    Validate.notBlank(metadataNamespace, "Missing metadata namespace.");
    Validate.notBlank(mimeType, "Missing mime type.");
    this.metadataNamespace = metadataNamespace;
    this.mimeType = mimeType;
  }

  @Override
  public String toString() {
    return "MetadataRendering{" + "metadataNamespace=" + metadataNamespace + ", mimeType=" + mimeType + '}';
  }
  
}
