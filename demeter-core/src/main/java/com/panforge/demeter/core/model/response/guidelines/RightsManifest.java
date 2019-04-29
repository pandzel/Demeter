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

import java.util.Collections;
import java.util.List;

/**
 * Rights manifest.
 */
public final class RightsManifest {
  /** list of rights */
  public final List<Rights> rights;

  /**
   * Creates instance of 'rights manifest' section.
   * @param rights list of rights
   */
  public RightsManifest(List<Rights> rights) {
    this.rights = rights!=null? Collections.unmodifiableList(rights): Collections.emptyList();
  }

  @Override
  public String toString() {
    return "RightsManifest{" + "rights=" + rights + '}';
  }
}
