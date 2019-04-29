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
  /** rights manifest */
  public final RightsManifest rightsManifest;
  /** branding */
  public final Branding branding;

  /**
   * Creates instance of 'set description' section.
   * @param rightsManifest rights manifest (mandatory)
   */
  public SetDescription(RightsManifest rightsManifest) {
    Validate.notNull(rightsManifest, "Missing rightsManifest.");
    this.rightsManifest = rightsManifest;
    this.branding = null;
  }

  /**
   * Creates instance of 'set description' section.
   * @param branding branding (mandatory)
   */
  public SetDescription(Branding branding) {
    Validate.notNull(branding, "Missing branding.");
    this.rightsManifest = null;
    this.branding = branding;
  }
  
  /**
   * Creates XML document.
   * @return document
   */
  public Document createDocument() {
    // TODO: genereta description document.
    return null;
  }

  @Override
  public String toString() {
    return "SetDescription{" + "rightsManifest=" + rightsManifest + ", branding=" + branding + '}';
  }
}
