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
package com.panforge.demeter.core.model.response;

import com.panforge.demeter.core.model.response.elements.Record;
import com.panforge.demeter.core.model.request.GetRecordRequest;
import java.time.OffsetDateTime;

/**
 * GetRecord response.
 */
public final class GetRecordResponse extends Response<GetRecordRequest> {
  
  /** record */
  public final Record record;

  /**
   * Creates instance of the response.
   * @param record record
   * @param responseDate response date
   * @param request request
   */
  public GetRecordResponse(Record record, OffsetDateTime responseDate, GetRecordRequest request) {
    super(responseDate, request);
    this.record = record;
  }
}
