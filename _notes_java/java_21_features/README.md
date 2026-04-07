# Java 21 Features (Including 18 - 21)

Java 21 is a monumental Long-Term Support (LTS) release, rivaling Java 8 in its impact. Its crowning achievement is bringing lightweight concurrency via Project Loom (Virtual Threads). It also stabilizes some incredibly helpful advanced structural features under Project Amber.

---

## 1. Virtual Threads (Project Loom) - Java 21
This is the biggest change in Java 21. Before Virtual Threads, a Java `Thread` was mapped 1:1 to an Operating System (OS) thread. OS threads are expensive, limited, and take a lot of memory (around 1MB/thread). If a server had 4,000 threads, it often choked.

**Virtual Threads** are managed by the JVM, not the OS. They are virtually "free". You can easily launch 1,000,000 virtual threads on a standard laptop. When a Virtual Thread waits on I/O (like a DB call or HTTP request), it automatically "unmounts" from the real OS thread, allowing another virtual thread to use CPU power.

```java
// Traditional Thread
Thread t1 = new Thread(() -> System.out.println("Heavy OS Thread"));
t1.start();

// Virtual Thread (The New Way)
Thread vThread = Thread.ofVirtual()
                       .name("my-virtual-thread")
                       .start(() -> System.out.println("Lightweight Virtual Thread"));

// You can launch 100,000 of them instantly:
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 100_000; i++) {
        executor.submit(() -> {
            Thread.sleep(1000); // Does NOT block the OS thread!
            return "Done";
        });
    }
}
```

---

## 2. Sequenced Collections - Java 21
For over two decades, Java developers complained about finding the first and last elements in a collection, or trying to iterate backward. (e.g., getting the last element of a `LinkedHashSet` was a nightmare). 

Java 21 introduces `SequencedCollection`, `SequencedSet`, and `SequencedMap` interfaces. All ordered collections now implement them.

**New Standard Methods:**
*   `addFirst()`, `addLast()`
*   `getFirst()`, `getLast()`
*   `removeFirst()`, `removeLast()`
*   `reversed()` (Returns a reverse-ordered view)

```java
List<String> list = new ArrayList<>(List.of("B", "C", "D"));

list.addFirst("A");     // [A, B, C, D]
list.addLast("E");      // [A, B, C, D, E]

System.out.println(list.getLast()); // "E"

var reversed = list.reversed(); // [E, D, C, B, A]
```

---

## 3. Pattern Matching for `switch` - Java 21
Building on the `instanceof` pattern matching in Java 16 and standard switch expressions in Java 14, you can now write massive, clean type-checking switches. 

It completely eliminates the "if-else chain of `instanceof` checks" anti-pattern.

```java
public String formatValue(Object obj) {
    return switch (obj) {
        case Integer i -> String.format("int %d", i);
        case Long l    -> String.format("long %d", l);
        case Double d  -> String.format("double %f", d);
        // You can add conditional guards using 'when'!
        case String s when s.length() > 5 -> "Long string: " + s;
        case String s -> "Short string: " + s;
        case null -> "It's a null value";
        default -> obj.toString();
    };
}
```

---

## 4. Record Patterns - Java 21
When dealing with Records, you often want to extract their inner fields immediately. Java 21 allows you to "deconstruct" a Record directly within `instanceof` checks or `switch` cases.

```java
public record Point(int x, int y) {}
public record ColoredPoint(Point p, String color) {}

public void printPointData(Object obj) {
    
    // Old Way:
    if (obj instanceof Point p) {
        System.out.println(p.x() + ", " + p.y());
    }

    // New Way (Deconstructing):
    if (obj instanceof Point(int x, int y)) {
        System.out.println("X is " + x + " and Y is " + y);
    }
    
    // Nested Deconstructing!
    if (obj instanceof ColoredPoint(Point(int x, int y), String color)) {
        System.out.println("Color: " + color + " at X:" + x);
    }
}
```

---

## 5. Simple Web Server (Out of the Box) - Java 18
Java 18 included a minimal, command-line HTTP server (similar to Python's `python -m http.server`). It is intended exclusively for rapid prototyping, testing, and learning, removing the need to spin up Spring Boot or Tomcat just to serve a static file directory.

You can run it straight from the terminal in any folder:
```bash
# Serves the current directory on port 8000
java -m jdk.httpserver
```
Or use it in code:
```java
// Starts a static file server pointing to /my/directory
SimpleFileServer.createFileServer(new InetSocketAddress(8080), 
                                  Path.of("/my/directory"), 
                                  OutputLevel.VERBOSE).start();
```

---

## Summary of the Modern Java Shift
If you upgrade from Java 8 direct to Java 21, the landscape looks wildly different:
1.  **Immutability First:** Using `record` instead of mutable POJOs.
2.  **Cleaner Code:** `var` bindings, text blocks, switch expressions, and pattern matching drastically reduce screen noise.
3.  **Insane Scale:** Changing standard thread pools to Virtual Thread executors allows servers to easily handle millions of concurrent connections doing I/O.
