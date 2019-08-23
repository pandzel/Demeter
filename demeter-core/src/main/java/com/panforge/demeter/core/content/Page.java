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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Page of data.
 * @param <T> data type
 * @param <PC> page cursor type
 */
public interface Page<T, PC> extends Closeable, StreamingIterable<T> {
  @Override
  public void close();
  
  /**
   * Gets total number of elements.
   * @return total number of elements
   */
  long total();
  
  /**
   * Gets cursor to the next page.
   * @return cursor to the next page or <code>null</code> if no more pages.
   */
  PC nextPageCursor();
  
  static <T,PC> Page<T,PC> of(final List<T> content, long total, final PC nextPageCursor) {
    return new Page<T,PC>() {
      @Override
      public long total() {
        return total;
      }

      @Override
      public PC nextPageCursor() {
        return nextPageCursor;
      }

      @Override
      public void close() {
      }

      @Override
      public Iterator<T> iterator() {
        return content.iterator();
      }
    };
  }
  
  static <T,PC> Page<T,PC> of(final List<T> content) {
    return of(content, content.size(), null);
  }
  
  static <T,PC> Page<T,PC> of(final Stream<T> content, long total, final PC nextPageCursor) {
    return new Page<T, PC>() {
      @Override
      public long total() {
        return total;
      }

      @Override
      public PC nextPageCursor() {
        return nextPageCursor;
      }

      @Override
      public void close() {
      }

      @Override
      public Iterator<T> iterator() {
        return content.iterator();
      }
    };
  }
  
  static <T,PC> Page<T,PC> of(final Stream<T> content, long total) {
    return of(content, total, null);
  }
}
