# Java Streams API

The **Stream API** (introduced in Java 8) is used to process collections of objects in a declarative manner. A stream is a sequence of objects that supports various methods which can be pipelined to produce the desired result.

It resides in the `java.util.stream` package.

## Key Characteristics of Streams

1.  **Not a Data Structure:** A stream does not store data. It operates on a source data structure (like a Collection or Array) and produces pipelined data that we can use and perform specific operations on.
2.  **Does Not Modify the Source:** Operations performed on a stream do not alter the source. For example, filtering a Stream obtained from a List produces a new Stream without the filtered elements, rather than removing elements from the original List.
3.  **Lazy Evaluation:** Intermediate operations are always lazy. They are not executed until a terminal operation is invoked.
4.  **Consumable:** A stream should be operated on (invoking an intermediate or terminal stream operation) only once. A stream is consumed once a terminal operation is called, and you must generate a new stream to perform another operation.

## Stream Pipeline

A stream pipeline consists of:
1.  **A Source:** (e.g., Collection, Array, generator function, I/O channel).
2.  **Zero or more Intermediate Operations:** (e.g., `filter`, `map`). These transform a stream into another stream.
3.  **A Terminal Operation:** (e.g., `collect`, `forEach`, `reduce`). This produces a result or a side-effect and finishes the pipeline.

---

## 1. Creating Streams

You can create streams from various sources.

```java
// From a Collection (Most common)
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream = list.stream();

// From an Array
String[] arr = {"a", "b", "c"};
Stream<String> streamFromArray = Arrays.stream(arr);

// Using Stream.of()
Stream<Integer> streamOf = Stream.of(1, 2, 3, 4, 5);

// Infinite Streams (using generate or iterate)
Stream<Double> randoms = Stream.generate(Math::random).limit(5);
Stream<Integer> evens = Stream.iterate(0, n -> n + 2).limit(5);
```

---

## 2. Intermediate Operations

Intermediate operations return a new Stream. They are always **lazy**, meaning they don't execute until a terminal operation is called.

### `filter()`
Filters elements based on a given condition (Predicate).
```java
List<Integer> evens = numbers.stream()
                             .filter(n -> n % 2 == 0)
                             .collect(Collectors.toList());
```

### `map()`
Transforms each element into another object (Function).
```java
List<Integer> lengths = words.stream()
                             .map(String::length) // or w -> w.length()
                             .collect(Collectors.toList());
```

### `flatMap()`
Used to flatten a stream of collections into a single stream of elements.
```java
List<List<String>> nestedList = Arrays.asList(
    Arrays.asList("A", "B"), 
    Arrays.asList("C", "D")
);
List<String> flatList = nestedList.stream()
                                  .flatMap(Collection::stream)
                                  .collect(Collectors.toList());
// Result: ["A", "B", "C", "D"]
```

### `distinct()`
Removes duplicate elements (based on `equals()`).
```java
List<Integer> unique = numbers.stream().distinct().collect(Collectors.toList());
```

### `sorted()`
Sorts the stream elements. Can use natural ordering or a custom `Comparator`.
```java
List<String> sortedList = words.stream()
                               .sorted() // Natural order
                               .collect(Collectors.toList());

List<String> reverseSorted = words.stream()
                                  .sorted(Comparator.reverseOrder())
                                  .collect(Collectors.toList());
```

### `limit(n)` & `skip(n)`
- `limit(n)`: Truncates the stream to be no longer than `n` elements.
- `skip(n)`: Discards the first `n` elements of the stream.

---

## 3. Terminal Operations

Terminal operations produce a non-stream result (like a primitive, a Collection, or nothing). Once a terminal operation executes, the stream is considered **consumed**.

### `collect()`
The most common way to gather the stream results into a Collection, String, or Map.
```java
// To List
List<String> list = stream.collect(Collectors.toList()); 

// To Set
Set<String> set = stream.collect(Collectors.toSet());

// To Map (keyMapper, valueMapper)
Map<Integer, String> map = stream.collect(Collectors.toMap(String::length, s -> s));

// Joining Strings
String joined = words.stream().collect(Collectors.joining(", "));
```

*(Note: In Java 16+, you can just use `.toList()` directly on the stream instead of `.collect(Collectors.toList())`)*

### Advanced `Collectors` (Grouping & Partitioning)
The `Collectors` utility class provides many advanced methods for complex data aggregation.

#### `groupingBy()`
Groups elements based on a classification function and returns a `Map`.
```java
List<String> items = Arrays.asList("apple", "banana", "cherry", "apricot", "blueberry");

// Group by the first letter
Map<Character, List<String>> groupedByFirstLetter = items.stream()
    .collect(Collectors.groupingBy(s -> s.charAt(0)));
// Result: {a=[apple, apricot], b=[banana, blueberry], c=[cherry]}

// Grouping and counting (using a downstream collector)
Map<Character, Long> countByFirstLetter = items.stream()
    .collect(Collectors.groupingBy(s -> s.charAt(0), Collectors.counting()));
// Result: {a=2, b=2, c=1}
```

#### `partitioningBy()`
Partitions elements into two groups (`true` and `false`) based on a Predicate.
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

// Partition into even and odd
Map<Boolean, List<Integer>> partitioned = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
// Result: {false=[1, 3, 5], true=[2, 4, 6]}
```

### `forEach()`
Performs an action for each element of this stream.
```java
names.stream().forEach(System.out::println);
```

### `reduce()`
Performs a reduction on the elements of the stream, using an associative accumulation function, and returns an `Optional`. Useful for sums, concatenations, etc.
```java
// Summing all numbers
Optional<Integer> sum = numbers.stream().reduce((a, b) -> a + b);
// Or with an identity value (returns Integer, not Optional)
int total = numbers.stream().reduce(0, Integer::sum);
```

### `count()`, `min()`, `max()`
```java
long count = stream.count();
Optional<Integer> max = numbers.stream().max(Integer::compareTo);
Optional<Integer> min = numbers.stream().min(Integer::compareTo);
```

### Matching: `anyMatch()`, `allMatch()`, `noneMatch()`
Returns a boolean based on whether elements match a given Predicate. These operations **short-circuit** (they stop processing as soon as the answer is determined).
```java
boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
boolean allPositive = numbers.stream().allMatch(n -> n > 0);
boolean noNegatives = numbers.stream().noneMatch(n -> n < 0);
```

### `findFirst()` & `findAny()`
Returns an `Optional` describing the first or any element of the stream. `findAny()` is particularly useful in parallel streams where the "first" element doesn't matter and you just want the fastest result.
```java
Optional<String> first = stream.findFirst();
```

---

## Parallel Streams

Parallel streams divide the provided task into many and run them in different threads from the common `ForkJoinPool`, utilizing multiple cores of the processor.

```java
// Creating a parallel stream
long count = list.parallelStream()
                 .filter(s -> s.length() > 5)
                 .count();

// Or converting an existing stream
stream.parallel().filter(...);
```

**Warning:** Only use parallel streams when:
1. You have a massive dataset.
2. The operations are independent (no shared state/synchronization needed).
3. The order of processing does not matter.
(For small datasets or simple operations, the overhead of creating threads actually makes parallel streams *slower* than sequential streams).
