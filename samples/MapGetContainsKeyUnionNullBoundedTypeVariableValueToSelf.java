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
import java.util.Map;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullnessUnspecified;

@NullMarked
class MapGetContainsKeyUnionNullBoundedTypeVariableValueToSelf<V extends @Nullable Object> {
  V noCheckObject(Map<Object, V> map, Object key) {
    // :: error: jspecify_nullness_mismatch
    return map.get(key);
  }

  V checkObject(Map<Object, V> map, Object key, V defaultValue) {
    if (map.containsKey(key)) {
      return map.get(key);
    }
    return defaultValue;
  }

  V noCheckObjectUnspec(Map<Object, @NullnessUnspecified V> map, Object key) {
    // :: error: jspecify_nullness_mismatch
    return map.get(key);
  }

  V checkObjectUspec(Map<Object, @NullnessUnspecified V> map, Object key, V defaultValue) {
    if (map.containsKey(key)) {
      // :: error: jspecify_nullness_not_enough_information
      return map.get(key);
    }
    return defaultValue;
  }

  V noCheckObjectUnionNull(Map<Object, @Nullable V> map, Object key) {
    // :: error: jspecify_nullness_mismatch
    return map.get(key);
  }

  V checkObjectUnionNull(Map<Object, @Nullable V> map, Object key, V defaultValue) {
    if (map.containsKey(key)) {
      // :: error: jspecify_nullness_mismatch
      return map.get(key);
    }
    return defaultValue;
  }
}
