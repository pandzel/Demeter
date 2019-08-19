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

import com.panforge.demeter.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.panforge.demeter.core.content.ContentProvider;
import com.panforge.demeter.server.ConfigService;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service bean.
 */
@Service
public class ServiceBean extends com.panforge.demeter.service.Service<PageCursorImpl> {
  private static final Logger LOG = LoggerFactory.getLogger(ServiceBean.class);

  @Autowired 
  public ServiceBean(ConfigService config, ContentProvider<PageCursorImpl> repo, TokenManager<PageCursorImpl> tokenManager, @Value("${batchSize}") int batchSize) {
    super(config.getConfig(), repo, tokenManager, batchSize);
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
