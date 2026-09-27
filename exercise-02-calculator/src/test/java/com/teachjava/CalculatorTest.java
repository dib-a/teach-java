package com.teachjava;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {

    private CalculatorImpl calculator;

    @BeforeEach
    void setUp() {
        calculator = new CalculatorImpl();
    }

    @Test
    void shouldAddTwoNumbers() {
        double result = calculator.add(2.0, 3.0);

        assertEquals(5.0, result);
    }

    @Test
    void shouldSubtractSecondNumberFromFirst() {
        double result = calculator.subtract(10.0, 4.0);

        assertEquals(6.0, result);
    }

    @Test
    void shouldMultiplyTwoNumbers() {
        double result = calculator.multiply(3.0, 4.0);

        assertEquals(12.0, result);
    }

    @Test
    void shouldDivideDividendByDivisor() {
        double result = calculator.divide(10.0, 2.0);

        assertEquals(5.0, result);
    }

    @Test
    void shouldThrowExceptionWhenDividingByZero() {
        assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(10.0, 0.0)
        );
    }
}