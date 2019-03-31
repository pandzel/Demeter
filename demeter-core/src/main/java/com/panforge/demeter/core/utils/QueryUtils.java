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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Query utilities.
 */
public class QueryUtils {
  
  /**
   * Converts query string into map of parameters.<p>
   * Resulting map associates possible multiple values into a single key.
   * @param query query string
   * @return map of parameters
   */
  public static Map<String,List<String>> queryToParams(String query) {
    Validate.notNull(query, "Missing query");
    return Arrays.stream(query.split("&"))
            .map(param -> parseParam(param))
            .filter(kvp -> kvp!=null && kvp.length==2)
            .collect(()->new TreeMap<>(String.CASE_INSENSITIVE_ORDER), (m, kvp) -> {
              List<String> values = m.get(kvp[0]);
              if (values==null) {
                values = new ArrayList<>();
                m.put(kvp[0], values);
              }
              values.add(kvp[1]);
            }, Map::putAll);
  }
  
  public static Map<String,List<String>> trimParams(Map<String,List<String>> params) {
    Map<String, List<String>> result = params.entrySet().stream()
            .collect(Collectors.toMap(e->e.getKey(), e->e.getValue().stream().filter(v->!StringUtils.isBlank(v)).collect(Collectors.toList())))
            .entrySet().stream().filter(e->!e.getValue().isEmpty()).collect(Collectors.toMap(e->e.getKey(), e->e.getValue()));
    return result;
  }
  
  /**
   * Converts map of parameters into a query string.
   * @param params map of parameters
   * @return query string
   */
  public static String paramsToQuery(Map<String, List<String>> params) {
    return paramsToQuery(params, Collectors.joining("&"));
  }
  
  /**
   * Modifies map of parameters by removing declared keys.
   * @param params map of parameters
   * @param keysToReject keys to reject
   * @return modified map
   */
  public static Map<String,List<String>> rejectKeys(Map<String,List<String>> params, String...keysToReject) {
    return params.entrySet().stream()
            .filter(e -> !Arrays.stream(keysToReject).anyMatch(k -> k.equalsIgnoreCase(e.getKey())))
            .collect(()->new TreeMap<>(String.CASE_INSENSITIVE_ORDER), (m, e) ->m.put(e.getKey(), e.getValue()), Map::putAll);
  }
  
  /**
   * Converts parameters into parameters list.
   * <p>
   * Each entry in the output list is an array of strings which is a key-value pair.
   * @param params parameters
   * @return parameters list
   */
  public static List<String[]> paramsToList(Map<String, List<String>> params) {
    return params.entrySet().stream()
            .flatMap(e -> e.getValue().stream().map(v -> new String[]{e.getKey(), v}))
            .collect(Collectors.toList());
  }
  
  private static String paramsToQuery(Map<String, List<String>> params, Collector<CharSequence,?,String> collector) {
    Validate.notNull(params, "Missing parameters");
    return paramsToList(params).stream()
            .map(kvp -> String.format("%s=%s", kvp[0], kvp[1]))
            .collect(collector);
  }
  
  private static String[] parseParam(String param) {
    if (param==null || param.trim().isEmpty()) {
      return null;
    }
    int eqIdx = param.indexOf("=");
    if (eqIdx < 0) {
      return new String[] {param, ""};
    } else if (eqIdx > 0) {
      String key = param.substring(0, eqIdx);
      if (key.trim().isEmpty()) {
        return null;
      }
      String value = param.substring(eqIdx+1);
      return new String[] {key, value};
    }
    return null;
  }
}
