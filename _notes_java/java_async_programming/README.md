# Async Programming in Java — Complete Notes

## Table of Contents
- [What is Asynchronous Programming?](#what-is-asynchronous-programming)
- [Sync vs Async — The Key Difference](#sync-vs-async--the-key-difference)
- [Why Do We Need Async?](#why-do-we-need-async)
- [Evolution of Async in Java](#evolution-of-async-in-java)
- [1. Thread & Runnable (Basic)](#1-thread--runnable-basic)
- [2. Callable & Future](#2-callable--future)
- [3. ExecutorService (Thread Pool)](#3-executorservice-thread-pool)
- [4. CompletableFuture (Modern Async)](#4-completablefuture-modern-async)
  - [Creating CompletableFutures](#creating-completablefutures)
  - [Chaining — thenApply, thenAccept, thenRun](#chaining--thenapply-thenaccept-thenrun)
  - [Combining — thenCombine, thenCompose, allOf, anyOf](#combining--thencombine-thencompose-allof-anyof)
  - [Error Handling — exceptionally, handle, whenComplete](#error-handling--exceptionally-handle-whencomplete)
  - [Running on Different Thread Pools](#running-on-different-thread-pools)
- [5. Virtual Threads (Java 21+)](#5-virtual-threads-java-21)
- [Common Patterns & Real-World Examples](#common-patterns--real-world-examples)
- [Pitfalls & Best Practices](#pitfalls--best-practices)
- [Quick Reference Cheat Sheet](#quick-reference-cheat-sheet)

---

## What is Asynchronous Programming?

**Asynchronous (Async) Programming** means executing tasks **without waiting** for each one to finish before starting the next. The calling thread is **not blocked** — it can continue doing other work while the async task runs in the background.

**Analogy:**
- **Synchronous** = You order food at a counter and **stand there waiting** until your food is ready. You can't do anything else.
- **Asynchronous** = You order food, get a **buzzer/token**, and go sit down or do other things. The buzzer notifies you when your food is ready.

---

## Sync vs Async — The Key Difference

```
SYNCHRONOUS (Blocking):
────────────────────────────────────────────
Thread:  [Task A ██████████] → [Task B ██████████] → [Task C ██████████]
Time:    0s                 5s                     10s                   15s
Total:   15 seconds (sequential)


ASYNCHRONOUS (Non-Blocking):
────────────────────────────────────────────
Thread 1:  [Task A ██████████]
Thread 2:  [Task B ██████████]
Thread 3:  [Task C ██████████]
Time:      0s                5s
Total:     5 seconds (parallel)
```

```java
// SYNCHRONOUS — Each call blocks the thread until it completes
public void syncExample() {
    String userData = fetchUserFromDB();       // Waits 2 seconds
    String orderData = fetchOrdersFromAPI();   // Waits 3 seconds
    String recommendations = computeRecs();    // Waits 1 second
    // Total: 6 seconds (2 + 3 + 1)
}

// ASYNCHRONOUS — All tasks start at the same time
public void asyncExample() {
    CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> fetchUserFromDB());
    CompletableFuture<String> orderFuture = CompletableFuture.supplyAsync(() -> fetchOrdersFromAPI());
    CompletableFuture<String> recsFuture = CompletableFuture.supplyAsync(() -> computeRecs());

    CompletableFuture.allOf(userFuture, orderFuture, recsFuture).join();
    // Total: ~3 seconds (max of all three, running in parallel)
}
```

---

## Why Do We Need Async?

| Problem                  | How Async Solves It                                    |
|--------------------------|--------------------------------------------------------|
| **Slow I/O operations**  | DB calls, API calls, file reads don't block the main thread |
| **Better performance**   | Multiple tasks run in parallel, reducing total time     |
| **Responsive UI/Server** | Server can handle more requests instead of waiting idle |
| **Resource efficiency**  | Threads aren't wasted sitting idle during I/O waits     |
| **Scalability**          | Handle thousands of concurrent requests with fewer threads |

### When to Use Async:
- ✅ Making HTTP API calls to external services
- ✅ Database queries (especially slow ones)
- ✅ File I/O (reading/writing large files)
- ✅ Sending emails, push notifications
- ✅ Processing multiple independent tasks
- ✅ Long-running computations

### When NOT to Use Async:
- ❌ Simple, fast in-memory operations (overhead of async > benefit)
- ❌ Tasks that MUST be sequential (Task B depends on Task A's result)
- ❌ When code readability matters more than speed (async adds complexity)

---

## Evolution of Async in Java

```
Java 1.0 (1996)     → Thread, Runnable           (manual, low-level)
Java 5   (2004)     → ExecutorService, Future     (thread pools, basic async)
Java 8   (2014)     → CompletableFuture           (modern async with chaining)
Java 21  (2023)     → Virtual Threads             (lightweight threads, millions of them)
```

---

## 1. Thread & Runnable (Basic)

The most basic way to run code asynchronously — create a separate thread.

### Using `Runnable` (No return value)

```java
public class BasicAsyncExample {

    public static void main(String[] args) {
        System.out.println("Main thread started: " + Thread.currentThread().getName());

        // Create a new thread using Runnable
        Thread thread = new Thread(() -> {
            System.out.println("Async task running on: " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000); // Simulate work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Async task completed!");
        });

        thread.start();  // Starts the thread (non-blocking)

        System.out.println("Main thread continues doing other work...");

        // Wait for the async thread to finish (optional)
        try {
            thread.join();  // Blocks main thread until 'thread' finishes
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All done!");
    }
}
```

**Output:**
```
Main thread started: main
Main thread continues doing other work...
Async task running on: Thread-0
Async task completed!
All done!
```

### Limitations of Thread/Runnable

| Problem | Explanation |
|---------|-------------|
| **No return value** | `Runnable.run()` returns `void` — you can't get a result from the async task |
| **No exception handling** | Can't throw checked exceptions from `run()` |
| **Thread management** | Creating a new `Thread` for every task is expensive (OS-level resource) |
| **No thread reuse** | Each thread is created and destroyed — highly wasteful |
| **No chaining** | Can't easily say "when Task A finishes, run Task B with its result" |

> **Verdict:** Don't use raw Threads in production. Use `ExecutorService` or `CompletableFuture` instead.

---

## 2. Callable & Future

`Callable` solves the biggest problem of `Runnable` — it can **return a value** and **throw exceptions**.

```java
import java.util.concurrent.*;

public class CallableFutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // ExecutorService manages a pool of threads
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Callable — like Runnable but RETURNS a value
        Callable<String> task = () -> {
            Thread.sleep(2000); // Simulate database call
            return "User: John Doe";
        };

        // Submit returns a Future — a placeholder for the result
        Future<String> future = executor.submit(task);

        System.out.println("Main thread is not blocked, doing other work...");

        // Get the result (BLOCKS until the result is available)
        String result = future.get();  // ⚠️ This blocks!
        System.out.println("Result: " + result);

        executor.shutdown();
    }
}
```

### Runnable vs Callable

| Feature             | `Runnable`                     | `Callable<V>`                    |
|---------------------|--------------------------------|----------------------------------|
| Method              | `void run()`                   | `V call() throws Exception`     |
| Return value        | ❌ None                        | ✅ Returns a result              |
| Checked exceptions  | ❌ Cannot throw                | ✅ Can throw                     |
| Used with           | `Thread`, `ExecutorService`    | `ExecutorService` only           |
| Result holder       | None                           | `Future<V>`                      |

### Future — The Result Placeholder

```java
Future<String> future = executor.submit(callable);

// Key methods:
future.get();                    // Blocks until result is ready
future.get(5, TimeUnit.SECONDS); // Blocks with timeout
future.isDone();                 // Check if completed (non-blocking)
future.isCancelled();            // Check if cancelled
future.cancel(true);             // Attempt to cancel the task
```

### Limitations of Future

| Problem | Explanation |
|---------|-------------|
| **Blocking `.get()`** | `future.get()` **blocks** the calling thread — defeats the purpose of async! |
| **No chaining** | Can't say "when this completes, do that". Must manually poll or block. |
| **No combining** | Can't easily combine results of multiple Futures |
| **No exception chaining** | Exception handling is clumsy — wrapped in `ExecutionException` |
| **No callbacks** | No way to say "call this function when the result is ready" |

> **Verdict:** `Future` was a step forward, but `CompletableFuture` (Java 8) fixes ALL these problems. Use `CompletableFuture` instead.

---

## 3. ExecutorService (Thread Pool)

Instead of creating a new thread for every task, a **thread pool** reuses a fixed set of threads.

### Why Thread Pools?

```
WITHOUT Thread Pool (Bad):
──────────────────────────
Request 1 → Create Thread 1 → Execute → Destroy Thread
Request 2 → Create Thread 2 → Execute → Destroy Thread
Request 3 → Create Thread 3 → Execute → Destroy Thread
(Creating/destroying threads is EXPENSIVE — ~1MB memory each!)

WITH Thread Pool (Good):
──────────────────────────
Pool: [Thread-1] [Thread-2] [Thread-3]
Request 1 → Thread-1 picks it up → Done → Thread-1 waits for next task
Request 2 → Thread-2 picks it up → Done → Thread-2 waits for next task
Request 3 → Thread-3 picks it up → Done → Thread-3 waits for next task
Request 4 → Thread-1 picks it up (reused!) → ...
(Threads are REUSED — no creation/destruction overhead!)
```

### Types of Thread Pools

```java
import java.util.concurrent.*;

// 1. Fixed Thread Pool — Fixed number of threads
// Best for: Known, steady workloads (e.g., processing N items)
ExecutorService fixedPool = Executors.newFixedThreadPool(4);

// 2. Cached Thread Pool — Creates threads as needed, reuses idle ones
// Best for: Many short-lived tasks with variable load
// ⚠️ WARNING: Can create unlimited threads if tasks pile up!
ExecutorService cachedPool = Executors.newCachedThreadPool();

// 3. Single Thread Executor — Only 1 thread, tasks executed sequentially
// Best for: Tasks that must NOT run in parallel (sequential order guaranteed)
ExecutorService singlePool = Executors.newSingleThreadExecutor();

// 4. Scheduled Thread Pool — Schedule tasks to run after a delay or periodically
// Best for: Cron-like scheduled tasks
ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);
```

### ExecutorService Example — Running Multiple Tasks

```java
public class ThreadPoolExample {

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit multiple tasks
        Future<String> user = executor.submit(() -> {
            Thread.sleep(2000);
            return "User: John";
        });

        Future<String> orders = executor.submit(() -> {
            Thread.sleep(3000);
            return "Orders: [Laptop, Phone]";
        });

        Future<String> recommendations = executor.submit(() -> {
            Thread.sleep(1000);
            return "Recommendations: [Tablet, Headphones]";
        });

        // All 3 tasks run IN PARALLEL on 3 threads
        // Total time: ~3 seconds (not 6 seconds)
        System.out.println(user.get());
        System.out.println(orders.get());
        System.out.println(recommendations.get());

        // IMPORTANT: Always shut down the executor!
        executor.shutdown();
    }
}
```

### Scheduled Executor — Run Tasks on a Schedule

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

// Run once after 5 seconds
scheduler.schedule(() -> System.out.println("Delayed task!"), 5, TimeUnit.SECONDS);

// Run every 10 seconds (starts after 2-second initial delay)
scheduler.scheduleAtFixedRate(
    () -> System.out.println("Periodic task at: " + LocalTime.now()),
    2,     // initial delay
    10,    // period
    TimeUnit.SECONDS
);

// Run with 5-second delay BETWEEN the end of one execution and start of next
scheduler.scheduleWithFixedDelay(
    () -> System.out.println("Fixed delay task"),
    0,     // initial delay
    5,     // delay between executions
    TimeUnit.SECONDS
);
```

### ExecutorService Shutdown Methods

| Method | Behavior |
|--------|----------|
| `shutdown()` | No new tasks accepted. Already submitted tasks finish executing. |
| `shutdownNow()` | Attempts to stop all running tasks immediately. Returns list of waiting tasks. |
| `awaitTermination(timeout, unit)` | Blocks until all tasks finish, or timeout occurs. |

> ⚠️ **Always call `shutdown()`!** If you don't, the JVM will NOT exit because the thread pool threads are still alive.

### Thread Pool Sizing Guidelines

| Task Type | Recommended Pool Size | Reason |
|-----------|----------------------|--------|
| **CPU-bound** (computation, algorithms) | `Number of CPU cores` | More threads than cores = context switching overhead |
| **I/O-bound** (DB, API, file) | `Number of CPU cores × 2` (or higher) | Threads spend time waiting for I/O, so more threads can overlap the waiting |
| **Mixed** | Start with `cores × 2`, tune with profiling | Balance between CPU and I/O |

```java
// Get number of available CPU cores
int cores = Runtime.getRuntime().availableProcessors();
ExecutorService pool = Executors.newFixedThreadPool(cores * 2);
```

---

## 4. CompletableFuture (Modern Async)

`CompletableFuture` (Java 8+) is the **modern, recommended way** to do async programming in Java. It solves ALL the problems of `Future`.

### Why CompletableFuture over Future?

| Feature | `Future` | `CompletableFuture` |
|---------|----------|---------------------|
| Blocking `.get()` | ⚠️ Only way to get result | ✅ Callbacks, no need to block |
| Chaining | ❌ No | ✅ `thenApply()`, `thenAccept()`, `thenRun()` |
| Combining | ❌ No | ✅ `thenCombine()`, `allOf()`, `anyOf()` |
| Error handling | ❌ Clumsy (ExecutionException) | ✅ `exceptionally()`, `handle()` |
| Manually complete | ❌ No | ✅ `complete()`, `completeExceptionally()` |
| Callbacks | ❌ No | ✅ Non-blocking result consumption |

---

### Creating CompletableFutures

#### `supplyAsync()` — Runs async task that RETURNS a value

```java
// Runs on ForkJoinPool.commonPool() by default
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    // This runs on a background thread
    System.out.println("Running on: " + Thread.currentThread().getName());
    return fetchDataFromDatabase();  // Returns a result
});

// Get the result (blocks only when you need it)
String result = future.join();  // or future.get()
System.out.println("Result: " + result);
```

#### `runAsync()` — Runs async task with NO return value

```java
// Like supplyAsync but returns CompletableFuture<Void>
CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
    System.out.println("Sending email on: " + Thread.currentThread().getName());
    sendEmail("user@example.com", "Welcome!");
    // No return value
});

future.join();  // Wait for completion
```

#### `completedFuture()` — Already completed (for testing / default values)

```java
CompletableFuture<String> future = CompletableFuture.completedFuture("Default Value");
System.out.println(future.join());  // "Default Value" — immediately available
```

#### `join()` vs `get()` — Getting the Result

| Method | Throws | Usage |
|--------|--------|-------|
| `join()` | `CompletionException` (unchecked) | ✅ Preferred — no try-catch needed |
| `get()` | `ExecutionException` + `InterruptedException` (checked) | Forces try-catch, more verbose |

```java
// join() — Cleaner, no checked exceptions
String result = future.join();

// get() — Must handle checked exceptions
try {
    String result = future.get();
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
}
```

---

### Chaining — thenApply, thenAccept, thenRun

Chaining lets you say: **"When this async task finishes, do THIS next."**

```
supplyAsync()  →  thenApply()  →  thenApply()  →  thenAccept()
  (returns T)     (T → U)          (U → V)        (V → void)
    "get data"    "transform"     "transform"     "consume/print"
```

#### `thenApply()` — Transform the result (like `map()` in Streams)

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        return "john";  // Step 1: Get username
    })
    .thenApply(name -> {
        return name.toUpperCase();  // Step 2: Transform to uppercase
    })
    .thenApply(upperName -> {
        return "Hello, " + upperName + "!";  // Step 3: Create greeting
    });

System.out.println(future.join());  // "Hello, JOHN!"
```

#### `thenAccept()` — Consume the result (no return value)

```java
CompletableFuture.supplyAsync(() -> fetchUser(42))
    .thenApply(user -> user.getName())
    .thenAccept(name -> {
        // Consume the result — returns nothing
        System.out.println("User name is: " + name);
        saveToLog(name);
    });
```

#### `thenRun()` — Run an action (ignores the result)

```java
CompletableFuture.supplyAsync(() -> fetchUser(42))
    .thenApply(user -> processUser(user))
    .thenRun(() -> {
        // Doesn't receive any value — just runs after previous step
        System.out.println("Processing complete! Sending notification...");
    });
```

#### Chaining Comparison

| Method | Input | Output | Use When |
|--------|-------|--------|----------|
| `thenApply(Function)` | Previous result `T` | Returns new value `U` | Transform/map the result |
| `thenAccept(Consumer)` | Previous result `T` | Returns `void` | Use the result (print, save, log) |
| `thenRun(Runnable)` | Nothing (ignores result) | Returns `void` | Just run something after completion |

---

### Combining — thenCombine, thenCompose, allOf, anyOf

#### `thenCombine()` — Combine results of TWO independent futures

Both futures run **in parallel**, and when both complete, their results are combined.

```java
CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> {
    sleep(2000);
    return "John Doe";
});

CompletableFuture<Integer> orderCountFuture = CompletableFuture.supplyAsync(() -> {
    sleep(3000);
    return 15;
});

// When BOTH complete, combine the results
CompletableFuture<String> combined = userFuture.thenCombine(
    orderCountFuture,
    (user, orderCount) -> user + " has " + orderCount + " orders"
);

System.out.println(combined.join());
// "John Doe has 15 orders" (takes ~3 seconds, not 5)
```

#### `thenCompose()` — Chain dependent futures (like `flatMap()`)

Use when the second async task **depends on** the result of the first.

```java
// thenCompose — Second call NEEDS the result of the first
CompletableFuture<String> result = getUserIdAsync()         // Returns CompletableFuture<Long>
    .thenCompose(userId -> getOrdersAsync(userId))           // Uses userId, returns CompletableFuture<String>
    .thenCompose(orders -> generateReportAsync(orders));     // Uses orders, returns CompletableFuture<String>

System.out.println(result.join());
```

#### `thenApply()` vs `thenCompose()` — Key Difference

| Scenario | Use | Explanation |
|----------|-----|-------------|
| Transform result synchronously | `thenApply()` | `Function<T, U>` — returns a plain value |
| Chain another async operation | `thenCompose()` | `Function<T, CompletableFuture<U>>` — returns a future |

```java
// thenApply — Returns a VALUE  (sync transformation)
future.thenApply(name -> name.toUpperCase());
// Result type: CompletableFuture<String>

// thenCompose — Returns a FUTURE  (async chaining)
future.thenCompose(name -> fetchFromDBAsync(name));
// Result type: CompletableFuture<String>

// ❌ If you use thenApply with an async function:
future.thenApply(name -> fetchFromDBAsync(name));
// Result type: CompletableFuture<CompletableFuture<String>>  ← NESTED! Bad!

// ✅ thenCompose flattens it:
future.thenCompose(name -> fetchFromDBAsync(name));
// Result type: CompletableFuture<String>  ← Flat! Good!
```

#### `allOf()` — Wait for ALL futures to complete

```java
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> { sleep(2000); return "DB Data"; });
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> { sleep(3000); return "API Data"; });
CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> { sleep(1000); return "Cache Data"; });

// Wait for ALL to complete (runs in parallel, total ~3 seconds)
CompletableFuture<Void> allDone = CompletableFuture.allOf(f1, f2, f3);
allDone.join();  // Blocks until all 3 are done

// Get individual results (they're already done at this point)
System.out.println(f1.join());  // "DB Data"
System.out.println(f2.join());  // "API Data"
System.out.println(f3.join());  // "Cache Data"
```

**Collecting all results into a List:**
```java
List<CompletableFuture<String>> futures = List.of(f1, f2, f3);

CompletableFuture<List<String>> allResults = CompletableFuture
    .allOf(futures.toArray(new CompletableFuture[0]))
    .thenApply(v -> futures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList())
    );

List<String> results = allResults.join();
// ["DB Data", "API Data", "Cache Data"]
```

#### `anyOf()` — Return as soon as ANY ONE future completes (the fastest wins)

```java
CompletableFuture<String> server1 = CompletableFuture.supplyAsync(() -> { sleep(3000); return "Server 1 response"; });
CompletableFuture<String> server2 = CompletableFuture.supplyAsync(() -> { sleep(1000); return "Server 2 response"; });
CompletableFuture<String> server3 = CompletableFuture.supplyAsync(() -> { sleep(2000); return "Server 3 response"; });

// Returns the result of whichever future completes FIRST
Object fastest = CompletableFuture.anyOf(server1, server2, server3).join();
System.out.println("Fastest response: " + fastest);
// "Fastest response: Server 2 response" (completed in 1 second)
```

**Use Case:** Querying multiple replicas/mirrors and using the fastest response.

---

### Error Handling — exceptionally, handle, whenComplete

#### `exceptionally()` — Recover from errors (like try-catch)

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        if (true) throw new RuntimeException("Database connection failed!");
        return "User Data";
    })
    .exceptionally(ex -> {
        // This runs ONLY if there's an exception
        System.out.println("Error: " + ex.getMessage());
        return "Default User";  // Fallback value
    });

System.out.println(future.join());  // "Default User"
```

#### `handle()` — Handle BOTH success and error

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        // May succeed or fail
        return riskyOperation();
    })
    .handle((result, exception) -> {
        if (exception != null) {
            System.out.println("Failed: " + exception.getMessage());
            return "Fallback Value";
        }
        return "Success: " + result;
    });

System.out.println(future.join());
```

#### `whenComplete()` — Side effects on completion (doesn't change the result)

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> "User Data")
    .whenComplete((result, exception) -> {
        // Runs for both success and failure
        // Does NOT change the result — just performs side effects
        if (exception != null) {
            logError(exception);
        } else {
            logSuccess(result);
        }
    });

System.out.println(future.join());  // "User Data" (unchanged)
```

#### Error Handling Comparison

| Method | Gets Result? | Gets Exception? | Can Change Result? | Use When |
|--------|-------------|-----------------|-------------------|----------|
| `exceptionally(ex)` | ❌ No | ✅ Yes (only on error) | ✅ Yes (return fallback) | Provide a default/fallback value |
| `handle(result, ex)` | ✅ Yes | ✅ Yes | ✅ Yes (return new value) | Handle both success and failure |
| `whenComplete(result, ex)` | ✅ Yes | ✅ Yes | ❌ No (original result passes through) | Logging, cleanup, side effects |

---

### Running on Different Thread Pools

By default, `CompletableFuture` uses the `ForkJoinPool.commonPool()`. You can provide a **custom thread pool** for better control.

#### Why Use a Custom Thread Pool?

| Problem with `ForkJoinPool.commonPool()` | Solution |
|------------------------------------------|----------|
| Shared across the entire application | Isolate your tasks with a dedicated pool |
| Limited threads (= CPU cores - 1) | Create pools with more threads for I/O tasks |
| One slow task can block others | Separate pool prevents interference |
| Hard to monitor/tune | Custom pools give you full control |

```java
// Create a custom thread pool
ExecutorService ioPool = Executors.newFixedThreadPool(10);

// Use it with supplyAsync
CompletableFuture<String> future = CompletableFuture.supplyAsync(
    () -> fetchFromDatabase(),
    ioPool  // ← Runs on our custom pool instead of common pool
);

// Async variants of chaining methods also accept an executor
future.thenApplyAsync(result -> transform(result), ioPool);
future.thenAcceptAsync(result -> save(result), ioPool);
future.thenRunAsync(() -> cleanup(), ioPool);

// Don't forget to shut down the pool when done
ioPool.shutdown();
```

#### `thenApply()` vs `thenApplyAsync()` — Which Thread Runs It?

| Method | Runs On |
|--------|---------|
| `thenApply()` | Same thread that completed the previous stage (or caller thread) |
| `thenApplyAsync()` | A thread from `ForkJoinPool.commonPool()` |
| `thenApplyAsync(fn, executor)` | A thread from the specified executor |

```java
CompletableFuture.supplyAsync(() -> {
    System.out.println("supplyAsync: " + Thread.currentThread().getName());
    return "data";
})
.thenApply(data -> {
    // Runs on the SAME thread that completed supplyAsync
    System.out.println("thenApply: " + Thread.currentThread().getName());
    return data.toUpperCase();
})
.thenApplyAsync(data -> {
    // Runs on a DIFFERENT thread from the common pool
    System.out.println("thenApplyAsync: " + Thread.currentThread().getName());
    return data + "!";
});
```

> **Rule of thumb:** Use `thenApply()` for fast, lightweight transformations. Use `thenApplyAsync()` for slow tasks or when you need a specific thread pool.

---

## 5. Virtual Threads (Java 21+)

Virtual Threads are **lightweight threads** introduced in Java 21 that are managed by the JVM (not the OS). You can create **millions** of them without running out of memory.

### Platform Threads vs Virtual Threads

| Feature | Platform Thread (Traditional) | Virtual Thread (Java 21+) |
|---------|-------------------------------|---------------------------|
| Managed by | Operating System | JVM |
| Memory per thread | ~1 MB (OS stack) | ~Few KB |
| Max count | Hundreds to low thousands | **Millions** |
| Creation cost | Expensive | Very cheap |
| Context switch | Expensive (OS-level) | Cheap (JVM-level) |
| Best for | CPU-intensive work | I/O-intensive work |

### Creating Virtual Threads

```java
// 1. Simple — Thread.startVirtualThread()
Thread vThread = Thread.startVirtualThread(() -> {
    System.out.println("Running on: " + Thread.currentThread());
    System.out.println("Is virtual: " + Thread.currentThread().isVirtual());  // true
});
vThread.join();

// 2. Using Thread.ofVirtual()
Thread vThread2 = Thread.ofVirtual()
    .name("my-virtual-thread")
    .start(() -> {
        System.out.println("Named virtual thread: " + Thread.currentThread().getName());
    });

// 3. Virtual Thread Executor — Best for production
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    // Creates a NEW virtual thread for EACH task (cheap!)
    Future<String> f1 = executor.submit(() -> fetchFromDB());
    Future<String> f2 = executor.submit(() -> callExternalAPI());
    Future<String> f3 = executor.submit(() -> readFile());

    System.out.println(f1.get());
    System.out.println(f2.get());
    System.out.println(f3.get());
}  // Auto-shutdown with try-with-resources
```

### When to Use Virtual Threads

```java
// ✅ PERFECT for I/O-heavy work — handles thousands of concurrent connections
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    // Simulate 10,000 concurrent HTTP requests
    List<Future<String>> futures = new ArrayList<>();
    for (int i = 0; i < 10_000; i++) {
        futures.add(executor.submit(() -> {
            // Each virtual thread waits for I/O — that's fine, millions can wait cheaply
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        }));
    }
    // With platform threads, this would require 10,000 OS threads (~10 GB memory!)
    // With virtual threads, this uses just a few MB
}
```

```java
// ❌ NOT ideal for CPU-intensive work
// Virtual threads don't help here because the JVM can only run
// as many simultaneously as you have CPU cores anyway
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> {
        // Heavy math — a virtual thread buys you nothing here
        return fibonacci(1_000_000);
    });
}
// Use a platform thread pool (Executors.newFixedThreadPool) for CPU-bound work
```

### Virtual Threads with CompletableFuture

```java
ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

CompletableFuture<String> future = CompletableFuture.supplyAsync(
    () -> fetchFromDatabase(),
    virtualExecutor  // Runs on a virtual thread!
);

future.thenApplyAsync(data -> transform(data), virtualExecutor)
      .thenAcceptAsync(result -> save(result), virtualExecutor)
      .join();

virtualExecutor.shutdown();
```

---

## Common Patterns & Real-World Examples

### Pattern 1: Parallel API Calls (Most Common)

```java
public UserDashboard getDashboard(Long userId) {

    CompletableFuture<User> userFuture = CompletableFuture
        .supplyAsync(() -> userService.findById(userId));

    CompletableFuture<List<Order>> ordersFuture = CompletableFuture
        .supplyAsync(() -> orderService.findByUserId(userId));

    CompletableFuture<List<Notification>> notifFuture = CompletableFuture
        .supplyAsync(() -> notificationService.getUnread(userId));

    // Wait for all and build the dashboard
    CompletableFuture.allOf(userFuture, ordersFuture, notifFuture).join();

    return new UserDashboard(
        userFuture.join(),
        ordersFuture.join(),
        notifFuture.join()
    );
}
```

### Pattern 2: Async with Timeout

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> slowExternalAPICall())
    .orTimeout(5, TimeUnit.SECONDS)          // Throws TimeoutException after 5s
    .exceptionally(ex -> "Timeout! Using cached response");

// OR — complete with a default value on timeout (Java 9+)
CompletableFuture<String> future2 = CompletableFuture
    .supplyAsync(() -> slowExternalAPICall())
    .completeOnTimeout("Default Response", 5, TimeUnit.SECONDS);
```

### Pattern 3: Retry on Failure

```java
public CompletableFuture<String> fetchWithRetry(int maxRetries) {
    return CompletableFuture.supplyAsync(() -> callUnreliableAPI())
        .exceptionally(ex -> {
            if (maxRetries > 0) {
                System.out.println("Retrying... attempts left: " + maxRetries);
                return fetchWithRetry(maxRetries - 1).join();
            }
            throw new CompletionException(ex);
        });
}

// Usage
String result = fetchWithRetry(3).join();
```

### Pattern 4: Fire-and-Forget (Don't Wait for Result)

```java
@Service
public class UserService {

    public User registerUser(User user) {
        User saved = userRepository.save(user);

        // Fire-and-forget — don't wait for email to be sent
        CompletableFuture.runAsync(() -> emailService.sendWelcome(saved.getEmail()));

        // Fire-and-forget — don't wait for analytics
        CompletableFuture.runAsync(() -> analyticsService.trackRegistration(saved.getId()));

        return saved;  // Returns immediately, emails sent in background
    }
}
```

### Pattern 5: Sequential Async Chain (When Tasks Depend on Each Other)

```java
CompletableFuture<String> pipeline = CompletableFuture
    .supplyAsync(() -> authenticate("user", "pass"))          // Step 1: Login
    .thenCompose(token -> fetchUserProfile(token))            // Step 2: Get profile (needs token)
    .thenCompose(profile -> fetchUserOrders(profile.getId())) // Step 3: Get orders (needs profile)
    .thenApply(orders -> generateReport(orders))              // Step 4: Generate report
    .exceptionally(ex -> {
        log.error("Pipeline failed: " + ex.getMessage());
        return "Error generating report";
    });

String report = pipeline.join();
```

---

## Pitfalls & Best Practices

### ❌ Common Pitfalls

#### 1. Swallowing Exceptions Silently
```java
// ❌ BAD — Exception is silently swallowed!
CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Error!");
});
// If you never call .join() or .get(), the exception is lost forever!

// ✅ GOOD — Always handle exceptions
CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Error!");
})
.exceptionally(ex -> {
    log.error("Task failed: ", ex);
    return "fallback";
})
.thenAccept(result -> System.out.println(result));
```

#### 2. Blocking the Common Pool
```java
// ❌ BAD — Blocking I/O on the common pool starves other tasks
CompletableFuture.supplyAsync(() -> {
    Thread.sleep(60000);  // Blocks a common pool thread for 60 seconds!
    return httpClient.send(request);  // Another blocking call
});

// ✅ GOOD — Use a dedicated I/O pool for blocking operations
ExecutorService ioPool = Executors.newFixedThreadPool(20);
CompletableFuture.supplyAsync(() -> {
    return httpClient.send(request);
}, ioPool);  // Runs on dedicated I/O pool
```

#### 3. Forgetting to Shut Down the Executor
```java
// ❌ BAD — JVM never exits because pool threads are alive
ExecutorService pool = Executors.newFixedThreadPool(5);
pool.submit(() -> doWork());
// Missing: pool.shutdown();

// ✅ GOOD — Use try-with-resources (Java 19+) or manual shutdown
try (var pool = Executors.newFixedThreadPool(5)) {
    pool.submit(() -> doWork());
}  // Auto-shuts down

// Or for older Java:
ExecutorService pool = Executors.newFixedThreadPool(5);
try {
    pool.submit(() -> doWork());
} finally {
    pool.shutdown();
}
```

#### 4. Calling `.get()` Immediately (Defeats Async Purpose)
```java
// ❌ BAD — Blocks immediately, making async pointless
String result = CompletableFuture.supplyAsync(() -> fetchData()).get();  // BLOCKS HERE!

// ✅ GOOD — Chain operations, block only when you NEED the final result
CompletableFuture.supplyAsync(() -> fetchData())
    .thenApply(data -> transform(data))
    .thenAccept(result -> display(result));
```

### ✅ Best Practices

| Practice | Why |
|----------|-----|
| Use **Constructor injection** for thread pools | Makes thread pools testable and configurable |
| Use **`join()` over `get()`** | Avoids verbose checked exception handling |
| Always handle exceptions with **`exceptionally()`** or **`handle()`** | Prevents silent failures |
| Use a **dedicated thread pool** for I/O tasks | Prevents blocking the common pool |
| **Shut down executors** when done | Prevents JVM from hanging |
| Use **`allOf()`** for independent parallel tasks | Maximum parallelism |
| Use **`thenCompose()`** for dependent async tasks | Avoids nested futures |
| Set **timeouts** with `orTimeout()` or `completeOnTimeout()` | Prevents indefinite waiting |
| Prefer **Virtual Threads** (Java 21+) for I/O-bound work | Scales to millions of concurrent tasks |

---

## Quick Reference Cheat Sheet

### Creating

| Method | Returns | Description |
|--------|---------|-------------|
| `supplyAsync(() -> value)` | `CompletableFuture<T>` | Async task that returns a value |
| `runAsync(() -> action)` | `CompletableFuture<Void>` | Async task with no return |
| `completedFuture(value)` | `CompletableFuture<T>` | Already completed with a value |

### Chaining (One-after-another)

| Method | Input | Output | Description |
|--------|-------|--------|-------------|
| `thenApply(T → U)` | Result | New value | Transform result |
| `thenAccept(T → void)` | Result | Nothing | Consume result |
| `thenRun(() → void)` | Nothing | Nothing | Run action after completion |
| `thenCompose(T → CF<U>)` | Result | New future | Chain dependent async tasks |

### Combining (Parallel)

| Method | Description |
|--------|-------------|
| `thenCombine(other, (a,b) → c)` | Combine results of 2 futures |
| `allOf(cf1, cf2, cf3...)` | Wait for ALL futures to complete |
| `anyOf(cf1, cf2, cf3...)` | Return when ANY ONE completes first |

### Error Handling

| Method | Description |
|--------|-------------|
| `exceptionally(ex → fallback)` | Recover from errors with a fallback |
| `handle((result, ex) → value)` | Handle success and error, return new value |
| `whenComplete((result, ex) → void)` | Side effects only, doesn't change result |

### Timeout (Java 9+)

| Method | Description |
|--------|-------------|
| `orTimeout(duration, unit)` | Throws `TimeoutException` after timeout |
| `completeOnTimeout(value, duration, unit)` | Uses default value on timeout |

### Async Variants

Every chaining method has an `*Async` variant that runs on a different thread:

```java
thenApply()       →  thenApplyAsync()        →  thenApplyAsync(fn, executor)
thenAccept()      →  thenAcceptAsync()       →  thenAcceptAsync(fn, executor)
thenRun()         →  thenRunAsync()          →  thenRunAsync(fn, executor)
thenCombine()     →  thenCombineAsync()      →  thenCombineAsync(fn, executor)
thenCompose()     →  thenComposeAsync()      →  thenComposeAsync(fn, executor)
```

---

### Complete Real-World Example

```java
@Service
public class ProductService {

    private final ExecutorService ioPool = Executors.newFixedThreadPool(10);

    public CompletableFuture<ProductDetail> getProductDetail(Long productId) {

        // 1. Fetch product, reviews, and inventory IN PARALLEL
        CompletableFuture<Product> productFuture = CompletableFuture
            .supplyAsync(() -> productRepo.findById(productId).orElseThrow(), ioPool);

        CompletableFuture<List<Review>> reviewsFuture = CompletableFuture
            .supplyAsync(() -> reviewService.getReviews(productId), ioPool);

        CompletableFuture<Integer> stockFuture = CompletableFuture
            .supplyAsync(() -> inventoryService.getStock(productId), ioPool)
            .exceptionally(ex -> {
                log.warn("Inventory unavailable, defaulting to 0");
                return 0;  // Graceful fallback
            });

        // 2. Combine all results when all complete
        return CompletableFuture.allOf(productFuture, reviewsFuture, stockFuture)
            .thenApply(v -> new ProductDetail(
                productFuture.join(),
                reviewsFuture.join(),
                stockFuture.join()
            ))
            .orTimeout(10, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                log.error("Failed to build product detail", ex);
                throw new CompletionException(ex);
            });
    }

    @PreDestroy
    public void cleanup() {
        ioPool.shutdown();
    }
}
```

---

> **Summary:** Start with `CompletableFuture` for most async needs. Use `Virtual Threads` (Java 21+) for I/O-heavy workloads at scale. Avoid raw `Thread/Runnable` and legacy `Future` in production code.

---

> **Created**: February 2026
> **Topic**: Async Programming in Java — From Threads to Virtual Threads
