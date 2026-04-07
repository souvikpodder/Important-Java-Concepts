# Microservices Architecture

## 1. What are Microservices?
Microservices (or Microservices Architecture) is an architectural style that structures an application as a collection of weakly coupled, highly cohesive, independently deployable services. 
Each service represents a specific business capability, runs in its own process, and communicates with other services using lightweight protocols (typically HTTP/REST, gRPC, or message brokers).

---

## 2. Monolithic vs. Microservices

### Monolithic Architecture
In a monolith, all components of the application (UI, business logic, data access, etc.) are bundled into a single unit and deployed together.
- **Pros:** Simple to develop, test, and deploy initially. Easy to debug.
- **Cons:** Hard to scale (must scale the entire app), tightly coupled, large codebase makes it hard for new developers to understand, single point of failure.

### Microservices Architecture
The application is broken down into smaller, standalone services.
- **Pros:** Independent deployment, independent scaling, polyglot programming (different languages/tech stack for different services), fault isolation.
- **Cons:** Increased complexity (distributed system), difficult debugging/tracing, complex deployment and testing, requires mature DevOps practices.

---

## 3. Key Characteristics of Microservices
1. **Independent Deployment:** You can update and deploy one service without affecting others.
2. **Loosely Coupled:** Services have minimal dependencies on each other.
3. **Highly Cohesive:** Each service focuses on a single business capability (Single Responsibility Principle).
4. **Decentralized Data Management:** Each service manages its own database (*Database per Microservice* pattern).
5. **Smart Endpoints and Dumb Pipes:** Microservices use simple messaging protocols (like REST or RabbitMQ/Kafka) rather than complex message routing engines (like ESB).
6. **Failure Isolation:** If one microservice fails, the entire application doesn't necessarily go down.

---

## 4. Core Components of a Microservices Ecosystem

To build a robust microservices architecture, several infrastructure components are typically required:

*   **API Gateway:** A single entry point for all clients. It handles routing, composition, authentication, rate limiting, and SSL termination. (e.g., Spring Cloud Gateway, Netflix Zuul, Kong).
*   **Service Registry & Discovery:** A mechanism for services to find each other dynamically without hardcoding IP addresses. (e.g., Eureka, Consul, Zookeeper).
*   **Client-Side Load Balancer:** Distributes traffic across instances of a service. (e.g., Netflix Ribbon - legacy, Spring Cloud LoadBalancer).
*   **Centralized Configuration Server:** Manages configuration properties for all environments centrally. (e.g., Spring Cloud Config).
*   **Circuit Breaker:** Prevents cascading failures when a downstream service is down. (e.g., Resilience4j, Netflix Hystrix).
*   **Distributed Tracing:** Tracks requests as they flow across multiple microservices. (e.g., Zipkin, Jaeger, Spring Cloud Sleuth/Micrometer Tracing).
*   **Centralized Logging:** Aggregates logs from all services into a single searchable interface. (e.g., ELK Stack - Elasticsearch, Logstash, Kibana).

---

## 5. Microservices Communication

Services need to communicate with each other. This falls into two main categories:

### Synchronous Communication
The client waits for a response from the service.
- **REST (HTTP):** JSON over HTTP. Simple and widely used.
- **gRPC:** Uses HTTP/2 and Protocol Buffers. Extremely fast, lightweight, and language-independent. Often used for internal service-to-service communication.

### Asynchronous Communication (Event-Driven)
The client does not wait for a response.
- **Message Brokers:** Services publish events/messages to a queue or topic, and other services subscribe to them.
- **Technologies:** Apache Kafka, RabbitMQ, ActiveMQ, AWS SQS/SNS.
- **Benefits:** Decouples services, improves resilience, and handles traffic spikes gracefully.

---

## 6. Important Microservices Design Patterns

1. **Database per Microservice:** Each service has its own private database. This ensures loose coupling.
2. **API Gateway Pattern:** Using a single entry point to abstract internal architecture.
3. **Saga Pattern:** Manages distributed transactions. Since 2PC (Two-Phase Commit) doesn't work well in distributed systems, Saga uses a sequence of local transactions. If one step fails, compensating transactions are triggered to undo previous steps.
    *   *Choreography:* Services publish/subscribe to events without a central coordinator. Example: Order service emits `OrderCreated`, Payment service listens and charges the card. It is less coupled but harder to track.
    *   *Orchestration:* A central coordinator tells participating services what local transactions to execute. Example: An Order Orchestrator explicitly calls the Payment service to charge the card. It is easier to track but increases coupling.
4. **CQRS (Command Query Responsibility Segregation):** Separates read and write operations into different models, sometimes using separate databases (e.g., optimized read db and write db).
5. **Event Sourcing:** Instead of storing just the current state of data, all changes are stored as an append-only sequence of events.
6. **Strangler Fig Pattern:** Incrementally migrating a legacy monolithic application to microservices by replacing specific functionalities with new services one by one.
7. **Bulkhead Pattern:** Isolating resources (like thread pools) for each downstream service, so a failure in one service doesn't exhaust resources for the whole application.
8. **Sidecar Pattern:** Deploying a helper process (sidecar) alongside the primary service to handle cross-cutting concerns (logging, proxying, metrics). Often used in Service Meshes.

---

## 7. Security in Microservices
- **Authentication & Authorization:** Usually handled at the API Gateway using **OAuth 2.0** and **JWT (JSON Web Tokens)**.
- Services validate the JWT to authorize requests independently without making calls to a central auth server.
- **mTLS (Mutual TLS):** Encrypts communication *between* internal microservices and ensures both client and server authenticate each other.

---

## 8. Development & Deployment Technologies (Java/Spring Ecosystem)
- **Frameworks:** Spring Boot, Spring Cloud, Micronaut, Quarkus.
- **Containerization:** Docker (packaging applications and dependencies into standardized units).
- **Orchestration:** Kubernetes (K8s) (automating deployment, scaling, and management of containerized applications).
- **Service Mesh:** Istio, Linkerd (manages service-to-service communication, security, and observability at the infrastructure layer without changing application code).
- **CI/CD:** Jenkins, GitHub Actions, GitLab CI (Automated pipelines for building, testing, and deploying services).
