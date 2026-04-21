# Java 17 Features (Including 12 - 16)

Java 17 is the next major LTS release after Java 11. This release focused heavily on "Developer Ergonomics"—reducing boilerplate and eliminating age-old pain points in the language. It brings some of the biggest syntax upgrades since lambdas in Java 8.

---

## 1. Records - Java 14/16
Records are a massive feature. They completely eliminate the need to write boilerplate for plain data carrier classes (POJOs/DTOs). By declaring a `record`, Java automatically generates:
1. `private final` fields.
2. An all-arguments constructor.
3. Public accessor/getter methods (e.g., `name()`, not `getName()`).
4. `equals()`, `hashCode()`, and `toString()`.

```java
// Using a Record
public record User(int id, String username, String email) {
    // You can also add custom validation in a compact constructor
    public User {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }
    }
}

// Usage
User dbUser = new User(1, "souvi", "souvi@example.com");
System.out.println(dbUser.username()); // "souvi"
System.out.println(dbUser.toString()); // User[id=1, username=souvi, email=souvi@example.com]
```

---

## 2. Text Blocks - Java 13/15
Text Blocks solve the decades-old nightmare of pasting HTML, JSON, or SQL into a Java string and suffocating in `+` concatenations and `\n` escape characters.

Use `"""` to start and end a text block.

```java
// Old Way:
String oldJson = "{\n" +
                 "  \"name\": \"Alice\",\n" +
                 "  \"age\": 25\n" +
                 "}";

// New Way (Text Blocks):
String newJson = """
    {
        "name": "Alice",
        "age": 25
    }
    """;
System.out.println(newJson);
```
*Note: The compiler intelligently strips common leading indentation, so your code block can be aligned nicely without ruining the actual string spaces!*

---

## 3. Switch Expressions - Java 12/14
Switch statements historically had two terrible flaws: Fall-through (if you forget `break`, execution bleeds into the next case) and the inability to return a value easily. Switch Expressions fix both.

**Key Changes:**
*   Uses `->` instead of `:`.
*   **No fall-through** by default.
*   The entire block can **return a value**.
*   Multiple cases on one line separated by commas.

```java
public String getDayType(String dayOfWeek) {
    // Switch returning a value:
    return switch (dayOfWeek) {
        case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> "Weekday";
        case "SATURDAY", "SUNDAY" -> "Weekend";
        default -> {
            // Use 'yield' if your case requires a block of code before returning
            System.out.println("Processing unknown day...");
            yield "Invalid Day";
        }
    };
}
```

---

## 4. Pattern Matching for `instanceof` - Java 14/16
Before Java 14, if you checked an object's type with `instanceof`, you immediately had to cast it on the next line to actually use it. Pattern matching creates a variable scope instantly.

```java
Object obj = "Hello Java 17";

// Old Way
if (obj instanceof String) {
    String s = (String) obj; // Annoying boilerplate cast
    System.out.println(s.toLowerCase());
}

// New Way
if (obj instanceof String s) {
    // 's' is already casted as a String and ready to use!
    System.out.println(s.toLowerCase());
}

// You can even chain it!
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Long String: " + s);
}
```

---

## 5. Sealed Classes - Java 15/17
Sometimes you want an interface or class to only be extended or implemented by a specific, finite list of known classes. `sealed` classes give you strict control over inheritance.

A sealed class must use the `permits` keyword to list who is allowed to inherit from it. Any allowed subclasses must declare themselves as `final` (no further subclassing), `sealed` (they have their own accepted list), or `non-sealed` (open to the world).

```java
// Only Circle, Square, and Rectangle are allowed to implement Shape.
public sealed interface Shape permits Circle, Square, Rectangle {
    double calculateArea();
}

public final class Circle implements Shape {
    public double radius;
    public double calculateArea() { return Math.PI * radius * radius; }
}

public final class Square implements Shape { ... }

// Rectangle is wide open - anyone can extend Rectangle.
public non-sealed class Rectangle implements Shape { ... } 

// class Triangle implements Shape { } 
// COMPILATION ERROR! Triangle is not permitted.
// error: class is not allowed to extend sealed class: Shape (as it is not listed in its permits clause)
```

---

## 6. Helpful NullPointerExceptions - Java 14
Historically, if a line like `a.b().c().d() = 5` threw a NullPointerException, JVM stack traces would only tell you the line number, leaving you to guess whether `a`, `b()`, or `c()` evaluated to null.

In Java 14+, the JVM tells you exactly which variable caused the fault.

**Example Trace Output:**
`Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.length()" because the return value of "User.getEmail()" is null`

---

## Interview Questions — Java 17 Features (12–17)

**Q1. What is a Record in Java? What does it auto-generate?**
```java
public record Point(int x, int y) {}
// Auto-generates:
// - private final int x, y
// - public int x(), int y()  (accessor methods, NOT getX/getY)
// - equals(), hashCode(), toString()
// - All-args constructor

Point p = new Point(3, 4);
p.x();          // 3 (not p.getX())
p.toString();   // Point[x=3, y=4]
```
- Records are **immutable** (fields are final).
- Can have custom methods, but cannot add instance fields beyond the components.
- Use for DTOs, response objects, value types.

**Q2. What are Text Blocks? What problem do they solve?**
```java
// Pre Java 13: painful JSON/SQL strings
String sql = "SELECT u.id, u.name, o.total\n" +
             "FROM users u\n" +
             "JOIN orders o ON u.id = o.user_id\n" +
             "WHERE u.active = true";

// Java 13+ Text Blocks: clean, readable multi-line strings
String sql = """
        SELECT u.id, u.name, o.total
        FROM users u
        JOIN orders o ON u.id = o.user_id
        WHERE u.active = true
        """;
// The closing """ determines the indentation baseline
```

**Q3. What is a Sealed class? When would you use it?**
- Restricts which classes can extend/implement it — gives you a **closed** type hierarchy.
- Works perfectly with pattern matching `switch` — compiler knows all possible subtypes.
```java
public sealed interface Shape permits Circle, Rectangle, Triangle {}
public record Circle(double radius) implements Shape {}
public record Rectangle(double w, double h) implements Shape {}
public record Triangle(double base, double height) implements Shape {}

// Compiler knows all cases — no default needed!
double area = switch (shape) {
    case Circle c    -> Math.PI * c.radius() * c.radius();
    case Rectangle r -> r.w() * r.h();
    case Triangle t  -> 0.5 * t.base() * t.height();
};
```

**Q4. What is `switch` expression (Java 14+)? How is it different from `switch` statement?**
```java
// Old switch statement — verbose, fall-through issues
int day = 3;
String dayName;
switch (day) {
    case 1: dayName = "Monday"; break;
    case 2: dayName = "Tuesday"; break;
    default: dayName = "Unknown"; break;
}

// New switch expression (Java 14+) — concise, no fall-through, returns a value
String dayName = switch (day) {
    case 1 -> "Monday";     // arrow syntax, no fall-through
    case 2 -> "Tuesday";
    case 3, 4 -> "Mid-week"; // multiple cases
    default -> "Unknown";
};

// Also supports yield for multi-statement blocks:
int result = switch (op) {
    case "add" -> a + b;
    case "multiply" -> {
        int product = a * b;
        yield product;        // 'yield' returns a value from a block
    }
    default -> throw new IllegalArgumentException("Unknown op: " + op);
};
```

**Q5. What is `instanceof` pattern matching (Java 16)?**
```java
// Old way — redundant cast
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// New way — pattern variable
if (obj instanceof String s) {
    System.out.println(s.length());  // s is already cast and in scope
}

// Combined with conditions (Java 16)
if (obj instanceof String s && s.length() > 10) {
    System.out.println("Long string: " + s);
}
// s is in scope only where the instanceof check is true
```

**Q6. What are the key differences between Java 8 and Java 17?**
| Feature | Java 8 | Java 17 |
|---|---|---|
| Data carriers | Mutable POJOs | `record` (immutable, concise) |
| Multi-line strings | `+` concatenation | Text Blocks `"""..."""` |
| Null-safe cast | `if (x instanceof T) { T t = (T)x; }` | `if (x instanceof T t)` |
| Switch | Statement only | Expression with `->`, `yield` |
| Type hierarchy | Open extension | `sealed` classes |
| Error messages | Vague NPE | Helpful NPE (what was null) |
| LTS? | Yes | Yes |
