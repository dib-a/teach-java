package com.teachjava.rules;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Checks the course rules on Java source code.
 * <p>
 * The rules are checked on the source files, not on compiled classes, because {@code @Override}
 * is not kept in compiled classes. The sources are parsed and analyzed with the JDK compiler,
 * but nothing is written. Code that does not compile yet (an unfinished exercise) can still be checked.
 */
public final class RuleChecker {

    public record Violation(CourseRule rule, String message) {
    }

    private RuleChecker() {
    }

    public static List<Violation> check(Path sourceRoot) throws IOException {
        List<Path> files;
        try (Stream<Path> paths = Files.walk(sourceRoot)) {
            files = paths.filter(path -> path.toString().endsWith(".java")).toList();
        }
        if (files.isEmpty()) {
            return List.of();
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8)) {
            JavacTask task = (JavacTask) compiler.getTask(
                    null, fileManager, diagnostic -> { }, List.of("-proc:none", "-Xlint:none"), null,
                    fileManager.getJavaFileObjectsFromPaths(files));

            Iterable<? extends CompilationUnitTree> units = task.parse();
            task.analyze();

            Trees trees = Trees.instance(task);
            Elements elements = task.getElements();
            Types types = task.getTypes();
            List<Violation> violations = new ArrayList<>();

            for (CompilationUnitTree unit : units) {
                new TreePathScanner<Void, Void>() {
                    @Override
                    public Void visitClass(ClassTree node, Void unused) {
                        if (trees.getElement(getCurrentPath()) instanceof TypeElement type) {
                            String where = where(sourceRoot, unit, trees, node);
                            checkImplSuffix(type, where, violations);
                            checkOverrides(type, elements, types, where, violations);
                        }
                        return super.visitClass(node, unused);
                    }
                }.scan(new TreePath(unit), null);
            }
            return violations;
        }
    }

    private static void checkImplSuffix(TypeElement type, String where, List<Violation> violations) {
        boolean namedClass = type.getKind() == ElementKind.CLASS && type.getNestingKind() != NestingKind.ANONYMOUS;
        String name = type.getSimpleName().toString();
        if (namedClass && !type.getInterfaces().isEmpty() && !name.endsWith("Impl")) {
            violations.add(new Violation(CourseRule.IMPL_SUFFIX,
                    where + ": class '" + name + "' implements an interface, so its name must end with 'Impl'"));
        }
    }

    private static void checkOverrides(TypeElement type, Elements elements, Types types, String where, List<Violation> violations) {
        List<ExecutableElement> inherited = new ArrayList<>();
        collectSuperMethods(type, types, inherited);

        for (Element member : type.getEnclosedElements()) {
            if (member.getKind() != ElementKind.METHOD || member.getModifiers().contains(Modifier.STATIC)) {
                continue;
            }
            ExecutableElement method = (ExecutableElement) member;
            boolean overrides = inherited.stream().anyMatch(other -> elements.overrides(method, other, type));
            if (overrides && method.getAnnotation(Override.class) == null) {
                violations.add(new Violation(CourseRule.USE_OVERRIDE,
                        where + ": method '" + method.getSimpleName() + "()' overrides another method, so it needs @Override"));
            }
        }
    }

    private static void collectSuperMethods(TypeElement type, Types types, List<ExecutableElement> methods) {
        List<TypeMirror> supertypes = new ArrayList<>(type.getInterfaces());
        supertypes.add(type.getSuperclass());
        for (TypeMirror supertype : supertypes) {
            if (types.asElement(supertype) instanceof TypeElement superElement) {
                superElement.getEnclosedElements().stream()
                        .filter(member -> member.getKind() == ElementKind.METHOD)
                        .forEach(member -> methods.add((ExecutableElement) member));
                collectSuperMethods(superElement, types, methods);
            }
        }
    }

    private static String where(Path sourceRoot, CompilationUnitTree unit, Trees trees, ClassTree node) {
        long position = trees.getSourcePositions().getStartPosition(unit, node);
        long line = unit.getLineMap().getLineNumber(position);
        Path file = Path.of(unit.getSourceFile().toUri());
        return sourceRoot.relativize(file) + ":" + line;
    }

}
