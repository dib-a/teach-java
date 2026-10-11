package com.teachjava;

import com.teachjava.interfaces.Hello;

public class HelloImpl implements Hello {
    @Override
    public String sayHello() {
      return "Hello, World!";
    }
}
