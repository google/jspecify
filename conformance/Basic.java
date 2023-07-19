/*
 * Copyright 2023 The JSpecify Authors.
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
package conformance;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

class Basic {
  @NonNull Object cannotConvertNullableToNonNull(@Nullable Object nullable) {
    // test:cannot-convert:Object? to Object!
    // test:expression-type:Object?:nullable
    return nullable;
  }

  @Nullable Object canConvertNonNullToNullable(@NonNull Object nonNull) {
    // test:expression-type:Object!:nonNull
    return nonNull;
  }
}
