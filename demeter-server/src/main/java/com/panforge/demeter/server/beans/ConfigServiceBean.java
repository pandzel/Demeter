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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.panforge.demeter.server.ConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.panforge.demeter.core.api.Config;
import com.panforge.demeter.core.utils.DateTimeUtils;
import com.panforge.demeter.server.RootFolderService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Configuration service bean.
 */
@Service
public class ConfigServiceBean implements ConfigService {
  
  private static final Logger LOG = LoggerFactory.getLogger(ConfigServiceBean.class);
  private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());
  
  static {
    SimpleModule module = new SimpleModule();
    module.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
    module.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
    MAPPER.registerModule(module);
  }
  
  @Autowired
  private RootFolderService rootFolder;
  
  private File configFile;
  private Config config = new Config();
  
  @PostConstruct
  public void construct() {
    configFile = new File(rootFolder.getRootFolder(), "identify.yaml");
    if (configFile.exists()) {
      // read configuration if exists
      try (InputStream configStream = new FileInputStream(configFile);
              Reader configReader = new BufferedReader(new InputStreamReader(configStream, "UTF-8"))) {
        config = MAPPER.readValue(configReader, Config.class);
        LOG.info(String.format("%s created.", this.getClass().getSimpleName()));
      } catch (IOException ex) {
        LOG.error(String.format("Error reading configuration from: '%s'", configFile), ex);
      }
    } else {
      // write template configuration if doesn't exist
      try (OutputStream configStream = new FileOutputStream(configFile);
              Writer configWritter = new BufferedWriter(new OutputStreamWriter(configStream, "UTF-8"))) {
        MAPPER.writeValue(configWritter, config);
        LOG.info(String.format("%s created.", this.getClass().getSimpleName()));
      } catch (IOException ex) {
        LOG.error(String.format("Error writing configuration into: '%s'", configFile), ex);
      }
    }
  }
  
  @PreDestroy
  public void destroy() {
    LOG.info(String.format("%s destroyed.", this.getClass().getSimpleName()));
  }
  
  @Override
  public Config getConfig() {
    return config;
  }
  
  public File getConfigFile() {
    return configFile;
  }

  
  /**
   * OffsetDateTime serializer.
   */
  private static class OffsetDateTimeSerializer extends StdSerializer<OffsetDateTime> {
    
    public OffsetDateTimeSerializer() {
      super(OffsetDateTime.class);
    }
    
    @Override
    public void serialize(OffsetDateTime value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
      gen.writeString(DateTimeFormatter.ISO_DATE_TIME.format(value));
    }
  }
  
  /**
   * OffsetDateTime deserializer.
   */
  private static class OffsetDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {
    
    public OffsetDateTimeDeserializer() {
      super(OffsetDateTime.class);
    }
    
    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      return DateTimeUtils.parseTimestamp(p.getText());
    }
    
  }  
}
