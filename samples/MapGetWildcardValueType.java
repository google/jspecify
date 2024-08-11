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

@NullMarked
class MapGetWildcardValueType {
  Object keySetObject(Map<Object, ? extends Object> map) {
    for (Object o : map.keySet()) {
      return map.get(o);
    }
    return "";
  }

  Object containsKeyObject(Map<Object, ? extends Object> map, Object o) {
    if (map.containsKey(o)) {
      return map.get(o);
    }
    return "";
  }

  Object keySetObjectUnionNull(Map<Object, ? extends @Nullable Object> map) {
    for (Object o : map.keySet()) {
      // :: error: jspecify_nullness_mismatch
      return map.get(o);
    }
    return "";
  }

  Object containsKeyObjectUnionNull(Map<Object, ? extends @Nullable Object> map, Object o) {
    if (map.containsKey(o)) {
      // :: error: jspecify_nullness_mismatch
      return map.get(o);
    }
    return "";
  }

  <V extends @Nullable Object> V keySetV(Map<Object, ? extends V> map, V defaultValue) {
    for (Object o : map.keySet()) {
      return map.get(o);
    }
    return defaultValue;
  }

  <V extends @Nullable Object> V containsKeyV(
      Map<Object, ? extends V> map, Object o, V defaultValue) {
    if (map.containsKey(o)) {
      return map.get(o);
    }
    return defaultValue;
  }
}
