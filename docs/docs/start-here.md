---
sidebar_position: 1
---

# Start Here

Quick links: [Release] | [GitHub] | [User Guide] | [Javadoc] | [Spec] | [Wiki] |
[FAQ] | [Issues] | [Discuss]

## What is this?

[A group of organizations](/about) are working together to define a common set
of annotation types for use in JVM languages, to improve static analysis and
language interoperation. Our initial focus is on nullness analysis.

This includes providing both an artifact of annotation types your code can
depend on (in the `org.jspecify.annotations` package), and *precise*
specifications of their semantics.

*   **Why standardize the annotations?** Because you deserve better than this
    tragic [stackoverflow answer] depicts.

*   **Why standardize the semantics?** Because you deserve a single source of
    truth for how your code should be annotated, instead of having to decide
    which tool to please at the expense of the others.

*   **Why do both together?** Because you deserve to find that information
    easily, right in the javadoc of the annotation classes themselves.

JSpecify is developed by consensus of major stakeholders in Java static
analysis. Our 1.0.0 release is the first tool-neutral, library-neutral artifact
for these annotations. (Note: `javax.annotation` was an attempt at this that
never reached consensus and was never actually released.)

Learn more about the JSpecify group and its goals in the JSpecify [FAQ].

## How do I learn about your nullness support?

Here are some links. As you read them, you'll probably have a number of "why?"
questions, which you can look for in the [Nullness Design FAQ]. If you like,
[send us mail](mailto:jspecify-discuss@googlegroups.com).

### Begin with one of

*   The [User Guide].
*   The [Javadoc], which is not a *tutorial* walkthrough, but is thorough and
    specific.

### Then if you're *really* interested

*   The [specification], written to be understood by owners of compilers and
    static analysis tools. Note that it's currently pretty old: it matches
    JSpecify 0.2.0, not 1.0.0. We're working on updating it.
*   Our [wiki] has about 20 informal, non-normative articles on various topics
*   Open [issues]
*   [Try it out](using)

### Reference implementation

*   Please experiment with our **[reference implementation]**. This lets you
    validate your usages of the annotations against our defined semantics, which
    is when you will really get to find out how helpful or annoying our current
    design choices are for you (which you should let us know!). However, this
    tool is still a work in progress, and is not at *full* conformance with our
    own specification quite yet.

## How can I get involved?

Great question.

It's not too late for your input to matter! After our 1.0.0 release, we have
plans to extend our support beyond nullness.

*   Join our new [Google Group]. Introduce yourself! Ask questions, complain, or
    just tell us what you're hoping to see. If your organization should be a
    member of our group, tell us about yourselves and self-nominate.

*   Do you use any libraries or tools that you think should use/support JSpecify
    annotations? Please tell them about us!

*   Give some thought to what factors would make your own projects more or less
    likely to adopt JSpecify, and let us know.

*   [File an issue] to request a feature or if something is wrong. (If something
    is wrong with the [reference implementation],
    [file an issue in its repo](https://github.com/jspecify/jspecify-reference-checker/issues/new).)

*   Star and Watch our [github] repo.

[discuss]: https://groups.google.com/g/jspecify-discuss
[file an issue]: https://github.com/jspecify/jspecify/issues/new
[github]: https://github.com/jspecify/jspecify
[google group]: https://groups.google.com/g/jspecify-discuss
[javadoc]: http://jspecify.org/docs/api/org/jspecify/annotations/package-summary.html
[faq]: http://github.com/jspecify/jspecify/wiki/jspecify-faq
[nullness design faq]: https://github.com/jspecify/jspecify/wiki/nullness-design-FAQ
[issues]: https://github.com/jspecify/jspecify/issues
[release]: https://search.maven.org/artifact/org.jspecify/jspecify/1.0.0/jar
[reference implementation]: https://github.com/jspecify/jspecify-reference-checker
[spec]: /docs/spec
[specification]: /docs/spec
[stackoverflow answer]: https://stackoverflow.com/questions/4963300/which-notnull-java-annotation-should-i-use
[user guide]: /docs/user-guide
[wiki]: https://github.com/jspecify/jspecify/wiki
