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

import java.util.AbstractList;
import java.util.List;

/**
 * List cursor.
 * @param <T> type of the data accessible by the cursor
 */
class ListCursor<T> extends AbstractList<T> implements Cursor<T> {

  private final List<T> data;

  public ListCursor(List<T> data) {
    this.data = data;
  }

  @Override
  public T get(int index) {
    return data.get(index);
  }

  @Override
  public int size() {
    return data.size();
  }

  @Override
  public long total() {
    return data.size();
  }

  @Override
  public void close() {
  }
  
}
