# Collections Framework in Java

The java.util package contains all the classes and interfaces for Collection framework.

**Map :**
An object that maps keys to values. A map is not ordered and cannot contain duplicate keys (but can contain duplicate values). Each key can map to at most one value.
    
**Collection :**
A collection represents a group of objects, known as its elements. The JDK provides implementations of more specific subinterfaces like Set and List.

**List :**
A list is an ordered list of objects, where the same object may well appear more than once. For example: [1, 7, 1, 3, 1, 1, 1, 5]. It makes sense to talk about the "third element" in a list. You can add an element anywhere in the list, change an element anywhere in the list, or remove an element from any position in the list.

**Queue :**
A queue is also ordered, but you'll only ever touch elements at one end. All elements get inserted at the "end" and removed from the "beginning" (or head) of the queue. You can find out how many elements are in the queue, but you can't find out what, say, the "third" element is. You'll see it when you get there.

**Set :**
A set is not ordered and cannot contain duplicates. Any given object either is or isn't in the set. {7, 5, 3, 1} is the exact same set as {1, 7, 3, 5}. You again can't ask for the "third" element or even the "first" element, since they are not in any particular order. You can add or remove elements, and you can find out if a certain element exists.

<table class="alt">
<tbody>
<tr><th> </th><th>List</th><th>Set</th><th>Queue</th><th>Map</th></tr>
<tr><th>Order</th><th>Yes</th><th>No</th><th>Yes</th><th>No</th></tr>
<tr><th>Duplicates</th><th>Yes</th><th>No</th><th>Yes</th><th>No (Allow duplicate values not keys)</th></tr>
<tr><th>Null Values</th><th>Yes</th><th>Single Null</th><th>Yes (LinkedList Queue). No (Priority Queue).</th><th>Single null key and many null values</th></tr>
</tbody></table>

## Hierarchy of Collection Interface :

![collection-hierarchy](https://user-images.githubusercontent.com/2780145/34073817-62945de4-e2c8-11e7-820b-84f9dad32af3.png)

### Collection Examples with Detailed Explanations

---

## LIST Implementations

Lists are ordered collections that allow duplicate elements. You can access elements by their index.

### 1. ArrayList
**Best for:** Random access, reading data frequently. Backed by dynamic array.

```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListExample {
    public static void main(String[] args) {
        // Creating ArrayList
        List<String> fruits = new ArrayList<>();
        
        // Adding elements - O(1) amortized
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Apple");  // Duplicates allowed!
        
        // Access by index - O(1) FAST!
        String first = fruits.get(0);  // "Apple"
        
        // Insert at specific index - O(n) slow, shifts elements
        fruits.add(1, "Mango");  // [Apple, Mango, Banana, Orange, Apple]
        
        // Update element - O(1)
        fruits.set(2, "Grapes");  // Replace Banana with Grapes
        
        // Remove by index - O(n) slow, shifts elements
        fruits.remove(0);  // Removes Apple
        
        // Remove by object - O(n)
        fruits.remove("Orange");
        
        // Check if contains - O(n)
        boolean hasApple = fruits.contains("Apple");
        
        // Size
        int size = fruits.size();
        
        // Iterate - Multiple ways
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
        
        // Using forEach (Java 8+)
        fruits.forEach(System.out::println);
        
        // Using iterator
        Iterator<String> it = fruits.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        
        // Convert to array
        String[] arr = fruits.toArray(new String[0]);
        
        // Clear all elements
        fruits.clear();
    }
}
```

**When to use:** When you need fast random access (get by index) and mostly add elements at the end.

---

### 2. LinkedList
**Best for:** Frequent insertions/deletions at beginning or middle. Implements both List and Deque.

```java
import java.util.LinkedList;
import java.util.List;

public class LinkedListExample {
    public static void main(String[] args) {
        // LinkedList as List
        LinkedList<String> names = new LinkedList<>();
        
        // Add elements
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        
        // Add at beginning - O(1) FAST!
        names.addFirst("Zara");
        
        // Add at end - O(1) FAST!
        names.addLast("David");
        
        // Access first/last - O(1)
        String first = names.getFirst();  // "Zara"
        String last = names.getLast();    // "David"
        
        // Access by index - O(n) SLOW! Must traverse
        String middle = names.get(2);
        
        // Remove first/last - O(1) FAST!
        names.removeFirst();
        names.removeLast();
        
        // LinkedList as Queue (FIFO)
        names.offer("Eve");     // Add to end
        String head = names.poll();  // Remove from front
        
        // LinkedList as Stack (LIFO)
        names.push("Frank");    // Add to front
        String top = names.pop();    // Remove from front
        
        // Peek without removing
        String peekFirst = names.peekFirst();
        String peekLast = names.peekLast();
        
        // Iterate in reverse
        Iterator<String> descIt = names.descendingIterator();
        while (descIt.hasNext()) {
            System.out.println(descIt.next());
        }
    }
}
```

**When to use:** When you frequently add/remove elements from beginning or need Queue/Deque functionality.

---

### 3. Vector (Legacy - Thread-Safe)
**Best for:** Thread-safe list operations. Similar to ArrayList but synchronized.

```java
import java.util.Vector;
import java.util.Enumeration;

public class VectorExample {
    public static void main(String[] args) {
        // Vector is synchronized (thread-safe)
        Vector<Integer> numbers = new Vector<>();
        
        // Add elements - same as ArrayList
        numbers.add(10);
        numbers.add(20);
        numbers.add(30);
        
        // Vector-specific: addElement (legacy method)
        numbers.addElement(40);
        
        // Capacity management
        numbers.ensureCapacity(100);  // Pre-allocate space
        int capacity = numbers.capacity();
        
        // Access elements
        Integer first = numbers.firstElement();
        Integer last = numbers.lastElement();
        Integer atIndex = numbers.elementAt(1);
        
        // Legacy iteration using Enumeration
        Enumeration<Integer> e = numbers.elements();
        while (e.hasMoreElements()) {
            System.out.println(e.nextElement());
        }
        
        // Modern iteration works too
        for (Integer num : numbers) {
            System.out.println(num);
        }
    }
}
```

**When to use:** In legacy code or when you need a thread-safe list (prefer `Collections.synchronizedList()` or `CopyOnWriteArrayList` for new code).

---

### 4. Stack (Legacy - LIFO)
**Best for:** Last-In-First-Out operations. Extends Vector.

```java
import java.util.Stack;

public class StackExample {
    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        
        // Push elements onto stack - O(1)
        stack.push("First");
        stack.push("Second");
        stack.push("Third");
        // Stack: [First, Second, Third] <- top
        
        // Peek at top without removing - O(1)
        String top = stack.peek();  // "Third"
        
        // Pop (remove) from top - O(1)
        String removed = stack.pop();  // "Third"
        // Stack: [First, Second] <- top
        
        // Check if empty
        boolean isEmpty = stack.empty();
        
        // Search for element (returns 1-based position from top)
        int position = stack.search("First");  // 2 (Second is 1, First is 2)
        // Returns -1 if not found
        
        // Pop all elements
        while (!stack.empty()) {
            System.out.println(stack.pop());
        }
    }
}
```

**Note:** For new code, prefer `ArrayDeque` as a stack - it's faster and not synchronized.

```java
// Modern alternative
Deque<String> modernStack = new ArrayDeque<>();
modernStack.push("First");
modernStack.push("Second");
String top = modernStack.pop();
```

---

## SET Implementations

Sets are collections that do NOT allow duplicate elements.

### 5. HashSet
**Best for:** Fast operations when order doesn't matter. Uses HashMap internally.

```java
import java.util.HashSet;
import java.util.Set;

public class HashSetExample {
    public static void main(String[] args) {
        Set<String> countries = new HashSet<>();
        
        // Add elements - O(1) average
        countries.add("India");
        countries.add("USA");
        countries.add("Japan");
        countries.add("India");  // Duplicate - NOT added! Returns false
        
        System.out.println(countries);  // Order NOT guaranteed!
        // Could print: [USA, Japan, India] or any order
        
        // Check if contains - O(1) average
        boolean hasIndia = countries.contains("India");  // true
        
        // Remove - O(1) average
        countries.remove("USA");
        
        // Size
        int size = countries.size();
        
        // Iterate (order not predictable)
        for (String country : countries) {
            System.out.println(country);
        }
        
        // Set operations
        Set<String> asianCountries = new HashSet<>();
        asianCountries.add("India");
        asianCountries.add("Japan");
        asianCountries.add("China");
        
        // Union
        Set<String> union = new HashSet<>(countries);
        union.addAll(asianCountries);
        
        // Intersection
        Set<String> intersection = new HashSet<>(countries);
        intersection.retainAll(asianCountries);
        
        // Difference
        Set<String> difference = new HashSet<>(countries);
        difference.removeAll(asianCountries);
    }
}
```

**When to use:** When you need fast uniqueness checks and don't care about order.

---

### 6. LinkedHashSet
**Best for:** Maintaining insertion order with uniqueness.

```java
import java.util.LinkedHashSet;
import java.util.Set;

public class LinkedHashSetExample {
    public static void main(String[] args) {
        // Maintains insertion order!
        Set<String> colors = new LinkedHashSet<>();
        
        colors.add("Red");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Red");  // Duplicate - not added
        
        System.out.println(colors);  // Always: [Red, Green, Blue]
        
        // All operations same as HashSet, but order is preserved
        for (String color : colors) {
            System.out.println(color);  // Red, Green, Blue (in order)
        }
        
        // Slightly slower than HashSet due to maintaining linked list
        // but faster than TreeSet
    }
}
```

**When to use:** When you need unique elements AND want to maintain insertion order.

---

### 7. TreeSet
**Best for:** Sorted unique elements. Implements NavigableSet.

```java
import java.util.TreeSet;
import java.util.NavigableSet;

public class TreeSetExample {
    public static void main(String[] args) {
        // Elements are automatically sorted!
        TreeSet<Integer> numbers = new TreeSet<>();
        
        numbers.add(50);
        numbers.add(20);
        numbers.add(80);
        numbers.add(10);
        numbers.add(60);
        
        System.out.println(numbers);  // [10, 20, 50, 60, 80] - Sorted!
        
        // Navigation methods - O(log n)
        Integer first = numbers.first();    // 10
        Integer last = numbers.last();      // 80
        
        // Lower/Higher (exclusive)
        Integer lower = numbers.lower(50);   // 20 (largest < 50)
        Integer higher = numbers.higher(50); // 60 (smallest > 50)
        
        // Floor/Ceiling (inclusive)
        Integer floor = numbers.floor(50);     // 50 (largest <= 50)
        Integer ceiling = numbers.ceiling(45); // 50 (smallest >= 45)
        
        // Poll (remove) first/last
        Integer pollFirst = numbers.pollFirst();  // Removes and returns 10
        Integer pollLast = numbers.pollLast();    // Removes and returns 80
        
        // Range views (subsets)
        NavigableSet<Integer> subset = numbers.subSet(20, true, 60, true);
        // [20, 50, 60]
        
        NavigableSet<Integer> headSet = numbers.headSet(50, false);  // < 50
        NavigableSet<Integer> tailSet = numbers.tailSet(50, true);   // >= 50
        
        // Descending order
        NavigableSet<Integer> descending = numbers.descendingSet();
        
        // Custom sorting with Comparator
        TreeSet<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        names.add("zebra");
        names.add("Apple");
        names.add("banana");
        System.out.println(names);  // [Apple, banana, zebra]
    }
}
```

**When to use:** When you need unique elements in sorted order or need range queries.

---

## QUEUE Implementations

Queues typically follow FIFO (First-In-First-Out) ordering.

### 8. PriorityQueue
**Best for:** Processing elements by priority (natural order or custom comparator).

```java
import java.util.PriorityQueue;
import java.util.Comparator;

public class PriorityQueueExample {
    public static void main(String[] args) {
        // Min-heap by default (smallest first)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        minHeap.add(50);
        minHeap.add(20);
        minHeap.add(80);
        minHeap.add(10);
        
        // Peek at smallest - O(1)
        Integer smallest = minHeap.peek();  // 10
        
        // Poll removes smallest - O(log n)
        while (!minHeap.isEmpty()) {
            System.out.println(minHeap.poll());  // 10, 20, 50, 80
        }
        
        // Max-heap (largest first)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.add(50);
        maxHeap.add(20);
        maxHeap.add(80);
        
        System.out.println(maxHeap.poll());  // 80
        System.out.println(maxHeap.poll());  // 50
        
        // Custom priority with objects
        PriorityQueue<Task> taskQueue = new PriorityQueue<>(
            Comparator.comparing(Task::getPriority)
        );
        taskQueue.add(new Task("Low priority task", 3));
        taskQueue.add(new Task("High priority task", 1));
        taskQueue.add(new Task("Medium priority task", 2));
        
        // Processes in priority order: 1, 2, 3
        while (!taskQueue.isEmpty()) {
            System.out.println(taskQueue.poll().getName());
        }
    }
}

class Task {
    private String name;
    private int priority;
    
    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
    
    public String getName() { return name; }
    public int getPriority() { return priority; }
}
```

**When to use:** Task scheduling, finding min/max elements, Dijkstra's algorithm.

---

### 9. ArrayDeque
**Best for:** Fast double-ended queue. Better than Stack and LinkedList for most cases.

```java
import java.util.ArrayDeque;
import java.util.Deque;

public class ArrayDequeExample {
    public static void main(String[] args) {
        Deque<String> deque = new ArrayDeque<>();
        
        // Add to front and back - O(1)
        deque.addFirst("A");    // [A]
        deque.addLast("B");     // [A, B]
        deque.addFirst("Z");    // [Z, A, B]
        deque.addLast("C");     // [Z, A, B, C]
        
        // Peek front and back - O(1)
        String first = deque.peekFirst();  // "Z"
        String last = deque.peekLast();    // "C"
        
        // Remove from front and back - O(1)
        String removedFirst = deque.pollFirst();  // "Z"
        String removedLast = deque.pollLast();    // "C"
        // Deque now: [A, B]
        
        // Use as STACK (LIFO)
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);   // addFirst
        stack.push(2);
        stack.push(3);
        System.out.println(stack.pop());  // 3 (removeFirst)
        
        // Use as QUEUE (FIFO)
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(1);  // addLast
        queue.offer(2);
        queue.offer(3);
        System.out.println(queue.poll());  // 1 (removeFirst)
        
        // offer vs add: offer returns false on failure, add throws exception
        // poll vs remove: poll returns null on empty, remove throws exception
        // peek vs element: peek returns null on empty, element throws exception
    }
}
```

**When to use:** As a faster replacement for Stack or LinkedList when used as queue/deque.

---

## MAP Implementations

Maps store key-value pairs. Keys must be unique.

### 10. HashMap
**Best for:** Fast key-value storage when order doesn't matter.

```java
import java.util.HashMap;
import java.util.Map;

public class HashMapExample {
    public static void main(String[] args) {
        Map<String, Integer> ages = new HashMap<>();
        
        // Put key-value pairs - O(1) average
        ages.put("Alice", 25);
        ages.put("Bob", 30);
        ages.put("Charlie", 35);
        ages.put("Alice", 26);  // Updates existing key!
        
        // Get value by key - O(1) average
        Integer aliceAge = ages.get("Alice");  // 26
        Integer unknownAge = ages.get("Unknown");  // null
        
        // Get with default
        Integer age = ages.getOrDefault("Unknown", 0);  // 0
        
        // Check if key/value exists - O(1)
        boolean hasAlice = ages.containsKey("Alice");
        boolean has25 = ages.containsValue(25);
        
        // Remove - O(1) average
        ages.remove("Bob");
        ages.remove("Charlie", 35);  // Remove only if value matches
        
        // Size and empty check
        int size = ages.size();
        boolean isEmpty = ages.isEmpty();
        
        // Iterate over entries
        for (Map.Entry<String, Integer> entry : ages.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
        // Iterate over keys
        for (String key : ages.keySet()) {
            System.out.println(key);
        }
        
        // Iterate over values
        for (Integer value : ages.values()) {
            System.out.println(value);
        }
        
        // Java 8+ forEach
        ages.forEach((k, v) -> System.out.println(k + " -> " + v));
        
        // Compute methods (Java 8+)
        ages.putIfAbsent("David", 40);  // Only adds if key doesn't exist
        ages.computeIfAbsent("Eve", k -> 45);  // Compute value if absent
        ages.computeIfPresent("Alice", (k, v) -> v + 1);  // Update if present
        ages.compute("Alice", (k, v) -> v == null ? 1 : v + 1);  // Both cases
        
        // Merge
        ages.merge("Alice", 1, Integer::sum);  // Add 1 to Alice's age
    }
}
```

**When to use:** Most common map choice. Fast operations, no order guarantee.

---

### 11. LinkedHashMap
**Best for:** Maintaining insertion order (or access order for LRU cache).

```java
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapExample {
    public static void main(String[] args) {
        // Maintains insertion order
        Map<String, String> capitals = new LinkedHashMap<>();
        
        capitals.put("India", "New Delhi");
        capitals.put("USA", "Washington DC");
        capitals.put("Japan", "Tokyo");
        
        // Always prints in insertion order!
        capitals.forEach((k, v) -> System.out.println(k + ": " + v));
        // India: New Delhi
        // USA: Washington DC
        // Japan: Tokyo
        
        // Access-order LinkedHashMap (for LRU cache)
        LinkedHashMap<String, String> lruCache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > 3;  // Keep only 3 entries
            }
        };
        
        lruCache.put("A", "1");
        lruCache.put("B", "2");
        lruCache.put("C", "3");
        lruCache.get("A");      // Access A, moves it to end
        lruCache.put("D", "4"); // Adds D, removes B (least recently used)
        
        System.out.println(lruCache.keySet());  // [C, A, D]
    }
}
```

**When to use:** When you need predictable iteration order or implementing LRU cache.

---

### 12. TreeMap
**Best for:** Sorted keys. Implements NavigableMap.

```java
import java.util.TreeMap;
import java.util.NavigableMap;
import java.util.Map;

public class TreeMapExample {
    public static void main(String[] args) {
        // Keys are automatically sorted!
        TreeMap<Integer, String> rankings = new TreeMap<>();
        
        rankings.put(3, "Bronze");
        rankings.put(1, "Gold");
        rankings.put(2, "Silver");
        rankings.put(5, "Fifth");
        rankings.put(4, "Fourth");
        
        System.out.println(rankings);  // {1=Gold, 2=Silver, 3=Bronze, 4=Fourth, 5=Fifth}
        
        // First and last entries
        Map.Entry<Integer, String> first = rankings.firstEntry();  // 1=Gold
        Map.Entry<Integer, String> last = rankings.lastEntry();    // 5=Fifth
        
        // First and last keys
        Integer firstKey = rankings.firstKey();  // 1
        Integer lastKey = rankings.lastKey();    // 5
        
        // Lower/Higher (exclusive)
        Integer lowerKey = rankings.lowerKey(3);   // 2
        Integer higherKey = rankings.higherKey(3); // 4
        
        // Floor/Ceiling (inclusive)
        Integer floorKey = rankings.floorKey(3);     // 3
        Integer ceilingKey = rankings.ceilingKey(3); // 3
        
        // Poll (remove) first/last
        Map.Entry<Integer, String> pollFirst = rankings.pollFirstEntry();
        Map.Entry<Integer, String> pollLast = rankings.pollLastEntry();
        
        // Submap views
        NavigableMap<Integer, String> subMap = rankings.subMap(2, true, 4, true);
        NavigableMap<Integer, String> headMap = rankings.headMap(3, false);  // < 3
        NavigableMap<Integer, String> tailMap = rankings.tailMap(3, true);   // >= 3
        
        // Descending map
        NavigableMap<Integer, String> descending = rankings.descendingMap();
        
        // Custom key ordering
        TreeMap<String, Integer> scores = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        scores.put("alice", 100);
        scores.put("Bob", 95);
        scores.put("CHARLIE", 90);
        // Keys sorted case-insensitively: alice, Bob, CHARLIE
    }
}
```

**When to use:** When you need keys in sorted order or range-based queries.

---

## Quick Reference: When to Use What?

| Need | Use |
|------|-----|
| Fast access by index | `ArrayList` |
| Fast insert/delete at ends | `LinkedList` or `ArrayDeque` |
| Unique elements, no order | `HashSet` |
| Unique elements, insertion order | `LinkedHashSet` |
| Unique elements, sorted | `TreeSet` |
| LIFO (Stack behavior) | `ArrayDeque` |
| FIFO (Queue behavior) | `ArrayDeque` or `LinkedList` |
| Priority-based processing | `PriorityQueue` |
| Key-value, no order | `HashMap` |
| Key-value, insertion order | `LinkedHashMap` |
| Key-value, sorted keys | `TreeMap` |
| Thread-safe list | `CopyOnWriteArrayList` or `Collections.synchronizedList()` |
| Thread-safe map | `ConcurrentHashMap` |

## Methods of Collection Interface :

<table class="alt">
<tbody><tr><th>No.</th><th>Method</th><th>Description</th></tr>
<tr><td>1</td><td>public boolean add(Object element)</td><td> is used to insert an element in this collection.</td></tr>
<tr><td>2</td><td>public boolean addAll(Collection c)</td><td>is used to insert the specified collection elements in the invoking collection.</td></tr>
<tr><td>3</td><td>public boolean remove(Object element)</td><td>is used to delete an element from this collection.</td></tr>
<tr><td>4</td><td>public boolean removeAll(Collection c)</td><td>is used to delete all the elements of specified collection from the invoking collection.</td></tr>
<tr><td>5</td><td>public boolean retainAll(Collection c)</td><td>is used to delete all the elements of invoking collection except the specified collection.</td></tr>
<tr><td>6</td><td>public int size()</td><td>return the total number of elements in the collection.</td></tr>
<tr><td>7</td><td>public void clear()</td><td>removes the total no of element from the collection.</td></tr>
<tr><td>8</td><td>public boolean contains(Object element)</td><td>is used to search an element.</td></tr>
<tr><td>9</td><td>public boolean containsAll(Collection c)</td><td>is used to search the specified collection in this collection.</td></tr>
<tr><td>10</td><td>public Iterator iterator()</td><td>returns an iterator.</td></tr>
<tr><td>11</td><td>public Object[] toArray()</td><td>converts collection into array.</td></tr>
<tr><td>12</td><td>public boolean isEmpty()</td><td>checks if collection is empty.</td></tr>
<tr><td>13</td><td>public boolean equals(Object element)</td><td>matches two collection.</td></tr>
<tr><td>14</td><td>public int hashCode()</td><td>returns the hashcode number for collection.</td></tr>
</tbody></table>

## Methods of Iterator Interface :

<table class="alt">
<tbody><tr><th>No.</th><th>Method</th><th>Description</th></tr>
<tr><td>1</td><td>public boolean hasNext()</td><td>It returns true if iterator has more elements.</td></tr>
<tr><td>2</td><td>public Object next()</td><td>It returns the element and moves the cursor pointer to the next element.</td></tr>
<tr><td>3</td><td>public void remove()</td><td>It removes the last elements returned by the iterator. It is rarely used.</td></tr>
</tbody></table>

## ArrayList vs LinkedList :

<table class="alt">
<tbody><tr><th>ArrayList</th><th>LinkedList</th></tr>
<tr><td>1) ArrayList internally uses <strong>dynamic array</strong> to store the elements.</td><td>LinkedList internally uses <strong>doubly linked list</strong> to store the elements.</td></tr>
<tr><td>2) Manipulation with ArrayList is <strong>slow</strong> because it internally uses array. If any element is removed from the array, all the bits are shifted in memory.</td><td>Manipulation with LinkedList is <strong>faster</strong> than ArrayList because it uses doubly linked list so no bit shifting is required in memory.</td></tr>
<tr><td>3) ArrayList class can <strong>act as a list</strong> only because it implements List only.</td><td>LinkedList class can <strong>act as a list and queue</strong> both because it implements List and Deque interfaces.</td></tr>
<tr><td>4) ArrayList is <strong>better for storing and accessing</strong> data.</td><td>LinkedList is <strong>better for manipulating</strong> data.</td></tr>
</tbody></table>

## ArrayList vs Vector :

<table class="alt">
<tbody><tr><th>ArrayList</th><th>Vector</th></tr>
<tr><td>1) ArrayList is <strong>not synchronized</strong>.</td><td>Vector is <strong>synchronized</strong>.</td></tr>
<tr><td>2) ArrayList <strong>increments 50%</strong> of current array size if number of element exceeds from its capacity.</td><td>Vector <strong>increments 100%</strong> means doubles the array size if total number of element exceeds than its capacity.</td></tr>
<tr><td>3) ArrayList is <strong>not a legacy</strong> class, it is introduced in JDK 1.2.</td><td>Vector is a <strong>legacy</strong> class.</td></tr>
<tr><td>4) ArrayList is <strong>fast</strong> because it is non-synchronized.</td><td>Vector is <strong>slow</strong> because it is synchronized i.e. in multithreading environment, it will hold the other threads in runnable or non-runnable state until current thread releases the lock of object.</td></tr>
<tr><td>5) ArrayList uses <strong>Iterator</strong> interface to traverse the elements.</td><td>Vector uses <strong>Enumeration</strong> interface to traverse the elements. But it can use Iterator also.</td></tr>
</tbody></table>

## Hierarchy of Map Interface :

![hierarchy-of-maps-in-java](https://user-images.githubusercontent.com/2780145/34075635-1a5bba52-e2f2-11e7-8962-384b5bc24ae5.png)

## Useful Methods of Map Interface :

<table class="alt">
<tbody><tr><th>Method</th><th>Description</th></tr>
<tr><td> Object put(Object key, Object value)</td><td>It is used to insert an entry in this map.</td></tr>
<tr><td>void putAll(Map map)</td><td>It is used to insert the specified map in this map.</td></tr>
<tr><td>Object remove(Object key)</td><td>It is used to delete an entry for the specified key.</td></tr>
<tr><td>Object get(Object key)</td><td>It is used to return the value for the specified key.</td></tr>
<tr><td>boolean containsKey(Object key)</td><td>It is used to search the specified key from this map.</td></tr>
<tr><td>Set keySet()</td><td>It is used to return the Set view containing all the keys.</td></tr>
<tr><td>Set entrySet()</td><td>It is used to return the Set view containing all the keys and values.</td></tr>
</tbody></table>

## Methods of Map.Entry Interface :
<table class="alt">
<tbody><tr><th>Method</th><th>Description</th></tr>
<tr><td> Object getKey()</td><td>It is used to obtain key.</td></tr>
<tr><td>Object getValue()</td><td>It is used to obtain value.</td></tr>
</tbody></table>

## HashMap vs HashTable :

<table class="alt">
<tbody><tr><th>HashMap</th><th>Hashtable</th></tr>
<tr><td>1) HashMap is <strong>non synchronized</strong>. It is not-thread safe and can't be shared between many threads without proper synchronization code.</td><td>Hashtable is <strong>synchronized</strong>. It is thread-safe and can be shared with many threads.</td></tr>
<tr><td>2) HashMap <strong>allows one null key and multiple null values</strong>.</td><td>Hashtable <strong>doesn't allow any null key or value</strong>.</td></tr>
<tr><td>3) HashMap is a <strong>new class introduced in JDK 1.2</strong>.</td><td>Hashtable is a <strong>legacy class</strong>.</td></tr>
<tr><td>4) HashMap is <strong>fast</strong>.</td><td>Hashtable is <strong>slow</strong>.</td></tr>
<tr><td>5) We can make the HashMap as synchronized by calling this code<br> Map m = Collections.synchronizedMap(hashMap);</td><td>Hashtable is internally synchronized and can't be unsynchronized.</td></tr>
<tr><td>6) HashMap is <strong>traversed by Iterator</strong>.</td><td>Hashtable is <strong>traversed by Enumerator and Iterator</strong>.</td></tr>
<tr><td>7) Iterator in HashMap is <strong>fail-fast</strong>.</td><td>Enumerator in Hashtable is <strong>not fail-fast</strong>.</td></tr>
<tr><td>8) HashMap inherits <strong>AbstractMap</strong> class.</td><td>Hashtable inherits <strong>Dictionary</strong> class.</td></tr>
</tbody></table>

## Collections Framework Implementation Classes Summary :

![collectionjava](https://user-images.githubusercontent.com/2780145/34075655-a59a8a1c-e2f2-11e7-94d7-a49c03df0fa8.png)

## Comparable vs Comparator Interfaces :

<table class="alt">
<tbody><tr><th>Comparable</th><th>Comparator</th></tr>
<tr><td>1) Comparable provides <strong>single sorting sequence</strong>. In other words, we can sort the collection on the basis of single element such as id or name or price etc.</td><td> Comparator provides <strong>multiple sorting sequence</strong>. In other words, we can sort the collection on the basis of multiple elements such as id, name and price etc.</td></tr>
<tr><td>2) Comparable <strong>affects the original class</strong> i.e. actual class is modified.</td><td>Comparator <strong>doesn't affect the original class</strong> i.e. actual class is not modified.</td></tr>
<tr><td>3) Comparable provides <strong>compareTo() method</strong> to sort elements.</td><td>Comparator provides <strong>compare() method</strong> to sort elements.</td></tr>
<tr><td>4) Comparable is found in <strong>java.lang</strong> package.</td><td>Comparator is found in <strong>java.util</strong> package.</td></tr>
<tr><td>5) We can sort the list elements of Comparable type by <strong>Collections.sort(List)</strong> method.</td><td>We can sort the list elements of Comparator type by <strong>Collections.sort(List,Comparator)</strong> method.</td></tr>
</tbody></table>

### Code Examples

**Comparable Example** - Single natural ordering (class is modified):
```java
// Student class implements Comparable - defines ONE natural ordering
class Student implements Comparable<Student> {
    int id;
    String name;
    double score;
    
    public Student(int id, String name, double score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }
    
    // Natural ordering by ID (only ONE way to sort)
    @Override
    public int compareTo(Student other) {
        return this.id - other.id;
    }
}

// Usage
List<Student> students = new ArrayList<>();
Collections.sort(students);  // Sorts by ID (natural ordering)
```

**Comparator Example** - Multiple custom orderings (class is NOT modified):
```java
// Multiple Comparator implementations for different sort orders
class NameComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return s1.name.compareTo(s2.name);
    }
}

class ScoreComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return Double.compare(s2.score, s1.score);  // Descending order
    }
}

// Usage - Choose any ordering without modifying Student class!
Collections.sort(students, new NameComparator());   // Sort by name
Collections.sort(students, new ScoreComparator());  // Sort by score (desc)
```

### Modern Java 8+ Comparator Features

```java
// Method references - cleaner syntax
students.sort(Comparator.comparing(Student::getName));
students.sort(Comparator.comparing(Student::getScore));

// Chained comparators - sort by multiple fields
students.sort(Comparator
    .comparing(Student::getName)
    .thenComparing(Student::getId)
    .thenComparing(Student::getScore));

// Reverse order
students.sort(Comparator.comparing(Student::getScore).reversed());

// Null-safe sorting
students.sort(Comparator.nullsFirst(Comparator.comparing(Student::getName)));
students.sort(Comparator.nullsLast(Comparator.comparing(Student::getName)));

// Lambda expressions
students.sort((s1, s2) -> s1.name.compareTo(s2.name));
students.sort((s1, s2) -> Double.compare(s2.score, s1.score));  // Descending
```

**Key Insight:** With `Comparable`, the class defines its own single ordering. With `Comparator`, you can create unlimited external ordering strategies without touching the original class - making it more flexible and following the Open/Closed Principle.

## Legacy Data Structures in Java

Legacy classes and interfaces are the classes and interfaces that formed the collections framework in the earlier versions of Java and has now been restructured or re-engineered. They are fully compatible with the framework.

All legacy classes were re-engineered to support generic in JDK5.

Legacy = heritage of old java version.

Legacy classes and interfaces - Enumeration, Vector, Stack, Dictionary, HashTable, Properties...

---

## Concurrent Collections (Thread-Safe)

Regular collections are NOT thread-safe. Use these in multi-threaded environments:

### ConcurrentHashMap
```java
import java.util.concurrent.ConcurrentHashMap;

// Thread-safe, high-performance alternative to Hashtable/synchronizedMap
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// Thread-safe operations
map.put("a", 1);
map.putIfAbsent("b", 2);     // atomic
map.computeIfAbsent("c", k -> 3);  // atomic
map.getOrDefault("d", 0);

// Atomic increment — safe across threads
map.merge("a", 1, Integer::sum);
```

**How ConcurrentHashMap differs from HashMap and Hashtable:**
- **HashMap**: Not thread-safe. Race conditions in multi-threaded use.
- **Hashtable**: Thread-safe but uses a single lock on the whole map → slow.
- **ConcurrentHashMap**: Thread-safe and fast — uses **segment-level locking** (Java 7) or **CAS + synchronized per bucket** (Java 8+). Allows concurrent reads without locking.

### CopyOnWriteArrayList
```java
import java.util.concurrent.CopyOnWriteArrayList;

// On every write (add/set/remove), creates a FRESH COPY of the array
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
list.add("a");
list.add("b");

// Safe to iterate while another thread modifies
for (String s : list) {
    list.add("c");  // Won't throw ConcurrentModificationException
}
```
**Use when:** Reads are far more frequent than writes (e.g., event listener lists).

---

## Fail-Fast vs Fail-Safe Iterators

### Fail-Fast (ArrayList, HashMap, HashSet)
- Throws `ConcurrentModificationException` if the collection is modified while iterating (outside of iterator's own `remove()`).
- Uses an internal `modCount` to detect structural changes.

```java
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
for (String s : list) {
    if (s.equals("b")) list.remove(s);  // ConcurrentModificationException!
}

// Safe way to remove while iterating:
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("b")) it.remove();  // Use iterator's remove()
}
// Or Java 8+:
list.removeIf(s -> s.equals("b"));
```

### Fail-Safe (ConcurrentHashMap, CopyOnWriteArrayList)
- Works on a snapshot of the collection — modifications don't affect ongoing iteration.
- Does NOT throw `ConcurrentModificationException`.
- May not reflect the latest state of the collection.

---

## Collections Utility Class — Key Methods

```java
List<Integer> nums = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6));

Collections.sort(nums);                      // [1, 1, 2, 3, 4, 5, 6, 9]
Collections.sort(nums, Comparator.reverseOrder()); // descending
Collections.reverse(nums);                   // reverse order
Collections.shuffle(nums);                   // random shuffle
Collections.min(nums);                       // smallest
Collections.max(nums);                       // largest
Collections.frequency(nums, 1);             // count occurrences of 1
Collections.binarySearch(nums, 4);          // index (list must be sorted)
Collections.fill(nums, 0);                  // fill all with 0
Collections.nCopies(5, "hello");            // [hello, hello, hello, hello, hello]
Collections.unmodifiableList(nums);         // immutable wrapper
Collections.synchronizedList(nums);         // thread-safe wrapper
Collections.disjoint(list1, list2);         // true if no common elements
Collections.swap(nums, 0, 3);              // swap elements at indexes 0 and 3
```

---

## Interview Questions — Collections Framework

**Q1. What is the difference between `ArrayList` and `LinkedList`? When to use which?**
- **ArrayList**: backed by a dynamic array. O(1) random access, O(n) insert/delete in middle.
- **LinkedList**: doubly-linked list. O(n) random access, O(1) insert/delete at head/tail.
- Use `ArrayList` for frequent reads; `LinkedList` for frequent insertions/deletions at the front.

**Q2. How does `HashSet` ensure uniqueness?**
- Internally backed by a `HashMap` where elements are keys with a dummy value.
- When you `add(element)`, it calls `hashCode()` to find the bucket, then `equals()` to check for existing duplicate. If duplicate found, add is rejected.

**Q3. What is the difference between `HashMap`, `LinkedHashMap`, and `TreeMap`?**
| | HashMap | LinkedHashMap | TreeMap |
|---|---|---|---|
| Order | No order | Insertion order | Sorted (natural/comparator) |
| Null key | 1 allowed | 1 allowed | NOT allowed |
| Performance | O(1) | O(1) | O(log n) |
| Backed by | Hash table | Hash table + linked list | Red-Black tree |

**Q4. Can we use a mutable object as a HashMap key?**
- It is **dangerous but technically possible**.
- If the object's fields change after being used as a key, its `hashCode()` will change, making it **impossible to retrieve the entry** (JVM looks in wrong bucket).
- Best practice: use **immutable objects** as keys (String, Integer, etc.)

**Q5. What is the difference between `Iterator` and `ListIterator`?**
- `Iterator`: unidirectional (forward only), works on all collections.
- `ListIterator`: bidirectional (forward + backward), works only on `List`, supports `add()` and `set()` during iteration.

**Q6. What happens when you add `null` to a `TreeSet` or `TreeMap`?**
- Throws `NullPointerException` because TreeSet/TreeMap uses `compareTo()` for ordering, and `null` cannot be compared.

**Q7. What is `ConcurrentModificationException` and how do you avoid it?**
- Thrown when a collection is structurally modified while being iterated (by a different thread or within the loop itself).
- Avoid by: using `Iterator.remove()`, `removeIf()`, `ConcurrentHashMap`, `CopyOnWriteArrayList`, or collecting to-remove items and removing after iteration.

**Q8. What is the difference between `poll()` and `remove()` in a Queue?**
- `poll()`: returns and removes the head, returns `null` if queue is empty.
- `remove()`: returns and removes the head, throws `NoSuchElementException` if empty.
- Similarly: `peek()` (null on empty) vs `element()` (throws on empty).

**Q9. How would you implement an LRU cache in Java?**
```java
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    LRUCache(int capacity) {
        super(capacity, 0.75f, true);  // true = access-order
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;  // remove LRU when over capacity
    }
}

LRUCache<Integer, String> cache = new LRUCache<>(3);
cache.put(1, "a"); cache.put(2, "b"); cache.put(3, "c");
cache.get(1);       // Access 1 — moves to end (most recently used)
cache.put(4, "d"); // Evicts 2 (least recently used)
```

**Q10. What is the difference between `Comparable` and `Comparator`? Which is preferred?**
- `Comparable.compareTo()` defines the natural ordering — modifies the class itself (intrusive).
- `Comparator.compare()` defines external orderings — doesn't modify the class (non-intrusive).
- Prefer `Comparator` when: you need multiple sort orders, you can't modify the class, or you want to use lambdas.

**Q11. What is an `EnumSet` and why is it fast?**
```java
enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

EnumSet<Day> weekdays = EnumSet.of(Day.MON, Day.TUE, Day.WED, Day.THU, Day.FRI);
EnumSet<Day> weekend = EnumSet.complementOf(weekdays);
```
- Implemented as a **bit vector** internally — each enum constant maps to a bit.
- All operations (add, contains, remove) are O(1) bit operations.
- Much faster than `HashSet<Enum>` for enum types.

**Q12. What is `Collections.unmodifiableList()` vs `List.of()` (Java 9+)?**
- `unmodifiableList()`: wraps a mutable list — the underlying list can still change if someone holds a reference to it.
- `List.of()`: truly immutable — no way to modify it, throws `UnsupportedOperationException` on any write attempt. Also doesn't allow `null` elements.
