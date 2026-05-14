# Spring Data

Spring Data's mission is to provide a familiar and consistent, Spring-based programming model for data access while still retaining the special traits of the underlying data store. It makes it easy to use data access technologies, relational and non-relational databases, map-reduce frameworks, and cloud-based data services.

## Core Concepts

### 1. Repository Interface
The central abstraction in Spring Data is the `Repository` interface. It takes the domain class to manage as well as the ID type of the domain class as type arguments. This interface acts primarily as a marker interface.

```java
public interface CrudRepository<T, ID> extends Repository<T, ID> {
    <S extends T> S save(S entity);
    Optional<T> findById(ID primaryKey);
    Iterable<T> findAll();
    long count();
    void delete(T entity);
    boolean existsById(ID primaryKey);
    // ...
}
```

### 2. PagingAndSortingRepository
Provides methods to do pagination and sorting of records.

```java
public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {
    Iterable<T> findAll(Sort sort);
    Page<T> findAll(Pageable pageable);
}
```

### 3. JpaRepository (For Relational Databases)
Extends `PagingAndSortingRepository` and adds JPA-specific methods like flushing the persistence context and batch deletion.

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods go here
}
```

## Key Features

### 1. Query Methods
Spring Data allows you to define queries simply by declaring method names in your interface. The framework parses the method name and creates the query automatically.

**Example:**
```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Finds users by email
    List<User> findByEmail(String email);

    // Finds users by last name and sorts them
    List<User> findByLastNameOrderByFirstNameAsc(String lastName);

    // Finds top 3 users by age
    List<User> findTop3ByAgeGreaterThan(int age);
}
```

**Keywords for Query Methods:**
- `And`, `Or`
- `Is`, `Equals`
- `Between`, `LessThan`, `GreaterThan`
- `Like`, `NotLike`, `StartingWith`, `EndingWith`, `Containing`
- `OrderBy`, `Top`, `First`
- `True`, `False`
- `IsNull`, `IsNotNull`

### 2. Custom Queries with `@Query`
When the method name approach becomes too complex, you can write custom JPQL or native SQL queries using the `@Query` annotation.

```java
public interface UserRepository extends JpaRepository<User, Long> {
    
    // JPQL
    @Query("SELECT u FROM User u WHERE u.status = 1")
    List<User> findAllActiveUsers();

    // Native SQL
    @Query(value = "SELECT * FROM users WHERE email_address = ?1", nativeQuery = true)
    User findByEmailAddress(String emailAddress);
    
    // Named Parameters
    @Query("SELECT u FROM User u WHERE u.firstname = :firstname OR u.lastname = :lastname")
    User findByLastnameOrFirstname(@Param("lastname") String lastname, @Param("firstname") String firstname);
}
```

### 3. Pagination and Sorting
Spring Data makes pagination and sorting extremely simple using `Pageable` and `Sort` objects.

```java
// In your Service
Pageable pageable = PageRequest.of(0, 10, Sort.by("lastName").descending());
Page<User> users = userRepository.findAll(pageable);

System.out.println("Total Pages: " + users.getTotalPages());
System.out.println("Total Elements: " + users.getTotalElements());
List<User> content = users.getContent();
```

## Spring Data JPA Annotations

To map your Java objects to database tables, you use standard JPA annotations:

- `@Entity`: Marks a class as a JPA entity.
- `@Table(name = "users")`: Specifies the name of the database table to be used for mapping.
- `@Id`: Defines the primary key.
- `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Configures the way of increment of the specified column(field).
- `@Column(name = "email_address", nullable = false, unique = true)`: Specifies the mapped column for a persistent property or field.
- `@Transient`: Specifies that the property or field is not persistent.
- Relationships: `@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`.

**Entity Example:**
```java
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    // Getters and Setters
}
```

## Common Spring Data Modules

Spring Data encompasses many sub-projects to support different data stores:

1.  **Spring Data JPA:** For working with relational databases (MySQL, PostgreSQL, Oracle) via JPA/Hibernate.
2.  **Spring Data MongoDB:** For working with MongoDB document databases.
3.  **Spring Data Redis:** For working with Redis key-value stores.
4.  **Spring Data Cassandra:** For working with Apache Cassandra.
5.  **Spring Data Elasticsearch:** For working with Elasticsearch search engines.
6.  **Spring Data REST:** Exposes Spring Data repositories as hypermedia-driven RESTful resources automatically.

## Auditing

Spring Data provides transparent auditing to automatically track who created or changed an entity and when it happened.

**Annotations:**
- `@CreatedDate`
- `@LastModifiedDate`
- `@CreatedBy`
- `@LastModifiedBy`

**Setup:**
1. Enable auditing in your configuration: `@EnableJpaAuditing`
2. Add `@EntityListeners(AuditingEntityListener.class)` to your entity.

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Note {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

## Summary
Spring Data significantly reduces the amount of boilerplate code required to implement data access layers. By using interfaces and naming conventions, you can achieve powerful and optimized data operations across various types of databases with minimal effort.
