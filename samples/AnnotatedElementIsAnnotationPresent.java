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
import java.lang.reflect.AnnotatedElement;
import org.jspecify.annotations.NullMarked;

@NullMarked
class AnnotatedElementIsAnnotationPresent {
  Object noCheck(AnnotatedElement element, Class<Foo> clazz) {
    // :: error: jspecify_nullness_mismatch
    return element.getAnnotation(clazz);
  }

  Object check(AnnotatedElement element, Class<Foo> clazz) {
    if (element.isAnnotationPresent(clazz)) {
      return element.getAnnotation(clazz);
    }
    return "";
  }

  Object checkWildcard(AnnotatedElement element, Class<? extends Foo> clazz) {
    if (element.isAnnotationPresent(clazz)) {
      return element.getAnnotation(clazz);
    }
    return "";
  }

  @interface Foo {}
}
