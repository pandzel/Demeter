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
package com.panforge.demeter.core.model;

import java.util.Arrays;

/**
 * Verb.
 */
public enum Verb {
  GetRecord,
  Identify,
  ListIdentifiers,
  ListMetadataFormats,
  ListRecords,
  ListSets;
  
  /**
   * Parses a verb as string.
   * @param verb verb as string
   * @return verb or <code>null</code> if bad verb
   */
  public static Verb parse(String verb) {
    final String normVerb = verb!=null? verb.toLowerCase(): null;
    return Arrays.stream(values()).filter((Verb v) -> v.name().toLowerCase().equals(normVerb)).findFirst().orElse(null);
  }
}
