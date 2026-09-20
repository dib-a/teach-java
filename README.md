# Teach Java

A Java and Git adventure for young programmers.

This project is a small Java course designed to teach kids how to program while also learning how professional software
projects are built and managed with Git.

The students are around 10–12 years old, so the course focuses on **small challenges, experimentation, feedback, and
discovery** rather than long theoretical explanations.

---

## The Big Idea

Each student gets their own long-lived Git branch.

The teacher maintains the course on `main`.

```text
                 main
                  │
          teacher/course material
                  │
        ┌─────────┼─────────┐
        │         │         │
        ▼         ▼         ▼
 student/alice  student/bob  student/charlie
```

The `main` branch is the source of truth for the course.

Students start from `main`, create their own branch, and solve the exercises there.

When new exercises are added to `main`, students merge `main` into their own branch to receive the new material.

This gives the students a real Git workflow while they are learning Java.

---

# What Students Learn

The project teaches two things at the same time:

### Java

Students gradually learn:

* Classes and objects
* Methods
* Parameters
* Return values
* Interfaces
* Conditions
* Loops
* Collections
* Basic object-oriented programming
* Testing
* Reading compiler and test errors

### Git

Students gradually learn:

* Working with a repository
* Making changes
* `git status`
* Staging changes
* Commits
* `git log`
* Branches
* Pushing to GitHub
* Pulling changes
* Merging
* Resolving merge conflicts
* Pull requests

Git is not taught as a separate subject. It is used naturally while solving Java problems.

---

# Project Structure

The project is a Maven multi-module project.

```text
teach-java/
├── course-rules/
├── exercise-01-hello/
├── exercise-02-...
├── exercise-03-...
├── pom.xml
└── README.md
```

Each exercise is its own Maven module.

For example:

```text
exercise-01-hello/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── teachjava/
    └── test/
        └── java/
            └── com/
                └── teachjava/
```

This keeps the exercises independent and makes it easier to add new exercises without changing old ones.

---

# Java Version

The project uses **Java 25**.

Modern Java features can be introduced where they make sense, while the students still learn the traditional Java
concepts and class structure underneath.

For example, the first exercise uses Java's simplified entry point:

```java
void main() {
    Hello hello = new HelloImpl();
    IO.println(hello.sayHello());
}
```

The implementation itself remains a normal Java class:

```java
public class HelloImpl implements Hello {
    // TODO: your code goes here
}
```

---

# Maven

Maven is used to build and test the project.

The students do not need to understand all of Maven at the beginning.

A useful mental model is:

```text
Java    → the programming language
Maven   → builds and tests the Java project
Git     → keeps track of changes
GitHub  → hosts the Git repository
```

The project uses a parent `pom.xml` to manage common versions and configuration.

Dependencies are managed centrally using `dependencyManagement`.

Maven plugins are managed centrally using `pluginManagement`.

This keeps the individual exercise POMs small and consistent.

---

# Exercises

| # | Module               | Topic                                    |
|---|----------------------|------------------------------------------|
| 1 | `exercise-01-hello`  | Interfaces, classes, return values, tests |

Each exercise module has its own `README.md` with the mission for the student.

Note: on `main`, an exercise is **intentionally unfinished**. The skeleton does not compile until the student writes
their code, so `mvn test` fails on `main` by design.

Each exercise gives the student a small programming challenge.

The teacher provides the problem, the required interfaces/classes, and the tests.

The student implements the missing code.

For example, the first exercise provides this interface:

```java
package com.teachjava.interfaces;

public interface Hello {
    String sayHello();
}
```

The student receives:

```java
package com.teachjava;

import com.teachjava.interfaces.Hello;

public class HelloImpl implements Hello {

    // TODO: your code goes here

}
```

The test defines the expected behaviour:

```java

@Test
void saysHello() {
    Hello hello = new HelloImpl();

    assertEquals("Hello, World!", hello.sayHello());
}
```

The student has to discover what needs to be implemented.

The completed implementation is intentionally **not** provided in the exercise.

The compiler, IDE, and tests provide feedback as the student works.

---

# Tests Are the Specification

Tests are an important part of the course.

Students learn that a test describes what their program is supposed to do.

For example:

```java
assertEquals("Hello, World!",hello.sayHello());
```

means:

> When I ask the `Hello` implementation to say hello, I expect `"Hello, World!"`.

The student can change their code, run the tests, and immediately see whether their solution works.

This creates a simple feedback loop:

```text
Write code
    ↓
Run tests
    ↓
Read the result
    ↓
Change code
    ↓
Run tests again
```

The goal is for students to become comfortable with errors and failed tests rather than being afraid of them.

---

# Course Rules

The project also contains rules that apply across the exercises.

These are different from the functional tests.

A functional test asks:

> Does the program behave correctly?

A course rule asks:

> Did the student follow the conventions we're teaching?

For example, one course rule is:

> A class that implements an interface must end with `Impl`.

Therefore:

```java
public class HelloImpl implements Hello {
}
```

is valid.

But:

```java
public class HelloImp implements Hello {
}
```

is not.

The current rules are:

| Rule            | Meaning                                                             |
|-----------------|---------------------------------------------------------------------|
| `IMPL_SUFFIX`   | A class that `implements` an interface must have a name ending in `Impl` |
| `USE_OVERRIDE`  | A method that overrides another method must have `@Override`        |

The rules live in the `course-rules` module and run automatically on **every** `exercise-*` module. A new exercise
needs no extra setup. To add a rule, add a value to `CourseRule` and a check to `RuleChecker`.

The rules are checked on the source code (`@Override` does not survive compilation), and they also work on code
that does not compile yet.

The rules are intended to reinforce good programming habits without requiring the students to memorize style rules.

---

# Teacher Code and Student Code

The teacher provides the structure of an exercise.

For example:

```text
Teacher provides
    │
    ├── Interface
    ├── Tests
    ├── Exercise description
    └── Student implementation skeleton
              │
              ▼
        Student writes
        the implementation
```

The interface and tests define the contract.

Students should implement the contract rather than change it.

This distinction becomes increasingly important as the course progresses.

---

# Git Workflow

The course uses one long-lived branch per student.

For example:

```text
main
│
├── student/alice
├── student/bob
└── student/charlie
```

The teacher works on `main`.

Students work on their own branches.

A typical workflow is:

```bash
git status
git add .
git commit -m "Implement hello exercise"
git push
```

When the teacher adds new material:

```text
main
 │
 │ new exercise
 ▼
student/alice
 │
 └── git merge main
```

This means students learn Git by actually needing it.

---

# Why Long-Lived Student Branches?

The long-lived branches make the Git history part of the learning experience.

A student can look back and see:

```text
commit
commit
commit
commit
```

and understand that their project is evolving over time.

It also creates opportunities to teach:

* Branches
* Merging
* Merge conflicts
* Reading Git history
* Pull requests
* Collaboration

Later in the course, a merge conflict can be deliberately introduced as a teaching exercise.

---

# Keeping Merges Simple

New exercises should generally be **additive**.

For example:

```text
main
├── exercise-01-hello/
├── exercise-02-number/
├── exercise-03-robot/
└── exercise-04-monster/
```

Instead of repeatedly changing the same files, new exercises are added as new modules.

This minimizes merge conflicts when students update their branches.

Some merge conflicts will still be introduced deliberately later so students can learn how to resolve them.

---

# Continuous Integration

The project can use GitHub Actions to run the Maven tests automatically.

The goal is that every push and pull request can be checked automatically.

Conceptually:

```text
Student pushes code
        │
        ▼
    GitHub
        │
        ▼
 GitHub Actions
        │
        ▼
     mvn test
        │
   ┌────┴────┐
   ▼         ▼
 PASS       FAIL
```

This introduces students to an important professional development practice:

> Code can be automatically checked whenever it changes.

---

# Teaching Philosophy

The course follows a few principles.

### 1. Small problems

Exercises should be small enough that a student can understand the whole problem.

### 2. Discover before explaining

When possible, students should encounter a problem first and discover the solution through the compiler, IDE, tests, and
experimentation.

### 3. Errors are feedback

A compiler error or failed test is not a failure of the student.

It is information about what the program is telling them.

### 4. The tests are part of the learning experience

Students learn to read tests and use them as documentation.

### 5. Git is part of programming

Students use Git while writing Java rather than learning Git independently from programming.

### 6. Build complexity stays hidden

Maven, CI, dependency management, and other infrastructure should support the course without becoming the main subject.

### 7. Good habits are introduced early

Naming conventions, interfaces, `@Override`, tests, commits, and clean project structure are introduced naturally as
part of solving problems.

---

# The Goal

The goal is not simply to teach children Java syntax.

By the end of the course, students should have experienced the basic cycle of a real software project:

```text
Understand a problem
       ↓
Write code
       ↓
Run the program
       ↓
Run tests
       ↓
Fix problems
       ↓
Commit changes
       ↓
Push to GitHub
       ↓
Merge new work
       ↓
Work with branches
       ↓
Resolve conflicts
       ↓
Build something bigger
```

The project is designed to make that process approachable, fun, and understandable for young programmers.
