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

---

## 7. Common WebFlux Operators

```java
// Mono operators
Mono.just("hello")
    .map(String::toUpperCase)           // sync transform
    .flatMap(s -> fetchFromDB(s))       // async transform (returns Mono)
    .filter(s -> !s.isEmpty())          // conditional
    .defaultIfEmpty("fallback")         // default if empty
    .switchIfEmpty(Mono.error(new NotFoundException()))  // throw if empty
    .onErrorReturn("error fallback")    // fallback on error
    .onErrorResume(ex -> Mono.just("fallback"))  // reactive fallback
    .doOnNext(s -> log.info("Got: " + s))  // side effect
    .doOnError(ex -> log.error("Error", ex))
    .timeout(Duration.ofSeconds(5));    // timeout

// Flux operators
Flux.range(1, 10)
    .map(n -> n * 2)
    .filter(n -> n > 5)
    .take(3)                    // take first 3
    .skip(2)                    // skip first 2
    .distinct()                 // unique values
    .flatMap(n -> asyncOp(n))   // async, may interleave results
    .concatMap(n -> asyncOp(n)) // async, preserves order
    .buffer(5)                  // group into List<T> of size 5
    .zipWith(Flux.range(1, 10), (a, b) -> a + ":" + b)  // pair elements
    .collectList()              // Flux<T> → Mono<List<T>>
    .reduce(0, Integer::sum);   // Flux<T> → Mono<T>
```

## 8. WebClient (Reactive HTTP Client)

`WebClient` is the reactive replacement for `RestTemplate`:

```java
@Service
public class ExternalApiService {

    private final WebClient webClient;

    public ExternalApiService(WebClient.Builder builder) {
        this.webClient = builder
            .baseUrl("https://api.example.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    // GET request
    public Mono<User> getUser(Long id) {
        return webClient.get()
            .uri("/users/{id}", id)
            .retrieve()
            .bodyToMono(User.class)
            .onErrorMap(WebClientResponseException.NotFound.class,
                ex -> new UserNotFoundException("User " + id + " not found"));
    }

    // POST request
    public Mono<User> createUser(User user) {
        return webClient.post()
            .uri("/users")
            .bodyValue(user)
            .retrieve()
            .bodyToMono(User.class);
    }

    // GET list
    public Flux<User> getAllUsers() {
        return webClient.get()
            .uri("/users")
            .retrieve()
            .bodyToFlux(User.class);
    }
}
```

---

## Interview Questions — Spring WebFlux

**Q1. What is reactive programming? What problem does it solve?**
- A programming paradigm for building **non-blocking, asynchronous, event-driven** applications.
- Traditional blocking I/O ties up a thread while waiting. Under heavy load (10K concurrent requests) you need 10K threads (~10GB RAM).
- Reactive: a small number of threads (= CPU cores) handle thousands of requests by not blocking — requests yield control during I/O waits and resume when data is ready.

**Q2. What is the difference between `Mono` and `Flux`?**
| | Mono<T> | Flux<T> |
|---|---|---|
| Items | 0 or 1 | 0 to N |
| Analogy | `Optional` / `CompletableFuture` | `Stream` / `List` |
| Use case | Single record fetch, save | List of items, streaming |

**Q3. What is backpressure in reactive programming?**
- Backpressure is a mechanism where the **consumer** signals to the **producer** how much data it can handle, preventing buffer overflow.
- In Project Reactor, this is handled via the `request(n)` subscription method.
- `Flux.range(1, 1_000_000).subscribe()` won't produce all million items at once — subscriber requests them in batches.
- Operators like `buffer()`, `sample()`, `onBackpressureDrop()`, `onBackpressureBuffer()` control backpressure strategies.

**Q4. What is the difference between `flatMap()` and `concatMap()` in Flux?**
```java
Flux.range(1, 5)
    .flatMap(n -> Mono.delay(Duration.ofMillis(100-n*10)).map(t -> n))
    .subscribe(System.out::println);  // May print: 5 4 3 2 1 (out of order — concurrent)

Flux.range(1, 5)
    .concatMap(n -> Mono.delay(Duration.ofMillis(100-n*10)).map(t -> n))
    .subscribe(System.out::println);  // Always prints: 1 2 3 4 5 (preserves order)
```
- `flatMap`: subscribes to inner publishers eagerly — concurrent, order NOT preserved.
- `concatMap`: subscribes sequentially — order preserved, but slower.

**Q5. What is `Schedulers` in Project Reactor?**
```java
// Switch execution context
Mono.fromCallable(() -> blockingDbCall())  // blocking operation in reactive
    .subscribeOn(Schedulers.boundedElastic())  // run on elastic thread pool
    .publishOn(Schedulers.parallel())          // switch to parallel pool for downstream

// Common schedulers:
Schedulers.parallel()          // bounded, # CPU cores, for CPU-bound
Schedulers.boundedElastic()    // unbounded (bounded growth), for I/O/blocking ops
Schedulers.single()            // single reusable thread
Schedulers.immediate()         // current thread
```

**Q6. When should you use WebFlux vs Spring MVC?**
- **Use WebFlux**: High concurrency, I/O-heavy (calling many services), streaming (SSE/WebSocket), need non-blocking from top to bottom (reactive DB driver).
- **Use Spring MVC**: CRUD apps, blocking DB (JPA/JDBC), simpler code, small-to-medium load.
- **Java 21 Virtual Threads alternative**: For I/O-heavy workloads, Spring MVC + Virtual Threads can achieve similar throughput as WebFlux with much simpler code. Virtual Threads handle the blocking-thread problem at the JVM level.

**Q7. How do you handle errors in WebFlux?**
```java
Mono<User> result = userService.findById(id)
    .onErrorReturn(new User("default"))       // return default on any error
    .onErrorResume(NotFoundException.class,   // reactive fallback for specific exception
        ex -> userService.getDefaultUser())
    .onErrorMap(SQLException.class,            // wrap/transform exception
        ex -> new ServiceException("DB error", ex))
    .doOnError(ex -> log.error("Error", ex)); // side effect, doesn't change flow
```

**Q8. What is the difference between `subscribeOn()` and `publishOn()`?**
- `subscribeOn()`: changes the thread used for the **source** (affects upstream). Should be at the bottom of the chain. Used for blocking sources.
- `publishOn()`: changes the thread for **downstream operators** (signals). Can switch threads mid-pipeline.
```java
Flux.range(1, 10)
    .publishOn(Schedulers.parallel())   // from here down runs on parallel
    .map(n -> transform(n))
    .subscribeOn(Schedulers.single())   // source runs on single thread
    .subscribe(System.out::println);
```
