/*
 * Copyright 2021 The JSpecify Authors.
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
import org.jspecify.annotations.Nullable;

@NullMarked
class NullExclusiveThreadLocal {
  void x(ThreadLocal<Object> threadLocal) {
    // :: error: jspecify_nullness_mismatch
    ThreadLocal<Object> x0 = new ThreadLocal<>();

    ThreadLocal<@Nullable Object> x1 = new ThreadLocal<>();

    ThreadLocal<Object> x2 =
        new ThreadLocal<Object>() {
          @Override
          protected Object initialValue() {
            return "";
          }
        };

    // :: error: jspecify_nullness_mismatch
    ThreadLocal<Object> x3 = new MyThreadLocalWithoutInitialValue();

    ThreadLocal<Object> x4 = new MyThreadLocalWithInitialValue();

    ThreadLocal<Object> x5 = ThreadLocal.withInitial(() -> "");

    // :: error: jspecify_nullness_mismatch
    ThreadLocal.withInitial(() -> null);
  }

  static class MyThreadLocalWithoutInitialValue extends ThreadLocal<Object> {}

  static class MyThreadLocalWithInitialValue extends ThreadLocal<Object> {
    @Override
    protected Object initialValue() {
      return "";
    }
  }
}
