package ds_array;

import java.util.Arrays;

/**
 * Program to shift an array anti-clockwise (left shift) by k positions.
 */
public class LeftShiftArray {

    /**
     * Shifts the given array anti-clockwise by k positions using the Reversal Algorithm.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param arr The array to be shifted
     * @param k   The number of positions to shift
     */
    public static void leftShift(int[] arr, int k) {
        if (arr == null || arr.length <= 1) {
            return;
        }

        int n = arr.length;
        k = k % n; // In case k is greater than the length of the array
        if (k < 0) {
            k += n; // Handle negative shift if needed
        }

        // 1. Reverse the first k elements
        reverse(arr, 0, k - 1);
        
        // 2. Reverse the remaining n-k elements
        reverse(arr, k, n - 1);
        
        // 3. Reverse the whole array
        reverse(arr, 0, n - 1);
    }

    /**
     * Helper method to reverse a portion of an array
     */
    private static void reverse(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3, 4, 5};
        int k1 = 2;
        System.out.println("Original Array: " + Arrays.toString(arr1));
        leftShift(arr1, k1);
        System.out.println("After left shifting by " + k1 + ": " + Arrays.toString(arr1));

        int[] arr2 = {10, 20, 30, 40, 50, 60};
        int k2 = 4;
        System.out.println("\nOriginal Array: " + Arrays.toString(arr2));
        leftShift(arr2, k2);
        System.out.println("After left shifting by " + k2 + ": " + Arrays.toString(arr2));
        
        int[] arr3 = {1, 2, 3};
        int k3 = 5;
        System.out.println("\nOriginal Array: " + Arrays.toString(arr3));
        leftShift(arr3, k3);
        System.out.println("After left shifting by " + k3 + ": " + Arrays.toString(arr3));
    }
}
