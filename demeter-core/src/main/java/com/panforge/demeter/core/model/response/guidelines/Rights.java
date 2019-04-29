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

import com.panforge.demeter.core.utils.XmlUtils;
import java.net.URL;
import org.apache.commons.lang3.Validate;
import org.w3c.dom.Document;

/**
 * Rights.
 */
public final class Rights {
  /** rights definition */
  public final Document rightsDefinition;
  /** rights reference */
  public final URL rightsReference;

  /**
   * Creates instance of 'rights' section.
   * @param rightsDefinition rights definition (mandatory)
   */
  public Rights(Document rightsDefinition) {
    Validate.notNull(rightsDefinition, "Missing rights definition");
    this.rightsDefinition = rightsDefinition;
    this.rightsReference = null;
  }

  /**
   * Creates instance of 'rights' section.
   * @param rightsReference rights reference (mandatory)
   */
  public Rights(URL rightsReference) {
    Validate.notNull(rightsReference, "Missing rights reference");
    this.rightsDefinition = null;
    this.rightsReference = rightsReference;
  }

  @Override
  public String toString() {
    return "Rights{" + "rightsDefinition=" + (rightsDefinition!=null? XmlUtils.formatToString(rightsDefinition): null) + ", rightsReference=" + rightsReference + '}';
  }
  
}
