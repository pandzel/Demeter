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
import org.w3c.dom.Document;

/**
 * Set description.
 */
public class SetDescription {
  public final RightsManifest rightsManifest;
  public final Branding branding;

  public SetDescription(RightsManifest rightsManifest) {
    Validate.notNull(rightsManifest, "Missing rightsManifest.");
    this.rightsManifest = rightsManifest;
    this.branding = null;
  }

  public SetDescription(Branding branding) {
    Validate.notNull(branding, "Missing branding.");
    this.rightsManifest = null;
    this.branding = branding;
  }
  
  public Document createDocument() {
    // TODO: genereta description document.
    return null;
  }
}
