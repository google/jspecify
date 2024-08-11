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
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullnessUnspecified;

@NullMarked
class ToArray {
  Object[] wildcard(Collection<?> c) {
    // :: error: jspecify_nullness_mismatch
    return c.toArray();
  }

  Object[] boundedWildcard(Collection<? extends Object> c) {
    return c.toArray();
  }

  Object[] object(Collection<Object> c) {
    return c.toArray();
  }

  Object[] objectUnspec(Collection<@NullnessUnspecified Object> c) {
    // :: error: jspecify_nullness_not_enough_information
    return c.toArray();
  }

  Object[] objectUnionNull(Collection<@Nullable Object> c) {
    // :: error: jspecify_nullness_mismatch
    return c.toArray();
  }

  <T> Object[] objectBounded(Collection<T> c) {
    return c.toArray();
  }

  <T> Object[] objectBoundedUnspec(Collection<@NullnessUnspecified T> c) {
    // :: error: jspecify_nullness_not_enough_information
    return c.toArray();
  }

  <T> Object[] objectBoundedUnionNull(Collection<@Nullable T> c) {
    // :: error: jspecify_nullness_mismatch
    return c.toArray();
  }

  <T extends @NullnessUnspecified Object> Object[] unspecBounded(Collection<T> c) {
    // :: error: jspecify_nullness_not_enough_information
    return c.toArray();
  }

  <T extends @NullnessUnspecified Object> Object[] unspecBoundedUnspec(
      Collection<@NullnessUnspecified T> c) {
    // :: error: jspecify_nullness_not_enough_information
    return c.toArray();
  }

  <T extends @NullnessUnspecified Object> Object[] unspecBoundedUnionNull(
      Collection<@Nullable T> c) {
    // :: error: jspecify_nullness_mismatch
    return c.toArray();
  }

  <T extends @Nullable Object> Object[] unbounded(Collection<T> c) {
    // :: error: jspecify_nullness_mismatch
    return c.toArray();
  }

  <T extends @Nullable Object> Object[] unboundedUnspec(Collection<@NullnessUnspecified T> c) {
    // :: error: jspecify_nullness_mismatch
    return c.toArray();
  }

  <T extends @Nullable Object> Object[] unboundedUnionNull(Collection<@Nullable T> c) {
    // :: error: jspecify_nullness_mismatch
    return c.toArray();
  }

  <T> Object[] objectBoundedTypeParameterWildcard(Collection<? extends T> c) {
    return c.toArray();
  }

  Object[] fromClassTypeParameterBound(CollectionWithObjectBound<?> c) {
    return c.toArray();
  }

  interface CollectionWithObjectBound<T> extends Collection<T> {
    default Object[] toArrayOfNonNull() {
      return toArray();
    }
  }

  interface CollectionWithUnspectBound<T extends @NullnessUnspecified Object>
      extends Collection<T> {
    default Object[] toArrayOfNonNull() {
      // :: error: jspecify_nullness_not_enough_information
      return toArray();
    }
  }

  interface CollectionWithNoBound<T extends @Nullable Object> extends Collection<T> {
    default Object[] toArrayOfNonNull() {
      // :: error: jspecify_nullness_mismatch
      return toArray();
    }
  }
}
