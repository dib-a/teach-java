package com.teachjava.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests that the rule checker itself catches what it should (and only that).
 */
class RuleCheckerTest {

    @TempDir
    Path sources;

    @Test
    void acceptsCorrectCode() throws IOException {
        write("Greeter", "public interface Greeter { String greet(); }");
        write("GreeterImpl", """
                public class GreeterImpl implements Greeter {
                    @Override public String greet() { return "hi"; }
                    @Override public String toString() { return "GreeterImpl"; }
                    public void extra() { }
                }
                """);

        assertEquals(List.of(), RuleChecker.check(sources));
    }

    @Test
    void findsClassWithoutImplSuffix() throws IOException {
        write("Greeter", "public interface Greeter { String greet(); }");
        write("GreeterImp", "public class GreeterImp implements Greeter { @Override public String greet() { return \"\"; } }");

        assertEquals(List.of(CourseRule.IMPL_SUFFIX), rules());
    }

    @Test
    void findsMissingOverrideOnInterfaceMethod() throws IOException {
        write("Greeter", "public interface Greeter { String greet(); }");
        write("GreeterImpl", "public class GreeterImpl implements Greeter { public String greet() { return \"\"; } }");

        assertEquals(List.of(CourseRule.USE_OVERRIDE), rules());
    }

    @Test
    void findsMissingOverrideOnObjectMethod() throws IOException {
        write("Plain", "public class Plain { public String toString() { return \"\"; } }");

        assertEquals(List.of(CourseRule.USE_OVERRIDE), rules());
    }

    @Test
    void checksCodeThatDoesNotCompileYet() throws IOException {
        write("Greeter", "public interface Greeter { String greet(); }");
        write("GreeterImp", "public class GreeterImp implements Greeter { }");

        assertEquals(List.of(CourseRule.IMPL_SUFFIX), rules());
    }

    private List<CourseRule> rules() throws IOException {
        return RuleChecker.check(sources).stream().map(RuleChecker.Violation::rule).toList();
    }

    private void write(String className, String code) throws IOException {
        Files.writeString(sources.resolve(className + ".java"), code);
    }

}
