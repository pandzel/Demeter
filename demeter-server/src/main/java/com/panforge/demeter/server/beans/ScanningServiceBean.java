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

import com.panforge.demeter.core.utils.XmlUtils;
import com.panforge.demeter.server.MetaDescriptor;
import com.panforge.demeter.server.MetaProcessorService;
import com.panforge.demeter.server.ScanningService;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Scanning service bean.
 */
@Service
public class ScanningServiceBean implements ScanningService {
  private static final Logger LOG = LoggerFactory.getLogger(ScanningServiceBean.class);
  
  @Autowired
  private MetaProcessorService metadataProcessorService;
  
  @PostConstruct
  public void construct() {
    LOG.info(String.format("%s created.", this.getClass().getSimpleName()));
  }
  
  @PreDestroy
  public void destroy() {
    LOG.info(String.format("%s destroyed.", this.getClass().getSimpleName()));
  }

  @Override
  public void scan(File folder, MetaListener listener) {
    Set<File> visitedDirs = new HashSet<>();
    scan(visitedDirs, folder, listener);
  }
  
  private void scan(Set<File> visitedDirs, File folder, MetaListener listener) {
    File[] xmls = folder.listFiles(f->f.isFile() && f.getName().toLowerCase().endsWith(".xml"));
    Arrays.stream(xmls).map(f->new FileData(f, parseToXml(f))).filter(fd->fd.doc!=null).forEach(fd->{
      MetaDescriptor md = metadataProcessorService.describe(fd.file, fd.doc);
      if (md!=null) {
        listener.accept(md);
      }
    });

    File[] dirs = folder.listFiles(f->f.isDirectory());
    Arrays.stream(dirs)
            .filter(dir->!visitedDirs.contains(dir))
            .forEach(dir->scan(visitedDirs, dir, listener));
  }
  
  private Document parseToXml(File file) {
    try {
      return XmlUtils.parseToXml(file);
    } catch (IOException | SAXException ex) {
      return null;
    }
  }
  
  private static class FileData {
    public final File file;
    public final Document doc;

    public FileData(File file, Document doc) {
      this.file = file;
      this.doc = doc;
    }
    
  }
}
