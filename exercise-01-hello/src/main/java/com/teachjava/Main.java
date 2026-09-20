package com.teachjava;

import com.teachjava.interfaces.Hello;

public class Main {

    void main() {
        Hello hello = new HelloImpl();
        IO.println(hello.sayHello());
    }

}
