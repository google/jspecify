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
import org.jspecify.annotations.NullnessUnspecified;

@NullMarked
class NullCheckDowngradesUnspecifiedForLater {
  Object x(@NullnessUnspecified Object o) {
    if (o != null) {
    } else {
    }

    /*
     * This happened by accident, but upon seeing it, I found it to be potentially useful. So I'm
     * adding a sample so that we notice if we "break" the current behavior. It might be fine --
     * even good! -- to change that behavior, but it will deserve some thought. There's some appeal
     * to the idea that, if you null-check a value once (and you don't throw or return immediately),
     * then you should probably check for null in the future, too.
     */

    // :: error: jspecify_nullness_mismatch
    return o;
  }
}
