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

import com.panforge.demeter.core.api.exception.BadArgumentException;
import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.api.exception.ProtocolException;
import com.panforge.demeter.core.model.request.ListIdentifiersRequest;
import com.panforge.demeter.core.model.response.ListIdentifiersResponse;
import com.panforge.demeter.core.model.response.elements.Header;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

/**
 * Header iterator.
 */
class HeaderIterator implements Iterator<Header> {

  private final Client client;
  private final String metadataPrefix;
  private final CrawlContext context;
  private final Predicate<Header> pred;
  private final BooleanSupplier cont;

  private Iterator<Header> iter;
  private String resumptionToken;
  private boolean lastBatch;

  public HeaderIterator(Client client, String metadataPrefix, CrawlContext context, Predicate<Header> pred, BooleanSupplier cont) {
    this.client = client;
    this.metadataPrefix = metadataPrefix;
    this.context = context;
    this.pred = pred != null ? pred : (h) -> true;
    this.cont = cont != null ? cont : () -> true;
  }

  /**
   * Enables custom error handling.
   *
   * @param ex exception
   */
  protected void onError(ErrorInfo[] errorInfos) {
  }

  @Override
  public boolean hasNext() {
    if (!cont.getAsBoolean()) {
      return false;
    }

    if (iter != null && iter.hasNext()) {
      return true;
    }

    if (lastBatch) {
      return false;
    }

    Header[] headers = fetchHeaders();
    if (headers == null) {
      return false;
    }

    iter = Arrays.asList(headers).stream().filter(pred).iterator();

    return hasNext();
  }

  @Override
  public Header next() {
    if (iter == null || !iter.hasNext()) {
      throw new NoSuchElementException();
    }
    return iter.next();
  }

  private Header[] fetchHeaders() {
    try {
      ListIdentifiersResponse response = (ListIdentifiersResponse) client.execute(makeRequestObject());
      if (response.resumptionToken != null && !StringUtils.isBlank(response.resumptionToken.value)) {
        resumptionToken = response.resumptionToken.value;
      } else {
        resumptionToken = null;
        lastBatch = true;
      }
      return response.headers;
    } catch (IOException | URISyntaxException | SAXException ex) {
      handleError(new BadArgumentException(String.format("Error accessing service"), ex));
      lastBatch = true;
      return null;
    } catch (ProtocolException ex) {
      handleError(ex);
      lastBatch = true;
      return null;
    }
  }

  private ListIdentifiersRequest makeRequestObject() {
    return resumptionToken == null
            ? new ListIdentifiersRequest(metadataPrefix, getFrom(), getUntil(), getSet())
            : ListIdentifiersRequest.resume(resumptionToken);
  }

  private void handleError(ProtocolException ex) {
    onError(ex.infos);
  }

  private OffsetDateTime getFrom() {
    return context != null ? context.from : null;
  }

  private OffsetDateTime getUntil() {
    return context != null ? context.until : null;
  }

  private String getSet() {
    return context != null ? context.set : null;
  }
}
