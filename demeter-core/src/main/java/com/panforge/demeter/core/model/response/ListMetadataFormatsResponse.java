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
package com.panforge.demeter.core.model.response;

import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.model.request.ListMetadataFormatsRequest;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * ListMetadataFormats response.
 */
public final class ListMetadataFormatsResponse extends Response<ListMetadataFormatsRequest> {
  
  /** metadata formats */
  public final MetadataFormat [] metadataFormats;

  /**
   * Creates instance of the response.
   * @param parameters request parameters
   * @param responseDate response date
   * @param metadataFormats metadata formats
   */
  public ListMetadataFormatsResponse(Map<String, String[]> parameters, OffsetDateTime responseDate, MetadataFormat[] metadataFormats) {
    super(parameters, responseDate);
    this.metadataFormats = metadataFormats;
  }
  
}
