# Asynchronous vs. Multithreading in Java (with the Java 21 Shift)

When building concurrent Java applications, developers often ask: **"Should I use Asynchronous Programming (like `CompletableFuture`) or Multithreading (like `ExecutorService`)?"** 

These two concepts are highly related but represent different dimensions of execution: **how a task is structured and handled by the caller** versus **who actually executes the work**. 

With **Java 21's Virtual Threads (Project Loom)**, the relationship between these two paradigms has shifted fundamentally.

---

## 1. Core Definitions

```
                      ┌────────────────────────────────────────┐
                      │              CONCURRENCY               │
                      └───────────────────┬────────────────────┘
                                          │
                  ┌───────────────────────┴───────────────────────┐
                  ▼                                               ▼
     ┌─────────────────────────┐                     ┌─────────────────────────┐
     │      MULTITHREADING     │                     │      ASYNCHRONOUS       │
     │    (Worker-Centric)     │                     │     (Task-Centric)      │
     ├─────────────────────────┤                     ├─────────────────────────┤
     │ Focuses on WHO executes  │                     │ Focuses on caller NOT   │
     │ the code (threads/CPUs) │                     │ blocking during waiting │
     └─────────────────────────┘                     └─────────────────────────┘
```

### Multithreading (Worker-Centric)
*   **Definition:** Splitting a program into multiple execution paths (threads) that run concurrently (often in parallel on multi-core CPUs).
*   **Core Question:** *How many workers do I need to execute this code?*
*   **Java APIs:** `Thread`, `Runnable`, `ExecutorService`.

### Asynchronous Programming (Task-Centric)
*   **Definition:** Executing a task in a way that allows the calling thread to continue working immediately without waiting (blocking) for the task to finish. 
*   **Core Question:** *How do I avoid waiting around (blocking) for this I/O or background task?*
*   **Java APIs:** `CompletableFuture`, `Callable/Future`, Spring WebFlux (Reactive).

---

## 2. Conceptual Differences

| Feature | Threading (Multithreading) | Asynchronous Programming |
| :--- | :--- | :--- |
| **Focus** | Parallel execution of CPU instructions. | Non-blocking execution of long-running tasks. |
| **Worker Allocation**| Assigns a physical worker thread to each task. | Offloads tasks to wait handlers, releasing the worker thread. |
| **Typical Target** | CPU-bound computations (e.g., sorting, image processing). | I/O-bound wait times (e.g., calling DBs, microservices, files). |
| **Complexity** | Thread safety, race conditions, deadlocks, context switching. | Callback chains, error propagation, "Callback Hell". |
| **Hardware Tie** | Scales with CPU core counts. | Can achieve massive concurrency on a single core (e.g., Node.js). |

---

## 3. The Analogy: Preparing Dinner

Imagine preparing a dinner of pasta, a salad, and a baked cake:

*   **Synchronous / Single-Threaded:** You boil the pasta, standing by the stove for 10 minutes doing nothing else. Then you bake the cake, staring at the oven. Finally, you chop the salad.
*   **Threading (Multithreading):** You **hire three chefs** (Platform Threads). Chef 1 boils the pasta. Chef 2 bakes the cake. Chef 3 chops the salad. They work in parallel. 
    *   *Downside:* You need to pay three chefs (uses ~1MB of memory per OS thread), and they might crash into each other in the kitchen (race conditions).
*   **Asynchronous (Event Loop):** You are **one chef** with timers. You put the pasta to boil and set a timer. Instead of standing there, you turn around and chop the salad. When the timer rings (callback), you drain the pasta.
    *   *Downside:* You have to coordinate timers and callbacks, which can make your recipe complex to write down.

---

## 4. Code Examples (The Evolution)

### Phase 1: Traditional Threading (Synchronous Blocking)
We distribute work to threads, but we block the main thread waiting for results.

```java
import java.util.concurrent.*;

public class TraditionalThreading {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Submit task (runs on thread pool)
        Future<String> dbCall = executor.submit(() -> {
            Thread.sleep(2000); // Simulate blocking DB call
            return "User Profile";
        });

        System.out.println("Main thread is doing some preparation...");

        // ⚠️ BLOCKS the calling (main) thread until the DB returns
        String result = dbCall.get(); 
        
        System.out.println("Result received: " + result);
        executor.shutdown();
    }
}
```

### Phase 2: Asynchronous Chaining (CompletableFuture - Java 8)
We avoid blocking by chaining actions. The callback runs asynchronously when the data becomes available.

```java
import java.util.concurrent.CompletableFuture;

public class AsyncCompletableFuture {
    public static void main(String[] args) {
        System.out.println("Main thread started: " + Thread.currentThread().getName());

        CompletableFuture.supplyAsync(() -> {
            // Simulate slow network call
            sleep(2000); 
            return "Raw API Data";
        })
        .thenApply(data -> data + " -> Cleaned")  // Transformation (non-blocking callback)
        .thenAccept(result -> {
            // Consumes the result
            System.out.println("Processed in thread " + Thread.currentThread().getName() + ": " + result);
        });

        System.out.println("Main thread is NOT blocked! Continues execution...");
        
        // Wait just to prevent JVM exit during demo
        sleep(3000); 
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
```

---

## 5. The Java 21 Paradigm Shift: Virtual Threads

Prior to Java 21, Java used **Platform Threads**. These mapped **1:1 to Operating System (OS) threads**. 
*   Because OS threads are expensive (~1MB stack size, slow context switching), web servers (like Tomcat) had a limit on concurrent requests (usually ~200 threads).
*   To scale, developers had to use complex reactive programming frameworks (Spring WebFlux, RxJava, Project Reactor) to achieve async non-blocking behavior.

### What is a Virtual Thread?
Virtual Threads (Project Loom) are **lightweight threads** managed by the **JVM**, not the OS. They map **M:N** to OS carrier threads. 

```
  Virtual Threads (Millions)       JVM Carrier Threads (Few)        OS Kernel Threads
      [ VT 1 ] ──┐
      [ VT 2 ] ──┼───────────────►     [ Carrier 1 ]     ────────►   [ OS Thread 1 ]
      [ VT 3 ] ──┘
      [ VT 4 ] ──┐
      [ VT 5 ] ──┼───────────────►     [ Carrier 2 ]     ────────►   [ OS Thread 2 ]
      [ VT 6 ] ──┘
```

*   **Extremely cheap:** You can create **millions** of virtual threads without running out of memory.
*   **Automatic Unmounting:** If a virtual thread blocks on I/O (e.g., calling a database or running `Thread.sleep()`), the JVM **unmounts** it from the OS carrier thread, parking it until the I/O event finishes. The carrier thread is immediately freed up to execute other virtual threads.
*   **Synchronous Code Style:** You no longer need to write complex asynchronous callbacks (`CompletableFuture`, `.flatMap()`, etc.). You write standard, readable, synchronous/blocking code, and the JVM makes it performant under the hood.

### Virtual Threads Code Example
This behaves asynchronously (non-blocking to the OS) but is written synchronously!

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadsShift {
    public static void main(String[] args) {
        // Creates a new executor that runs every task in a new Virtual Thread
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            for (int i = 0; i < 10_000; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    // This looks like standard blocking code
                    // But it is executing inside a virtual thread!
                    System.out.println("Task " + taskId + " running on: " + Thread.currentThread());
                    
                    try {
                        // Under the hood, the JVM unmounts the carrier thread here
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    System.out.println("Task " + taskId + " finished.");
                    return taskId;
                });
            }
            // Executor automatically closes and waits for all tasks to finish (due to AutoCloseable)
        }
    }
}
```

---

## 6. Sizing up Threading vs. Async Today

| Scenario | Pre-Java 21 Approach | Post-Java 21 Approach |
| :--- | :--- | :--- |
| **CPU-Bound Task**<br>*(e.g., video compression, encryption)* | Traditional thread pools sized to match CPU cores (`Executors.newFixedThreadPool(cores)`). | **Same.** Virtual threads do NOT speed up CPU tasks (since the hardware cores remain the limit). |
| **I/O-Bound Task**<br>*(e.g., reading files, fetching databases)* | `CompletableFuture` or WebFlux (Reactive programming) to free up blocking threads. | **Virtual Threads.** Write standard blocking Java code. It is easier to write, debug, and trace. |

> [!TIP]
> With Java 21, the rule of thumb is: **"Write simple, readable synchronous blocking code, and run it on Virtual Threads."** Avoid using reactive frameworks or complex `CompletableFuture` nesting unless you need advanced scheduling or asynchronous event streaming features.

---

## Related Notes
*   [Async Programming in Java](java_async_programming.md)
*   [Multithreading in Java](java_multithreading.md)
*   [Synchronization in Java](java_thread_synchronization.md)
*   [Java 21 Features](java_21_features.md)
