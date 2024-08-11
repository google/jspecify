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
package annotatedboundsofwildcard;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullnessUnspecified;

@NullMarked
public class AnnotatedBoundsOfWildcard {
  public void superAsIs(
      // :: error: jspecify_nullness_not_enough_information jspecify_but_expect_nothing
      Test<? super Base, ? super @Nullable Base, ? super @NullnessUnspecified Base> a) {}

  public void superNotNull(Test<? super Base, ? super Base, ? super Base> a) {}

  public void superNullable(
      // :: error: jspecify_nullness_not_enough_information jspecify_but_expect_nothing
      Test<? super @Nullable Base, ? super @Nullable Base, ? super @Nullable Base> a) {}

  public void extendsAsIs(
      Test<? extends Base, ? extends @Nullable Base, ? extends @NullnessUnspecified Base> a) {}

  public void extendsNotNull(Test<? extends Base, ? extends Base, ? extends Base> a) {}

  public void extendsNullable(
      Test<? extends @Nullable Base, ? extends @Nullable Base, ? extends @Nullable Base> a) {}

  public void noBounds(
      Test<
              ? extends @NullnessUnspecified Object,
              ? extends @NullnessUnspecified Object,
              ? extends @NullnessUnspecified Object>
          a) {}
}

class Base {}

class Derived extends Base {}

@NullMarked
class Test<T extends Object, E extends @Nullable Object, F extends @NullnessUnspecified Object> {}

@NullMarked
class Use {
  public void main(
      Test<Derived, Derived, Derived> aNotNullNotNullNotNull,
      // :: error: jspecify_nullness_not_enough_information
      Test<Derived, Derived, @Nullable Derived> aNotNullNotNullNull,
      Test<Derived, @Nullable Derived, Derived> aNotNullNullNotNull,
      // :: error: jspecify_nullness_not_enough_information
      Test<Derived, @Nullable Derived, @Nullable Derived> aNotNullNullNull,
      //
      Test<Object, Object, Object> aObjectNotNullNotNullNotNull,
      // :: error: jspecify_nullness_not_enough_information
      Test<Object, Object, @Nullable Object> aObjectNotNullNotNullNull,
      Test<Object, @Nullable Object, Object> aObjectNotNullNullNotNull,
      // :: error: jspecify_nullness_not_enough_information
      Test<Object, @Nullable Object, @Nullable Object> aObjectNotNullNullNull,
      //
      AnnotatedBoundsOfWildcard b) {
    // :: error: jspecify_nullness_mismatch
    b.superAsIs(aObjectNotNullNotNullNotNull);
    // :: error: jspecify_nullness_mismatch
    b.superAsIs(aObjectNotNullNotNullNull);
    // :: error: jspecify_nullness_not_enough_information
    b.superAsIs(aObjectNotNullNullNotNull);
    b.superAsIs(aObjectNotNullNullNull);

    b.superNotNull(aObjectNotNullNotNullNotNull);
    b.superNotNull(aObjectNotNullNotNullNull);
    b.superNotNull(aObjectNotNullNullNotNull);
    b.superNotNull(aObjectNotNullNullNull);

    // :: error: jspecify_nullness_mismatch
    b.superNullable(aObjectNotNullNotNullNotNull);
    // :: error: jspecify_nullness_mismatch
    b.superNullable(aObjectNotNullNotNullNull);
    // :: error: jspecify_nullness_mismatch
    b.superNullable(aObjectNotNullNullNotNull);
    // :: error: jspecify_nullness_mismatch
    b.superNullable(aObjectNotNullNullNull);

    b.extendsAsIs(aNotNullNotNullNotNull);
    // :: error: jspecify_nullness_not_enough_information
    b.extendsAsIs(aNotNullNotNullNull);
    b.extendsAsIs(aNotNullNullNotNull);
    // :: error: jspecify_nullness_not_enough_information
    b.extendsAsIs(aNotNullNullNull);

    b.extendsNotNull(aNotNullNotNullNotNull);
    // :: error: jspecify_nullness_mismatch
    b.extendsNotNull(aNotNullNotNullNull);
    // :: error: jspecify_nullness_mismatch
    b.extendsNotNull(aNotNullNullNotNull);
    // :: error: jspecify_nullness_mismatch
    b.extendsNotNull(aNotNullNullNull);

    b.extendsNullable(aNotNullNotNullNotNull);
    b.extendsNullable(aNotNullNotNullNull);
    b.extendsNullable(aNotNullNullNotNull);
    b.extendsNullable(aNotNullNullNull);

    b.noBounds(aNotNullNotNullNotNull);
    // :: error: jspecify_nullness_not_enough_information
    b.noBounds(aNotNullNotNullNull);
    // :: error: jspecify_nullness_not_enough_information
    b.noBounds(aNotNullNullNotNull);
    // :: error: jspecify_nullness_not_enough_information
    b.noBounds(aNotNullNullNull);
  }
}
