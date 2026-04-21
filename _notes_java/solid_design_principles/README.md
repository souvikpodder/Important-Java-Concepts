# SOLID Design Principles in Java

**SOLID** is a set of five object-oriented design principles that help developers write code that is **easy to maintain, extend, and understand**. These principles were introduced by **Robert C. Martin (Uncle Bob)**.

```
S — Single Responsibility Principle (SRP)
O — Open/Closed Principle (OCP)
L — Liskov Substitution Principle (LSP)
I — Interface Segregation Principle (ISP)
D — Dependency Inversion Principle (DIP)
```

---

## Table of Contents
- [Why SOLID Matters](#why-solid-matters)
- [1. Single Responsibility Principle (SRP)](#1-single-responsibility-principle-srp)
- [2. Open/Closed Principle (OCP)](#2-openclosed-principle-ocp)
- [3. Liskov Substitution Principle (LSP)](#3-liskov-substitution-principle-lsp)
- [4. Interface Segregation Principle (ISP)](#4-interface-segregation-principle-isp)
- [5. Dependency Inversion Principle (DIP)](#5-dependency-inversion-principle-dip)
- [SOLID at a Glance — Quick Reference](#solid-at-a-glance--quick-reference)
- [Common Mistakes & Anti-Patterns](#common-mistakes--anti-patterns)
- [How SOLID Principles Work Together](#how-solid-principles-work-together)

---

## Why SOLID Matters

| Without SOLID | With SOLID |
|---------------|------------|
| One change breaks many things | Changes are isolated and safe |
| Hard to test individual components | Each class/module is independently testable |
| Tight coupling between classes | Loose coupling through abstractions |
| Code is rigid and fragile | Code is flexible and extensible |
| Duplicated logic everywhere | Clean, reusable components |

> **Key Insight:** SOLID principles don't add complexity — they **manage** complexity. The upfront effort pays off exponentially as the project grows.

---

## 1. Single Responsibility Principle (SRP)

> **"A class should have one, and only one, reason to change."**

A class should do **one thing** and do it well. If a class has multiple responsibilities, changes to one responsibility can break the other.

### ❌ Violating SRP — One class doing everything

```java
// BAD: This class has 3 reasons to change:
// 1. Business logic changes (calculate salary)
// 2. Database schema changes (save employee)
// 3. Report format changes (generate report)
class Employee {
    private String name;
    private double baseSalary;
    
    public double calculateSalary() {
        // Complex salary calculation with bonuses, deductions, tax
        return baseSalary * 1.2 - (baseSalary * 0.1);
    }
    
    public void saveToDatabase() {
        // JDBC code to save employee to database
        Connection conn = DriverManager.getConnection("jdbc:mysql://...");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO employees...");
        stmt.setString(1, name);
        stmt.executeUpdate();
    }
    
    public String generatePayslipReport() {
        // Format payslip as PDF/HTML
        return "<html><body>Payslip for " + name + ": $" + calculateSalary() + "</body></html>";
    }
}
```

### ✅ Following SRP — Each class has one job

```java
// Class 1: Only handles salary calculation logic
class SalaryCalculator {
    public double calculateSalary(Employee employee) {
        double base = employee.getBaseSalary();
        double bonus = base * 0.2;
        double tax = base * 0.1;
        return base + bonus - tax;
    }
}

// Class 2: Only handles database persistence
class EmployeeRepository {
    public void save(Employee employee) {
        // JDBC or JPA code to persist employee
        entityManager.persist(employee);
    }
    
    public Employee findById(long id) {
        return entityManager.find(Employee.class, id);
    }
}

// Class 3: Only handles report generation
class PayslipReportGenerator {
    public String generate(Employee employee, double salary) {
        return String.format("Payslip for %s: $%.2f", employee.getName(), salary);
    }
}

// Class 4: Employee is now just a data holder (POJO)
class Employee {
    private String name;
    private double baseSalary;
    
    // Only getters and setters — no business logic
    public String getName() { return name; }
    public double getBaseSalary() { return baseSalary; }
}
```

### How to Identify SRP Violations

| Red Flag | Example |
|----------|---------|
| Class name has "And" or "Or" | `UserValidatorAndSaver` |
| Class has methods for unrelated concerns | `calculateTax()` + `sendEmail()` in same class |
| Changes in UI require modifying business logic class | Front-end changes propagate to backend classes |
| Class imports from many unrelated packages | `java.sql.*`, `javax.mail.*`, `java.io.*` all in one class |

---

## 2. Open/Closed Principle (OCP)

> **"Software entities should be open for extension, but closed for modification."**

You should be able to **add new behavior** without changing existing, tested code. This is typically achieved through **abstraction** (interfaces, abstract classes) and **polymorphism**.

### ❌ Violating OCP — Modifying existing code for every new shape

```java
// BAD: Every time we add a new shape, we MODIFY this class
class AreaCalculator {
    public double calculateArea(Object shape) {
        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            return Math.PI * circle.getRadius() * circle.getRadius();
        } else if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            return rect.getWidth() * rect.getHeight();
        } else if (shape instanceof Triangle) {
            // Adding new shape = MODIFYING existing code ❌
            Triangle tri = (Triangle) shape;
            return 0.5 * tri.getBase() * tri.getHeight();
        }
        // What about Pentagon? Hexagon? We keep modifying this method!
        throw new IllegalArgumentException("Unknown shape");
    }
}
```

### ✅ Following OCP — Extending via abstraction

```java
// Define abstraction — this is CLOSED for modification
interface Shape {
    double calculateArea();
}

// Each shape is OPEN for extension — add new shapes without touching existing code
class Circle implements Shape {
    private double radius;
    
    public Circle(double radius) { this.radius = radius; }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle implements Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
}

// NEW shape — no existing code modified!
class Triangle implements Shape {
    private double base, height;
    
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }
}

// AreaCalculator is now CLOSED for modification — works with ANY shape
class AreaCalculator {
    public double calculateTotalArea(List<Shape> shapes) {
        return shapes.stream()
                     .mapToDouble(Shape::calculateArea)
                     .sum();
    }
}
```

### Real-World OCP Example — Payment Processing

```java
// Define abstraction
interface PaymentProcessor {
    void processPayment(double amount);
    boolean supports(String paymentType);
}

// Implementations — each can be added independently
class CreditCardProcessor implements PaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment: $" + amount);
    }
    public boolean supports(String type) { return "CREDIT_CARD".equals(type); }
}

class PayPalProcessor implements PaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("Processing PayPal payment: $" + amount);
    }
    public boolean supports(String type) { return "PAYPAL".equals(type); }
}

// Adding UPI? Just create a new class — NO modification of existing classes!
class UpiProcessor implements PaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("Processing UPI payment: $" + amount);
    }
    public boolean supports(String type) { return "UPI".equals(type); }
}

// Payment service — CLOSED for modification
class PaymentService {
    private final List<PaymentProcessor> processors;
    
    public PaymentService(List<PaymentProcessor> processors) {
        this.processors = processors;
    }
    
    public void pay(String type, double amount) {
        PaymentProcessor processor = processors.stream()
            .filter(p -> p.supports(type))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported: " + type));
        processor.processPayment(amount);
    }
}
```

---

## 3. Liskov Substitution Principle (LSP)

> **"Objects of a superclass should be replaceable with objects of its subclass without breaking the application."**

If class `B` extends class `A`, then you should be able to use `B` everywhere `A` is expected — **without surprises**. Subtypes must honor the contract of the parent type.

### ❌ Violating LSP — Classic Square-Rectangle Problem

```java
class Rectangle {
    protected int width;
    protected int height;
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getArea() { return width * height; }
}

// BAD: Square changes Rectangle's behavior
class Square extends Rectangle {
    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width;  // ⚠️ Forces height = width
    }
    
    @Override
    public void setHeight(int height) {
        this.width = height;  // ⚠️ Forces width = height
        this.height = height;
    }
}

// This code BREAKS when using Square instead of Rectangle
void testRectangle(Rectangle rect) {
    rect.setWidth(5);
    rect.setHeight(4);
    
    // For a Rectangle, we expect area = 5 * 4 = 20
    assert rect.getArea() == 20;  // ❌ FAILS with Square! Area = 4 * 4 = 16
}
```

**Why it breaks:** `Square.setHeight(4)` silently changes the width too, violating the expected behavior of `Rectangle`.

### ✅ Following LSP — Proper abstraction

```java
// Use an interface/abstract class that both can implement correctly
interface Shape {
    double getArea();
}

class Rectangle implements Shape {
    private final int width;
    private final int height;
    
    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double getArea() { return width * height; }
}

class Square implements Shape {
    private final int side;
    
    public Square(int side) {
        this.side = side;
    }
    
    @Override
    public double getArea() { return side * side; }
}

// Now both work correctly — no surprises!
void printArea(Shape shape) {
    System.out.println("Area: " + shape.getArea());  // ✅ Works for both
}
```

### Another LSP Example — Bird Problem

```java
// ❌ BAD: Not all birds can fly!
class Bird {
    public void fly() {
        System.out.println("Flying...");
    }
}

class Ostrich extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Ostriches can't fly!");  // ❌ Violates LSP
    }
}

// ✅ GOOD: Separate flying from non-flying birds
interface Bird {
    void eat();
}

interface FlyingBird extends Bird {
    void fly();
}

class Sparrow implements FlyingBird {
    public void eat() { System.out.println("Sparrow eating"); }
    public void fly() { System.out.println("Sparrow flying"); }
}

class Ostrich implements Bird {
    public void eat() { System.out.println("Ostrich eating"); }
    // No fly() method — Ostrich is just a Bird, not a FlyingBird ✅
}
```

### LSP Rules of Thumb

| Rule | Explanation |
|------|-------------|
| Don't throw unexpected exceptions in subclass | If parent doesn't throw, subclass shouldn't throw |
| Don't strengthen preconditions | Subclass shouldn't require MORE than parent |
| Don't weaken postconditions | Subclass should deliver AT LEAST what parent promises |
| Preserve parent's invariants | If parent says "balance >= 0", subclass must too |

---

## 4. Interface Segregation Principle (ISP)

> **"Clients should not be forced to depend on interfaces they do not use."**

Don't create **fat interfaces** that force classes to implement methods they don't need. Instead, split them into **smaller, focused interfaces**.

### ❌ Violating ISP — Fat interface forces empty implementations

```java
// BAD: One giant interface for all workers
interface Worker {
    void work();
    void eat();
    void sleep();
    void attendMeeting();
    void writeReport();
    void codeReview();
}

// A Robot worker is forced to implement eating and sleeping — makes no sense!
class RobotWorker implements Worker {
    public void work() { System.out.println("Robot working..."); }
    
    // ❌ Forced to implement these — but robots don't eat or sleep!
    public void eat() { /* do nothing */ }
    public void sleep() { /* do nothing */ }
    public void attendMeeting() { /* do nothing */ }
    public void writeReport() { /* do nothing */ }
    public void codeReview() { /* do nothing */ }
}
```

### ✅ Following ISP — Smaller, focused interfaces

```java
// Split into focused interfaces
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

interface Meetable {
    void attendMeeting();
}

// Human implements all relevant interfaces
class HumanWorker implements Workable, Eatable, Sleepable, Meetable {
    public void work() { System.out.println("Human working..."); }
    public void eat() { System.out.println("Having lunch..."); }
    public void sleep() { System.out.println("Sleeping..."); }
    public void attendMeeting() { System.out.println("In a meeting..."); }
}

// Robot only implements what it actually does
class RobotWorker implements Workable {
    public void work() { System.out.println("Robot working 24/7..."); }
    // No forced empty implementations! ✅
}
```

### Real-World ISP Example — Repository Pattern

```java
// ❌ BAD: Fat repository interface
interface Repository<T> {
    T findById(long id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(T entity);
    void bulkInsert(List<T> entities);
    List<T> search(String query);
    void export(String format);
}

// An AuditLogRepository is read-only — why force save/update/delete?

// ✅ GOOD: Segregated interfaces
interface ReadRepository<T> {
    T findById(long id);
    List<T> findAll();
}

interface WriteRepository<T> {
    void save(T entity);
    void update(T entity);
    void delete(T entity);
}

interface SearchableRepository<T> {
    List<T> search(String query);
}

// Compose only what you need
class UserRepository implements ReadRepository<User>, WriteRepository<User>, SearchableRepository<User> {
    // Implements all — Users need full CRUD + search
}

class AuditLogRepository implements ReadRepository<AuditLog> {
    // Only read access — audit logs should never be modified ✅
}
```

---

## 5. Dependency Inversion Principle (DIP)

> **"High-level modules should not depend on low-level modules. Both should depend on abstractions."**
> 
> **"Abstractions should not depend on details. Details should depend on abstractions."**

Instead of high-level classes directly creating or depending on low-level classes, both should depend on **interfaces/abstractions**. This makes swapping implementations easy.

### ❌ Violating DIP — Direct dependency on concrete class

```java
// LOW-LEVEL module
class MySQLDatabase {
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }
}

// HIGH-LEVEL module — directly depends on MySQL ❌
class UserService {
    private MySQLDatabase database = new MySQLDatabase();  // ❌ Tight coupling!
    
    public void createUser(String name) {
        // What if we want to switch to PostgreSQL or MongoDB?
        // We'd have to MODIFY this class!
        database.save(name);
    }
}
```

**Problems:**
- Switching from MySQL to PostgreSQL requires modifying `UserService`
- Can't unit test `UserService` without a real MySQL database
- `UserService` is tightly coupled to a specific database implementation

### ✅ Following DIP — Depend on abstraction

```java
// ABSTRACTION — both high-level and low-level depend on this
interface Database {
    void save(String data);
    String findById(long id);
}

// LOW-LEVEL module — depends on Database abstraction
class MySQLDatabase implements Database {
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }
    public String findById(long id) {
        return "MySQL result for " + id;
    }
}

class PostgreSQLDatabase implements Database {
    public void save(String data) {
        System.out.println("Saving to PostgreSQL: " + data);
    }
    public String findById(long id) {
        return "PostgreSQL result for " + id;
    }
}

class MongoDBDatabase implements Database {
    public void save(String data) {
        System.out.println("Saving to MongoDB: " + data);
    }
    public String findById(long id) {
        return "MongoDB result for " + id;
    }
}

// HIGH-LEVEL module — depends on Database ABSTRACTION, not a specific DB
class UserService {
    private final Database database;  // ✅ Depends on abstraction
    
    // Dependency is INJECTED (Dependency Injection)
    public UserService(Database database) {
        this.database = database;
    }
    
    public void createUser(String name) {
        database.save(name);  // Works with ANY database!
    }
}

// Usage — easy to swap implementations
UserService service1 = new UserService(new MySQLDatabase());
UserService service2 = new UserService(new PostgreSQLDatabase());
UserService service3 = new UserService(new MongoDBDatabase());
```

### DIP with Dependency Injection (Spring Boot Example)

```java
// Abstraction
interface NotificationService {
    void send(String to, String message);
}

// Implementations
@Service("email")
class EmailNotificationService implements NotificationService {
    public void send(String to, String message) {
        System.out.println("Email to " + to + ": " + message);
    }
}

@Service("sms")
class SmsNotificationService implements NotificationService {
    public void send(String to, String message) {
        System.out.println("SMS to " + to + ": " + message);
    }
}

// High-level class — Spring injects the dependency
@Service
class OrderService {
    private final NotificationService notificationService;
    
    @Autowired  // Spring injects the implementation
    public OrderService(@Qualifier("email") NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    public void placeOrder(String customerEmail) {
        // ... process order ...
        notificationService.send(customerEmail, "Order placed successfully!");
    }
}
```

### DIP Enables Easy Testing

```java
// Create a mock/fake for testing — no real database needed!
class FakeDatabase implements Database {
    private final Map<Long, String> storage = new HashMap<>();
    private long nextId = 1;
    
    public void save(String data) {
        storage.put(nextId++, data);
    }
    
    public String findById(long id) {
        return storage.get(id);
    }
}

// Unit test — fast, no external dependencies
@Test
void testCreateUser() {
    FakeDatabase fakeDb = new FakeDatabase();
    UserService service = new UserService(fakeDb);
    
    service.createUser("John");
    
    assertEquals("John", fakeDb.findById(1));  // ✅ Test without real DB!
}
```

---

## SOLID at a Glance — Quick Reference

| Principle | One-Liner | Key Question to Ask |
|-----------|-----------|---------------------|
| **S** — Single Responsibility | One class = One job | *"Does this class have more than one reason to change?"* |
| **O** — Open/Closed | Add new features without changing old code | *"Can I add new behavior without modifying existing classes?"* |
| **L** — Liskov Substitution | Subclass can replace parent without breaking things | *"Can I use this subclass everywhere the parent is used?"* |
| **I** — Interface Segregation | Many small interfaces > one fat interface | *"Are classes forced to implement methods they don't use?"* |
| **D** — Dependency Inversion | Depend on abstractions, not concrete classes | *"Does my high-level class directly depend on a low-level class?"* |

---

## Common Mistakes & Anti-Patterns

### 1. God Class (Violates SRP)
```java
// ❌ A class that does EVERYTHING
class ApplicationManager {
    public void handleUserLogin() { ... }
    public void processPayment() { ... }
    public void sendEmail() { ... }
    public void generateReport() { ... }
    public void manageInventory() { ... }
    public void backupDatabase() { ... }
}
// Fix: Split into UserService, PaymentService, EmailService, etc.
```

### 2. Shotgun Surgery (Violates OCP)
```java
// ❌ Adding a new feature requires changes in MANY files
// Adding "Gift Card" payment requires editing:
// - PaymentController.java
// - PaymentService.java
// - PaymentValidator.java
// - PaymentRepository.java
// - PaymentReport.java
// Fix: Use Strategy pattern with PaymentProcessor interface
```

### 3. Refused Bequest (Violates LSP)
```java
// ❌ Subclass refuses inherited behavior
class List {
    public void add(Object item) { ... }
    public void remove(Object item) { ... }
}

class ReadOnlyList extends List {
    @Override
    public void add(Object item) {
        throw new UnsupportedOperationException();  // ❌ Violates parent contract
    }
}
// Fix: Don't extend List. Implement a separate ReadableCollection interface
```

### 4. Interface Pollution (Violates ISP)
```java
// ❌ Interface with 20+ methods nobody fully implements
interface SmartDevice {
    void turnOn();
    void turnOff();
    void connectWifi();
    void connectBluetooth();
    void playMusic();
    void makeCall();
    void browseInternet();
    void takePhoto();
    // ... 15 more methods
}
// Fix: Split into Connectable, MediaPlayer, Camera, Phone interfaces
```

---

## How SOLID Principles Work Together

```
┌──────────────────────────────────────────────────────────────────┐
│                    SOLID Working Together                        │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  SRP: Each class has ONE responsibility                          │
│   └──> Makes classes small and focused                           │
│                                                                  │
│  OCP: Use interfaces/abstract classes for extension points       │
│   └──> New behavior = New class (no modification of existing)    │
│                                                                  │
│  LSP: Subclasses honor parent contracts                          │
│   └──> Polymorphism works correctly and predictably              │
│                                                                  │
│  ISP: Interfaces are small and focused                           │
│   └──> Classes only implement what they actually need            │
│                                                                  │
│  DIP: High-level depends on abstractions (not concretions)       │
│   └──> Easy to swap implementations and write tests              │
│                                                                  │
│  ─────────────────────────────────────────────                   │
│  Together they produce code that is:                             │
│  ✅ Maintainable  ✅ Testable  ✅ Extensible  ✅ Reusable        │
└──────────────────────────────────────────────────────────────────┘
```

### A Combined Example

```java
// SRP: Each service has one responsibility
// OCP: New notification types don't change existing code
// LSP: Any NotificationSender can be used interchangeably
// ISP: Interfaces are focused (Sendable, Loggable, Trackable)
// DIP: OrderService depends on abstractions, not concrete classes

interface NotificationSender {    // ISP: Small, focused interface
    void send(String to, String message);
}

class EmailSender implements NotificationSender {  // OCP: Add new senders freely
    public void send(String to, String message) {
        System.out.println("Email → " + to);
    }
}

class SmsSender implements NotificationSender {  // LSP: Substitutable for EmailSender
    public void send(String to, String message) {
        System.out.println("SMS → " + to);
    }
}

class OrderService {                              // SRP: Only handles orders
    private final NotificationSender sender;      // DIP: Depends on abstraction
    
    public OrderService(NotificationSender sender) {
        this.sender = sender;                     // Injected dependency
    }
    
    public void placeOrder(String customer) {
        // ... order logic ...
        sender.send(customer, "Order confirmed!");
    }
}
```

---

> **Remember:** SOLID principles are **guidelines**, not strict rules. Apply them thoughtfully — over-engineering a simple CRUD app with SOLID patterns everywhere is just as bad as ignoring them in a complex system. Use your judgment! 🎯

---

## Interview Questions — SOLID Principles

**Q1. Explain SOLID principles in one sentence each.**
- **S** — A class should have only one reason to change (one job).
- **O** — Add new features by adding new code, not changing existing code.
- **L** — A subclass should be usable wherever its parent is used without breaking things.
- **I** — Don't force classes to implement interface methods they don't need.
- **D** — High-level code should depend on interfaces/abstractions, not concrete implementations.

**Q2. How does Spring Boot implement SOLID principles?**
- **SRP**: `@Service`, `@Repository`, `@Controller` separate concerns by layer.
- **OCP**: Adding a new payment type = new `@Component` implementing `PaymentProcessor`, no existing code changes.
- **LSP**: Any `UserRepository` (in-memory for tests, JPA for prod) is substitutable.
- **ISP**: `CrudRepository`, `JpaRepository`, `PagingAndSortingRepository` are segregated interfaces.
- **DIP**: `@Autowired` injects by interface type — `UserService` depends on `UserRepository` interface, not `JpaUserRepository`.

**Q3. What is the Open/Closed Principle? Give a real Spring Boot example.**
```java
// BAD: Adding a new discount type requires modifying existing code
public double calculateDiscount(Order order) {
    if (order.getType().equals("PREMIUM")) return order.getTotal() * 0.1;
    if (order.getType().equals("STUDENT")) return order.getTotal() * 0.2;
    // Every new discount type = modify this method = violates OCP
    return 0;
}

// GOOD: Open for extension, closed for modification
interface DiscountStrategy {
    double calculate(Order order);
    boolean supports(String orderType);
}

@Component
class PremiumDiscount implements DiscountStrategy {
    public double calculate(Order o) { return o.getTotal() * 0.1; }
    public boolean supports(String t) { return "PREMIUM".equals(t); }
}

@Component
class StudentDiscount implements DiscountStrategy {
    public double calculate(Order o) { return o.getTotal() * 0.2; }
    public boolean supports(String t) { return "STUDENT".equals(t); }
}

@Service
class DiscountService {
    private final List<DiscountStrategy> strategies;  // all injected by Spring
    DiscountService(List<DiscountStrategy> strategies) { this.strategies = strategies; }
    
    public double getDiscount(Order order) {
        return strategies.stream()
            .filter(s -> s.supports(order.getType()))
            .findFirst()
            .map(s -> s.calculate(order))
            .orElse(0.0);
        // New discount type = new @Component class, NO changes here
    }
}
```

**Q4. What is the Liskov Substitution Principle? Give an example of a violation.**
```java
// Classic violation: Square extends Rectangle
class Rectangle {
    protected int width, height;
    void setWidth(int w) { this.width = w; }
    void setHeight(int h) { this.height = h; }
    int getArea() { return width * height; }
}

class Square extends Rectangle {
    @Override
    void setWidth(int w) { this.width = this.height = w; }   // VIOLATION!
    @Override
    void setHeight(int h) { this.width = this.height = h; }  // VIOLATION!
}

// Code that works for Rectangle BREAKS for Square:
void printArea(Rectangle r) {
    r.setWidth(5);
    r.setHeight(10);
    System.out.println(r.getArea());  // Expected: 50, but Square gives: 100!
}
// Fix: Don't extend. Make both implement a common Shape interface separately.
```

**Q5. How does the Single Responsibility Principle relate to microservices?**
- SRP at the code level → one class, one responsibility.
- SRP at the service level → **microservices architecture**: one service handles one business domain (UserService, OrderService, PaymentService).
- A microservice that handles users AND payments AND inventory violates SRP — changes to inventory requirements affect user management code.

**Q6. What is Dependency Injection and how does it support DIP?**
- DI is the practice of passing (injecting) dependencies into an object rather than having it create them.
- **Constructor injection** (preferred) ensures the dependency is an abstraction (interface):
```java
// Without DI — tight coupling
class OrderService {
    private MySQLOrderRepo repo = new MySQLOrderRepo();  // hardcoded concrete class
}

// With DI — DIP satisfied
class OrderService {
    private final OrderRepository repo;  // depends on interface
    OrderService(OrderRepository repo) { this.repo = repo; }  // injected
}
// Spring injects the concrete impl at startup
// Test injects a mock/fake
```

**Q7. What is "Code Smell" and how do SOLID principles address common smells?**
| Code Smell | Violated Principle | Fix |
|---|---|---|
| God Class | SRP | Split into focused classes |
| Shotgun Surgery | OCP | Use abstractions for extension points |
| Refused Bequest | LSP | Refactor inheritance hierarchy |
| Interface Pollution | ISP | Split fat interfaces |
| Hardcoded Dependencies | DIP | Use interfaces + DI |
