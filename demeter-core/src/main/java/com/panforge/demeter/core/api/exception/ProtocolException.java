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
import com.panforge.demeter.core.model.ErrorInfo;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Protocol exception.
 */
public abstract class ProtocolException extends Exception {
  
  /** array of all error information's recorded by an exception */
  public final ErrorInfo[] infos;

  /**
   * Constructs an instance of <code>RequestException</code> with the specified
   * detail message.
   *
   * @param errorCode error code
   * @param message message
   */
  ProtocolException(ErrorCode errorCode, String message) {
    this(new ErrorInfo(errorCode, message));
  }

  /**
   * Constructs an instance of <code>RequestException</code> with the specified
   * detail message.
   *
   * @param errorCode error code
   * @param message message
   * @param cause cause
   */
  ProtocolException(ErrorCode errorCode, String message, Throwable cause) {
    this(cause, new ErrorInfo(errorCode, message));
  }

  /**
   * Constructs an instance of <code>RequestException</code> with the specified
   * detail message.
   *
   * @param infos error information
   */
  ProtocolException(ErrorInfo...infos) {
    super(Arrays.stream(infos).map(ei->ei.message).collect(Collectors.joining("; ")));
    this.infos = infos;
  }

  /**
   * Constructs an instance of <code>RequestException</code> with the specified
   * detail message.
   *
   * @param infos error information
   * @param cause cause
   */
  ProtocolException(Throwable cause, ErrorInfo...infos) {
    super(Arrays.stream(infos).map(ei->ei.message).collect(Collectors.joining("; ")), cause);
    this.infos = infos;
  }
}
