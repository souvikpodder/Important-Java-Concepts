# Apache Kafka & Spring Boot

Apache Kafka is a distributed event streaming platform capable of handling trillions of events a day. It is used for high-performance data pipelines, streaming analytics, data integration, and mission-critical applications.

## Core Kafka Concepts

*   **Topic:** A category or feed name to which records are published. Topics in Kafka are always multi-subscriber.
*   **Partition:** Topics are divided into partitions. Partitions allow a topic to scale beyond a single server. Records within a partition are ordered and assigned a sequential ID number called the **offset**.
*   **Producer:** Applications that publish (write) events to a Kafka topic.
*   **Consumer:** Applications that subscribe to (read) topics and process the feed of published events.
*   **Broker:** A single Kafka server. A Kafka cluster consists of multiple brokers.
*   **Consumer Group:** Consumers can be organized into consumer groups. Each message published to a topic is delivered to one consumer instance within each subscribing consumer group. This allows you to scale consumption.
*   **Zookeeper / KRaft:** Traditionally, Kafka used Zookeeper for cluster management. Modern Kafka uses KRaft (Kafka Raft) as an internal consensus protocol, removing the dependency on Zookeeper.

## Using Kafka with Spring Boot

Spring Boot provides excellent auto-configuration for Kafka through the `Spring for Apache Kafka` project.

### 1. Dependency

Add the `spring-kafka` dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### 2. Configuration (`application.yml`)

Spring Boot auto-configures the Producer and Consumer factories. You just need to provide the basic properties.

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    
    # Producer Configuration
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      
    # Consumer Configuration
    consumer:
      group-id: my-application-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
```

### 3. Producing Messages

You can use the autowired `KafkaTemplate` to send messages to a topic.

```java
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Sent message=[" + message + "] to topic=[" + topic + "]");
    }
    
    // Sending a message with a specific key (ensures order for the same key)
    public void sendMessageWithKey(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
    }
}
```

### 4. Consuming Messages

Use the `@KafkaListener` annotation to mark a method to be the target of a Kafka message listener on the specified topics.

```java
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "my-topic", groupId = "my-application-group")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
    }
    
    // Getting more details (headers, partition, offset)
    @KafkaListener(topics = "my-topic")
    public void listenWithDetails(
            @Payload String message, 
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) int offset) {
            
        System.out.println("Received Message: " + message + " from partition: " + partition);
    }
}
```

## Advanced Concepts in Spring Kafka

### JSON Serialization/Deserialization

Instead of sending raw strings, you often want to send Java Objects. Spring Kafka makes this easy with `JsonSerializer` and `JsonDeserializer`.

**application.yml:**
```yaml
spring:
  kafka:
    producer:
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "com.yourdomain.models" # Important for security!
```

### Message Ordering & Kafka Keys (Deep Dive)

A Kafka record is a key-value pair: `ProducerRecord(topic, partition, timestamp, key, value, headers)`. Both `key` and `value` are byte arrays serialized via serializers.

#### 1. How Partition Assignment Works with Keys
* **When a Key is provided**:
  Kafka runs the default partitioner using **MurmurHash2**:
  $$\text{partition} = |\text{MurmurHash2}(\text{key})| \pmod{\text{num\_partitions}}$$
  All records with the same key deterministically land in the **exact same partition**.
* **When Key is `null`**:
  Kafka (since 2.4+) uses the **UniformStickyPartitioner**. It batches messages and sends them to a single partition until the batch size/linger time is met, then switches to another partition. This drastically cuts request overhead and latency compared to older round-robin.

#### 2. Ordering Guarantees
* Kafka **only guarantees ordering within a partition**, NEVER across multiple partitions of a topic.
* Assigning an entity key (e.g., `userId`, `orderId`) guarantees that all operations for that entity are strictly ordered.

#### 3. Log Compaction & Tombstones
* In topics configured with `cleanup.policy=compact`, Kafka retains only the latest record for each key.
* If a producer publishes a record with a valid key and a `null` value, it acts as a **Tombstone marker**, signaling Kafka to permanently delete that key upon log compaction.

### Acknowledgment Modes (Committing Offsets)

By default, Spring Kafka automatically commits the offsets when the `@KafkaListener` method completes successfully. You can change this behavior for finer control (e.g., manual commits).

**application.yml:**
```yaml
spring:
  kafka:
    listener:
      ack-mode: MANUAL_IMMEDIATE
```

**Consumer:**
```java
@KafkaListener(topics = "my-topic")
public void listen(String message, Acknowledgment acknowledgment) {
    try {
        // Process message
        acknowledgment.acknowledge(); // Manually commit the offset
    } catch (Exception e) {
        // Handle failure, maybe don't acknowledge so it gets re-delivered
    }
}
```

### Concurrency for a Single Topic

How do you scale consumption throughput on a single topic? There are 4 distinct approaches:

#### 1. Partitions + Consumer Group (Standard Architecture)
* **Golden Rule**: $\text{Max active consumers in a group} = \text{Number of partitions in the topic}$.
* If a topic has **6 partitions**, you can run up to **6 parallel consumers** (in containers or pods) with the same `group.id`.
* Any extra consumers beyond partition count sit **idle** as hot standbys.

#### 2. Spring Boot Listener Concurrency (`concurrency` property)
To consume multiple partitions concurrently within a **single Spring Boot instance**, configure the `concurrency` attribute:
```java
@KafkaListener(
    topics = "orders-topic", 
    groupId = "orders-group", 
    concurrency = "3" // Spawns 3 consumer threads inside this single app instance
)
public void listen(String message) {
    System.out.println(Thread.currentThread().getName() + " processing: " + message);
}
```
Or globally via `application.yml`:
```yaml
spring:
  kafka:
    listener:
      concurrency: 3
```

#### 3. In-Memory Worker Pool Pattern (Decoupling Partitions from Processing)
If a topic has only 1 or 2 partitions, but processing each message involves a slow I/O call (e.g., 500ms payment gateway call):
* Hand off records from the Kafka polling thread to an internal `ThreadPoolExecutor` or `CompletableFuture`.
* *Trade-off*: Ordering per entity can be lost unless you route by key to a specific worker thread (Keyed Thread Pool).

#### 4. Confluent Parallel Consumer
* Client-side engine providing parallel processing *within* a single partition while guaranteeing sequential processing by record key.

---

### Error Handling & Retries in Kafka (Deep Dive)

Production error handling spans both **Consumer-side** and **Producer-side**.

#### 1. Handling Deserialization Failures (`ErrorHandlingDeserializer`)
If a message is malformed (e.g., corrupted JSON), standard deserializers throw an exception before `@KafkaListener` is ever invoked, creating an infinite poison-pill loop.
* **Fix**: Wrap serializers in `ErrorHandlingDeserializer`. It catches the error, wraps it in a `DeserializationException` header, and passes it forward cleanly to your error handler without crashing the container:
```yaml
spring:
  kafka:
    consumer:
      key-deserializer: org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
      properties:
        spring.deserializer.key.delegate.class: org.apache.kafka.common.serialization.StringDeserializer
        spring.deserializer.value.delegate.class: org.springframework.kafka.support.serializer.JsonDeserializer
```

#### 2. Blocking Retries & DLQ (`DefaultErrorHandler`)
Spring Kafka's `DefaultErrorHandler` allows configuring backoff, retryable vs. non-retryable exceptions, and Dead Letter Topic (DLT) routing:
```java
@Configuration
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
        // Sends exhausted failures to <topic-name>.DLT
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        
        // Retry 3 times with 2-second backoff
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3));
        
        // Poison pills: skip retries immediately
        errorHandler.addNotRetryableExceptions(
            IllegalArgumentException.class, 
            DeserializationException.class
        );
        return errorHandler;
    }
}
```

#### 3. Non-Blocking Retries (`@RetryableTopic`)
* **The Problem with Blocking Retries**: If message $M_1$ on Partition 0 fails and retries 5 times with a 10s backoff, the **entire partition is blocked for 50 seconds** (Head-of-Line blocking). Messages $M_2, M_3, M_4$ must wait.
* **The Solution**: Multi-Topic Non-Blocking Retries via `@RetryableTopic`.

```java
@Component
public class OrderConsumer {

    @RetryableTopic(
        attempts = "4",
        backoff = @Backoff(delay = 1000, multiplier = 2.0), // Delays: 1s, 2s, 4s
        autoCreateTopics = "true",
        exclude = { NullPointerException.class, IllegalArgumentException.class },
        dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "orders", groupId = "order-processor-group")
    public void consume(OrderEvent order) {
        processOrder(order);
    }

    @DltHandler
    public void handleDlt(OrderEvent order, @Header(KafkaHeaders.ORIGINAL_OFFSET) long offset) {
        System.err.println("Order permanently failed and routed to DLT: " + order);
    }
}
```

##### Deep Dive: Who Consumes the Retry Topics and How Do Delays Work?
1. **Who consumes retry topics?**
   * Spring dynamically registers separate hidden `@KafkaListener` containers for each retry topic (`orders-retry-1000`, `orders-retry-2000`, `orders-dlt`).
   * Each container runs in its own consumer group (e.g., `order-processor-group-retry-1000`) and internally invokes your exact same business method (`consume(order)`).
2. **How does Kafka enforce delays without native timer support?**
   * When $M_1$ fails on the main topic, Spring writes it to `orders-retry-1000` with a header `kafka_original-timestamp`.
   * The listener on `orders-retry-1000` polls the message, reads the timestamp header, and checks if the delay has elapsed.
   * If not yet elapsed, Spring calls **`consumer.pause(partition)`** for the remaining time while maintaining background heartbeats to avoid triggering consumer group rebalances. Once the timer expires, it calls **`consumer.resume(partition)`** and processes the record.
3. **Crucial Trade-off**:
   * Non-blocking retries sacrifice strict chronological ordering. Subsequent messages ($M_2, M_3$) on the main topic will finish processing before $M_1$ completes its retries.

#### 4. Producer-Side Error Handling & Reliability
* Use **`CompletableFuture` callbacks** with `KafkaTemplate.send()` to detect network drops or broker write rejections.
* Enable Idempotence (`enable.idempotence = true`) and configure `acks = all` to guarantee that message retries on transient broker errors never result in duplicates or loss.


---

## 🎯 Top Kafka Interview Deep-Dive Questions

### Q1: Does Kafka require a Key for every message? What happens if the key is `null` vs provided?
* **Answer**:
  * **No**, keys are optional (`null` is valid).
  * **When Key is provided**: Default Partitioner calculates `hash(key) % num_partitions`. All records with the identical key land on the same partition, preserving strict chronological processing order.
  * **When Key is `null`**: Kafka uses the **Sticky Partitioner** (`UniformStickyPartitioner`), batching messages to fill a batch for a partition before moving to the next partition, achieving high throughput and low broker latency without round-robin overhead.

---

### Q2: What happens to message ordering if you increase the number of partitions for an existing topic that uses keys?
* **Answer**:
  * **It breaks ordering for that key!**
  * Because partition assignment is based on `hash(key) % num_partitions`, changing the total partition count ($N \to N+k$) alters the modulo output for subsequent messages with the same key.
  * *Result*: New messages for `user-123` route to Partition B, while earlier messages may still be unconsumed or in-flight in Partition A.
  * *Best Practice*: Calculate partition scale ahead of time or route to a new topic via an intermediate event stream.

---

### Q3: How do you guarantee absolute, end-to-end global message ordering in Kafka?
* **Answer**:
  * Set topic partition count to **`1`** (`partitions = 1`).
  * Set consumer group size to **`1` consumer**.
  * On the Producer side:
    * `enable.idempotence = true` (prevents duplicate writes on retry).
    * `max.in.flight.requests.per.connection <= 5` (Kafka $\ge 1.0$) or `= 1` (older versions) to prevent reordering during network retries.
    * `acks = all` (wait for all ISR replicas to acknowledge).
  * *Trade-off*: You sacrifice Kafka's horizontal scalability and parallel consumption throughput.

---

### Q4: What is "Partition Hotspotting" (Key Skew), and how do you mitigate it?
* **Answer**:
  * **Problem**: If keys are unevenly distributed (e.g., a "Celebrity" user account or a default dummy key like `"UNKNOWN"` generating 80% of events), one partition receives the majority of the traffic, overwhelming its assigned consumer while others sit idle.
  * **Mitigation**:
    1. **Salting**: Append random salt or round-robin suffix to high-frequency keys: `orderId + "_" + (randomInt % 5)`.
    2. **Custom Partitioner**: Implement `org.apache.kafka.clients.producer.Partitioner` with custom routing logic.
    3. Separate high-volume entities into a dedicated topic.

---

### Q5: What is a "Tombstone" message and how does Log Compaction use it?
* **Answer**:
  * A tombstone is a record with a non-null **Key** and a **`null` Value**.
  * When a topic is configured with `cleanup.policy=compact`:
    * Normal messages keep only the latest value for each key over time.
    * A tombstone record tells the log cleaner to delete the key entirely after `delete.retention.ms` expires.
  * Extensively used in CDC (Change Data Capture like Debezium) and Kafka Streams KTables to represent SQL `DELETE` events.

---

### Q6: If 5 consumers are in the same Consumer Group reading a topic with 3 partitions, how many consumers will be active?
* **Answer**:
  * **Only 3 consumers will be active**. 2 consumers will remain idle as hot standbys.
  * A single partition can be assigned to **at most one consumer instance** within the same consumer group to guarantee order without lock contention.
  * To increase consumer parallelism, you must increase the partition count.

---

### Q7: What is the difference between `enable.idempotence=true` and Kafka Transactions (`TransactionalId`)?
* **Answer**:
  * **Idempotent Producer (`enable.idempotence=true`)**:
    * Guarantees exactly-once delivery **per partition for a single producer session**.
    * Broker tracks `ProducerId` (PID) + monotonically increasing `SequenceNumber` to deduplicate retried messages due to network blips.
  * **Transactional Producer (`transactional.id`)**:
    * Guarantees atomic writes **across multiple partitions and multiple topics** (e.g., Read-Process-Write pattern in Kafka Streams: committing offsets + producing output atomically).
    * Uses a 2-Phase Commit managed by Kafka's Transaction Coordinator.

---

### Q8: How can you achieve high concurrency for a single Kafka topic?
* **Answer**:
  1. **Topic Partitioning (Standard)**: Increase partition count to $N$. Run up to $N$ consumer instances in the consumer group.
  2. **Spring `concurrency` property**: Set `concurrency = N` on `@KafkaListener` to run $N$ concurrent listener threads within a single Spring Boot node.
  3. **Internal Worker Pool**: Offload slow I/O operations from the polling loop to a Java `ExecutorService` (use Key-hashed queues if entity ordering is required).
  4. **Confluent Parallel Consumer**: Client-side concurrency engine providing ordered concurrent processing keyed inside each partition.

---

### Q9: In `@RetryableTopic`, who consumes the retry topics and how is the delay enforced?
* **Answer**:
  * **Who consumes?** Spring automatically generates internal hidden `@KafkaListener` containers for each retry topic (`topic-retry-1000`, `topic-retry-2000`, etc.) and the DLT. Each runs under its own suffixed consumer group and invokes your existing listener method.
  * **How is the delay enforced?** Kafka brokers have no built-in delay queue mechanism. When a record fails, Spring writes it to the retry topic with a `kafka_original-timestamp` header. The retry container polls the message, detects remaining delay, and issues **`consumer.pause(partition)`** while maintaining heartbeats. Once the timer expires, it calls **`consumer.resume(partition)`** and processes the record.
  * **Trade-off**: Non-blocking retries break strict chronological ordering across messages on the main topic.

