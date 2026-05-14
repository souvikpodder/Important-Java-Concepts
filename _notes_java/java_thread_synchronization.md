# Synchronization in Java
Synchronization in java is the capability to control the access of multiple threads to any shared resource. Java Synchronization is better option where we want to allow only one thread to access the shared resource.

**The synchronization is mainly used to :**
- To **prevent Thread Interference** (Thread interference is a condition which occurs when more than one threads, executing simultaneously, access same piece of data.).
- To **prevent Consistency Problem** (Memory consistency errors occur when different threads have inconsistent views of what should be the same data.).

## Types of Synchronization :
![synchronization-types](https://user-images.githubusercontent.com/2780145/35073887-214be2f0-fc11-11e7-9a84-604feca9bbb4.png)

## Thread Synchronization
There are two types of thread synchronization mutual exclusive and inter-thread communication.
1. Mutual Exclusive
2. Cooperation (Inter-thread communication in java)

**Mutual Exclusive :**
Mutual Exclusive helps keep threads from interfering with one another while sharing data. 
This can be done by three ways in java :
1. by synchronized method
2. by synchronized block
3. by static synchronization

**Concept of Lock in Java :**
Synchronization is built around an internal entity known as the lock or monitor. Every object has an lock associated with it. By convention, a thread that needs consistent access to an object's fields has to acquire the object's lock before accessing them, and then release the lock when it's done with them. The package java.util.concurrent.locks contains several lock implementations.

## Synchronized Method in Java :
If you declare any method as synchronized, it is known as synchronized method. Synchronized method is used to lock an object for any shared resource. When a thread invokes a synchronized method, it automatically acquires the lock for that object and releases it when the thread completes its task.
```java
synchronized void printTable(int n){  //synchronized method  
   for(int i=1;i<=5;i++){  
     System.out.println(n*i);  
     try{  Thread.sleep(200);  
     }catch(Exception e){System.out.println(e);}  
   } }  
```

## Synchronized Block in Java
Synchronized block can be used to perform synchronization on any specific resource of the method. Suppose you have 50 lines of code in your method, but you want to synchronize only 5 lines, you can use synchronized block. Scope of synchronized block is smaller than the method. If you put all the codes of the method in the synchronized block, it will work same as the synchronized method. Syntax - synchronized (object reference expression) { ... }  
```java
void printTable(int n) {
    synchronized(this) { //synchronized block  
        for (int i = 1; i <= 5; i++) {
            System.out.println(n * i);
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
```

## Static Synchronization in Java
If you make any static method as synchronized, the lock will be on the class not on object.

**Problem without static synchronization :**

![static synchronization](https://user-images.githubusercontent.com/2780145/35074524-596ce5f0-fc14-11e7-8fd9-e7defb2de0ae.png)

Suppose there are two objects of a shared class(e.g. Table) named object1 and object2.In case of synchronized method and synchronized block there cannot be interference between t1 and t2 or t3 and t4 because t1 and t2 both refers to a common object that have a single lock.But there can be interference between t1 and t3 or t2 and t4 because t1 acquires another lock and t3 acquires another lock.I want no interference between t1 and t3 or t2 and t4.Static synchronization solves this problem.
```java
synchronized static void printTable(int n) {
    for (int i = 1; i <= 10; i++) {
        System.out.println(n * i);
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
```

## Deadlock in Java

![deadlock of threads](https://user-images.githubusercontent.com/2780145/35073886-2122bc2c-fc11-11e7-97a0-a04938a49227.png)

Deadlock can occur in a situation when a thread is waiting for an object lock, that is acquired by another thread and second thread is waiting for an object lock that is acquired by first thread. Since, both threads are waiting for each other to release the lock, the condition is called deadlock.
```java
public class TestDeadlockExample1 {
    public static void main(String[] args) {
        final String resource1 = "John";
        final String resource2 = "Tom";

        // t1 tries to lock resource1 then resource2  
        Thread t1 = new Thread() {
            public void run() {
                synchronized(resource1) {
                    System.out.println("Thread 1: locked resource 1");
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {}
                    synchronized(resource2) {
                        System.out.println("Thread 1: locked resource 2");
                    }
                }
            }
        };

        // t2 tries to lock resource2 then resource1  
        Thread t2 = new Thread() {
            public void run() {
                synchronized(resource2) {
                    System.out.println("Thread 2: locked resource 2");
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {}
                    synchronized(resource1) {
                        System.out.println("Thread 2: locked resource 1");
                    }
                }
            }
        };

        t1.start();
        t2.start();
    }
}
```
**Output:** 
```
Thread 1: locked resource 1
Thread 2: locked resource 2
```

## Inter-Thread Communication in Java
Inter-thread communication or Co-operation is all about allowing synchronized threads to communicate with each other. Cooperation (Inter-thread communication) is a mechanism in which a thread is paused running in its critical section and another thread is allowed to enter (or lock) in the same critical section to be executed.

It is implemented by following 3 methods of **Object class:**
1. wait()
2. notify()
3. notifyAll()

### wait() Method
Causes current thread to release the lock and wait until either another thread invokes the notify() method or the notifyAll() method for this object, or a specified amount of time has elapsed. The current thread must own this object's monitor, so it must be called from the synchronized method only otherwise it will throw exception.
- public final void wait()throws InterruptedException	
- public final void wait(long timeout)throws InterruptedException

### notify() Method
Wakes up a single thread that is waiting on this object's monitor. If any threads are waiting on this object, one of them is chosen to be awakened. The choice is arbitrary and occurs at the discretion of the implementation.
- public final void notify()

### notifyAll() Method
Wakes up all threads that are waiting on this object's monitor.
- public final void notifyAll()

## Understanding the process of inter-thread communication

![inter-thread communication](https://user-images.githubusercontent.com/2780145/35122584-44ef23e8-fcc5-11e7-9caf-581529986ace.png)

**Explanation of the above diagram :**
1. Threads enter to acquire lock.
2. Lock is acquired by one thread.
3. Now thread goes to waiting state if you call wait() method on the object. Otherwise, it releases the lock & exits when done.
4. If you call notify() or notifyAll() method, thread moves to the notified state (runnable state).
5. Now thread is available to acquire lock.
6. After completion of the task, thread releases the lock and exits the monitor state of the object.

**NOTE :** wait(), notify() and notifyAll() methods are defined in Object class not Thread class because they are related to lock and object has a lock.

## Difference between wait and sleep
<table class="alt">
<tbody><tr><th>wait()</th><th>sleep()</th></tr>
<tr><td>wait() method releases the lock</td><td>sleep() method doesn't release the lock.</td></tr>
<tr><td>is a method of Object class</td><td>is a method of Thread class</td></tr>
<tr><td>is a non-static method</td><td>is a static method</td></tr>
<tr><td>should be notified by notify() or notifyAll() methods</td><td>after the specified amount of time, sleep is completed.</td></tr>
</tbody></table>

**Example of Inter Thread Communication :**
```java
class Customer {
 int amount = 10000;

 synchronized void withdraw(int amount) {
  System.out.println("going to withdraw...");
  if (this.amount < amount) {
   System.out.println("Less balance; waiting for deposit...");
   try { wait(); } catch (Exception e) {} 
  }   // Simple Eg. So, doesn't consider if amount is again low after notify()
  this.amount -= amount;
  System.out.println("withdraw completed...");
 }
 
 synchronized void deposit(int amount) {
  System.out.println("going to deposit...");
  this.amount += amount;
  System.out.println("deposit completed... ");
  notify();
 }
}

class Test {
 public static void main(String args[]) {
  final Customer c = new Customer();
  new Thread() { public void run() { c.withdraw(15000); }}.start();
  new Thread() { public void run() { c.deposit(10000); }}.start();
 }
}
```

## Interrupting a Thread
An interrupt is an indication to a thread that it should stop what it is doing and do something else. It's up to the programmer to decide exactly how a thread responds to an interrupt, but it is very common for the thread to terminate.

The 3 methods provided by the Thread class for interrupting a thread :
- public void interrupt()
If any thread is in sleeping or waiting state (i.e. sleep() or wait()) is invoked, calling the interrupt() method on the thread, breaks out the sleeping or waiting state throwing InterruptedException. If the thread is not in the sleeping or waiting state, calling the interrupt() method performs normal behaviour and doesn't interrupt the thread but sets the interrupt flag to true.
```
t1.interrupt();  
```
- public static boolean interrupted()
The static interrupted() method returns the interrupted flag afterthat it sets the flag to false if it is true.
```
t1.interrupted()
```
- public boolean isInterrupted()
The isInterrupted() method returns the interrupted flag either true or false.

**NOTE :** If we interrupt a thread, and propagate the exception, it will stop working. If we don't want to stop the thread, we should handle it where sleep() or wait() method is invoked.
```java
class TestIntrpt extends Thread {
 public void run() {
  try { Thread.sleep(1000);
   System.out.println("task");
  } catch (InterruptedException e) {
   System.out.println("Exception handled " + e); }
  System.out.println("thread is still running after exception...");
 }
 public static void main(String args[]) {
  TestIntrpt t1 = new TestIntrpt();
  t1.start();
  t1.interrupt();
 }}
```

## Reentrant Monitor in Java
According to Sun Microsystems, **Java monitors are reentrant** means java thread can reuse the same monitor for different synchronized methods if method is called from the method.

**Advantage of Reentrant Monitor:** It eliminates the possibility of single thread deadlocking.

**Example :**
```java
class Reentrant {  
    public synchronized void m() {  
    n();  
    System.out.println("this is m() method"); }  
    
    public synchronized void n() {  
    System.out.println("this is n() method"); }  
}  

public class ReentrantExample {
 public static void main(String args[]) {
  final ReentrantExample re = new ReentrantExample();
  
  Thread t1 = new Thread() { //creating thread using annonymous class
   public void run() {
    re.m(); }};  //calling m() method of Reentrant class  
   
  t1.start();
 }}


---

## `ReentrantLock` — Explicit Locking (java.util.concurrent.locks)

`ReentrantLock` gives more control than `synchronized`:

```java
import java.util.concurrent.locks.ReentrantLock;

class SafeCounter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();  // ALWAYS unlock in finally block
        }
    }

    public int getCount() { return count; }
}
```

**ReentrantLock vs `synchronized`:**
| Feature | synchronized | ReentrantLock |
|---|---|---|
| Fairness | Not fair (OS decides) | Can be fair: `new ReentrantLock(true)` |
| Interruptible wait | No | Yes: `lockInterruptibly()` |
| Try without blocking | No | Yes: `tryLock()`, `tryLock(timeout)` |
| Multiple conditions | No (1 wait-set per object) | Yes: multiple `Condition` objects |
| Read/Write split | No | Via `ReadWriteLock` |

```java
// tryLock — avoid deadlock with timeout
if (lock.tryLock(2, TimeUnit.SECONDS)) {
    try {
        // do work
    } finally {
        lock.unlock();
    }
} else {
    System.out.println("Could not acquire lock — skip or retry");
}
```

---

## `ReadWriteLock` — High-Concurrency Reads

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Cache {
    private final Map<String, String> data = new HashMap<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public String read(String key) {
        rwLock.readLock().lock();     // Multiple threads can read concurrently
        try {
            return data.get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void write(String key, String value) {
        rwLock.writeLock().lock();    // Exclusive — blocks all reads and writes
        try {
            data.put(key, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
// ReadWriteLock shines when reads >> writes (e.g., caches, configuration)
```

---

## `Atomic` Classes (Lock-Free Thread Safety)

```java
import java.util.concurrent.atomic.*;

AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();    // atomic i++
counter.decrementAndGet();    // atomic i--
counter.addAndGet(5);         // atomic i += 5
counter.compareAndSet(5, 10); // if current==5, set to 10 (CAS)
counter.get();                // read current value

AtomicLong longCounter = new AtomicLong(0);
AtomicBoolean flag = new AtomicBoolean(false);
AtomicReference<String> ref = new AtomicReference<>("initial");
ref.compareAndSet("initial", "updated");  // atomic CAS on reference

// LongAdder — better than AtomicLong for high-contention counters
LongAdder adder = new LongAdder();
adder.increment();
adder.add(5);
long total = adder.sum();
```

**Why atomic classes?** They use **CAS (Compare-And-Swap)** CPU instructions — no locking needed, no context switching → faster than synchronized for simple operations.

---

## `Condition` — Advanced Wait/Notify with ReentrantLock

```java
import java.util.concurrent.locks.*;

class BoundedBuffer<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int maxSize;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    BoundedBuffer(int maxSize) { this.maxSize = maxSize; }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == maxSize) notFull.await();  // wait if full
            queue.offer(item);
            notEmpty.signal();  // wake up a consumer
        } finally { lock.unlock(); }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) notEmpty.await();  // wait if empty
            T item = queue.poll();
            notFull.signal();   // wake up a producer
            return item;
        } finally { lock.unlock(); }
    }
}
// Advantage over wait/notify: separate conditions (notFull, notEmpty)
// → targeted signaling instead of waking everyone up
```

---

## `Semaphore` — Limit Concurrent Access

```java
import java.util.concurrent.Semaphore;

// Allow max 3 concurrent database connections
Semaphore semaphore = new Semaphore(3);

class DatabaseWorker implements Runnable {
    @Override
    public void run() {
        try {
            semaphore.acquire();        // acquire a permit (blocks if 0)
            System.out.println("Working: " + Thread.currentThread().getName());
            Thread.sleep(2000);         // simulated DB work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();        // release permit
        }
    }
}
```

---

## Interview Questions — Synchronization

**Q1. What is the difference between `wait()` and `sleep()`?**
| | `wait()` | `sleep()` |
|---|---|---|
| Lock | Releases the lock | Holds the lock |
| Class | Object | Thread |
| Static? | No | Yes |
| Wake up | `notify()` / `notifyAll()` | After timeout |
| Context | Must be in `synchronized` | Can be anywhere |

**Q2. Why are `wait()`, `notify()`, `notifyAll()` in `Object` and not in `Thread`?**
- Because locks are per-object (every object has a monitor), not per-thread.
- Any object can be a lock. `wait()` releases the lock on the **object** it's called on. So the method belongs to `Object`.

**Q3. What is a livelock? How is it different from deadlock?**
- **Deadlock**: threads are stuck, not doing anything.
- **Livelock**: threads are active (not stuck), but keep responding to each other and making no progress.
```
// Example: two people in a corridor both stepping the same direction to let the other pass
Thread A: detects conflict → backs off → tries again
Thread B: detects conflict → backs off → tries again
// Both keep backing off and retrying forever
```
- Fix: add randomized backoff timing.

**Q4. What is a starvation?**
- A thread is perpetually denied access to a resource because other threads keep acquiring it first.
- Example: low-priority thread waiting while high-priority threads always run first.
- Fix: use fair locks (`new ReentrantLock(true)`).

**Q5. When should you use `notifyAll()` instead of `notify()`?**
- Use `notify()`: when all waiting threads are **identical** in what they're waiting for (one wake-up is enough).
- Use `notifyAll()`: when waiting threads have **different conditions** — if only one is woken but it's not the right one, progress is missed.
- In most production code, prefer `notifyAll()` for correctness (less risk of missed signals).

**Q6. What is double-checked locking for Singleton?**
```java
class Singleton {
    private static volatile Singleton instance;  // volatile is REQUIRED

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {                  // first check (no lock)
            synchronized (Singleton.class) {
                if (instance == null) {          // second check (with lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
// volatile prevents partial-initialization visibility issues (JMM)
// Without volatile, another thread may see a non-null but incompletely constructed instance
```

**Q7. What is `synchronized` on a static method vs an instance method?**
- `static synchronized method()` → locks the **Class object** (`Singleton.class`) — shared across ALL instances.
- `instance synchronized method()` → locks `this` — one lock per object instance.
- Two threads can simultaneously run static-sync and instance-sync methods on the same object because they use **different locks**.

**Q8. What is CAS (Compare-And-Swap) and how does it differ from locking?**
- CAS is a hardware-level atomic instruction: `if (current value == expected) { set to new value; return true } else { return false }`.
- **Locking**: blocking — thread waits if lock is taken (context switch = expensive).
- **CAS (lock-free)**: non-blocking — thread retries (spin) if CAS fails — no context switch → faster for low-contention scenarios.
- Java's `AtomicInteger`, `LongAdder`, `ConcurrentHashMap` use CAS internally.

**Q9. Explain the producer-consumer problem and its Java solution.**
```java
// Solution using BlockingQueue — the cleanest approach
BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

// Producer
new Thread(() -> {
    for (int i = 0; i < 100; i++) {
        queue.put(i);   // blocks if queue is full
    }
}).start();

// Consumer
new Thread(() -> {
    while (true) {
        Integer item = queue.take();  // blocks if queue is empty
        process(item);
    }
}).start();
```
- `BlockingQueue` implementations: `LinkedBlockingQueue`, `ArrayBlockingQueue`, `PriorityBlockingQueue`, `SynchronousQueue`.

**Q10. What is the `happens-before` relationship?**
- A guarantee from the Java Memory Model that if action A `happens-before` action B, the effects of A are visible to B.
- Key `happens-before` rules:
  - A thread's `start()` happens-before any action in the started thread.
  - All actions in a thread happen-before `join()` returns.
  - Unlocking a monitor happens-before locking the same monitor.
  - Write to `volatile` happens-before read of that variable.


