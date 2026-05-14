# Tree Data Structures

A **tree** is a hierarchical data structure made of nodes connected by edges. It starts from one top node called the **root**, and every child node belongs to exactly one parent. Trees are used when data has a natural hierarchy or when we need fast searching, ordering, indexing, and range queries.

Common real-world examples:

- File system folders
- HTML/XML DOM
- Organization hierarchy
- Database indexes
- Java `TreeMap` and `TreeSet`
- Priority queues
- Auto-complete dictionaries
- Routing tables

---

## Basic Tree Terminology

```text
          A
        / | \
       B  C  D
      / \    |
     E   F   G
```

| Term | Meaning |
|---|---|
| Node | Each element in a tree, such as `A`, `B`, `C` |
| Root | Topmost node, here `A` |
| Edge | Connection between two nodes |
| Parent | Node directly above another node, `B` is parent of `E` and `F` |
| Child | Node directly below another node |
| Sibling | Nodes with the same parent, `B`, `C`, and `D` are siblings |
| Leaf | Node with no children, `E`, `F`, `C`, and `G` |
| Internal node | Node with at least one child |
| Ancestor | Any node above a node on the path to root |
| Descendant | Any node below a node |
| Subtree | A node and all of its descendants |
| Degree of node | Number of children a node has |
| Degree of tree | Maximum degree of any node in the tree |
| Level | Distance from root if root is level `0` |
| Height of node | Longest path from that node to a leaf |
| Height of tree | Height of root node |
| Depth of node | Number of edges from root to that node |

---

## Tree vs Graph

A tree is a special type of graph.

| Tree | Graph |
|---|---|
| Hierarchical | General network |
| Has one root | May not have a root |
| No cycles | Can have cycles |
| One path between two nodes | Can have many paths |
| `n` nodes have `n - 1` edges | Any number of edges |

```text
Tree:
A -> B -> D
 \-> C

Graph:
A -- B
|  / |
C -- D
```

---

## 1. General Tree

A **general tree** allows each node to have any number of children.

```text
          Company
        /    |     \
      HR   Tech   Sales
           /  \
        API   UI
```

**Use cases:**

- File systems
- Menus
- Organization charts
- JSON/XML document models

**Operations:**

- Insert child
- Delete subtree
- Traverse hierarchy
- Search node

**Complexity:**

| Operation | Time |
|---|---|
| Search | `O(n)` |
| Insert child if parent is known | `O(1)` or `O(k)` depending on child storage |
| Delete subtree | `O(size of subtree)` |

---

## 2. Binary Tree

A **binary tree** is a tree where every node has at most two children:

- Left child
- Right child

```text
        10
       /  \
      5    20
     / \     \
    3   7     30
```

Binary trees are the base for many important structures such as BST, AVL tree, Red-Black tree, heap, and expression tree.

### Binary Tree Node in Java

```java
class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;

    TreeNode(int value) {
        this.value = value;
    }
}
```

---

## 3. Full Binary Tree

A **full binary tree** is a binary tree where every node has either:

- `0` children, or
- `2` children

No node has only one child.

```text
        1
       / \
      2   3
     / \
    4   5
```

**Useful for:**

- Expression trees
- Some recursive divide-and-conquer structures

---

## 4. Perfect Binary Tree

A **perfect binary tree** is a binary tree where:

- Every internal node has exactly two children
- All leaf nodes are at the same level

```text
          1
       /     \
      2       3
     / \     / \
    4   5   6   7
```

For height `h`:

```text
Number of nodes = 2^(h + 1) - 1
Number of leaves = 2^h
```

---

## 5. Complete Binary Tree

A **complete binary tree** is a binary tree where:

- All levels are completely filled except possibly the last
- The last level is filled from left to right

```text
          1
       /     \
      2       3
     / \     /
    4   5   6
```

**Important use case:**

- Binary heap
- Java `PriorityQueue`

A complete binary tree can be stored efficiently in an array.

For node at index `i`:

```text
left child  = 2 * i + 1
right child = 2 * i + 2
parent      = (i - 1) / 2
```

---

## 6. Balanced Binary Tree

A **balanced binary tree** keeps its height small, usually around `O(log n)`.

The exact definition depends on the tree type. For example:

- AVL tree is strictly balanced
- Red-Black tree is approximately balanced

Why balance matters:

```text
Balanced:
        4
      /   \
     2     6
    / \   / \
   1   3 5   7

Height = O(log n)

Skewed:
1
 \
  2
   \
    3
     \
      4

Height = O(n)
```

If a tree becomes skewed, search becomes as slow as a linked list.

---

## 7. Binary Search Tree

A **Binary Search Tree** or **BST** is a binary tree with ordering rules.

For every node:

```text
left subtree values  < node value
right subtree values > node value
```

Example:

```text
        50
       /  \
     30    70
    / \    / \
   20 40  60 80
```

### Search in BST

To search `60`:

```text
60 > 50 -> go right
60 < 70 -> go left
60 found
```

### BST Complexity

| Operation | Balanced BST | Skewed BST |
|---|---:|---:|
| Search | `O(log n)` | `O(n)` |
| Insert | `O(log n)` | `O(n)` |
| Delete | `O(log n)` | `O(n)` |
| Inorder traversal | `O(n)` | `O(n)` |

### Important property

Inorder traversal of BST gives sorted order.

```text
20, 30, 40, 50, 60, 70, 80
```

---

## 8. AVL Tree

An **AVL Tree** is a self-balancing Binary Search Tree.

For every node:

```text
balance factor = height(left subtree) - height(right subtree)
```

Allowed balance factor:

```text
-1, 0, +1
```

If the balance factor becomes invalid, the tree uses rotations.

### AVL Rotations

| Case | Problem | Fix |
|---|---|---|
| LL | Left-left heavy | Right rotation |
| RR | Right-right heavy | Left rotation |
| LR | Left-right heavy | Left rotation, then right rotation |
| RL | Right-left heavy | Right rotation, then left rotation |

### AVL Complexity

| Operation | Time |
|---|---:|
| Search | `O(log n)` |
| Insert | `O(log n)` |
| Delete | `O(log n)` |

**Best for:**

- Read-heavy systems
- Cases where faster lookup is more important than simpler insertion/deletion

**Tradeoff:**

- More rotations than Red-Black tree
- Stricter balancing

---

## 9. Red-Black Tree

A **Red-Black Tree** is a self-balancing Binary Search Tree where each node has a color:

```text
RED or BLACK
```

It uses color rules to keep the tree approximately balanced.

### Red-Black Tree Rules

1. Every node is either red or black.
2. Root is always black.
3. All null leaves are considered black.
4. Red node cannot have a red child.
5. Every path from a node to descendant null leaves has the same number of black nodes.

Example:

```text
          10B
        /     \
      5R       20R
     / \       /  \
   3B  7B    15B  30B
```

### Why it works

The color rules prevent the tree from becoming too tall. It may not be as strictly balanced as AVL, but it performs fewer rotations during updates.

### Red-Black Tree Complexity

| Operation | Time |
|---|---:|
| Search | `O(log n)` |
| Insert | `O(log n)` |
| Delete | `O(log n)` |

### Java usage

Java uses Red-Black trees in:

```java
TreeMap<K, V>
TreeSet<E>
```

Java 8+ `HashMap` also converts a bucket's linked list into a Red-Black tree when too many keys collide in the same bucket.

---

## 10. B-Tree

A **B-Tree** is a self-balancing search tree where one node can store multiple keys and have multiple children.

It is designed for systems where reading from disk or storage is expensive.

```text
             [20 | 40]
          /      |       \
   [5 | 10]  [25 | 30]  [50 | 60]
```

**Why B-Trees are useful:**

- Reduce tree height
- Reduce disk reads
- Store many keys per node
- Keep keys sorted

**Use cases:**

- Database indexes
- File systems

### B-Tree Complexity

| Operation | Time |
|---|---:|
| Search | `O(log n)` |
| Insert | `O(log n)` |
| Delete | `O(log n)` |

---

## 11. B+ Tree

A **B+ Tree** is a variation of B-Tree commonly used in databases.

Main idea:

- Internal nodes store only keys for navigation
- Actual records are stored at leaf nodes
- Leaf nodes are linked together for fast range queries

```text
             [20 | 40]
          /      |       \
       [10]     [30]     [50]
        |        |        |
      data <-> data <-> data
```

**Why databases like B+ Trees:**

- Fast range search
- Efficient sorted scans
- High fan-out means small height
- Good disk/page locality

Example queries that benefit:

```sql
WHERE age BETWEEN 20 AND 40
ORDER BY created_at
```

---

## 12. Heap

A **heap** is a complete binary tree that follows a priority rule.

### Min Heap

Parent is smaller than or equal to children.

```text
        5
      /   \
     10    20
    / \   /
   30 40 50
```

Minimum value is always at the root.

### Max Heap

Parent is greater than or equal to children.

```text
        50
      /    \
     30     40
    / \    /
   10 20  5
```

Maximum value is always at the root.

### Heap Complexity

| Operation | Time |
|---|---:|
| Peek min/max | `O(1)` |
| Insert | `O(log n)` |
| Remove min/max | `O(log n)` |
| Search arbitrary value | `O(n)` |

### Java usage

Java's `PriorityQueue` is implemented using a binary heap.

```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

PriorityQueue<Integer> maxHeap =
    new PriorityQueue<>(Comparator.reverseOrder());
```

**Use cases:**

- Priority queue
- Scheduling
- Dijkstra's algorithm
- Top K problems
- Heap sort

---

## 13. Trie

A **Trie** or **Prefix Tree** stores strings character by character.

```text
          root
         /    \
        c      d
        |      |
        a      o
       / \     |
      t   r    g
```

This trie stores:

```text
cat, car, dog
```

### Trie Complexity

Let `L` be the length of the word.

| Operation | Time |
|---|---:|
| Insert word | `O(L)` |
| Search word | `O(L)` |
| Prefix search | `O(L)` |

**Use cases:**

- Auto-complete
- Spell checker
- Dictionary lookup
- Prefix matching
- IP routing

---

## 14. Segment Tree

A **Segment Tree** is used for fast range queries and updates on arrays.

Example array:

```text
[2, 1, 5, 3, 4]
```

Queries:

```text
sum from index 1 to 3
minimum from index 0 to 4
maximum from index 2 to 4
```

### Segment Tree Complexity

| Operation | Time |
|---|---:|
| Build | `O(n)` |
| Range query | `O(log n)` |
| Point update | `O(log n)` |
| Space | `O(n)` |

**Use cases:**

- Range sum query
- Range min/max query
- Competitive programming
- Interval statistics

---

## 15. Fenwick Tree

A **Fenwick Tree**, also called **Binary Indexed Tree**, is used for prefix sums and range sum queries.

It is simpler and more memory-efficient than a Segment Tree, but supports fewer types of operations.

### Fenwick Tree Complexity

| Operation | Time |
|---|---:|
| Build | `O(n log n)` or `O(n)` with optimized build |
| Prefix sum | `O(log n)` |
| Point update | `O(log n)` |
| Range sum | `O(log n)` |
| Space | `O(n)` |

**Use cases:**

- Prefix sums with updates
- Frequency counting
- Inversion count

---

## 16. Expression Tree

An **Expression Tree** represents an arithmetic or logical expression.

```text
        *
      /   \
     +     -
    / \   / \
   a   b c   d
```

This represents:

```text
(a + b) * (c - d)
```

Traversal meaning:

| Traversal | Output |
|---|---|
| Preorder | `* + a b - c d` |
| Inorder | `a + b * c - d` with parentheses needed |
| Postorder | `a b + c d - *` |

**Use cases:**

- Compilers
- Expression evaluation
- Query planners
- Calculator implementations

---

## 17. Splay Tree

A **Splay Tree** is a self-adjusting Binary Search Tree.

Whenever a node is accessed, it is moved to the root using rotations. This operation is called **splaying**.

**Why this helps:**

- Recently accessed items become faster to access again
- Good for workloads with repeated access to the same keys

### Splay Tree Complexity

| Operation | Amortized Time |
|---|---:|
| Search | `O(log n)` |
| Insert | `O(log n)` |
| Delete | `O(log n)` |

Worst-case single operation can be `O(n)`, but average over many operations is `O(log n)`.

---

## 18. Treap

A **Treap** combines:

- BST property by key
- Heap property by priority

Each node has:

```text
key + priority
```

It is usually randomized by assigning random priorities.

**Use cases:**

- Randomized balanced search tree
- Ordered set/map implementation
- Split and merge operations

Expected complexity:

```text
Search: O(log n)
Insert: O(log n)
Delete: O(log n)
```

---

## 19. N-ary Tree

An **N-ary tree** is a tree where each node can have up to `N` children.

Example ternary tree where `N = 3`:

```text
          A
       /  |  \
      B   C   D
     / \
    E   F
```

**Use cases:**

- Game trees
- XML/HTML DOM
- Folder structures
- Menu structures

Java node example:

```java
class Node {
    int value;
    List<Node> children = new ArrayList<>();

    Node(int value) {
        this.value = value;
    }
}
```

---

## 20. Quad Tree and Octree

A **Quad Tree** recursively divides a 2D space into four regions.

```text
+---------+---------+
|   NW    |   NE    |
+---------+---------+
|   SW    |   SE    |
+---------+---------+
```

**Use cases:**

- 2D collision detection
- Maps
- Image compression
- Spatial indexing

An **Octree** is similar but divides 3D space into eight regions.

**Use cases:**

- 3D graphics
- Game engines
- 3D collision detection
- Point cloud storage

---

## 21. Suffix Tree

A **Suffix Tree** stores all suffixes of a string in compressed trie form.

For string:

```text
banana
```

Suffixes:

```text
banana
anana
nana
ana
na
a
```

**Use cases:**

- Fast substring search
- Pattern matching
- Text indexing
- Bioinformatics

Complexity after construction:

```text
Substring search: O(m)
```

where `m` is the pattern length.

---

## 22. Merkle Tree

A **Merkle Tree** is a hash tree. Leaf nodes store hashes of data blocks, and internal nodes store hashes of their children.

```text
          H1234
        /       \
      H12       H34
     /  \      /  \
    H1  H2    H3  H4
```

**Use cases:**

- Blockchain
- Git-like content verification
- Distributed systems
- File integrity checking

Why it is useful:

- Efficiently proves whether data is present
- Detects changed or corrupted data
- Does not require comparing the full dataset

---

## Tree Traversals

Traversal means visiting every node in a specific order.

Example:

```text
        1
       / \
      2   3
     / \
    4   5
```

### Depth First Search

| Traversal | Order | Result |
|---|---|---|
| Preorder | Root, Left, Right | `1, 2, 4, 5, 3` |
| Inorder | Left, Root, Right | `4, 2, 5, 1, 3` |
| Postorder | Left, Right, Root | `4, 5, 2, 3, 1` |

### Breadth First Search

| Traversal | Order | Result |
|---|---|---|
| Level order | Level by level | `1, 2, 3, 4, 5` |

### Recursive DFS in Java

```java
void inorder(TreeNode root) {
    if (root == null) {
        return;
    }

    inorder(root.left);
    System.out.println(root.value);
    inorder(root.right);
}
```

### BFS in Java

```java
void levelOrder(TreeNode root) {
    if (root == null) {
        return;
    }

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
        TreeNode current = queue.poll();
        System.out.println(current.value);

        if (current.left != null) {
            queue.offer(current.left);
        }
        if (current.right != null) {
            queue.offer(current.right);
        }
    }
}
```

---

## Complexity Summary

| Tree Type | Search | Insert | Delete | Main Use |
|---|---:|---:|---:|---|
| General Tree | `O(n)` | `O(1)` if parent known | `O(subtree)` | Hierarchy |
| Binary Tree | `O(n)` | Depends | Depends | Base structure |
| BST balanced | `O(log n)` | `O(log n)` | `O(log n)` | Sorted data |
| BST skewed | `O(n)` | `O(n)` | `O(n)` | Avoid in production |
| AVL Tree | `O(log n)` | `O(log n)` | `O(log n)` | Fast lookup |
| Red-Black Tree | `O(log n)` | `O(log n)` | `O(log n)` | Sorted map/set |
| B-Tree | `O(log n)` | `O(log n)` | `O(log n)` | Disk indexes |
| B+ Tree | `O(log n)` | `O(log n)` | `O(log n)` | Database indexes |
| Heap | `O(n)` | `O(log n)` | `O(log n)` root only | Priority queue |
| Trie | `O(L)` | `O(L)` | `O(L)` | Prefix search |
| Segment Tree | `O(log n)` range query | `O(n)` build | `O(log n)` update | Range queries |
| Fenwick Tree | `O(log n)` prefix/range sum | `O(n)` build | `O(log n)` update | Prefix sums |
| Splay Tree | `O(log n)` amortized | `O(log n)` amortized | `O(log n)` amortized | Repeated access |
| Treap | `O(log n)` expected | `O(log n)` expected | `O(log n)` expected | Randomized BST |

---

## Which Tree Should You Use?

| Requirement | Good Choice |
|---|---|
| Need sorted key-value map in Java | `TreeMap` |
| Need sorted unique values in Java | `TreeSet` |
| Need priority ordering | Heap / `PriorityQueue` |
| Need auto-complete | Trie |
| Need range sum/min/max with updates | Segment Tree |
| Need prefix sums with updates | Fenwick Tree |
| Need database-style indexing | B-Tree / B+ Tree |
| Need strict balanced search | AVL Tree |
| Need practical balanced map/set | Red-Black Tree |
| Need spatial indexing in 2D | Quad Tree |
| Need string pattern matching | Suffix Tree |
| Need data integrity proof | Merkle Tree |

---

## Java Collection Tree Examples

### TreeSet

`TreeSet` stores unique values in sorted order.

```java
Set<Integer> numbers = new TreeSet<>();
numbers.add(30);
numbers.add(10);
numbers.add(20);

System.out.println(numbers); // [10, 20, 30]
```

Internally, `TreeSet` uses `TreeMap`, and `TreeMap` uses a Red-Black tree.

### TreeMap

`TreeMap` stores key-value pairs sorted by key.

```java
Map<Integer, String> ranks = new TreeMap<>();
ranks.put(3, "Bronze");
ranks.put(1, "Gold");
ranks.put(2, "Silver");

System.out.println(ranks);
// {1=Gold, 2=Silver, 3=Bronze}
```

### PriorityQueue

`PriorityQueue` gives the smallest element first by default.

```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.offer(40);
pq.offer(10);
pq.offer(30);

System.out.println(pq.poll()); // 10
```

Internally, `PriorityQueue` uses a binary heap.

---

## Interview Points

- A tree with `n` nodes has `n - 1` edges.
- A Binary Search Tree can degrade to `O(n)` if it becomes skewed.
- AVL Tree is more strictly balanced than Red-Black Tree.
- Red-Black Tree is commonly used in libraries because it gives good practical performance with fewer rotations.
- Java `TreeMap` and `TreeSet` are based on Red-Black Tree.
- Java `PriorityQueue` is based on binary heap.
- Heap is not good for arbitrary search, but excellent for repeated min/max removal.
- Trie search time depends on word length, not total number of words.
- B+ Tree is preferred in databases because linked leaves make range queries efficient.
- Inorder traversal of a BST gives sorted order.

