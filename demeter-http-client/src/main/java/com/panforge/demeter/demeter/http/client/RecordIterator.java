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

import com.panforge.demeter.core.api.exception.BadArgumentException;
import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.api.exception.ProtocolException;
import com.panforge.demeter.core.model.request.GetRecordRequest;
import com.panforge.demeter.core.model.response.GetRecordResponse;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.elements.Record;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Record iterator.
 */
class RecordIterator implements Iterator<Record> {

  private final Client client;
  private final String metadataPrefix;

  private final HeaderIterator iter;
  private Record record;

  public RecordIterator(Client client, String metadataPrefix, CrawlContext context, Predicate<Header> pred, BooleanSupplier cont) {
    this.client = client;
    this.metadataPrefix = metadataPrefix;
    this.iter = new HeaderIterator(client, metadataPrefix, context, pred, cont) {
      @Override
      protected void onError(ErrorInfo[] errorInfos) {
        RecordIterator.this.onError(errorInfos);
      }
    };
  }

  /**
   * Enables custom error handling.
   *
   * @param errorInfos error infos
   */
  protected void onError(ErrorInfo[] errorInfos) {
  }

  @Override
  public boolean hasNext() {
    if (!iter.hasNext()) {
      return false;
    }

    Header header = iter.next();
    if (header == null || header.deleted || header.identifier == null) {
      return hasNext();
    }

    record = fetchRecord(header.identifier);

    if (record == null) {
      return hasNext();
    }

    return true;
  }

  @Override
  public Record next() {
    if (record == null) {
      throw new NoSuchElementException();
    }
    return record;
  }

  private Record fetchRecord(URI identifier) {
    try {
      GetRecordRequest request = makeRequestObject(identifier);
      GetRecordResponse response = (GetRecordResponse) client.execute(request);
      return response.record;
    } catch (IOException | URISyntaxException ex) {
      handleError(new BadArgumentException(String.format("Error accessing service"), ex));
      return null;
    } catch (ProtocolException ex) {
      handleError(ex);
      return null;
    }
  }

  private GetRecordRequest makeRequestObject(URI identifier) {
    GetRecordRequest request = new GetRecordRequest(identifier, metadataPrefix);
    return request;
  }

  private void handleError(ProtocolException ex) {
    onError(ex.infos);
  }
}
