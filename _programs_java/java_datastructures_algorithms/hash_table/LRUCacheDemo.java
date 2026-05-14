package hash_table;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCacheDemo {

    public static void main(String[] args) {
        System.out.println("=== Custom LRU Cache ===");
        CustomLRUCache<Integer, String> customCache = new CustomLRUCache<>(3);
        customCache.put(1, "One");
        customCache.put(2, "Two");
        customCache.put(3, "Three");
        System.out.println("Access 1: " + customCache.get(1)); // 1 becomes recently used
        customCache.put(4, "Four"); // Removes 2 (least recently used)
        
        System.out.println("Get 2 (should be null): " + customCache.get(2));
        System.out.println("Get 3: " + customCache.get(3));
        System.out.println("Get 4: " + customCache.get(4));

        System.out.println("\n=== Built-in LRU Cache (LinkedHashMap) ===");
        BuiltInLRUCache<Integer, String> builtInCache = new BuiltInLRUCache<>(3);
        builtInCache.put(1, "One");
        builtInCache.put(2, "Two");
        builtInCache.put(3, "Three");
        System.out.println("Access 1: " + builtInCache.get(1));
        builtInCache.put(4, "Four"); // Removes 2

        System.out.println("Get 2 (should be null): " + builtInCache.get(2));
        System.out.println("Get 3: " + builtInCache.get(3));
        System.out.println("Get 4: " + builtInCache.get(4));
        System.out.println("Current cache state: " + builtInCache);
    }
}

/**
 * 1. Custom LRU Cache Implementation
 * Uses a Hash Map (for O(1) lookups) and a Doubly Linked List (for O(1) additions/removals)
 */
class CustomLRUCache<K, V> {
    
    // Doubly Linked List node
    private class Node {
        K key;
        V value;
        Node prev;
        Node next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, Node> cache;
    private Node head;
    private Node tail;

    public CustomLRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        
        // Dummy head and tail for easier operations
        head = new Node(null, null);
        tail = new Node(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        
        Node node = cache.get(key);
        // Move to head (mark as most recently used)
        removeNode(node);
        addNodeToHead(node);
        
        return node.value;
    }

    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            // Update existing node
            Node node = cache.get(key);
            node.value = value;
            removeNode(node);
            addNodeToHead(node);
        } else {
            // Add new node
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addNodeToHead(newNode);
            
            // Check capacity
            if (cache.size() > capacity) {
                // Remove from tail (least recently used)
                Node lruNode = tail.prev;
                removeNode(lruNode);
                cache.remove(lruNode.key);
            }
        }
    }

    private void addNodeToHead(Node node) {
        node.next = head.next;
        node.prev = head;
        
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }
}

/**
 * 2. LRU Cache using Java's built-in LinkedHashMap
 * LinkedHashMap has a constructor that supports "access order" instead of "insertion order".
 */
class BuiltInLRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public BuiltInLRUCache(int capacity) {
        // initialCapacity, loadFactor, accessOrder (true for LRU, false for insertion order)
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // Automatically remove the eldest entry when capacity is exceeded
        return size() > capacity;
    }
}
