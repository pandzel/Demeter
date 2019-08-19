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
package com.panforge.demeter.server.beans;

import com.panforge.demeter.core.content.PageCursorCodec;
import com.panforge.demeter.service.SimpleTokenManager;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Token manager bean.
 */
@Service
public class TokenManagerBean extends SimpleTokenManager<PageCursorByPageNumber> {
  private static final Logger LOG = LoggerFactory.getLogger(TokenManagerBean.class);
  private final PageCursorCodec<PageCursorByPageNumber> codec;

  @Autowired
  public TokenManagerBean(PageCursorCodec<PageCursorByPageNumber> codec, @Value("${tokenExpiration}") long expiration) {
    super(expiration);
    this.codec = codec;
  }
  
  @PostConstruct
  public void construct() {
    LOG.info(String.format("%s created.", this.getClass().getSimpleName()));
  }
  
  @PreDestroy
  public void destroy() {
    LOG.info(String.format("%s destroyed.", this.getClass().getSimpleName()));
  }
}
