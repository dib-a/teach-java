package com.teachjava.rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runs every course rule on the code of every exercise. New exercises are picked up automatically.
 */
class CourseRulesTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("exercises")
    void classesThatImplementEndWithImpl(String exercise, Path sources) throws IOException {
        assertNoViolations(CourseRule.IMPL_SUFFIX, sources);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("exercises")
    void overridingMethodsUseOverride(String exercise, Path sources) throws IOException {
        assertNoViolations(CourseRule.USE_OVERRIDE, sources);
    }

    private static void assertNoViolations(CourseRule rule, Path sources) throws IOException {
        List<String> messages = RuleChecker.check(sources).stream()
                .filter(violation -> violation.rule() == rule)
                .map(RuleChecker.Violation::message)
                .toList();
        assertTrue(messages.isEmpty(), () -> rule.description() + ":\n  " + String.join("\n  ", messages));
    }

    static Stream<Arguments> exercises() throws IOException {
        Path projectRoot = Path.of(System.getProperty("basedir", "")).toAbsolutePath().getParent();
        try (Stream<Path> modules = Files.list(projectRoot)) {
            return modules
                    .filter(module -> module.getFileName().toString().startsWith("exercise-"))
                    .filter(module -> Files.isDirectory(module.resolve("src/main/java")))
                    .sorted()
                    .map(module -> Arguments.of(module.getFileName().toString(), module.resolve("src/main/java")))
                    .toList()
                    .stream();
        }
    }

}
