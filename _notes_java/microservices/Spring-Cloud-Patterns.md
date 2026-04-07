# Spring Cloud Microservices Patterns
Detailed guide and examples for implementing API Gateway, Circuit Breaker, and Centralized Logging in a Spring Boot environment.

---

## 1. API Gateway (Spring Cloud Gateway)

### The Problem
In a microservices architecture, a client (e.g., a mobile app) shouldn't have to know the URLs of dozens of different services. Direct communication leads to tight coupling and requires the client to handle cross-cutting concerns like security, CORS, and rate limiting locally in every service.

### The Solution: Spring Cloud Gateway
Spring Cloud Gateway acts as a single entry point for all client requests. It routes requests to the appropriate microservice based on predicates (matching paths/headers) and can modify requests/responses using filters.

### Implementation Example

**Dependencies (`pom.xml`):**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<!-- For discovering services from a Service Registry like Eureka -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**Configuration (`application.yml`):**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: order-service
          uri: lb://ORDER-SERVICE  # 'lb://' uses the load balancer to find the service in Eureka
          predicates:
            - Path=/api/orders/**  # Matches any request starting with /api/orders
          filters:
            - StripPrefix=1        # Removes the first segment (/api) before forwarding
            - AddRequestHeader=X-Tenant, AcmeCorp
```
**Explanation:** If a client calls `http://gateway:8080/api/orders/123`, the Gateway forwards it to `http://ORDER-SERVICE/orders/123` and injects an `X-Tenant: AcmeCorp` header context.

---

## 2. Circuit Breaker (Resilience4j)

### The Problem
If Service A calls Service B, and Service B is down or extremely slow, Service A's threads will eventually get exhausted waiting for timeouts. This causes a cascading failure, bringing down Service A as well.

### The Solution: Resilience4j Circuit Breaker
A circuit breaker monitors external calls. If the failure rate exceeds a threshold, the circuit "opens," and subsequent calls fail fast (often returning a default/fallback response) instead of waiting. This gives the failing service time to recover.

### Implementation Example

**Dependencies (`pom.xml`):**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>  <!-- Required for annotations to work -->
</dependency>
```

**Java Code Example (`OrderService.java`):**
```java
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // When the downstream call fails, execute 'fallbackInventoryResponse'
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackInventoryResponse")
    public String checkInventory(String productId) {
        // Simulating a call to a slow or failing microservice
        return restTemplate.getForObject("http://INVENTORY-SERVICE/inventory/" + productId, String.class);
    }

    // Fallback method must have the exact same signature + Throwable parameter at the end
    public String fallbackInventoryResponse(String productId, Throwable t) {
        return "Fallback: Inventory check failed. Assuming item is OUT OF STOCK. Error: " + t.getMessage();
    }
}
```

**Configuration (`application.yml`):**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      inventoryService: # Matches the name in the @CircuitBreaker annotation
        slidingWindowSize: 10              # Look at the last 10 calls
        failureRateThreshold: 50           # Open circuit if 50% or more fail
        waitDurationInOpenState: 10000ms   # Wait 10s before transitioning to HALF-OPEN to test recovery
        permittedNumberOfCallsInHalfOpenState: 3
```

---

## 3. Centralized Logging & Tracing (ELK + Micrometer Tracing)

### The Problem
When a single user request travels sequentially starting from the Gateway -> Order Service -> Payment Service -> Inventory Service, tracking down where an error occurred is a nightmare if logs are isolated on 4 different server consoles.

### The Solution: Centralized Logging
1. **Distributed Tracing:** Use **Micrometer Tracing** (formerly Spring Cloud Sleuth) to automatically inject a `traceId` (unique per entire request) and `spanId` (unique per individual service hop) into your logs.
2. **Log Aggregation:** Stream logs formatted as structured JSON to **Logstash**, which indexes them in a database called **Elasticsearch**, making them searchable via a UI dashboard called **Kibana** (the ELK stack).

### Implementation Example

**Dependencies (`pom.xml` for Spring Boot 3):**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId> <!-- Required for observability -->
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId> <!-- Adds traceId/spanId to MDC (Mapped Diagnostic Context) -->
</dependency>
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId> <!-- Formats Spring logs as JSON output -->
</dependency>
```

**Log Format Configuration (`logback-spring.xml` in `src/main/resources`):**
Instead of writing plain text to a local console file, we configure Logback to format logs as JSON and send them directly over the network to Logstash via TCP.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- Console appender for local development -->
    <include resource="org/springframework/boot/logging/logback/base.xml"/>

    <!-- Network appender for Logstash -->
    <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <destination>your-logstash-server-ip:5000</destination>
        
        <!-- This encoder converts standard Java logs (including traceId/spanId) into structured JSON -->
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <customFields>{"app_name":"order-service"}</customFields>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="LOGSTASH" />
    </root>
</configuration>
```

**What the Logs Look Like in Kibana:**
Because of Micrometer Tracing, every single log message tied to a user's action will share the same `traceId`. 

If an error happens, you grab the `traceId` and query it in the Kibana search bar:
`traceId: "64a2b1c3e4d5f6g7"`

Kibana will then elegantly display the entire cross-network lifecycle of that request across all your microservices sequentially:
1. `INFO  [Gateway] Received request /api/orders`
2. `INFO  [OrderService] Creating order...`
3. `INFO  [PaymentService] Processing payment...`
4. `ERROR [InventoryService] Error connecting to DB...` 

You now know exactly *where* and *why* the request failed without checking 4 different log files!

---

## 4. Centralized Error Handling (@RestControllerAdvice)

### The Problem
When different microservices throw exceptions (like `UserNotFoundException` or `IllegalArgumentException`), the default Spring Boot behavior is to return a generic, ugly JSON error response or an HTML Whitelabel Error Page. This makes it very difficult for API consumers (like a frontend framework) to consistently parse error messages.

### The Solution: @RestControllerAdvice
Spring provides `@RestControllerAdvice` to intercept exceptions thrown globally across all your controllers. It allows you to catch specific exceptions and return a standardized, clean JSON error format containing an HTTP status code, a timestamp, and a helpful message.

### Implementation Example

**1. Create a Standardized Error Response Object:**
```java
import java.time.LocalDateTime;

public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // Constructors, Getters, and Setters omitted for brevity
    
    public ApiErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
```

**2. Create Custom Exceptions (Optional but Recommended):**
```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

**3. Implement the Global Exception Handler:**
```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catch specific custom exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
            
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Catch all other unhandled exceptions (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
            
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred.",
                request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

**What the API Consumer Sees:**
If someone calls `GET /api/users/999` and that user doesn't exist, instead of a stack trace, they get a consistent API contract:
```json
{
    "timestamp": "2026-03-21T10:15:30",
    "status": 404,
    "error": "Not Found",
    "message": "User with ID 999 not found",
    "path": "/api/users/999"
}
```
