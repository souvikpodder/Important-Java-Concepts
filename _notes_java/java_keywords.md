# Java Keywords and Contextual Keywords Reference (Up to Java 25)

In Java, there is a distinction between **Reserved Keywords** (which can never be used as identifiers) and **Contextual Keywords** (which have a special meaning only in specific contexts, but can be used as identifiers elsewhere).

---

### 1. Reserved Keywords (51 Keywords)
As of Java 25, there are **51 reserved keywords**. This includes `_` (underscore, reserved in Java 9), and `goto`/`const` (which are reserved but unused).

| <!-- --> | <!-- --> | <!-- --> | <!-- --> | <!-- --> | <!-- --> |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `abstract` | `assert` | `boolean` | `break` | `byte` | `case` |
| `catch` | `char` | `class` | `const`* | `continue` | `default` |
| `do` | `double` | `else` | `enum` | `extends` | `final` |
| `finally` | `float` | `for` | `goto`* | `if` | `implements` |
| `import` | `instanceof` | `int` | `interface` | `long` | `native` |
| `new` | `package` | `private` | `protected` | `public` | `return` |
| `short` | `static` | `strictfp` | `super` | `switch` | `synchronized` |
| `this` | `throw` | `throws` | `transient` | `try` | `void` |
| `volatile` | `while` | `_`* (Java 9+) | | | |

*\*Note: `const` and `goto` are reserved but not used. `_` (single underscore) is a reserved keyword since Java 9 and is used for unnamed patterns and variables since Java 22.*

---

### 2. Contextual Keywords (17 Keywords)
These function as keywords only in specific syntactic positions (e.g., in a module declaration or sealed class definition) to preserve backward compatibility.

| Keyword | Introduced In | Description |
| :--- | :--- | :--- |
| `exports` | Java 9 | Declares package export in `module-info.java` |
| `module` | Java 9 | Declares a module in `module-info.java` |
| `open` | Java 9 | Declares an open module or package |
| `opens` | Java 9 | Opens a package for deep reflection |
| `provides` | Java 9 | Declares a service provider implementation |
| `requires` | Java 9 | Declares a module dependency |
| `to` | Java 9 | Restricts exports/opens to specific modules |
| `transitive` | Java 9 | Declares transitive module dependencies |
| `uses` | Java 9 | Declares a service interface dependency |
| `with` | Java 9 | Specifies service implementation class |
| `var` | Java 10 | Local variable type inference |
| `yield` | Java 14 | Returns a value from a switch expression |
| `record` | Java 16 | Declares a record class (immutable DTO) |
| `non-sealed` | Java 17 | Declares a class/interface open for extension in a sealed hierarchy |
| `permits` | Java 17 | Declares allowed subclasses in a sealed hierarchy |
| `sealed` | Java 17 | Restricts subclasses inheriting from class/interface |
| `when` | Java 21 | Specifies guard conditions in pattern matching switch cases |

## 1) abstract

abstract keyword is used to implement the abstraction in java. A method which doesn’t have method definition must be declared as abstract and the class containing it must be declared as abstract. You can’t instantiate abstract classes. Abstract methods must be implemented in the sub classes. You can’t use abstract keyword with variables and constructors.

```java
abstract class AbstractClass
{
    abstract void abstractMethod();
}
```

## 2) assert

assert keyword is used in the assertion statements. These statements will enable you to test your assumptions about a program. Assertion statements provide the best way to detect and correct the programming errors. Assertion statements take one boolean expression as input and assumes that this will be always true. If the boolean expression returns false, AssertionError will be thrown.

```java
System.out.println("Enter your marks");
         
Scanner sc = new Scanner(System.in);
         
int marks = sc.nextInt();
         
assert marks > 35 : "FAIL";
```

## 3) boolean

boolean keyword is used to define boolean type variables. boolean type variables can hold only two values – either true or false.

```java
boolean isActive = true;
```

## 4) break

The break keyword is used to stop the execution of a loop(for, while, switch-case) based on some condition.


```java
for (int i = 0; i < 100; i++)
{
    System.out.println(i);
             
    if(i == 50) break;
}
```

## 5) byte

byte keyword is used to declare byte type of variables. A byte variable can hold a numeric value in the range from -128 to 127.


```java
byte b = 50;
```

## 6) switch       7) case

Both switch and case keywords are used in the switch-case statement.


```java
Scanner sc = new Scanner(System.in);

System.out.println("Enter Day :");

int day = sc.nextInt();

switch (day) 
{
    case 1:
        System.out.println("SUNDAY");
        break;

    case 2:
        System.out.println("MONDAY");
        break;

    //...

    case 7:
        System.out.println("SATURDAY");
        break;

    default:
        System.out.println("Invalid");
        break;
}
```

## 8) try     9) catch     10) finally

try, catch and finally keywords are used to handle the exceptions in java. The statements which are to be monitored for exceptions are kept in the try block. The exceptions thrown by the try block are caught in the catch block. finally block is always executed.


```java
try 
{
    int i = Integer.parseInt("abc");
} 
catch (NumberFormatException ex) 
{
    System.out.println(ex);
} 
finally 
{
    System.out.println("This will be always executed");
}
```

## 11) char

char keyword is used to declare primitive char type variables. char represents the characters in java.


```java
char a = 'A';
         
char b = 'B';
         
char c = 'C';
```

## 12) class

class keyword is used to define the classes in java.


```java
class MyClass
{
    class MyInnerClass
    {
        //Inner Class
    }
}
```

## 13) continue

continue keyword is used to stop the execution of current iteration and start the execution of next iteration in a loop.


```java
for (int i = 0; i <= 100; i++)
{
    if(i % 5 != 0)  continue;
             
    System.out.println(i);
}
```

## 14) default

The `default` keyword is used in two ways:
1. In `switch-case` statements as the default fallback branch.
2. To define default method implementations within interfaces (**introduced in Java 8**).

```java
interface MyInterface 
{
    public default void myDefaultMethod() 
    {
        System.out.println("Default Method (Java 8)");
    }
}
```

## 15) do

do keyword is used in a do–while loop. do-while loop is used to execute one or more statements repetitively until a condition returns false.


```java
int a = 10;

int b = 20;

do {
    a = a + b;

    b = b + 10;

    System.out.println("a = " + a);

    System.out.println("b = " + b);

} while (a <= 100);
```

## 16) double

double keyword is used to declare primitive double type of variables.


```java
double d1 = 23.56;
         
double d2 = 56.23;
         
double d3 = d1 + d2;
         
System.out.println(d3);
```

## 17) if         18) else

if and else keywords are used in if-else block.


```java
Scanner sc = new Scanner(System.in);
         
System.out.println("Enter a string :");
         
String input = sc.next();
         
if(input.equalsIgnoreCase("JAVA"))
{
    System.out.println("It's JAVA");
}
else
{
    System.out.println("It's not JAVA");
}
```

## 19) enum

enum keyword is used to define enum types.


```java
enum Color
{
    RED, GREEN, BLUE;
}
 
public class Test
{
    public static void main(String[] args)
    {
        Color c1 = Color.RED;
        System.out.println(c1);
    }
}
```

## 20) extends

extends keyword is used in inheritance. It is used when a class extends another class.


```java
class SuperClass
{
    //Super Class
}
 
class SubClass extends SuperClass
{
    //Sub Class
}
```

## 21) final

final keyword is used when a class or a method or a field doesn’t need further modifications. final class can’t be extended, final method can’t be overridden and the value of a final field can’t be changed.


```java
final class FinalClass
{
    final int finalVariable = 10;
     
    final void finalMethod()
    {
        //final method
    }
}
```

## 22) float

float keyword indicates primitive float type of variables.


```java
float f1 = 45.26f;
         
float f2 = 84.25f;
         
float f3 = f2 - f1;
         
System.out.println(f3);
```

## 23) for

for loop is used to execute the set of statements until a condition is true.


```java
for (int i = 0; i <= 10; i++)
{
    System.out.println(i);
}
```

## 24) implements

implements keyword is used while implementing an interface.


```java
interface MyInterface
{
    void myMethod();
}
 
class MyClass implements MyInterface
{
    public void myMethod()
    {
        System.out.println("My Method");
    }
}
```

## 25) import

import keyword is used to import the members of a particular package into current java file.


```java
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;
```

## 26) instanceOf

instanceOf is used to check whether an object is of specified type. The syntax for using instanceOf keyword is “Object_Reference instanceOf Type“.


```java
class A
{
     //...
}
 
public class MainClass
{
    public static void main(String[] args) 
    {
        A a = new A();
         
        if(a instanceof A)
        {
            System.out.println("a is of type A");
        }
    }
}
```

## 27) int

int keyword is used to declare primitive integer type of variables.


```java
int i1 = 10;
         
int i2 = 20;
         
int i3 = i1 *  i2;
         
System.out.println(i3);
```

## 28) interface

interface keyword is used to define the interfaces in java. It is a mechanism to achieve abstraction. There can be only abstract methods in the Java interface, not method body.


```java
interface MyInterface
{
    void myMethod();
}
```

## 29) long

long is used to define the primitive long type variables.


```java
long l1 = 101;
         
long l2 = 202;
         
long l3 = l1 +  l2;
         
System.out.println(l3);
```

## 30) native

native keyword is used with a method to indicate that a particular method is implemented in native code using Java Native Interfaces(JNI).


```java
class AnyClass
{
    public native void anyMethod(int i, double d);
}
```

## 31) new

new keyword is used while creating the instances of a class.


```java
class A
{
     //...
}
 
public class MainClass
{
    public static void main(String[] args) 
    {
        A a = new A();
    }
}
```

## 32) package

package keyword is used to specify a package to which the current file belongs to.


```java
package pack1;
 
class A
{
     //...
}
```

## 33) private

private keyword is used to declare a member of a class as private. private methods and fields are visible within the class in which they are defined.


```java
class A
{
    private int i = 111;   //private field
     
    private void method()
    {
        //private method
    }
}
```

## 34) protected

protected keyword is used to declare a member of a class as protected. protected members of a class are visible within the package only, but they can be inherited to any sub classes.


```java
class A
{
    protected int i = 111;   //protected field
     
    protected void method()
    {
        //protected method
    }
}
```

## 35) public

public keyword is used to declare the members of a class or class itself as public. public members of a class are visible from anywhere and they can be inherited to any sub classes.


```java
public class A
{
    public int i = 222;   //public field
     
    public A()
    {
        //public constructor
    }
     
    public void method()
    {
        //public method
    }
}
```

## 36) return

return keyword is used to return the control back to the caller from the method.


```java
class A
{
    int method(int i)
    {
        return i*i;     //method returning a value
    }
}
```

## 37) short

short keyword is used to declare primitive short type variables.


```java
short s1 = 11;
         
short s2 = 22;
```

## 38) static

static keyword is used to define the class level members of a class. static members of a class are stored in the class memory and you can access them directly through class name. No need to instantiate a class.


```java
class A
{
    static int staticField = 555;    //Static Field
     
    static void staticMethod()
    {
        //Static method
    }
}
 
public class MainClass
{
    public static void main(String[] args) 
    {
        System.out.println(A.staticField);    //Accessing staticField via class name
         
        A.staticMethod();     //Accessing staticMethod via class name
    }
}
```

## 39) strictfp

The `strictfp` keyword was historically used to ensure that floating-point calculations had strict precision (IEEE 754 standards) across all platforms. 

> [!NOTE]
> **Obsolete in Java 17:** Since **Java 17** (via JEP 306), the JVM evaluates all floating-point operations strictly by default. The `strictfp` keyword is now obsolete/redundant and has no effect, although it remains a reserved keyword for backward compatibility.

```java
// strictfp is now redundant but still legal to compile
strictfp class LegacyPrecisionClass
{
    double calculate(double a, double b) {
        return a * b;
    }
}
```

## 40) super

super keyword is used to access super class members inside a sub class.


```java
class A
{
    int i;
     
    public A(int i) 
    {
        this.i = i;
    }
     
    void methodA()
    {
        System.out.println(i);
    }
}
 
class B extends A
{
    public B()
    {
        super(10);    //Calling super class constructor
    }
     
    void methodB()
    {
        System.out.println(super.i);    //accessing super class field
         
        super.methodA();    //Calling super class method
    }
}
```

## 41) synchronized

synchronized keyword is used to implement the synchronization in java. only one thread can enter into a method or a block which is declared as synchronized. Any thread which wants to enter synchronized method or block must acquire object lock of those methods or blocks.


```java
class AnyClass
{
    synchronized void synchronizedMethod()
    {
        //Synchronized method
    }
     
    void anyMethod()
    {
        synchronized (this) 
        {
            //Synchronized block
        }
    }
}
```

## 42) this

this keyword is used to access other members of the same class.


```java
class AnyClass
{
    int i;
  
    AnyClass()
    {
        System.out.println("First Constructor");
    }
  
    AnyClass(int j)
    {
        this();    //calling statement to First Constructor
        System.out.println("Second Constructor");
    }
  
    void methodOne()
    {
        System.out.println("From method one");
    }
  
    void methodTwo()
    {
        System.out.println(this.i);  //Accessing same class field
        this.methodOne();      //Accessing same class method
    }
}
```

## 43) throw

throw keyword is used to throw the exceptions manually.


```java
public class MainClass
{
    public static void main(String[] args) 
    {
        try
        {
            //throwing NumberFormatException manually 
             
            throw new NumberFormatException();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }
}
```

## 44) throws

throws keyword is used to specify the exceptions which the current method may throw.


```java
class A
{
    void method() throws NumberFormatException
    {
        int i = Integer.parseInt("abc");
    }
}
```

## 45) transient

transient keyword is used in serialization. A variable which is declared as transient will not be eligible for serialization.


```java
class MyClass implements Serializable
{
    int a;
     
    transient String s;   //This will not be serialized
     
    double d;
}
```

## 46) void

void keyword is used to indicate that method returns nothing.


```java
class A
{
    void methodReturnsNothing()
    {
        //Method returns no value
    }
}
```

## 47) volatile

The `volatile` keyword is used in concurrent programming to solve the memory visibility problem. 

When a variable is declared as `volatile`, it guarantees two things:
1. **Visibility:** Every read of a volatile variable will be read from the main memory, not from the thread's local CPU cache. Every write to a volatile variable will be written directly to the main memory.
2. **Prevents Instruction Reordering:** It establishes a "happens-before" relationship, preventing the compiler from reordering instructions in a way that could break the expected logic in a multithreaded environment.

**Important Note:** `volatile` does **not** provide atomicity. For example, `counter++` is a read-modify-write operation, and it is not thread-safe even if `counter` is `volatile`. For atomic operations, you should use `synchronized` blocks or classes like `AtomicInteger`.

```java
class SharedResource 
{
    // A flag to signal a background thread to stop
    // Using volatile ensures the background thread immediately sees the change
    public volatile boolean isRunning = true;

    public void stopRunning() 
    {
        isRunning = false;
    }
}
```

## 48) while

while keyword is used in the while loop.


```java
int i = 10;
         
while (i <= 100)
{
    System.out.println(i);
             
    i = i + 10;
}
```

## 49) _ (Underscore)

The single underscore `_` is a reserved keyword in Java.

> [!IMPORTANT]
> **Keyword since Java 9:** In **Java 9**, the single underscore `_` was turned into a reserved keyword and could no longer be used as a variable or method identifier.
> 
> **Unnamed Variables and Patterns in Java 22:** In **Java 22** (following preview in **Java 21**), the underscore was finalized as the official syntax for **unnamed variables and patterns**. It acts as a placeholder when a variable declaration is syntactically required but its value is never used.

```java
// Example 1: Unused Exception in Catch Block (Java 22+)
try {
    int num = Integer.parseInt("abc");
} catch (NumberFormatException _) { // No need to declare a variable name like 'ex'
    System.out.println("Failed to parse integer");
}

// Example 2: Unused Lambda Parameter (Java 22+)
map.forEach((_, value) -> System.out.println("Value: " + value));
```

## 50) goto        51) const

Both `goto` and `const` are reserved keywords in Java, but they are not used by the language. Declaring them will result in compile-time errors. They were reserved to prevent developers from using them if future versions of Java decided to implement them.

**Note:** `true`, `false`, and `null` are not keywords. They are literals in Java.

---

# Contextual Keywords (Special Syntax Keywords)

Unlike reserved keywords, contextual keywords are only treated as keywords in specific places in the code. This ensures backward compatibility.

## 1) var

The `var` keyword is used for local variable type inference (**introduced in Java 10**). The compiler automatically infers the data type of the variable based on the value assigned to it. It can only be used for local variables.

```java
// The compiler infers that 'message' is of type String
var message = "Hello, Java!"; 

// Equivalent to: List<String> list = new ArrayList<>();
var list = new ArrayList<String>(); 
```

## 2) yield

The `yield` keyword is used to return a value from a `switch` expression block (**introduced in Java 14** after previewing in **Java 12**).

```java
int day = 3;
String dayType = switch (day) {
    case 1, 2, 3, 4, 5 -> "Weekday";
    case 6, 7          -> "Weekend";
    default            -> {
        System.out.println("Processing invalid day...");
        yield "Invalid Day"; // yield returns value from block
    }
};
```

## 3) record

The `record` keyword is used to declare record classes, which are concise data carrier classes (**introduced in Java 16** after previewing in **Java 14**). It automatically generates final fields, a constructor, getters, `equals()`, `hashCode()`, and `toString()`.

```java
// Declaring a record (Java 16+)
public record User(int id, String name) {}

// Usage
User user = new User(1, "Alice");
System.out.println(user.name()); // "Alice"
```

## 4) sealed, permits, non-sealed

These three keywords are used to strictly define and control class/interface inheritance (**introduced in Java 17** after previewing in **Java 15**).
*   `sealed`: Specifies that only permitted classes can extend this class/interface.
*   `permits`: Lists the subclasses permitted to extend the sealed class.
*   `non-sealed`: Declares a subclass as open for extension by any other class (re-opening the hierarchy).

```java
// Sealed class definition (Java 17+)
public sealed class Shape permits Circle, Rectangle {}

// Permitted subclass must be final, sealed, or non-sealed
public final class Circle extends Shape {}

// non-sealed subclass can be extended by any class
public non-sealed class Rectangle extends Shape {}
```

## 5) when

The `when` keyword is used in pattern matching `switch` cases to declare boolean guard conditions (**introduced in Java 21** after previewing in **Java 19**).

```java
public String testValue(Object obj) {
    return switch (obj) {
        // 'when' guard condition restricts the match (Java 21+)
        case String s when s.length() > 5 -> "Long string: " + s;
        case String s -> "Short string: " + s;
        default -> "Not a string";
    };
}
```

## 6) Module Keywords (module, requires, exports, opens, uses, provides, with, to, open, transitive)

These contextual keywords are used to define modules in the module descriptor file `module-info.java` (**introduced in Java 9**).

```java
// module-info.java example (Java 9+)
open module my.custom.module {
    // Requires another module's public APIs
    requires transitive java.sql; 

    // Exports a package to specific modules
    exports com.mycompany.app to com.mycompany.helper;

    // Opens a package for reflection
    opens com.mycompany.internal;

    // Provides a service implementation
    provides com.mycompany.service.MyService with com.mycompany.service.impl.MyServiceImpl;
}
```
