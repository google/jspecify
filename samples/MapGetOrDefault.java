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
import java.util.Map;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class MapGetOrDefault {
  Object nonnullValuesNonnullDefault(Map<Object, Object> map, Object key, Object defaultValue) {
    return map.getOrDefault(key, defaultValue);
  }

  Object nonnullValuesNullDefault(
      Map<Object, Object> map, Object key, @Nullable Object defaultValue) {
    // :: error: jspecify_nullness_mismatch
    return map.getOrDefault(key, defaultValue);
  }

  Object nullValuesNonnullDefault(
      Map<Object, @Nullable Object> map, Object key, Object defaultValue) {
    // :: error: jspecify_nullness_mismatch
    return map.getOrDefault(key, defaultValue);
  }

  Object nullValuesNullDefault(
      Map<Object, @Nullable Object> map, Object key, @Nullable Object defaultValue) {
    // :: error: jspecify_nullness_mismatch
    return map.getOrDefault(key, defaultValue);
  }
}
