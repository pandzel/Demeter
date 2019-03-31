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
package com.panforge.demeter.demeter.http.client;

import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.elements.Record;
import java.io.Closeable;
import java.io.IOException;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Crawler.
 */
public class Crawler implements Closeable {
  private final Client client;
  private final RecordIterable iter;

  /**
   * Creates instance of the crawler.
   * @param client OAI-PMH client
   * @param metadataPrefix metadata prefix
   * @param context crawl context
   * @param pred header predicate
   * @param cont crawl continuity indicator
   */
  public Crawler(Client client, String metadataPrefix, CrawlContext context, Predicate<Header> pred, BooleanSupplier cont) {
    this.client = client;
    this.iter = new RecordIterable(client, metadataPrefix, context, pred, cont) {
      @Override
      protected void onError(ErrorInfo [] errorInfo) {
        Crawler.this.onError(errorInfo);
      }
    };
  }

  /**
   * Creates instance of the crawler.
   * @param client OAI-PMH client
   * @param metadataPrefix metadata prefix
   */
  public Crawler(Client client, String metadataPrefix) {
    this.client = client;
    this.iter = new RecordIterable(client, metadataPrefix, null, null, null) {
      @Override
      protected void onError(ErrorInfo [] errorInfo) {
        Crawler.this.onError(errorInfo);
      }
    };
  }
  
  /**
   * Gets stream of records.
   * @return stream of records
   */
  public Stream<Record> stream() {
    return StreamSupport.stream(iter.spliterator(), true);
  }

  @Override
  public void close() throws IOException {
    if (client!=null) {
      client.close();
    }
  }
  
  /**
   * Enables custom error handling.
   * @param errorInfo error info
   */
  protected void onError(ErrorInfo [] errorInfo) {}
  
}
