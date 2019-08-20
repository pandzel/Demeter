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

import com.panforge.demeter.core.model.ResumptionToken;
import com.panforge.demeter.core.api.exception.BadResumptionTokenException;
import com.panforge.demeter.core.content.PageCursor;
import com.panforge.demeter.core.content.PageCursorCodec;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.lang3.StringUtils;

/**
 * Simple token manager.
 * @param <PC> page cursor type
 */
public class SimpleTokenManager<PC extends PageCursor> implements TokenManager<PC> {
  public static final long DEFAULT_EXPIRATION = 60000;
  private final PassiveExpiringMap<String, String> tokens;
  
  private final PageCursorCodec<PC> codec;
  private final long expiration;

  /**
   * Creates instance of the default token manager.
   * @param expiration expiration time (in milliseconds) of the token
   */
  public SimpleTokenManager(PageCursorCodec<PC> codec, long expiration) {
    this.codec = codec;
    this.expiration = expiration;
    this.tokens = new PassiveExpiringMap<>(expiration);
  }

  @Override
  public ResumptionToken put(PC pageCursor, long total) {
    OffsetDateTime now = OffsetDateTime.now();
    String tokenValue = UUID.randomUUID().toString();
    String pcString = codec.toString(pageCursor);
    tokens.put(tokenValue, pcString);
    ResumptionToken resumptionToken = new ResumptionToken(tokenValue, now.plus(expiration, ChronoUnit.MILLIS), total, pageCursor.cursor());
    return resumptionToken;
  }

  @Override
  public PC pull(String tokenId) throws BadResumptionTokenException {
    String pcString = tokens.get(tokenId);
    if (pcString==null) {
      throw new BadResumptionTokenException(String.format("Invalid token: '%s'", StringUtils.trimToEmpty(tokenId)));
    }
    PC pageCursor = codec.fromString(pcString);
    return pageCursor;
  }
}
