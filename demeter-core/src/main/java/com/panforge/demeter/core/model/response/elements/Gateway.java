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

import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.Validate;

/**
 * Gateway.
 */
public final class Gateway {
  public final String source;
  public final URL gatewayDescription;
  public final List<String> gatewayAdmin;
  public final URL gatewayURL;
  public final URL gatewayNotes;

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
}
