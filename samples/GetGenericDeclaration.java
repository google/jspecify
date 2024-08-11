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
import java.lang.reflect.TypeVariable;
import org.jspecify.annotations.NullMarked;

// did not manage to reproduce exactly the same set of problems as i saw with TypeResolver at 3904
// (that is, with addComputedTypeAnnotations commented out): reproduced the error at
// getGenericDeclaration but also had the undesired error at TypeVariable<?>, which somehow doesn't
// come up when analyzing Guava, perhaps related to the specific order in which the compiler visits
// elements and types...?
//
// and the errors in this file persist even with my addComputedTypeAnnotations override restored...
@NullMarked
class GetGenericDeclaration {
  Object x(TypeVariable<?> typeVariable) {
    // :: error: jspecify_but_expect_warning
    return typeVariable.getGenericDeclaration();
  }
}
