# Java 21 to Java 25 LTS Feature Notes

**Question:** What are the important Java 25 features if you are coming from Java 21?

**Answer:** Java 25 is the next major LTS release after Java 21 for most vendors. The safest way to think about it is: Java 25 includes all finalized features delivered in Java 22, 23, 24, and 25, plus some preview/incubator features that are still not final.

Reference: [OpenJDK: JEPs in JDK 25 integrated since JDK 21](https://openjdk.org/projects/jdk/25/jeps-since-jdk-21)

## 1. Unnamed Variables and Patterns

Final in Java 22.

Use `_` when a variable or pattern is required syntactically but the value is intentionally ignored.

```java
for (Order _ : orders) {
    count++;
}

try {
    Integer.parseInt(input);
} catch (NumberFormatException _) {
    System.out.println("Invalid number");
}

switch (point) {
    case Point(int x, _) -> System.out.println(x);
}
```

**Interview point:** `_` means "I do not need this value." It improves readability and avoids fake variable names like `ignored`, `unused`, or `dummy`.

## 2. Compact Source Files and Instance Main Methods

Final in Java 25.

Small programs no longer need the full `public class Main` and `public static void main(String[] args)` ceremony.

```java
void main() {
    IO.println("Hello, Java 25");
}
```

Traditional style still works:

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello");
    }
}
```

**Interview point:** This is mainly for beginners, scripts, demos, and small programs. Large applications should still use normal classes, packages, and explicit structure.

## 3. Module Import Declarations

Final in Java 25.

You can import all exported packages from a module at once.

```java
import module java.base;

void main() {
    List<String> names = List.of("A", "B");
    IO.println(names);
}
```

**Interview point:** This is different from `import java.util.*`. A package import imports types from one package; a module import imports exported packages from a module. It is convenient for learning, prototyping, and modular APIs.

## 4. Flexible Constructor Bodies

Final in Java 25.

Before Java 25, `super(...)` or `this(...)` had to be the first statement in a constructor. Java 25 allows safe statements before the constructor call.

```java
class Employee extends Person {
    Employee(String name, int age) {
        if (age < 18 || age > 67) {
            throw new IllegalArgumentException("Invalid employee age");
        }

        super(name, age);
    }
}
```

**Important rule:** code before `super(...)` runs in an early construction context. It must not use the current object, for example by calling instance methods or reading instance fields. It may validate arguments and do safe computations.

## 5. Foreign Function and Memory API

Final in Java 22.

The `java.lang.foreign` API lets Java call native functions and safely access memory outside the JVM heap.

**Why it matters:**
1. It is a modern alternative to many JNI use cases.
2. It supports off-heap memory with better safety than `sun.misc.Unsafe`.
3. It uses `MemorySegment`, `Arena`, `Linker`, and `SymbolLookup`.

```java
try (Arena arena = Arena.ofConfined()) {
    MemorySegment segment = arena.allocate(100);
    segment.set(ValueLayout.JAVA_INT, 0, 42);
}
```

**Interview point:** FFM does not mean JNI is gone, but new native interop should prefer FFM when possible.

## 6. Stream Gatherers

Final in Java 24.

Gatherers are custom intermediate stream operations. Think of them as `Collector`-like extensibility, but for the middle of a stream pipeline instead of only the terminal step.

```java
var result = Stream.iterate(1, n -> n + 1)
        .gather(Gatherers.windowFixed(3))
        .limit(2)
        .toList();

// [[1, 2, 3], [4, 5, 6]]
```

**Interview point:** `collect(...)` is for terminal aggregation. `gather(...)` is for custom intermediate transformations, including windowing, scanning, stateful transforms, and short-circuiting.

## 7. Scoped Values

Final in Java 25.

Scoped values allow immutable data to be shared safely with methods deeper in the call stack and with child threads.

```java
private static final ScopedValue<String> USER = ScopedValue.newInstance();

ScopedValue.where(USER, "alice").run(() -> {
    processRequest();
});

static void processRequest() {
    String currentUser = USER.get();
}
```

**Interview point:** Scoped values are often compared with `ThreadLocal`, but they are safer for virtual threads because the value has a clear lexical lifetime and is immutable.

## 8. Class-File API

Final in Java 24.

Java now has a standard API for reading, generating, and transforming `.class` files.

**Why it matters:** bytecode tools, instrumentation libraries, agents, and frameworks no longer need to depend only on third-party bytecode libraries for every class-file task.

## 9. Security and Cryptography Updates

Important additions after Java 21:

1. **Key Derivation Function API** - final in Java 25. Adds `javax.crypto.KDF` for deriving keys from secret material.
2. **Quantum-resistant ML-KEM** - Java 24.
3. **Quantum-resistant ML-DSA** - Java 24.
4. **PEM Encodings of Cryptographic Objects** - preview in Java 25.
5. **Security Manager permanently disabled** - Java 24.

**Interview point:** Java is moving toward post-quantum cryptography support and away from older security mechanisms like the Security Manager.

## 10. Runtime, GC, and Performance Updates

Not all Java 25 features are syntax changes. Several important changes are JVM-level:

1. **Virtual threads synchronized without pinning** - Java 24. Blocking inside `synchronized` is much less dangerous for virtual threads.
2. **Compact Object Headers** - Java 25. Reduces object header size to improve memory footprint.
3. **Generational Shenandoah** - Java 25.
4. **ZGC generational mode by default** - Java 23, and non-generational ZGC removed in Java 24.
5. **AOT class loading/linking and method profiling** - Java 24/25. Improves startup and warmup possibilities.
6. **JFR CPU-time profiling, cooperative sampling, method timing, and tracing** - Java 25.
7. **G1 region pinning and late barrier expansion** - Java 22/24.

**Interview point:** Java 25 is not just "new syntax"; it also improves startup, observability, GC behavior, virtual-thread behavior, and memory footprint.

## 11. Tools and Documentation

1. **Launch multi-file source-code programs** - Java 22.
2. **Markdown documentation comments** - Java 23.
3. **Link run-time images without JMODs** - Java 24.

Markdown documentation comments allow Javadoc comments to be written more naturally:

```java
/// Returns the display name.
///
/// This method never returns `null`.
String displayName() {
    return name;
}
```

## 12. Still Preview or Incubator in Java 25

These are available in Java 25 but are not final yet:

1. **Primitive Types in Patterns, `instanceof`, and `switch`** - third preview.
2. **Structured Concurrency** - fifth preview.
3. **Vector API** - tenth incubator.
4. **Stable Values** - preview.
5. **PEM Encodings of Cryptographic Objects** - preview.

**Rule:** preview features require `--enable-preview` at compile time and runtime. Incubator APIs are not final APIs and may still change.

## 13. Removed or Deprecated Since Java 21

1. **32-bit Windows x86 port removed** in Java 24.
2. **32-bit x86 port removed** in Java 25.
3. **Memory-access methods in `sun.misc.Unsafe` deprecated for removal** in Java 23.
4. **JNI usage is being prepared for stronger restrictions**.

**Upgrade point:** if an old Java 21 application depends on 32-bit x86 builds, `Unsafe` memory access, JNI-heavy libraries, or the Security Manager, test carefully before moving to Java 25.
