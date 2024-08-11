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
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullnessUnspecified;

@NullMarked
class CaptureConvertedTypeVariableBounded {
  abstract class ImplicitlyObjectBounded<T> {
    abstract class Inner<U extends T> {
      abstract U get();
    }

    Object x0(Inner<? extends Object> p) {
      return p.get();
    }

    Object x1(Inner<? extends @NullnessUnspecified Object> p) {
      return p.get();
    }

    Object x2(Inner<? extends @Nullable Object> p) {
      return p.get();
    }

    Object x3(Inner<?> p) {
      return p.get();
    }
  }

  abstract class ExplicitlyObjectBounded<T extends Object> {
    abstract class Inner<U extends T> {
      abstract U get();
    }

    Object x0(Inner<? extends Object> p) {
      return p.get();
    }

    Object x1(Inner<? extends @NullnessUnspecified Object> p) {
      return p.get();
    }

    Object x2(Inner<? extends @Nullable Object> p) {
      return p.get();
    }

    Object x3(Inner<?> p) {
      return p.get();
    }
  }

  abstract class UnspecBounded<T extends @NullnessUnspecified Object> {
    abstract class Inner<U extends T> {
      abstract U get();
    }

    Object x0(Inner<? extends Object> p) {
      return p.get();
    }

    Object x1(Inner<? extends @NullnessUnspecified Object> p) {
      // :: error: jspecify_nullness_not_enough_information 
      return p.get();
    }

    Object x2(Inner<? extends @Nullable Object> p) {
      // :: error: jspecify_nullness_not_enough_information
      return p.get();
    }

    Object x3(Inner<?> p) {
      // :: error: jspecify_nullness_not_enough_information
      return p.get();
    }
  }

  abstract class NullableBounded<T extends @Nullable Object> {
    abstract class Inner<U extends T> {
      abstract U get();
    }

    Object x0(Inner<? extends Object> p) {
      return p.get();
    }

    Object x1(Inner<? extends @NullnessUnspecified Object> p) {
      // :: error: jspecify_nullness_not_enough_information
      return p.get();
    }

    Object x2(Inner<? extends @Nullable Object> p) {
      // :: error: jspecify_nullness_mismatch
      return p.get();
    }

    Object x3(Inner<?> p) {
      // :: error: jspecify_nullness_mismatch
      return p.get();
    }
  }

  interface Lib {}
}
