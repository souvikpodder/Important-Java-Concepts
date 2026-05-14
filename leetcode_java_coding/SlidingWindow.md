# Sliding Window Problem Solving Pattern

The **Sliding Window** technique is a powerful algorithmic paradigm used to solve problems that deal with contiguous subarrays or substrings of a given array or string.

## What is a Sliding Window?
Imagine a "window" of a certain size moving from the beginning to the end of an array or string. As the window slides, we keep track of some condition (e.g., maximum sum, unique characters) without needing to recompute the result from scratch for each new window position. This helps reduce the time complexity significantly, often from $O(n^2)$ or $O(n^3)$ to $O(n)$.

## When to use it?
You should think of the Sliding Window technique when you see problems asking for:
- Maximum, minimum, longest, or shortest something.
- **Contiguous** subarray or substring.
- Given an array or a string.

## Types of Sliding Window Problems
1. **Fixed Size Window:** The size of the window `k` is constant. We move the window one step at a time, removing the element that slips out of the window and adding the newly included element.
2. **Variable Size Window:** The size of the window can grow or shrink depending on a certain condition. We usually use two pointers (`left` and `right`) to expand and contract the window.

---

### Pattern 1: Fixed Size Window
**Problem example:** Find the maximum sum of any contiguous subarray of size `k`.

**General Template:**
```java
int fixedWindow(int[] arr, int k) {
    int max = 0;
    int currentSum = 0;
    
    // 1. Process the first window of size k
    for (int i = 0; i < k; i++) {
        currentSum += arr[i];
    }
    max = currentSum;
    
    // 2. Slide the window from k to end of the array
    for (int i = k; i < arr.length; i++) {
        // Add the rightmost element, remove the leftmost element
        currentSum += arr[i] - arr[i - k];
        max = Math.max(max, currentSum);
    }
    
    return max;
}
```

### Pattern 2: Variable Size Window
**Problem example:** Find the length of the longest substring without repeating characters.

**General Template:**
```java
int variableWindow(int[] arr, int k) {
    int left = 0, right = 0;
    int maxLen = 0;
    // Data structure to maintain window state (e.g., sum, hash map, etc.)
    int currentSum = 0; 
    
    while (right < arr.length) {
        // 1. Add right element to window state
        currentSum += arr[right]; // Example action
        
        // 2. If window violates the condition, shrink from the left
        while (currentSum > k) { // Replace condition as per problem
            currentSum -= arr[left]; // Remove element leaving window
            left++;
        }
        
        // 3. Update the answer based on valid window
        maxLen = Math.max(maxLen, right - left + 1);
        
        // 4. Move right pointer
        right++;
    }
    
    return maxLen;
}
```

---

## Similar Problem Questions / Practice List
Here is a list of common questions patterned by difficulty:

### Fixed Window Problems:
1. **Maximum Average Subarray I** (Easy) - Find the contiguous subarray of given length `k` that has the maximum average value.
2. **Diet Plan Performance** (Easy) - Sliding window for summing elements within `k` consecutive days.
3. **Number of Sub-arrays of Size K and Average Greater than or Equal to Threshold** (Medium)
4. **Permutation in String** (Medium) - Finding a specific permutation format within a window.
5. **Find All Anagrams in a String** (Medium) - Window size is fixed to the length of the target string.
6. **Sliding Window Maximum** (Hard) - Find the maximum in every window of size `k` (Often solved efficiently using a Deque).

### Variable Window Problems:
1. **Minimum Size Subarray Sum** (Medium) - Find the minimal length of a contiguous subarray of which the sum is greater than or equal to a target.
2. **Longest Substring Without Repeating Characters** (Medium) - Find the length of the longest substring without repeating characters.
3. **Longest Repeating Character Replacement** (Medium) - Replace at most `k` characters to get the longest substring containing the same letter.
4. **Max Consecutive Ones III** (Medium) - Find the longest contiguous subarray containing only `1`s if you can change at most `k` `0`s to `1`s.
5. **Fruit Into Baskets** (Medium) - Longest contiguous subarray with at most 2 distinct elements.
6. **Subarrays with K Different Integers** (Hard) - Given an integer array, return the number of good subarrays where the number of different integers is exactly `k`.
7. **Minimum Window Substring** (Hard) - Find the minimum window in string `S` which will contain all the characters in string `T` in complexity $O(n)$.

---

### Key Takeaways
- Check if the problem asks for a contiguous sequence (substring, subarray).
- Check if the window size is fixed (given `k` length) or variable.
- For fixed windows, advance `right` and increment `left` simultaneously when the size reaches `k`.
- For variable windows, dynamically expand `right` and shrink `left` using an inner `while` loop when constraints are broken.
- State-tracking data structures (such as `currentSum` variable, frequency `HashMap`, `HashSet`, or `Deque` for monotonic properties) play a crucial role.
