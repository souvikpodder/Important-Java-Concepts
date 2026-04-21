# Spring Boot — Complete Notes

## Table of Contents
- [What is Spring Boot?](#what-is-spring-boot)
- [Spring vs Spring Boot](#spring-vs-spring-boot)
- [Key Features of Spring Boot](#key-features-of-spring-boot)
- [Spring Boot Architecture](#spring-boot-architecture)
- [Spring Boot Project Structure](#spring-boot-project-structure)
- [Spring Boot Starters](#spring-boot-starters)
- [Spring Boot Annotations](#spring-boot-annotations)
  - [Core / Application Annotations](#1-core--application-annotations)
  - [Stereotype Annotations](#2-stereotype-annotations)
  - [REST / Web Annotations](#3-rest--web-annotations)
  - [Dependency Injection Annotations](#4-dependency-injection-annotations)
  - [Configuration & Properties Annotations](#5-configuration--properties-annotations)
  - [JPA / Database Annotations](#6-jpa--database-annotations)
  - [Validation Annotations](#7-validation-annotations)
  - [Security Annotations](#8-security-annotations)
  - [AOP Annotations](#9-aop-annotations)
  - [Scheduling & Async Annotations](#10-scheduling--async-annotations)
  - [Testing Annotations](#11-testing-annotations)
- [application.properties / application.yml](#applicationproperties--applicationyml)
- [Spring Boot Actuator](#spring-boot-actuator)
- [Spring Boot DevTools](#spring-boot-devtools)
- [Spring Boot Profiles](#spring-boot-profiles)

---

## What is Spring Boot?

**Spring Boot** is an open-source Java-based framework built on top of the **Spring Framework**. It is used to create **stand-alone, production-grade** Spring-based applications with **minimal configuration**.

> Spring Boot makes it easy to create Spring-powered applications that you can "just run".

### Why Spring Boot?
- Traditional Spring applications required extensive XML configuration and boilerplate code.
- Spring Boot eliminates this by providing **auto-configuration**, **embedded servers**, and **opinionated defaults**.

---

## Spring vs Spring Boot

| Feature                | Spring Framework                        | Spring Boot                              |
|------------------------|-----------------------------------------|------------------------------------------|
| Configuration          | Manual (XML / Java-based)               | Auto-configuration                       |
| Server                 | Requires external server (Tomcat, etc.) | Embedded server (Tomcat, Jetty, Undertow)|
| Project Setup          | Complex and time-consuming              | Quick setup via Spring Initializr        |
| Dependency Management  | Manual                                  | Managed via Starters                     |
| Boilerplate Code       | High                                    | Minimal                                  |
| Production-Ready       | Requires manual setup                   | Built-in (Actuator, Health Checks, etc.) |

---

## Key Features of Spring Boot

### 1. Auto-Configuration
Spring Boot automatically configures your application based on the dependencies present in the classpath.

```java
// If spring-boot-starter-web is on the classpath,
// Spring Boot auto-configures an embedded Tomcat server,
// a DispatcherServlet, and default error mappings — all without any XML.
```

### 2. Embedded Server
No need to deploy WAR files to an external server. Spring Boot packages an embedded server (Tomcat by default).

```bash
# Run your app like a regular Java program
java -jar myapp.jar
```

### 3. Spring Boot Starters
Pre-configured dependency bundles that simplify Maven/Gradle configuration.

```xml
<!-- Just add this ONE dependency to get a full web application setup -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 4. Spring Initializr
A web-based tool ([start.spring.io](https://start.spring.io)) to generate a project skeleton with dependencies pre-configured.

### 5. Opinionated Defaults
Spring Boot provides sensible default configurations that work out of the box, but allows you to override them when needed.

### 6. Production-Ready Features
- Health checks
- Metrics
- Externalized configuration
- Logging

### 7. No Code Generation & No XML Configuration
Spring Boot does not generate code and does not require XML configuration files.

---

## Spring Boot Architecture

```
┌─────────────────────────────────────────────────┐
│                  CLIENT (Browser / API)          │
└──────────────────────┬──────────────────────────┘
                       │ HTTP Request
                       ▼
┌─────────────────────────────────────────────────┐
│              CONTROLLER LAYER                    │
│         (@RestController / @Controller)          │
│  - Receives requests, validates input            │
│  - Delegates to Service layer                    │
└──────────────────────┬──────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────┐
│               SERVICE LAYER                      │
│               (@Service)                         │
│  - Contains business logic                       │
│  - Orchestrates data flow                        │
└──────────────────────┬──────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────┐
│             REPOSITORY LAYER                     │
│             (@Repository)                        │
│  - Communicates with the database                │
│  - Uses Spring Data JPA / JDBC                   │
└──────────────────────┬──────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────┐
│                DATABASE                          │
│        (MySQL, PostgreSQL, H2, etc.)             │
└─────────────────────────────────────────────────┘
```

---

## Spring Boot Project Structure

```
my-spring-boot-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/myapp/
│   │   │       ├── MyAppApplication.java        ← Main class
│   │   │       ├── controller/
│   │   │       │   └── UserController.java      ← REST endpoints
│   │   │       ├── service/
│   │   │       │   └── UserService.java         ← Business logic
│   │   │       ├── repository/
│   │   │       │   └── UserRepository.java      ← Data access
│   │   │       ├── model/
│   │   │       │   └── User.java                ← Entity / DTO
│   │   │       └── config/
│   │   │           └── AppConfig.java           ← Custom config
│   │   └── resources/
│   │       ├── application.properties           ← App configuration
│   │       ├── application.yml                  ← Alternative config
│   │       ├── static/                          ← Static files (CSS, JS)
│   │       └── templates/                       ← Thymeleaf templates
│   └── test/
│       └── java/
│           └── com/example/myapp/
│               └── MyAppApplicationTests.java   ← Unit/Integration tests
├── pom.xml                                      ← Maven dependencies
└── mvnw / mvnw.cmd                              ← Maven wrapper
```

---

## Spring Boot Starters

Starters are pre-packaged dependency descriptors. Instead of searching for compatible libraries, just add a starter.

| Starter                              | Purpose                                     |
|--------------------------------------|---------------------------------------------|
| `spring-boot-starter-web`            | Build RESTful web apps (includes Tomcat)     |
| `spring-boot-starter-data-jpa`       | JPA + Hibernate for database access          |
| `spring-boot-starter-security`       | Spring Security for authentication & auth    |
| `spring-boot-starter-thymeleaf`      | Thymeleaf template engine                    |
| `spring-boot-starter-test`           | Testing (JUnit, Mockito, Spring Test)        |
| `spring-boot-starter-actuator`       | Production monitoring & health checks        |
| `spring-boot-starter-mail`           | Email support                                |
| `spring-boot-starter-validation`     | Bean validation (Hibernate Validator)        |
| `spring-boot-starter-cache`          | Caching support                              |
| `spring-boot-starter-aop`           | Aspect-Oriented Programming                  |

---

## Spring Boot Annotations

### 1. Core / Application Annotations

#### `@SpringBootApplication`
The **most important** annotation. It is a combination of three annotations:
- `@Configuration` — Marks the class as a source of bean definitions
- `@EnableAutoConfiguration` — Enables auto-configuration
- `@ComponentScan` — Scans for components in the current package and sub-packages

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyAppApplication.class, args);
    }
}
```

#### `@Configuration`
Indicates that a class declares one or more `@Bean` methods. Spring container processes these to generate bean definitions.

```java
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
```

#### `@Bean`
Marks a method as a **bean producer**. The returned object is registered in the Spring IoC container.

```java
@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
        ds.setUsername("root");
        ds.setPassword("password");
        return ds;
    }
}
```

#### `@EnableAutoConfiguration`
Tells Spring Boot to automatically configure beans based on classpath dependencies. Usually not used directly (included in `@SpringBootApplication`).

```java
@EnableAutoConfiguration
public class MyApp {
    // Spring Boot will auto-configure DataSource if H2/MySQL driver is on classpath
}
```

#### `@ComponentScan`
Tells Spring where to look for annotated components. By default, it scans the package of the annotated class and all sub-packages.

```java
@ComponentScan(basePackages = {"com.example.service", "com.example.repository"})
@Configuration
public class AppConfig {
}
```

---

### 2. Stereotype Annotations

These annotations mark classes as Spring-managed components (beans).

#### `@Component`
Generic annotation for any Spring-managed component. It's the **parent** of `@Service`, `@Repository`, and `@Controller`.

```java
@Component
public class EmailHelper {

    public void sendEmail(String to, String body) {
        // email sending logic
        System.out.println("Sending email to: " + to);
    }
}
```

#### `@Service`
Marks a class as a **service layer** component. Semantically indicates business logic.

```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
```

#### `@Repository`
Marks a class as a **data access layer** component. Enables automatic exception translation (converts database exceptions to Spring's `DataAccessException`).

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA auto-generates the implementation!
    List<User> findByName(String name);

    List<User> findByAgeGreaterThan(int age);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
```

#### `@Controller`
Marks a class as a **web controller** (MVC). Used with view templates like Thymeleaf.

```java
@Controller
public class HomeController {

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("message", "Welcome to Spring Boot!");
        return "home"; // returns home.html (Thymeleaf template)
    }
}
```

---

### 3. REST / Web Annotations

#### `@RestController`
Combination of `@Controller` + `@ResponseBody`. Every method returns data directly (JSON/XML) instead of a view.

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully!";
    }
}
```

#### `@RequestMapping`
Maps HTTP requests to handler methods. Can specify path, method, produces, consumes.

```java
@RestController
@RequestMapping(value = "/api/products", produces = "application/json")
public class ProductController {

    // Maps to: GET /api/products
    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getAll() {
        return productService.findAll();
    }

    // Maps to: GET /api/products/search?name=phone
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Product> search(@RequestParam String name) {
        return productService.searchByName(name);
    }
}
```

#### `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`
Shortcut annotations for specific HTTP methods.

```java
@GetMapping("/users")           // GET    → Retrieve data
@PostMapping("/users")          // POST   → Create data
@PutMapping("/users/{id}")      // PUT    → Update entire resource
@PatchMapping("/users/{id}")    // PATCH  → Partial update
@DeleteMapping("/users/{id}")   // DELETE → Remove data
```

#### `@PathVariable`
Extracts a value from the **URL path**.

```java
// URL: /api/users/42
@GetMapping("/api/users/{id}")
public User getUser(@PathVariable("id") Long userId) {
    return userService.getUserById(userId);
}

// Multiple path variables
// URL: /api/departments/5/employees/12
@GetMapping("/api/departments/{deptId}/employees/{empId}")
public Employee getEmployee(@PathVariable Long deptId, @PathVariable Long empId) {
    return employeeService.findByDeptAndId(deptId, empId);
}
```

#### `@RequestParam`
Extracts a value from the **query string** (`?key=value`).

```java
// URL: /api/users/search?name=John&age=25
@GetMapping("/api/users/search")
public List<User> searchUsers(
        @RequestParam String name,
        @RequestParam(required = false, defaultValue = "0") int age) {
    return userService.search(name, age);
}
```

#### `@RequestBody`
Binds the **HTTP request body** (JSON/XML) to a Java object.

```java
// POST /api/users
// Body: { "name": "John", "email": "john@email.com", "age": 25 }
@PostMapping("/api/users")
public User createUser(@RequestBody User user) {
    return userService.createUser(user);
}
```

#### `@ResponseBody`
Tells Spring to write the return value directly to the HTTP response body (as JSON). Not needed with `@RestController` (it's already included).

```java
@Controller
public class ApiController {

    @GetMapping("/api/status")
    @ResponseBody   // Without this, Spring would look for a view template
    public Map<String, String> getStatus() {
        return Map.of("status", "UP", "version", "1.0.0");
    }
}
```

#### `@ResponseStatus`
Sets the HTTP status code for the response.

```java
@PostMapping("/api/users")
@ResponseStatus(HttpStatus.CREATED)  // Returns 201 instead of default 200
public User createUser(@RequestBody User user) {
    return userService.createUser(user);
}

// Custom exception with status
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

#### `@CrossOrigin`
Enables Cross-Origin Resource Sharing (CORS) for specific controllers or methods.

```java
@CrossOrigin(origins = "http://localhost:3000")  // Allow requests from React app
@RestController
@RequestMapping("/api/users")
public class UserController {
    // ...
}
```

---

### 4. Dependency Injection Annotations

#### `@Autowired`
Automatically injects a dependency. Can be used on constructors, setters, or fields.

There are **3 types** of injection. Here's each with a full explanation of **why** it is or isn't recommended:

---

##### ✅ Constructor Injection (RECOMMENDED — Best Practice)

```java
@Service
public class OrderService {

    private final UserService userService;
    private final ProductService productService;

    @Autowired  // Optional on single constructor (Spring 4.3+)
    public OrderService(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
}
```

**Why is Constructor Injection recommended?**

| Reason | Explanation |
|--------|-------------|
| **Immutability** | Fields can be declared `final`, so once set they can never change. This makes the object thread-safe and less error-prone. |
| **Mandatory Dependencies** | If a dependency is missing, the application **fails at startup** with a clear error ("No bean found...") instead of crashing later at runtime with a `NullPointerException`. |
| **Testability** | You can easily create the object in unit tests by simply calling the constructor with mock objects — **no Spring context or reflection needed**. |
| **No Reflection** | Spring calls the constructor normally (no reflection hacks), which is cleaner and faster. |
| **Clear Contract** | The constructor signature explicitly shows ALL the dependencies a class needs. Anyone reading the code instantly knows what this class depends on. |
| **Prevents Circular Dependencies** | If two beans depend on each other via constructors, Spring will throw an error at startup. This forces you to fix the bad design early. |

```java
// Example: Easy unit testing with constructor injection
@Test
void testOrderService() {
    // No Spring context needed! Just pass mocks directly.
    UserService mockUserService = Mockito.mock(UserService.class);
    ProductService mockProductService = Mockito.mock(ProductService.class);

    OrderService orderService = new OrderService(mockUserService, mockProductService);
    // test orderService...
}
```

---

##### ⚠️ Field Injection (NOT recommended for production)

```java
@Service
public class OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;
}
```

**Why is Field Injection NOT recommended?**

| Problem | Explanation |
|---------|-------------|
| **No Immutability** | Fields cannot be `final`, so they could be accidentally changed or left null. |
| **Hidden Dependencies** | The class looks like it has no dependencies from the outside (no constructor parameters). You only discover them by reading every `@Autowired` field. |
| **Hard to Test** | You **cannot** create the object with `new OrderService()` in a test and pass mocks — the fields are private. You need reflection or a Spring context to set them, making tests slower and more complex. |
| **Uses Reflection** | Spring uses Java reflection to forcefully set `private` fields. This bypasses normal access controls and is slower. |
| **Null at Runtime** | If you forget to set up the Spring context, the fields will be `null`, leading to `NullPointerException` at runtime instead of a clear startup error. |
| **Hides Circular Dependencies** | Circular dependencies silently work with field injection, hiding a design problem that should be fixed. |

> **When is Field Injection OK?** It's acceptable in test classes (e.g., `@SpringBootTest`) where convenience matters more than design purity, but avoid it in production code.

---

##### 🔄 Setter Injection (Use for Optional Dependencies)

```java
@Service
public class OrderService {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
```

**When to use Setter Injection?**

| Reason | Explanation |
|--------|-------------|
| **Optional Dependencies** | Use when a dependency is **not mandatory**. The object can function without it. For example, an optional cache or an optional notification service. |
| **Reconfiguration** | Allows changing the dependency after object creation (rare but sometimes needed). |
| **Avoids Constructor Bloat** | If a class has too many constructor parameters (a code smell!), setter injection can help — but the better fix is to refactor the class. |

**Why NOT use Setter Injection for required dependencies?**
- Fields cannot be `final` → no immutability
- No guarantee the setter was called → possible `NullPointerException` at runtime
- The object could be in an incomplete state between construction and setter calls

---

##### 📊 Injection Type Comparison

| Feature                  | Constructor ✅ | Field ⚠️  | Setter 🔄 |
|--------------------------|---------------|-----------|----------|
| Fields can be `final`    | ✅ Yes         | ❌ No      | ❌ No     |
| Fails fast at startup    | ✅ Yes         | ❌ No      | ❌ No     |
| Easy to unit test        | ✅ Yes         | ❌ No      | ✅ Yes    |
| No reflection needed     | ✅ Yes         | ❌ No      | ✅ Yes    |
| Shows dependencies clearly| ✅ Yes        | ❌ No      | ⚠️ Partially |
| Good for optional deps   | ❌ No          | ❌ No      | ✅ Yes    |
| Spring-recommended       | ✅ Yes         | ❌ No      | ⚠️ Situational |

#### `@Qualifier`
Used with `@Autowired` to resolve ambiguity when multiple beans of the same type exist.

```java
public interface NotificationService {
    void send(String message);
}

@Service("emailService")
public class EmailNotificationService implements NotificationService {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

@Service("smsService")
public class SmsNotificationService implements NotificationService {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

@RestController
public class AlertController {

    @Autowired
    @Qualifier("emailService")  // Specifies WHICH implementation to inject
    private NotificationService notificationService;

    @GetMapping("/alert")
    public String sendAlert() {
        notificationService.send("Server is down!");
        return "Alert sent!";
    }
}
```

#### `@Primary`
Marks a bean as the **default choice** when multiple candidates exist for autowiring.

```java
@Primary
@Service
public class EmailNotificationService implements NotificationService {
    // This will be injected by default when no @Qualifier is specified
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
```

#### `@Lazy`
Delays the initialization of a bean until it is first requested.

```java
@Service
@Lazy  // Bean is created only when first used, not at startup
public class HeavyReportService {

    public HeavyReportService() {
        System.out.println("HeavyReportService initialized!");
        // Some expensive initialization
    }
}
```

#### `@Scope`
Defines the scope (lifecycle) of a bean.

```java
@Component
@Scope("prototype")  // New instance every time it's requested
public class ShoppingCart {
    private List<String> items = new ArrayList<>();

    public void addItem(String item) {
        items.add(item);
    }
}
```

**Available Scopes:**

| Scope        | Behavior | When to Use |
|--------------|----------|-------------|
| `singleton` (default) | **One instance** per Spring container, shared across the entire app | Most beans (services, repositories, controllers). This is the default because most beans are **stateless** — they don't hold user-specific data, so one shared instance is efficient and thread-safe. |
| `prototype`  | **New instance** every time the bean is requested | Stateful objects like a `ShoppingCart` that holds data unique to each use. Each caller gets its own copy. |
| `request`    | **One instance per HTTP request** (web only) | When you need request-scoped data (e.g., request logging context). Bean is created at the start of a request and destroyed at the end. |
| `session`    | **One instance per HTTP session** (web only) | When you need to store user session data (e.g., logged-in user's preferences). Lives as long as the user's browser session. |

> **Why is `singleton` the default?** Because services, repositories, and controllers are typically **stateless** — they process requests using method parameters, not instance variables. A single shared instance saves memory and avoids the overhead of creating new objects repeatedly.

---

### 5. Configuration & Properties Annotations

#### `@Value`
Injects values from `application.properties` or environment variables into fields.

```properties
# application.properties
app.name=MySpringApp
app.version=2.5.0
server.port=8080
```

```java
@Service
public class AppInfoService {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.timeout:5000}")  // Default value if property not found
    private int timeout;

    public String getInfo() {
        return appName + " v" + appVersion;
    }
}
```

#### `@ConfigurationProperties`
Binds a group of properties to a Java object. **More type-safe and recommended over `@Value`** for grouping related properties.

**Why use `@ConfigurationProperties` over `@Value`?**

| Feature | `@Value` | `@ConfigurationProperties` |
|---------|----------|---------------------------|
| Type safety | ❌ No (fails at runtime if type mismatch) | ✅ Yes (validated at startup) |
| Grouping | ❌ Scattered `@Value` across fields | ✅ All related properties in one class |
| Refactoring | ❌ Hard (property key is a string) | ✅ Easy (just rename Java field) |
| Nested properties | ❌ Complex to handle | ✅ Maps naturally to nested Java objects |
| IDE support | ⚠️ Limited | ✅ Autocomplete, documentation |
| Best for | Individual, simple values | Groups of related config (mail, database, API keys) |

> **Rule of thumb:** Use `@Value` for one or two simple properties. Use `@ConfigurationProperties` when you have 3+ related properties or need structured/nested configuration.

```properties
# application.properties
mail.host=smtp.gmail.com
mail.port=587
mail.username=myemail@gmail.com
mail.password=secret123
mail.properties.starttls=true
```

```java
@Component
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String host;
    private int port;
    private String username;
    private String password;

    // Getters and Setters
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
```

#### `@PropertySource`
Loads a custom properties file.

```java
@Configuration
@PropertySource("classpath:custom-config.properties")
public class CustomConfig {

    @Value("${custom.property}")
    private String customProperty;
}
```

#### `@Profile`
Activates a bean only for specific profiles (dev, test, prod).

```java
@Service
@Profile("dev")
public class DevEmailService implements EmailService {
    public void send(String to, String body) {
        System.out.println("DEV MODE - Simulating email to: " + to);
    }
}

@Service
@Profile("prod")
public class ProdEmailService implements EmailService {
    public void send(String to, String body) {
        // Actual email sending logic
    }
}
```

#### `@ConditionalOnProperty`
Creates a bean only if a specific property exists and has a certain value.

```java
@Configuration
public class CacheConfig {

    @Bean
    @ConditionalOnProperty(name = "app.cache.enabled", havingValue = "true")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users", "products");
    }
}
```

---

### 6. JPA / Database Annotations

#### `@Entity`
Marks a class as a JPA entity (mapped to a database table).

#### `@Table`
Specifies the table name in the database.

#### `@Id`
Marks a field as the primary key.

#### `@GeneratedValue`
Configures how the primary key is generated.

#### `@Column`
Customizes the column mapping.

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "age")
    private int age;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors, Getters, Setters
    public User() {}

    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // getters and setters...
}
```

#### Relationship Annotations

```java
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees;
}

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)  // See explanation below
    @JoinColumn(name = "department_id")
    private Department department;
}

// --- Other relationships ---

// @OneToOne  → One user has one profile
@Entity
public class UserProfile {
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

// @ManyToMany → Students enrolled in many courses, courses have many students
@Entity
public class Student {
    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses;
}
```

**Understanding `FetchType.LAZY` vs `FetchType.EAGER`:**

| Fetch Type | Behavior | When to Use |
|------------|----------|-------------|
| `LAZY` (RECOMMENDED) | Related data is **NOT loaded from the database** until you explicitly access it (e.g., call `employee.getDepartment()`). Loads only when needed. | **Default choice.** Use for most relationships, especially `@OneToMany` and `@ManyToMany`, because loading all related data upfront can be very expensive. |
| `EAGER` | Related data is loaded **immediately** along with the parent entity, even if you never use it. | Use only when you **always** need the related data. Common for `@ManyToOne` and `@OneToOne` where the related object is small and always required. |

**Why is `LAZY` recommended?**
- **Performance:** If a `Department` has 10,000 employees, EAGER loading would fetch all 10,000 employees every time you load a department — even if you only need the department name.
- **N+1 Problem Prevention:** EAGER on collections can cause hundreds of extra database queries.
- **Memory Efficiency:** Only loads data you actually use.

> **Default behavior:** `@OneToMany` and `@ManyToMany` default to `LAZY`. `@ManyToOne` and `@OneToOne` default to `EAGER`. It's a good practice to explicitly set `LAZY` on `@ManyToOne` as well.

#### `@Transactional`
Manages database transactions. If any operation fails, **all changes are rolled back**.

```java
@Service
public class BankService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional  // Ensures atomicity — both operations succeed or both fail
    public void transferMoney(Long fromId, Long toId, double amount) {
        Account from = accountRepository.findById(fromId).orElseThrow();
        Account to = accountRepository.findById(toId).orElseThrow();

        if (from.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds!");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountRepository.save(from);
        accountRepository.save(to);
    }
}
```

---

### 7. Validation Annotations

Used with `spring-boot-starter-validation` to validate request data.

```java
public class UserDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 120, message = "Age must be at most 120")
    private int age;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;

    // Getters and Setters
}

@RestController
public class UserController {

    @PostMapping("/api/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        // If validation fails, Spring returns 400 Bad Request automatically
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    // Global exception handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }
}
```

| Annotation      | Description                          |
|-----------------|--------------------------------------|
| `@NotNull`      | Field must not be null               |
| `@NotBlank`     | String must not be null or empty     |
| `@NotEmpty`     | Collection/String must not be empty  |
| `@Size`         | String/Collection size constraints   |
| `@Min` / `@Max` | Numeric min/max value                |
| `@Email`        | Must be a valid email format         |
| `@Pattern`      | Must match a regex pattern           |
| `@Past`         | Date must be in the past             |
| `@Future`       | Date must be in the future           |
| `@Valid`        | Triggers validation on the object    |

---

### 8. Security Annotations

Used with `spring-boot-starter-security`.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @PreAuthorize("hasRole('ADMIN')")  // Method-level security
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Welcome, Admin!";
    }

    @PreAuthorize("hasRole('ADMIN') and #id == authentication.principal.id")
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        return "User deleted!";
    }
}
```

| Annotation                | Description                                          |
|---------------------------|------------------------------------------------------|
| `@EnableWebSecurity`      | Enables Spring Security configuration                |
| `@PreAuthorize`           | Method-level authorization (checked BEFORE execution)|
| `@PostAuthorize`          | Method-level authorization (checked AFTER execution) |
| `@Secured("ROLE_ADMIN")`  | Simpler form of method-level security                |
| `@EnableMethodSecurity`   | Enables `@PreAuthorize` and `@PostAuthorize`         |

---

### 9. AOP Annotations

Aspect-Oriented Programming for cross-cutting concerns (logging, security, transactions).

```java
@Aspect
@Component
public class LoggingAspect {

    // Runs BEFORE the method executes
    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println(">>> Calling: " + joinPoint.getSignature().getName());
    }

    // Runs AFTER the method executes (success or failure)
    @After("execution(* com.example.service.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("<<< Finished: " + joinPoint.getSignature().getName());
    }

    // Runs AFTER successful execution, can access return value
    @AfterReturning(pointcut = "execution(* com.example.service.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("Returned: " + result);
    }

    // Runs AFTER an exception is thrown
    @AfterThrowing(pointcut = "execution(* com.example.service.*.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        System.out.println("Exception in: " + joinPoint.getSignature().getName());
        System.out.println("Error: " + error.getMessage());
    }

    // Wraps AROUND the method — can control execution
    @Around("execution(* com.example.service.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();  // Execute the actual method
        long duration = System.currentTimeMillis() - start;
        System.out.println(joinPoint.getSignature().getName() + " took " + duration + "ms");
        return result;
    }
}
```

| Annotation        | Description                                      |
|-------------------|--------------------------------------------------|
| `@Aspect`         | Marks a class as an aspect                       |
| `@Before`         | Runs before the target method                    |
| `@After`          | Runs after the target method (always)            |
| `@AfterReturning` | Runs after successful method execution           |
| `@AfterThrowing`  | Runs after method throws an exception            |
| `@Around`         | Wraps method execution (before + after)          |

---

### 10. Scheduling & Async Annotations

#### `@Scheduled`
Runs a method at fixed intervals or cron schedules.

```java
@SpringBootApplication
@EnableScheduling  // Required to activate scheduling
public class MyAppApplication { }

@Service
public class ReportService {

    // Runs every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void generateReport() {
        System.out.println("Report generated at: " + LocalDateTime.now());
    }

    // Runs every day at 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailyCleanup() {
        System.out.println("Running daily cleanup...");
    }

    // Runs 10 seconds AFTER the previous execution finishes
    @Scheduled(fixedDelay = 10000)
    public void processQueue() {
        System.out.println("Processing queue...");
    }
}
```

#### `@Async`
Runs a method **asynchronously** in a separate thread.

```java
@SpringBootApplication
@EnableAsync  // Required to activate async
public class MyAppApplication { }

@Service
public class EmailService {

    @Async
    public void sendWelcomeEmail(String email) {
        // This runs in a separate thread — doesn't block the caller
        try {
            Thread.sleep(3000); // Simulate slow email sending
            System.out.println("Welcome email sent to: " + email);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Async
    public CompletableFuture<String> processData(String data) {
        // Async method that returns a result
        String result = "Processed: " + data;
        return CompletableFuture.completedFuture(result);
    }
}
```

---

### 11. Testing Annotations

```java
@SpringBootTest  // Loads full application context
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean  // Creates a mock and adds it to the application context
    private UserRepository userRepository;

    @Test
    void testGetUserById() {
        // Arrange
        User mockUser = new User("John", "john@email.com", 25);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertEquals("John", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }
}

@WebMvcTest(UserController.class)  // Tests only the web layer
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = List.of(new User("Alice", "alice@email.com", 30));
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value("Alice"));
    }
}

@DataJpaTest  // Tests only the JPA layer (repository tests)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByEmail() {
        // Arrange
        User user = new User("Bob", "bob@email.com", 28);
        entityManager.persistAndFlush(user);

        // Act
        Optional<User> found = userRepository.findByEmail("bob@email.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Bob", found.get().getName());
    }
}
```

| Annotation          | Description                                       |
|---------------------|---------------------------------------------------|
| `@SpringBootTest`   | Full integration test (loads entire app context)   |
| `@WebMvcTest`       | Tests only the controller layer                    |
| `@DataJpaTest`      | Tests only the JPA/repository layer                |
| `@MockBean`         | Creates a mock bean in the Spring context          |
| `@Test`             | Marks a method as a test case                      |
| `@BeforeEach`       | Runs before each test method                       |
| `@AfterEach`        | Runs after each test method                        |
| `@DisplayName`      | Custom display name for a test                     |

---

## application.properties / application.yml

The central configuration file for Spring Boot.

### Properties Format
```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=secret
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Logging
logging.level.root=INFO
logging.level.com.example=DEBUG
logging.file.name=logs/app.log

# Custom Properties
app.name=MySpringApp
app.version=1.0.0
```

### YAML Format (Alternative)
```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: secret
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

logging:
  level:
    root: INFO
    com.example: DEBUG
```

### `spring.jpa.hibernate.ddl-auto` Values

| Value          | Description                                        |
|----------------|----------------------------------------------------|
| `none`         | No action (use in production)                      |
| `validate`     | Validates schema, makes no changes                 |
| `update`       | Updates schema without dropping data               |
| `create`       | Drops and recreates schema on every startup        |
| `create-drop`  | Creates schema, drops on application shutdown      |

---

## Spring Boot Actuator

Provides **production-ready** features for monitoring and managing your application.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```properties
# Enable all actuator endpoints
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```

| Endpoint          | URL                            | Description               |
|-------------------|--------------------------------|---------------------------|
| Health            | `/actuator/health`             | Application health status |
| Info              | `/actuator/info`               | App metadata              |
| Metrics           | `/actuator/metrics`            | Application metrics       |
| Environment       | `/actuator/env`                | Environment properties    |
| Beans             | `/actuator/beans`              | All Spring beans          |
| Mappings          | `/actuator/mappings`           | All request mappings      |

---

## Spring Boot DevTools

Provides **developer-friendly features** like auto-restart and live-reload.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

**Features:**
- **Automatic Restart** — App restarts when code changes are detected
- **LiveReload** — Browser auto-refreshes when resources change
- **Disabled Caching** — Template caching is disabled for development
- **Enhanced Logging** — Auto-configuration report is logged

---

## Spring Boot Profiles

Profiles allow you to define environment-specific configurations.

### Create Profile-Specific Property Files
```
application.properties          ← Common properties
application-dev.properties      ← Development properties
application-test.properties     ← Testing properties
application-prod.properties     ← Production properties
```

### Activate a Profile
```properties
# In application.properties
spring.profiles.active=dev
```

```bash
# Via command line
java -jar myapp.jar --spring.profiles.active=prod
```

### Example
```properties
# application-dev.properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:devdb
spring.jpa.hibernate.ddl-auto=create-drop
logging.level.root=DEBUG
```

```properties
# application-prod.properties
server.port=80
spring.datasource.url=jdbc:mysql://prod-server:3306/proddb
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=WARN
```

---

## Quick Reference — Annotation Cheat Sheet

| Annotation                 | Layer         | Purpose                                       |
|----------------------------|---------------|-----------------------------------------------|
| `@SpringBootApplication`   | Application   | Main entry point, combines 3 annotations      |
| `@RestController`          | Controller    | REST API controller (returns JSON)             |
| `@Controller`              | Controller    | MVC controller (returns views)                 |
| `@Service`                 | Service       | Business logic component                       |
| `@Repository`              | Repository    | Data access component                          |
| `@Component`               | Any           | Generic Spring-managed bean                    |
| `@Autowired`               | Any           | Dependency injection                           |
| `@Qualifier`               | Any           | Resolve ambiguous beans                        |
| `@Value`                   | Any           | Inject property values                         |
| `@GetMapping`              | Controller    | Handle GET requests                            |
| `@PostMapping`             | Controller    | Handle POST requests                           |
| `@PutMapping`              | Controller    | Handle PUT requests                            |
| `@DeleteMapping`           | Controller    | Handle DELETE requests                         |
| `@PathVariable`            | Controller    | Extract URL path parameter                     |
| `@RequestParam`            | Controller    | Extract query parameter                        |
| `@RequestBody`             | Controller    | Bind request body to object                    |
| `@Entity`                  | Model         | JPA entity (database table)                    |
| `@Id`                      | Model         | Primary key                                    |
| `@Transactional`           | Service       | Manage database transactions                   |
| `@Valid`                    | Controller    | Trigger bean validation                        |
| `@Configuration`           | Config        | Declare bean configuration class               |
| `@Bean`                    | Config        | Declare a bean method                          |
| `@Profile`                 | Any           | Activate bean for specific profile             |
| `@Scheduled`               | Service       | Schedule recurring tasks                       |
| `@Async`                   | Service       | Run method asynchronously                      |
| `@SpringBootTest`          | Test          | Full application context test                  |

---

> **Created**: February 2026  
> **Topic**: Spring Boot — Features, Architecture & Annotations

---

## Interview Questions — Spring Boot

**Q1. What is Spring Boot and how is it different from the Spring Framework?**
- **Spring Framework**: a comprehensive framework requiring manual configuration of beans, XML files, or @Configuration classes. You wire everything yourself.
- **Spring Boot**: opinionated, convention-over-configuration wrapper on Spring. Provides:
  - Auto-configuration (detects classpath and configures beans automatically)
  - Embedded server (Tomcat/Jetty — no WAR deployment needed)
  - Starter dependencies (spring-boot-starter-web pulls all needed transitive deps)
  - Production-ready features (Actuator)

**Q2. What does `@SpringBootApplication` do? What annotations does it combine?**
```java
@SpringBootApplication
// Equivalent to:
@Configuration     // marks as config class, allows @Bean methods
@EnableAutoConfiguration  // enables Spring Boot's auto-configuration
@ComponentScan    // scans current package and sub-packages for @Component, @Service, etc.
public class App { ... }
```

**Q3. What is Spring Boot auto-configuration? How does it work?**
- Auto-configuration classes are listed in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` (Spring Boot 3+) or `META-INF/spring.factories` (Spring Boot 2).
- Each auto-config class uses `@ConditionalOn*` annotations to activate only when the right classes/beans are present.
```java
// Example: DataSourceAutoConfiguration activates only if JDBC is on classpath
@ConditionalOnClass(DataSource.class)
@ConditionalOnMissingBean(DataSource.class)  // but only if user hasn't defined one
public class DataSourceAutoConfiguration { ... }
```

**Q4. What is the difference between `@Component`, `@Service`, `@Repository`, and `@Controller`?**
- All are specializations of `@Component` — functionally equivalent in terms of bean creation.
- **Semantic differences**:
  - `@Repository`: enables JPA exception translation (wraps DB exceptions to Spring's `DataAccessException`).
  - `@Service`: marks business logic layer — no special behavior, just readability.
  - `@Controller`: marks MVC controller — Spring MVC recognizes it for request mapping.
  - `@RestController` = `@Controller` + `@ResponseBody`.

**Q5. What is the difference between constructor injection, setter injection, and field injection?**
```java
// Constructor injection (PREFERRED)
@Service
class UserService {
    private final UserRepository repo;
    UserService(UserRepository repo) { this.repo = repo; }  // immutable, testable
}

// Setter injection (for optional dependencies)
@Autowired
public void setRepo(UserRepository repo) { this.repo = repo; }

// Field injection (convenient but NOT recommended for production)
@Autowired
private UserRepository repo;  // hard to test without Spring context
```
- **Prefer constructor injection**: immutable fields, no null risks, works without Spring in unit tests.

**Q6. What is `@Transactional`? What are its propagation levels?**
```java
@Transactional(
    propagation = Propagation.REQUIRED,   // default: use existing tx or create new
    isolation = Isolation.READ_COMMITTED, // default depends on DB
    readOnly = true,                      // optimization for read-only queries
    rollbackFor = IOException.class,      // rollback on this exception too
    timeout = 30                          // timeout in seconds
)
public void doWork() { ... }
```

| Propagation | Behavior |
|---|---|
| `REQUIRED` | Use existing tx; create new if none (default) |
| `REQUIRES_NEW` | Always create a new tx; suspend existing |
| `SUPPORTS` | Use existing tx; run non-transactionally if none |
| `NOT_SUPPORTED` | Run non-transactionally; suspend existing tx |
| `MANDATORY` | Use existing tx; throw if none |
| `NEVER` | Run non-transactionally; throw if tx exists |
| `NESTED` | Run in nested tx if tx exists; create new if none |

**Q7. What is the N+1 problem in JPA and how do you fix it?**
```java
// N+1 problem: loading 1 department + N queries for employees
List<Department> depts = deptRepo.findAll();  // 1 query
depts.forEach(d -> d.getEmployees().size()); // N more queries!

// Fix 1: JOIN FETCH in JPQL
@Query("SELECT d FROM Department d JOIN FETCH d.employees")
List<Department> findAllWithEmployees();

// Fix 2: @EntityGraph
@EntityGraph(attributePaths = "employees")
List<Department> findAll();

// Fix 3: Batch size (reduces N+1 to N/batch_size + 1)
@BatchSize(size = 20)
@OneToMany
private List<Employee> employees;
```

**Q8. What is the difference between `@RestController` and `@Controller`?**
- `@Controller` returns view names (for MVC with templates like Thymeleaf).
- `@RestController` = `@Controller` + `@ResponseBody` — every method returns the response body directly (serialized to JSON/XML), not a view name.

**Q9. How do Spring Boot profiles work? How do you activate them?**
```bash
# Via command line
java -jar app.jar --spring.profiles.active=prod

# Via environment variable
export SPRING_PROFILES_ACTIVE=prod

# Via application.properties
spring.profiles.active=dev
```
- Profile-specific files: `application-dev.yml`, `application-prod.yml` override/extend `application.yml`.
- `@Profile("dev")` on a `@Bean` or `@Component` activates it only for that profile.

**Q10. What is Spring AOP? What problems does it solve?**
- AOP (Aspect-Oriented Programming) separates **cross-cutting concerns** (logging, security, transactions, caching) from business logic.
- Key concepts: **Aspect** (what), **JoinPoint** (where), **Pointcut** (selector for JoinPoints), **Advice** (when: `@Before`, `@After`, `@Around`).
- Spring's `@Transactional` is itself implemented as an AOP aspect.
- AOP works via **proxy objects** — Spring wraps your bean in a proxy that intercepts calls.

**Q11. What is Actuator? Which endpoints are most important in production?**
- Actuator exposes operational endpoints for monitoring/management.
- Key endpoints: `/health` (load balancer checks), `/metrics` (Micrometer metrics), `/env` (active properties), `/loggers` (change log level at runtime), `/info` (app metadata), `/shutdown` (controlled shutdown — disabled by default).
- Secure actuator in production: only expose `/health` and `/info` publicly.

**Q12. What is `@Cacheable` and how does Spring caching work?**
```java
@EnableCaching  // on @SpringBootApplication or @Configuration
public class AppConfig { }

@Service
class ProductService {
    @Cacheable(value = "products", key = "#id")  // cache result by id
    public Product findById(Long id) {
        return repository.findById(id).orElseThrow();  // not called if cached
    }

    @CacheEvict(value = "products", key = "#id")  // remove from cache
    public void update(Long id, Product p) { ... }

    @CachePut(value = "products", key = "#result.id")  // update cache
    public Product save(Product p) { return repository.save(p); }
}
```
- Default cache: `ConcurrentHashMap` (in-memory, JVM-local).
- For distributed caching: use Redis with `spring-boot-starter-data-redis`.
