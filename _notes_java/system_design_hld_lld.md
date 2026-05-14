# High-Level Design (HLD) vs Low-Level Design (LLD)

System design interviews and software engineering lifecycles are commonly broken down into two major phases: **High-Level Design (HLD)** and **Low-Level Design (LLD)**. Understanding the distinction is crucial for structuring an application effectively.

---

## 🌍 1. High-Level Design (HLD)
Also known as **System Architecture** or **Macro Design**.

HLD focuses on the "big picture." It's about designing how multiple different systems, services, and infrastructure components interact with each other to solve a massive problem (like building Netflix or Uber). It abstracts away the actual code and focuses heavily on scalability, reliability, and data flow.

### Key Characteristics of HLD:
* **Scope**: The entire system or application architecture.
* **Audience**: Solution Architects, Product Managers, Lead Engineers, and Stakeholders.
* **Granularity**: Extremely broad. You don't care about what classes/objects look like. You care about how Service A talks to Service B.

### What is decided in HLD?
1. **Architecture Pattern**: Monolith vs. Microservices vs. Serverless.
2. **Technology Stack**: Deciding to use Java/Spring Boot for APIs, React for Frontend, Python for Data Science.
3. **Database Selection**: SQL (PostgreSQL, MySQL) vs. NoSQL (MongoDB, Cassandra) vs. Graph DB based on the CAP Theorem.
4. **Network & Infrastructure**: 
    * Load Balancers (to distribute traffic).
    * API Gateways (Rate limiting, authentication routing).
    * Content Delivery Networks (CDNs) for static assets.
5. **Scaling Strategies**: 
    * Message Queues/Brokers (Kafka, RabbitMQ) for asynchronous processing.
    * Distributed Caching (Redis, Memcached) to reduce DB load.
6. **Capacity Estimation**: Calculating expected QPS (Queries Per Second), storage requirements (in TB/PB), and network bandwidth.

### Deliverables:
* Architecture Diagrams (showing boxes representing servers, databases, and clients).
* Block diagrams.
* Rough API Contracts (e.g., REST endpoints map).

---

## 🔬 2. Low-Level Design (LLD)
Also known as **Component Design** or **Micro Design**.

LLD is the "zoom-in" on a specific box from your HLD diagram. If HLD says "Here is the Payment Service," LLD says "Here are the exact Java classes, interfaces, and database tables we need to write to make the Payment Service work." It is the step taken immediately before you start typing code.

### Key Characteristics of LLD:
* **Scope**: A single application, service, or feature.
* **Audience**: Developers, Software Engineers, and QA teams.
* **Granularity**: Extremely detailed. You are defining the exact properties of a class and what exceptions a method might throw.

### What is decided in LLD?
1. **Object-Oriented Design (OOD)**: Structuring classes to ensure loose coupling and high cohesion.
2. **Design Principles**: Applying **SOLID** principles, DRY (Don't Repeat Yourself), and KISS (Keep It Simple, Stupid).
3. **Design Patterns**: Deciding exactly where to use a `Singleton`, `Factory`, `Strategy`, or `Observer` pattern to solve an algorithmic problem gracefully. (Like the files you are currently editing in your `gof_creational` folder!)
4. **Database Schema Design**: 
    * Exact tables, columns, primary keys, and foreign keys.
    * Entity-Relationship (ER) Diagrams.
    * Indexes to speed up queries.
5. **Class & Interface Definitions**: Defining exact method signatures, access modifiers (public/private), and return types.
6. **Algorithms & Data Structures**: Choosing whether to use a `HashMap` vs `TreeMap`, or calculating the Big-O time complexity of a sorting function.

### Deliverables:
* UML Class Diagrams.
* Sequence Diagrams (showing how objects interact step-by-step).
* ER Diagrams for the database.

---

## ⚖️ HLD vs LLD Summary Timeline

| Feature | High-Level Design (HLD) | Low-Level Design (LLD) |
| :--- | :--- | :--- |
| **Focus** | System as a whole | Individual components/modules |
| **Input** | Business requirements / Product requirements | The completed HLD document |
| **Output** | Architecture Blueprint | Code Blueprint |
| **Complexity** | Scaling, Hardware, Networking | Logic, Algorithms, Object relations |
| **Mentality** | "Which database should we use?" | "Which design pattern should we use?" |

## Example: Building WhatsApp
* **HLD Phase**: You decide that users will connect via WebSockets to a Chat Server. The Chat Server sends messages to a Kafka Queue. A Cassandra database will store the chat history because it handles massive write volumes well.
* **LLD Phase**: You zoom into the "Chat Server" itself. You design a `WebSocketSessionManager` class (Singleton). You design an interface `MessageSender` with a strategy pattern implementation (`TextSender`, `ImageSender`, `VideoSender`). You outline the actual `CREATE TABLE messages (...)` schema for Cassandra.

---

## Key System Design Concepts

### CAP Theorem
A distributed system can only guarantee 2 out of 3:
- **C**onsistency: every read gets the most recent write (or an error).
- **A**vailability: every request gets a response (but may not be latest data).
- **P**artition tolerance: system continues operating if network splits.

During a network partition, you must choose C or A:
- **CP systems**: choose consistency (may be unavailable). E.g., HBase, Zookeeper.
- **AP systems**: choose availability (may return stale data). E.g., Cassandra, DynamoDB, CouchDB.
- **CA systems**: only theoretical — real networks always have partitions.

### Horizontal vs Vertical Scaling
- **Vertical (Scale-Up)**: add more CPU/RAM to one server. Simpler, has hardware limit.
- **Horizontal (Scale-Out)**: add more servers. Unlimited scale, requires stateless services or shared state (Redis/DB).

### SQL vs NoSQL
| | SQL (PostgreSQL, MySQL) | NoSQL (MongoDB, Cassandra) |
|---|---|---|
| Schema | Fixed schema | Dynamic schema |
| ACID | Full ACID | Limited (BASE — Eventually consistent) |
| Scaling | Vertical (or sharding) | Horizontal |
| Use case | Complex queries, joins, transactions | High-speed reads/writes, flexible data |

### Caching Strategies
```
Cache-Aside (Lazy Loading): App checks cache → miss → load from DB → write to cache
Write-Through: Every write goes to cache AND DB simultaneously
Write-Behind: Write to cache, async write to DB (eventual consistency)
Read-Through: Cache sits in front of DB, cache handles DB reads automatically
```

---

## Interview Questions — System Design & HLD/LLD

**Q1. How would you design a URL shortener (like bit.ly)?**
- **HLD**: Client → API Gateway → URL Shortener Service → (Cache: Redis, DB: Cassandra). CDN for static assets.
- **LLD**: `UrlShortener` service with `encode(longUrl): String` and `decode(shortCode): String`. Use base62 encoding. Store mapping in Redis (fast lookup) + Cassandra (persistence).
- **Scale concerns**: 1000 writes/sec, 100K reads/sec → read-heavy → cache aggressively.

**Q2. What is consistent hashing? Why is it used in distributed systems?**
- Maps servers and keys to the same hash ring. When a server is added/removed, only keys on the adjacent arc are redistributed (not all keys).
- Used in: Redis clusters, Cassandra, load balancers, CDNs.
- Normal hashing with N servers: changing N remaps ALL keys.
- Consistent hashing: changing N remaps only `K/N` keys (minimal disruption).

**Q3. What is the difference between a Load Balancer and an API Gateway?**
| | Load Balancer | API Gateway |
|---|---|---|
| Purpose | Distribute traffic across servers | Smart routing + security + aggregation |
| Authentication | No | Yes |
| Rate limiting | Basic | Advanced |
| SSL termination | Yes | Yes |
| Request transformation | No | Yes |
| Service discovery | Basic | Yes |

**Q4. What is database sharding?**
- Horizontal partitioning of a database across multiple servers (shards).
- Each shard holds a subset of data (e.g., users A-M on shard 1, N-Z on shard 2).
- **Benefits**: scale beyond single server capacity.
- **Challenges**: cross-shard joins, resharding when adding nodes, hotspot shards.

**Q5. What is the difference between HLD and LLD in an interview context?**
- **HLD interview** (system design): design Twitter, Netflix, Uber at scale. Focus on components, databases, scalability, availability, latency.
- **LLD interview** (OO design): design a parking lot, elevator, chess game. Focus on classes, interfaces, patterns, SOLID principles.
- HLD → CAP theorem, sharding, caching, microservices.
- LLD → SOLID, design patterns, class diagrams, UML.

**Q6. How do you approach a system design interview?**
1. **Clarify requirements** (5 min): functional & non-functional (scale, latency, availability).
2. **Capacity estimation** (5 min): QPS, storage, bandwidth.
3. **Define APIs** (5 min): key endpoints.
4. **HLD diagram** (10 min): major components and their interactions.
5. **Deep dive** (15 min): dive into the most critical components.
6. **Discuss trade-offs**: consistency vs. availability, SQL vs. NoSQL, etc.
