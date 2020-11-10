/*
 * Copyright 2020 The jspecify Authors.
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

import org.jspecify.annotations.*;

@DefaultNonNull
public class WildcardsWithDefault {
  public void noBoundsNotNull(A<?, ?, ?> a) {}

  public void noBoundsNullable(
      A<? extends @Nullable Object, ? extends @Nullable Object, ? extends @Nullable Object> a) {}
}

@DefaultNonNull
class A<T extends Object, E extends @Nullable Object, F extends @NullnessUnspecified Object> {}

@DefaultNonNull
class Use {
  public static void main(
      A<Object, Object, Object> aNotNullNotNullNotNull,
      A<Object, Object, @Nullable Object> aNotNullNotNullNull,
      A<Object, @Nullable Object, Object> aNotNullNullNotNull,
      A<Object, @Nullable Object, @Nullable Object> aNotNullNullNull,
      WildcardsWithDefault b) {
    b.noBoundsNotNull(aNotNullNotNullNotNull);
    // jspecify_nullness_mismatch
    b.noBoundsNotNull(aNotNullNotNullNull);
    // jspecify_nullness_mismatch
    b.noBoundsNotNull(aNotNullNullNotNull);
    // jspecify_nullness_mismatch
    b.noBoundsNotNull(aNotNullNullNull);

    b.noBoundsNullable(aNotNullNotNullNotNull);
    b.noBoundsNullable(aNotNullNotNullNull);
    b.noBoundsNullable(aNotNullNullNotNull);
    b.noBoundsNullable(aNotNullNullNull);
  }
}
