package com.teachjava.interfaces;

public interface Calculator {
    /**
     * Adds two numbers.
     */
    double add(double first, double second);

    /**
     * Subtracts the second number from the first.
     */
    double subtract(double first, double second);

    /**
     * Multiplies two numbers.
     */
    double multiply(double first, double second);

    /**
     * Divides the dividend by the divisor.
     *
     * @throws ArithmeticException if divisor is zero
     */
    double divide(double dividend, double divisor);
}
