# Low-Level Design (LLD) Patterns (Gang of Four)

In Low-Level Design (LLD), developers rely heavily on **Design Patterns** to solve recurring software engineering problems. The most famous catalog of these patterns was created by the "Gang of Four" (GoF), who categorized 23 patterns into three main types based on their purpose:

1. **Creational** (How objects are created)
2. **Structural** (How classes and objects are composed together)
3. **Behavioral** (How objects communicate with each other)

Below is a detailed breakdown of the most commonly asked and used design patterns in LLD.

---

## 🏗️ 1. Creational Patterns
These patterns abstract the instantiation process. They help make a system independent of how its objects are created, composed, and represented.

### A. Singleton Pattern
**Purpose:** Ensures a class has only *one* instance in the entire application, and provides a global point of access to it.
**Use Case:** Database connection pools, Loggers, Configuration Managers, Caching mechanisms.
**Implementation Detail:** 
Make the constructor `private`, provide a `static` method that returns the instance. Use "double-checked locking" in Java to ensure thread safety without massive performance hits.

### B. Factory Method Pattern
**Purpose:** Defines an interface for creating an object, but lets subclasses decide which class to instantiate.
**Use Case:** When a class doesn't know what sub-classes it must create. For example, a `NotificationService` that creates either an `EmailNotification`, `SMSNotification`, or `PushNotification` based on user input.
**Example Concept:**
```java
public interface Notification { void notifyUser(); }

public class NotificationFactory {
    public Notification createNotification(String type) {
        if ("SMS".equals(type)) return new SMSNotification();
        else if ("EMAIL".equals(type)) return new EmailNotification();
        return null;
    }
}
```

### C. Abstract Factory Pattern
**Purpose:** A factory of factories. It lets you produce families of related objects without specifying their concrete classes.
**Use Case:** Cross-platform UI elements (e.g., Mac Button & Mac Checkbox vs Windows Button & Windows Checkbox).

### D. Builder Pattern
**Purpose:** Separates the construction of a complex object from its representation. Allows you to create different flavors of an object step-by-step.
**Use Case:** Building an HTTP Request, or a complex `User` object with 15 optional fields (avoids the telescopic constructor anti-pattern).

---

## 🧱 2. Structural Patterns
These patterns deal with object composition. They simplify structure by identifying standard ways to realize relationships between entities.

### A. Adapter Pattern
**Purpose:** Acts as a bridge between two incompatible interfaces. It converts the interface of a class into another interface that the client expects.
**Use Case:** Integrating a legacy 3rd-party Payment Gateway library that works with XML into your modern system that strictly uses JSON. 

### B. Decorator Pattern
**Purpose:** Attaches new responsibilities/behaviors to an object dynamically at runtime without altering its structure or inheriting it.
**Use Case:** Adding features to a coffee order. You have a `BasicCoffee` object, and you wrap it with a `MilkDecorator`, and then wrap that with a `CaramelDecorator`. Java's `InputStream` IO classes (`BufferedInputStream(new FileInputStream())`) heavily use this.

### C. Facade Pattern
**Purpose:** Provides a simplified, unified high-level interface to a complex subsystem.
**Use Case:** A complex Video Conversion library requires configuring codecs, bitrates, audio tracks, and formats. Instead of making the client do this, you create a `VideoConverterFacade` class with a single method: `convertVideo(file, format)`.

### D. Proxy Pattern
**Purpose:** Provides a surrogate or placeholder for another object to control access to it.
**Use Case:** Lazy loading heavy objects (Virtual Proxy), Access control/security (Protection Proxy), or making network calls look like local calls (Remote Proxy - e.g., Spring's `@Transactional` annotations use proxies).

---

## ⚙️ 3. Behavioral Patterns
These patterns are concerned with algorithms and the assignment of responsibilities between objects. They describe not just patterns of objects or classes, but also the patterns of communication between them.

### A. Strategy Pattern
**Purpose:** Defines a family of algorithms, encapsulates each one, and makes them interchangeable. It lets the algorithm vary independently from clients that use it.
**Use Case:** A `PaymentProcessor` class where the strategy can be swapped at runtime between `CreditCardStrategy`, `PayPalStrategy`, or `CryptoStrategy`.
**Why it's highly tested in LLD:** It is the direct implementation of the "Open-Closed Principle" (Solid 'O') and replaces massive `if/else` or `switch` statements.

### B. Observer Pattern
**Purpose:** Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.
**Use Case:** Event listeners in UI frameworks, Pub/Sub mechanisms, or a Stock Market ticker where multiple displays update when a stock price changes.

### C. Command Pattern
**Purpose:** Encapsulates a request as a standalone object. This lets you parameterize methods with different requests, delay or queue a request's execution, and support undoable operations.
**Use Case:** Implementing "Undo/Redo" functionality in text editors, or Smart Home remotes where an action (`LightOnCommand`) is encapsulated and executed sequentially.

### D. Iterator Pattern
**Purpose:** Provides a way to access the elements of an aggregate object (like a list or tree) sequentially without exposing its underlying representation.
**Use Case:** Java's `Iterator` interface, which lets you loop through a standard `ArrayList`, a linked `LinkedList`, or a `HashSet` using the exact same `hasNext()` and `next()` syntax, entirely hiding how the iteration is actually happening under the hood.
