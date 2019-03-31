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
package com.panforge.demeter.service;

import java.io.Closeable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Cursor.
 * @param <T> type of the data accessible by the cursor
 */
public interface Cursor<T> extends Closeable, Iterable<T> {
  @Override
  public void close();
  
  /**
   * Gets total number of elements.
   * @return total number of elements
   */
  public long total();
  
  /**
   * Creates instance of the cursor from the list.
   * @param <T> type of data
   * @param data data
   * @return instance
   */
  public static <T> Cursor<T> of(List<T> data) {
    return new ListCursor<>(data);
  }
  
  /**
   * Creates instance of the cursor from an array.
   * @param <T> type of data
   * @param data data
   * @return instance
   */
  public static <T> Cursor<T> of(T [] data) {
    return new ListCursor<>(Arrays.asList(data));
  }
  
  /**
   * Creates instance of the cursor from a stream.
   * @param <T> type of data
   * @param data data
   * @param total total number of elements
   * @return instance
   */
  public static <T> Cursor<T> of(Stream<T> data, long total) {
    return new StreamCursor<>(data, total);
  }
  
}
