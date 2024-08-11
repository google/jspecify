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
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.SortedSet;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface ComparatorPure {
  default void noCheck(SortedSet<?> s) {
    // :: error: jspecify_nullness_mismatch
    accept(s.comparator());
  }

  default void noCheck(SortedMap<?, ?> s) {
    // :: error: jspecify_nullness_mismatch
    accept(s.comparator());
  }

  default void noCheck(NavigableSet<?> s) {
    // :: error: jspecify_nullness_mismatch
    accept(s.comparator());
  }

  default void noCheck(NavigableMap<?, ?> s) {
    // :: error: jspecify_nullness_mismatch
    accept(s.comparator());
  }

  default void check(SortedSet<?> s) {
    if (s.comparator() != null) {
      accept(s.comparator());
    }
  }

  default void check(SortedMap<?, ?> s) {
    if (s.comparator() != null) {
      accept(s.comparator());
    }
  }

  default void check(NavigableSet<?> s) {
    if (s.comparator() != null) {
      accept(s.comparator());
    }
  }

  default void check(NavigableMap<?, ?> s) {
    if (s.comparator() != null) {
      accept(s.comparator());
    }
  }

  void accept(Object o);
}
