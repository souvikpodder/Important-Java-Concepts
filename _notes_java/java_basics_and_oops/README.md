# Java Fundamentals and Object Oriented Programming

Object-Oriented Programming is a methodology or paradigm to design a program using classes and objects. It simplifies the software development and maintenance. Main Concepts - Inheritance, Polymorphism, Abstraction, Encapsulation.

## Data Types in Java

<table class="alt"> 
<tbody><tr> 
  <th id="table_dvpt_datatype"><strong>Data Type</strong></th> 
  <th id="table_dvpt_defaultvalue"><strong>Default Value</strong></th> 
  <th id="table_dvpt_defaultsize"><strong>Default size</strong></th> 
</tr> 
<tr> 
<td headers="table_dvpt_datatype">boolean</td> 
<td headers="table_dvpt_defaultvalue">false</td> 
<td headers="table_dvpt_defaultsize">1 bit</td> 
</tr> 
<tr> 
<td headers="table_dvpt_datatype">char</td> 
<td headers="table_dvpt_defaultvalue">'\u0000'</td> 
<td headers="table_dvpt_defaultsize">2 byte</td> 
</tr> 
<tr> 
<td headers="table_dvpt_datatype">byte</td> 
<td headers="table_dvpt_defaultvalue">0</td> 
<td headers="table_dvpt_defaultsize">1 byte</td> 
</tr> 
<tr> 
<td headers="table_dvpt_datatype">short</td> 
<td headers="table_dvpt_defaultvalue">0</td> 
<td headers="table_dvpt_defaultsize">2 byte</td> 
</tr> 
<tr> 
<td headers="table_dvpt_datatype">int</td> 
<td headers="table_dvpt_defaultvalue">0</td> 
<td headers="table_dvpt_defaultsize">4 byte</td> 
</tr> 
<tr> 
<td headers="table_dvpt_datatype">long</td> 
<td headers="table_dvpt_defaultvalue">0L</td> 
<td headers="table_dvpt_defaultsize">8 byte</td> 
</tr> 
<tr> 
<td headers="table_dvpt_datatype">float</td> 
<td headers="table_dvpt_defaultvalue">0.0f</td> 
<td headers="table_dvpt_defaultsize">4 byte</td> 
</tr> 
<tr> 
<td headers="table_dvpt_datatype">double</td> 
<td headers="table_dvpt_defaultvalue">0.0d</td> 
<td headers="table_dvpt_defaultsize">8 byte</td> 
</tr> 
</tbody></table>

NOTE : UTF-8 is the most popular unicode character encoding with 90% websites using it.

## Data Type Promotion in Java :

![data type promotion small](https://user-images.githubusercontent.com/2780145/34364362-403e9db4-eaab-11e7-914b-7acc9007cf41.png)

## Wrapper Classes in Java

<table class="alt">
<tbody><tr><th>Primitive Type</th><th>Wrapper class</th></tr>
<tr><td>boolean</td><td>Boolean</td></tr>
<tr><td>char</td><td>Character</td></tr>
<tr><td>byte</td><td>Byte</td></tr>
<tr><td>short</td><td>Short</td></tr>
<tr><td>int</td><td>Integer</td></tr>
<tr><td>long</td><td>Long</td></tr>
<tr><td>float</td><td>Float</td></tr>
<tr><td>double</td><td>Double</td></tr>
</tbody></table>

## Operators in Java

<table class="alt"> 
<tbody><tr><th>Operator Type</th><th>Category</th><th>Precedence</th></tr> 
<tr> 
<td rowspan="2">Unary</td><td>postfix</td><td><code><em>expr</em>++ <em>expr</em>--</code></td> 
</tr>  
<tr> 
<td>prefix</td><td headers="precedence"><code>++<em>expr</em> --<em>expr</em> +<em>expr</em> -<em>expr</em> ~ !</code></td> 
</tr> 
<tr> 
<td rowspan="2">Arithmetic</td><td>multiplicative</td><td headers="precedence"><code>* / %</code></td> 
</tr> 
<tr> 
<td>additive</td><td headers="precedence"><code>+ -</code></td> 
</tr> 
<tr> 
<td>Shift</td><td>shift</td><td headers="precedence"><code>&lt;&lt; &gt;&gt; &gt;&gt;&gt;</code></td>  
</tr> 
<tr> 
<td rowspan="2">Relational</td><td>comparison</td><td headers="precedence"><code>&lt; &gt; &lt;= &gt;= instanceof</code></td> 
</tr> 
<tr> 
<td>equality</td><td headers="precedence"><code>== !=</code></td> 
</tr>  
<tr> 
<td rowspan="3">Bitwise</td><td>bitwise AND</td><td headers="precedence"><code>&amp;</code></td> 
</tr> 
<tr> 
<td>bitwise exclusive OR</td><td headers="precedence"><code>^</code></td> 
</tr> 
<tr> 
<td>bitwise inclusive OR</td><td headers="precedence"><code>|</code></td> 
</tr>  
<tr> 
<td rowspan="2">Logical</td><td>logical AND</td><td headers="precedence"><code>&amp;&amp;</code></td> 
</tr> 
<tr> 
<td>logical OR</td><td headers="precedence"><code>||</code></td> 
</tr> 
<tr> 
<td>Ternary</td><td>ternary</td><td headers="precedence"><code>? :</code></td> 
</tr> 
<tr> 
<td>Assignment</td><td>assignment</td><td headers="precedence"><code>= += -= *= /= %= &amp;= ^= |= &lt;&lt;= &gt;&gt;= &gt;&gt;&gt;=</code></td> 
</tr> 
</tbody></table>

## Java Naming Conventions :

<table class="alt">
<tbody><tr><th>Name</th><th>Convention</th></tr>
<tr><td>class name</td><td> should start with uppercase letter and be a noun 
<br>e.g. String, Color, Button, System, Thread etc.</td></tr>
<tr><td>interface name</td><td>should start with uppercase letter and be an adjective 
<br>e.g. Runnable, Remote, ActionListener etc.</td></tr>
<tr><td>method name</td><td>should start with lowercase letter and be a verb 
<br>e.g. actionPerformed(), main(), print(), println() etc.
</td></tr>
<tr><td>variable name</td><td>should start with lowercase letter
<br>e.g. firstName, orderNumber etc.</td></tr>
<tr><td>package name</td><td>should be in lowercase letter 
<br>e.g. java, lang, sql, util etc.
</td></tr>
<tr><td>constants name</td><td>should be in uppercase letter.
<br>e.g. RED, YELLOW, MAX_PRIORITY etc.</td></tr>
</tbody></table>

## Object vs Class

<table class="alt">
<tbody><tr><th>Object</th><th>Class</th></tr>
<tr><td>Object is an <strong>instance</strong> of a class.</td><td>Class is a <strong>blueprint or template</strong> from which objects are created.</td></tr>
<tr><td>Object is a <strong>real world entity</strong> such as pen, laptop, mobile, bed, keyboard, mouse, chair etc.</td><td>Class is a <strong>group of similar objects</strong>.</td></tr>
<tr><td>Object is a <strong>physical</strong> entity.</td><td>Class is a <strong>logical</strong> entity.</td></tr>
<tr><td>Object is created through <strong>new keyword</strong> mainly e.g. Student s1=new Student();</td><td>Class is declared using <strong>class keyword</strong> e.g. class Student{}</td></tr>
<tr><td>Object is created <strong>many times</strong> as per requirement.</td><td>Class is declared <strong>once</strong>.</td></tr>
<tr><td>Object <strong>allocates memory when it is created</strong>.</td><td>Class <strong>doesn't allocated memory when it is created</strong>.</td></tr>
<tr><td>There are <strong>many ways to create object</strong> like new keyword, newInstance() method, clone() method, factory method & deserialization.</td><td>There is only <strong>one way to define class</strong> in java using class keyword.</td></tr>
</tbody></table>

## Constructors vs Methods

<table class="alt">
<tbody><tr><th>Java Constructor</th><th>Java Method</th></tr>
<tr><td>Constructor is used to initialize the state of an object.</td><td>Method is used to expose behaviour of an object.</td></tr>
<tr><td>Constructor must not have return type.</td><td>Method must have return type.</td></tr>
<tr><td>Constructor is invoked implicitly.</td><td>Method is invoked explicitly.</td></tr>
<tr><td>Compiler provides a default constructor if you don't have any constructor.</td><td>Method is not provided by compiler in any case.</td></tr>
<tr><td>Constructor name must be same as the class name.</td><td> Method name may or may not be same as class name.</td></tr>
</tbody></table>

## Types of Inheritance (Supported through Class)

![single inheritance](https://user-images.githubusercontent.com/2780145/34364364-40b6b646-eaab-11e7-8c92-2c4cd9d0b2ca.png)

## Types of Inheritance (Supported through Interface only)

![multiple inheritance](https://user-images.githubusercontent.com/2780145/34364363-407486b8-eaab-11e7-94e2-5c1876f414d3.png)

## Association vs Aggregation vs Composition

![association-aggregation-composition](https://user-images.githubusercontent.com/2780145/34364371-5db00694-eaab-11e7-8ef2-bf56d3394f15.png)

## Aggregation vs Composition

<table class="alt">
<tbody><tr><th>Aggregation</th><th>Composition</th></tr>
<tr><td>Aggregation is a weak Association.</td><td>Composition is a strong Association.</td></tr>
<tr><td>Class can exist independently without owner.</td><td>Class can not meaningfully exist without owner.</td></tr>
<tr><td>Have their own Life Time.</td><td>Life Time depends on the Owner.</td></tr>
<tr><td>A uses B.</td><td>A owns B.</td></tr>
<tr><td>Child is not owned by 1 owner.</td><td>Child can have only 1 owner.</td></tr>
<tr><td>Has-A relationship. A has B.</td><td>Part-Of relationship. B is part of A.</td></tr>
<tr><td>Denoted by a empty diamond in UML.</td><td>Denoted by a filled diamond in UML.</td></tr>
<tr><td>We do not use "final" keyword for Aggregation.</td><td>"final" keyword is used to represent Composition.</td></tr>
<tr><td>Examples:<br>- Car has a Driver.<br>- A Human uses Clothes.<br>- A Company is an aggregation of People.<br>- A Text Editor uses a File.<br>- Mobile has a SIM Card.</td><td>Examples:<br>- Engine is a part of Car.<br>- A Human owns the Heart.<br>- A Company is a composition of Accounts.<br>- A Text Editor owns a Buffer.<br>- IMEI Number is a part of a Mobile.</td></tr>
</tbody></table>

NOTE : "final" keyword is used in Composition to make sure child variable is initialized.

## Polymorphism - Method Overloading vs Method Overriding

<table class="alt">
<tbody><tr><th>Method Overloading </th><th>Method Overriding</th></tr>
<tr><td>Method overloading is used <em>to increase the readability</em> of the program.</td><td>Method overriding is used <em>to provide the specific implementation</em> of the method that is already provided by its super class.</td></tr>
<tr><td>Method overloading is performed <em>within class</em>.</td><td>Method overriding occurs <em>in two classes</em> that have IS-A (inheritance) relationship.</td></tr>
<tr><td>In case of method overloading, <em>parameter must be different</em>.</td><td>In case of method overriding, <em>parameter must be same</em>.</td></tr>
<tr><td>Method overloading is the example of <em>compile time polymorphism</em>.</td><td>Method overriding is the example of <em>run time polymorphism</em>.</td></tr>
<tr><td>In java, method overloading can't be done by changing only the return type of method. <em>Return type can be same/different</em> in overloading, but you must change the parameter.</td><td><em>Return type must be same or covariant (changing return type to subclass type)</em> in method overriding.</td></tr>
</tbody></table>

## Abstract Class vs Interface

<table class="alt">
<tbody><tr><th>Abstract class</th><th>Interface</th></tr>
<tr><td>Abstract class can <strong>have abstract and non-abstract</strong> methods.</td><td>Interface can have <strong>only abstract</strong> methods. Since Java 8, it can have <strong>default & static methods</strong>. Since Java 9, it can have <strong>private methods</strong> also.</td></tr>
<tr><td>Abstract class <strong>doesn't support multiple inheritance</strong>.</td><td>Interface <strong>supports multiple inheritance</strong>.</td></tr>
<tr><td>Abstract class <strong>can have final, non-final, static and non-static variables</strong>.</td><td>Interface has <strong>only static and final variables</strong>.</td></tr>
<tr><td>Abstract class <strong>can provide the implementation of interface</strong>.</td><td>Interface <strong>can't provide the implementation of abstract class</strong>.</td></tr>
<tr><td>The <strong>abstract keyword</strong> is used to declare abstract class.</td><td>The <strong>interface keyword</strong> is used to declare interface.</td></tr>
<tr><td><strong>Example:</strong><br> public abstract class Shape{<br>public abstract void draw();}</td><td><strong>Example:</strong><br> public interface Drawable{<br>void draw();}</td></tr>
</tbody></table>

### Modern Java Interface Evolution

**Java 8 introduced:**
- **default methods** - Interfaces can have method implementations using `default` keyword
- **static methods** - Interfaces can have static utility methods

**Java 9 introduced:**
- **private methods** - Helper methods for default methods (code reuse within interface)
- **private static methods** - Static helper methods

```java
public interface ModernInterface {
    // Abstract method (traditional)
    void abstractMethod();
    
    // Default method (Java 8+) - provides default implementation
    default void defaultMethod() {
        privateHelper();  // Can use private methods
        System.out.println("Default implementation");
    }
    
    // Static method (Java 8+) - utility method
    static void staticMethod() {
        System.out.println("Static method in interface");
    }
    
    // Private method (Java 9+) - helper for default methods
    private void privateHelper() {
        System.out.println("Private helper");
    }
    
    // Private static method (Java 9+)
    private static void privateStaticHelper() {
        System.out.println("Private static helper");
    }
}
```

## Java Access Modifiers

<table class="alt">
<tbody><tr><th>Access Modifier</th><th>within class</th><th>within package</th><th>outside package by subclass only</th><th>outside package</th></tr>
<tr><td><b>Private</b></td><td>Y</td><td>N</td><td>N</td><td>N</td></tr>
<tr><td><b>Default</b></td><td>Y</td><td>Y</td><td>N</td><td>N</td></tr>
<tr><td><b>Protected</b></td><td>Y</td><td>Y</td><td>Y</td><td>N</td></tr>
<tr><td><b>Public</b></td><td>Y</td><td>Y</td><td>Y</td><td>Y</td></tr>
</tbody></table>

## Abstraction vs Encapsulation

<table class="alt">
<tbody><tr><th>Abstraction</th><th>Encapsulation</th></tr>
<tr><td>Abstraction is a process of hiding the implementation details and showing only functionality to the user.</td>
<td> Encapsulation is a process of wrapping code and data together into a single unit</td></tr>
<tr><td>Abstraction lets you focus on what the object does instead of how it does it.</td>
<td>Encapsulation provides you the control over the data and keeping it safe from outside misuse.</td></tr>
<tr><td>Abstraction solves the problem in the Design Level.</td>
<td>Encapsulation solves the problem in the Implementation Level.</td></tr>
<tr><td>Abstraction is implemented by using Interfaces and Abstract Classes.</td>
<td>Encapsulation is implemented by using Access Modifiers (private, default, protected, public)</td></tr>
<tr><td>Abstraction means hiding implementation complexities by using interfaces and abstract class.</td>
<td>Encapsulation means hiding data by using setters and getters.</td></tr>
</tbody></table>

### Code Examples

**Abstraction Example** - Hiding complexity, showing only what matters:
```java
// Abstract class defines WHAT a vehicle does (abstraction)
abstract class Vehicle {
    abstract void start();
    abstract void stop();
    
    // User doesn't need to know HOW these work internally
}

// Interface provides abstraction for payment processing
interface PaymentProcessor {
    void processPayment(double amount);  // Hide the complex payment logic
}

class CreditCardProcessor implements PaymentProcessor {
    @Override
    public void processPayment(double amount) {
        // Complex implementation hidden from user
        validateCard();
        connectToBank();
        deductAmount(amount);
        sendConfirmation();
    }
    // All these private methods are hidden (abstracted away)
    private void validateCard() { }
    private void connectToBank() { }
    private void deductAmount(double amt) { }
    private void sendConfirmation() { }
}
```

**Encapsulation Example** - Protecting data with controlled access:
```java
class BankAccount {
    // Private fields - data is HIDDEN (encapsulated)
    private String accountNumber;
    private double balance;
    
    // Constructor
    public BankAccount(String accNo, double initialBalance) {
        this.accountNumber = accNo;
        this.balance = initialBalance;
    }
    
    // Public getter - CONTROLLED read access
    public double getBalance() {
        return balance;
    }
    
    // Public methods - CONTROLLED write access with validation
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;  // Protected modification
        }
    }
    
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;  // Safe withdrawal with validation
        }
    }
    
    // No setter for balance - prevents direct manipulation!
    // No getter for accountNumber - extra security!
}
```

**Key Insight:** In the `BankAccount` example, you can't do `account.balance = -1000;` (encapsulation prevents misuse), and you don't need to know how `withdraw()` internally processes the transaction (abstraction hides complexity).

## String, String Pool & Immutability

### Why is String immutable in Java?
- String is stored in a **String Constant Pool (SCP)** — a special memory region in the heap.
- When you write `String s = "Hello"`, the JVM checks if `"Hello"` already exists in the pool. If yes, it reuses the same reference.
- Immutability makes this sharing safe: multiple references can point to the same object without risk of one reference changing the value for others.
- Immutability also makes Strings **thread-safe by default** and safe to use as HashMap keys.

```java
String s1 = "Hello";        // Goes to String Pool
String s2 = "Hello";        // Reuses same pool object
String s3 = new String("Hello"); // Creates a NEW object on the heap (not in pool)

System.out.println(s1 == s2);      // true  (same pool reference)
System.out.println(s1 == s3);      // false (different heap object)
System.out.println(s1.equals(s3)); // true  (same content)

// intern() forces a heap string into the pool
String s4 = s3.intern();
System.out.println(s1 == s4);  // true (s4 now points to pool)
```

### String vs StringBuilder vs StringBuffer

| | String | StringBuilder | StringBuffer |
|---|---|---|---|
| Mutability | Immutable | Mutable | Mutable |
| Thread-safe | Yes (by nature) | No | Yes (synchronized) |
| Performance | Slow (for concat) | Fast | Slower than StringBuilder |
| Use when | Fixed value | Single-thread concat | Multi-thread concat |

```java
// String concatenation in loop — BAD (creates many objects)
String result = "";
for (int i = 0; i < 1000; i++) result += i; // 1000 new String objects!

// StringBuilder — GOOD
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) sb.append(i); // Only 1 object
String result2 = sb.toString();
```

## equals() and hashCode() Contract

This is one of the most important contracts in Java, critical for correct behavior in HashMap/HashSet.

**The Contract:**
1. If `a.equals(b)` is `true`, then `a.hashCode() == b.hashCode()` MUST be `true`.
2. If `a.hashCode() == b.hashCode()`, `a.equals(b)` MAY or MAY NOT be `true` (collision).

```java
class Employee {
    int id;
    String name;

    // MUST override both together
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee e = (Employee) o;
        return id == e.id && Objects.equals(name, e.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);  // Same fields as equals()
    }
}

// What happens if you only override equals() but NOT hashCode():
Set<Employee> set = new HashSet<>();
Employee e1 = new Employee(1, "Alice");
set.add(e1);
Employee e2 = new Employee(1, "Alice");
System.out.println(set.contains(e2)); // FALSE! Wrong — should be true
// Reason: e1 and e2 are equal by equals(), but different hashCodes
// → they go to different buckets in HashSet → not found
```

## Methods of Object Class
The Object class is the parent class of all the classes in java by default.

<table class="alt">
<tbody><tr><th>Method</th><th>Description</th></tr>
<tr><td>public final Class getClass()</td><td>returns the Class class object of this object. The Class class can further be used to get the metadata of this class.</td></tr>
<tr><td>public int hashCode()</td><td> returns the hashcode number for this object.</td></tr>
<tr><td>public boolean equals(Object obj)</td><td> compares the given object to this object.</td></tr>
<tr><td>protected Object clone() throws CloneNotSupportedException</td><td> creates and returns the exact copy (clone) of this object.</td></tr>
<tr><td>public String toString()</td><td> returns the string representation of this object.</td></tr>
<tr><td>public final void notify()</td><td> wakes up single thread, waiting on this object's monitor.</td></tr>
<tr><td>public final void notifyAll()</td><td> wakes up all the threads, waiting on this object's monitor.</td></tr>
<tr><td>public final void wait(long timeout)throws InterruptedException</td><td> causes the current thread to wait for the specified milliseconds, until another thread notifies (invokes notify() or notifyAll() method).</td></tr>
<tr><td>public final void wait(long timeout,int nanos)throws InterruptedException</td><td>causes the current thread to wait for the specified milliseconds and nanoseconds, until another thread notifies (invokes notify() or notifyAll() method).</td></tr>
<tr><td>public final void wait()throws InterruptedException</td><td> causes the current thread to wait, until another thread notifies (invokes notify() or notifyAll() method).</td></tr>
<tr><td>protected void finalize()throws Throwable</td><td> is invoked by the garbage collector before object is being garbage collected.</td></tr>
</tbody></table>

---

## Additional Deep Concepts

### Covariant Return Type
A subclass can override a method and return a more specific (sub) type.
```java
class Animal {
    Animal create() { return new Animal(); }
}
class Dog extends Animal {
    @Override
    Dog create() { return new Dog(); }  // Covariant — Dog is a subtype of Animal
}
```

### Constructor Chaining
```java
class Person {
    String name;
    int age;

    Person() { this("Unknown", 0); }            // calls 2-arg constructor
    Person(String name) { this(name, 0); }       // calls 2-arg constructor
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
// super() call: calls parent constructor — must be first statement
class Student extends Person {
    String school;
    Student(String name, int age, String school) {
        super(name, age);        // must be FIRST
        this.school = school;
    }
}
```

### Static vs Instance Members
```java
class Counter {
    static int count = 0;  // shared across ALL instances
    int id;                // unique per instance

    Counter() {
        count++;           // shared counter
        id = count;        // unique id
    }
}
Counter c1 = new Counter();  // count=1, c1.id=1
Counter c2 = new Counter();  // count=2, c2.id=2
Counter c3 = new Counter();  // count=3, c3.id=3
```

### `final` keyword — 3 uses
```java
final int MAX = 100;          // final variable: cannot reassign
final class Immutable { }     // final class: cannot extend (e.g., String)
final void doSomething() { }  // final method: cannot override
```

### `instanceof` Pattern Matching (Java 16+)
```java
// Old way
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// New way — pattern matching
if (obj instanceof String s) {
    System.out.println(s.length());  // s is already cast
}
```

---

## Interview Questions — Java OOPs & Fundamentals

**Q1. What is the difference between `==` and `.equals()`?**
- `==` checks **reference equality** (same memory address).
- `.equals()` checks **logical equality** (content, as defined by the class).
- For primitives, `==` compares values. For objects, always use `.equals()` unless you intentionally want reference comparison.

**Q2. Can we override a `static` method? What is method hiding?**
- No. Static methods are resolved at **compile time** based on the reference type, not at runtime (no dynamic dispatch).
- If a subclass defines a static method with the same signature, it **hides** the parent's method — it doesn't override it.
```java
class Parent {
    static void show() { System.out.println("Parent"); }
}
class Child extends Parent {
    static void show() { System.out.println("Child"); }  // hiding
}
Parent p = new Child();
p.show();  // "Parent" — resolved by reference type, not object type
```

**Q3. Why can't we instantiate an abstract class?**
- An abstract class may have abstract methods with no body. Instantiating it would create an object with undefined behavior. The compiler enforces this rule.

**Q4. What is the diamond problem and how does Java solve it?**
- When a class inherits from two interfaces that both have a default method with the same signature, it's ambiguous which to use.
- Java requires the implementing class to **explicitly override** the method and choose (or provide a new implementation).
```java
interface A { default void greet() { System.out.println("A"); } }
interface B { default void greet() { System.out.println("B"); } }
class C implements A, B {
    @Override
    public void greet() { A.super.greet(); }  // explicitly pick A's version
}
```

**Q5. What is the difference between Composition and Inheritance? When to prefer which?**
- **Inheritance** ("is-a"): `Dog extends Animal` — use when the subclass truly IS a type of the parent.
- **Composition** ("has-a"): `Car has an Engine` — use when you want to reuse behavior without creating a tight coupling.
- **Prefer composition over inheritance** (Effective Java): it's more flexible, avoids fragile base class problems, and is easier to test.

**Q6. Can a constructor be `private`? What's the use case?**
- Yes. A private constructor prevents external instantiation.
- Use cases: **Singleton pattern**, **utility classes** (like `Math`, `Collections`), **factory methods**.
```java
class Singleton {
    private static Singleton instance;
    private Singleton() { }  // private constructor
    public static Singleton getInstance() {
        if (instance == null) instance = new Singleton();
        return instance;
    }
}
```

**Q7. What happens if `hashCode()` always returns the same value (e.g., return 42)?**
- It's technically valid (doesn't break the contract).
- But it causes catastrophic performance: all keys hash to the same bucket → the HashMap becomes a linked list → O(n) lookup instead of O(1).

**Q8. Explain the difference between shallow copy and deep copy.**
```java
// Shallow copy: copies references, not underlying objects
class Team {
    List<String> members;
    Team(List<String> members) { this.members = members; }
    Team shallowCopy() { return new Team(this.members); } // same list ref!
}

// Deep copy: copies everything recursively
Team deepCopy() { return new Team(new ArrayList<>(this.members)); }

// With clone(): Object.clone() does a shallow copy by default
```

**Q9. What is the difference between an interface and an abstract class? When to use which?**
- Use **interface** when: you want to define a contract (what a class CAN do), multiple inheritance is needed, or you have unrelated classes sharing behavior.
- Use **abstract class** when: you want shared state (fields) or common implementation, and classes share an "is-a" relationship.
- Rule of thumb: interfaces define *capabilities* (`Flyable`, `Serializable`), abstract classes define *types* (`Animal`, `Shape`).

**Q10. What is polymorphism and how does Java implement it?**
- **Compile-time polymorphism** = method overloading (resolved by compiler based on method signature).
- **Runtime polymorphism** = method overriding + dynamic method dispatch (JVM decides which method to call based on actual object type at runtime).
```java
Animal a = new Dog();  // reference is Animal, object is Dog
a.sound();  // calls Dog's sound(), not Animal's — runtime dispatch
```

**Q11. Explain `this` and `super` keywords.**
- `this` refers to the **current object** instance. Used to call current class constructor (`this()`), access current class fields/methods.
- `super` refers to the **parent class**. Used to call parent constructor (`super()`), access parent's overridden method (`super.method()`).
- Both `this()` and `super()` must be the **first statement** in a constructor and cannot be used together.

**Q12. What is the significance of the `transient` keyword?**
- Marks a field to be excluded from serialization.
- When an object is serialized, `transient` fields are not written to the byte stream and are reset to their default values upon deserialization.
```java
class User implements Serializable {
    String username;
    transient String password;  // won't be serialized — security!
}
```
