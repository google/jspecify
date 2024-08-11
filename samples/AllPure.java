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
interface AllPure {
  interface Foo {
    @Nullable Object bar();

    @Nullable Object bar(Object o);
  }

  interface FooSupplier {
    Foo foo();
  }

  default void noCheck(Foo foo) {
    // :: error: jspecify_nullness_mismatch
    accept(foo.bar());
  }

  default void check(Foo foo) {
    if (foo.bar() != null) {
      accept(foo.bar());
    }
  }

  default void noCheckWithArg(Foo foo) {
    // :: error: jspecify_nullness_mismatch
    accept(foo.bar(makeArg()));
  }

  default void checkWithArg(Foo foo) {
    if (foo.bar(makeArg()) != null) {
      accept(foo.bar(makeArg()));
    }
  }

  default void noCheck(FooSupplier supplier) {
    // :: error: jspecify_nullness_mismatch
    accept(supplier.foo().bar());
  }

  default void check(FooSupplier supplier) {
    if (supplier.foo().bar() != null) {
      accept(supplier.foo().bar());
    }
  }

  void accept(Object o);

  Object makeArg();
}
