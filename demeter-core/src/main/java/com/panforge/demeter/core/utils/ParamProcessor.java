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

import com.panforge.demeter.core.model.ErrorCode;
import com.panforge.demeter.core.model.ErrorInfo;
import com.panforge.demeter.core.api.exception.BadArgumentException;
import com.panforge.demeter.core.api.exception.ProtocolException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;

/**
 * Parameters processor.
 */
public class ParamProcessor {
  private Map<String, Setter> actors = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  
  /**
   * Creates instance of the builder.
   * @param name name of the parameter
   * @param setter setter of the parameter
   * @return the builder
   */
  public static ParamProcessor.Builder with(String name, Setter setter) {
    Validate.notEmpty(name, "Missing name");
    Validate.notNull(setter, "Missing setter");
    return new Builder(name, setter);
  }
  
  /**
   * Builds the parameter processor.
   * @return the parameter processor
   */
  public static ParamProcessor build() {
    return new ParamProcessor();
  }
  
  /**
   * Hidden constructor.
   */
  private ParamProcessor() {
    
  }
  
  /**
   * Executes processor.
   * @param params input parameters
   * @throws BadArgumentException if error processing request
   */
  public void execute(Map<String, List<String>> params) throws BadArgumentException {
    Validate.notNull(params, "Missing parameters");
    List<ErrorInfo> errorInfos = new ArrayList<>();
    
    for (Map.Entry<String, List<String>> param: params.entrySet()) {
      Setter op = actors.remove(param.getKey());
      if (op==null) {
        errorInfos.add(new ErrorInfo(ErrorCode.badArgument, 
                String.format("Unrecognized argument: %s", param.getValue().stream().map(v -> String.format("%s=%s", param.getKey(), v)).collect(Collectors.joining(", ")))));
      } else {
        try {
          op.set(param.getValue());
        } catch (ProtocolException ex) {
          errorInfos.addAll(Arrays.asList(ex.infos));
        }
      }
    }
    
    for (Map.Entry<String, Setter> actor: actors.entrySet()) {
      List<String> values = params.get(actor.getKey());
      try {
        actor.getValue().set(values);
      } catch (ProtocolException ex) {
        errorInfos.addAll(Arrays.asList(ex.infos));
      }
    }
    
    if (!errorInfos.isEmpty()) {
      throw new BadArgumentException(errorInfos.toArray(new ErrorInfo[errorInfos.size()]));
    }
  }

  /**
   * Processor builder.
   */
  public static class Builder {
    private ParamProcessor processor = new ParamProcessor();
    
    /** 
     * Non-argument constructor 
     */
    Builder() {
    }
    
    /**
     * Constructor with initial parameter setter.
     * @param name parameter name
     * @param setter value setter
     */
    Builder(String name, Setter setter) {
      processor.actors.put(name, setter);
    }
    
    /**
     * Adds another parameter setter.
     * @param name parameter name
     * @param setter value setter
     * @return the builder
     */
    public Builder with(String name, Setter setter) {
      processor.actors.put(name, setter);
      return this;
    }
    
    /**
     * Builds parameter processor.
     * @return parameter processor
     */
    public ParamProcessor build() {
      return processor;
    }
  }
  
  /**
   * Value setter.
   */
  public static interface Setter {
    /**
     * Sets value from parameters.
     * @param parameters input parameters
     * @throws ProtocolException if error setting parameter
     */
    void set(List<String> parameters) throws ProtocolException;
  }
}
