package com.teachjava;

import com.teachjava.interfaces.Hello;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloTest {

    @Test
    void saysHello() {
        Hello hello = new HelloImpl();

        assertEquals("Hello, World!", hello.sayHello());
    }

}
