# Exercise 2: Calculator

Now it is time to make the calculator work.

## Your mission

Open `CalculatorImpl.java` and implement the calculator operations so that the program can:

- add two numbers
- subtract two numbers
- multiply two numbers
- divide two numbers

## What you have

| File                  | What it is                                             | Can I change it? |
|-----------------------|--------------------------------------------------------|------------------|
| `Calculator.java`     | The **interface**: it defines what the calculator does | No               |
| `CalculatorTest.java` | The **test**: it checks whether your calculator works  | No               |
| `Main.java`           | Lets you try the calculator yourself                   | YES              |
| `CalculatorImpl.java` | The **implementation**: this is where you work         | **Yes!**         |

## Step by step

1. Run the tests. Some of them will fail or the project may not compile yet. That is fine — the error messages are
   hints.
2. Read `Calculator.java`. Which methods does `CalculatorImpl` need to provide?
3. Read `CalculatorTest.java`. What results does the test expect?
4. Open `CalculatorImpl.java`.
5. Implement the methods:
    - `add`
    - `subtract`
    - `multiply`
    - `divide`
6. Think about what should happen when someone tries to divide by zero.
7. Run the tests again.
8. Fix any failing tests until everything is green.
9. Run `Main` and try your calculator yourself.

## Example

If the calculator receives:

```text
1 + 2
```

the result should be:

```text
3.0
```

If it receives:

```text
10 / 2
```

the result should be:

```text
5.0
```]()

## Try it yourself

You can use `Main.java` to test your implementation manually:

```java
void main() {
    Calculator calculator = new CalculatorImpl();

    System.out.println("Addition: " + calculator.add(1, 2));
    System.out.println("Subtraktion: " + calculator.subtract(5, 3));
    System.out.println("Multiplikation: " + calculator.multiply(4, 2));
    System.out.println("Division: " + calculator.divide(10, 2));
}
```

## Save your work with Git

```bash
git status
git add .
git commit -m "Implement calculator exercise"
git push
```