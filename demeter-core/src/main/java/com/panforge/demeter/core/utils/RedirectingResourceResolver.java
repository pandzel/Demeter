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
package com.panforge.demeter.core.utils;

import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;
import com.sun.org.apache.xerces.internal.util.XMLCatalogResolver;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.w3c.dom.ls.LSInput;

/**
 * Redirecting resource resolver.
 */
public class RedirectingResourceResolver extends XMLCatalogResolver {
      @Override
      public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        if (systemId!=null) {
          try {
            URL url = new URL(systemId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode()==301 || connection.getResponseCode()==302) {
              String location = connection.getHeaderField("Location");
              if (location!=null && !location.isEmpty()) {
                url = new URL(location);
                connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
                return new DOMInputImpl(null, null, null, reader, "UTF-8");
              }
            }
          } catch (IOException ex) {}
        }
        return super.resolveResource(type, namespaceURI, publicId, systemId, baseURI); //To change body of generated methods, choose Tools | Templates.
      }
  
}
