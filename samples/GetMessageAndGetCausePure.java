/*
 * Copyright 2020 The JSpecify Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.jspecify.annotations.NullMarked;

@NullMarked
interface GetMessageAndGetCausePure {
  default void noCheck(Throwable t) {
    // :: error: jspecify_nullness_mismatch
    accept(t.getCause());

    // :: error: jspecify_nullness_mismatch
    accept(t.getMessage());
  }

  default void check(Throwable t) {
    if (t.getCause() != null) {
      accept(t.getCause());
    }

    if (t.getMessage() != null) {
      accept(t.getMessage());
    }
  }

  default void noCheck(Exception e) {
    // :: error: jspecify_nullness_mismatch
    accept(e.getCause());

    // :: error: jspecify_nullness_mismatch
    accept(e.getMessage());
  }

  default void check(Exception e) {
    if (e.getCause() != null) {
      accept(e.getCause());
    }

    if (e.getMessage() != null) {
      accept(e.getMessage());
    }
  }

  void accept(Object o);
}
