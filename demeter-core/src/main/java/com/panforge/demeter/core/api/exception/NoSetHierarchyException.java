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
package com.panforge.demeter.core.api.exception;

import com.panforge.demeter.core.model.ErrorCode;

/**
 * No set hierarchy exception.
 */
public final class NoSetHierarchyException extends ProtocolException {

  /**
   * Creates instance of the exception.
   * @param message exception message
   */
  public NoSetHierarchyException(String message) {
    super(ErrorCode.noSetHierarchy, message);
  }
}
