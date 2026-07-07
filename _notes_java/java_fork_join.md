# Fork/Join Framework in Java

The **Fork/Join framework** was introduced in Java 7 to help speed up parallel processing by taking advantage of multiple processors. It is designed for work that can be broken down recursively into smaller pieces (the **"divide and conquer"** approach).

It is part of the `java.util.concurrent` package.

## Core Concepts

The framework operates on two basic principles:
1. **Fork:** Breaking a large task into smaller, independent subtasks that can be executed concurrently.
2. **Join:** Waiting for the completion of the subtasks and merging their results to produce the final outcome.

### Work-Stealing Algorithm

The `ForkJoinPool` uses a **work-stealing algorithm**. 
- Every thread in the pool has its own double-ended queue (deque) of tasks.
- If a thread completes all tasks in its own deque, it can "steal" tasks from the tail of another thread's deque.
- This balances the workload and ensures that all threads remain busy, maximizing CPU utilization.

## Key Classes

1. **`ForkJoinPool`**: The heart of the framework. It manages the threads and the execution of the tasks. It implements the `ExecutorService` interface.
2. **`ForkJoinTask<V>`**: The base type for tasks executed within a `ForkJoinPool`. You rarely use it directly; instead, you extend one of its two primary subclasses:
   - **`RecursiveAction`**: Used for tasks that **do not return a result** (e.g., sorting an array, updating records).
   - **`RecursiveTask<V>`**: Used for tasks that **return a result** (e.g., calculating a sum, searching for a value).

## How to use `RecursiveTask` / `RecursiveAction`

When implementing a task, you generally follow this pattern in the `compute()` method:

```java
protected Result compute() {
    if (task is small enough) {
        // base case: solve the problem directly
        return computeDirectly();
    } else {
        // recursive case: divide into subtasks
        Task subtask1 = new Task(firstHalf);
        Task subtask2 = new Task(secondHalf);
        
        // fork subtask1 to run asynchronously
        subtask1.fork();
        
        // compute subtask2 directly in the current thread
        Result result2 = subtask2.compute();
        
        // join subtask1 to get its result
        Result result1 = subtask1.join();
        
        // combine and return
        return combine(result1, result2);
    }
}
```

### Important Rule for Forking and Joining
Notice the order:
1. `left.fork()`
2. `right.compute()`
3. `left.join()`

**Why not `left.fork(); right.fork(); left.join(); right.join();`?**
While you *can* do that, calling `compute()` on the right task directly avoids the overhead of enqueuing the right task onto the pool and instead executes it immediately on the current thread, which is more efficient.

## Example: Array Sum (using RecursiveTask)

Here is a brief example of how to calculate the sum of a large array:

```java
class SumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10000;
    private int[] array;
    private int start, end;

    public SumTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) sum += array[i];
            return sum;
        } else {
            int mid = start + (end - start) / 2;
            SumTask left = new SumTask(array, start, mid);
            SumTask right = new SumTask(array, mid, end);
            
            left.fork(); // Async execution
            Long rightResult = right.compute(); // Synchronous execution in current thread
            Long leftResult = left.join(); // Wait for left to complete
            
            return leftResult + rightResult;
        }
    }
}
```

Usage:
```java
ForkJoinPool pool = new ForkJoinPool();
SumTask task = new SumTask(array, 0, array.length);
long totalSum = pool.invoke(task);
```

## Difference between ExecutorService and ForkJoinPool

| Feature | `ExecutorService` | `ForkJoinPool` |
|---------|--------------------|-----------------|
| **Design** | Good for independent, heterogeneous tasks. | Good for recursively dividing tasks (divide and conquer). |
| **Work Stealing** | No work stealing. A thread takes from a common blocking queue. | Uses work-stealing algorithm (idle threads steal from busy threads). |
| **Creation of Tasks** | Tasks do not generally spawn other tasks. | Tasks frequently spawn subtasks (forking). |

## When to use Fork/Join
- When you have a massive amount of data and a problem that can be naturally divided into independent sub-problems (like sorting, searching, mathematical computations).
- The sub-problems should be completely independent of each other to avoid synchronization issues.
- Be careful setting the `THRESHOLD`. If the threshold is too low, the overhead of creating tasks will outweigh the benefits of parallelization. If it's too high, you won't utilize all available processors.
