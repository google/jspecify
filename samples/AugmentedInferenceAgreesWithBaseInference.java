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

@NullMarked
abstract class AugmentedInferenceAgreesWithBaseInference {
  void x(Foo<Object> a, Foo<@Nullable Object> b) {
    // List of possibly heterogeneous Foo types.
    List<Foo<?>> l1 = makeList(a, b);

    /*
     * List of some unspecified (possibly homogeneous) Foo type. javac infers `makeList` to have the
     * type List<Foo<Object>>, but that's a problem for JSpecify, which needs List<Foo<?>>.
     *
     * Notice that `makeList(a, b)` is fine "in a vacuum" even under JSpecify (as shown above). Only
     * here, where the type of the expression is forced to conform to the javac's type for the
     * expression, is there a problem.
     */
    // jspecify_nullness_mismatch
    List<? extends Foo<?>> l2 = makeList(a, b);

    /*
     * Here's another way to make javac agree with Gnully on the base type. Like the l1 example, it
     * produces no mismatch.
     */
    List<? extends Foo<?>> l3 = this.<Foo<?>>makeList(a, b);
  }

  abstract <T extends @Nullable Object> List<T> makeList(T a, T b);

  interface Foo<T extends @Nullable Object> {}

  interface List<T extends @Nullable Object> {}
}
