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

import com.panforge.demeter.service.Cursor;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Stream cursor.
 * @param <T> type of the data
 */
class StreamCursor<T> implements Cursor<T> {
  private final Stream<T> stream;
  private final long total;

  public StreamCursor(Stream<T> stream, long total) {
    this.stream = stream;
    this.total = total;
  }

  @Override
  public Iterator<T> iterator() {
    return stream.iterator();
  }

  @Override
  public long total() {
    return total;
  }

  @Override
  public void close() {
    stream.close();
  }
  
}
