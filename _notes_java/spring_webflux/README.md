# Spring WebFlux and Reactive Programming

## 1. What is Spring WebFlux?
Spring WebFlux is a fully non-blocking, reactive web framework included in the Spring ecosystem (introduced in Spring 5). While traditional Spring MVC relies on the Servlet API and blocked threads, WebFlux is built on **Project Reactor** and uses an event-loop execution model. It is designed to handle massive concurrency with a small number of threads.

---

## 2. Why WebFlux? (The Problem with Traditional MVC)
In traditional **Spring MVC (Thread-per-request model)**:
* Every incoming request gets its own thread from a servlet container's thread pool (e.g., Tomcat defaults to 200 threads).
* If your application makes a database call or an external API call, the thread **blocks** and waits idle until the response comes back.
* Under heavy load, if all threads are blocked waiting for I/O, new requests are rejected or queued up, leading to poor scalability and high memory consumption.

In **Spring WebFlux (Event-Loop model)**:
* Uses a small number of threads (typically one per CPU core) to handle requests (e.g., via Netty).
* Operations are **non-blocking**. When a request needs to wait for a database or API, it offloads the task and immediately frees up the thread to handle other incoming requests.
* When the database/API responds, an event is triggered, and a worker thread continues processing the original request.
* **Result**: High throughput, better resource utilization, and the ability to scale to thousands of concurrent users with minimal threads.

---

## 3. Core Concepts (Project Reactor)
WebFlux uses two core data types provided by Project Reactor to represent streams of data asynchronously:

### A. Mono (0 or 1 item)
A `Mono<T>` represents an asynchronous stream that will emit **at most one** item (or an error), and then complete.
* Think of it as a reactive equivalent of `Optional<T>` or `CompletableFuture<T>`.
* Commonly used for single record lookups, saves, or API responses.
```java
// Example returning a single user
Mono<User> userMono = userRepository.findById(1);
```

### B. Flux (0 to N items)
A `Flux<T>` represents an asynchronous stream that can emit **zero to many** items (or an error), and then complete.
* Think of it as a reactive equivalent of `List<T>` or `Stream<T>`.
* Used for fetching lists, streaming data (Server-Sent Events), or continuous data flows.
```java
// Example returning a stream of users
Flux<User> usersFlux = userRepository.findAll();
```

---

## 4. Programming Models
Spring WebFlux offers two ways to write your endpoints:

### 1. Annotated Controllers (Familiar approach)
You can use the exact same annotations as Spring MVC (`@RestController`, `@GetMapping`, etc.). The only difference is what you return (`Mono` or `Flux`).

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repository; // Assume this is an R2DBC reactive repository

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return repository.findById(id);
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        return repository.findAll();
    }
}
```

### 2. Functional Endpoints (New approach)
A more lightweight, functional routing approach separating the routing configuration from the actual request handling.

```java
@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> route(UserHandler handler) {
        return RouterFunctions.route()
            .GET("/api/users/{id}", handler::getUserById)
            .GET("/api/users", handler::getAllUsers)
            .build();
    }
}

@Component
public class UserHandler {
    
    private final UserRepository repository;
    
    public UserHandler(UserRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ServerResponse.ok().body(repository.findById(id), User.class);
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return ServerResponse.ok().body(repository.findAll(), User.class);
    }
}
```

---

## 5. When SHOULD you use WebFlux?
* **High concurrency scenarios**: A massive number of requests where threads spend a lot of time waiting for IO.
* **Microservices**: Services calling other services aggressively. It pairs perfectly with the reactive `WebClient`.
* **Streaming data**: Server-sent events (SSE) or WebSockets.

## 6. When SHOULDN'T you use WebFlux?
* **If you rely on blocking APIs**: If your driver (like traditional JDBC, JPA/Hibernate) is blocking, it will tie up the few event-loop threads and crash your application's performance. You **must** use reactive database drivers (like R2DBC for SQL or reactive Mongo/Redis drivers).
* **If it's a simple CRUD app with low load**: The complexity of debugging reactive chains and mental overhead is not worth it if you don't actually have a scaling issue. Spring MVC + Virtual Threads (Java 21) is often a much easier modern solution.
