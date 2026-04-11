# Service Discovery Patterns: Kubernetes, Eureka, and Zookeeper

Service Discovery is a mechanism that allows microservices to locate each other on a network without hardcoding IP addresses. Below are examples of how this is handled using Kubernetes, Netflix Eureka, and Apache Zookeeper.

---

## 1. Kubernetes Service Discovery

In Kubernetes (K8s), you do not need an application-level registry like Eureka. Instead, Kubernetes provides **platform-level service discovery** via CoreDNS and Services.

When you create a `Service` in K8s, it gets a predictable DNS name based on its name and namespace. Other pods in the cluster can simply make HTTP requests to that DNS name, and Kubernetes handles the load balancing and routing to the underlying target pods.

### How it works:
1. You deploy an `Order-Service` and expose it via a Kubernetes `Service` named `order-service`.
2. Kubernetes DNS creates a record: `order-service.default.svc.cluster.local`.
3. The `User-Service` simply calls `http://order-service:8080/api/orders`. 

### Spring Boot Example in K8s:
You don't need any complex Spring Cloud registry dependencies. A standard `RestTemplate` works perfectly as long as it points to the Kubernetes Service name:

```java
@RestController
@RequestMapping("/users")
public class UserController {
    
    private final RestTemplate restTemplate;

    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{id}/orders")
    public String getUserOrders(@PathVariable String id) {
        // Because of Kubernetes DNS, "order-service" resolves directly to the K8s Service load balancer
        return restTemplate.getForObject("http://order-service:8080/api/orders/" + id, String.class);
    }
}
```
*(Note: You can also use the `spring-cloud-starter-kubernetes-fabric8-all` dependency if you deliberately want Spring Cloud to bridge into the K8s API directly, but native DNS mapping is adequate and preferred for most standard use cases).*

---

## 2. Netflix Eureka Service Discovery

With Eureka, you must explicitly run a **Eureka Server** component to act as the registry. Microservices (clients) register themselves with the server on startup, and they also periodically query the server to find out where other services are located.

### 2.1. The Eureka Server
**Dependency (`pom.xml`):**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

**Java Application:**
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // <--- Enables the Eureka Server registry
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

**Configuration (`application.yml`):**
```yaml
server:
  port: 8761
eureka:
  client:
    register-with-eureka: false # The Server shouldn't register itself
    fetch-registry: false 
```

### 2.2. The Eureka Client (Microservice)
**Dependency (`pom.xml`):**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**Java Application:**
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // <--- Registers this app with Eureka and enables lookups
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

**Configuration (`application.yml`):**
```yaml
spring:
  application:
    name: order-service # The logical name registered in Eureka
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/ # Pointing to the Server
```

*(To make service calls as a client, you would configure a `RestTemplate` with the `@LoadBalanced` annotation. You can then refer to the target service by its `spring.application.name`, like `http://order-service/`)*

---

## 3. Apache Zookeeper Service Discovery

Zookeeper is a centralized service widely used in Hadoop and Kafka ecosystems for maintaining configuration information and naming. Like Eureka, it can act as your application-level service registry, but it requires you to run an external Zookeeper cluster instead of a custom-built Java Spring server.

### The Zookeeper Client (Microservice)
The Spring code is incredibly similar to Eureka by design. You just add the Zookeeper dependency and configure the connection string to point to your running Zookeeper instance. 

**Dependency (`pom.xml`):**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
</dependency>
```

**Java Application:**
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // <--- Notice this is the exact same annotation used with Eureka!
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
```

**Configuration (`application.yml`):**
```yaml
spring:
  application:
    name: inventory-service
  cloud:
    zookeeper:
      connect-string: localhost:2181 # Points to the running external Zookeeper instance
      discovery:
        enabled: true
```

### 4. Summary Comparison

| Feature | Kubernetes Discovery | Netflix Eureka | Apache Zookeeper |
|---------|----------------------|----------------|------------------|
| **Architecture** | Platform-level Native Services | Application-level Java Server | Application-level External System |
| **Server Setup** | Handled implicitly by K8s | You build & deploy a Spring Boot App | Setup external Zookeeper binary/cluster |
| **Health Checks**| K8s Liveness/Readiness probes | Application Heartbeats | Zookeeper Ephemeral Nodes |
| **Best For** | Cloud-native Docker/containerized apps | Pure Spring Boot ecosystems running on VMs | System where Zookeeper is already running for distributed locking/queues |
