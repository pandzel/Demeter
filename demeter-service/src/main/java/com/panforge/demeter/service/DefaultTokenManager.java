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
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Supplier;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.lang3.StringUtils;

/**
 * Default token manager.
 */
public class DefaultTokenManager implements TokenManager {
  private final PassiveExpiringMap<String, TokenEntry> tokens;
  private final long expiration;

  public DefaultTokenManager(long expiration) {
    this.expiration = expiration;
    this.tokens = new PassiveExpiringMap<>(expiration);
  }

  @Override
  public synchronized ResumptionToken register(Supplier<String> supplier, long completeListSize, long cursor) {
    OffsetDateTime now = OffsetDateTime.now();
    
    String tokenValue = UUID.randomUUID().toString();
    ResumptionToken resumptionToken = new ResumptionToken(tokenValue, now.plus(expiration, ChronoUnit.MILLIS), completeListSize, cursor);
    TokenEntry tokenEntry = new TokenEntry(resumptionToken, supplier);
    tokens.put(tokenValue, tokenEntry);
    
    return resumptionToken;
  }

  @Override
  public synchronized String invoke(String token) throws BadResumptionTokenException {
    TokenEntry tokenEntry = tokens.get(token);
    if (tokenEntry==null) {
      throw new BadResumptionTokenException(String.format("Invalid token: '%s'", StringUtils.trimToEmpty(token)));
    }
    return tokenEntry.supplier.get();
  }
  
  private class TokenEntry {
    public final ResumptionToken resumptionToken;
    public final Supplier<String> supplier;

    public TokenEntry(ResumptionToken resumptionToken, Supplier<String> supplier) {
      this.resumptionToken = resumptionToken;
      this.supplier = supplier;
    }
    
  }
  
}
