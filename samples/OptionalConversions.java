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
import com.google.common.base.Optional;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// The warnings below will go away once Optional is @NullMarked.
@NullMarked
class OptionalConversions {
  java.util.Optional<Foo> to0(Optional<Foo> p) {
    // :: error: jspecify_but_expect_warning
    return Optional.toJavaUtil(p);
  }

  java.util.Optional<Foo> to1(@Nullable Optional<Foo> p) {
    // :: error: jspecify_nullness_mismatch
    return Optional.toJavaUtil(p);
  }

  Optional<Foo> from0(java.util.Optional<Foo> p) {
    // :: error: jspecify_but_expect_warning
    return Optional.fromJavaUtil(p);
  }

  Optional<Foo> from1(java.util.@Nullable Optional<Foo> p) {
    // :: error: jspecify_nullness_mismatch
    return Optional.fromJavaUtil(p);
  }

  java.util.Optional<Foo> toUsingInstanceMethod(Optional<Foo> p) {
    // :: error: jspecify_but_expect_warning
    return p.toJavaUtil();
  }

  class Foo {}
}
