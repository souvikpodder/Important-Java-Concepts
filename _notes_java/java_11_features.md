# Java 11 Features (Including 9 & 10)

Java 11 is the first Long-Term Support (LTS) release after Java 8. It encompasses several smaller releases (Java 9 and 10), bringing significant improvements to developer productivity, collection APIs, and a much-needed modern HTTP Client.

---

## 1. Local Variable Type Inference (`var`) - Java 10
The `var` keyword reduces boilerplate code by allowing the compiler to infer the type of a local variable based on its right-hand side initialization. 

**Rules:**
*   Only for **local variables** inside methods. You cannot use `var` for class/instance fields or method bounds.
*   You **must** initialize the variable immediately (cannot write `var name; name = "Bob";`).
*   It is not dynamically typed (unlike JavaScript). Once the type is inferred at compile time, it cannot be changed.

```java
// Prior to Java 10
Map<String, List<String>> myComplexMap = new HashMap<>();

// With Java 10 `var`
var myComplexMap = new HashMap<String, List<String>>();
var name = "Alice"; // Inferred as String
var age = 30;       // Inferred as int
```

---

## 2. Factory Methods for Collections - Java 9
Creating small, unmodifiable collections before Java 9 required multiple `.add()` physical lines or using `Arrays.asList()`. Java 9 introduced `List.of()`, `Set.of()`, and `Map.of()` to make this incredibly brief.

```java
// List
List<String> immutableList = List.of("Apple", "Banana", "Cherry");

// Set
Set<String> immutableSet = Set.of("Red", "Green", "Blue");

// Map (Key, Value, Key, Value...)
Map<String, String> immMap = Map.of("key1", "value1", "key2", "value2");

// Using Map.entry for more than 10 pairs:
Map<Integer, String> bigMap = Map.ofEntries(
    Map.entry(1, "One"),
    Map.entry(2, "Two")
);
```
*Note: Collections created this way are completely Immutable (unmodifiable) and do NOT allow `null` values.*

---

## 3. The Modern HTTP Client API - Java 11
The old `HttpURLConnection` was notoriously difficult to use, and required lots of boilerplate. Java 11 introduces `java.net.http`, a modern, async-capable HTTP client supporting HTTP/2 and WebSockets.

```java
HttpClient client = HttpClient.newHttpClient();

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://jsonplaceholder.typicode.com/todos/1"))
    .GET()
    .build();

// Synchronous Request
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
System.out.println(response.statusCode());
System.out.println(response.body());

// Asynchronous Request (returns CompletableFuture)
client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    .thenApply(HttpResponse::body)
    .thenAccept(System.out::println);
```

---

## 4. String API Enhancements - Java 11
Several heavily-requested utility methods were added directly to the `String` class.

```java
String text = " \n \t   ";

// isBlank(): Better than isEmpty(). Checks if string is empty or contains only whitespace
System.out.println(text.isBlank()); // true

// strip(), stripLeading(), stripTrailing(): 
// Modern version of trim() that correctly handles all Unicode whitespace characters.
String greeting = "   Hello World!   ";
System.out.println(greeting.strip()); // "Hello World!"

// repeat(n): Repeats a string n times
String str = "Java";
System.out.println(str.repeat(3)); // "JavaJavaJava"

// lines(): Returns a Stream of lines extracted from a multi-line string
String multiLine = "Line 1\nLine 2\nLine 3";
multiLine.lines().forEach(System.out::println);
```

---

## 5. File API Enhancements - Java 11
Reading and writing strings to files became one-liners without needing buffering or streams.

```java
Path filePath = Path.of("sample.txt");

// Writing string to a file
Files.writeString(filePath, "Hello Java 11 Files API!");

// Reading string from a file
String content = Files.readString(filePath);
System.out.println(content);
```

---

## 6. Optional Enhancements
`Optional` received a few helpful methods across versions 9, 10, and 11 to mitigate common annoyances.

```java
Optional<String> name = Optional.of("Souvi");

// ifPresentOrElse() (Java 9): Takes a Consumer AND a Runnable
name.ifPresentOrElse(
    val -> System.out.println("Name is " + val),    // If present
    () -> System.out.println("Name is missing")     // If empty
);

// isEmpty() (Java 11): The inverse of isPresent()
if (name.isEmpty()) {
    System.out.println("It's blank!");
}

// orElseThrow() (Java 10): Identical to get(), but its name signals intent clearly that it might throw an exception.
String unwrapped = name.orElseThrow();
```

---

## 7. Local-Variable Syntax for Lambda Parameters - Java 11
You can now use `var` inside Lambda expressions. This is extremely useful when you want to use annotations on lambda inputs (like `@NonNull`), since annotations require a type declaration, and `var` fulfills that requirement without forcing you to write out the full complex type.

```java
BiFunction<Integer, Integer, Integer> adder = (var a, var b) -> a + b;

// Primarily used when you need annotations:
BiFunction<String, String, String> concat = (@NonNull var s1, @NonNull var s2) -> s1 + s2;
```

---

## 8. Java Platform Module System (JPMS / Project Jigsaw) - Java 9
*This is an architectural feature rather than a daily syntax feature.*
Java 9 introduced the Module System to divide the massive Java SDK into smaller, manageable chunks (modules). This allows you to build smaller, custom JREs tailored explicitly for your application by only including the modules you need (using `jlink`), drastically reducing memory footprint and startup time.

You define a module using a `module-info.java` file at the root of your package:
```java
module my.custom.app {
    // Other modules we need to function
    requires java.sql;
    requires java.net.http;
    
    // Packages we are publicly exposing to the world
    exports com.myapp.services;
}
```
