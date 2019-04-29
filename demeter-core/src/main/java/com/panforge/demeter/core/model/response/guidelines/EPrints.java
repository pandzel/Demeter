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
import org.apache.commons.lang3.Validate;

/**
 * EPrints.
 */
public final class EPrints {
  /** content */
  public final EPrintsElement content;
  /** metadata policy */
  public final EPrintsElement metadataPolicy;
  /** data policy */
  public final EPrintsElement dataPolicy;
  /** submission policy */
  public final EPrintsElement submissionPolicy;
  /** comment */
  public final List<String> comment;

  /**
   * Creates instance of EPrints section.
   * @param content content (optional)
   * @param metadataPolicy metadata policy (mandatory)
   * @param dataPolicy data policy (mandatory)
   * @param submissionPolicy submission policy (optional)
   * @param comment comment (optional)
   */
  public EPrints(EPrintsElement content, EPrintsElement metadataPolicy, EPrintsElement dataPolicy, EPrintsElement submissionPolicy, List<String> comment) {
    Validate.notNull(metadataPolicy, "Missing metadata policy.");
    Validate.notNull(dataPolicy, "Missing data policy.");
    this.content = content;
    this.metadataPolicy = metadataPolicy;
    this.dataPolicy = dataPolicy;
    this.submissionPolicy = submissionPolicy;
    this.comment = comment!=null? Collections.unmodifiableList(comment): Collections.emptyList();
  }

  @Override
  public String toString() {
    return "EPrints{" + "content=" + content + ", metadataPolicy=" + metadataPolicy + ", dataPolicy=" + dataPolicy + ", submissionPolicy=" + submissionPolicy + ", comment=" + comment + '}';
  }
}
