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
package com.panforge.demeter.http.client;

import com.panforge.demeter.core.api.ResponseParser;
import com.panforge.demeter.core.api.exception.ProtocolException;
import com.panforge.demeter.core.model.request.Request;
import com.panforge.demeter.core.model.response.Response;
import com.panforge.demeter.core.utils.UnicodeBOMInputStream;
import com.panforge.robotstxt.client.HttpClientWrapper;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 * OAI-PMH client.
 */
public class Client implements Closeable {

  private final ResponseParser parser = new ResponseParser();
  private final CloseableHttpClient httpClient;
  private final URL url;

  /**
   * Creates instance of the client.
   *
   * @param httpClient the HTTP client
   * @param url the end-point URL.
   */
  public Client(CloseableHttpClient httpClient, URL url) {
    this.httpClient = httpClient;
    this.url = url;
    Validate.notNull(httpClient, "Missing HTTP client");
    Validate.notNull(url, "Missing end-point url");
  }

  /**
   * Creates instance of the client.
   *
   * @param url the end-point URL.
   * @param ignoreRobots <code>true</code> to ignore robots.txt
   */
  public Client(URL url, boolean ignoreRobots) {
    this(ignoreRobots ? HttpClients.createSystem() : new HttpClientWrapper(HttpClients.createSystem()), url);
  }

  /**
   * Creates instance of the client.
   *
   * @param url the end-point URL.
   */
  public Client(URL url) {
    this(url, false);
  }

  /**
   * Executes request.
   *
   * @param request request
   * @return response
   * @throws ProtocolException if error executing request
   * @throws java.net.URISyntaxException if invalid URI
   * @throws java.io.IOException if error reading response
   */
  public Response execute(Request request) throws ProtocolException, URISyntaxException, IOException {
    Validate.notNull(request, "Missing request");

    Map<String, List<String>> params = request.getParameters();
    List<NameValuePair> parmPairs = params.entrySet().stream()
            .flatMap(e -> e.getValue().stream().map(v -> new BasicNameValuePair(e.getKey(), v)))
            .collect(Collectors.toList());

    URIBuilder builder = new URIBuilder()
            .setScheme(url.getProtocol())
            .setHost(url.getHost())
            .setPort(url.getPort())
            .setPath(url.getPath())
            .addParameters(parmPairs);
    URI requestUri = builder.build();

    HttpGet httpRequest = new HttpGet(requestUri);

    try (CloseableHttpResponse httpResponse = httpClient.execute(httpRequest); InputStream httpStream = new UnicodeBOMInputStream(httpResponse.getEntity().getContent());) {
      if (httpResponse.getStatusLine().getStatusCode() == 429 || httpResponse.getStatusLine().getStatusCode() == 503) {
        Date retryAfter = getRetryAfter(httpResponse);
        if (retryAfter != null) {
          long delay = retryAfter.getTime() - System.currentTimeMillis();
          if (delay > 0) {
            try {
              Thread.sleep(delay);
            } catch (InterruptedException ex) {
              // ignore
            }
          }
          return execute(request);
        }
      }

      if (httpResponse.getStatusLine().getStatusCode() >= 400) {
        throw new HttpResponseException(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase());
      }
      
      return parser.parse(httpStream);
    }
  }

  @Override
  public void close() throws IOException {
    httpClient.close();
  }

  private Date getRetryAfter(HttpResponse response) {
    org.apache.http.Header retryAfterHeader = response.getFirstHeader("Retry-After");
    if (retryAfterHeader != null) {
      Date parseDate = DateUtils.parseDate(retryAfterHeader.getValue());
      if (parseDate != null) {
        return parseDate;
      }
      try {
        return new Date(System.currentTimeMillis() + 1000L * Integer.valueOf(retryAfterHeader.getValue()));
      } catch (NumberFormatException e) {
      }
    }
    return null;
  }
}
