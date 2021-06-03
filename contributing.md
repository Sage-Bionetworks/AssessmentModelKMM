# Sage Bionetworks Bridge Mobile Client Coding Standards

The following guidelines should be followed when writing code for the Bridge mobile client platforms. This includes both platform code and sample code provided as part of the platform.

Standards for Java and Kotlin formatting have been established. Please follow these two philosophies when contributing:

* if you are contributing new code, please follow the guidelines established for the project.

* if you are contributing to an existing file, please follow the conventions found in that file.

Our standards also require automated tests and sample code. All code should have corresponding unit tests and be included in the sample and reference apps.

## Java Coding Standards

Always follow the Java coding style guidelines published by Google, ([Java Style Guide](https://google.github.io/styleguide/javaguide.html)) and the Android conventions of the Android Open Source Project ([Android Open Source Project Java Code Style](https://source.android.com/setup/contribute/code-style)).

## Kotlin Coding Standards

Always follow the Android style guide for Kotlin ([Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)).

# Git commit best practices

Split commits to into separate commits for each issue/bug.
Commits should be organized logically to address only related issues within a single commit. For example, separate out a bug discovered during testing into a separate commit from the commits related to a feature you are adding.
## Commit often
Committing frequently helps to keep commits small and to include only related changes. That way it is easier for someone to see the history of how you have addressed an issue you are working on and to figure out why the changes were introduced. Additionally, it reduces the risk of merge conflicts and makes it easier to resolve those conflicts.
## Squash “WIP” commits before submitting a pull request
Ideally, each commit should include a workable feature that could stand alone. That means that the unit tests have been added, and the work has been done to address the issue (or a logical component of the issue). This does not mean that if your task is to add a major feature that the entire feature needs to be addressed in a single commit or even a single pull request, but that the commits should be organized like this:
 
    Add model for foo
    Add state machine for foo
    Add X-style presentation for foo
    Add Y-style presentation for foo
 
Rather than like this:
 
    Added a unit test
    WIP
    Refactored the goo model to fix the foo test
    Fixed the broken tests
    Added the x-style presentation
    Tweaked x-style presentation
    Fixed a typo
    More stuff
    Include a descriptive message
Commit messages should be in the format of a short summary followed (if necessary) by a longer description on a separate line. If the commit addresses a Github Issue, then it should include “Fix #xxx” or “Resolve #xxx”. Likewise, if a commit is in response to changes requested by a pull request reviewer, then the commit message should include the PR # for the pull request.

Additional References

https://chris.beams.io/posts/git-commit/

https://medium.com/@nawarpianist/git-commit-best-practices-dab8d722de99


