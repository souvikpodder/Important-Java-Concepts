package multithreading.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Example demonstrating the Fork/Join framework in Java.
 * This program calculates the sum of a large array of integers using ForkJoinPool.
 */
public class ForkJoinExample {

    public static void main(String[] args) {
        // 1. Create a large array
        int[] array = new int[100_000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        // 2. Create the ForkJoinPool
        // By default, it creates a pool with a number of threads equal to available processors.
        try (ForkJoinPool pool = new ForkJoinPool()) {
            
            // 3. Create the main task representing the entire workload
            SumTask mainTask = new SumTask(array, 0, array.length);
            
            // 4. Submit the task to the pool and get the result
            long result = pool.invoke(mainTask);
            
            System.out.println("The sum is: " + result);
            // Verify: n*(n+1)/2 => 100000 * 100001 / 2 = 5000050000
        }
    }
}

/**
 * RecursiveTask is used when the task returns a result.
 * If no result is returned, use RecursiveAction.
 */
class SumTask extends RecursiveTask<Long> {

    // Threshold for splitting tasks (workload too small to split further)
    private static final int THRESHOLD = 10_000;
    
    private final int[] array;
    private final int start;
    private final int end;

    public SumTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        
        // Base case: if the task is small enough, compute directly
        if (length <= THRESHOLD) {
            return computeDirectly();
        } 
        
        // Recursive case: split the task into two subtasks
        int middle = start + (length / 2);
        
        System.out.println(Thread.currentThread().getName() + " - Splitting task into [" + start + ", " + middle + ") and [" + middle + ", " + end + ")");
        
        SumTask leftTask = new SumTask(array, start, middle);
        SumTask rightTask = new SumTask(array, middle, end);
        
        // Fork the left task (it runs asynchronously in the pool)
        leftTask.fork();
        
        // Compute the right task in the current thread
        Long rightResult = rightTask.compute();
        
        // Join the left task (wait for its result)
        Long leftResult = leftTask.join();
        
        // Combine results
        return leftResult + rightResult;
    }

    private Long computeDirectly() {
        System.out.println(Thread.currentThread().getName() + " - Computing directly for [" + start + ", " + end + ")");
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
        return sum;
    }
}
