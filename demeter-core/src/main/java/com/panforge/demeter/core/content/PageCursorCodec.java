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
package com.panforge.demeter.core.content;

/**
 * Page cursor codec.
 * @param <PC> type of the page cursor
 */
public interface PageCursorCodec<PC extends PageCursor> {
  
  /**
   * Encode page cursor.
   * @param pageCursor page cursor
   * @return encoded page cursor
   */
  String toString(PC pageCursor);
  
  /**
   * Decode page cursor.
   * @param pageCursorStr encoded page cursor
   * @return page cursor
   */
  PC fromString(String pageCursorStr);
}
