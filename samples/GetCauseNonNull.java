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
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class GetCauseNonNull {
  Object getCause(Throwable t) {
    // :: error: jspecify_nullness_mismatch
    return t.getCause();
  }

  Object getCause(ExecutionException e) {
    return e.getCause();
  }

  Object getCause(InvocationTargetException e) {
    return e.getCause();
  }

  Object getTargetException(InvocationTargetException e) {
    return e.getTargetException();
  }

  void afterMulticatch() {
    try {
      doSomething();
    } catch (TimeoutException | InterruptedException e) {
    } catch (ExecutionException e) {
      consume(e.getCause());
    }
  }

  abstract void doSomething() throws ExecutionException, InterruptedException, TimeoutException;

  abstract void consume(Throwable t);
}
