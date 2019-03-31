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

import com.panforge.demeter.core.api.exception.BadArgumentException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Date and time utilities.
 */
public class DateTimeUtils {

  private static final DateTimeFormatter ISO_DATE_ZONE = new DateTimeFormatterBuilder()
          .append(DateTimeFormatter.ISO_OFFSET_DATE)
          .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
          .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
          .toFormatter();

  /**
   * Parses ISO-style time/timestamp into date+time.
   *
   * @param timestamp timestamp
   * @return date+time
   * @throws BadArgumentException if parsing fails
   */
  public static OffsetDateTime parseRequestTimestamp(String timestamp) throws BadArgumentException {
    try {
      return parseTimestamp(timestamp);
    } catch (DateTimeParseException ex) {
      throw new BadArgumentException(String.format("Invalid timestamp: '%s'", StringUtils.trimToEmpty(timestamp)), ex);
    }
  }

  /**
   * Parses ISO-style time/timestamp into date+time.
   *
   * @param timestamp timestamp
   * @return date+time
   * @throws DateTimeParseException if parsing fails
   */
  public static OffsetDateTime parseTimestamp(String timestamp) throws DateTimeParseException {
    Validate.notEmpty(timestamp, "Missing time stamp");
    DateTimeFormatter fmt = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_OFFSET_DATE)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter();
    try {
      return OffsetDateTime.parse(timestamp);
    } catch (DateTimeParseException e1) {
      try {
        return OffsetDateTime.parse(timestamp, ISO_DATE_ZONE);
      } catch (DateTimeParseException e2) {
        try {
          return OffsetDateTime.parse(timestamp, fmt);
        } catch (DateTimeParseException e3) {
          try {
            LocalDateTime localDateTime = LocalDateTime.parse(timestamp);
            return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
          } catch (DateTimeParseException e4) {
            LocalDate localDate = LocalDate.parse(timestamp);
            return OffsetDateTime.of(LocalDateTime.of(localDate, LocalTime.MIDNIGHT), ZoneOffset.UTC);
          }
        }
      }
    }
  }
}
