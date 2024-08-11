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
import org.jspecify.annotations.NullMarked;

// did not manage to reproduce the failure from Invokable.java at 3861...
@NullMarked
abstract class ClassSuperVsUnbounded<T, R> implements java.lang.reflect.Member {
  abstract Class<? super Number> make0();

  @Override
  public abstract Class<? super T> getDeclaringClass();

  abstract static class Sub<T> extends ClassSuperVsUnbounded<T, T> {
    void x0() {
      Class<?> x = make0();
    }

    void x1() {
      Class<?> x = getDeclaringClass();
    }
  }
}
