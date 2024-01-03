---
title: Nullness Specification (draft)
---

# Nullness Specification (draft)

This document is a draft specification for the precise semantics of our set of
annotations for nullness analysis.

:::note Advice to readers (non-normative)

The primary audience for this document is the authors of analysis tools. Some
very advanced users might find it interesting. But it would make a very poor
introduction for anyone else; instead see our **[Start Here](/docs/start-here) page**.
:::

:::note Status of this specification

This document is current as of JSpecify **0.2.0**, but does not reflect several
design changes between then and **0.3.0**.
:::

--------------------------------------------------------------------------------

### The word "nullable"

In this doc, we aim not to refer to whether a type "is nullable." Instead, we
draw some distinctions, creating at least four kinds of "Is it nullable?"
questions we can ask for any given type usage:

1.  Does `@Nullable` appear directly on that type usage?
2.  What is the [nullness operator] of that type usage?
3.  Is it reasonable to assume that `null` won't come "out" of it?
4.  Is it reasonable to assume that `null` can't be put "in" to it?

### The scope of this spec

Currently, this spec does not address *when* tools must apply any part of the
spec. For example, it does not state when tools must check that the [subtyping]
relation holds.

We anticipate that tools will typically apply parts of this spec in the same
cases that they (or `javac`) already apply the corresponding parts of the Java
Language Specification. For example, if code contains the parameterized type
`List<@Nullable Foo>`, we anticipate that tools will check that `@Nullable Foo`
is a subtype of the bound of the type parameter of `List`.

However, this is up to tool authors, who may have reasons to take a different
approach. For example:

-   Java [places some restrictions that aren't necessary for soundness][#49],
    and it [is lenient in at least one way that can lead to runtime
    errors][#65].

-   JSpecify annotations can be used even by tools that are not "nullness
    checkers" at all. For example, a tool that lists the members of an API could
    show the nullness of each type in the API, without any checking that those
    types are "correct."

-   Even when a tool is a "nullness checker," it might be written for another
    language, like Kotlin, with its own rules for when to perform type checks.
    Or the tool might target a future version of Java whose language features
    would not be covered by this version of this spec.

Note also that this spec covers only nullness information *from JSpecify
annotations*. Tools may have additional sources of information. For example, a
tool may recognize additional annotations. Or a tool may omit the concept of
`UNSPECIFIED` and apply a policy that type usages like `Object` are always
non-nullable.

### That's all!

On to the spec.

--------------------------------------------------------------------------------

## Normative and non-normative sections

This document contains some non-normative comments to emphasize points or to
anticipate likely questions. Those comments are set off as block quotes.

> This is an example of a non-normative comment.

This document also links to other documents. Those documents are non-normative,
except for when we link to the Java Language Specification to defer to its
rules.

## Relationship between this spec and JLS {#concept-references}

When a rule in this spec refers to any concept that is defined in this spec (for
example, [substitution] or [containment]), apply this spec's definition (as
opposed to other definitions, such as the ones in the Java Language
Specification (JLS)).

Additionally, when a rule in this spec refers to a JLS rule that in turn refers
to a concept that is defined in this spec, likewise apply this spec's
definition.

In particular, when a JLS rule refers to types, apply this spec's definition of
[augmented types] \(as opposed to [base types]).

## Base type

A *base type* is a type as defined in [JLS 4].

> JLS 4 does not consider a type-use annotation to be a part of the type it
> annotates, so neither does our concept of "base type."

## Type components

A *type component* of a given type is a type that transitively forms some part
of that type. Specifically, a type component is one of the following:

-   a non-wildcard type argument
-   a wildcard bound
-   an array component type
-   an enclosing type
-   an element of an intersection type
-   the entire type

## Nullness operator

A nullness operator is one of 4 values:

-   `UNION_NULL`
-   `NO_CHANGE`
-   `UNSPECIFIED`
-   `MINUS_NULL`

> The informal meaning of the operators is:
>
> -   `UNION_NULL`: This is the operator produced by putting `@Nullable` on a
>     type usage.
>     -   The type usage `String UNION_NULL` includes `"a"`, `"b"`, `"ab"`,
>         etc., plus `null`.
>     -   The type-variable usage `T UNION_NULL` includes all members of `T`,
>         plus `null` if it wasn't already included.
> -   `NO_CHANGE`: This is the operator produced by *not* putting `@Nullable` on
>     a type usage (aside from the exception discussed under `UNSPECIFIED`
>     below).
>     -   The type usage `String NO_CHANGE` includes `"a"`, `"b"`, `"ab"`, etc.,
>         without including `null`.
>     -   The type-variable usage `T NO_CHANGE` includes exactly the members of
>         `T`: If `null` was a member of `T`, then it's a member of `T
>         NO_CHANGE`. If it was not a member of `T`, then it is not a member of
>         `T NO_CHANGE`.
>     -   One way to conceptualize this is that `String NO_CHANGE` means
>         "non-null `String`" but that `T NO_CHANGE` means "nullness comes from
>         the value of `T`."
> -   `UNSPECIFIED`: This is the operator produced by not putting `@Nullable` on
>     a type usage *in code that is outside a [null-marked scope]*. Roughly, it
>     is the operator assigned to "completely unannotated code."
>     -   The type usage `String UNSPECIFIED` includes `"a"`, `"b"`, `"ab"`,
>         etc., but whether `null` should be included is not specified.
>     -   The type-variable usage `T UNSPECIFIED` includes all members of `T`.
>         But whether `null` should be added to the set (if it isn't already)
>         is not specified.
> -   `MINUS_NULL`: This operator not only does not *add* `null` but also
>     actively *removes* it from a type-variable usage that might otherwise
>     include it.
>     -   The type usage `String MINUS_NULL` includes `"a"`, `"b"`, `"ab"`,
>         etc., without including `null`. (This is equivalent to `String
>         NO_CHANGE`.)
>     -   The type-variable usage `T MINUS_NULL` includes all members of `T`
>         *except* for `null`. (This is equivalent to `T NO_CHANGE` unless
>         `null` was a member of `T`.)

## Augmented type

An augmented type consists of a [base type] and a [nullness operator]
corresponding to *each* of its [type components].

> Arguably, an augmented type with nullness operator `UNSPECIFIED` is better
> understood not as representing "a type" but as representing a *lack* of the
> nullness portion of the type.

For our purposes, base types (and thus augmented types) include not just class
and interface types, array types, and type variables but also
[intersection types] and the null type.

> This spec aims to define rules for augmented types compatible with those that
> the JLS defines for base types.
>
> Accordingly, in almost all cases, this spec agrees with the JLS's rules when
> specifying what *base* types appear in a piece of code. It makes an exception
> for ["Bound of an unbounded wildcard,"](#unbounded-wildcard) for which it
> specifies a bound of `Object` that the JLS does not specify.

When this spec uses capital letters, they refer to augmented types (unless
otherwise noted). This is in contrast to the JLS, which typically uses them to
refer to base types.

When this spec refers to "the nullness operator of" a type `T`, it refers
specifically to the nullness operator of the type component that is the entire
type `T`, without reference to the nullness operator of any other type that is
a component of `T` or has `T` as a component.

> For example, "the nullness operator of `List<Object>`" refers to whether the
> list itself may be `null`, not whether its elements may be.

## Details common to all annotations

For all named annotations referred to by this spec:

-   The package name is `org.jspecify.nullness`. \[[#260]\]
-   The Java module name is `org.jspecify`. \[[#181]\]
-   The Maven artifact is `org.jspecify:jspecify`. \[[#181]\]

All annotations have runtime retention. \[[#28]\] None of the annotations are
marked [repeatable].

## The type-use annotation

We provide a parameterless type-use annotation called `@Nullable`.

### Recognized locations for type-use annotations

A location is a *recognized* location for our type-use annotation in the
circumstances detailed below. This spec does not define semantics for
annotations in other locations.

> For now, we've chosen to restrict ourselves to API locations for which tools
> mostly agree on what it means for a type in that location to be `@Nullable`.
>
> When analyzing source code, tools are encouraged to offer an option to issue
> an error for an annotation in an unrecognized location (unless they define
> semantics for that location). Tools are especially encouraged to issue an
> error for an annotation in a location that is "intrinsically non-nullable"
> (defined below).
>
> When reading *bytecode*, however, tools may be best off ignoring an annotation
> in an unrecognized location (again, unless they define semantics for that
> location).

The following locations are recognized except when overruled by one of the
exceptions in the subsequent sections: \[[#17]\]

-   return type of a method

-   formal parameter type of a method or constructor, as defined in [JLS 8.4.1]

    > This excludes the receiver parameter.

-   field type

-   type parameter upper bound \[[#60]\]

-   non-wildcard type argument

-   wildcard bound

-   array component type

-   type used in a variadic parameter declaration

However, any location above is unrecognized if it matches either of the
following cases: \[[#17]\]

> We refer to these cases (and some other cases below) as "intrinsically
> non-nullable."

-   a type usage of a value type (currently, the 8 predefined primitive types)

-   the outer type that qualifies an inner type

    > For example, the annotation in `@Nullable Foo.Bar` is in an unrecognized
    > location: Java syntax attaches it to the outer type `Foo`.
    >
    > (Note that `@Nullable Foo.Bar` is a *Java* syntax error when `Bar` is a
    > *static* type. If `Bar` is a non-static type, then Java permits the code.
    > So JSpecify tools have the oppotunity to reject it, given that the author
    > probably intended `Foo.@Nullable Bar`.)

    > Every outer type is intrinsically non-nullable because every instance of
    > an inner class has an associated instance of the outer class.

Additionally, any location above is unrecognized if it makes up *any
[type component]* of a type in the following locations: \[[#17]\]

> These locations all fit under the umbrella of "implementation code."
> Implementation code may use types that contain type arguments, wildcard
> bounds, and array component types, which would be recognized locations if not
> for the exceptions defined by this section.

-   a local variable type
-   an exception parameter
-   the type in a cast or `instanceof` expression
-   an array or object creation expression (including via a member reference)
-   an explicit type argument supplied to a generic method or constructor
    (including via a member reference) or to an instance creation expression for
    a generic class

> In practice, we anticipate that tools will treat types (and their annotations)
> in *most* of the above locations much like they treat types in other
> locations. Still, this spec does not concern itself with implementation code:
> We believe that the most important domain for us to focus on is that of APIs.

All locations that are not explicitly listed as recognized are unrecognized.

> Other notable unrecognized annotations include: \[[#17]\]
>
> Some additional intrinsically non-nullable locations:
>
> -   supertype in a class declaration
> -   thrown exception type
> -   enum constant declaration
> -   receiver parameter type
>
> Some other locations that individual tools are more likely to assign semantics
> to:
>
> -   a class declaration \[[#7]\]: For example, the annotation in `public
>     @Nullable class Foo {}` is in an unrecognized location.
> -   a type-parameter declaration or a wildcard *itself* \[[#19], [#31]\]
> -   any [type component] of a receiver parameter type \[[#157]\]
>
> But note that types "inside" some of these locations can still be recognized,
> such as a *type argument* of a supertype.

## The declaration annotation

We provide a single parameterless declaration annotation called `@NullMarked`.
\[[#5], [#87]\]

### Recognized locations for declaration annotations

Our declaration annotation is specified to be *recognized* when applied to the
locations listed below:

-   A *named* class.
-   A package. \[[#34]\]
-   A module. \[[#34]\]

> *Not* a method \[[#43]\], constructor \[[#43]\], or field \[[#50]\].

## Null-marked scope

To determine whether a type usage appears in a null-marked scope:

Look for a `@NullMarked` annotation on any of the scopes
enclosing the type usage.

Class members are enclosed by classes, which may be enclosed by other class
members or classes. and top-level classes are enclosed by packages, which may be
enclosed by modules.

> Packages are *not* enclosed by "parent" packages.

> This definition of "enclosing" likely matches
> [the definition in the Java compiler API](https://docs.oracle.com/en/java/javase/14/docs/api/java.compiler/javax/lang/model/element/Element.html#getEnclosingElement\(\)).

If one of those scopes is directly annotated with
`@NullMarked`, then the type usage is in a null-marked
scope. Otherwise, it is not.

## Augmented type of a type usage appearing in code {#augmented-type-of-usage}

For most type usages in source code or bytecode on which JSpecify nullness
annotations are [recognized], this section defines how to determine their
[augmented types]. Note, however, that rules for specific cases below take
precedence over the general rule here.

Because the JLS already has rules for determining the [base type] for a type
usage, this section covers only how to determine its [nullness operator].

To determine the nullness operator, apply the following rules in order. Once one
condition is met, skip the remaining conditions.

-   If the type usage is annotated with `@Nullable`, its
    nullness operator is `UNION_NULL`.
-   If the type usage appears in a [null-marked scope], its nullness operator is
    `NO_CHANGE`.
-   Its nullness operator is `UNSPECIFIED`.

> The choice of nullness operator is *not* affected by any nullness operator
> that appears in a corresponding location in a supertype. For example, if one
> type declares a method whose return type is annotated `@Nullable`, and if
> another type overrides that method but does not declare the return type as
> `@Nullable`, then the override's return type will *not* have nullness operator
> `UNION_NULL`.

> The rules here never produce the fourth nullness operator, `MINUS_NULL`.
> However, if tool authors prefer, they can safely produce `MINUS_NULL` in any
> case in which it is equivalent to `NO_CHANGE`. For example, there is no
> difference between `String NO_CHANGE` and `String MINUS_NULL`.

> So why does `MINUS_NULL` exist at all? It does appear later in this spec in
> the section on [substitution]. However, its main purpose is to provide tools
> with a way to represent the nullness of certain expressions in implementation
> code: Consider `ArrayList<E>`. `ArrayList` supports null elements, so the
> class has to handle the possibility that any expression of type `E` may be
> null. However, if implementation code contains the statement `if (e != null) {
> ... }`, then tools can assume that `e` is non-null inside. The purpose of
> `MINUS_NULL` is to represent that such an expression is known not to be null,
> even though its base type `E` suggests otherwise.

## Augmented type of an intersection type {#intersection-types}

> Technically speaking, the JLS does not define syntax for an intersection type.
> Instead, it defines a syntax for type parameters and casts that supports
> multiple types. Then the intersection type is derived from those. Intersection
> types can also arise from operations like [capture conversion]. See [JLS 4.9].
>
> One result of this is that it's never possible for a programmer to write an
> annotation "on an intersection type."

This spec assigns a [nullness operator] to each individual element of an
intersection type, following our normal rules for type usages. It also assigns a
nullness operator to the intersection type as a whole. The nullness operator of
the type as a whole is always `NO_CHANGE`.

> This lets us provide, for every [base type], a rule for computing its
> [augmented type]. But we require `NO_CHANGE` so as to avoid questions like
> whether "a `UNION_NULL` intersection type whose members are `Foo UNION_NULL`
> and `Bar UNION_NULL`" is a subtype of "a `NO_CHANGE` intersection type with
> those same members." Plus, it would be difficult for tools to output the
> nullness operator of an intersection type in a human-readable way.

> To avoid ever creating an intersection type with a nullness operator other
> than `NO_CHANGE`, we define special handling for intersection types under
> ["Applying a nullness operator to an augmented type."][applying operator]

## Bound of an "unbounded" wildcard {#unbounded-wildcard}

In source, an unbounded wildcard is written as `<?>`. This section does *not*
apply to `<? extends Object>`, even though that is often equivalent to `<?>`.

> See [JLS 4.5.1].

In bytecode, such a wildcard is represented as a wildcard type with an empty
list of upper bounds and an empty list of lower bounds. This section does *not*
apply to a wildcard with any bounds in either list, even a sole upper bound of
`Object`.

> For a wildcard with an explicit bound of `Object` (that is, `<? extends
> Object>`, perhaps with an annotation on `Object`), instead apply
> [the normal rules](#augmented-type-of-usage) for the explicit bound type.

If an unbounded wildcard appears in a [null-marked scope], then it has a single
upper bound whose [base type] is `Object` and whose [nullness operator] is
`UNION_NULL`.

If an unbounded wildcard appears outside a null-marked scope, then it has a
single upper bound whose base type is `Object` and whose nullness operator is
`UNSPECIFIED`.

> In both cases, we specify a bound that does not exist in the source or
> bytecode, deviating from the JLS. Because the base type of the bound is
> `Object`, this should produce no user-visible differences except to tools that
> implement JSpecify nullness analysis.

Whenever a JLS rule refers specifically to `<?>`, disregard it, and instead
apply the rules for `<? extends T>`, where `T` has a base type of `Object` and
the nullness operator defined by this section.

## Bound of an `Object`-bounded type parameter {#object-bounded-type-parameter}

In source, an `Object`-bounded type parameter can be writen in either of 2 ways:

-   `<T>`
-   `<T extends Object>` with no JSpecify nullness type annotations on the bound

> See [JLS 4.4].

In bytecode, `<T>` and `<T extends Object>` are both represented as a type
parameter with a single upper bound, `Object`, and no JSpecify nullness type
annotations on the bound.

If an `Object`-bounded type parameter appears in a [null-marked scope], then its
bound has a [base type] of `Object` and a [nullness operator] of `NO_CHANGE`.

> Note that this gives `<T>` a different bound than `<?>` (though only in a
> null-marked scope).

If an `Object`-bounded type parameter appears outside a null-marked scope, then
its bound has a base type of `Object` and a nullness operator of `UNSPECIFIED`.

> All these rules match the behavior of
> [our normal rules](#augmented-type-of-usage) for determining the
> [augmented type] of the bound `Object`. The only "special" part is that we
> consider the source code `<T>` to have a bound of `Object`, just as it does
> when compiled to bytecode.

## Augmented null types {#null-types}

The JLS refers to "the null type." In this spec, we assign a [nullness operator]
to all types, including the null type. This produces multiple null types:

-   the null [base type] with nullness operator `NO_CHANGE`: the
    "bottom"/"nothing" type used in [capture conversion]

    > No value has this type, not even `null` itself.

-   the null base type with nullness operator `MINUS_NULL`

    > This is equivalent to the previous type. Tools may use the 2
    > interchangeably.

-   the null base type with nullness operator `UNION_NULL`: the type of the null
    reference

-   the null base type with nullness operator `UNSPECIFIED`

    > This may be relevant only in implementation code.

## Multiple "worlds" {#multiple-worlds}

Some of the rules in this spec come in 2 versions: One version requires a
property to hold "in all worlds," and the other requires it to hold only "in
some world."

Tool authors may choose to implement neither, either, or both versions of the
rules.

> Our goal is to allow tools and their users to choose their desired level of
> strictness in the presence of `UNSPECIFIED`. The basic idea is that, every
> time a tool encounters a type component with the nullness operator
> `UNSPECIFIED`, it has the option to fork off 2 "worlds": 1 in which the
> operator is `UNION_NULL` and 1 in which it is `NO_CHANGE`.
>
> In more detail: When tools lack a nullness specification for a type, they may
> choose to assume that either of the resulting worlds may be the "correct"
> specification. The all-worlds version of a rule, by requiring types to be
> compatible in all possible worlds, holds that types are incompatible unless it
> has enough information to prove they are compatible. The some-world version,
> by requiring types to be compatible only in *some* world, holds that types are
> compatible unless it has enough information to prove they are incompatible.
> (By behaving "optimistically," the some-world version is much like Kotlin's
> rules for "platform types.")
>
> Thus, a strict tool might choose to implement the all-worlds version of rules,
> and a lenient tool might choose to implement the some-world version. Yet
> another tool might implement both and let users select which rules to apply.
>
> Still another possibility is for a tool to implement both versions and to use
> that to distinguish between "errors" and "warnings." Such a tool might always
> first process code with the all-worlds version and then with the some-world
> version. If the tools detects, say, an out-of-bounds type argument in both
> cases, the tool would produce an error. But, if the tool detects such a
> problem with the all-worlds version but not with the some-world version, the
> tool would produce a warning. Under this scheme, a warning means roughly that
> "There is some way that the code could be annotated that would produce an
> error here."

The main body of each section of the spec describes the all-worlds rule. If the
some-world rule differs, the differences are explained at the end.

> A small warning: To implement the full some-world rules, a tool must also
> implement at least part of the all-worlds rules. Those rules are required as
> part of [substitution].

### Propagating how many worlds a relation must hold in {#propagating-multiple-worlds}

When one rule in this spec refers to another, it refers to the same version of
the rule. For example, when the rules for [containment] refer to the rules for
[subtyping], the some-world containment relation refers to the some-world
subtyping relation, and the all-worlds containment relation refers to the
all-worlds subtyping relation.

This meta-rule applies except when a rule refers explicitly to a particular
version of another rule.

## Same type

`S` and `T` are the same type if `S` is a [subtype] of `T` and `T` is a subtype
of `S`.

The same-type relation is *not* defined to be reflexive or transitive.

> For more discussion of reflexive and transitive relations, see the comments
> under [nullness subtyping].

## Subtyping

`A` is a subtype of `F` if both of the following conditions are met:

-   `A` is a [nullness subtype] of `F`.
-   `A` is a subtype of `F` according to the
    [nullness-delegating subtyping rules for Java].

> The first condition suffices for most cases. The second condition is necessary
> only for types that have subcomponents --- namely, parameterized types and
> arrays. And it essentially says "Check the first condition on subcomponents as
> appropriate."

## Nullness subtyping

`A` is a nullness subtype of `F` if any of the following conditions are met:

> Nullness subtyping asks the question: If `A` includes `null`, does `F` also
> include `null`? There are 4 cases in which this is true, 2 easy and 2 hard:

-   `F` is [null-inclusive under every parameterization].

    > This is the first easy case: `F` always includes `null`.

-   `A` is [null-exclusive under every parameterization].

    > This is the second easy case: `A` never includes `null`.

-   `A` has a [nullness-subtype-establishing path] to any type whose base type
    is the same as the base type of `F`, and `F` does *not* have
    [nullness operator] `MINUS_NULL`.

    > This is the first hard case: A given type-variable usage does not
    > necessarily always include `null`, nor does it necessarily always exclude
    > `null`. (For example, consider a usage of `E` inside `ArrayList<E>`.
    > `ArrayList` may be instantiated as either an `ArrayList<@Nullable String>`
    > or an `ArrayList<String>`.)
    >
    > Subtyping questions for type-variable usages are more complex: `E` is a
    > nullness subtype of `E`; `@Nullable E` is not. Similarly, if `<F extends
    > E>`, then `F` is a nullness subtype of `E`. But if `<F extends @Nullable
    > E>`, it is not.

-   `F` is a type-variable usage that meets *both* of the following conditions:

    -   It does *not* have nullness operator `MINUS_NULL`.

    -   `A` is a nullness subtype of its lower bound.

    > This is the second hard case: It covers type variables that are introduced
    > by capture conversion of `? super` wildcards.
    >
    > In short, whether you have a `Predicate<? super String>`, a `Predicate<?
    > super @Nullable String>`, or unannotated code that doesn't specify the
    > nullness operator for the bound, you can always pass its `test` method a
    > `String`. (If you want to pass a `@Nullable String`, then you'll need for
    > the bound to be [null-inclusive under every parameterization]. The
    > existence of the null-inclusiveness rule frees this current rule from
    > having to cover that case.)

> A further level of complexity in all this is `UNSPECIFIED`. For example, in
> the [all-worlds] version of the following rules, a type with nullness operator
> `UNSPECIFIED` can be both null-_inclusive_ under every parameterization and
> null-_exclusive_ under every parameterization.

Nullness subtyping (and thus subtyping itself) is *not* defined to be reflexive
or transitive.

> If we defined nullness subtyping to be reflexive, then `String UNSPECIFIED`
> would be a subtype of `String UNSPECIFIED`, even under the [all-worlds] rules.
> In other words, we'd be saying that unannotated code is always free from
> nullness errors. That is clearly false. (Nevertheless, lenient tools will
> choose not to issue errors for such code. They can do this by implementing the
> [some-world] rules.)
>
> If we defined nullness subtyping to be transitive, then `String UNION_NULL`
> would be a subtype of `String NO_CHANGE` under the some-world rules. That
> would happen because of a chain of subtyping rules:
>
> -   `String UNION_NULL` is a subtype of `String UNSPECIFIED`.
>
> -   `String UNSPECIFIED` is a subtype of `String NO_CHANGE`.
>
> Therefore, `String UNION_NULL` is a subtype of `String NO_CHANGE`.
>
> Yes, it's pretty terrible for something called "subtyping" not to be reflexive
> or transitive. A more accurate name for this concept would be "consistent," a
> term used in gradual typing. However, we use "subtyping" anyway. In our
> defense, we need to name multiple concepts, including not just subtyping but
> also the [same-type] relation and [containment]. If we were to coin a new term
> for each, tool authors would need to mentally map between those terms and the
> analogous Java terms. (Still, yes: Feel free to read terms like "subtyping" as
> if they hvae scare quotes around them.)
>
> Subtyping does end up being transitive when the relation is required to hold
> in all worlds. And it does end up being reflexive when the relation is
> required to hold only in [some world]. We don't state those properties as
> rules for 2 reasons: First, they arise naturally from the definitions. Second,
> we don't want to suggest that subtyping is reflexive and transitive under both
> versions of the rule.

Contrast this with our [nullness-delegating subtyping] rules and [containment]
rules: Each of those is defined as a transitive closure. However, this is
incorrect, and we should fix it: Transitivity causes the same problem there as
it does here: `List<? extends @Nullable String>` ends up as a subtype of `List<?
extends String>` because of a chain of subtyping rules that uses `String
UNSPECIFIED` as part of the intermediate step. Luckily, tool authors that set
out to implement transitivity for these two rules are very unlikely to write
code that "notices" this chain. So, in practice, users are likely to see the
"mostly transitive" behavior that we intend, even if we haven't found a way to
formally specify it yet.

## Null-inclusive under every parameterization

A type is null-inclusive under every parameterization if it meets any of the
following conditions:

-   Its [nullness operator] is `UNION_NULL`.

    > This is the simplest part of the simplest case: A type usage always
    > includes `null` if it's annotated with `@Nullable`.

-   It is an [intersection type] whose elements all are null-inclusive under
    every parameterization.

-   It is a type variable that meets *both* of the following conditions:

    -   It does *not* have nullness operator `MINUS_NULL`.

    -   Its lower bound is null-inclusive under every parameterization.

    > This third case is probably irrelevant in practice: It covers `? super
    > @Nullable Foo`, which is already covered by the rules for
    > [nullness subtyping]. It's included here in case some tool has reason to
    > check whether a type is null-inclusive under every parameterization
    > *outside* of a check for nullness subtyping.

**Some-world version:** The rule is the same except that the requirement for
"`UNION_NULL`" is loosened to "`UNION_NULL` or `UNSPECIFIED`."

> That is: It's possible that any type usage in unannotated code "ought to be"
> annotated with `@Nullable`.

## Null-exclusive under every parameterization

> This is a straightforward concept ("never includes `null`"), but it's not as
> simple to implement as the null-_inclusive_ rule was. This null-_exclusive_
> rule has to cover cases like `String`, `E` (where `<E extends Object>`), and
> `E` (where `<E extends @Nullable Object>` but nearby code has performed a null
> check on the expression). The case of `<E extends Object>` is an example of
> why the following rule requires looking for a "path."

A type is null-exclusive under every parameterization if it has a
[nullness-subtype-establishing path] to either of the following:

-   any type whose [nullness operator] is `MINUS_NULL`
-   any augmented class or array type

    > This rule refers specifically to a "class or array type," as distinct from
    > other types like type variables and [intersection types].

> When code dereferences an expression, we anticipate that tools will check
> whether the expression is null-exclusive under every parameterization.

## Nullness-subtype-establishing path

> Note that this definition is used both by the definition of
> [null-inclusive under every parameterization] and by the third condition in
> the definition [nullness subtyping] itself (the "type-variable case").

`A` has a nullness-subtype-establishing path to `F` if both of the following
hold:

-   `A` has [nullness operator] `NO_CHANGE` or `MINUS_NULL`.
-   There is a path from `A` to `F` through
    [nullness-subtype-establishing direct-supertype edges].

    > The path may be empty. That is, `A` has a nullness-subtype-establishing
    > path to itself --- as long as it has one of the required nullness
    > operators.

**Some-world version:** The rules are the same except that the requirement for
"`NO_CHANGE` or `MINUS_NULL`" is loosened to "`NO_CHANGE`, `MINUS_NULL`, or
`UNSPECIFIED`."

## Nullness-subtype-establishing direct-supertype edges

> This section defines the supertypes for a given type --- but limited to those
> that fill the gaps in our nullness checking of "top-level" types. For example,
> there's no need for the rules to reflect that `String NO_CHANGE` extends
> `Object NO_CHANGE`: If we've established that a type has a path to `String
> NO_CHANGE`, then we already know that it's
> [null-exclusive under every parameterization], based on the rules above, and
> that's enough to prove subtyping. And if we *haven't* established that, then
> the `String`-`Object` edge isn't going to change that.
>
> Thus, the rules here are restricted to type variables and intersection types,
> whose supertypes may have nullness annotations.

`T` has nullness-subtype-establishing direct-supertype edges to the following:

-   if `T` is an augmented [intersection type]: all the intersection type's
    elements whose [nullness operator] is `NO_CHANGE` or `MINUS_NULL`

-   if `T` is an augmented type variable: all the corresponding type parameter's
    upper bounds whose nullness operator is `NO_CHANGE` or `MINUS_NULL`

-   otherwise: no nodes

**Some-world version:** The rules are the same except that the requirements for
"`NO_CHANGE` or `MINUS_NULL`" are loosened to "`NO_CHANGE`, `MINUS_NULL`, or
`UNSPECIFIED`."

## Nullness-delegating subtyping rules for Java {#nullness-delegating-subtyping}

> Recall that this rule exists to handle subcomponents of types --- namely, type
> arguments and array component types. It essentially says "Check nullness
> subtyping for subcomponents as appropriate."

The Java subtyping rules are defined in [JLS 4.10]. (Each rule takes a type as
input and produces zero or more direct supertypes as outputs.) We add to them as
follows:

-   [As always](#concept-references), interpret the Java rules as operating on
    [augmented types], not [base types]. This raises the question of *how* to
    extend these particular rules to operate on augmented types. The answer has
    two parts:

    -   The first part of the answer applies only to the nullness operator *"of
        the type."* ([As always](#augmented-type), this means the nullness
        operator of the type component that is the entire type.)

        And the first part of the answer is: No matter what nullness operator
        the input augmented type has, the rules still apply, and they still
        produce the same direct supertypes.

        > Thanks to that rule, the nullness operator of any *output* type is
        > never read by the subtyping rules.

        So, when computing output types, tools may produce them with *any*
        nullness operator.

        > Essentially, this rule says that the top-level types do no matter:
        > They have already been checked by the [nullness subtyping] check.
        >
        > For simplicity, we recommend producing a nullness operator of
        > `NO_CHANGE`: That operator is valid for all types, including
        > [intersection types].

    -   The nullness operators of *subcomponents* of the augmented types *do*
        matter. For example, some Java rules produce subtypes only if
        subcomponents meet certain requirements.
        [As always](#concept-references), check those requirements by applying
        *this spec's* definitions.

        > Those definitions (like [containment]) refer back to definitions (like
        > [nullness subtyping]) that use the nullness operators of the
        > subcomponents in question.

-   When the Java array rules require one type to be a *direct* supertype of
    another, consider the direct supertypes of `T` to be *every* type that `T`
    is a [subtype] of.

## Containment

The Java rules are defined in [JLS 4.5.1]. We add to them as follows:

-   Disregard the 2 rules that refer to a bare `?`. Instead, treat `?` like `?
    extends Object`, where the [nullness operator] of the `Object` bound is
    specified by ["Bound of an unbounded wildcard."](#unbounded-wildcard)

    > This is just a part of our universal rule to treat a bare `?` like `?
    > extends Object`.

-   The rule written specifically for `? extends Object` applies only if the
    nullness operator of the `Object` bound is `UNION_NULL`.

-   When the JLS refers to the same type `T` on both sides of a rule, the rule
    applies if and only if this spec defines the 2 types to be the [same type].

**Some-world version:** The rules are the same except that the requirement for
"`UNION_NULL`" is loosened to "`UNION_NULL` or `UNSPECIFIED`."

## Substitution

> Substitution on Java base types barely requires an explanation: See [JLS 1.3].
> Substitution on [augmented types], however, is trickier: If `Map.get` returns
> `V UNION_NULL`, and if a user has a map whose value type is `String
> UNSPECIFIED`, then what does its `get` method return? Naive substitution would
> produce `String UNSPECIFIED UNION_NULL`. To reduce that to a proper augmented
> type with a single nullness operator, we define this process.

To substitute each type argument `Aᵢ` for each corresponding type parameter
`Pᵢ`:

For every type-variable usage `V` whose [base type] is `Pᵢ`, replace `V` with
the result of the following operation:

-   If `V` is [null-exclusive under every parameterization] in [all worlds],
    then replace it with the result of [applying][applying operator]
    `MINUS_NULL` to `Aᵢ`.

    > This is the one instance in which a rule specifically refers to the
    > [all-worlds] version of another rule. Normally,
    > [a rule "propagates" its version to other rules](#propagating-multiple-worlds).
    > But in this instance, the null-exclusivity rule (and all rules that it in
    > turn applies) are the [all-worlds] versions.

    > The purpose of this special case is to improve behavior in "the
    > `ImmutableList.Builder` case": Because `ImmutableList.Builder.add` always
    > throws `NullPointerException` for a null argument, we would like for
    > `add(null)` to be a compile error, even under lenient tools.
    > Unfortunately, without this special case, lenient tools could permit
    > `add(null)` in unannotated code. For an example, read on.
    >
    > Consider an unannotated user of `ImmutableList.Builder<Foo> builder`. Its
    > type argument `Foo` will have a [nullness operator] of `UNSPECIFIED`.
    > Without this special case, the parameter of `builder.add` would have a
    > nullness operator of `UNSPECIFIED`, too. Then, when a lenient tool would
    > check whether the [some-world] subtyping relation holds for
    > `builder.add(null)`, it would find that it does.
    >
    > To solve this, we need a special case for substitution for null-exclusive
    > type parameters like the one on `ImmutableList.Builder`. That special case
    > needs to produce a type with a nullness operator other than `UNSPECIFIED`.
    > One valid option is to produce `NO_CHANGE`; we happened to choose
    > `MINUS_NULL`.
    >
    > The choice between `NO_CHANGE` and `MINUS_NULL` makes little difference
    > for the parameter types of `ImmutableList.Builder`, but it can matter more
    > for other APIs' *return types*. For example, consider `@NullMarked class
    > Foo<E extends @Nullable Object>`, which somewhere uses the type
    > [`FluentIterable<E>`]. `FluentIterable` has a method `Optional<E>
    > first()`. Even when `E` is a type like `String UNION_NULL` (or `String
    > UNSPECIFIED`), we know that `first().get()` will never return `null`. To
    > surface that information to tools, we need to define our substitution rule
    > to return `E MINUS_NULL`: If we instead used `E NO_CHANGE`, then the
    > return type would look like it might include `null`.

-   Otherwise, replace `V` with the result of applying the nullness operator of
    `V` to `Aᵢ`.

## Applying a nullness operator to an augmented type {#applying-operator}

The process of applying a [nullness operator] requires 2 inputs:

-   the nullness operator to apply
-   the [augmented type] \(which, as always, includes a nullness operator for
    that type) to apply it to

The result of the process is an augmented type.

The process is as follows:

First, based on the pair of nullness operators (the one to apply and the one
from the augmented type), compute a "desired nullness operator." Do so by
applying the following rules in order. Once one condition is met, skip the
remaining conditions.

-   If the nullness operator to apply is `MINUS_NULL`, the desired nullness
    operator is `MINUS_NULL`.
-   If either nullness operator is `UNION_NULL`, the desired nullness operator
    is `UNION_NULL`.
-   If either nullness operator is `UNSPECIFIED`, the desired nullness operator
    is `UNSPECIFIED`.
-   The desired nullness operator is `NO_CHANGE`.

Then, if the input augmented type is *not* an [intersection type], the output is
the same as the input but with its nullness operator replaced with the desired
nullness operator.

Otherwise, the output is an intersection type. For every element `Tᵢ` of the
input type, the output type has an element that is the result of applying the
desired nullness operator to `Tᵢ`.

> In this case, the desired nullness operator is always equal to the nullness
> operator to apply that was an input to this process. That's because the
> nullness operator of the intersection type itself is defined to always be
> `NO_CHANGE`.

## Capture conversion

The Java rules are defined in [JLS 5.1.10]. We add to them as follows:

-   The parameterized type that is the output of the conversion has the same
    [nullness operator] as the parameterized type that is the input type.

-   Disregard the JLS rule about `<?>`. Instead, treat `?` like `? extends
    Object`, where the [nullness operator] of the `Object` bound is specified by
    ["Bound of an unbounded wildcard."](#unbounded-wildcard)

    > This is just a part of our universal rule to treat a bare `?` like `?
    > extends Object`.

-   When a rule generates a lower bound that is the null type, we specify that
    its nullness operator is `NO_CHANGE`.

    > See ["Augmented null types."](#null-types)

[#100]: https://github.com/jspecify/jspecify/issues/100
[#157]: https://github.com/jspecify/jspecify/issues/157
[#17]: https://github.com/jspecify/jspecify/issues/17
[#181]: https://github.com/jspecify/jspecify/issues/181
[#19]: https://github.com/jspecify/jspecify/issues/19
[#1]: https://github.com/jspecify/jspecify/issues/1
[#260]: https://github.com/jspecify/jspecify/issues/260
[#28]: https://github.com/jspecify/jspecify/issues/28
[#31]: https://github.com/jspecify/jspecify/issues/31
[#33]: https://github.com/jspecify/jspecify/issues/33
[#34]: https://github.com/jspecify/jspecify/issues/34
[#43]: https://github.com/jspecify/jspecify/issues/43
[#49]: https://github.com/jspecify/jspecify/issues/49
[#50]: https://github.com/jspecify/jspecify/issues/50
[#5]: https://github.com/jspecify/jspecify/issues/5
[#60]: https://github.com/jspecify/jspecify/issues/60
[#65]: https://github.com/jspecify/jspecify/issues/65
[#69]: https://github.com/jspecify/jspecify/issues/69
[#7]: https://github.com/jspecify/jspecify/issues/7
[#80]: https://github.com/jspecify/jspecify/issues/80
[#87]: https://github.com/jspecify/jspecify/issues/87
[3-valued logic]: https://en.wikipedia.org/wiki/Three-valued_logic
[JLS 1.3]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-1.html#jls-1.3
[JLS 4.10.4]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.10.4
[JLS 4.10]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.10
[JLS 4.4]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.4
[JLS 4.5.1]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.5.1
[JLS 4.5.2]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.5.2
[JLS 4.5]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.5
[JLS 4.9]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.9
[JLS 4]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html
[JLS 5.1.10]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-5.html#jls-5.1.10
[JLS 8.4.1]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-8.html#jls-8.4.1
[JLS 8.4.8.1]: https://docs.oracle.com/javase/specs/jls/se14/html/jls-8.html#jls-8.4.8.1
[JVMS 5.4.5]: https://docs.oracle.com/javase/specs/jvms/se14/html/jvms-5.html#jvms-5.4.5
[`FluentIterable<E>`]: https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/collect/FluentIterable.html
[all worlds]: #multiple-worlds
[all-worlds]: #multiple-worlds
[applying operator]: #applying-operator
[augmented type]: #augmented-type
[augmented types]: #augmented-type
[base type]: #base-type
[base types]: #base-type
[capture conversion]: #capture-conversion
[containment]: #containment
[in all worlds]: #multiple-worlds
[in some world]: #multiple-worlds
[intersection type]: #intersection-types
[intersection types]: #intersection-types
[javadoc]: http://jspecify.org/docs/api/org/jspecify/annotations/package-summary.html
[null-exclusive under every parameterization]: #null-exclusive-under-every-parameterization
[null-inclusive under every parameterization]: #null-inclusive-under-every-parameterization
[null-marked scope]: #null-marked-scope
[nullness operator]: #nullness-operator
[nullness subtype]: #nullness-subtyping
[nullness subtyping]: #nullness-subtyping
[nullness-delegating subtyping rules for Java]: #nullness-delegating-subtyping
[nullness-delegating subtyping]: #nullness-delegating-subtyping
[nullness-subtype-establishing direct-supertype edges]: #nullness-subtype-establishing-direct-supertype-edges
[nullness-subtype-establishing path]: #nullness-subtype-establishing-path
[recognized]: #recognized-locations-for-type-use-annotations
[repeatable]: https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/lang/annotation/Repeatable.html
[same type]: #same-type
[same-type]: #same-type
[semantics]: #semantics
[shared folder]: https://drive.google.com/drive/folders/1vZl1odNCBncVaN7EwlwfqI05T_CHIqN-
[some world]: #multiple-worlds
[some-world]: #multiple-worlds
[substitution]: #substitution
[subtype]: #subtyping
[subtyping]: #subtyping
[type component]: #type-components
[type components]: #type-components
