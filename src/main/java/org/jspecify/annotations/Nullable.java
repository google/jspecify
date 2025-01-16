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
package org.jspecify.annotations;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated <a href="https://github.com/jspecify/jspecify/wiki/type-usages">type
 * usage</a> (commonly a parameter type or return type) is considered to include {@code null} as a
 * value.
 *
 * <p>Example usages:
 *
 * <pre>{@code
 * @Nullable String field;
 *
 * @Nullable String getField() { return field; }
 *
 * void setField(@Nullable String value) { field = value; }
 *
 * List<@Nullable String> getList() { … }
 * }</pre>
 *
 * <p>For a comprehensive introduction to JSpecify, please see <a
 * href="http://jspecify.org">jspecify.org</a>.
 *
 * <h2>Meaning for each kind of type context</h2>
 *
 * <p>The essential meaning of this annotation is always the same: the type it annotates is
 * considered to include {@code null} as a value. That's really the whole story, but this section
 * offers some additional details for each kind of type context.
 *
 * <ul>
 *   <li>On a <b>parameter type</b>: The {@code setField} method (at top) permissively accepts a
 *       "string-or-null", meaning that it is okay to pass an actual string, or to pass {@code
 *       null}. (This doesn't guarantee that passing {@code null} won't produce an exception at
 *       runtime, but it should be much less likely.) This also applies to the type of a lambda
 *       expression parameter, if that type is given explicitly (rather than using {@code var} or
 *       the concise lambda parameter syntax).
 *   <li>On a <b>method return type</b>: The {@code getField} method returns a "string-or-null", so
 *       while the caller might get a string back, it should also address the possibility of getting
 *       {@code null} instead. (This doesn't necessarily mean that {@code null} will ever actually
 *       be returned.)
 *   <li>On a <b>field type</b>: The {@code field} field has the type "string-or-null", so at times
 *       it might hold a string, and at times it might intentionally hold {@code null}. (Of course,
 *       any field of a reference type might <i>originally</i> have held {@code null} before it was
 *       initialized, but as long as the class ensures that its uninitialized states can't be
 *       observed, it's generally okay to overlook that fact.)
 *   <li>On a <b>type argument</b>: A type usage of "string-or-null" appears within the compound
 *       type {@code List<@Nullable String>}. However this type is used (return type, etc.), this
 *       means the same thing: every appearance of {@code E} in {@code List}'s member signatures
 *       will be considered nullable (unless it is itself annotated otherwise). For a list, this
 *       means it may contain null <i>elements</i>. If the list reference itself might be null as
 *       well, we can write {@code @Nullable List<@Nullable String>}, a "nullable list of nullable
 *       strings".
 *   <li>On the upper bound of a <b>type parameter</b>: For example, as seen in {@code interface
 *       List<E extends @Nullable Object>}. This means that a type argument supplied for that type
 *       parameter has the <i>option</i> of being nullable: {@code List<@Nullable String>}. (A
 *       non-null type argument, as in {@code List<String>}, is permitted either way.)
 *   <li>On a usage of a <b>type variable</b>: A type parameter, like the {@code E} declared in
 *       {@code interface List<E>}, introduces a <i>type variable</i> of the same name, usable only
 *       within the scope of the declaring API element. All the preceding examples using {@code
 *       String} are equally valid for a type variable like {@code E}. {@code @Nullable} continues
 *       to mean "or null" as always, regardless of whether the type argument "already" includes
 *       {@code null}. For example, suppose that {@code class Foo<E extends @Nullable Object>} has a
 *       method {@code @Nullable E eOrNull()}. Then, whether a variable {@code foo} is of type
 *       {@code Foo<String>} or {@code Foo<@Nullable String>}, the expression {@code foo.eOrNull()}
 *       is nullable either way. Using {@code @Nullable E} in this way is called <i>nullable
 *       projection</i>. (The more rarely needed <a href="NonNull.html#projection">non-null
 *       projection</a> is also supported.)
 *   <li>On an <b>array type</b>: The nullness of the array and of its components can each be
 *       indicated separately. For example, in null-marked code, {@code @Nullable String[]} is a
 *       non-null array of nullable strings, whereas {@code String @Nullable []} is a nullable array
 *       of non-null strings. Note that arrays are treated as covariant: {@code String[]} (array of
 *       non-null strings) is assignable to {@code @Nullable String[]} (array of nullable strings).
 *   <li>On a parameter declaration of variable arity ("varargs"): Use {@code @Nullable String...}
 *       to indicate that the individual strings may be null. In contrast, {@code String @Nullable
 *       ...} would instead allow null to be passed as the entire array.
 *   <li>On a <b>nested type</b>: In most examples above, in place of {@code String} we might use a
 *       nested type such as {@code Map.Entry}. The Java syntax for annotating such a type as
 *       nullable looks like {@code Map.@Nullable Entry}.
 *   <li>On a <b>record component</b>: As expected, {@code @Nullable} here applies equally to the
 *       corresponding parameter type of the canonical constructor, and to the return type of the
 *       corresponding accessor method, if these are actually generated. If the constructor
 *       signature or accessor method is written explicitly, it must still be annotated explicitly.
 * </ul>
 *
 * <!-- TODO: move up -->
 * <h2 id="applicability">Where it is not applicable</h2>
 *
 * <p>This annotation and {@link NonNull} are applicable to any <a
 * href="https://github.com/jspecify/jspecify/wiki/type-usages">type usage</a> <b>except</b> the
 * following cases, where they have no defined meaning:
 *
 * <ul>
 *   <li>On a <b>local variable</b> declaration. JSpecify treats the nullness of a local variable as
 *       being a purely dynamic property, not being a fixed declarative property of its type. (<a
 *       href="https://bit.ly/3ppb8ZC">Why?</a>). An analyzer should infer its nullness from how it
 *       is used, and that nullness can change over time. Note that this rule applies only to the
 *       <i>root type</i> of the variable; a type argument or array component type <em>of</em> that
 *       type is annotatable as usual.
 *   <li>On any<b> intrinsically non-null type usage</b>. Some type usages are incapable of
 *       including {@code null} by the rules of the Java language. Examples include any usage of a
 *       primitive type, a method return type in an annotation interface, or the type following
 *       {@code throws} or {@code catch}. In such locations, a nullness annotation could only be
 *       contradictory ({@code @Nullable}) or redundant ({@code @NonNull}).
 *   <li>On the root type in a <b>cast expression</b>. To inform an analyzer that an expression it
 *       sees as nullable is truly non-null, use an assertion or a method like {@link
 *       java.util.Objects#requireNonNull}. (<a href="https://bit.ly/3ppb8ZC">Why?</a>)
 *       Subcomponents of the type (type arguments, etc.) are annotatable as usual.
 *   <li>On any part of the argument to <b>{@code instanceof}</b>. The root type is intrinsically
 *       non-null, and nothing else about nullness is checked at runtime.
 *   <li>On any part of a <b>receiver parameter</b> type. This parameter must specify the exact
 *       type of {@code this}; its nullness information cannot be altered in any way.
 *   <li>If both {@code @Nullable} and {@code @NonNull} appear on the same type usage, <i>both</i>
 *       are unrecognized.
 * </ul>
 *
 * Whether the code is {@link NullMarked} also has no consequence in the above locations.
 *
 * <h2>Unannotated type usages</h2>
 *
 * <p>For a type usage where nullness annotations are <a href="#applicability">applicable</a> but
 * not present, its nullness depends on whether it appears within {@linkplain NullMarked
 * null-marked} code; see that class for details. Note in particular that nullness information from
 * a superclass is never automatically inherited.
 */
@Documented
@Target(TYPE_USE)
@Retention(RUNTIME)
public @interface Nullable {}
