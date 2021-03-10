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
import org.jspecify.annotations.DefaultNonNull;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullnessUnspecified;

@DefaultNonNull
class MapGetContainsKeyNonNullBoundedTypeVariableValue<V> {
  Object noCheckObject(Map<Object, V> map, Object key) {
    // jspecify_nullness_mismatch
    return map.get(key);
  }

  Object checkObject(Map<Object, V> map, Object key) {
    if (map.containsKey(key)) {
      return map.get(key);
    }
    return "";
  }

  Object noCheckObjectUnspec(Map<Object, @NullnessUnspecified V> map, Object key) {
    // jspecify_nullness_mismatch
    return map.get(key);
  }

  Object checkObjectUspec(Map<Object, @NullnessUnspecified V> map, Object key) {
    if (map.containsKey(key)) {
      // jspecify_nullness_not_enough_information
      return map.get(key);
    }
    return "";
  }

  Object noCheckObjectUnionNull(Map<Object, @Nullable V> map, Object key) {
    // jspecify_nullness_mismatch
    return map.get(key);
  }

  Object checkObjectUnionNull(Map<Object, @Nullable V> map, Object key) {
    if (map.containsKey(key)) {
      // jspecify_nullness_mismatch
      return map.get(key);
    }
    return "";
  }
}