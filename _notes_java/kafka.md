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

### Message Ordering (Keys)

Kafka guarantees ordering **only within a single partition**. If you want messages related to the same entity (e.g., updates for User ID 123) to be processed in order, you must use a **Key** when producing the message. Kafka hashes the key to determine the partition, ensuring all messages with the same key go to the same partition.

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

### Error Handling & Dead Letter Queues (DLQ)

If a consumer repeatedly fails to process a message, you don't want it to block the entire partition. Spring Kafka supports routing failed messages to a Dead Letter Topic.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
        // Retry 3 times with a 1 second delay, then send to DLQ (original-topic-name.DLT)
        return new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(template), 
                new FixedBackOff(1000L, 3));
    }
}
```
