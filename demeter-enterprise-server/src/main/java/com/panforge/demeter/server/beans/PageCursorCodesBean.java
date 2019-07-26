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

import com.panforge.demeter.core.content.PageCursor;
import com.panforge.demeter.core.content.PageCursorCodec;
import org.springframework.stereotype.Service;

/**
 * Page cursor codec bean.
 */
@Service
public class PageCursorCodesBean implements PageCursorCodec {

  @Override
  public String toString(PageCursor pageCursor) {
    if (!(pageCursor instanceof PageCursorImpl)) throw new IllegalArgumentException(String.format("Page cursor is not %s", PageCursorImpl.class.getCanonicalName()));
    return ((PageCursorImpl)pageCursor).pageCursorStr;
  }

  @Override
  public PageCursor fromString(String pageCursorStr) {
    return new PageCursorImpl(pageCursorStr);
  }
 
  private static class PageCursorImpl implements PageCursor {
    public final String pageCursorStr;

    public PageCursorImpl(String pageCursorStr) {
      this.pageCursorStr = pageCursorStr;
    }
  }
}
