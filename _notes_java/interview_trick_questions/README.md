# Java and Spring Boot: Tricky Interview Questions 

This document contains some of the most common "trick" questions asked in Java and Spring Boot interviews. These questions are designed to test your deep understanding of how Java and the Spring Framework actually work under the hood.

---

## 🏗️ Core Java Trick Questions

### 1. The Integer Pool Trick
**Question:** What will the following code output?
```java
Integer a = 100;
Integer b = 100;
System.out.println(a == b);

Integer c = 1000;
Integer d = 1000;
System.out.println(c == d);
```
**Answer:** `true`, then `false`. 
**Why?** Java caches `Integer` objects for values from **-128 to 127** (Integer Cache). Since `100` falls in this range, `a` and `b` point to the exact same object in memory. However, `1000` is outside the cache, so `c` and `d` are two different objects created on the heap. `==` checks for memory reference equality, not value equality.

### 2. Method Overloading with `null`
**Question:** What happens when you run this code?
```java
public class Test {
    public void print(Object o) { System.out.println("Object"); }
    public void print(String s) { System.out.println("String"); }
    
    public static void main(String[] args) {
        new Test().print(null);
    }
}
```
**Answer:** It prints `"String"`.
**Why?** In Java method overloading, the compiler always chooses the most **specific** type. Since `String` is a subclass of `Object`, `String` is more specific. (If you added a third method `public void print(Integer i)`, the compiler would throw an "ambiguous method call" error because `String` and `Integer` are siblings).

### 3. The `try-finally` Return Override
**Question:** What value does this method return?
```java
public int getNumber() {
    try {
        return 1;
    } finally {
        return 2;
    }
}
```
**Answer:** `2`.
**Why?** The `finally` block **always** executes before the method actually returns to the caller. If the `finally` block itself contains a `return` statement, it will override any `return` statement executed in the `try` or `catch` block.

### 4. The `System.exit()` Trap
**Question:** Will the `finally` block execute here?
```java
try {
    System.out.println("Try");
    System.exit(0);
} finally {
    System.out.println("Finally");
}
```
**Answer:** No. It only prints "Try".
**Why?** `System.exit(0)` forcefully terminates the JVM immediately. This is the one rare case where a `finally` block will **not** execute.

### 5. String Constants vs Heap Objects
**Question:** How many objects are created here?
```java
String s1 = new String("Hello");
```
**Answer:** Two (usually).
**Why?** 
1. `"Hello"` is a literal, so an object is created and placed in the **String Constant Pool**.
2. The `new String(...)` explicitly forces Java to create a second, separate object on the **Heap**, outside the pool. 
*(Note: If "Hello" was already in the pool from elsewhere in the application, only one new object is created on the heap.)*

---

## 🍃 Spring & Spring Boot Trick Questions

### 1. The `@Transactional` Self-Invocation Trap
**Question:** If method `A` calls method `B` inside the same class, and method `B` is annotated with `@Transactional`, will a transaction be started?
```java
@Service
public class UserService {
    public void createAccount() {
        // ... some logic ...
        saveToDatabase(); // Calling the method below
    }

    @Transactional
    public void saveToDatabase() {
        // ... DB save ...
    }
}
```
**Answer:** **No transaction is started.**
**Why?** Spring uses AOP proxies for annotations like `@Transactional`. When another class calls your Spring Bean, it actually calls the Proxy, which starts the transaction and then calls the real method. However, when you call a method from *within the same class* (`this.saveToDatabase()`), you are completely bypassing the Proxy and calling the direct method. Hence, the annotation is ignored.

### 2. Singleton Beans containing Prototype Beans
**Question:** Application has a Singleton Bean `A` and a Prototype Bean `B`. Bean `A` injects Bean `B`. If you call `A.doSomething()` which uses `B` multiple times, do you get a new instance of `B` each time?
```java
@Component // Singleton by default
public class SingletonA {
    @Autowired
    private PrototypeB prototypeB;
    
    public void useB() {
        prototypeB.doWork();
    }
}
```
**Answer:** **No, you get the exact same instance every time.**
**Why?** Bean `A` is a Singleton, so Spring only creates `A` **once**. When `A` is created, Spring provides it with a new instance of `B`. But since `A` is never created again, the injected `B` is never refreshed. 
**Fix:** To get a new `B` every time, you must inject `ObjectFactory<PrototypeB>` or use `@Lookup` method injection.

### 3. Circular Dependencies
**Question:** Class A needs Class B, and Class B needs Class A. What happens?
**Answer:** 
* **If configuring via Constructor Injection:** Spring will throw a `BeanCurrentlyInCreationException` on startup and the app crashes. (This is generally the expected behavior to prevent bad design).
* **If configuring via Field/Setter Injection:** Spring *can* resolve this via a 3-level caching mechanism (early exposing the bean reference before fully initializing it). 

*(Note: Starting in Spring Boot 2.6+, circular dependencies are strictly forbidden by default even with Field injection, requiring `spring.main.allow-circular-references=true` to override).*

### 4. Bean Initialization Lifecycle Order
**Question:** If a Spring bean implements `InitializingBean`, has a method with `@PostConstruct`, and has a custom `initMethod` defined, in what order do they execute?
**Answer:**
1. `@PostConstruct` annotated method.
2. `afterPropertiesSet()` from `InitializingBean`.
3. Custom `initMethod` (e.g., defined in XML or via `@Bean(initMethod="...")`).

### 5. Private `@Transactional` Methods
**Question:** What happens if you mark a `private` method with `@Transactional`?
**Answer:** **It fails silently**. Spring AOP proxies only work on public methods (when using default standard proxying). A private method cannot be proxied or intercepted, so the `@Transactional` annotation is simply ignored by Spring. 

### 6. `@Value` null evaluations
**Question:** Is it possible to assign a default value to `@Value` if the property doesn't exist?
**Answer:** Yes, via the Elvis-like operator format: `@Value("${my.property:defaultValue}")`. If you don't provide the `:defaultValue` and the property is missing in `application.properties`, the application will crash on startup rather than assigning `null`.
