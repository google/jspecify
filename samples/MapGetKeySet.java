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
import java.util.NavigableMap;
import org.jspecify.annotations.NullMarked;

@NullMarked
class MapGetKeySet {
  Object otherColletion(Map<Object, Object> map) {
    for (Object o : map.values()) {
      // :: error: jspecify_nullness_mismatch
      return map.get(o);
    }
    return "";
  }

  Object otherMapKeySet(Map<Object, Object> map, Map<Object, Object> otherMap) {
    for (Object o : otherMap.keySet()) {
      // :: error: jspecify_nullness_mismatch
      return map.get(o);
    }
    return "";
  }

  Object otherObject(Map<Object, Object> map, Object something) {
    for (Object o : map.keySet()) {
      // :: error: jspecify_nullness_mismatch
      return map.get(something);
    }
    return "";
  }

  Object keySet(Map<Object, Object> map) {
    for (Object o : map.keySet()) {
      return map.get(o);
    }
    return "";
  }

  Object navigableKeySet(NavigableMap<Object, Object> map) {
    for (Object o : map.navigableKeySet()) {
      return map.get(o);
    }
    return "";
  }

  Object descendingKeySet(NavigableMap<Object, Object> map) {
    for (Object o : map.descendingKeySet()) {
      return map.get(o);
    }
    return "";
  }

  Object keySetOfNonVariable(MapHolder holder) {
    // We assume everything is @Pure, as usual.
    for (Object o : holder.get().keySet()) {
      return holder.get().get(o);
    }
    return "";
  }

  Object keySetOfNonVariableOtherObjectSameElement(MapHolder holder1, MapHolder holder2) {
    for (Object o : holder1.get().keySet()) {
      // :: error: jspecify_nullness_mismatch
      return holder2.get().get(o);
    }
    return "";
  }

  interface MapHolder {
    Map<Object, Object> get();
  }
}
