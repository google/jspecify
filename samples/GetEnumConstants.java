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

@NullMarked
class GetEnumConstants {
  Object arbitraryClass(Class<?> c) {
    // :: error: jspecify_nullness_mismatch
    return c.getEnumConstants();
  }

  Object arbitraryObject(Object o) {
    // :: error: jspecify_nullness_mismatch
    return o.getClass().getEnumConstants();
  }

  Object literalNonEnum() {
    // :: error: jspecify_nullness_mismatch
    return Object.class.getEnumConstants();
  }

  Object enumWildcard(Class<? extends Enum<?>> c) {
    return c.getEnumConstants();
  }

  Object enumRawWildcard(Class<? extends Enum> c) {
    return c.getEnumConstants();
  }

  <E extends Enum<E>> Object enumTypeVariable(Class<E> c) {
    return c.getEnumConstants();
  }

  Object literalEnum() {
    return MyEnum.class.getEnumConstants();
  }

  Object literalJavaLangEnum() {
    // :: error: jspecify_nullness_mismatch jspecify_but_expect_nothing
    return Enum.class.getEnumConstants();
  }

  enum MyEnum {
    FOO {
      Object fromSubclass() {
        // :: error: jspecify_nullness_mismatch jspecify_but_expect_nothing
        return getClass().getEnumConstants();
      }
    }
  }
}
