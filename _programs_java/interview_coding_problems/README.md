# Java Coding & Java 8 Stream Problem Statements

This document contains a comprehensive collection of core Java and Java 8 Streams coding problem statements, categorized by topic, complete with input/output examples, constraints, and edge cases.

---

## Quick Navigation Table

| # | Problem Title | Category / Tags | Difficulty |
|---|---|---|---|
| 1 | [Find Second Highest Word Length](#problem-1-find-second-highest-word-length-in-a-sentence) | Java 8 Streams, Strings | Simple |
| 2 | [Sum of Current & Previous k-1 Elements](#problem-2-calculate-sum-of-the-current-element-and-up-to-the-previous-k-1-elements) | Arrays, Sliding Window | Medium |
| 3 | [Find Missing Number in Sequence (1 to n)](#problem-3-find-the-missing-number-in-a-sequence-from-1-to-n) | Arrays, Math, Bitwise XOR | Simple |
| 4 | [Filter Valid Integers from List of Strings](#problem-4-filter-valid-integers-from-a-list-of-strings) | Java 8 Streams, Parsing, Regex | Simple / Medium |
| 5 | [Group Words by First Letter](#problem-5-group-words-by-first-letter) | Java 8 Streams, Collectors | Simple / Medium |
| 6 | [Reverse Array of Characters](#problem-6-reverse-the-array-of-characters) | Arrays, Two Pointers, In-Place | Simple |
| 7 | [Filter Palindrome Words](#problem-7-filter-palindrome-words) | Java 8 Streams, String Algorithms | Simple |
| 8 | [Right Shift All Zeros in Integer Array](#problem-8-right-shift-all-the-zeros-in-an-integer-array) | Arrays, Two Pointers, In-Place | Simple / Medium |
| 9 | [Convert Sentence to Hashtag](#problem-9-convert-sentence-to-hashtag) | Java 8 Streams, String Formatting | Simple / Medium |
| 10 | [Find Duplicate Elements](#problem-10-find-duplicate-elements-using-java-8-streams) | Java 8 Streams, Collections | Simple / Medium |
| 11 | [Merge Two Sorted Integer Arrays](#problem-11-merge-two-sorted-integer-arrays-into-a-single-sorted-array) | Arrays, Two Pointers, Merge Sort | Simple / Medium |

---

## Problem 1: Find Second Highest Word Length in a Sentence

### Category
`Java 8 Streams` | `Strings` | `Sorting`

### Problem Statement
Given a sentence consisting of space-separated words, find the **second highest distinct word length** using the **Java 8 Stream API**. If fewer than two distinct word lengths exist, handle the scenario appropriately (e.g., return empty or not found). Optionally, also identify all words that match this second highest length.

### Input / Output
- **Input:** `String sentence`
- **Output:** `Optional<Integer>` (or integer value representing the 2nd highest length) and matching words `List<String>`.

### Example 1
```text
Input: "The quick brown fox jumps over the lazy dog and explores wonderland"
Word Lengths:
  - "The" (3), "quick" (5), "brown" (5), "fox" (3), "jumps" (5)
  - "over" (4), "the" (3), "lazy" (4), "dog" (3), "and" (3)
  - "explores" (8), "wonderland" (10)
Distinct Lengths (descending order): [10, 8, 5, 4, 3]

Output:
  2nd Highest Length: 8
  Matching Word(s): ["explores"]
```

### Example 2
```text
Input: "Java 8 Stream API provides powerful functional operations"
Distinct Lengths (descending): [10 ("functional", "operations"), 8 ("powerful", "provides"), 6 ("Stream"), 4 ("Java"), 3 ("API"), 1 ("8")]

Output:
  2nd Highest Length: 8
  Matching Word(s): ["provides", "powerful"]
```

### Constraints & Edge Cases
- Sentence may contain multiple consecutive spaces or leading/trailing spaces.
- If all words have the same length (e.g., `"cat dog bat"`), there is no 2nd highest length.
- Empty or `null` string input should be safely handled.

---

## Problem 2: Calculate Sum of the Current Element and Up to the Previous k-1 Elements

### Category
`General Algorithms` | `Arrays` | `Sliding Window` | `Prefix Sum`

### Problem Statement
Given an integer array `arr` and a positive integer `k`, for each index `i` (from `0` to `arr.length - 1`), calculate the sum of the current element `arr[i]` and up to the previous `k - 1` elements. 
In other words, calculate the sum of the sliding window of size at most `k` ending at index `i` (from index `max(0, i - k + 1)` up to index `i`).

### Input / Output
- **Input:** `int[] arr`, `int k`
- **Output:** `int[] result` of the same length as `arr`

### Example 1
```text
Input: arr = [2, 1, 3, 4, 5], k = 3

Calculations per index:
- Index 0: sum([2]) = 2
- Index 1: sum([2, 1]) = 3
- Index 2: sum([2, 1, 3]) = 6
- Index 3: sum([1, 3, 4]) = 8   (previous k-1=2 elements: [1, 3] + current: 4)
- Index 4: sum([3, 4, 5]) = 12  (previous k-1=2 elements: [3, 4] + current: 5)

Output: [2, 3, 6, 8, 12]
```

### Example 2
```text
Input: arr = [10, 20, 30, 40], k = 1
Output: [10, 20, 30, 40] (only current element)

Input: arr = [1, 2, 3, 4], k = 5 (where k >= arr.length)
Output: [1, 3, 6, 10] (cumulative sum)
```

### Constraints & Complexity
- `1 <= arr.length <= 10^6`
- `k >= 1`
- **Expected Time Complexity:** $O(N)$ using running window sum or prefix sum.
- **Expected Space Complexity:** $O(1)$ auxiliary (or $O(N)$ for the result array).

---

## Problem 3: Find the Missing Number in a Sequence from 1 to n

### Category
`Simple Problem Solving` | `Math` | `Bit Manipulation (XOR)` | `Java 8 Streams`

### Problem Statement
You are given an array of `n - 1` distinct integers taken from the range `1` to `n` (inclusive). One number from the sequence is missing. Write an algorithm to find the missing integer.

### Input / Output
- **Input:** `int[] arr`, `int n`
- **Output:** `int missingNumber`

### Example 1
```text
Input: arr = [1, 2, 4, 6, 3, 7, 8], n = 8
Output: 5
Explanation: The full range 1..8 is [1, 2, 3, 4, 5, 6, 7, 8]. The number 5 is missing.
```

### Example 2
```text
Input: arr = [2, 3, 1, 5], n = 5
Output: 4
```

### Constraints & Variations
- Array contains distinct integers in the range `[1, n]`.
- Array is not necessarily sorted.
- **Common Approaches to Compare:**
  1. **Gauss Formula:** $\text{Total Sum} = \frac{n \times (n+1)}{2}$, Missing = $\text{Total Sum} - \sum(\text{arr})$.
  2. **XOR Method:** $XOR(1..n) \oplus XOR(\text{arr elements})$ (prevents potential integer overflow).
  3. **Java 8 Streams:** `IntStream.rangeClosed(1, n).sum() - Arrays.stream(arr).sum()`.

---

## Problem 4: Filter Valid Integers from a List of Strings

### Category
`Java 8 Streams` | `Regular Expressions` | `Exception Handling` | `Type Parsing`

### Problem Statement
Given a `List<String>` containing a mix of valid integer representations, floating-point numbers, alphanumeric strings, special characters, and out-of-range values, filter out only the valid integers and store them in a `List<Integer>`.

### Input / Output
- **Input:** `List<String> inputList`
- **Output:** `List<Integer> validIntegers`

### Example 1
```text
Input: ["10", "abc", "-25", "3.14", "100", "0", "45a", "999", "++5", "--3", "null", ""]
Output: [10, -25, 100, 0, 999]
```

### Example 2
```text
Input: ["-0", "2147483647", "-2147483648", "999999999999999999", "12 3"]
Output: [0, 2147483647, -2147483648]
Explanation: "999999999999999999" exceeds 32-bit Integer range and is excluded (or parsed to BigInteger if specified).
```

### Key Considerations
- Positive and negative signs (e.g., `"-42"`, `"+15"`).
- Rejecting decimals (`"3.14"`), whitespace (`" 12 "`), non-digits (`"12a"`), and overflow values.
- Idiomatic Java 8 Stream implementation (`flatMap`, `filter` with regex, or custom parser method).

---

## Problem 5: Group Words by First Letter

### Category
`Java 8 Streams` | `Collections` | `Collectors.groupingBy` | `Map Interface`

### Problem Statement
Given a collection or sentence of words, group the words by their starting character into a `Map<Character, List<String>>`. The grouping should support case-insensitive grouping (or uppercase/lowercase normalization) and preserve word order within each group.

### Input / Output
- **Input:** `List<String> words` (or `String sentence`)
- **Output:** `Map<Character, List<String>>`

### Example 1
```text
Input: ["apple", "banana", "avocado", "blueberry", "cherry", "apricot"]

Output:
{
  'a' -> ["apple", "avocado", "apricot"],
  'b' -> ["banana", "blueberry"],
  'c' -> ["cherry"]
}
```

### Example 2 (From Sentence with Mixed Casing)
```text
Input: "The quick brown fox jumps over the lazy dog"

Output (normalized to lowercase keys):
{
  'b' -> ["brown"],
  'd' -> ["dog"],
  'f' -> ["fox"],
  'j' -> ["jumps"],
  'l' -> ["lazy"],
  'o' -> ["over"],
  'q' -> ["quick"],
  't' -> ["The", "the"]
}
```

### Extensions
- Group and collect as `Map<Character, Set<String>>` (unique words per character).
- Group and count word occurrences: `Map<Character, Long>`.

---

## Problem 6: Reverse the Array of Characters

### Category
`General Problem` | `Arrays` | `Two Pointers` | `In-Place Manipulation`

### Problem Statement
Given an array of characters (`char[]`), reverse the array **in-place** with $O(1)$ extra memory space and $O(N)$ time complexity.

### Input / Output
- **Input:** `char[] arr`
- **Output:** Modified `char[] arr` in-place

### Example 1
```text
Input: ['h', 'e', 'l', 'l', 'o']
Output: ['o', 'l', 'l', 'e', 'h']
```

### Example 2
```text
Input: ['J', 'a', 'v', 'a', '8']
Output: ['8', 'a', 'v', 'a', 'J']
```

### Constraints & Edge Cases
- `0 <= arr.length <= 10^6`
- Memory complexity must strictly be $O(1)$ (no allocating a new array).
- Edge cases: empty array `[]`, single character `['a']`, even vs odd length arrays.

---

## Problem 7: Filter Palindrome Words

### Category
`Java 8 Streams` | `Strings` | `Predicate Filtering`

### Problem Statement
Given a sentence or a list of words, filter out and collect only the words that are **palindromes** (words that read the same backwards as forwards, such as `"madam"`, `"racecar"`, `"level"`).

### Input / Output
- **Input:** `List<String> words` (or `String sentence`)
- **Output:** `List<String> palindromeWords`

### Example 1
```text
Input: ["madam", "racecar", "apple", "noon", "java", "radar", "level", "world", "civic", "kayak"]
Output: ["madam", "racecar", "noon", "radar", "level", "civic", "kayak"]
```

### Example 2
```text
Input: "Madam in Eden I'm Adam, did noon refer to radar?"
(After cleaning punctuation & case normalization)
Output Palindrome Words: ["madam", "did", "noon", "refer", "radar"]
```

### Constraints & Considerations
- Case sensitivity (e.g., `"Madam"` vs `"madam"`).
- Single letter words (e.g., `"a"`, `"I"`) are valid palindromes.
- Ignore punctuation / special characters when extracting words.

---

## Problem 8: Right Shift All the Zeros in an Integer Array

### Category
`General Algorithms` | `Arrays` | `Two Pointers` | `In-Place Manipulation`

### Problem Statement
Given an integer array `nums`, shift/move all `0`s to the right end (end of the array) while maintaining the relative order of all non-zero elements. You must perform this **in-place** without making a copy of the entire array.

### Input / Output
- **Input:** `int[] nums`
- **Output:** `int[] nums` (modified in-place)

### Example 1
```text
Input: [0, 1, 0, 3, 12]
Output: [1, 3, 12, 0, 0]
```

### Example 2
```text
Input: [0, 0, 0, 1, 0, 2, 0, 3]
Output: [1, 2, 3, 0, 0, 0, 0, 0]
```

### Example 3
```text
Input: [1, 2, 3, 4]
Output: [1, 2, 3, 4] (No zeros present)
```

### Constraints & Complexity
- `1 <= nums.length <= 10^5`
- **Time Complexity:** $O(N)$ (single or two-pass).
- **Space Complexity:** $O(1)$ auxiliary space.

---

## Problem 9: Convert Sentence to Hashtag

### Category
`Java 8 Streams` | `Strings` | `Collectors` | `String Transformation`

### Problem Statement
Given an input sentence, convert it into a **Hashtag** string:
1. Every word must start with an uppercase letter, with remaining characters in lowercase (PascalCase / TitleCase).
2. All spaces and punctuation separating words must be removed.
3. The resulting string must be prefixed with `#`.
4. If the input is empty or null, handle gracefully (e.g., return `""` or `"#"`).

### Input / Output
- **Input:** `String sentence`
- **Output:** `String hashtag`

### Example 1
```text
Input: "hello world welcome to java"
Output: "#HelloWorldWelcomeToJava"
```

### Example 2
```text
Input: "java 8 stream api and functional programming"
Output: "#Java8StreamApiAndFunctionalProgramming"
```

### Example 3
```text
Input: "   multiple    irregular   SPACES   and   MiXeD   cAsE  "
Output: "#MultipleIrregularSpacesAndMixedCase"
```

---

## Problem 10: Find Duplicate Elements using Java 8 Streams

### Category
`Java 8 Streams` | `Collections` | `Frequency Count` | `Set Interface`

### Problem Statement
Given a list or stream of elements (e.g., integers or strings) containing duplicate values, find and return all elements that appear **more than once** using Java 8 Streams.

### Input / Output
- **Input:** `List<T> list` (e.g., `List<Integer>` or `List<String>`)
- **Output:** `Set<T>` or `List<T>` containing only duplicate items, or `Map<T, Long>` with element frequencies.

### Example 1 (Integer List)
```text
Input: [10, 20, 30, 40, 20, 50, 10, 60, 10, 70, 80, 70]
Output (Unique Duplicates): [10, 20, 70]

With Frequency Map:
{
  10 -> 3 occurrences,
  20 -> 2 occurrences,
  70 -> 2 occurrences
}
```

### Example 2 (String List)
```text
Input: ["apple", "banana", "orange", "apple", "grape", "banana", "kiwi"]
Output: ["apple", "banana"]
```

### Common Stream Approaches
1. **Grouping and Counting:** `Collectors.groupingBy(Function.identity(), Collectors.counting())` followed by filtering `entry.getValue() > 1`.
2. **Set Addition Filter:** Using `!set.add(element)` inside `filter()` with `.distinct()`.
3. **Collections Frequency:** `filter(x -> Collections.frequency(list, x) > 1).distinct()`.

---

## Problem 11: Merge Two Sorted Integer Arrays into a Single Sorted Array

### Category
`General Algorithms` | `Arrays` | `Two Pointers` | `Merge Sort`

### Problem Statement
Given two integer arrays `arr1` of size $M$ and `arr2` of size $N$, both sorted in non-decreasing order, merge them into a single sorted array of size $M + N$ in linear $O(M + N)$ time complexity.

### Input / Output
- **Input:** `int[] arr1`, `int[] arr2`
- **Output:** `int[] mergedArray` of size `arr1.length + arr2.length`

### Example 1
```text
Input:
  arr1 = [1, 3, 5, 7, 9]
  arr2 = [2, 4, 6, 8, 10]

Output:
  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
```

### Example 2 (Different Sizes & Duplicate Elements)
```text
Input:
  arr1 = [1, 2, 3, 8, 9]
  arr2 = [2, 5, 6]

Output:
  [1, 2, 2, 3, 5, 6, 8, 9]
```

### Constraints & Complexity
- `arr1` and `arr2` are already sorted.
- **Time Complexity:** $O(M + N)$ using two pointers.
- **Space Complexity:** $O(M + N)$ for the new merged array (or $O(1)$ auxiliary memory).

---
