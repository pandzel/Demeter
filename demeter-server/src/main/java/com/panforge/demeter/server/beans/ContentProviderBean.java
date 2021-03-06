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

import com.panforge.demeter.core.api.exception.CannotDisseminateFormatException;
import com.panforge.demeter.core.api.exception.IdDoesNotExistException;
import com.panforge.demeter.core.api.exception.NoMetadataFormatsException;
import com.panforge.demeter.core.api.exception.NoRecordsMatchException;
import com.panforge.demeter.core.api.exception.NoSetHierarchyException;
import com.panforge.demeter.core.model.response.elements.Header;
import com.panforge.demeter.core.model.response.elements.MetadataFormat;
import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.response.elements.Set;
import com.panforge.demeter.core.utils.XmlUtils;
import java.net.URI;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import com.panforge.demeter.server.MetaDescriptor;
import com.panforge.demeter.server.MetaProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import com.panforge.demeter.server.RootFolderService;
import com.panforge.demeter.server.ScanningService;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.panforge.demeter.core.content.ContentProvider;
import com.panforge.demeter.core.content.Filter;
import com.panforge.demeter.core.content.Page;
import com.panforge.demeter.core.content.StreamingIterable;
import com.panforge.demeter.core.utils.DefaultPageCursor;
import java.util.List;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Repository bean.
 */
@Service
public class ContentProviderBean implements ContentProvider<DefaultPageCursor> {
  private static final Logger LOG = LoggerFactory.getLogger(ContentProviderBean.class);
  
  @Autowired
  private RootFolderService rootFolderServise;
  
  @Autowired
  private ScanningService scanningService;
  
  @Autowired
  private MetaProcessorService metadataProcessorService;
  
  private final Map<URI, Map<String,MetaDescriptor>> descriptors = Collections.synchronizedMap(new HashMap<>());
  
  @PostConstruct
  public void construct() {
    new Thread(()->scanningService.scan(rootFolderServise.getRootFolder(), md->storeDescriptor(md)))
            .start();
    LOG.info(String.format("%s created.", this.getClass().getSimpleName()));
  }
  
  @PreDestroy
  public void destroy() {
    LOG.info(String.format("%s destroyed.", this.getClass().getSimpleName()));
  }

  @Override
  public StreamingIterable<MetadataFormat> listMetadataFormats(URI uri) throws  IdDoesNotExistException, NoMetadataFormatsException {
    List<MetadataFormat> formats = metadataProcessorService.listMetadataFormats().stream().collect(Collectors.toList());
    if (formats.isEmpty()) {
      throw new NoMetadataFormatsException(uri==null? String.format("No metadata formats"): String.format("No metadata formats for: %s", uri));
    }
    return Page.of(formats);
  }

  @Override
  public Page<Set, DefaultPageCursor> listSets(DefaultPageCursor pageCursor, int pageSize) throws NoSetHierarchyException {
    throw new NoSetHierarchyException("This repository does not support set hierarchy.");
  }

  @Override
  public Page<Header, DefaultPageCursor> listHeaders(Filter filter, DefaultPageCursor pageCursor, int pageSize) throws CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
    if (filter.set!=null) {
      throw new NoSetHierarchyException("This repository does not support set hierarchy.");
    }
    try {
      if (!StreamSupport.stream(listMetadataFormats(null).spliterator(), false).map(f->f.metadataPrefix).anyMatch(p->p.equals(filter.metadataPrefix))) {
        throw new CannotDisseminateFormatException(String.format("Invalid metadata format prefix: '%s'", filter.metadataPrefix));
      }
    } catch (NoMetadataFormatsException|IdDoesNotExistException ex) {
        throw new CannotDisseminateFormatException(String.format("Invalid metadata format prefix: '%s'", filter.metadataPrefix), ex);
    }
    long skip = pageCursor!=null && pageCursor.cursor!=null? pageCursor.cursor(): 0;
    List<Header> headers = descriptors.values().stream()
                    .map(l -> {
                      MetaDescriptor md = l.values().stream()
                              .filter(desc->desc.matches(filter))
                              .sorted((a, b) -> a.datestamp.compareTo(b.datestamp))
                              .findFirst()
                              .orElse(null);
                      return md != null ? md.toHeader() : null;
                    })
                    .filter(h -> h != null)
                    .skip(skip)
                    .limit(pageSize)
                    .collect(Collectors.toList());    
    
    if (headers.isEmpty()) {
      throw new NoRecordsMatchException(String.format("No matching records."));
    }
    DefaultPageCursor nextPageCursor = null;
    if (headers.size()>=pageSize) {
      nextPageCursor = new DefaultPageCursor();
      nextPageCursor.cursor = skip + pageSize;
    }
    return Page.of(headers, descriptors.values().size(), nextPageCursor);
  }

  @Override
  public Record readRecord(URI identifier, String metadataPrefix) throws IdDoesNotExistException, CannotDisseminateFormatException {
    Map<String, MetaDescriptor> commonDescriptors = descriptors.get(identifier);
    if (commonDescriptors==null) {
      throw new IdDoesNotExistException(identifier.toASCIIString());
    }
    MetaDescriptor md = commonDescriptors.get(metadataPrefix);
    if (md==null) {
      throw new CannotDisseminateFormatException(metadataPrefix);
    }
    Header header = md.toHeader();
    Document doc = parseToXml(md.source);
    if (doc!=null) {
      doc = md.mp.adopt(md.source, doc);
    }
    
    return new Record(header, doc, null);
  }
  
  private void storeDescriptor(MetaDescriptor md) {
    Map<String, MetaDescriptor> commonDescriptors = descriptors.get(md.uri);
    if (commonDescriptors==null) {
      commonDescriptors = Collections.synchronizedMap(new HashMap<>());
      descriptors.put(md.uri, commonDescriptors);
    }
    commonDescriptors.put(md.format.metadataPrefix, md);
  }
  
  private Document parseToXml(File file) {
    try {
      return XmlUtils.parseToXml(file);
    } catch (IOException | SAXException ex) {
      LOG.debug(String.format("Error parsing to XML: %s", file), ex);
      return null;
    }
  }
}
