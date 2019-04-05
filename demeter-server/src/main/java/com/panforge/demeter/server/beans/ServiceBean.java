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

/**
 * Service bean.
 */
@Service
public class ServiceBean extends com.panforge.demeter.service.Service {
  
  @Autowired 
  public ServiceBean(ContentProvider repo, TokenManager tokenManager, @Value("${batchSize}") int batchSize) {
    super(repo, tokenManager, batchSize);
  }
  
}
