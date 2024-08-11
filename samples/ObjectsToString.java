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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullnessUnspecified;

@NullMarked
class ObjectsToString {
  String x0(@Nullable Object nullable, String defaultValue) {
    String result = Objects.toString(nullable, defaultValue);
    return result;
  }

  String x1(@Nullable Object nullable, @NullnessUnspecified String defaultValue) {
    String result = Objects.toString(nullable, defaultValue);
    // :: error: jspecify_nullness_not_enough_information
    return result;
  }

  String x2(@Nullable Object nullable, @Nullable String defaultValue) {
    String result = Objects.toString(nullable, defaultValue);
    // :: error: jspecify_nullness_mismatch
    return result;
  }

  String x(@Nullable Object nullable) {
    // Make sure the checker doesn't crash when it sees the 1-arg overload.
    return Objects.toString(nullable);
  }
}
