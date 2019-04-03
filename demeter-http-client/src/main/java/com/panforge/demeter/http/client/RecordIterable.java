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
package com.panforge.demeter.http.client;

import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.api.exception.ProtocolException;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.elements.Record;
import java.util.Iterator;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Record iterable.
 */
class RecordIterable implements Iterable<Record> {
  private final Client client;
  private final String metadataPrefix;
  private final CrawlContext context;
  private final Predicate<Header> pred;
  private final BooleanSupplier cont;

  public RecordIterable(Client client, String metadataPrefix, CrawlContext context, Predicate<Header> pred, BooleanSupplier cont) {
    this.client = client;
    this.metadataPrefix = metadataPrefix;
    this.context = context;
    this.pred = pred;
    this.cont = cont;
  }

  @Override
  public Iterator<Record> iterator() {
    return new RecordIterator(client, metadataPrefix, context, pred, cont) {
      @Override
      protected void onError(ErrorInfo [] errorInfos) {
        RecordIterable.this.onError(errorInfos);
      }
    };
  }
  
  /**
   * Enables custom error handling.
   * @param errorInfos error infos
   */
  protected void onError(ErrorInfo [] errorInfos) {}

  
}
