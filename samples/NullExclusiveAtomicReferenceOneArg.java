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
import java.util.concurrent.atomic.AtomicReference;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullnessUnspecified;

@NullMarked
class NullExclusiveAtomicReferenceOneArg {
  void x0(Object o) {
    AtomicReference<Object> a = new AtomicReference<Object>(o);
    AtomicReference<Object> b = new AtomicReference<>(o);

    AtomicReference<?> c = new AtomicReference<@NullnessUnspecified Object>(o);
    // :: error: jspecify_nullness_not_enough_information
    AtomicReference<@NullnessUnspecified Object> d = new AtomicReference<>(o);

    AtomicReference<@Nullable Object> e = new AtomicReference<@Nullable Object>(o);
    AtomicReference<@Nullable Object> f = new AtomicReference<>(o);
  }

  <T> void x1(T o) {
    AtomicReference<T> a = new AtomicReference<T>(o);

    AtomicReference<?> b = new AtomicReference<@NullnessUnspecified T>(o);

    AtomicReference<@Nullable T> c = new AtomicReference<@Nullable T>(o);
  }

  <T extends @NullnessUnspecified Object> void x2(T o) {
    AtomicReference<T> a = new AtomicReference<T>(o);

    AtomicReference<?> b = new AtomicReference<@NullnessUnspecified T>(o);

    AtomicReference<@Nullable T> c = new AtomicReference<@Nullable T>(o);
  }

  <T extends @Nullable Object> void x3(T o) {
    AtomicReference<T> a = new AtomicReference<T>(o);

    AtomicReference<?> b = new AtomicReference<@NullnessUnspecified T>(o);

    AtomicReference<@Nullable T> c = new AtomicReference<@Nullable T>(o);
  }

  static class MyAtomicReferenceExplicitSuper extends AtomicReference<Object> {
    MyAtomicReferenceExplicitSuper() {
      super("");
    }
  }

  static class MyAtomicReferenceExplicitSuperUnspec
      extends AtomicReference<@NullnessUnspecified Object> {
    MyAtomicReferenceExplicitSuperUnspec() {
      super("");
    }
  }

  static class MyAtomicReferenceExplicitSuperUnionNull extends AtomicReference<@Nullable Object> {
    MyAtomicReferenceExplicitSuperUnionNull() {
      super("");
    }
  }
}
