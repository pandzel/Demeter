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
package com.panforge.demeter.server;

import com.panforge.demeter.core.content.ContentProvider;
import com.panforge.demeter.core.content.Filter;
import com.panforge.demeter.core.content.Page;
import com.panforge.demeter.core.model.response.elements.Header;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * View controller.
 */
public class ViewController extends AbstractController {
  
  @Autowired
  private RootFolderService rootFolderService;
  
  @Autowired
  private ConfigService configService;
  
  @Autowired
  private ContentProvider contentProvider;
  
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response) throws Exception {

		ModelAndView model = new ModelAndView("index");
    model.addObject("rootFolder", rootFolderService.getRootFolder().getAbsolutePath());
    model.addObject("configFile", configService.getConfigFile().getAbsoluteFile());
    model.addObject("propFile", Thread.currentThread().getContextClassLoader().getResource("config/config.properties"));
    
    Page<Header> headers = contentProvider.listHeaders(new Filter(null, null, "oai_dc", null));
    
    List<String> firstIds = headers.stream().limit(5).map(h->h.identifier.toASCIIString()).collect(Collectors.toList());
    model.addObject("firstIds", firstIds);
		
		return model;
	}  
}
