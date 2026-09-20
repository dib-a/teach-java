package com.teachjava.rules;

public enum CourseRule {

    IMPL_SUFFIX("A class that implements an interface must end with 'Impl'"),
    USE_OVERRIDE("A method that overrides another method must have @Override");

    private final String description;

    CourseRule(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
