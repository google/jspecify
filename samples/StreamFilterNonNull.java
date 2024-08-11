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
import java.util.Objects;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class StreamFilterNonNull {
  Stream<Object> operator(Stream<@Nullable Object> s) {
    return s.filter(x -> x != null);
  }

  Stream<Object> operatorReverse(Stream<@Nullable Object> s) {
    return s.filter(x -> null != x);
  }

  Stream<Object> operatorOtherOperand(Stream<@Nullable Object> s, Object other) {
    // :: error: jspecify_nullness_mismatch
    return s.filter(x -> other != null);
  }

  Stream<Object> objectsNonNull(Stream<@Nullable Object> s) {
    return s.filter(Objects::nonNull);
  }

  Stream<Object> otherMethodReference(Stream<@Nullable Object> s) {
    // :: error: jspecify_nullness_mismatch
    return s.filter(Objects::isNull);
  }
}
