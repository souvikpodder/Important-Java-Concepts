# Microservices Design Patterns (Detailed Guide with Examples)

Designing a microservices architecture involves solving recurring problems related to integration, data management, reliability, and observability. Here is a comprehensive overview of the essential microservice design patterns with detailed explanations and real-world examples.

---

## 1. Decomposition Patterns
How do you break down a monolithic application into microservices?

### A. Decompose by Business Capability
Services are organized around specific business functions or capabilities. 
*   **Concept:** A business capability is something a business does to generate value (e.g., Order Management, Inventory, Shipping, Marketing). Each service is responsible for a distinct business function.
*   **Example:** In an e-commerce platform:
    *   `Order Management Service`: Handles order placement, status updates, and history.
    *   `Inventory Service`: Tracks product stock levels and reserves items during checkout.
    *   `Billing Service`: Handles payment processing and invoice generation.

### B. Decompose by Subdomain (Domain-Driven Design - DDD)
Defines services corresponding to Domain-Driven Design subdomains. 
*   **Concept:** The application's domain is divided into subdomains (Core, Supporting, Generic). A service acts as an independent Bounded Context around a specific subdomain.
*   **Example:** For a restaurant delivery app:
    *   *Core Subdomain*: `Courier Tracking Service` (The unique value proposition).
    *   *Supporting Subdomain*: `Restaurant Menu Catalog Service`.
    *   *Generic Subdomain*: `Accounting Service` (Off-the-shelf software can be used, like QuickBooks integrated via API).

### C. Strangler Fig Pattern
A strategy to migrate a monolithic application to a microservices architecture gradually.
*   **Concept:** You create a new microservices-based system around the edges of the old monolithic system. As new features are built or old features are refactored, they are added to the new microservices ecosystem. An API Gateway routes traffic between the legacy monolith and the new microservices. Once all functionality is migrated, the old monolith is "strangled" and retired.
*   **Example:** Suppose you have a legacy monolithic e-commerce app. First, you pull out "User Recommendations" into a new microservice. The API Gateway routes all `/recommendations` API calls to the new service, while the rest went to the monolith. Next, you pull out "Customer Reviews". Eventually, the monolith handles zero traffic and can be shut down.

---

## 2. Integration / Connectivity Patterns
How do different clients (Web, Mobile, External APIs) interact with the microservices?

### A. API Gateway Pattern
Provides a single, unified entry point for all clients.
*   **Concept:** Instead of clients calling dozens of different microservices directly (which leads to tight coupling, chatty communication, and security nightmares), all traffic goes through an API Gateway. It handles request routing, composition, and protocol translation (e.g., HTTP to gRPC). It also centralizes cross-cutting concerns like authentication, SSL termination, rate limiting, and caching.
*   **Example:** A mobile app makes a single request to `api.myapp.com/products/123`. The API Gateway (e.g., Spring Cloud Gateway or Kong) routes this request internally to the `Product Catalog Service` (running on port 8081) and returns the JSON payload back to the app. 

### B. Aggregator Pattern
A service (or API Gateway component) that acts as a composite service.
*   **Concept:** It receives a request from the client, makes multiple internal requests to different microservices, aggregates the data, and sends a single unified response back to the client. This reduces the number of round trips the client has to make over the internet.
*   **Example:** When a user opens their "Order Details" page:
    1. The API Gateway/Aggregator receives the request.
    2. It calls the `Order Service` to get the item list.
    3. It calls the `User Service` to get customer shipping details.
    4. It calls the `Payment Service` to get the invoice status.
    5. It merges all three JSON responses into one cohesive `OrderDetailsResponse` object and sends it to the frontend.

### C. Backend for Frontend (BFF) Pattern
A variation of the API Gateway pattern where there is a separate API Gateway tailored for each specific type of client.
*   **Concept:** A mobile app needs less data and different formats compared to a heavy desktop web app. Instead of one monolithic API Gateway trying to serve everyone (and becoming bloated), you build one BFF for Mobile and one BFF for Web.
*   **Example:** 
    *   `Mobile-BFF`: Returns compressed JSON with only the essential fields needed for the small mobile screen.
    *   `Web-Desktop-BFF`: Returns highly detailed, nested JSON for the large web dashboard.

---

## 3. Data Management Patterns
How do you handle transactions and queries that span multiple independent microservices?

### A. Database per Microservice
The golden rule of microservices.
*   **Concept:** Each microservice has its own private data store. Another service cannot query this database directly; it must use the owning service's API. This ensures loose coupling and allows polyglot persistence (e.g., one service uses PostgreSQL, another uses MongoDB, another uses Neo4j).
*   **Example:** The `User Service` has a relational MySQL database to store standardized user profiles. The `Product Catalog Service` uses a Document DB like MongoDB because product attributes vary wildly between electronics and clothing. The `Search Service` uses Elasticsearch.

### B. Saga Pattern (Distributed Transactions)
Manages transactions that span multiple services.
*   **Concept:** Since traditional 2-Phase Commit (2PC) blocks resources and scales poorly in distributed systems, Sagas are used. A Saga is a sequence of local transactions. If a local transaction fails, the Saga executes a series of **compensating transactions** to undo the previous steps.
*   **Example (E-Commerce Order):**
    1. `Order Service`: Create Order (State: PENDING).
    2. `Inventory Service`: Reserve inventory items.
    3. `Payment Service`: Process credit card. *(Let's say this FAILS due to insufficient funds)*.
    4. *Compensating Transaction*: The Saga tells the `Inventory Service` to release the reserved items back to stock.
    5. *Compensating Transaction*: The Saga tells the `Order Service` to mark the order as CANCELED.
*   **Types:**
    *   **Choreography (The "Dance" Approach):** Microservices publish and subscribe to events (e.g., via Kafka) to know when to act. No central controller. Each service works independently and reacts to events from others.
        *   *Pros:* Loose coupling, no single point of failure, highly scalable.
        *   *Cons:* Hard to track overall business flow, complex debugging.
        *   *Example:* Order Service publishes `OrderCreated` -> Payment Service listens, charges card, publishes `PaymentProcessed` -> Inventory Service listens, reserves items.
    *   **Orchestration (The "Conductor" Approach):** A central "Orchestrator" service (state machine) tells other services exactly what step to execute next and waits for a response.
        *   *Pros:* Centralized visibility of the entire workflow, easier error handling and rollbacks (compensating transactions).
        *   *Cons:* Tighter coupling, risk of the orchestrator becoming a bottleneck or a bloated "God Service".
        *   *Example:* Order Orchestrator tells Payment Service "charge card" -> waits for 'done' -> tells Inventory Service "reserve items" -> waits for 'done'.

### C. CQRS (Command Query Responsibility Segregation)
Segregates operations that modify data from those that read data.
*   **Concept:** Reading data (Queries) and writing data (Commands - Create, Update, Delete) have different performance and scaling requirements. CQRS separates these into two different models, and often, two different databases. The "Write DB" handles transactions and emits events. The "Read DB" listens to these events and updates highly optimized, denormalized read models.
*   **Example:** In a social media app, updating a profile (Command) happens rarely compared to viewing a profile (Query). 
    *   Writes go to a relational database (Write Model).
    *   Reads come from a highly optimized NoSQL cache or Elasticsearch (Read Model) that syncs asynchronously from the Write DB via events.

### D. Event Sourcing
Stores the state of a business entity as a sequence of state-changing events.
*   **Concept:** Instead of updating the current state of a record in a table, you save an append-only log of events. The current state is calculated by replaying all the events from the beginning. Provides a 100% reliable audit log.
*   **Example:** A bank account. Instead of a table with `Balance: $500`, the database stores:
    1. `Account_Created` Event
    2. `Deposited_$1000` Event
    3. `Withdrawn_$500` Event
    If you replay these events, the current state is naturally $500. If the system crashes, rebuilding state is trivial.

---

## 4. Reliability & Resilience Patterns
How do you prevent a failure in one service from cascading and bringing down the entire system?

### A. Circuit Breaker Pattern
Prevents a microservice from repeatedly trying to execute an operation that's likely to fail.
*   **Concept:** Like an electrical circuit breaker. It monitors calls to a downstream service. `CLOSED` (normal operation). If failures exceed a threshold, it trips to `OPEN` (calls fail immediately or return a fallback without trying the downstream service). Periodically, it transitions to `HALF-OPEN` to test if the downstream service has recovered.
*   **Example:** `Order Service` calls `Inventory Service`. If `Inventory Service` goes down, `Order Service` will experience massive thread exhaustion waiting for timeouts. A Circuit Breaker detects that `Inventory Service` is returning 500 Errors. It "trips" the circuit. For the next 30 seconds, `Order Service` immediately returns an error to the user ("Inventory unavailable") without even trying to call `Inventory Service`, giving it time to recover. (Tools: Resilience4j, Netflix Hystrix).

### B. Bulkhead Pattern
Isolates resources to prevent systemic failure.
*   **Concept:** Named after the partitioned watertight sections of a ship's hull. Elements of an application are isolated into pools (e.g., thread pools or connection pools). If one downstream service starts hanging, it only exhausts its dedicated pool, allowing other operations to continue.
*   **Example:** An `API Gateway` routes traffic to `Service A` and `Service B`. If it uses a shared thread pool and `Service A` starts hanging, all threads will eventually be stuck waiting for `Service A`, meaning `Service B` can't be reached either. By assigning a bulkhead (max 50 threads for A, max 50 for B), `Service A`'s failure is contained.

### C. Retry Pattern with Exponential Backoff
Automatically retries a failed request transparently.
*   **Concept:** Many network errors in distributed systems are transient (temporary glitches). The service automatically retries the call. However, immediate rapid retries can overload a struggling service, so you wait exponentially longer between attempts (e.g., wait 1s, then 2s, then 4s).
*   **Example:** A `Billing Service` fails to call an external payment gateway due to a 503 HTTP status. It retries automatically after 2 seconds. The second call succeeds. The user never knew an error occurred.

---

## 5. Observability Patterns
How do you troubleshoot issues across dozens of distributed services?

### A. Distributed Tracing
Tracks a request as it flows across multiple microservices.
*   **Concept:** When a request enters the API Gateway, it generates a unique `Trace ID` and a `Span ID`. This ID is injected into HTTP headers and passed to every subsequent microservice in the chain. Tracing servers aggregate these logs to visualize the entire journey of a request and pinpoint exactly where latency or errors occurred.
*   **Example:** A user complains checkout took 5 seconds. You look up the Trace ID in zipkin/Jaeger. The UI shows a waterfall graph:
    *   Gateway (10ms) -> Order Service (20ms) -> Payment Service (4950ms) -> Inventory (20ms).
    *   You immediately know the Payment Service is the bottleneck. (Tools: Zipkin, Jaeger, OpenTelemetry).

### B. Centralized Log Aggregation
Centralizes logs from all service instances.
*   **Concept:** You can't SSH into 50 different servers to read log files. Microservices stream their logs to a central, searchable database.
*   **Example:** All microservices output logs in JSON format via standard out. A daemon (like Fluentd or Logstash) ships them to Elasticsearch. Developers use Kibana to search across all services using queries like `trace_id="abc-123" AND log_level="ERROR"`. (ELK Stack).

---

## 6. Cross-Cutting Concerns Patterns

### A. Service Registry & Discovery
Allows services to find each other dynamically.
*   **Concept:** Because microservices spin up and down dynamically (e.g., in Kubernetes), IP addresses change constantly. Hardcoding IPs is impossible. Services register their networking location (IP/Port) with a Service Registry upon startup. When `Service A` needs to call `Service B`, it asks the Registry for `Service B`'s current IP address.
*   **Example:** Spring Boot apps register with Netflix Eureka or HashiCorp Consul. The client-side load balancer looks up instances of "payment-service" and routes HTTP calls to a healthy instance.

### B. Externalized Configuration
Configuration parameters are managed separately from the code.
*   **Concept:** Hardcoding application properties (DB passwords, API limits) in code requires rebuilding and redeploying the app just to change a value. Microservices retrieve their configurations from a centralized config server at startup.
*   **Example:** Using Spring Cloud Config. When the `UserService` boots up, it fetches its `application.yml` from a central Git repository managed by the Config Server. You can change the DB password in Git, and the services can refresh their configuration without restarting.

### C. Sidecar Pattern
Deploying helper components in a separate process/container alongside the main service.
*   **Concept:** To prevent writing boilerplate code for logging, tracing, metrics, and security (mTLS) in every single microservice (especially in polyglot environments), you move these responsibilities into a proxy container (the "sidecar") deployed right next to your application container in the same pod.
*   **Example:** In a Service Mesh (like Istio or Linkerd), an Envoy proxy sidecar is injected into every Kubernetes pod. The application only communicates over `localhost` to its sidecar, and the sidecar handles all the complex network routing, retries, and encryption to other services' sidecars.
