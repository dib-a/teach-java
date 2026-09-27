package com.teachjava;

import com.teachjava.interfaces.Calculator;

public class Main {

    void main() {
        // Here you can write your code to test your programm
        // See the example below
        Calculator calculator = new CalculatorImpl();

        System.out.println("Addition: " + calculator.add(1, 2));
        System.out.println("Subtraktion: " + calculator.subtract(5, 3));
        System.out.println("Multiplikation: " + calculator.multiply(4, 2));
        System.out.println("Division: " + calculator.divide(10, 2));
    }
}
