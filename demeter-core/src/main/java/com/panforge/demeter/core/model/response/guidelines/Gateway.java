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

import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.Validate;

/**
 * Gateway.
 */
public final class Gateway {
  /** source */
  public final String source;
  /** gateway description */
  public final URL gatewayDescription;
  /** list of admins emails */
  public final List<String> gatewayAdmin;
  /** gateway URL */
  public final URL gatewayURL;
  /** gateway notes */
  public final URL gatewayNotes;

  /**
   * Creates instance of the 'gateway' section.
   * @param source source (mandatory)
   * @param gatewayDescription gateway description (mandatory)
   * @param gatewayAdmin admin emails (at least one)
   * @param gatewayURL gateway URL (optional)
   * @param gatewayNotes gateway notes (optional)
   */
  public Gateway(String source, URL gatewayDescription, List<String> gatewayAdmin, URL gatewayURL, URL gatewayNotes) {
    Validate.notBlank(source, "Missing source.");
    Validate.notNull(gatewayDescription, "Missing gateway description.");
    Validate.notEmpty(gatewayAdmin, "Missing gateway admin.");
    this.source = source;
    this.gatewayDescription = gatewayDescription;
    this.gatewayAdmin = Collections.unmodifiableList(gatewayAdmin);
    this.gatewayURL = gatewayURL;
    this.gatewayNotes = gatewayNotes;
  }

  @Override
  public String toString() {
    return "Gateway{" + "source=" + source + ", gatewayDescription=" + gatewayDescription + ", gatewayAdmin=" + gatewayAdmin + ", gatewayURL=" + gatewayURL + ", gatewayNotes=" + gatewayNotes + '}';
  }
}
