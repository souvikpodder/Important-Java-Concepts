# Generics in Java

## Generic Classes

A generic class declaration looks like a non-generic class declaration, except that the class name is followed by a type parameter section.

The type parameter section of a generic class can have one or more type parameters separated by commas. These classes are known as parameterized classes or parameterized types because they accept one or more parameters.

**Syntax:**
```java
public class Box<T> {
   private T t;
}
```

<ul class="list">
<li><p><b>Box</b> − Box is a generic class.</p></li>
<li><p><b>T</b> − The generic type parameter passed to generic class. It can take any Object.</p></li>
<li><p><b>t</b> − Instance of generic type T.</p></li>
</ul>

## Naming Conventions

By convention, type parameter names are named as single, uppercase letters so that a type parameter can be distinguished easily with an ordinary class or interface name. Following is the list of commonly used type parameter names −

<ul class="list">
<li><p><b>E</b> − Element (mainly used by Java Collections framework)</p></li>
<li><p><b>K</b> − Key (mainly used to represent key of a map)</p></li>
<li><p><b>V</b> − Value (mainly used to represent value of a map)</p></li>
<li><p><b>N</b> − Number (represents numbers)</p></li>
<li><p><b>T</b> − Type (represents first generic type parameter)</p></li>
<li><p><b>S, U, V, etc</b> − 2nd, 3rd, 4th Types</p></li>
</ul>

## Type Inference

Type inference represents the Java compiler's ability to look at a method invocation and its corresponding declaration to check and determine the type argument(s). The inference algorithm checks the types of the arguments and, if available, assigned type is returned. Inference algorithms tries to find a specific type which can fullfil all type parameters.

Compiler generates unchecked conversion warning in-case type inference is not used.

**Syntax:**
```java
Box<Integer> integerBox = new Box<>();
```

<ul class="list">
<li><p><b>Box</b> − Box is a generic class.</p></li>
<li><p><b><></b> − The diamond operator denotes type inference.</p></li>
</ul>

Using diamond operator, compiler determines the type of the parameter. This operator is available from Java SE 7 version onwards.

## Generic Methods

You can write a single generic method declaration that can be called with arguments of different types. Based on the types of the arguments passed to the generic method, the compiler handles each method call appropriately. Following are the rules to define Generic Methods −

<ul class="list">
<li><p>All generic method declarations have a type parameter section delimited by angle brackets (< and >) that precedes the method's return type ( < E > in the next example).</p></li>
<li><p>Each type parameter section contains one or more type parameters separated by commas. A type parameter, also known as a type variable, is an identifier that specifies a generic type name.</p></li>
<li><p>The type parameters can be used to declare the return type and act as placeholders for the types of the arguments passed to the generic method, which are known as actual type arguments.</p></li>
<li><p>A generic method's body is declared like that of any other method. Note that type parameters can represent only reference types, not primitive types (like int, double and char).</p></li>
</ul>

```java
public static <E> void printArray( E[] inputArray ) {
      // Display array elements
      for(E element : inputArray) {
         System.out.printf("%s ", element);
      }
      System.out.println();
   }
```

## Multiple Type Parameters

A Generic class can have multiple type parameters. Following example will showcase above mentioned concept.

```java
public class Box<S,T> {
   private T t;
   private S s;
}
```

## Parameterized Types

A Generic class can have parameterized types where a type parameter can be substituted with a parameterized type. 
Parameterized Types are types that take other types as parameters. Eg - Collection<String>, ArrayList<String>, etc.
  
```java
public class Box<S,T> {
   ...
}
...
Box<Integer, List<String>> box = new Box<Integer, List<String>>(); //Parameterized Types
...
```

## Raw Types

A raw type is an object of a generic class or interface if its type arguments are not passed during its creation.

```java
Box rawBox = new Box();
```

## Bounded Type Parameters

There may be times when you'll want to restrict the kinds of types that are allowed to be passed to a type parameter. For example, a method that operates on numbers might only want to accept instances of Number or its subclasses. This is what bounded type parameters are for.

To declare a bounded type parameter, list the type parameter's name, followed by the extends keyword, followed by its upper bound.

**Single Bound:**
```java
public static <T extends Comparable<T>> T maximum(T x, T y, T z)
```

**Multiple Bounds:**
```java
public static <T extends Number & Comparable<T>> T maximum(T x, T y, T z)
```
<ul class="list">
<li><p><b>maximum</b> − maximum is a generic method.</p></li>
<li><p><b>T</b> − The generic type parameter passed to generic method. It can take any Object.</p></li>
</ul>

The T is a type parameter passed to the generic class Box and should be subtype of Number class and must implements Comparable interface. In case a class is passed as bound, it should be passed first before interface otherwise compile time error will occur.

**Calling eg.:**
```java
maximum( 6.6, 8.8, 7.7 )
```

## Collections Framework Examples

Java has provided generic support in Collections Framework Interfaces like List, Set, Map, etc.

### List

```java
List<T> list = new ArrayList<T>();
```
<ul class="list">
<li><p><b>list</b> − object of List interface.</p></li>
<li><p><b>T</b> − The generic type parameter passed during List declaration.</p></li>
</ul>
The T is a type parameter passed to the generic interface List and its implementation class ArrayList.

### Set

```java
Set<T> set = new HashSet<T>();
```
<ul class="list">
<li><p><b>set</b> − object of Set Interface.</p></li>
<li><p><b>T</b> − The generic type parameter passed during Set declaration.</p></li>
</ul>
The T is a type parameter passed to the generic interface Set and its implementation class HashSet.

### Map
```java
Map<T> set = new HashMap<T>();
```
<ul class="list">
<li><p><b>set</b> − object of Map Interface.</p></li>
<li><p><b>T</b> − The generic type parameter passed during Map declaration.</p></li>
</ul>
The T is a type parameter passed to the generic interface Map and its implementation class HashMap.

## Generics Wild Cards

The question mark (?), represents the wildcard, stands for unknown type in generics. 

### Upper Bounded Wildcards

There may be times when you'll want to restrict the kinds of types that are allowed to be passed to a type parameter. For example, a method that operates on numbers might only want to accept instances of Number or its subclasses.

To declare a upper bounded Wildcard parameter, list the ?, followed by the extends keyword, followed by its upper bound.
```java
public static double sum(List<? extends Number> numberlist) {
      ...
   }
```

### Unbounded Wildcards

There may be times when any object can be used when a method can be implemented using functionality provided in the Object class or When the code is independent of the type parameter.

To declare a Unbounded Wildcard parameter, list the ? only.
```java
public static void printAll(List<?> list) {
    ...
   }
```

### Lower Bounded Wildcards

There may be times when you'll want to restrict the kinds of types that are allowed to be passed to a type parameter. For example, a method that operates on numbers might only want to accept instances of Integer or its superclasses like Number.

To declare a lower bounded Wildcard parameter, list the ?, followed by the super keyword, followed by its lower bound.
```java
public static void addCat(List<? super Cat> catList) {
      ...
   }
...
//You can add list of Cat or Animal (super class of the Cat class)
addCat(animalList);
addCat(catList);
```

## Type Erasure

Generics are used for tighter type checks at compile time and to provide a generic programming. To implement generic behaviour, java compiler apply type erasure. Type erasure is a process in which compiler replaces a generic parameter with actual class or bridge method. In type erasure, compiler ensures that no extra classes are created and there is no runtime overhead.

**Type Erasure rules:**
<ul class="list">
<li><p>Replace type parameters in generic type with their bound if bounded type parameters are used.</p></li>
<li><p>Replace type parameters in generic type with Object if unbounded type parameters are used.</p></li>
<li><p>Insert type casts to preserve type safety.</p></li>
<li><p>Generate bridge methods to keep polymorphism in extended generic types.</p></li>
</ul>

## Restrictions on Generics

**No Primitive Types** - Using generics, primitive types can not be passed as type parameters.
```java
Box<int> intBox = new Box<int>() //Error
```   
NOTE: Use Wrappers like Integer instead.

**No Instance** - A type parameter cannot be used to instantiate its object inside a method.
```java
public static <T> void add(Box<T> box) //Error
```   
NOTE: To achieve such functionality, reflection can be used.

**No Static field** - Using generics, type parameters are not allowed to be static. As static variable is shared among object so compiler can not determine which type to used.
```java
class Box<T> {   
   private static T t; //Error
}
```   

**No Cast** - Casting to a parameterized type is not allowed unless it is parameterized by unbounded wildcards.

```java
Box<Integer> integerBox = new Box<Integer>();
Box<Number> numberBox = new Box<Number>();
integerBox = (Box<Integer>)numberBox; //Error: Cannot cast from Box<Number> to Box<Integer>
```
NOTE: To achieve the same, unbounded wildcards can be used.

**No instanceOf** - Because compiler uses type erasure, the runtime does not keep track of type parameters, so at runtime difference between Box<Integer> and Box<String> cannot be verified using instanceOf operator.

```java
... integerBox instanceof Box<Integer> ... 
```

**No Array** - Arrays of parameterized types are not allowed. Because compiler uses type erasure, the type parameter is replaced with Object and user can add any type of object to the array. And at runtime, code will not able to throw ArrayStoreException.

```java
Object[] stringBoxes = new Box<String>[]; //Error
```

**No Exception** - A generic class is not allowed to extend the Throwable class directly or indirectly. 

```java
//The generic class Box<T> may not subclass java.lang.Throwable
class Box<T> extends Exception {}
class Box1<T> extends Throwable {}
```
A method is not allowed to catch an instance of a type parameter. 
```java
... catch (T e) ...
```

**No Overload** - A class is not allowed to have two overloaded methods that can have the same signature after type erasure.
```java
...
public void print(List<String> stringList) { }  // Error
public void print(List<Integer> integerList) { }
```

---

## Additional Examples — PECS (Producer Extends, Consumer Super)

The most important rule for working with wildcards in practice:

- **`? extends T`** (upper bounded) — use when the generic is a **producer** (you read from it)
- **`? super T`** (lower bounded) — use when the generic is a **consumer** (you write into it)

```java
// PECS Example
public static <T> void copy(List<? extends T> source,   // produces T values
                             List<? super T> destination) { // consumes T values
    for (T item : source) {
        destination.add(item);
    }
}

List<Integer> ints = List.of(1, 2, 3);
List<Number> numbers = new ArrayList<>();
copy(ints, numbers);  // works: Integer extends Number

// Collections.copy() uses the same principle
Collections.copy(dest, src);  // void copy(List<? super T> dest, List<? extends T> src)
```

---

## Interview Questions — Generics

**Q1. What is the purpose of generics? What problems do they solve?**
- **Type safety at compile time**: errors caught early, no `ClassCastException` at runtime.
- **Elimination of casts**: no need to cast when reading from a collection.
- **Code reuse**: one algorithm/data structure works with any type.
```java
// Without generics (pre Java 5)
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);  // unchecked cast — could fail at runtime

// With generics
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);  // no cast — safe and clean
```

**Q2. What is type erasure? How does it affect generics at runtime?**
- During compilation, generic type information is **erased** — `List<String>` becomes `List` at bytecode level.
- At runtime, `List<String>` and `List<Integer>` are the same class (`ArrayList`).
- Implication: you cannot use `instanceof` with parameterized types, and you cannot create arrays of generic types.
```java
List<String> strList = new ArrayList<>();
List<Integer> intList = new ArrayList<>();
System.out.println(strList.getClass() == intList.getClass()); // TRUE — both are ArrayList
```

**Q3. What is the difference between `List<Object>`, `List<?>`, and a raw `List`?**
```java
List<Object> objList;  // accepts ONLY List<Object> — not List<String>!
List<?> wildcardList;  // accepts ANY parameterized List (but you can only read, not add non-null)
List rawList;          // unchecked — like pre-Java-5 (avoid — no type safety)

// Example
void processObjects(List<Object> list) { }
processObjects(new ArrayList<String>());  // COMPILE ERROR — List<String> is NOT a List<Object>

void processAny(List<?> list) { list.get(0); } // can read
processAny(new ArrayList<String>());  // OK
```

**Q4. Explain `? extends T` vs `? super T`. Give an example.**
```java
// ? extends T — you can READ T but NOT write (except null)
List<? extends Number> nums = new ArrayList<Integer>();
Number n = nums.get(0);   // OK
nums.add(42);             // COMPILE ERROR — could be List<Double>, not safe

// ? super T — you can WRITE T but reading gives Object
List<? super Integer> nums2 = new ArrayList<Number>();
nums2.add(42);            // OK — Integer is safe to add
Object obj = nums2.get(0); // only Object returned — type lost

// Mnemonic: PECS — Producer Extends, Consumer Super
```

**Q5. Can generics work with primitive types?**
- No. Generics only work with reference types. Use wrapper classes instead.
- Java auto-boxes: `List<Integer>` accepts `int` via autoboxing, but there's overhead.
- Java 21+ preview (Valhalla project) may eventually support `List<int>`.

**Q6. What is a generic method? How is it different from a method in a generic class?**
```java
// Generic class — T defined at class level
class Box<T> {
    T value;
    T get() { return value; }  // uses class-level T
}

// Generic method — T defined at method level (independent of class)
class Util {
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }
}

// Call:
int m = Util.max(3, 7);        // T inferred as Integer
String s = Util.max("a", "z"); // T inferred as String
```

**Q7. Why can't you do `new T()` or `new T[]` in a generic method?**
- Due to type erasure, `T` is replaced with `Object` at runtime — the JVM doesn't know the concrete type.
- Workaround: pass a `Class<T>` parameter and use reflection.
```java
public <T> T create(Class<T> clazz) throws Exception {
    return clazz.getDeclaredConstructor().newInstance();
}
String s = create(String.class);
```

**Q8. What is a bounded wildcard? Give a real use case.**
```java
// Upper bounded — process any list of Numbers or subtypes
public double sum(List<? extends Number> numbers) {
    return numbers.stream().mapToDouble(Number::doubleValue).sum();
}
sum(List.of(1, 2, 3));       // List<Integer> — OK
sum(List.of(1.5, 2.5));      // List<Double> — OK

// Lower bounded — add Numbers to a list that accepts Numbers or supertypes
public void fillWithNumbers(List<? super Integer> list) {
    for (int i = 0; i < 10; i++) list.add(i);
}
fillWithNumbers(new ArrayList<Integer>());  // OK
fillWithNumbers(new ArrayList<Number>());   // OK
fillWithNumbers(new ArrayList<Object>());   // OK
```

**Q9. What is the diamond operator `<>` and when was it introduced?**
- Introduced in **Java 7**. Allows the compiler to infer the type parameter from context.
```java
// Before Java 7 — redundant type on the right
Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

// Java 7+ — diamond infers from left side
Map<String, List<Integer>> map = new HashMap<>();
```

**Q10. Can you have a generic static field? Why not?**
- No. A static field is shared across all instances of the class. Since different instances can have different type parameters, a generic static field would be ambiguous.
```java
class Container<T> {
    static T value;  // COMPILE ERROR — T is per-instance, static is per-class
    T instanceValue; // OK
}
```
