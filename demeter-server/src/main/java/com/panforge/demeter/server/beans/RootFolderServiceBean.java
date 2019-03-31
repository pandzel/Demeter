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

import java.io.File;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.panforge.demeter.server.RootFolderService;

/**
 * Root folder bean.
 */
@Service
public class RootFolderServiceBean implements RootFolderService {
  
  @Value("${dataPath}")
  private String dataPath;
  private File rootFolder;
  
  @PostConstruct
  public void construct() {
    File dataPathFile = new File(dataPath);
    rootFolder = dataPathFile.isAbsolute()? dataPathFile: new File(System.getProperty("user.home"), dataPathFile.getPath());
    rootFolder.mkdirs();
  }
  
  @PreDestroy
  public void destroy() {
    
  }

  @Override
  public File getRootFolder() {
    return rootFolder;
  }
  
}
