# R2DBC (Reactive Relational Database Connectivity)

## 1. What is R2DBC?
Traditionally, calling a SQL database from Java requires JDBC (Java Database Connectivity). However, JDBC is fundamentally a **blocking API**. When your application issues a query via JDBC, the thread making the request freezes until the database finishes its work and returns the data.

**R2DBC** is a standard API designed from the ground up for non-blocking, reactive applications. It allows you to interface with relational databases (SQL) while returning reactive streams (like Project Reactor's `Mono` and `Flux`), ensuring that WebFlux's tiny pool of worker threads is never blocked waiting for a database I/O response.

---

## 2. How R2DBC Works

### The Blocking Model (JDBC)
1. Thread A receives an HTTP request.
2. Thread A sends a `SELECT` query to MySQL via JDBC.
3. Thread A **waits idle (blocks)** for 50ms while MySQL processes the query. During this time, Thread A cannot handle any other incoming HTTP requests.
4. MySQL responds. Thread A wakes up, maps the data, and returns the HTTP response.

### The Reactive Model (R2DBC)
1. Event Loop Thread receives an HTTP request.
2. Event Loop Thread sends a `SELECT` query to MySQL via R2DBC.
3. Instead of waiting, the R2DBC driver registers a callback (subscribes to a stream) and **immediately releases the Event Loop Thread**.
4. The Event Loop Thread goes off to serve dozens of other incoming HTTP requests.
5. 50ms later, MySQL's response packets hit the network card. An OS-level event occurs, triggering the R2DBC driver.
6. A worker thread picks up the database response, executes your mapping logic, and returns the HTTP response.

Because of this architecture, **you absolutely cannot use JPA / Hibernate** with R2DBC. Instead, Spring provides **Spring Data R2DBC**, which acts like a lightweight, reactive ORM.

---

## 3. Database Configurations

To use R2DBC in a Spring Boot application, you generally need two things:
1. The common Spring Data dependency: `spring-boot-starter-data-r2dbc`
2. The specific driver dependency for your exact database engine.

Unlike JDBC which uses the `jdbc:` URL format, R2DBC uses the `r2dbc:` prefix.

### A. MySQL Configuration

**Dependencies (`pom.xml`):**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-r2dbc</artifactId>
</dependency>
<!-- Note: The official r2dbc-mysql driver is maintained by asyncer -->
<dependency>
    <groupId>io.asyncer</groupId>
    <artifactId>r2dbc-mysql</artifactId>
    <version>1.0.6</version> <!-- Use latest version -->
</dependency>
```

**`application.yml` Properties:**
```yaml
spring:
  r2dbc:
    # Notice 'r2dbc:mysql://' instead of 'jdbc:mysql://'
    url: r2dbc:mysql://localhost:3306/my_database_name
    username: root
    password: my_secret_password
    pool:
      initial-size: 5
      max-size: 20
```

### B. Microsoft SQL Server Configuration

**Dependencies (`pom.xml`):**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-r2dbc</artifactId>
</dependency>
<dependency>
    <groupId>io.r2dbc</groupId>
    <artifactId>r2dbc-mssql</artifactId>
</dependency>
```

**`application.yml` Properties:**
```yaml
spring:
  r2dbc:
    # Notice 'r2dbc:mssql://'
    url: r2dbc:mssql://localhost:1433
    username: sa
    password: my_Strong_Password123!
    # Optional properties can be appended to the URL or passed directly via spring.r2dbc.properties
    properties:
      database: my_database_name
```

---

## 4. Basic Repository Example

Even though R2DBC doesn't use JPA/Hibernate, **Spring Data R2DBC** allows you to operate in a very familiar way using `@Table` and `@Id`.

```java
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users") // Belongs to Spring Data Relational, NOT javax.persistence
public class User {
    @Id
    private Long id;
    private String username;
    
    // Getters and Setters...
}
```

**The Repository Interface:**
```java
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    // Returns a Flux (0 to N items) non-blockingly
    Flux<User> findByUsernameContaining(String prefix);

    // Returns a Mono (0 or 1 item) non-blockingly
    Mono<User> findByUsername(String username);
}
```
