The Simplest(?) Thing That Could Possibly Work for subtyping
============================================================

**This is not “the JSpecify spec.” This is an initial attempt to
formally specify only a subset of the rules we’ll need for a subset of
features we wish to cover. Additionally, it deviates from some of our
current working decisions in an effort to remain simple.**

   .. rubric:: High-level overview
      :name: high-level-overview

   It may be that some people will use this doc to guide their
   implementations in the near future. Please don’t hesitate to let
   cpovirk know of any confusing bits.

   I should probably preemptively clarify at least one thing. In this
   doc, I have tried to distinguish explicitly between 3 “kinds of
   nullability” of a given type usage. Each kind is derived (at least in
   part) from the previous:

   1. What annotation (if any) appears directly on that type usage?
   2. What is the `nullness operator <#nullness-operator>`__ of that
      type usage?
   3. For that type usage…

      -  Is it safe to assume that is not ``null``?
      -  Is it safe to put a ``null`` into it?
      -  neither (as in “parametric nullness”)
      -  both (as in “unspecified nullness” in “lenient mode”)

   TODO(cpovirk): Link to my “Don’t say ‘nullable’” doc once I write it.

.. _concept-references:

References to concepts defined by this spec
-------------------------------------------

When a rule in this spec refers to any concept that is defined in this
spec (for example, `substitution <#substitution>`__ or
`containment <#containment>`__), apply this spec’s definition (as
opposed to other definitions, such as the ones in the JLS).

Additionally, when a rule in this spec refers to a JLS rule that in turn
refers to a concept that is defined in this spec, likewise apply this
spec’s definition.

In particular, when a JLS rule refers to types, apply this spec’s
definition of `augmented types <#augmented-type>`__ (as oppposed to
`base
types <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.k81vs7t5p45i>`__).

Nullness operator
-----------------

An nullness operator is one of 4 values:

-  ``UNION_NULL``
-  ``NO_CHANGE``
-  ``UNSPECIFIED``
-  ``MINUS_NULL``

..

   The distinction among these 4 values is similar to the distinction
   among the Kotlin types ``Foo?``, ``Foo``, ``Foo!``, and ``Foo!!``,
   respectively.

Augmented type
--------------

An augmented type consists of a `base
type <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.k81vs7t5p45i>`__
and a `nullness operator <#nullness-operator>`__ corresponding to *each*
of its `type
components <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.g7gl9fwq1tt5>`__.

   This differs from our current `glossary
   definition <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=id.367l48xhsikk>`__,
   which is written in terms of a “nullness” for each component, not a
   “nullness operator.” Still, the glossary’s concept of the “nullness”
   of a type is derivable from the type’s nullness operator. Notably,
   the glossary’s “nullable” type is our `least convenient
   world <#multiple-worlds>`__\ ’s `trusted null-inclusive under every
   parameterization <#trusted-null-inclusive-under-every-parameterization>`__,
   and the glossary’s “non-nullable” type is our least convenient
   world’s `trusted null-exclusive under every
   parameterization <#trusted-null-exclusive-under-every-parameterization>`__.

For our purposes, base types (and thus augmented types) include not just
class and interface types, array types, and type variables but also
`intersection types <#intersection-types>`__ and the null type. This is
true even though the JLS sometimes does not supply rules for
intersection types and sometimes has separate rules for the null type.

The goal of this spec is to define rules for augmented types compatible
with those that the JLS defines for base types.

   In almost all cases, this spec agrees with the JLS’s rules when
   specifying what *base* types appear in a piece of code. It makes an
   exception for `“Bound of an unbounded
   wildcard,” <#unbounded-wildcard>`__ for which it specifies a bound of
   ``Object`` that the JLS does not specify.

When this spec uses capital letters, they refer to augmented types
(unless otherwise noted). This is in contrast to the JLS, which
typically uses them to refer to base types.

When this spec refers to “the nullness operator of” a type ``T``, it
refers specifically to the nullness operator of the type component that
is the entire type ``T``, without reference to the nullness operator of
any other type components of ``T``.

   For example, the nullness operator of ``List<@Nullable Object>``
   would be ``NO_CHANGE`` (at least in a `null-aware
   context <#null-aware-context>`__), even though the nullness operator
   of its element type ``Object`` is ``UNION_NULL``.

Null-aware context
------------------

To determine whether a type usage appears in a null-aware context:

Look for an ``@org.jspecify.annotations.NullAware`` annotation on any of
the enclosing scopes surrounding the type usage.

Class members are enclosed by classes, which may be enclosed by other
class members or classes. and top-level classes are enclosed by
packages, which may be enclosed by modules.

   Packages are *not* enclosed by “parent” packages.

..

   This definition of “enclosing” likely matches `the definition in the
   Java compiler
   API <https://docs.oracle.com/en/java/javase/14/docs/api/java.compiler/javax/lang/model/element/Element.html#getEnclosingElement()>`__.

If an ``@org.jspecify.annotations.NullAware`` annotation exists on one
of these scopes, then the type usage is in a null-aware context.
Otherwise, it is not.

.. _augmented-type-of-usage:

Augmented type of a type usage appearing in code
------------------------------------------------

For most type usages in source code or bytecode on which JSpecify
nullness annotations are structurally valid, this section defines how to
determine their `augmented types <#augmented-type>`__. Note, however,
that rules for specific cases below take precedence over the general
rule here.

Because the JLS already has rules for determining the `base
type <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.k81vs7t5p45i>`__
for a type usage, this section covers only how to determine its
`nullness operator <#nullness-operator>`__.

To determine the nullness operator, apply the following rules in order.
Once one condition is met, skip the remaining conditions.

-  If the type usage is annotated with
   ``@org.jspecify.annotations.Nullable``, its nullness operator is
   ``UNION_NULL``.
-  If the type usage appears in a `null-aware
   context <#null-aware-context>`__, its nullness operator is
   ``NO_CHANGE``.
-  Its nullness operator is ``UNSPECIFIED``.

..

   The choice of nullness operator is *not* affected by any nullness
   operator that appears in a corresponding location in a supertype. For
   example, if one type declares a method whose return type is annotated
   ``@Nullable``, and if another type overrides that method but does not
   declare the return type as ``@Nullable``, then the override’s return
   type will *not* have nullness operator ``UNION_NULL``.

   The rules here never produce the fourth nullness operator,
   ``MINUS_NULL``. (It will appear later in
   `substitution <#substitution>`__. Additionally, we expect for tool
   authors to produce ``MINUS_NULL`` based on the results of null checks
   in implementation code.) However, if tool authors prefer, they can
   safely produce ``MINUS_NULL`` in any case in which it is equivalent
   to ``NO_CHANGE``. For example, there is no difference between a
   ``String`` with ``NO_CHANGE`` and a ``String`` with ``MINUS_NULL``.

.. _intersection-types:

Augmented type of an intersection type
--------------------------------------

   Technically speaking, the JLS does not define syntax for an
   intersection type. Instead, it defines a syntax for type parameters
   and casts that supports multiple types. Then the intersection type is
   derived from those. Intersection types can also arise from operations
   like `capture conversion <#capture-conversion>`__. See `JLS
   4.9 <https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.9>`__.

   One result of all this is that it’s never possible for a programmer
   to write an annotation “on an intersection type.”

This spec assigns a `nullness operator <#nullness-operator>`__ to each
individual element of an intersection type, following our normal rules
for type usages. It also assigns a nullness operator to the intersection
type as a whole. The nullness operator of the type as a whole is always
``NO_CHANGE``.

   This lets us provide, for every `base
   type <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.k81vs7t5p45i>`__,
   a rule for computing its `augmented type <#augmented-type>`__. But we
   require ``NO_CHANGE`` so as to avoid questions like whether “a
   ``UNION_NULL`` intersection type whose members are ``UNION_NULL``
   ``Foo`` and ``UNION_NULL`` ``Bar``” is a subtype of “a ``NO_CHANGE``
   intersection type with those same members.” Plus, it would be
   difficult for tools to output the nullness operator of an
   intersection type in a human-readable way.

..

   To avoid ever creating an intersection type with a nullness operator
   other than ``NO_CHANGE``, we define special handling for intersection
   types under `“Applying a nullness operator to an augmented
   type.” <#applying-operator>`__

.. _unbounded-wildcard:

Bound of an “unbounded” wildcard
--------------------------------

In source, an unbounded wildcard is written as ``<?>``. This section
does **not** apply to ``<? extends Object>``, even though that is often
equivalent to ``<?>``. See `JLS
4.5.1 <https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.5.1>`__.

In bytecode, such a wildcard is represented as a wildcard type with an
empty list of upper bounds and an empty list of lower bounds. This
section does **not** apply to a wildcard with any bounds in either list,
even a sole upper bound of ``Object``.

   For a wildcard with an explicit bound of ``Object`` (that is,
   ``<? extends Object>``, perhaps with an annotation on ``Object``),
   instead apply `the normal rules <#augmented-type-of-usage>`__ for the
   explicit bound type.

If an unbounded wildcard appears in a `null-aware
context <#null-aware-context>`__, then it has a single upper bound whose
`base
type <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.k81vs7t5p45i>`__
is ``Object`` and whose `nullness operator <#nullness-operator>`__ is
``UNION_NULL``.

If an unbounded wildcard appears outside a null-aware context, then it
has a single upper bound whose base type is ``Object`` and whose
nullness operator is ``UNSPECIFIED``.

   In both cases, we specify a bound that does not exist in the source
   or bytecode, deviating from the JLS. Because the base type of the
   bound is ``Object``, this should produce no user-visible differences
   except to tools that implement JSpecify nullness analysis.

Whenever a JLS rule refers specifically to ``<?>``, disregard it, and
instead apply the rules for ``<? extends T>``, where ``T`` has a base
type of ``Object`` and the nullness operator defined by this section.

.. _object-bounded-type-parameter:

Bound of an ``Object``-bounded type parameter
---------------------------------------------

In source, an ``Object``-bounded type parameter can be writen in either
of 2 ways:

-  ``<T>``
-  ``<T extends Object>`` with no JSpecify nullness type annotations on
   the bound

See `JLS
4.4 <https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.4>`__.

In bytecode, ``<T>`` and ``<T extends Object>`` are both represented as
a type parameter with only a single upper bound, ``Object``, and no
JSpecify nullness type annotations on the bound.

If an ``Object``-bounded type parameter appears in a `null-aware
context <#null-aware-context>`__, then its bound has a `base
type <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.k81vs7t5p45i>`__
of ``Object`` and a `nullness operator <#nullness-operator>`__ of
``NO_CHANGE``.

   Note that this gives ``<T>`` a different bound than ``<?>`` (though
   only in a null-aware context).

If an ``Object``-bounded type parameter appears outside a null-aware
context, then its bound has a base type of ``Object`` and a nullness
operator of ``UNSPECIFIED``.

   All these rules match the behavior of `our normal
   rules <#augmented-type-of-usage>`__ for determining the `augmented
   type <#augmented-type>`__ of the bound ``Object``. The only “special”
   part is that we consider the source code ``<T>`` to have a bound of
   ``Object``, just as it does when compiled to bytecode.

.. _null-types:

Augmented null types
--------------------

The JLS refers to “the null type.” In this spec, we assign a `nullness
operator <#nullness-operator>`__ to all types, including the null type.
This produces multiple null types:

-  the null `base
   type <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.k81vs7t5p45i>`__
   with nullness operator ``NO_CHANGE``: the “bottom”/“nothing” type
   used in `capture conversion <#capture-conversion>`__

      No value, including ``null`` itself, has this type.

-  the null base type with nullness operator ``MINUS_NULL``

      This is equivalent to the previous type. Tools may use the 2
      interchangeably.

-  the null base type with nullness operator ``UNION_NULL``: the type of
   the null reference

-  the null base type with nullness operator ``UNSPECIFIED``

      This may be relevant only in implementation code.

.. _multiple-worlds:

The least convenient world and the most convenient world
--------------------------------------------------------

Some of the rules in this spec come in 2 versions, 1 for “the least
convenient world” and 1 for “the most convenient world.”

Tools may implement either or both versions of the rules.

   Our goal is to allow tools and their users to choose their desired
   level of strictness in the presence of ``UNSPECIFIED``. “The least
   convenient world” usually assumes that types are incompatible unless
   it has enough information to prove they are compatible; “the most
   convenient world” assumes that types are compatible unless it has
   enough information to prove they are incompatible.

   Thus, strict tools may want to implement the least-convenient-world
   version of rules, and lenient tools may wish to implement the
   most-convenient-world version. Or a tool might implement both and let
   users select which rules to apply.

   Another possibility is for a tool to implement both versions and to
   use that to distinguish between “errors” and “warnings.” Such a tool
   might run each check first in the least convenient world and then, if
   the check fails, run it again in the most convenient world. If the
   check fails in both worlds, the tool would produce an error. If it
   passes only because of the most convenient interpretation, the tool
   would produce a warning.

The main body of each section describes the *least*-convenient-world
rule. If the most-convenient-world rule differs, the differences are
explained at the end.

.. _propagating-multiple-worlds:

Propagating the most/least convenient world
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

When one rule in this spec refers to another, it refers to the rule for
the same “world.” For example, when the rules for
`containment <#containment>`__ refer to the rules for
`subtyping <#subtyping>`__, the most-convenient-world containment check
applies the most-convenient-world subtyping check, and the
least-convenient-world containment check applies the
least-convenient-world subtyping check.

This applies even if a rule says it is the same for both worlds: It
means “the same except that any other rules are applied in the
corresponding world.”

Same type
---------

``S`` and ``T`` are the same type if ``S`` is a `subtype <#subtyping>`__
of ``T`` and ``T`` is a subtype of ``S``.

Subtyping
---------

``A`` is a subtype of ``F`` if both of the following conditions are met:

-  ``A`` is a subtype of ``F`` according to the `nullness-delegating
   subtyping rules for Java <#nullness-delegating-subtyping>`__.
-  ``A`` is a `nullness subtype <#nullness-subtyping>`__ of ``F``.

.. _nullness-delegating-subtyping:

Nullness-delegating subtyping rules for Java
--------------------------------------------

The Java subtyping rules are defined in `JLS
4.10 <https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.10>`__.
We add to them as follows:

-  `As always <#concept-references>`__, interpret the Java rules as
   operating on `augmented types <#augmented-type>`__, not `base
   types <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.k81vs7t5p45i>`__.
   However, when applying the Java direct-supertype rules themselves,
   *ignore* the `nullness operator <#nullness-operator>`__ of the input
   types and output types. The augmented types matter only when the Java
   rules refer to *other* rules that are defined in this spec. *Those*
   rules respect the nullness operator of some type components — but
   never the nullness operator of the type component that represents the
   whole input or output type.

      To “ignore” the output’s nullness operator, we recommend
      outputting a value of ``NO_CHANGE``, since that is valid for all
      types, including `intersection types <#intersection-types>`__.

-  When the Java array rules require one type to be a *direct* supertype
   of another, consider the direct supertypes of ``T`` to be *every*
   type that ``T`` is a `subtype <#subtyping>`__ of (as always, applying
   the definition of subtyping in this spec).

Nullness subtyping
------------------

   The primary complication in subtyping comes from type-variable
   usages. Our rules for them must account for every combination of type
   arguments with which a given generic type can be parameterized.

``A`` is a nullness subtype of ``F`` if any of the following conditions
are met:

-  ``F`` is `trusted null-inclusive under every
   parameterization <#trusted-null-inclusive-under-every-parameterization>`__.
-  ``A`` is `trusted null-exclusive under every
   parameterization <#trusted-null-exclusive-under-every-parameterization>`__.
-  ``A`` has a `nullness-subtype-establishing
   path <#nullness-subtype-establishing-path>`__ to any type whose base
   type is the same as the base type of ``F``.

Nullness subtyping (and thus subtyping itself) is *not* transitive.

(Contrast this with our `nullness-delegating
subtyping <#nullness-delegating-subtyping>`__ rules and
`containment <#containment>`__ rules: Each of those is defined as a
transitive closure. However, technically speaking, `there are cases in
which those should not be transitive,
either <https://groups.google.com/d/msg/jspecify-dev/yPnkx_GSb0Q/hLgS_431AQAJ>`__.
Fortunately, this “mostly transitive” behavior is exactly the behavior
that implementations are likely to produce naturally. Maybe someday we
will find a way to specify this fully correctly.)

Nullness subtyping (and thus subtyping itself) is *not* reflexive.

   It does end up being reflexive in the `most convenient
   world <#multiple-worlds>`__. We don’t state that as a rule for 2
   reasons: First, it arises naturally from the definitions in that
   world. Second, we don’t want to suggest that subtyping is reflexive
   in the `least convenient world <#multiple-worlds>`__.

Trusted null-inclusive under every parameterization
---------------------------------------------------

A type is trusted null-inclusive under every parameterization if it
meets either of the following conditions:

-  Its `nullness operator <#nullness-operator>`__ is ``UNION_NULL``.
-  It is an `intersection type <#intersection-types>`__ whose elements
   all are trusted null-inclusive under every parameterization.

**Most convenient world:** The rule is the same except that the
requirement for “``UNION_NULL``” is loosened to “``UNION_NULL`` or
``UNSPECIFIED``.”

Trusted null-exclusive under every parameterization
---------------------------------------------------

A type is trusted null-exclusive under every parameterization if it has
a `nullness-subtype-establishing
path <#nullness-subtype-establishing-path>`__ to either of the
following:

-  any type whose `nullness operator <#nullness-operator>`__ is
   ``MINUS_NULL``

-  any augmented class or array type

      This rule refers specifically to a “class or array type,” as
      distinct from other types like type variables and `intersection
      types <#intersection-types>`__.

Nullness-subtype-establishing path
----------------------------------

``A`` has a nullness-subtype-establishing path to ``F`` if both of the
following hold:

-  ``A`` has `nullness operator <#nullness-operator>`__ ``NO_CHANGE`` or
   ``MINUS_NULL``.

-  There is a path from ``A`` to ``F`` through
   `nullness-subtype-establishing direct-supertype
   edges <#nullness-subtype-establishing-direct-supertype-edges>`__.

      The path may be empty. That is, ``A`` has a
      nullness-subtype-establishing path to itself — as long as it has
      one of the required nullness operators.

**Most convenient world:** The rules are the same except that the
requirement for “``NO_CHANGE`` or ``MINUS_NULL``” is loosened to
“``NO_CHANGE``, ``MINUS_NULL``, or ``UNSPECIFIED``.”

Nullness-subtype-establishing direct-supertype edges
----------------------------------------------------

``T`` has nullness-subtype-establishing direct-supertype edges to the
union of the nodes computed by the following 2 rules:

Upper-bound rule:

-  if ``T`` is an augmented `intersection type <#intersection-types>`__:
   all the intersection type’s elements whose `nullness
   operator <#nullness-operator>`__ is ``NO_CHANGE`` or ``MINUS_NULL``
-  if ``T`` is an augmented type variable: all the corresponding type
   parameter’s upper bounds whose nullness operator is ``NO_CHANGE`` or
   ``MINUS_NULL``
-  otherwise: no nodes

Lower-bound rule:

-  for every type parameter ``P`` that has a lower bound whose `base
   type <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.k81vs7t5p45i>`__
   is the same as ``T``\ ’s base type and whose nullness operator is
   ``NO_CHANGE``: the type variable ``P``

   TODO(cpovirk): What if the lower bound has some other nullness
   operator? I’m pretty sure that we want to allow ``UNSPECIFIED`` in
   the most convenient world (as we did before my recent edits), and we
   may want to allow more.

-  otherwise: no nodes

**Most convenient world:** The rules are the same except that the
requirements for “``NO_CHANGE`` or ``MINUS_NULL``” are loosened to
“``NO_CHANGE``, ``MINUS_NULL``, or ``UNSPECIFIED``.”

Containment
-----------

The Java rules are defined in `JLS
4.5.1 <https://docs.oracle.com/javase/specs/jls/se14/html/jls-4.html#jls-4.5.1>`__.
We add to them as follows:

-  Disregard the 2 rules that refer to a bare ``?``. Instead, treat
   ``?`` like ``? extends Object``, where the `nullness
   operator <#nullness-operator>`__ of the ``Object`` bound is specified
   by `“Bound of an unbounded wildcard.” <#unbounded-wildcard>`__

      This is just a part of our universal rule to treat a bare ``?``
      like ``? extends Object``.

-  The rule written specifically for ``? extends Object`` applies only
   if the nullness operator of the ``Object`` bound is ``UNION_NULL``.

-  When the JLS refers to the same type ``T`` on both sides of a rule,
   the rule applies if and only if this spec defines the 2 types to be
   the `same type <#same-type>`__.

**Most convenient world:** The rules are the same except that the
requirement for “``UNION_NULL``” is loosened to “``UNION_NULL`` or
``UNSPECIFIED``.”

Substitution
------------

   Substitution on Java base types barely requires an explanation: See
   `JLS
   1.3 <https://docs.oracle.com/javase/specs/jls/se14/html/jls-1.html#jls-1.3>`__.
   Substitution on `augmented types <#augmented-type>`__, however, is
   trickier: If ``Map.get`` returns “``V`` with `nullness
   operator <#nullness-operator>`__ ``UNION_NULL``,” and if a user has a
   map whose value type is “``String`` with nullness operator
   ``UNSPECIFIED``,” then what does its ``get`` method return? Naive
   substitution would produce “``String`` with nullness operator
   ``UNSPECIFIED`` with nullness operator ``UNION_NULL``.” To reduce
   that to a proper augmented type with a single nullness operator, we
   define this process.

To substitute each type argument ``Aᵢ`` for each corresponding type
parameter ``Pᵢ``:

For every type-variable usage ``V`` whose `base
type <https://docs.google.com/document/d/1KQrBxwaVIPIac_6SCf--w-vZBeHkTvtaqPSU_icIccc/edit#bookmark=kix.k81vs7t5p45i>`__
is ``Pᵢ``, replace ``V`` with the result of the following operation:

-  If ``V`` is `trusted null-exclusive under every
   parameterization <#trusted-null-exclusive-under-every-parameterization>`__
   in the `least convenient world <#multiple-worlds>`__, then replace it
   with the result of `applying <#applying-operator>`__ ``MINUS_NULL``
   to ``Aᵢ``.

      This is the one instance in which a rule references another rule
      to be run under a *different* “world.” Normally, all rules are run
      `under the same “world.” <#propagating-multiple-worlds>`__ But in
      this instance, the null-exclusivity rule (and all rules that it in
      turn applies) are always run in the least convenient world.

   ..

      This special case improves behavior in “the
      ``ImmutableList.Builder`` case”: Consider an unannotated user of
      that class. Its builder will have an element type whose `nullness
      operator <#nullness-operator>`__ is ``UNSPECIFIED``. Without this
      special case, ``builder.add(objectUnionNull)`` would pass the
      subtyping check in the `most convenient
      world <#multiple-worlds>`__. This would happen even though we have
      enough information to know that the parameter to ``add`` is
      universally null-exclusive — even in the most convenient world.
      The special case here makes that subtyping check fail.

-  Otherwise, replace ``V`` with the result of applying the nullness
   operator of ``V`` to ``Aᵢ``.

.. _applying-operator:

Applying a nullness operator to an augmented type
-------------------------------------------------

The process of applying a `nullness operator <#nullness-operator>`__
requires 2 inputs:

-  the nullness operator to apply
-  the `augmented type <#augmented-type>`__ (which, again, includes a
   `nullness operator <#nullness-operator>`__ for that type) to apply it
   to

The result of the process is an augmented type.

The process is as follows:

First, based on the pair of nullness operators (the one to apply and the
one from the augmented type), compute a “desired nullness operator.” Do
so by applying the following rules in order. Once one condition is met,
skip the remaining conditions.

-  If the nullness operator to apply is ``MINUS_NULL``, the desired
   nullness operator is ``MINUS_NULL``.
-  If either nullness operator is ``UNION_NULL``, the desired nullness
   operator is ``UNION_NULL``.
-  If either nullness operator is ``UNSPECIFIED``, the desired nullness
   operator is ``UNSPECIFIED``.
-  The desired nullness operator is ``NO_CHANGE``.

Then, if the input augmented type is *not* an `intersection
type <#intersection-types>`__, the output is the same as the input but
with its nullness operator replaced with the desired nullness operator.

Otherwise, the output is an intersection type. For every element ``Tᵢ``
of the input type, the output type has an element that is the result of
applying the desired nullness operator to ``Tᵢ``.

   In this case, the desired nullness operator is always equal to the
   nullness operator to apply that was an input to this process. That’s
   because the nullness operator of the intersection type itself is
   defined to always be ``NO_CHANGE``.

Capture conversion
------------------

The Java rules are defined in `JLS
5.1.10 <https://docs.oracle.com/javase/specs/jls/se14/html/jls-5.html#jls-5.1.10>`__.
We add to them as follows:

-  The parameterized type that is the output of the conversion has the
   same `nullness operator <#nullness-operator>`__ as the parameterized
   type that is the input type.

-  Disregard the JLS rule about ``<?>``. Instead, treat ``?`` like
   ``? extends Object``, where the `nullness
   operator <#nullness-operator>`__ of the ``Object`` bound is specified
   by `“Bound of an unbounded wildcard.” <#unbounded-wildcard>`__

      This is just a part of our universal rule to treat a bare ``?``
      like ``? extends Object``.

-  When a rule generates a lower bound that is the null type, we specify
   that its nullness operator is ``NO_CHANGE``. (See `“Augmented null
   types.” <#null-types>`__)
