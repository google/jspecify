# JSpecify user guide

In Java code, whether an expression may evaluate to null is often documented
only in natural language, if at all.  The goal of JSpecify is to permit
programmers to express specifications (initially, just nullness properties) in a
machine-readable way.

JSpecify defines annotations that describe whether a Java type contains the value null.
Such annotations are useful to (for example):
 * programmers reading the code,
 * tools that help developers avoid NullPointerExceptions,
 * tools that perform run-time checking and test generation, and
 * documentation systems.


## Java variables are references

In Java, all variables, except primitives like `int`, are references. We often
think of `String x;` as meaning "`x` is a `String`", but actually it means "`x` is a
_reference_ to a `String`". One important difference between these two views is that
`x` can also be null. The goal of JSpecify is to move towards a world where `String
x;` means "`x` is a reference to an actual string", and if you want a reference
that might be null then you write `@Nullable String x;`.

## Types and nullness

In the JSpecify world, most references have one of three possible properties
regarding nullness:
1. They can be null.
2. They can't be null.
3. We don't know if they can be null or not.

If `x` is a reference of a certain type, then if `x` can be null, `x.toString()` is
unsafe because it could produce a NullPointerException. If `x` can't be null,
`x.toString()` can never produce a NullPointerException. And if we don't know
whether `x` can be null or not, `x.toString()` might or might not be unsafe.

There are two JSpecify annotations that indicate these properties:
* `@Nullable` applied to a type denotes a reference of that type that can be null.
* `@NullMarked` applied to a module, class, or package means that a reference in
that scope can't be null unless its type is explicitly marked `@Nullable`. (Below
we will see that there are some exceptions to this for [local
variables](#local-variables) and [type variables](#defining-generics).)

If a type is not explicitly marked `@Nullable` and is not inside a `@NullMarked`
class or package, then we don't know if references of that type can be null or
not.

The notion of "can't be null" should really be read with a footnote that says
"if all the code in question is `@NullMarked`". For example, if you have some code
that is not `@NullMarked` and that calls a `@NullMarked` method, then tools might
allow it to pass a possibly-null value to a method that is expecting a "can't be
null" parameter.

## `@Nullable`

The `@Nullable` annotation applied to a type means that that use of the type
includes references that can be null. Code that deals with those references must
be able to deal with the null case.

```java
  public static void print(@Nullable String x) {
    System.out.println(String.valueOf(x));
  }
```

In this example, the parameter `x` can be null, so `print(null)` is a valid method
call. The body of the `print` method does not do anything with `x` that would
provoke a NullPointerException so this method is safe.

```java
  public static @Nullable String emptyToNull(@Nullable String x) {
    return (x == null || x.isEmpty()) ? null : x;
  }
```

In this example, the parameter `x` can still be null, but now the return value can
be too. You might use this method like this:

```java
  public static void doSomething(@Nullable String x) {
    print(emptyToNull(x));
    // OK: print accepts a @Nullable String

    String z = emptyToNull(x).toString();
    // Not OK: emptyToNull(x) can be null
  }
```

Tools could then use the `@Nullable` information to determine that the first use
is safe but the second is not.

## `@NullMarked`

The `@NullMarked` annotation indicates that references can't be null in its scope,
unless their types are explicitly marked `@Nullable`. If applied to a package then
its scope is all the code in the package. If applied to a class or interface
then its scope is all the code in that class or interface.

```java
@NullMarked
public class Strings {
  public static String nullToEmpty(@Nullable String x) {
    return (x == null) ? "" : x;
  }

  public static int spaceIndex(String x) {
    return x.indexOf(' ');
  }
}
```

In this example, both methods are in the scope of `@NullMarked`, so plain `String`
means "a reference to a `String` that can't be null". `@Nullable String` continues
to mean "a reference to a `String` that can be null". Tools should warn you if you
try to pass a "reference to a `String` that can be null" to `spaceIndex`, since its
argument can't be null, and indeed it will throw NullPointerException if given a
null argument.

As mentioned above, there are some exceptions to this interpretation for local
variables (as we'll see next) and [type variables](#defining-generics).

## <a id="local-variables">Local variables</a>

Tools that understand JSpecify annotations typically _don't_ require `@Nullable` to
be applied to local variables. They may in fact not even allow that. The reason
is that it should be possible to _infer_ whether a variable can be null based on
the values that are assigned to the variable. For example:

```java
@NullMarked
public class MyClass {
  void myMethod(@Nullable String one, String two) {
    String copyOfOne = one;
    String copyOfTwo = two;
    String oneOrTwo = random() ? one : two;
    String twoOrNull = Strings.emptyToNull(two);
    ...
  }
}
```

Analysis can tell that all of these variables except `copyOfTwo` can be null.
`copyOfTwo` can't be null since `two` can't be null: it is not `@Nullable` and it is
inside the scope of `@NullMarked`. `copyOfOne` can be null since it is a copy of a
`@Nullable` parameter. `oneOrTwo` can be null because it may be a copy of a
`@Nullable` parameter. And `twoOrNull` can be null because its value comes from a
method that returns `@Nullable String`.

## Generics

When you are referencing a generic type, the rules about `@Nullable` and
`@NullMarked` are as you would expect from what we have seen. For example,
`List<@Nullable String>` means a list where each element is either a reference to
an actual string or it is null. In a `@NullMarked` context, `List<String>` means a
list where each element is a reference to an actual string and _can't_ be null.

### <a id="defining-generics">Defining generic types</a>

Things are a bit more complicated when you are _defining_ a generic type. Consider
this:

```java
@NullMarked
public class NumberList<E extends Number> implements List<E> {...}
```

The `extends Number` defines a _bound_ for the type variable `E`. It means that you
can write `NumberList<Integer>`, since `Integer` can be assigned to `Number`, but you
can't write `NumberList<String>`, since `String` can't be assigned to `Number`. This
is standard Java behavior.

But now let's think about that bound as it relates to `@NullMarked`. Can we write
`NumberList<@Nullable Integer>`? The answer is no, because we have `<E extends
Number>` inside `@NullMarked`. Since it is `Number` and not `@Nullable Number`, that
means it can't be null. `@Nullable Integer` can't be assigned to it, since that
_can_ be null. Inside `@NullMarked`, you must explicitly provide a `@Nullable` bound
on your type variable if you want it to be able to represent a `@Nullable` type:

```
@NullMarked
public class NumberList<E extends @Nullable Number> implements List<E> {...}
```

Now it _is_ legal to write `NumberList<@Nullable Integer`>, since `@Nullable Integer`
is assignable to the bound `@Nullable Number`. It's _also_ legal to write
`NumberList<Integer>`, since plain `Integer` is assignable to `@Nullable Number`.
Inside `@NullMarked`, plain `Integer` means a reference to an actual `Integer` value,
never null. That just means that the `@Nullable Number` can technically be null
but in this case it never will be.

Of course this assumes that `List` itself is written in a way that allows null:

```java
@NullMarked
public interface List<E extends @Nullable Object> {...}
```

If it were `interface List<E>` rather than `interface List<E extends @Nullable
Object>` then `NumberList<E extends @Nullable Number> extends List<E>` would not be
legal. That's because `List<E>` is short for `List<E extends Object>`. Inside
`@NullMarked`, plain `Object` means "`Object` reference that can't be null". The `<E
extends @Nullable Number>` from NumberList would not be compatible with that.

The implication of all this is that every time you define a type variable like `E`
you need to decide whether it can represent a `@Nullable` type. If it can, then it
must have a `@Nullable` bound. Most often this will be `<E extends @Nullable
Object>`. On the other hand, if it _can't_ represent a `@Nullable` type, that is
expressed by not having `@Nullable` in its bound (including the case of not having
an explicit bound at all). Here's another example:

```java
@NullMarked
public class ImmutableList<E> implements List<E> {...}
```

Here, because it is `ImmutableList<E>` and not `ImmutableList<E extends @Nullable
Object>`, it is not legal to write `ImmutableList<@Nullable String>`. You can only
write `ImmutableList<String>`, which is a list of non-null references to
`String` objects.

### Using type variables in generic types

Let's look at what the methods in the `List` interface might look like:

```
@NullMarked
public interface List<E extends @Nullable Object> {
  public boolean add(E element);
  public E get(int index);
  public @Nullable E getFirst();
  ...
}
```

The parameter type `E` of `add` means a reference that is compatible with the actual
type of the `List` elements. Just as you can't add an `Integer` to a `List<String>`,
you also can't add a `@Nullable String` to a `List<String>`, but you _can_ add it to a
`List<@Nullable String>`.

Similarly, the return type `E` of `get` means that it returns a reference with the
actual type of the list elements. If the list is a `List<@Nullable String>` then
that reference is a `@Nullable String`. If the list is a `List<String>` then the
reference is a `String`.

On the other hand, the return type `@Nullable E` of the (fictitious) `getFirst`
method is always `@Nullable`. It will be `@Nullable String` whether called on a
`List<@Nullable String>` or a `List<String>`. The idea is that the method returns
the first element of the list, or null if the list is empty. Similarly, the real
methods `@Nullable V get(Object key)` in `Map` and `@Nullable E peek()` in `Queue` can
return null even when `V` and `E` can't be null.

The distinction here is an important one that is worth repeating. A use of a
type variable like `E` should only be `@Nullable E` if it means a reference that can
be null _even if_ `E` itself can't be null. Otherwise, plain `E` means a reference
that can only be null if `E` is a `@Nullable` type, like `@Nullable String` in this
example. (And, as we've seen, `E` can only be a `@Nullable` type if the definition
of `E` has a `@Nullable` bound like `<E extends @Nullable Object>`.)

We saw earlier that `@NullMarked` usually means "references can't be null unless
they are marked `@Nullable`", and also that that doesn't apply to local variables.
Here we see that it doesn't apply to type variable uses either. When not marked
`@Nullable`, they can still be null if they have a `@Nullable` bound.

### Using type variables in generic methods

Essentially the same considerations that we just saw with generic types apply to
generic methods too. Here's an example:

```java
@NullMarked
public class Methods {
  public static <T> @Nullable T
      firstOrNull(List<T> list) {
    return list.isEmpty() ? null : list.get(0);
  }

  public static <T> T
      firstOrNonNullDefault(List<T> list, T defaultValue) {
    return list.isEmpty() ? defaultValue : list.get(0);
  }

  public static <T extends @Nullable Object> T
      firstOrDefault(List<T> list, T defaultValue) {
    return list.isEmpty() ? defaultValue : list.get(0);
  }

  public static <T extends @Nullable Object> @Nullable T
      firstOrNullableDefault(List<T> list, @Nullable T defaultValue) {
    return list.isEmpty() ? defaultValue : list.get(0);
  }
}
```

The `firstOrNull` method will accept a `List<String>` but not a `List<@Nullable
String>`. When given an argument of type `List<String>`, `T` is `String`, so the return
type `@Nullable T` is `@Nullable String`. The input list can't contain null
elements, but the return value can be null.

The `firstOrNonNullDefault` method again does not allow `T` to be a `@Nullable` type,
so `List<@Nullable String>` is not allowed. Now the return value is not `@Nullable`
either which means it will never be null.

The `firstOrDefault` method will accept both `List<String>` and `List<@Nullable
String>`. In the first case, `T` is `String`, so the type of the `defaultValue`
parameter and of the return value is `String`, meaning neither can be null. In the
second case, `T` is `@Nullable String`, so the type of `defaultValue` and of the
return value is `@Nullable String`, meaning both can be null.

The `firstOrNullableDefault` method again accepts both `List<String>` and
`List<@Nullable String>`, but now the `defaultValue` parameter is marked `@Nullable`
so it can be null even in the `List<String>` case. Likewise the return value is
`@Nullable T` so it can be null even when `T` can't.

Here's another example:

```java
  public static <T> List<@Nullable T> nullOutMatches(List<T> list, T toRemove) {
    List<@Nullable T> copy = new ArrayList<>(list);
    for (int i = 0; i < copy.size(); i++) {
      if (copy.get(i).equals(toRemove)) {
        copy.set(i, null);
      }
    }
    return copy;
  }
```

This takes a `List<T>`, which by definition does not contain null elements, and
produces a `List<@Nullable T>`, with null in place of every element that matched
`toRemove`. The output is a `List<@Nullable T>` because it _can_ contain null
elements, even if `T` itself can't be null.

## Some subtler details

The previous sections cover 99% of everything you need to know to be able to use
JSpecify annotations effectively. Here we'll cover a few details you probably
won't need to know.

### Type annotation syntax

There are a couple of places where the syntax of type annotations like `@Nullable`
is counterintuitive.

1. For a nested type like `Map.Entry`, if you want to say that the value can be null
then the syntax is `Map.@Nullable Entry`. You can often avoid dealing with this by
importing the nested type directly, but in this case `import java.util.Map.Entry`
might be undesirable because `Entry` is such a common type name.

1. For an array type, if you want to say that the _elements_ of the array can be null
then the syntax is `@Nullable String[]`. If you want to say that the _array itself_
can be null then the syntax is `String @Nullable []`. And if both the elements and
the array itself can be null, the syntax is `@Nullable String @Nullable []`.

### Wildcard bounds

Inside `@NullMarked`, wildcard bounds work almost exactly the same as type
variable bounds. We saw that `<E extends @Nullable Number>` means that E can be a
`@Nullable` type and `<E extends Number>` means it can't. Likewise, `List<? extends
@Nullable Number>` means a list where the elements can be null, and `List<?
extends Number>` means they can't.

However, there's a difference when there is no explicit bound. We saw that a
type variable definition like `<E>` means `<E extends Object>` and that means it is
not `@Nullable`. But `<?>` actually means `<? extends B>`, where `B` is the bound of the
corresponding type variable. So if we have

```java
interface List<E extends @Nullable Object> {...}
```

then `List<?>` means the same as `List<? extends @Nullable Object>`. If we have

```java
class ImmutableList<E> implements List<E> {...}
```

then we saw that that means the same as

```java
class ImmutableList<E extends Object> implements List<E>
```

so `ImmutableList<?>` means the same as `ImmutableList<? extends Object>`. And here,
`@NullMarked` means that `Object` excludes null. The `get(int)` method of `List<?>` can
return null but the same method of `ImmutableList<?>` can't.
