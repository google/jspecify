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

*   The [specification][spec], written to be understood by owners of compilers
    and static analysis tools.
*   Our [wiki] has about 20 informal, non-normative articles on various topics
*   Open [issues]
*   [Try it out](/docs/using)

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

*   Join our [Google Group]. Introduce yourself! Ask questions, complain, or
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

## Press

Since we [released version 1.0.0](/blog/release-1.0.0) we've seen some positive
reaction in the community. Here are some interesting posts, articles, and
videos:

*   The November 2025 release of Spring Framework 7.0 was [announced][spring-announcement],
    and the announcement mentions that its
    "null-safety strategy is converging with the recently released JSpecify annotations".
    This [GitHub issue comment][spring-migration-comment] lists some of the reasons for their migration effort.

[spring-announcement]: https://spring.io/blog/2024/10/01/from-spring-framework-6-2-to-7-0
[spring-migration-comment]: https://github.com/spring-projects/spring-framework/issues/28797#issuecomment-2387137015

*   [Moderne mass-migrated its nullness annotations](https://www.moderne.ai/blog/mass-migration-of-nullability-annotations-to-jspecify)
    and published a guide explaining how they did it using their
    [OpenRewrite](https://docs.openrewrite.org/) automated refactoring platform.

*   Ben Evans at InfoQ
    [wrote about the release](https://www.infoq.com/news/2024/08/jspecify-java-nullability/)
    and interviewed Jurgen Hoeller, Spring Framework's cofounder and project
    lead, about it.

*   A Russian video overview of recent news in the Java/JDK world includes
    [a discussion of JSpecify](https://www.youtube.com/watch?v=CkAywkCby58&t=429s).

[discuss]: https://groups.google.com/g/jspecify-discuss
[file an issue]: https://github.com/jspecify/jspecify/issues/new
[github]: https://github.com/jspecify/jspecify
[google group]: https://groups.google.com/g/jspecify-discuss
[javadoc]: https://jspecify.dev/docs/api/org/jspecify/annotations/package-summary.html
[faq]: http://github.com/jspecify/jspecify/wiki/jspecify-faq
[nullness design faq]: https://github.com/jspecify/jspecify/wiki/nullness-design-FAQ
[issues]: https://github.com/jspecify/jspecify/issues
[release]: https://search.maven.org/artifact/org.jspecify/jspecify/1.0.0/jar
[reference implementation]: https://github.com/jspecify/jspecify-reference-checker
[spec]: /docs/spec
[stackoverflow answer]: https://stackoverflow.com/questions/4963300/which-notnull-java-annotation-should-i-use
[user guide]: /docs/user-guide
[wiki]: https://github.com/jspecify/jspecify/wiki
