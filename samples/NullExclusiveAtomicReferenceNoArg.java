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
class NullExclusiveAtomicReferenceNoArg {
  void x0() {
    // :: error: jspecify_nullness_mismatch
    AtomicReference<Object> a = new AtomicReference<Object>();
    // :: error: jspecify_nullness_mismatch
    AtomicReference<Object> b = new AtomicReference<>();
    AtomicReference<Object> c = new AtomicReference<Object>() {};

    // :: error: jspecify_nullness_not_enough_information
    AtomicReference<?> d = new AtomicReference<@NullnessUnspecified Object>();
    // :: error: jspecify_nullness_not_enough_information
    AtomicReference<@NullnessUnspecified Object> e = new AtomicReference<>();

    AtomicReference<@Nullable Object> f = new AtomicReference<@Nullable Object>();
    AtomicReference<@Nullable Object> g = new AtomicReference<>();
  }

  <T> void x1() {
    // :: error: jspecify_nullness_mismatch
    AtomicReference<T> a = new AtomicReference<T>();

    // :: error: jspecify_nullness_not_enough_information
    AtomicReference<?> b = new AtomicReference<@NullnessUnspecified T>();

    AtomicReference<@Nullable T> c = new AtomicReference<@Nullable T>();
  }

  <T extends @NullnessUnspecified Object> void x2() {
    // :: error: jspecify_nullness_mismatch
    AtomicReference<T> a = new AtomicReference<T>();

    // :: error: jspecify_nullness_not_enough_information
    AtomicReference<?> b = new AtomicReference<@NullnessUnspecified T>();

    AtomicReference<@Nullable T> c = new AtomicReference<@Nullable T>();
  }

  <T extends @Nullable Object> void x3() {
    // :: error: jspecify_nullness_mismatch
    AtomicReference<T> a = new AtomicReference<T>();

    // :: error: jspecify_nullness_not_enough_information
    AtomicReference<?> b = new AtomicReference<@NullnessUnspecified T>();

    AtomicReference<@Nullable T> c = new AtomicReference<@Nullable T>();
  }

  // :: error: jspecify_nullness_mismatch
  static class MyAtomicReferenceImplicitSuper extends AtomicReference<Object> {}

  static class MyAtomicReferenceExplicitSuper extends AtomicReference<Object> {
    MyAtomicReferenceExplicitSuper() {
      // :: error: jspecify_nullness_mismatch
      super();
    }
  }

  // :: error: jspecify_nullness_not_enough_information
  static class MyAtomicReferenceImplicitSuperUnspec
      extends AtomicReference<@NullnessUnspecified Object> {}

  static class MyAtomicReferenceExplicitSuperUnspec
      extends AtomicReference<@NullnessUnspecified Object> {
    MyAtomicReferenceExplicitSuperUnspec() {
      // :: error: jspecify_nullness_not_enough_information
      super();
    }
  }

  static class MyAtomicReferenceImplicitSuperUnionNull extends AtomicReference<@Nullable Object> {}

  static class MyAtomicReferenceExplicitSuperUnionNull extends AtomicReference<@Nullable Object> {
    MyAtomicReferenceExplicitSuperUnionNull() {
      super();
    }
  }
}
