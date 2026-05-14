# SQL Interview Questions and Trick Questions

This note covers common SQL interview questions, tricky SQL behavior, query-writing patterns, performance concepts, and transaction/database fundamentals. Examples are mostly SQL Server friendly, but most concepts apply to MySQL, PostgreSQL, Oracle, and other relational databases.

---

## Quick Revision

| Topic | Important Points |
|---|---|
| SQL | Language used to query and manage relational databases |
| DDL | `CREATE`, `ALTER`, `DROP`, `TRUNCATE` |
| DML | `INSERT`, `UPDATE`, `DELETE`, `MERGE` |
| DQL | `SELECT` |
| DCL | `GRANT`, `REVOKE` |
| TCL | `COMMIT`, `ROLLBACK`, `SAVEPOINT` |
| Primary Key | Uniquely identifies each row; cannot be NULL |
| Foreign Key | Enforces relationship with another table |
| Unique Key | Prevents duplicate values; NULL behavior depends on database |
| Index | Data structure that improves read/search performance |
| View | Saved query treated like a virtual table |
| Stored Procedure | Saved executable SQL logic |
| Trigger | SQL logic executed automatically after/before data changes |
| Transaction | Group of operations treated as one logical unit |

---

## Sample Tables Used in Examples

```sql
CREATE TABLE Departments (
    DeptId INT PRIMARY KEY,
    DeptName VARCHAR(50)
);

CREATE TABLE Employees (
    EmpId INT PRIMARY KEY,
    EmpName VARCHAR(50),
    DeptId INT NULL,
    Salary DECIMAL(10, 2),
    ManagerId INT NULL,
    HireDate DATE,
    Email VARCHAR(100),
    FOREIGN KEY (DeptId) REFERENCES Departments(DeptId)
);
```

---

## Basic Interview Questions

### 1. What is SQL?

SQL stands for **Structured Query Language**. It is used to store, retrieve, update, delete, and manage data in relational databases.

Example:

```sql
SELECT EmpName, Salary
FROM Employees
WHERE Salary > 50000;
```

---

### 2. What is the difference between SQL and NoSQL?

| SQL | NoSQL |
|---|---|
| Relational database | Non-relational database |
| Fixed schema | Flexible schema |
| Uses tables, rows, columns | Uses documents, key-value, graph, wide-column, etc. |
| Best for structured data | Best for flexible or high-scale unstructured data |
| Supports joins | Joins are limited or avoided |
| Strong ACID support | Often optimized for scalability and availability |

Examples:

- SQL: SQL Server, MySQL, PostgreSQL, Oracle
- NoSQL: MongoDB, Cassandra, Redis, DynamoDB

---

### 3. What are DDL, DML, DCL, TCL, and DQL?

| Type | Full Form | Commands |
|---|---|---|
| DDL | Data Definition Language | `CREATE`, `ALTER`, `DROP`, `TRUNCATE` |
| DML | Data Manipulation Language | `INSERT`, `UPDATE`, `DELETE`, `MERGE` |
| DQL | Data Query Language | `SELECT` |
| DCL | Data Control Language | `GRANT`, `REVOKE` |
| TCL | Transaction Control Language | `COMMIT`, `ROLLBACK`, `SAVEPOINT` |

---

### 4. What is a primary key?

A **primary key** uniquely identifies each row in a table.

Rules:

- Must be unique
- Cannot be NULL
- One table can have only one primary key
- Can be made from one column or multiple columns

```sql
CREATE TABLE Users (
    UserId INT PRIMARY KEY,
    Email VARCHAR(100)
);
```

---

### 5. What is a composite key?

A **composite key** is a key made from multiple columns.

```sql
CREATE TABLE StudentCourses (
    StudentId INT,
    CourseId INT,
    EnrolledOn DATE,
    PRIMARY KEY (StudentId, CourseId)
);
```

Here, neither `StudentId` nor `CourseId` alone is unique, but their combination is unique.

---

### 6. What is a foreign key?

A **foreign key** links one table to another table and enforces referential integrity.

```sql
CREATE TABLE Orders (
    OrderId INT PRIMARY KEY,
    CustomerId INT,
    FOREIGN KEY (CustomerId) REFERENCES Customers(CustomerId)
);
```

This prevents inserting an order for a customer that does not exist.

---

### 7. What is the difference between primary key and unique key?

| Primary Key | Unique Key |
|---|---|
| Uniquely identifies a row | Ensures column values are unique |
| Cannot contain NULL | May allow NULL depending on database |
| Only one primary key per table | Multiple unique keys allowed |
| Usually creates clustered index in SQL Server by default | Usually creates non-clustered index by default |

---

### 8. What is normalization?

**Normalization** is the process of organizing data to reduce duplication and improve data integrity.

Common normal forms:

| Normal Form | Meaning |
|---|---|
| 1NF | Atomic values; no repeating groups |
| 2NF | 1NF + no partial dependency on composite key |
| 3NF | 2NF + no transitive dependency |
| BCNF | Stronger version of 3NF |

Example problem:

```text
OrderId, CustomerName, CustomerAddress, ProductName, ProductPrice
```

Customer details and product details are repeated for every order. Normalization separates this into `Customers`, `Products`, and `Orders`.

---

### 9. What is denormalization?

**Denormalization** intentionally adds duplicate or precomputed data to improve read performance.

Example:

```text
Orders table stores CustomerName along with CustomerId
```

This avoids joining with `Customers` for every report, but it increases update complexity.

Use denormalization when:

- Reads are much more frequent than writes
- Joins are expensive
- Reporting queries need faster response

---

## Query Execution and Clauses

### 10. What is the logical execution order of a SQL query?

SQL is not logically processed in the same order it is written.

Written order:

```sql
SELECT DeptId, COUNT(*) AS EmployeeCount
FROM Employees
WHERE Salary > 50000
GROUP BY DeptId
HAVING COUNT(*) > 2
ORDER BY EmployeeCount DESC;
```

Logical order:

```text
FROM
JOIN
WHERE
GROUP BY
HAVING
SELECT
DISTINCT
ORDER BY
OFFSET / LIMIT / TOP
```

**Trick point:** You usually cannot use a `SELECT` alias in `WHERE` because `WHERE` runs before `SELECT`.

---

### 11. What is the difference between WHERE and HAVING?

| WHERE | HAVING |
|---|---|
| Filters rows before grouping | Filters groups after grouping |
| Cannot use aggregate functions directly | Can use aggregate functions |
| Runs before `GROUP BY` | Runs after `GROUP BY` |

```sql
SELECT DeptId, COUNT(*) AS EmployeeCount
FROM Employees
WHERE Salary > 50000
GROUP BY DeptId
HAVING COUNT(*) >= 3;
```

`WHERE` filters employees first. `HAVING` filters departments after grouping.

---

### 12. What is the difference between GROUP BY and DISTINCT?

`DISTINCT` removes duplicate rows from the final result.

```sql
SELECT DISTINCT DeptId
FROM Employees;
```

`GROUP BY` groups rows and is commonly used with aggregate functions.

```sql
SELECT DeptId, COUNT(*) AS EmployeeCount
FROM Employees
GROUP BY DeptId;
```

Trick point:

```sql
SELECT DISTINCT DeptId, EmpName
FROM Employees;
```

This gives distinct combinations of `DeptId` and `EmpName`, not distinct departments only.

---

### 13. What is the difference between ORDER BY and GROUP BY?

| GROUP BY | ORDER BY |
|---|---|
| Combines rows into groups | Sorts final result |
| Often used with aggregates | Used for display order |
| Runs before `SELECT` logically | Runs near the end |

---

## Join Questions

### 14. What is an INNER JOIN?

`INNER JOIN` returns only matching rows from both tables.

```sql
SELECT e.EmpName, d.DeptName
FROM Employees e
INNER JOIN Departments d
    ON e.DeptId = d.DeptId;
```

Employees without a matching department are not returned.

---

### 15. What is a LEFT JOIN?

`LEFT JOIN` returns all rows from the left table and matching rows from the right table.

```sql
SELECT e.EmpName, d.DeptName
FROM Employees e
LEFT JOIN Departments d
    ON e.DeptId = d.DeptId;
```

If an employee has no department, department columns return `NULL`.

---

### 16. How do you find employees without a department?

```sql
SELECT e.*
FROM Employees e
LEFT JOIN Departments d
    ON e.DeptId = d.DeptId
WHERE d.DeptId IS NULL;
```

This is an anti-join pattern.

---

### 17. What is a SELF JOIN?

A **self join** joins a table with itself.

Example: employee and manager from the same table.

```sql
SELECT
    e.EmpName AS Employee,
    m.EmpName AS Manager
FROM Employees e
LEFT JOIN Employees m
    ON e.ManagerId = m.EmpId;
```

---

### 18. What is a CROSS JOIN?

`CROSS JOIN` returns the Cartesian product of two tables.

```sql
SELECT e.EmpName, d.DeptName
FROM Employees e
CROSS JOIN Departments d;
```

If `Employees` has 10 rows and `Departments` has 5 rows, result has 50 rows.

---

### 19. What is the difference between ON and WHERE in a LEFT JOIN?

This is a common trick question.

Filter in `ON` keeps unmatched left rows:

```sql
SELECT e.EmpName, d.DeptName
FROM Employees e
LEFT JOIN Departments d
    ON e.DeptId = d.DeptId
   AND d.DeptName = 'IT';
```

Filter in `WHERE` can turn the left join into an inner join:

```sql
SELECT e.EmpName, d.DeptName
FROM Employees e
LEFT JOIN Departments d
    ON e.DeptId = d.DeptId
WHERE d.DeptName = 'IT';
```

The second query removes rows where `d.DeptName` is `NULL`.

---

## NULL Trick Questions

### 20. What is NULL in SQL?

`NULL` means unknown, missing, or not applicable. It is not equal to zero, empty string, or space.

Wrong:

```sql
SELECT *
FROM Employees
WHERE Email = NULL;
```

Correct:

```sql
SELECT *
FROM Employees
WHERE Email IS NULL;
```

---

### 21. What is the result of NULL = NULL?

The result is not `TRUE`. It is `UNKNOWN`.

```sql
SELECT *
FROM Employees
WHERE NULL = NULL;
```

This returns no rows because `WHERE` only keeps rows where the condition is `TRUE`.

Use:

```sql
WHERE column_name IS NULL
```

---

### 22. What is three-valued logic?

SQL conditions can evaluate to:

```text
TRUE
FALSE
UNKNOWN
```

Any comparison with `NULL` usually gives `UNKNOWN`.

```sql
Salary > NULL
Salary = NULL
Salary <> NULL
```

All evaluate to `UNKNOWN`.

---

### 23. What is the difference between COUNT(*), COUNT(1), and COUNT(column)?

| Expression | Meaning |
|---|---|
| `COUNT(*)` | Counts all rows |
| `COUNT(1)` | Counts all rows |
| `COUNT(column)` | Counts only rows where column is not NULL |

```sql
SELECT
    COUNT(*) AS TotalRows,
    COUNT(Email) AS RowsWithEmail
FROM Employees;
```

Trick point: `COUNT(column)` ignores `NULL`.

---

### 24. What is the difference between COALESCE and ISNULL?

| COALESCE | ISNULL |
|---|---|
| ANSI SQL standard | SQL Server specific |
| Accepts multiple arguments | Accepts two arguments |
| Returns first non-NULL value | Replaces NULL with fallback |
| Type resolution follows SQL standard rules | Return type follows first argument in SQL Server |

```sql
SELECT COALESCE(Email, 'No Email')
FROM Employees;

SELECT ISNULL(Email, 'No Email')
FROM Employees;
```

---

### 25. What does NULLIF do?

`NULLIF(a, b)` returns `NULL` if `a = b`, otherwise returns `a`.

Useful to avoid divide-by-zero:

```sql
SELECT TotalMarks / NULLIF(SubjectCount, 0) AS AverageMarks
FROM StudentMarks;
```

---

## Aggregation and Window Functions

### 26. How do you find the second highest salary?

Using `DENSE_RANK()`:

```sql
WITH RankedEmployees AS (
    SELECT
        EmpName,
        Salary,
        DENSE_RANK() OVER (ORDER BY Salary DESC) AS SalaryRank
    FROM Employees
)
SELECT EmpName, Salary
FROM RankedEmployees
WHERE SalaryRank = 2;
```

This handles ties correctly.

---

### 27. What is the difference between ROW_NUMBER, RANK, and DENSE_RANK?

Given salaries:

```text
100, 90, 90, 80
```

| Function | Result |
|---|---|
| `ROW_NUMBER()` | 1, 2, 3, 4 |
| `RANK()` | 1, 2, 2, 4 |
| `DENSE_RANK()` | 1, 2, 2, 3 |

Example:

```sql
SELECT
    EmpName,
    Salary,
    ROW_NUMBER() OVER (ORDER BY Salary DESC) AS RowNo,
    RANK() OVER (ORDER BY Salary DESC) AS RankNo,
    DENSE_RANK() OVER (ORDER BY Salary DESC) AS DenseRankNo
FROM Employees;
```

---

### 28. How do you find the highest salary in each department?

```sql
WITH RankedEmployees AS (
    SELECT
        EmpName,
        DeptId,
        Salary,
        DENSE_RANK() OVER (
            PARTITION BY DeptId
            ORDER BY Salary DESC
        ) AS SalaryRank
    FROM Employees
)
SELECT EmpName, DeptId, Salary
FROM RankedEmployees
WHERE SalaryRank = 1;
```

`PARTITION BY` restarts ranking for each department.

---

### 29. What is the difference between aggregate function and window function?

Aggregate function collapses rows:

```sql
SELECT DeptId, AVG(Salary) AS AvgSalary
FROM Employees
GROUP BY DeptId;
```

Window function keeps row detail:

```sql
SELECT
    EmpName,
    DeptId,
    Salary,
    AVG(Salary) OVER (PARTITION BY DeptId) AS DeptAvgSalary
FROM Employees;
```

---

### 30. How do you find duplicate emails?

```sql
SELECT Email, COUNT(*) AS DuplicateCount
FROM Employees
WHERE Email IS NOT NULL
GROUP BY Email
HAVING COUNT(*) > 1;
```

---

### 31. How do you delete duplicate rows but keep one?

```sql
WITH DuplicateRows AS (
    SELECT
        EmpId,
        Email,
        ROW_NUMBER() OVER (
            PARTITION BY Email
            ORDER BY EmpId
        ) AS rn
    FROM Employees
    WHERE Email IS NOT NULL
)
DELETE FROM DuplicateRows
WHERE rn > 1;
```

In SQL Server, deleting from a CTE like this deletes from the underlying table.

---

## Subqueries, CTEs, and Set Operators

### 32. What is a subquery?

A **subquery** is a query inside another query.

```sql
SELECT EmpName, Salary
FROM Employees
WHERE Salary > (
    SELECT AVG(Salary)
    FROM Employees
);
```

---

### 33. What is a correlated subquery?

A **correlated subquery** depends on the outer query and may execute once per outer row.

```sql
SELECT e.EmpName, e.Salary
FROM Employees e
WHERE e.Salary > (
    SELECT AVG(e2.Salary)
    FROM Employees e2
    WHERE e2.DeptId = e.DeptId
);
```

This finds employees earning above their department average.

---

### 34. What is a CTE?

A **Common Table Expression** makes a query more readable by defining a named temporary result.

```sql
WITH HighEarners AS (
    SELECT *
    FROM Employees
    WHERE Salary > 70000
)
SELECT *
FROM HighEarners;
```

CTEs are useful for:

- Breaking complex queries into steps
- Recursive queries
- Ranking and duplicate removal

---

### 35. What is a recursive CTE?

A recursive CTE calls itself. It is useful for hierarchical data.

Example: employee manager hierarchy.

```sql
WITH EmployeeHierarchy AS (
    SELECT EmpId, EmpName, ManagerId, 0 AS Level
    FROM Employees
    WHERE ManagerId IS NULL

    UNION ALL

    SELECT e.EmpId, e.EmpName, e.ManagerId, eh.Level + 1
    FROM Employees e
    INNER JOIN EmployeeHierarchy eh
        ON e.ManagerId = eh.EmpId
)
SELECT *
FROM EmployeeHierarchy;
```

---

### 36. What is the difference between UNION and UNION ALL?

| UNION | UNION ALL |
|---|---|
| Removes duplicates | Keeps duplicates |
| Usually slower | Usually faster |
| Requires duplicate elimination | No duplicate elimination |

```sql
SELECT Email FROM Customers
UNION
SELECT Email FROM Employees;

SELECT Email FROM Customers
UNION ALL
SELECT Email FROM Employees;
```

Use `UNION ALL` when duplicates are acceptable or impossible.

---

### 37. What is the difference between IN and EXISTS?

`IN` checks whether a value exists in a list or subquery result.

```sql
SELECT *
FROM Employees
WHERE DeptId IN (
    SELECT DeptId
    FROM Departments
);
```

`EXISTS` checks whether the subquery returns at least one row.

```sql
SELECT *
FROM Employees e
WHERE EXISTS (
    SELECT 1
    FROM Departments d
    WHERE d.DeptId = e.DeptId
);
```

`EXISTS` is often safer with correlated checks and can short-circuit after finding a match.

---

### 38. Why can NOT IN be dangerous with NULL?

If the subquery returns `NULL`, `NOT IN` can return no rows unexpectedly.

Problem:

```sql
SELECT *
FROM Employees
WHERE DeptId NOT IN (
    SELECT DeptId
    FROM Departments
);
```

If the subquery contains `NULL`, the comparison becomes `UNKNOWN`.

Safer:

```sql
SELECT *
FROM Employees e
WHERE NOT EXISTS (
    SELECT 1
    FROM Departments d
    WHERE d.DeptId = e.DeptId
);
```

---

## DML, DDL, and Data Changes

### 39. What is the difference between DELETE, TRUNCATE, and DROP?

| Command | Meaning | Rollback | WHERE allowed | Structure remains |
|---|---|---|---|---|
| `DELETE` | Removes selected rows | Yes, inside transaction | Yes | Yes |
| `TRUNCATE` | Removes all rows quickly | Database dependent; SQL Server supports rollback in explicit transaction | No | Yes |
| `DROP` | Removes table object | Database dependent | No | No |

```sql
DELETE FROM Employees WHERE DeptId = 10;
TRUNCATE TABLE Employees;
DROP TABLE Employees;
```

---

### 40. What is the difference between CHAR and VARCHAR?

| CHAR | VARCHAR |
|---|---|
| Fixed length | Variable length |
| Pads unused space | Stores only actual characters plus overhead |
| Good for fixed-size values | Good for variable-size values |

Example:

```sql
CHAR(10)     -- 'ABC       '
VARCHAR(10)  -- 'ABC'
```

---

### 41. What is the difference between VARCHAR and NVARCHAR?

| VARCHAR | NVARCHAR |
|---|---|
| Non-Unicode | Unicode |
| Uses less storage for English-only data | Supports multilingual data |
| Literal: `'text'` | Literal: `N'text'` |

Use `NVARCHAR` when storing names, addresses, or text in multiple languages.

---

### 42. What is MERGE?

`MERGE` performs insert, update, or delete based on whether rows match.

```sql
MERGE TargetTable AS target
USING SourceTable AS source
    ON target.Id = source.Id
WHEN MATCHED THEN
    UPDATE SET target.Name = source.Name
WHEN NOT MATCHED THEN
    INSERT (Id, Name) VALUES (source.Id, source.Name);
```

It is useful for upsert operations, but in production it should be used carefully because concurrency and database-specific behavior can be tricky.

---

## Index and Performance Questions

### 43. What is an index?

An **index** is a data structure that helps the database find rows faster.

Without an index:

```text
Scan many rows
```

With an index:

```text
Navigate quickly to matching rows
```

Example:

```sql
CREATE INDEX IX_Employees_Email
ON Employees (Email);
```

---

### 44. What is the difference between clustered and non-clustered index?

| Clustered Index | Non-Clustered Index |
|---|---|
| Determines physical/logical row order of table data | Separate structure pointing to table rows |
| Only one per table | Multiple allowed |
| Best for range queries and ordering | Best for lookups and filters |
| Leaf level contains actual data rows | Leaf level contains key + row locator |

In SQL Server, a primary key creates a clustered index by default unless specified otherwise.

---

### 45. When should you create an index?

Create indexes on columns used frequently in:

- `WHERE`
- `JOIN`
- `ORDER BY`
- `GROUP BY`
- Foreign key lookups

Example:

```sql
CREATE INDEX IX_Employees_DeptId
ON Employees (DeptId);
```

Do not create too many indexes because they slow down `INSERT`, `UPDATE`, and `DELETE`.

---

### 46. What is a covering index?

A **covering index** contains all columns needed by a query, so the database can answer from the index without reading the base table.

```sql
CREATE INDEX IX_Employees_Dept_Salary
ON Employees (DeptId, Salary)
INCLUDE (EmpName);
```

Query:

```sql
SELECT EmpName, Salary
FROM Employees
WHERE DeptId = 10;
```

The index covers `DeptId`, `Salary`, and `EmpName`.

---

### 47. Why can functions on indexed columns hurt performance?

This may prevent index seek usage.

Bad:

```sql
SELECT *
FROM Employees
WHERE YEAR(HireDate) = 2025;
```

Better:

```sql
SELECT *
FROM Employees
WHERE HireDate >= '2025-01-01'
  AND HireDate < '2026-01-01';
```

The second query is more index-friendly because it does not apply a function to the column.

---

### 48. What is SARGable?

SARGable means **Search ARGument Able**. A condition is SARGable when the database can use an index efficiently.

SARGable:

```sql
WHERE Salary >= 50000
```

Not SARGable:

```sql
WHERE Salary + 1000 >= 50000
```

Prefer conditions that compare columns directly to constants or parameters.

---

### 49. What is an execution plan?

An **execution plan** shows how the database will execute a query.

It can show:

- Table scan
- Index scan
- Index seek
- Join algorithm
- Sort operation
- Estimated rows
- Costly operators

Common tuning goal:

```text
Avoid unnecessary scans, sorts, key lookups, and bad join choices.
```

---

## Transaction Questions

### 50. What are ACID properties?

| Property | Meaning |
|---|---|
| Atomicity | All operations succeed or all fail |
| Consistency | Data moves from one valid state to another |
| Isolation | Concurrent transactions do not interfere incorrectly |
| Durability | Committed data survives failures |

---

### 51. What is a transaction?

A **transaction** is a group of SQL operations executed as a single unit.

```sql
BEGIN TRANSACTION;

UPDATE Accounts
SET Balance = Balance - 500
WHERE AccountId = 1;

UPDATE Accounts
SET Balance = Balance + 500
WHERE AccountId = 2;

COMMIT;
```

If any step fails:

```sql
ROLLBACK;
```

---

### 52. What are isolation levels?

| Isolation Level | Prevents | Allows |
|---|---|---|
| Read Uncommitted | Almost nothing | Dirty reads |
| Read Committed | Dirty reads | Non-repeatable reads, phantom reads |
| Repeatable Read | Dirty reads, non-repeatable reads | Phantom reads |
| Serializable | Dirty, non-repeatable, phantom reads | Least concurrency |
| Snapshot | Reads consistent version | Depends on database/versioning |

---

### 53. What is a dirty read?

A **dirty read** happens when one transaction reads uncommitted data from another transaction.

Example:

```text
Transaction A updates salary but does not commit.
Transaction B reads the updated salary.
Transaction A rolls back.
Transaction B read data that never truly existed.
```

---

### 54. What is a deadlock?

A **deadlock** happens when two transactions wait for each other forever.

```text
T1 locks Row A and waits for Row B
T2 locks Row B and waits for Row A
```

Prevention ideas:

- Access tables in consistent order
- Keep transactions short
- Add useful indexes
- Avoid user interaction inside transactions
- Retry safely when a deadlock happens

---

## Practical Query Questions

### 55. Find employees whose salary is above department average.

```sql
SELECT e.EmpName, e.DeptId, e.Salary
FROM Employees e
WHERE e.Salary > (
    SELECT AVG(e2.Salary)
    FROM Employees e2
    WHERE e2.DeptId = e.DeptId
);
```

Window function version:

```sql
WITH EmployeeSalary AS (
    SELECT
        EmpName,
        DeptId,
        Salary,
        AVG(Salary) OVER (PARTITION BY DeptId) AS DeptAvgSalary
    FROM Employees
)
SELECT EmpName, DeptId, Salary
FROM EmployeeSalary
WHERE Salary > DeptAvgSalary;
```

---

### 56. Find departments with no employees.

```sql
SELECT d.*
FROM Departments d
LEFT JOIN Employees e
    ON d.DeptId = e.DeptId
WHERE e.EmpId IS NULL;
```

---

### 57. Find top 3 salaries in each department.

```sql
WITH RankedEmployees AS (
    SELECT
        EmpName,
        DeptId,
        Salary,
        DENSE_RANK() OVER (
            PARTITION BY DeptId
            ORDER BY Salary DESC
        ) AS SalaryRank
    FROM Employees
)
SELECT EmpName, DeptId, Salary
FROM RankedEmployees
WHERE SalaryRank <= 3;
```

---

### 58. Find employees hired in the last 30 days.

```sql
SELECT *
FROM Employees
WHERE HireDate >= DATEADD(DAY, -30, CAST(GETDATE() AS DATE));
```

---

### 59. Find running total of salaries by hire date.

```sql
SELECT
    EmpName,
    HireDate,
    Salary,
    SUM(Salary) OVER (
        ORDER BY HireDate
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS RunningSalaryTotal
FROM Employees;
```

---

### 60. Find gaps in a sequence.

Example table:

```sql
CREATE TABLE Numbers (
    Id INT PRIMARY KEY
);
```

Find missing numbers between existing rows:

```sql
SELECT n1.Id + 1 AS GapStart, n2.Id - 1 AS GapEnd
FROM Numbers n1
JOIN Numbers n2
    ON n2.Id > n1.Id
WHERE n2.Id = (
    SELECT MIN(n3.Id)
    FROM Numbers n3
    WHERE n3.Id > n1.Id
)
AND n2.Id - n1.Id > 1;
```

---

## Trick Questions

### 61. Why does this query fail?

```sql
SELECT Salary * 12 AS AnnualSalary
FROM Employees
WHERE AnnualSalary > 100000;
```

Because `WHERE` is evaluated before `SELECT`, so `AnnualSalary` alias is not available.

Correct:

```sql
SELECT Salary * 12 AS AnnualSalary
FROM Employees
WHERE Salary * 12 > 100000;
```

Or:

```sql
WITH SalaryData AS (
    SELECT Salary * 12 AS AnnualSalary
    FROM Employees
)
SELECT *
FROM SalaryData
WHERE AnnualSalary > 100000;
```

---

### 62. What is wrong with SELECT *?

`SELECT *` is convenient but risky in production.

Problems:

- Reads unnecessary columns
- More network transfer
- Can break code if table schema changes
- Prevents covering index benefits
- Makes query intent unclear

Prefer:

```sql
SELECT EmpId, EmpName, Salary
FROM Employees;
```

---

### 63. Why can BETWEEN be tricky with dates?

This query may miss rows after midnight on the end date:

```sql
WHERE CreatedAt BETWEEN '2026-05-01' AND '2026-05-31'
```

If `CreatedAt` has time, `'2026-05-31 10:00:00'` may be excluded depending on how the end date is interpreted.

Better:

```sql
WHERE CreatedAt >= '2026-05-01'
  AND CreatedAt < '2026-06-01';
```

---

### 64. Why is this NOT equal filter wrong?

```sql
SELECT *
FROM Employees
WHERE DeptId <> 10;
```

This does not return rows where `DeptId` is `NULL`.

If you want both not `10` and `NULL`:

```sql
SELECT *
FROM Employees
WHERE DeptId <> 10
   OR DeptId IS NULL;
```

---

### 65. What is wrong with using LIKE '%abc'?

Leading wildcard prevents normal index seek usage in many databases.

Less efficient:

```sql
WHERE Email LIKE '%gmail.com'
```

More index-friendly:

```sql
WHERE Email LIKE 'john%'
```

For suffix search at scale, consider full-text search or a separate indexed computed/reversed column depending on database.

---

### 66. Why can implicit conversion hurt performance?

If a column is `VARCHAR` and the query compares it to a number:

```sql
WHERE PhoneNumber = 9876543210
```

The database may convert the column value for many rows, causing index issues.

Better:

```sql
WHERE PhoneNumber = '9876543210'
```

Match parameter types with column types.

---

### 67. Is ORDER BY guaranteed without writing ORDER BY?

No.

Rows may appear sorted because of indexes, insertion order, or execution plan, but SQL does not guarantee result order without `ORDER BY`.

Correct:

```sql
SELECT *
FROM Employees
ORDER BY EmpName;
```

---

### 68. Does GROUP BY guarantee sorted output?

No. Some databases may output grouped rows in sorted order due to implementation, but it is not guaranteed.

Use:

```sql
SELECT DeptId, COUNT(*) AS EmployeeCount
FROM Employees
GROUP BY DeptId
ORDER BY DeptId;
```

---

### 69. What happens when an aggregate query has no matching rows?

```sql
SELECT COUNT(*) FROM Employees WHERE DeptId = -1;
```

Returns:

```text
0
```

But:

```sql
SELECT SUM(Salary) FROM Employees WHERE DeptId = -1;
```

Returns:

```text
NULL
```

Use:

```sql
SELECT COALESCE(SUM(Salary), 0)
FROM Employees
WHERE DeptId = -1;
```

---

### 70. What is SQL injection?

SQL injection happens when user input is concatenated directly into SQL.

Bad:

```sql
SET @sql = 'SELECT * FROM Users WHERE Email = ''' + @email + '''';
EXEC(@sql);
```

Safe pattern:

```sql
EXEC sp_executesql
    N'SELECT * FROM Users WHERE Email = @email',
    N'@email VARCHAR(100)',
    @email = @email;
```

In application code, use prepared statements or parameterized queries.

---

## Fast Interview Answers

| Question | Short Answer |
|---|---|
| Why use index? | To speed up search, join, filter, and sort operations |
| Can index slow writes? | Yes, indexes must be updated on insert/update/delete |
| Which join returns unmatched left rows? | `LEFT JOIN` |
| Which join returns only matching rows? | `INNER JOIN` |
| Can primary key be NULL? | No |
| Can foreign key be NULL? | Yes, unless column is declared `NOT NULL` |
| Does `COUNT(column)` count NULL? | No |
| Does `COUNT(*)` count NULL rows? | Yes, it counts rows |
| What gives sorted BST-like map in Java? | `TreeMap`, backed by Red-Black tree |
| What is ACID? | Atomicity, Consistency, Isolation, Durability |
| Best way to compare NULL? | `IS NULL` or `IS NOT NULL` |
| Safest alternative to `NOT IN` with nullable subquery? | `NOT EXISTS` |
| Best way to get nth salary with ties? | `DENSE_RANK()` |
| Is result order guaranteed without `ORDER BY`? | No |

