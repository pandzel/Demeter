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
package com.panforge.demeter.core.api;

import java.time.OffsetDateTime;
import java.util.Arrays;

/**
 * Repository configuration/identification.
 */
public class Config {
  /** repository name */
  public String repositoryName;
  /** base URL */
  public String baseURL;
  /** protocol version (default: 2.0) */
  public String protocolVersion = "2.0";
  /** administrators emails (default: empty array)*/
  public String[] adminEmail = new String[]{};
  /** earliest date stamp */
  public OffsetDateTime earliestDatestamp;
  /** determines how information is stored about deleted records (default: no)*/
  public Deletion deletedRecord = Deletion.No;
  /** granularity (default: YYYY-MM-DDThh:mm:ssZ)*/
  public String granularity = "YYYY-MM-DDThh:mm:ssZ";
  /** data compression (default: all) */
  public Compression [] compression = Compression.values();
  
  
  /** 
   * modes of storing information about deleted records.
   * @see <a href='https://www.openarchives.org/OAI/openarchivesprotocol.html#DeletedRecords'>https://www.openarchives.org/OAI/openarchivesprotocol.html#DeletedRecords</a>
   */
  public static enum Deletion {
    /** no information is being stored */
    No,
    /** only information about the fact of deletion is being stored */
    Transient,
    /** full information about deletion, including record content itself, is being stored */
    Permanent;       
    
    /**
     * Parses deletion.
     * @param value string representation of deletion
     * @return deletion
     */
    public static Deletion parse(String value) {
      final String sValue = value!=null? value.toLowerCase(): null;
      return Arrays.stream(values()).filter((Deletion v) -> v.name().toLowerCase().equals(sValue)).findFirst().orElse(null);
    }
  }
  
  /** 
   * compression algorithms.
   * @see <a href='https://www.openarchives.org/OAI/openarchivesprotocol.html#ResponseCompression'>https://www.openarchives.org/OAI/openarchivesprotocol.html#ResponseCompression</a>
   */
  public static enum Compression {
    /** gzip */
    Gzip,
    /** compress */
    Compress,
    /** defalate */
    Deflate,
    /** identity */
    Identity;
    
    /**
     * Parses compression.
     * @param value string representation of deletion
     * @return compression
     */
    public static Compression parse(String value) {
      final String sValue = value!=null? value.toLowerCase(): null;
      return Arrays.stream(values()).filter((v) -> v.name().toLowerCase().equals(sValue)).findFirst().orElse(null);
    }
  }
}
