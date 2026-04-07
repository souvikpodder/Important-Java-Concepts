# Java 8 Features: In-Depth Guide

Java 8 completely transformed the way we write Java by introducing functional programming constructs, reducing boilerplate code, and modernizing core APIs. Below is a deep dive into every major feature, complete with exhaustive examples.

---

## 1. Lambda Expressions

Lambdas provide a clear and concise way to implement interfaces that have only one abstract method (Functional Interfaces). They represent an anonymous function (a function without a name or access modifier) that can be passed as an argument.

### Syntax Variations
The basic syntax is `(parameters) -> expression` or `(parameters) -> { statements; }`.

```java
// 1. No parameters
Runnable r = () -> System.out.println("No params");

// 2. Single parameter (parentheses are optional)
Consumer<String> printer = message -> System.out.println(message);

// 3. Multiple parameters (type can be inferred)
BiFunction<Integer, Integer, Integer> adder = (a, b) -> a + b;

// 4. Block body (requires explicit return keyword if returning a value)
MathOperation complexMath = (a, b) -> {
    int sum = a + b;
    return sum * 2;
};
```

### Variable Capture (Closures)
Lambdas can interact with variables defined outside their body. However, any local variable used inside a lambda must be **final or effectively final** (its value is never changed after initialization).

```java
public void testScope() {
    int multiplier = 10; // Effectively final
    Function<Integer, Integer> math = x -> x * multiplier;
    // multiplier = 20; // UNCOMMENTING THIS WILL CAUSE A COMPILATION ERROR
}
```

---

## 2. Functional Interfaces

A functional interface is an interface that contains **exactly one abstract method** (SAM - Single Abstract Method). Java 8 introduced the `@FunctionalInterface` annotation to enforce this structurally.

### Custom Functional Interfaces
You can define your own functional interfaces to pass custom behavior via lambdas. Here is a complete example:

```java
// 1. Declare the functional interface
@FunctionalInterface
public interface StringFormatter {
    // The Single Abstract Method (SAM)
    String format(String input);
    
    // Remember: Functional interfaces CAN have default or static methods
    default void printHelp() {
        System.out.println("Takes a string and formats it.");
    }
}

// 2. Use it
public class Main {
    public static void printFormattedString(String text, StringFormatter formatter) {
        System.out.println(formatter.format(text));
    }

    public static void main(String[] args) {
        String myPhrase = "Hello Java 8";
        
        // Pass behavior inline
        printFormattedString(myPhrase, str -> str.toUpperCase());
        printFormattedString(myPhrase, str -> "[" + str + "]");
    }
}
```

### Pre-defined Functional Interfaces (`java.util.function`)
Java 8 introduced over 40 standard functional interfaces so you rarely have to write your own. They fall into four main categories.

#### A. The "Big Four" (Core Interfaces)
Whenever an API takes a lambda, it is usually one of these.

| Interface | Abstract Method | What it does | Code Example |
| :--- | :--- | :--- | :--- |
| `Predicate<T>` | `boolean test(T t)` | Evaluates a condition and returns boolean. | `Predicate<String> maxLen = s -> s.length() < 5;` |
| `Consumer<T>` | `void accept(T t)` | Performs an action with the input, returns void. | `Consumer<String> print = s -> System.out.println(s);` |
| `Function<T, R>` | `R apply(T t)` | Transforms input type `T` into output type `R`. | `Function<String, Integer> toLen = s -> s.length();` |
| `Supplier<T>` | `T get()` | Takes NO input, but returns/generates an output. | `Supplier<Double> rand = () -> Math.random();` |

#### B. Bi-Interfaces (Two Arguments)
When you need to pass **two** variables instead of one.

| Interface | Abstract Method | What it does | Code Example |
| :--- | :--- | :--- | :--- |
| `BiPredicate<T, U>` | `boolean test(T t, U u)`| Takes 2 inputs, checks condition, returns boolean. | `(word, len) -> word.length() == len;` |
| `BiConsumer<T, U>` | `void accept(T t, U u)` | Takes 2 inputs, performs an action, returns void. | `(key, value) -> map.put(key, value);` |
| `BiFunction<T,U,R>` | `R apply(T t, U u)` | Takes 2 inputs, transforms them, returns type R. | `(str1, str2) -> str1 + str2;` |

#### C. Operator Interfaces
Operators are special cases of `Function` and `BiFunction` where the **Input Type(s) and Output Type are exactly the same**.

| Interface | Extends | Abstract Method | Code Example |
| :--- | :--- | :--- | :--- |
| `UnaryOperator<T>` | `Function<T, T>` | `T apply(T t)` | `UnaryOperator<String> toUpper = s -> s.toUpperCase();` |
| `BinaryOperator<T>`| `BiFunction<T,T,T>`| `T apply(T t1, T t2)` | `BinaryOperator<Integer> add = (a, b) -> a + b;` |

#### D. Primitive Specializations (For Performance)
Using generics like `Predicate<Integer>` causes **autoboxing** (converting primitive `int` to Object `Integer`), which hurts performance. Java provides specialized interfaces for `int`, `long`, and `double` to avoid this.

*   **When INPUT is Primitive:** `IntPredicate`, `DoubleConsumer`, `LongFunction<String>`
*   **When OUTPUT is Primitive:** `IntSupplier`, `ToIntFunction<String>`
*   **When BOTH are Primitives:** `IntToDoubleFunction`, `IntUnaryOperator`

---

## 3. The Stream API

A Stream is a sequence of elements supporting sequential and parallel bulk operations. It does not store data; it processes data from a collection, array, or I/O channel.

### Stream Lifecycle
1.  **Source:** How the stream originates (e.g., `list.stream()`).
2.  **Intermediate Operations:** Return a *new* Stream. They are lazy (not executed until a terminal operation is called). Examples: `map`, `filter`, `sorted`.
3.  **Terminal Operations:** Produce a final result or side-effect. Closes the stream. Examples: `collect`, `forEach`, `count`.

### Thorough Example
```java
List<Employee> employees = Arrays.asList(
    new Employee("Alice", 50000, "IT"),
    new Employee("Bob", 80000, "HR"),
    new Employee("Charlie", 60000, "IT"),
    new Employee("Dave", 40000, "Sales")
);

// Goal: Get a comma-separated string of the names of IT employees earning > 55k, sorted alphabetically.

String highEarnersInIT = employees.stream()
    // 1. Filter: Keep only IT
    .filter(e -> e.getDepartment().equals("IT"))
    // 2. Filter: Keep salary > 55000
    .filter(e -> e.getSalary() > 55000)
    // 3. Map: Transform Employee object into a String (their name)
    .map(e -> e.getName())
    // 4. Sorted: Sort the names alphabetically
    .sorted()
    // 5. Collect (Terminal): Join them as a single string
    .collect(Collectors.joining(", "));

System.out.println(highEarnersInIT); // Outputs: Charlie
```

### Other common operations:
*   `distinct()`: Removes duplicates.
*   `limit(n)`: Truncates the stream to `n` elements.
*   `skip(n)`: Discards the first `n` elements.
*   `anyMatch(Predicate)`: Returns true if any elements match.
*   `reduce()`: Combines elements into a single value (like summing a stream of numbers).

---

## 4. Default and Static Methods in Interfaces

Prior to Java 8, interfaces were 100% abstract. Adding a new method to `java.util.List` would have broken every implementation of `List` in the world because they wouldn't have implemented the newly added method. 

**Default Methods** were introduced to solve this. They allow interfaces to provide a concrete, default implementation.

```java
interface Animal {
    void eat(); // standard abstract method

    // Default method - implements can override this, or keep it
    default void sleep() {
        System.out.println("Zzzzzz...");
    }
}

class Dog implements Animal {
    @Override
    public void eat() { System.out.println("Eating kibble."); }
    // No need to implement sleep() unless we want custom behavior
}
```

**Static Methods** in interfaces are used to provide utility or helper methods directly on the interface. They cannot be overridden by implementing classes.

```java
interface StringUtils {
    static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
// Usage: boolean check = StringUtils.isNullOrEmpty("hello");
```

---

## 5. Optional Class

`Optional<T>` is a wrapper class designed to eliminate `NullPointerException` (NPE). It forces developers to explicitly handle the case where a value might not exist.

### Creating Optionals
```java
// 1. Explicitly empty
Optional<String> emptyOpt = Optional.empty();

// 2. With a known non-null value (Throws exception if passed null)
Optional<String> knownOpt = Optional.of("Hello");

// 3. MOST COMMON: With a value that might be null
String potentialNull = getFromDatabase(); 
Optional<String> safeOpt = Optional.ofNullable(potentialNull);
```

### Consuming Optionals Safely
```java
Optional<String> user = Optional.of("Souvi");

// DO NOT do this (defeats the purpose):
// if (user.isPresent()) { System.out.println(user.get()); }

// 1. Only execute if present (uses Consumer)
user.ifPresent(name -> System.out.println("Welcome " + name));

// 2. Provide a default value if missing
String finalName = user.orElse("Guest User");

// 3. Compute a default value lazily (uses Supplier)
String lazyName = user.orElseGet(() -> computeExpensiveDefaultName());

// 4. Throw an exception if missing (uses Supplier)
String strictlyRequired = user.orElseThrow(() -> new IllegalArgumentException("User not found!"));
```

---

## 6. Method References

Method references provide a shorthand way to write lambdas that do nothing but pass their parameters directly to an existing method. They use the `::` operator.

There are four types:

```java
// 1. Reference to a Static Method
// Lambda: str -> Integer.parseInt(str)
Function<String, Integer> parser = Integer::parseInt;

// 2. Reference to an Instance Method of a specific Object
String prefix = "Hello ";
// Lambda: str -> prefix.concat(str)
Function<String, String> concater = prefix::concat;

// 3. Reference to an Instance Method of an arbitrary object of a particular type
// Lambda: str -> str.toLowerCase()
Function<String, String> lowerCaser = String::toLowerCase;

// 4. Reference to a Constructor
// Lambda: () -> new ArrayList<>()
Supplier<List<String>> listSupplier = ArrayList::new;
```

---

## 7. Date and Time API (`java.time`)

The old `Date` API was terrible because it was mutable, not thread-safe, and had index issues (months started at 0). The new `java.time` API is immutable, logical, and thread-safe.

### Local Dates and Times (No Timezones)
```java
// LocalDate (Date only: YYYY-MM-DD)
LocalDate today = LocalDate.now();
LocalDate pastDate = LocalDate.of(2020, Month.JANUARY, 1);
LocalDate nextWeek = today.plusDays(7);
boolean isBefore = pastDate.isBefore(today); // true

// LocalTime (Time only: HH:MM:SS)
LocalTime time = LocalTime.now();
LocalTime parsedTime = LocalTime.parse("10:15:30");

// LocalDateTime (Date and Time)
LocalDateTime current = LocalDateTime.now();
```

### Period and Duration
```java
// Period: Calculates difference in days/months/years
LocalDate start = LocalDate.of(2023, 1, 1);
LocalDate end = LocalDate.of(2024, 5, 20);
Period gap = Period.between(start, end);
System.out.println(gap.getYears() + " years, " + gap.getMonths() + " months");

// Duration: Calculates difference in seconds/nanoseconds
LocalTime t1 = LocalTime.of(10, 0);
LocalTime t2 = LocalTime.of(12, 30);
Duration timeGap = Duration.between(t1, t2);
```

---

## 8. Map and Collection API Enhancements

Java 8 added powerful default methods to standard Collections to make use of Lambdas.

### Collections:
```java
List<String> list = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));

// forEach
list.forEach(System.out::println);

// removeIf - safely remove elements while iterating
list.removeIf(item -> item.startsWith("a")); // removes apple

// replaceAll - apply a function to all elements
list.replaceAll(item -> item.toUpperCase()); // BANANA, CHERRY
```

### Maps:
```java
Map<String, Integer> inventory = new HashMap<>();
inventory.put("Shoes", 10);

// forEach (BiConsumer)
inventory.forEach((key, val) -> System.out.println(key + ": " + val));

// putIfAbsent: Only inserts if key doesn't exist
inventory.putIfAbsent("Hats", 5);

// computeIfAbsent: Computes a value dynamically if key is missing
inventory.computeIfAbsent("Socks", key -> calculateSocksInventory());

// computeIfPresent: Updates value if key exists
inventory.computeIfPresent("Shoes", (key, oldVal) -> oldVal + 5); 
```

---

## 9. CompletableFuture

`CompletableFuture` is Java 8's answer to Promises in JavaScript. It allows for non-blocking asynchronous programming, making it easy to chain multiple async tasks together.

```java
// 1. Run a task asynchronously that returns data
CompletableFuture<String> fetchingData = CompletableFuture.supplyAsync(() -> {
    try { Thread.sleep(1000); } catch (Exception e) {}
    return "Data from Server API";
});

// 2. Chain operations without blocking the main thread
fetchingData
    .thenApply(String::toUpperCase) // Transform data
    .thenAccept(result -> System.out.println("Processing finished: " + result));

// The main thread can do other work here while the CompletableFuture handles the rest in the background
System.out.println("Main thread is not blocked!");
```
