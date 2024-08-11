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
import java.util.Collections;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/*
 * I haven't thought deeply about where checkers could identify mismatches or not-enough-information
 * findings in this class: I wrote it mainly to reproduce a crash, and I updated our prototype
 * checker with the goal of avoiding that crash, not fully handling the situation correctly.
 */
class TernaryUnspecVsNoQualifier {
  @NullMarked
  static class ImmutableList<E> implements Iterable<E> {
    static <E> ImmutableList<E> of(E e) {
      return new ImmutableList<>();
    }

    @Override
    public java.util.Iterator<E> iterator() {
      return Collections.<E>emptyList().iterator();
    }
  }

  static class Iterables {
    static <E> Iterable<E> concat(Iterable<? extends E> a, Iterable<? extends E> b) {
      // :: error: jspecify_nullness_not_enough_information
      return Collections.emptyList();
    }
  }

  abstract static class Foo<T extends @Nullable Object> {
    abstract Iterable<Foo<T>> others();

    void go(boolean b) {
      Iterable<Foo<T>> it =
          // :: error: jspecify_nullness_not_enough_information
          b ? ImmutableList.of(this) : Iterables.concat(others(), ImmutableList.of(this));
    }
  }
}
