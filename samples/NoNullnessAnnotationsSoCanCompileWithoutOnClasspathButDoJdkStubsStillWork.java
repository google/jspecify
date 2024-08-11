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
class NoNullnessAnnotationsSoCanCompileWithoutOnClasspathButDoJdkStubsStillWork {
  void foo() {
    // :: error: jspecify_nullness_mismatch
    Integer.parseInt(null);
  }
}
/*
 * Our CI is not set up to take advantage of this, but: This file is a sample input that contains no
 * JSpecify nullness annotations and thus can be analyzed without those annotations on the
 * classpath.
 *
 * Such a run "works" with our checker but only in a limited sense. (I'm a little surprised that it
 * works at all, even though our checker refers to NullMarked.class.) The problem: our checker will
 * fail to parse its JDK stubs and then fail to report the expected error below.
 *
 * This demonstrates that it's still not a good idea to run the checker unless the JSpecify
 * annotations are on the classpath.
 *
 * We could in theory "fix" this by changing our JDK stubs to use *other* nullness annotations that
 * our checker would recognize. But then we'd have to do the same for any other stubs or else risk
 * the same problem. Plus, the problem is unlikely to begin with.
 */
