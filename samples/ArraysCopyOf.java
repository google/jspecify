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
import static java.util.Arrays.copyOf;
import static java.util.Arrays.copyOfRange;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullnessUnspecified;

@NullMarked
class ArraysCopyOf {
  Object[] arbitrary(Object[] o, int length) {
    // :: error: jspecify_nullness_mismatch jspecify_but_expect_nothing
    return copyOf(o, length);
  }

  Object[] arrayLength(Object[] o) {
    return copyOf(o, o.length);
  }

  Object[] arbitraryUnionNull(@Nullable Object[] o, int length) {
    // :: error: jspecify_nullness_mismatch jspecify_but_expect_nothing
    return copyOf(o, length);
  }

  Object[] arrayLengthUnionNull(@Nullable Object[] o) {
    // :: error: jspecify_nullness_mismatch jspecify_but_expect_nothing
    return copyOf(o, o.length);
  }

  Object[] arbitraryUnspec(@NullnessUnspecified Object[] o, int length) {
    // :: error: jspecify_nullness_mismatch jspecify_but_expect_nothing
    return copyOf(o, length);
  }

  Object[] arrayLengthUnspec(@NullnessUnspecified Object[] o) {
    // :: error: jspecify_nullness_not_enough_information jspecify_but_expect_nothing
    return copyOf(o, o.length);
  }

  // TODO(cpovirk): Samples for copyOfRange besides just this one:

  Object[] arbitrary(Object[] o, int from, int to) {
    // :: error: jspecify_nullness_mismatch jspecify_but_expect_nothing
    return copyOfRange(o, from, to);
  }

  // TODO(cpovirk): Samples for the Class-accepting overloads.

  <T extends @Nullable Object> T[] parametric(T[] o) {
    return copyOf(o, o.length);
  }

  <T extends @Nullable Object> @Nullable T[] parametricUnionNull(@Nullable T[] o) {
    return copyOf(o, o.length);
  }
}
