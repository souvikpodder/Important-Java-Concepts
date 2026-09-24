# Java Features and Program Execution

Java is a **programming language** and a **platform**. 

**Platform:** Any hardware or software environment in which a program runs, is known as a platform. Since Java has its own runtime environment (JRE) and Application Programming Interface (API), it is called platform.

## Features of Java :

![java-features](https://user-images.githubusercontent.com/2780145/34343690-2fd47db0-e9ff-11e7-9630-75423dda7eaa.png)

- **Simple:**
  - User friendly syntax based on C++
  - It has Automatic Garbage Collection
  - It has Rich set of APIs 
  - Removed confusing features - explicit pointers, operator overloading, multiple inheritance, etc
 
- **Object-Oriented:**
  - In Java, we organize the software as a combination of different types of objects that incorporates both data and behavior.
  - Based on the concept of Objects, Class, Inheritance, Polymorphism, Abstraction, Encapsulation
  
- **Platform Independent:**
  - Java provides software-based platform. It has two components:
    - JRE (Runtime Environment)
    - API (Application Programming Interface)
  - Java code is compiled by the compiler and converted into bytecode. This bytecode is a platform-independent. Can run on many platforms - Windows, Linux, Mac, etc.
  
- **Secured:**
  - **No explicit pointer**
  - **JVM -** java Programs run inside virtual machine sandbox 
  - **Classloader -** adds security by separating the package for the classes of the local file system from those that are imported from network sources.
  - **Bytecode Verifier -** checks the code fragments for illegal code that can violate access right to objects.
  - **Security Manager -** determines what resources a class can access such as reading and writing to the local disk.
  - **More -** developers can add extra security through SSL, JAAS, Cryptography etc.
  
- **Robust:**
  - **Good memory management -** automatic garbage collection.
  - **No pointers -** increases security. 
  - **Exception handling -** increases robustness against errors.
  - **Strongly typed -** every variable must be declared with a data type.
  - **Statically typed -** type checking of variables is performed at compile time.
  
- **Architecture-Neutral:**
  - There is no implementation dependent features. e.g. size of primitive types is fixed.
  
- **Portable:**
  - Write Once and Run Anywhere.
   
- **Interpreted:**
  - Java is compiled to bytecodes, which are interpreted by a Java run-time environment.
  - The interpreter reads bytecode stream then execute the instructions.
  
- **High-Performance:**
  - **Uses ByteCode -** Java is faster than traditional interpreted languages since byte code is "close" to native code. 
  - **Just-In-Time (JIT) -** it is designed to support JIT compilers, which dynamically compile bytecodes to machine code. 
  - **Garbage collector -** collect the unused memory space and improve the performance of the application.
  - NOTE: Java is still slower than a compiled language like C/C++.
  
- **Distributed:**
  - We can create distributed applications in java. RMI and EJB are used for creating distributed applications.
  - We may access files by calling the methods from any machine on the internet.
  
- **Multi-threaded:**
  - A thread is like a separate program, executing concurrently. We can write Java programs that deal with many tasks at once by defining multiple threads.
  - Threads share the heap, but each thread has its own execution state and logical call stack, so creating threads still consumes memory.
  - Threads are important for multi-media, Web applications etc.
  
- **Dynamic:**
  - **Dynamic Compilation (JIT) -** Implementations to gain performance during program execution. The machine code emitted by a dynamic compiler is constructed and optimized at program runtime, the use of dynamic compilation enables optimizations for efficiency.
  - **Load on Demand -** Loads in classes as they are needed, even from across the network.
  - **Dynamic memory allocation -** All Java objects are dynamically allocated. 
  - **Dynamic Polymorphism -** Compiler doesn’t know which method to be called in advance. JVM decides which method to called at run time.

## Java Program Execution Process :

![java-execution-process](https://user-images.githubusercontent.com/2780145/34343683-d3aea7e0-e9fe-11e7-866d-26a8857e04c9.png)

## JDK - JRE - JVM - JIT :

![jdk-jre-jvm-jit](https://user-images.githubusercontent.com/2780145/34342877-771d2796-e9e4-11e7-9d18-98ed672a4b53.png)

**Java Development Kit (JDK):** It is a collection of development tools including JRE.

**Java Runtime Environment (JRE):** It contains set of libraries and the JVM.

**Java Virtual Machine (JVM):** It is an abstract machine. It is a specification that provides runtime environment in which java bytecode can be executed. The JVM performs following main tasks: Loads code, Verifies code, Executes code and Provides runtime environment.

**NOTE -** JVMs are available for many hardware and software platforms. JVM, JRE and JDK are platform dependent because configuration of each OS differs. But, Java is platform independent.

## Internal Architecture of JVM :

![jvm-architecture](https://user-images.githubusercontent.com/2780145/34343635-f405f2f2-e9fc-11e7-9628-28992defdd0b.png)

JVM (Java Virtual Machine) has various sub components internally. You can see the most important ones in the above diagram.

- **Class loader sub system:** JVM's class loader sub system performs 3 tasks
  - It loads .class file into memory.
  - It verifies byte code instructions.
  - It allots memory required for the program.
  
- **Run time data area:** This is the memory resource used by JVM and it is divided into 5 parts
  - **Class (Method) area:** Stores constant pool, field and method data, the code for methods.
  - **Heap:** Objects are allocated on the heap.
  - **Java stacks:** Java stacks are the places where the Java methods are executed. A Java stack contains frames. It holds local variables and partial results, and plays a part in method invocation and return. On each frame, a separate method is executed. Each thread has a private JVM stack, created at the same time as thread. A new frame is created each time a method is invoked. A frame is destroyed when its method invocation completes.
  - **Program counter registers:** PC (program counter) register. It contains the address of the JVM instruction currently being executed.
  - **Native method stacks:** Are places where native methods (eg. C language programs, etc) are executed.
  
-  **Native method interface:** Native method interface is a program that connects native methods libraries (C header files) with JVM for executing native methods.

- **Native method library:** Holds the native libraries information.

- **Execution engine:** 
  - **Just-In-Time(JIT) compiler:** It is used to improve the performance. It coverts byte code into machine code. JIT compiles parts of the byte code that have similar functionality at the same time, and hence reduces the amount of time needed for compilation.Here the term ?compiler? refers to a translator from the instruction set of a Java virtual machine (JVM) to the instruction set of a specific CPU.
  - **Interpreter:** Read bytecode stream then execute the instructions.
  - **Virtual processor** 
  - **NOTE -** JVM uses optimization technique to decide which part to be interpreted and which part to be used with JIT compiler.

## Stack vs Heap: Data Structures and Internal Working

The stack organizes active method calls, while the heap stores objects and arrays whose lifetimes can extend beyond a method call. A method stack follows **last in, first out (LIFO)**; the memory heap is a managed allocation area, not the binary-heap data structure used by a priority queue.

### 1. Stack Frames and Method Calls

Each thread has its own logical JVM stack. Every method invocation creates a frame containing local-variable slots, an operand stack, and information supporting execution and return. Frames are removed when invocations complete normally or unwind because of an exception. These are logical runtime structures; the specification does not require contiguous physical storage. See [JVM stacks and frames](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.6).

| Frame component | Purpose |
|---|---|
| Local-variable slots | Hold parameters and local values, including object references |
| Operand stack | Holds intermediate values while bytecode evaluates expressions and invokes methods |
| Runtime bookkeeping | Supports linking, returning to the caller, and exception handling |

Consider these methods inside a class:

```java
static void mainTask() {
    int result = add(10, 20);
    System.out.println(result);
}

static int add(int a, int b) {
    int sum = a + b;
    return sum;
}
```

1. **Enter the caller.** Invoking `mainTask()` creates its frame, with a slot for `result`.
2. **Call the nested method.** Invoking `add(10, 20)` creates another frame, whose parameters hold `a = 10` and `b = 20`. The caller waits underneath it.
3. **Calculate the result.** The callee evaluates the addition using its operand stack and stores 30 in `sum`.
4. **Return to the caller.** The `add()` frame is removed and its result is passed back. The caller stores 30 in `result` and continues.

```text
TOP: currently executing
+---------------------------+
| add() frame               |
| a = 10, b = 20, sum = 30   |
+---------------------------+
| mainTask() frame          |
| waiting for add()         |
+---------------------------+
BOTTOM
```

This is LIFO because the most recently called method completes before its caller resumes normally. Recursion creates a separate frame for every invocation, even though each invocation executes the same method's code.

### 2. The Operand Stack Inside a Frame

The thread's method stack tracks calls; a frame's operand stack evaluates expressions within one call. For `int sum = a + b`, the relevant bytecode operations behave conceptually as follows:

| Operation | Operand stack, bottom to top |
|---|---|
| Load `a` | `[10]` |
| Load `b` | `[10, 20]` |
| Add the top two values | `[30]` |
| Store the result in `sum` | `[]` |

Local-variable slots support indexed access; they are not themselves a LIFO collection. The operand stack holds temporary results, while local slots retain values that later instructions can reload. The JIT can translate this model into machine instructions using registers instead of literally pushing every intermediate value into memory.

### 3. The Memory Heap Is Not a Binary Heap

Heap objects have independent lifetimes. An object created first can become unreachable before a newer object, so reclaiming objects cannot simply follow reverse allocation order.

```java
Person first = new Person(25);
Person second = new Person(30);
first = null;
// The first object may now be unreachable while second is still needed.
```

The two meanings of heap should be kept separate:

| Term | Structure and purpose |
|---|---|
| JVM memory heap | Managed memory used to allocate objects and arrays |
| Binary heap | A complete binary tree, commonly stored in an array, maintaining a min/max ordering rule |

Java's `PriorityQueue` uses a binary heap, and its backing array is itself allocated in the JVM memory heap. The priority-queue structure does not describe how the JVM arranges all application objects. See the separate [heap data-structure notes](tree_graph.md#12-heap).

### 4. How Object Allocation Works

For `Person person = new Person(25)`, the JVM conceptually obtains storage, initializes the object, and supplies a reference. Storage includes implementation-specific object metadata, instance fields, and alignment. Fields receive default values before instance initializers and constructor logic establish the intended state.

Common JVM implementations can allocate small objects from a **thread-local allocation buffer (TLAB)** within the shared heap. When sufficient space exists, allocation mainly advances a pointer:

```text
Before:
[ Existing objects ][              Free space              ]
                    ^ allocation pointer

After:
[ Existing objects ][ New object ][       Free space       ]
                                  ^ allocation pointer
```

This avoids coordinating with other threads for every small allocation. Refilling a buffer, handling a large object, or running out of available space takes another path. A TLAB is an allocation optimization, not a rule that its objects must remain private to the allocating thread.

The exact allocator, object layout, and collection strategy depend on the JVM. Do not describe the entire heap as one fixed array, tree, or linked list with a universal allocation algorithm.

### 5. References and Objects Have Different Lifetimes

This complete example shows an object surviving the method that creates it:

```java
public class MemoryExample {
    static class Person {
        int age;

        Person(int age) {
            this.age = age;
        }
    }

    static Person createPerson() {
        Person p = new Person(25);
        return p;
    }

    public static void main(String[] args) {
        Person person = createPerson();
        System.out.println(person.age); // 25

        Person alias = person;
        alias.age = 30;
        System.out.println(person.age); // 30
    }
}
```

During creation, the local reference points from the callee's frame to the object:

```text
THREAD STACK                        HEAP
+----------------------+
| createPerson()       |
| p -------------------+----------> Person { age = 25 }
+----------------------+
| main() waiting       |
+----------------------+
```

After return, the caller holds the reference and the callee's frame is gone:

```text
THREAD STACK                        HEAP
+----------------------+
| main()               |
| person --------------+----------> Person { age = 25 }
+----------------------+
```

Returning the reference does not copy the object or move it from stack to heap. Assigning `alias = person` also copies only the reference, so modifying `alias.age` changes the same object observed through `person`.

Java passes arguments by value, including reference values. A callee can mutate an object through a copied reference, but assigning that parameter to a different object does not reassign the caller's variable.

### 6. Garbage Collection and Reachability

A tracing garbage collector follows references from roots to determine which objects remain reachable. Examples of roots or root paths include live references associated with executing threads and static references of live classes. Once no such path reaches an object, it is eligible for reclamation.

```text
GC root --> Object A --> Object B      Reachable: retained

            Object C <--> Object D    Unreachable: collectible
```

C and D can be collected even though they reference each other, because that cycle has no path from a root. Counting references alone would not explain this behavior.

Setting one variable to `null` does not immediately free its object: another reference may still reach it, and collection is scheduled separately. Likewise, Java can have memory leaks when a long-lived collection keeps retaining objects the application no longer needs. Those objects remain reachable, so the collector cannot infer that they should be discarded.

Some collectors organize memory into generations or regions and move surviving objects to reclaim contiguous space. The layout and algorithm vary; generation names and collection phases are not universal properties of every JVM heap.

### 7. Why Stack and Heap Have Different Cleanup Costs

A frame has a structured lifetime tied to one invocation, so it can be discarded when that invocation finishes. An object may be referenced by many methods, objects, or threads, so its reclaimability depends on reachability instead.

| Aspect | Stack | Heap |
|---|---|---|
| Logical organization | LIFO method frames | Managed allocation areas containing objects |
| Lifetime | Method invocation | Object reachability |
| Reclamation | Return or exception unwinding removes frames | Garbage collection reclaims eligible objects |
| Ownership | Each thread has its own call state | Objects can be shared across threads |
| Typical cost | Frame management is usually cheap | Allocation can be cheap; garbage collection is separate work |

Avoid saying that stack access is always fast and heap access is always slow. A heap allocation can be a pointer increment, and reading a field through a reference does not search the whole heap. Locality, indirection, optimization, and collection work determine practical performance.

Sharing the heap also does not make object access automatically thread-safe. Two threads can hold references in their separate frames to the same mutable object, so concurrent updates may still require synchronization.

### 8. Stack Overflow vs Heap Exhaustion

Unbounded recursion keeps adding active frames because no invocation returns:

```java
static void recurse() {
    recurse();
}
```

This typically ends with `StackOverflowError` when the permitted stack depth is exceeded. Very deep finite recursion can do the same; an infinite loop without recursive calls does not inherently grow the call stack.

Heap exhaustion can occur when too many objects remain reachable:

```java
// Illustrative failure pattern; requires java.util.List and ArrayList.
List<byte[]> retained = new ArrayList<>();
while (true) {
    retained.add(new byte[1_000_000]);
}
```

The collection retains every array, preventing their reclamation. Eventually another allocation can fail with `OutOfMemoryError`. These are failure demonstrations, not examples to execute as part of normal practice. Also, OutOfMemoryError can indicate resource shortages other than Java heap exhaustion.

### 9. Where Do Primitives and References Live?

```java
class Example {
    int age = 25;
    int[] scores = new int[3];

    void calculate() {
        int count = 5;
        int[] localScores = new int[2];
    }
}
```

| Value | Conceptual location | Reason |
|---|---|---|
| Local `count` | Method frame | It belongs to this invocation |
| Local reference `localScores` | Method frame | It is a local variable |
| Array referenced by `localScores` | Heap | Arrays are objects |
| Instance field `age` | Inside the `Example` object | It belongs to the instance |
| Instance reference `scores` | Inside the `Example` object | It is an instance field |
| Array referenced by `scores` | Heap, as a separate object | A reference field does not embed the array itself |

Therefore, "primitives live on the stack" is incomplete: primitive fields and primitive array elements are part of heap objects. A reference can also be stored either in a method frame or as a field of another object.

### 10. Logical Model vs Physical Implementation

These diagrams explain program behavior, not a mandatory physical layout. Optimized execution can keep values in registers or eliminate an allocation through escape analysis and scalar replacement when observable behavior stays the same. An eliminated object need not exist as a complete object on either stack or heap.

Virtual threads make this distinction especially visible: OpenJDK stores their suspended stack state in garbage-collected heap objects called stack chunks. Each virtual thread still has its own logical call stack, even though its backing storage differs from a conventional platform thread's stack. See [JEP 444: memory use and garbage collection](https://openjdk.org/jeps/444#Memory-use-and-interaction-with-garbage-collection).

### Interview Answer to Remember

The stack manages active method invocations in LIFO order, with local-variable slots and an operand stack in each frame. The heap manages objects and arrays with independent lifetimes, reclaiming unreachable objects through garbage collection. References connect the two, and implementation optimizations can change physical storage without changing that logical behavior.
