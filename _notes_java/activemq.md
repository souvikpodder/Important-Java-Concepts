# Apache ActiveMQ & Spring Boot

Apache ActiveMQ is a popular, open-source, multi-protocol, Java-based message broker. It fully implements the Java Message Service (JMS) 1.1 API, offering enterprise features like clustering, multiple message stores, and ability to use any database as a JMS persistence provider.

## Core JMS & ActiveMQ Concepts

Unlike Kafka (which is a distributed event log), ActiveMQ is a traditional message broker implementing the JMS standard.

*   **JMS (Java Message Service):** A Java API that allows applications to create, send, receive, and read messages.
*   **Queue (Point-to-Point):** Messages sent to a queue are delivered to **only one** consumer. Once a consumer reads the message, it is removed from the queue. It's ideal for tasks like job processing where work should only be done once.
*   **Topic (Publish/Subscribe):** Messages sent to a topic are delivered to **all active subscribers** (consumers) of that topic. It's ideal for broadcasting events (e.g., price updates).
*   **Broker:** The ActiveMQ server that manages the routing and delivery of messages.
*   **Producer/Sender:** An application that creates and sends messages to a Queue or Topic.
*   **Consumer/Receiver:** An application that receives messages from a Queue or Topic.

## Using ActiveMQ with Spring Boot

Spring Boot provides excellent auto-configuration for JMS and ActiveMQ.

### 1. Dependency

Add the `spring-boot-starter-activemq` dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-activemq</artifactId>
</dependency>
```

*(Note: ActiveMQ "Classic" is the default. ActiveMQ "Artemis" is the newer, higher-performance version, which has its own starter: `spring-boot-starter-artemis`)*

### 2. Configuration (`application.yml`)

Spring Boot auto-configures the connection factory. You can define the broker URL and credentials. By default, Spring Boot will try to connect to a broker at `tcp://localhost:61616`.

```yaml
spring:
  activemq:
    broker-url: tcp://localhost:61616
    user: admin
    password: admin
    # By default, Spring uses Queues. To use Topics (Pub/Sub), set this to true:
  jms:
    pub-sub-domain: false 
```

*(Note: You can also use an embedded in-memory ActiveMQ broker for testing by just including the dependency without specifying a broker URL.)*

### 3. Producing Messages

You can use the autowired `JmsTemplate` to send messages to a Queue or Topic.

```java
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsProducerService {

    private final JmsTemplate jmsTemplate;

    public JmsProducerService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String destinationName, String message) {
        // destinationName is the name of your Queue (or Topic)
        jmsTemplate.convertAndSend(destinationName, message);
        System.out.println("Sent message=[" + message + "] to destination=[" + destinationName + "]");
    }
}
```

### 4. Consuming Messages

Use the `@JmsListener` annotation to mark a method to be the target of a JMS message listener.

```java
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class JmsConsumerService {

    // Listens to the specified Queue (or Topic)
    @JmsListener(destination = "my-queue")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
    }
}
```

## Advanced Concepts in Spring JMS

### JSON Serialization/Deserialization

By default, `JmsTemplate` uses a `SimpleMessageConverter` which works well for Strings and `Serializable` Java objects. However, it's better practice to send messages as JSON.

You can configure a `MappingJackson2MessageConverter` to automatically convert objects to JSON.

**Configuration Class:**
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        // Send the message as a JMS TextMessage (JSON string) instead of BytesMessage
        converter.setTargetType(MessageType.TEXT);
        // This property tells the receiver what class to deserialize into
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
```

Now you can send and receive custom objects:

```java
// Producer
jmsTemplate.convertAndSend("order-queue", new Order(123, "Laptop", 1500.0));

// Consumer
@JmsListener(destination = "order-queue")
public void receiveOrder(Order order) {
    System.out.println("Received Order ID: " + order.getId());
}
```

### Enable Application to Support BOTH Queues and Topics

If `spring.jms.pub-sub-domain` is `false`, `@JmsListener` listens to Queues. If `true`, it listens to Topics. What if your application needs to do both?

You need to define a custom `JmsListenerContainerFactory` specifically for Topics.

```java
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@Configuration
public class ActiveMQConfig {

    // Custom factory for Topics
    @Bean
    public JmsListenerContainerFactory<?> topicListenerFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
            
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(true); // Enable Pub/Sub (Topics)
        return factory;
    }
}
```

Then, explicitly tell the `@JmsListener` to use the topic factory:

```java
// Listens to a Queue (uses default factory)
@JmsListener(destination = "my-queue")
public void listenToQueue(String message) { ... }

// Listens to a Topic (uses custom factory)
@JmsListener(destination = "my-topic", containerFactory = "topicListenerFactory")
public void listenToTopic(String message) { ... }
```

### Transactions

ActiveMQ supports JMS transactions. If an error occurs while processing a message, you can roll back the transaction, and the message will be redelivered.

You can enable this by adding `@Transactional` to your listener method, or by setting `sessionTransacted` on your listener factory.
