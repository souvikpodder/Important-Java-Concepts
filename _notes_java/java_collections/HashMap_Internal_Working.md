# HashMap in Java and its Internal Working

`HashMap` is one of the most popular and frequently used classes in the Java Collections Framework. It implements the `Map` interface and stores data in **Key-Value** pairs.

## 1. Key Characteristics
*   **Fast Access:** Offers constant-time performance $O(1)$ for basic operations like `get` and `put`, assuming the hash function disperses elements properly among the buckets.
*   **Null Values:** Allows one `null` key and multiple `null` values.
*   **Ordering:** Does not guarantee any specific order of the elements (order can change over time, especially after resizing).
*   **Thread Safety:** It is **not synchronized** (not thread-safe). For thread-safe operations, use `ConcurrentHashMap` or `Collections.synchronizedMap(new HashMap(...))`.

---

## 2. Internal Data Structure

Internally, `HashMap` uses an **Array of Nodes** (often called an Array of Buckets or a Hash Table). 

```java
transient Node<K,V>[] table;
```

Each `Node` is fundamentally a linked list node that implements `Map.Entry<K, V>`.

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}
```
*   `hash`: The calculated hash value of the key.
*   `key`: The key object.
*   `value`: The value object.
*   `next`: A reference to the next Node (used when dealing with collisions).

---

## 3. Important Concepts and Parameters

1.  **Capacity**: The number of buckets in the hash table. The default initial capacity is **16**. It is always a power of 2.
2.  **Load Factor**: A measure of how full the hash table is allowed to get before its capacity is automatically increased. The default load factor is **0.75**.
3.  **Threshold**: The condition at which the capacity is increased. \n    `Threshold = (Capacity * Load Factor)`. \n    For defaults: `16 * 0.75 = 12`. When the 13th element is added, the HashMap is resized.
4.  **Bucket**: One element/index of the `table` array. A bucket can store a single node, a linked list of nodes, or a Red-Black tree.

---

## 4. Internal Working: The `put(K key, V value)` Operation

When you call `map.put(key, value)`, the following sequential steps take place:

### Step 1: Compute Hash
The `hash()` function is called on the key to calculate an integer hash value. `HashMap` uses its own internal hash function to mitigate poor hash functions written by users.
```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```
*   If the key is `null`, its hash is `0`. This is why `null` key always goes to `bucket[0]`.
*   It does an XOR (`^`) with the right-shifted (`>>> 16`) hashcode to spread the higher bits of the hash across the lower bits, minimizing collisions.

### Step 2: Calculate Bucket Index
The index where the key-value pair will be stored in the array is calculated using the hash and the array length ($n$).
```java
index = (n - 1) & hash
```
This bitwise AND operation is much faster than the modulo operation (`hash % n`) and works accurately because the capacity $n$ is always guaranteed to be a power of 2.

### Step 3: Insert into the Bucket
Now `HashMap` checks the array at the calculated `index`:

*   **Case A: Bucket is empty (No Collision)**
    If `table[index]` is `null`, a new `Node` is created and placed in the bucket.
*   **Case B: Bucket is not empty (Collision)**
    If `table[index]` already has an element, this is a **Hash Collision**. The HashMap must traverse the existing data structure at that bucket (either a Linked List or a Red-Black Tree).
    1.  **Check Key Equality**: It iterates through the nodes. For each node, it checks if the keys are identical based on their hash and `equals()` method:
        `if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))`
    2.  **Update Value**: If a matching key is found, the existing value is replaced with the new value.
    3.  **Append to End**: If no matching key is found in the linked list, a new `Node` is appended securely to the end of the list.
    
### Step 4: Treeification (Java 8 Optimization)
If a collision occurs and a new node is cleanly appended, `HashMap` checks the size of the linked list in that bucket.
*   If the number of nodes in a single bucket reaches the **`TREEIFY_THRESHOLD` (which is 8)**, the Linked List is converted into a **Red-Black Tree**.
*   This improves worst-case search performance from $O(n)$ to $O(\log n)$.
*   *(Note: Treeification only happens if the total array capacity is at least `MIN_TREEIFY_CAPACITY` (64). If the array size is less than 64, the table is simply resized instead).*

### Step 5: Rehashing (Resizing)
Finally, after adding the new node, `HashMap` increments its internal size counter.
*   If `size > threshold`, a `resize()` operation is triggered.
*   The capacity is **doubled** (e.g., from 16 to 32).
*   A new array is created, and all existing entries are re-hashed and re-distributed into the new array. Because the capacity is a power of 2, nodes either stay in their original index or move to `originalIndex + oldCapacity`.

---

## 5. Internal Working: The `get(Object key)` Operation

When you call `map.get(key)`, data is retrieved using a similar philosophy:

1.  **Calculate Hash**: The `hash()` function calculates the hash of the target key.
2.  **Calculate Index**: The bucket array index is determined using `(n - 1) & hash`.
3.  **Check the Bucket**:
    *   If the bucket at that index is `null`, return `null`.
    *   If the bucket contains a Node (or Linked List / Tree), traverse it.
4.  **Key Matching**: Compare each node's key with the target key using `hashCode()` and the `equals()` method.
5.  **Return**: If an exact match is found, return the node's `value`. If the traversal finishes without a match, return `null`.

---

## 6. Significance of `hashCode()` and `equals()` Rules

The internal mechanics of `HashMap` rely entirely on these two methods. That is why there is a strict contract:

1.  **If two objects are equal according to `equals(Object)`, they MUST have the same `hashCode()`.**
    *   *Why?* Because `HashMap` uses the hashcode to find the bucket index. If two "equal" objects have different hashcodes, `HashMap` will place them in different buckets and will fail to find the original object when you try to retrieve it using an equal object as the key.
2.  **If two objects have the same `hashCode()`, they are not necessarily equal.**
    *   *Why?* Because of Hash Collisions. `HashMap` resolves this by placing them in the same bucket (LinkedList/Tree) and iterating through them to find the true match using `equals()`.

---

## 7. Performance Summary
| Operation | Average Case | Worst Case (pre-Java 8) | Worst Case (Java 8+) |
| :--- | :--- | :--- | :--- |
| **`put()`** | $O(1)$ | $O(n)$ | $O(\log n)$ |
| **`get()`** | $O(1)$ | $O(n)$ | $O(\log n)$ |
| **`remove()`**| $O(1)$ | $O(n)$ | $O(\log n)$ |

*The worst-case scenario occurs when there are massive hash collisions, placing all elements in the same bucket.*

---

## 8. Key Thresholds — Quick Reference

| Constant | Value | Meaning |
|---|---|---|
| `DEFAULT_INITIAL_CAPACITY` | 16 | Initial number of buckets |
| `DEFAULT_LOAD_FACTOR` | 0.75 | Fill ratio before resize |
| `TREEIFY_THRESHOLD` | 8 | Bucket size triggering linked list → tree |
| `UNTREEIFY_THRESHOLD` | 6 | Bucket size triggering tree → linked list (on remove) |
| `MIN_TREEIFY_CAPACITY` | 64 | Min table size for treeification to happen |

---

## 9. HashMap vs ConcurrentHashMap vs Hashtable

| Feature | HashMap | ConcurrentHashMap | Hashtable |
|---|---|---|---|
| Thread-safe | No | Yes | Yes |
| Null key | 1 allowed | Not allowed | Not allowed |
| Null value | Multiple allowed | Not allowed | Not allowed |
| Lock mechanism | None | Per-bucket (Java 8+) | Whole map |
| Performance | Best (single thread) | Best (multi-thread) | Slowest |
| Iterator | Fail-fast | Fail-safe (weakly consistent) | Fail-safe |
| Inheritance | AbstractMap | AbstractMap | Dictionary |
| Since | Java 1.2 | Java 1.5 | Java 1.0 (legacy) |

---

## 10. How `ConcurrentHashMap` Achieves Thread Safety (Java 8)

- Uses **CAS (Compare-And-Swap)** operations for empty bucket insertions.
- Uses `synchronized` block on the individual **bucket head node** for collision chains (not the whole map).
- Multiple threads can write to **different buckets simultaneously** — this is the key advantage.
- `size()` uses a `LongAdder`-like mechanism to avoid lock contention.

---

## Interview Questions — HashMap Internal Working

**Q1. What is a Hash Collision and how does HashMap handle it?**
- A collision occurs when two different keys produce the same bucket index (`(n-1) & hash`).
- Java 8+ handles it via a **linked list** (up to 8 nodes) that converts to a **Red-Black Tree** beyond 8 nodes.
- Resolution uses `equals()` to find the correct key within the bucket.

**Q2. Why is the default initial capacity 16 and not some other number?**
- It must be a **power of 2** so the bucket index calculation `(n-1) & hash` works correctly as a fast modulo replacement.
- 16 is a sweet spot — small enough to not waste memory, large enough to avoid immediate resizing for typical use.

**Q3. Why is the load factor 0.75 by default?**
- It balances space and time efficiency.
- A lower load factor (e.g., 0.5) reduces collisions but wastes memory (map resizes too early).
- A higher load factor (e.g., 1.0) saves memory but increases collisions → slower lookups.
- 0.75 is a mathematically derived value from Poisson distribution that provides optimal performance.

**Q4. Walk me through what happens when you call `map.put("key", "value")` on a HashMap.**
1. `hash("key")` → computes `hashCode()` XOR'd with itself right-shifted 16 bits.
2. `index = (16-1) & hash` → finds which bucket (0..15).
3. If bucket is empty → create `Node` and place it.
4. If bucket is not empty → traverse list/tree:
   - If matching key found → update value.
   - Else → append new `Node` at end.
5. If bucket's linked list length reaches 8 AND total capacity ≥ 64 → convert to Red-Black Tree.
6. If `++size > threshold (12)` → `resize()` doubles capacity to 32, re-hashes all entries.

**Q5. Why does HashMap not guarantee ordering? Can this change across JVM runs?**
- Bucket index depends on `hashCode()` and current capacity. Since capacity changes on resize, order changes.
- `String.hashCode()` is deterministic within a JVM run but was historically randomized between runs (for security) — though Java's String hashCode is now stable.
- For consistent iteration order: use `LinkedHashMap` (insertion order) or `TreeMap` (sorted order).

**Q6. What happens if you use an object as a key, then mutate the object?**
```java
class MutableKey {
    int val;
    int hashCode() { return val; }
    boolean equals(Object o) { ... }
}
Map<MutableKey, String> map = new HashMap<>();
MutableKey k = new MutableKey();
k.val = 1;
map.put(k, "one");    // stored at bucket (n-1) & 1

k.val = 2;            // NOW MUTATED
map.get(k);           // looks in bucket (n-1) & 2 — NOT FOUND!
map.containsKey(k);   // false — the entry is now "lost"
```
- **Always use immutable objects as map keys.**

**Q7. What is the difference between `get()` returning `null` vs key not existing?**
```java
map.put("a", null);       // key "a" exists, value is null
map.get("a");             // returns null
map.containsKey("a");     // true

map.get("b");             // returns null
map.containsKey("b");     // false

// To distinguish:
if (map.containsKey(key)) { ... }  // or use getOrDefault()
```

**Q8. How does HashMap behave when you insert the same key twice?**
- It **replaces** the old value with the new value and returns the old value.
- The map size does NOT increase.
```java
map.put("a", 1);  // size=1
map.put("a", 2);  // size still 1, value updated to 2
Integer old = map.put("a", 3);  // returns 2 (previous value)
```

**Q9. When would you prefer `LinkedHashMap` over `HashMap`?**
- When you need predictable iteration order (e.g., building a JSON object, printing a config).
- When implementing an **LRU Cache** — use access-order mode with `removeEldestEntry()`.
- Slight performance overhead due to maintaining the doubly-linked list.

**Q10. What is `HashMap.compute()` and how is it different from `put()`?**
```java
// put() — always sets the value
map.put("count", map.getOrDefault("count", 0) + 1);  // NOT atomic

// compute() — atomic read-modify-write
map.compute("count", (k, v) -> v == null ? 1 : v + 1);  // safer

// computeIfAbsent() — only if key is absent
map.computeIfAbsent("list", k -> new ArrayList<>()).add("item");

// merge() — most readable for counters
map.merge("count", 1, Integer::sum);  // add 1 to existing, or put 1 if absent
```
