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
**Example Concept:**
```java
public class DatabaseConnection {
    // Volatile ensures changes to instance are immediately visible to other threads
    private static volatile DatabaseConnection instance;

    // Private constructor prevents instantiation from other classes
    private DatabaseConnection() { }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            // Double-checked locking for thread safety
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
}
```

### B. Factory Method Pattern
**Purpose:** Defines an interface for creating an object, but lets subclasses decide which class to instantiate.
**Use Case:** When a class doesn't know what sub-classes it must create. For example, a `NotificationService` that creates either an `EmailNotification`, `SMSNotification`, or `PushNotification` based on user input.
**Example Concept:**
```java
public interface Notification { void notifyUser(); }
public class SMSNotification implements Notification { public void notifyUser() { System.out.println("SMS sent!"); } }
public class EmailNotification implements Notification { public void notifyUser() { System.out.println("Email sent!"); } }

public class NotificationFactory {
    public Notification createNotification(String type) {
        if ("SMS".equalsIgnoreCase(type)) return new SMSNotification();
        else if ("EMAIL".equalsIgnoreCase(type)) return new EmailNotification();
        throw new IllegalArgumentException("Unknown channel");
    }
}
```

### C. Abstract Factory Pattern
**Purpose:** A factory of factories. It lets you produce families of related objects without specifying their concrete classes.
**Use Case:** Cross-platform UI elements (e.g., Mac Button & Mac Checkbox vs Windows Button & Windows Checkbox).

### D. Builder Pattern
**Purpose:** Separates the construction of a complex object from its representation. Allows you to create different flavors of an object step-by-step.
**Use Case:** Building an HTTP Request, or a complex `User` object with 15 optional fields (avoids the telescopic constructor anti-pattern).
**Example Concept:**
```java
public class User {
    private final String name; // Required
    private final int age;     // Optional
    
    private User(UserBuilder builder) {
        this.name = builder.name;
        this.age = builder.age;
    }
    
    public static class UserBuilder {
        private String name;
        private int age;
        
        public UserBuilder(String name) { this.name = name; }
        public UserBuilder age(int age) { this.age = age; return this; }
        public User build() { return new User(this); }
    }
}
// Usage: 
// User u = new User.UserBuilder("John").age(30).build();
```

---

## 🧱 2. Structural Patterns
These patterns deal with object composition. They simplify structure by identifying standard ways to realize relationships between entities.

### A. Adapter Pattern
**Purpose:** Acts as a bridge between two incompatible interfaces. It converts the interface of a class into another interface that the client expects.
**Use Case:** Integrating a legacy 3rd-party Payment Gateway library that works with XML into your modern system that strictly uses JSON. 
**Example Concept:**
```java
// Modern interface we want to use
interface ModernPaymentProcessor { void pay(String jsonRequest); }

// Legacy 3rd party class (Cannot be modified)
class LegacyXmlPaymentSys { public void processPayment(String xmlData) { /* Logic */ } }

// The Adapter
class PaymentAdapter implements ModernPaymentProcessor {
    private LegacyXmlPaymentSys legacySys = new LegacyXmlPaymentSys();
    public void pay(String jsonRequest) {
        String xml = convertJsonToXml(jsonRequest); // translation step
        legacySys.processPayment(xml);
    }
    private String convertJsonToXml(String json) { return "<xml>...</xml>"; }
}
```

### B. Decorator Pattern
**Purpose:** Attaches new responsibilities/behaviors to an object dynamically at runtime without altering its structure or inheriting it.
**Use Case:** Adding features to a coffee order. You have a `BasicCoffee` object, and you wrap it with a `MilkDecorator`.
**Example Concept:**
```java
interface Coffee { double getCost(); }
class BasicCoffee implements Coffee { public double getCost() { return 5.0; } }

class MilkDecorator implements Coffee {
    private final Coffee coffee; // Has-a relationship
    public MilkDecorator(Coffee coffee) { this.coffee = coffee; }
    public double getCost() { 
        return coffee.getCost() + 1.5; // Adding new behavior dynamically
    }
}
// Usage: 
// Coffee myOrder = new MilkDecorator(new BasicCoffee()); // Total Cost: 6.5
```

### C. Facade Pattern
**Purpose:** Provides a simplified, unified high-level interface to a complex subsystem.
**Use Case:** A complex Video Conversion library requires configuring codecs, bitrates, audio tracks, and formats. Instead of making the client do this, you create a facade.
**Example Concept:**
```java
class VideoConverterFacade {
    public File convertVideo(String fileName, String format) {
        // Hiding the messy subsystem from the client
        VideoFile file = new VideoFile(fileName);
        Codec sourceCodec = CodecFactory.extract(file);
        Codec destinationCodec = new MPEG4CompressionCodec();
        VideoFile buffer = BitrateReader.read(file, sourceCodec);
        VideoFile intermediateResult = BitrateReader.convert(buffer, destinationCodec);
        return new AudioMixer().fix(intermediateResult);
    }
}
// Usage: 
// File mp4 = new VideoConverterFacade().convertVideo("cat.ogg", "mp4");
```

### D. Proxy Pattern
**Purpose:** Provides a surrogate or placeholder for another object to control access to it.
**Use Case:** Lazy loading heavy objects (Virtual Proxy), Access control/security (Protection Proxy), or making network calls look like local calls.

---

## ⚙️ 3. Behavioral Patterns
These patterns are concerned with algorithms and the assignment of responsibilities between objects. They describe not just patterns of objects or classes, but also the patterns of communication between them.

### A. Strategy Pattern
**Purpose:** Defines a family of algorithms, encapsulates each one, and makes them interchangeable. It lets the algorithm vary independently from clients that use it.
**Use Case:** A checkout system where the strategy can be swapped at runtime between credit card, PayPal, or crypto.
**Example Concept:**
```java
interface PaymentStrategy { void pay(int amount); }

class PayPalStrategy implements PaymentStrategy {
    public void pay(int amount) { System.out.println("Paid " + amount + " via PayPal."); }
}
class CreditCardStrategy implements PaymentStrategy {
    public void pay(int amount) { System.out.println("Paid " + amount + " via Credit Card."); }
}

class ShoppingCart {
    private PaymentStrategy strategy; // Reference to the interface
    public void setPaymentStrategy(PaymentStrategy strategy) { this.strategy = strategy; }
    public void checkout(int amount) { strategy.pay(amount); } // Delegates logic
}
// Usage: 
// cart.setPaymentStrategy(new CreditCardStrategy());
// cart.checkout(100);
```

### B. Observer Pattern
**Purpose:** Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.
**Use Case:** Event listeners in UI frameworks, Pub/Sub mechanisms, or a Stock Market ticker.
**Example Concept:**
```java
interface Observer { void update(float price); }

class StockTicker {
    private List<Observer> observers = new ArrayList<>();
    private float price;
    
    public void addObserver(Observer obs) { observers.add(obs); }
    public void setPrice(float newPrice) { 
        this.price = newPrice;
        for (Observer obs : observers) obs.update(price); // Notify all clients
    }
}

class DisplayApp implements Observer {
    public void update(float price) { System.out.println("Displaying: $" + price); }
}
```

### C. Command Pattern
**Purpose:** Encapsulates a request as a standalone object. This lets you parameterize methods with different requests, delay or queue a request's execution, and support undoable operations.
**Example Concept:**
```java
interface Command { void execute(); }

class Light { public void turnOn() { System.out.println("Light is ON"); } }

// The Command
class LightOnCommand implements Command {
    private Light light;
    public LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.turnOn(); }
}
// Usage: 
// Command cmd = new LightOnCommand(new Light());
// cmd.execute();
```

### D. Iterator Pattern
**Purpose:** Provides a way to access the elements of an aggregate object (like a list or tree) sequentially without exposing its underlying representation.
**Use Case:** Java's `Iterator` interface, which lets you loop through a standard `ArrayList`, a linked `LinkedList`, or a `HashSet` using the exact same `hasNext()` and `next()` syntax, entirely hiding how the iteration is actually happening under the hood.
