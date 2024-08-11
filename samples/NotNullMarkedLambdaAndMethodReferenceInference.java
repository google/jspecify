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

class NotNullMarkedLambdaAndMethodReferenceInference {
  @NullMarked
  interface Map<K extends @Nullable Object, V extends @Nullable Object> {}

  @NullMarked
  interface List<E extends @Nullable Object> {}

  @NullMarked
  interface Foo {
    boolean isBar();
  }

  @NullMarked
  interface Stream<T extends @Nullable Object> {
    <R extends @Nullable Object, A extends @Nullable Object> R collect(
        Collector<? super T, A, R> collector);
  }

  @NullMarked
  interface Predicate<T extends @Nullable Object> {
    boolean test(T t);
  }

  @NullMarked
  interface Collectors {
    <T extends @Nullable Object> Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(
        Predicate<? super T> predicate);
  }

  @NullMarked
  interface Collector<
      T extends @Nullable Object, A extends @Nullable Object, R extends @Nullable Object> {}

  @NullMarked
  abstract class Super {
    abstract Stream<Foo> foos();

    abstract Collectors collectors();

    abstract void useResult(Map<Boolean, List<Foo>> m);
  }

  abstract class User extends Super {
    void go() {
      useResult(foos().collect(collectors().partitioningBy(Foo::isBar)));
      /*
       * The way to fix the warning might be to make getFnInterfaceFromTree overwrite any
       * parameters' default additional nullnesses with those from the corresponding interface's
       * parameters. Maybe use TreeUtils.findFunction?
       */
      // :: error: jspecify_but_expect_warning
      useResult(foos().collect(collectors().partitioningBy(foo -> foo.isBar())));
    }
  }
}
