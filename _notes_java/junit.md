# JUnit 5 (Jupiter)

JUnit is the most popular testing framework for Java. JUnit 5 is the next generation of JUnit, representing a major overhaul of the framework to support modern Java features (Java 8+) and provide a more robust testing architecture.

## JUnit 5 Architecture

Unlike previous versions which were a single monolithic jar, JUnit 5 is composed of three main modules:
1.  **JUnit Platform:** The foundation that serves as a launching framework for testing frameworks on the JVM. It provides an API for IDEs and build tools (Maven/Gradle) to discover and execute tests.
2.  **JUnit Jupiter:** The new programming model and extension model for writing tests and extensions in JUnit 5. It contains all the new annotations and assertions.
3.  **JUnit Vintage:** A test engine for running older JUnit 3 and JUnit 4 based tests on the platform, ensuring backwards compatibility.

## Core Annotations

JUnit Jupiter provides several annotations to configure how tests are run.

| Annotation | Description |
| :--- | :--- |
| `@Test` | Denotes that a method is a test method. Unlike JUnit 4's `@Test` annotation, this annotation does not declare any attributes. |
| `@ParameterizedTest` | Denotes that a method is a parameterized test. |
| `@BeforeEach` | Denotes that the annotated method should be executed *before each* `@Test`, `@RepeatedTest`, or `@ParameterizedTest` method in the current class. |
| `@AfterEach` | Denotes that the annotated method should be executed *after each* test method in the current class. |
| `@BeforeAll` | Denotes that the annotated method should be executed *before all* test methods in the current class. Must be `static`. |
| `@AfterAll` | Denotes that the annotated method should be executed *after all* test methods in the current class. Must be `static`. |
| `@Disabled` | Used to disable a test class or test method (similar to `@Ignore` in JUnit 4). |
| `@DisplayName` | Declares a custom display name for the test class or test method. |
| `@RepeatedTest` | Denotes that a method is a test template for a repeated test. |
| `@Nested` | Denotes that the annotated class is a non-static nested test class. |

### Lifecycle Example

```java
import org.junit.jupiter.api.*;

@DisplayName("Math Utility Tests")
class MathUtilsTest {

    MathUtils mathUtils;

    @BeforeAll
    static void setupAll() {
        System.out.println("Executing before any test runs...");
    }

    @BeforeEach
    void setup() {
        System.out.println("Executing before each test...");
        mathUtils = new MathUtils();
    }

    @Test
    @DisplayName("Testing add method")
    void testAdd() {
        int expected = 2;
        int actual = mathUtils.add(1, 1);
        Assertions.assertEquals(expected, actual, "The add method should add two numbers");
    }

    @Test
    @Disabled("Disabled until bug #123 is fixed")
    void testSubtract() {
        Assertions.assertEquals(0, mathUtils.subtract(1, 1));
    }

    @AfterEach
    void cleanup() {
        System.out.println("Executing after each test...");
    }

    @AfterAll
    static void cleanupAll() {
        System.out.println("Executing after all tests are done...");
    }
}
```

## Assertions

Assertions are utility methods used to verify that the expected behavior matches the actual behavior. In JUnit 5, all standard assertions are static methods in the `org.junit.jupiter.api.Assertions` class.

*   `assertEquals(expected, actual)`
*   `assertTrue(condition)`
*   `assertFalse(condition)`
*   `assertNull(actual)`
*   `assertNotNull(actual)`
*   `assertArrayEquals(expectedArray, actualArray)`
*   `assertIterableEquals(expectedIterable, actualIterable)`

### Advanced Assertions

**1. Asserting Exceptions (`assertThrows`)**
Verifies that a specific piece of code throws a specific exception.

```java
@Test
void testDivideByZero() {
    MathUtils mathUtils = new MathUtils();
    
    ArithmeticException exception = Assertions.assertThrows(
        ArithmeticException.class, 
        () -> mathUtils.divide(1, 0),
        "Divide by zero should throw ArithmeticException"
    );
    
    Assertions.assertEquals("/ by zero", exception.getMessage());
}
```

**2. Grouped Assertions (`assertAll`)**
Allows executing multiple assertions together. Even if one fails, the rest will still be executed, and all failures will be reported together.

```java
@Test
void testMultiply() {
    MathUtils mathUtils = new MathUtils();
    Assertions.assertAll(
        () -> Assertions.assertEquals(4, mathUtils.multiply(2, 2)),
        () -> Assertions.assertEquals(0, mathUtils.multiply(2, 0)),
        () -> Assertions.assertEquals(-2, mathUtils.multiply(2, -1))
    );
}
```

## Assumptions

Assumptions provide a way to execute tests only if a certain condition is met. If an assumption fails, the test is aborted (skipped) rather than failing. This is useful for tests that should only run in specific environments (e.g., only on Linux, or only when a specific server is reachable).

```java
import org.junit.jupiter.api.Assumptions;

@Test
void testOnlyOnDevEnvironment() {
    boolean isDevEnv = "DEV".equals(System.getenv("ENV"));
    
    // If this evaluates to false, the test is skipped
    Assumptions.assumeTrue(isDevEnv);
    
    // Test logic here...
}
```

## Parameterized Tests

Parameterized tests make it possible to run a test multiple times with different arguments. You need the `junit-jupiter-params` dependency to use this feature.

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

class ParameterizedExampleTest {

    @ParameterizedTest
    @ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
    void palindromes(String candidate) {
        Assertions.assertTrue(StringUtils.isPalindrome(candidate));
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1, 2",
        "2, 3, 5",
        "10, -5, 5"
    })
    void testAddWithMultipleInputs(int a, int b, int expected) {
        MathUtils mathUtils = new MathUtils();
        Assertions.assertEquals(expected, mathUtils.add(a, b));
    }
}
```

## Integrating with Mockito

While JUnit is a testing framework, Mockito is a mocking framework. They are almost always used together to test components in isolation.

```xml
<!-- Maven Dependencies for Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <!-- This includes JUnit 5 (Jupiter), Mockito, and AssertJ -->
</dependency>
```

```java
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository; // Mocked dependency

    @InjectMocks
    UserService userService; // System Under Test (SUT)

    @Test
    void testGetUser() {
        // Arrange (Setup mock behavior)
        User mockUser = new User(1L, "John");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUser(1L);

        // Assert
        Assertions.assertEquals("John", result.getName());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }
}
```

### `@Mock` vs `@InjectMocks`

Understanding the difference between these two annotations is critical when using Mockito:

*   **`@Mock`**: Creates a fake (mock) instance of a class or interface. You use this for the dependencies that your class needs to function, so you can control their behavior (e.g., using `Mockito.when()`).
*   **`@InjectMocks`**: Creates a real instance of the class you are actually trying to test (the System Under Test). Mockito will automatically look for any `@Mock` objects you've declared in the test class and inject them into this real instance (via constructor, setter, or field injection).

**Rule of Thumb:**
- Use `@InjectMocks` for the **one** class you are testing.
- Use `@Mock` for **everything else** (the dependencies) that class relies on.

## Best Practices
1. **Test Isolation:** Each test should be independent. Avoid shared state.
2. **Clear Naming:** Use descriptive test method names (e.g., `calculateTotal_withDiscount_returnsCorrectAmount`). Consider `@DisplayName`.
3. **Arrange-Act-Assert (AAA):** Structure your tests clearly by grouping setup, execution, and verification.
4. **Test One Thing:** A single test method should ideally verify a single logical behavior.
5. **Mock External Dependencies:** Use Mockito to mock databases, APIs, or complex services so you are only unit testing your specific logic.
