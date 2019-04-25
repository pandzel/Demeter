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

import com.panforge.demeter.core.api.Config;
import com.panforge.demeter.core.model.request.IdentifyRequest;
import com.panforge.demeter.core.model.response.guidelines.RepositoryDescription;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Identify response.
 */
public final class IdentifyResponse extends Response<IdentifyRequest> {
  
  /** repository name */
  public final String repositoryName;
  /** base URL */
  public final String baseURL;
  /** protocol version */
  public final String protocolVersion;
  /** admin emails */
  public final String[] adminEmail;
  /** earliest date stamp */
  public final OffsetDateTime earliestDatestamp;
  /** deleted record policy */
  public final Config.Deletion deletedRecord;
  /** granularity policy */
  public final String granularity;
  /** compression policy */
  public final Config.Compression [] compression;
  /** descriptions */
  public final RepositoryDescription [] descriptions;

  /**
   * Creates instance of the response.
   * @param repositoryName repository name
   * @param baseURL base URL
   * @param protocolVersion protocol version
   * @param adminEmail admin emails
   * @param earliestDatestamp earliest date stamp
   * @param deletedRecord deleted record policy
   * @param granularity granularity policy
   * @param compression compression policy
   * @param descriptions descriptions
   * @param responseDate response date
   * @param parameters request parameters
   */
  public IdentifyResponse(Map<String, String[]> parameters,  OffsetDateTime responseDate, String repositoryName, String baseURL, String protocolVersion, String[] adminEmail, OffsetDateTime earliestDatestamp, Config.Deletion deletedRecord, String granularity, Config.Compression [] compression, RepositoryDescription[] descriptions) {
    super(parameters, responseDate);
    this.repositoryName = repositoryName;
    this.baseURL = baseURL;
    this.protocolVersion = protocolVersion;
    this.adminEmail = adminEmail;
    this.earliestDatestamp = earliestDatestamp;
    this.deletedRecord = deletedRecord;
    this.granularity = granularity;
    this.compression = compression;
    this.descriptions = descriptions;
  }
  
  /**
   * Creates identify response form configuration.
   * @param config the configuration
   * @param descriptions descriptions
   * @param responseDate response date
   * @param parameters request parameters
   * @return identify response
   */
  public static IdentifyResponse createFromConfig(Map<String, String[]> parameters,  OffsetDateTime responseDate, Config config, RepositoryDescription[] descriptions) {
    IdentifyResponse response = new IdentifyResponse(
            parameters,
            responseDate,
            config.repositoryName,
            config.baseURL,
            config.protocolVersion,
            config.adminEmail,
            config.earliestDatestamp,
            config.deletedRecord,
            config.granularity,
            config.compression,
            descriptions
    );
    
    return response;
  }
}
