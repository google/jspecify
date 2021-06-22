/*
 * Copyright 2018-2020 The JSpecify Authors.
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
package org.jspecify.nullness;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <b>This annotation is not planned for inclusion in JSpecify 1.0</b>, unless <a
 * href="https://github.com/jspecify/jspecify/issues/137">this issue is reopened and resolved
 * favorably. It's here temporarily until some tests are fixed, and in the meantime we will be
 * deleting from any release branch we start.
 */
@Documented
@Target(TYPE_USE)
@Retention(RUNTIME)
public @interface NullnessUnspecified {}
