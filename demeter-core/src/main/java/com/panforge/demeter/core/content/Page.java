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

import java.io.Closeable;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Page of data.
 * @param <T> data type
 */
public interface Page<T> extends Closeable, StreamingIterable<T> {
  
  /**
   * Gets total number of elements.
   * @return total number of elements
   */
  long total();
  
  /**
   * Gets cursor to the next page.
   * @return cursor to the next page or <code>null</code> if no more pages.
   */
  PageCursor nextPageCursor();
  
}
