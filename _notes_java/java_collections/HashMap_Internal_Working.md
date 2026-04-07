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
