# SQL Interview Questions and Trick Questions

This note covers common SQL interview questions, tricky SQL behavior, query-writing patterns, performance concepts, and transaction/database fundamentals. Examples use SQL Server syntax unless stated otherwise. Core relational concepts transfer to other databases, but date functions, NULL behavior, indexes, and transaction rules can differ. Each answer includes reasoning and likely interview follow-ups.

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

These tables support the employee and department queries. Other scenarios introduce separate illustrative tables such as Accounts and StudentMarks; this is a study guide, not a script to execute from top to bottom. Salary comparisons assume a common currency and pay period; question 61 assumes monthly pay.

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


**Reasoning and interview follow-ups**

SQL is declarative: you describe the rows you want, and the optimizer chooses how to retrieve them. Here you request names and salaries above a threshold without specifying a loop or an index traversal. That separation lets the database choose a different strategy when the data changes.

In an interview, distinguish the language from the product: SQL Server, PostgreSQL, and MySQL are database systems that implement SQL with different dialects. Syntax such as TOP, LIMIT, and date arithmetic is not universally interchangeable.

---

### 2. What is the difference between SQL and NoSQL?

| SQL | NoSQL |
|---|---|
| Relational database | Non-relational database |
| Explicit relational schema | Schema flexibility depends on the model and product |
| Uses tables, rows, columns | Uses documents, key-value, graph, wide-column, etc. |
| Fits relational constraints and varied queries | Fits access patterns suited to its specific data model |
| Supports joins | Joins are limited or avoided |
| Transaction guarantees depend on engine and configuration | Transaction guarantees depend on engine and configuration |

Examples:

- SQL: SQL Server, MySQL, PostgreSQL, Oracle
- NoSQL: MongoDB, Cassandra, Redis, DynamoDB


**Reasoning and interview follow-ups**

The useful comparison is between relational databases and particular non-relational models; SQL itself is a language. Relational modeling fits connected entities whose constraints matter, such as customers, orders, and payments. A document model can fit an aggregate normally read together, such as a product with variable attributes.

Avoid saying that NoSQL means no transactions or that relational databases cannot scale horizontally. Those capabilities depend on the product. Explain your choice using relationships, access patterns, transaction boundaries, and consistency requirements. Flexible storage still needs application-level rules for interpreting the data.

---

### 3. What are DDL, DML, DCL, TCL, and DQL?

| Type | Full Form | Commands |
|---|---|---|
| DDL | Data Definition Language | `CREATE`, `ALTER`, `DROP`, `TRUNCATE` |
| DML | Data Manipulation Language | `INSERT`, `UPDATE`, `DELETE`, `MERGE` |
| DQL | Data Query Language | `SELECT` |
| DCL | Data Control Language | `GRANT`, `REVOKE` |
| TCL | Transaction Control Language | `COMMIT`, `ROLLBACK`, `SAVEPOINT` |


**Reasoning and interview follow-ups**

These labels describe intent: DDL changes structure, DML changes rows, DCL changes permissions, and TCL controls transactions. For example, ALTER TABLE adds a salary column, UPDATE changes salaries, and ROLLBACK cancels uncommitted transactional work.

DQL is a common teaching category, but some classifications include SELECT under DML. A statement's category does not determine whether it can be rolled back; the database's transaction rules do. SQL Server uses `SAVE TRANSACTION savepoint_name` rather than the standalone SAVEPOINT syntax used by some other engines.

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


**Reasoning and interview follow-ups**

A primary key gives other tables and application code a stable way to identify one row. A name is usually unsuitable because names can change and different people can share one. A generated UserId avoids that problem, but it does not prevent two IDs from representing the same real-world person; business uniqueness needs separate constraints.

A candidate key is a minimal set of columns that uniquely identifies a row. The primary key is the candidate key chosen as the main identifier; the others are alternate keys. A key is a logical constraint, while an index is an access structure used to enforce or accelerate operations.

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


**Reasoning and interview follow-ups**

The composite constraint rejects a second enrollment for the same student-course pair while allowing one student in many courses and many students in one course. Neither column needs to be unique on its own.

If students can retake courses in different terms, this key is too restrictive. Include TermId in the business key or introduce an enrollment ID and a suitable unique constraint. A surrogate ID alone does not prevent duplicate enrollments, so derive the constraint from the business rule first.

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

This prevents inserting a non-NULL reference to a nonexistent customer. The example assumes Customers exists with CustomerId as its primary key.


**Reasoning and interview follow-ups**

The database checks every non-NULL child reference against the parent key, preventing orphaned references even when multiple applications write concurrently. The referenced columns need suitable enforced uniqueness, usually a primary key or unique constraint.

CustomerId is nullable here, so an order with no customer is permitted. Add NOT NULL when a customer is mandatory. A foreign key does not automatically require cascade deletion: rejecting parent deletion, cascading, or clearing an optional reference are separate policies. In SQL Server, a child-side foreign-key index is not created automatically.

---

### 7. What is the difference between primary key and unique key?

| Primary Key | Unique Key |
|---|---|
| Uniquely identifies a row | Ensures column values are unique |
| Cannot contain NULL | May allow NULL depending on database |
| Only one primary key per table | Multiple unique keys allowed |
| Defaults to clustered in SQL Server if no clustered index already exists | Defaults to nonclustered in SQL Server |


**Reasoning and interview follow-ups**

UserId can identify a user while a unique Email constraint prevents duplicate addresses. Those are separate requirements. SQL Server's conventional single-column unique constraint allows at most one NULL; other databases can treat NULLs differently.

If many users may lack an email but supplied emails must be unique, a SQL Server filtered unique index expresses that rule:

```sql
CREATE UNIQUE INDEX UX_Users_Email
ON Users (Email)
WHERE Email IS NOT NULL;
```

A primary key can be nonclustered. Do not treat primary key and clustered index as synonyms.

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


**Reasoning and interview follow-ups**

Normalization prevents update, insertion, and deletion anomalies. If the current customer address is copied into 100 orders, updating only 99 produces conflicting answers. Keeping it once in Customers avoids that inconsistency and lets a customer exist before their first order or after their last order is removed.

Explain the dependencies behind each form:

1. **1NF:** Store one value from the attribute's domain in each cell, rather than a comma-separated list of products. Represent order lines separately so they can be queried and constrained.
2. **2NF:** Every non-prime attribute depends on the whole of every candidate key, not only part of a composite candidate key. In OrderLines(OrderId, ProductId, ProductName, Quantity), ProductName depends on ProductId alone and belongs in Products.
3. **3NF:** For each nontrivial dependency X -> A, X must be a superkey or A must be prime, meaning part of a candidate key. The usual practical example is moving CurrentCustomerAddress out of Orders because OrderId -> CustomerId -> CurrentCustomerAddress.
4. **BCNF:** Every determinant of a nontrivial dependency must be a superkey. This removes 3NF's prime-attribute exception and matters with overlapping candidate keys.

A real order model generally needs Orders and OrderLines as well as Customers and Products. The price actually charged belongs on an order line because it is a historical fact, not a copy that should track today's product price.

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


**Reasoning and interview follow-ups**

Denormalization moves work from read time to write or refresh time. A stored order total avoids summing every line for each dashboard request, but changing a line must also update that total or arrange a refresh.

Explain who maintains the duplicate and whether stale results are acceptable. Updating it in the same transaction costs more write work; asynchronous refresh introduces lag. Also distinguish duplication from history: an invoice may need the original customer name even after the customer's current name changes.

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


**Reasoning and interview follow-ups**

Read the example as a pipeline: form employee rows, retain salaries above 50,000, group by department, keep groups with more than two qualifying employees, project the count, then sort. The count describes high earners, not every employee in the department.

This is logical processing, not the actual physical execution sequence. The optimizer can push down filters and reorder eligible joins while preserving meaning. Window calculations logically follow grouping and HAVING, which is why filtering their results normally needs an outer query.

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


**Reasoning and interview follow-ups**

For department salaries 40,000, 60,000, and 80,000, WHERE leaves two employees, so HAVING COUNT(*) >= 3 rejects the department even though it has three employees overall. Moving conditions between the clauses can therefore change the question.

Use WHERE for input-row conditions and HAVING for group-result conditions, such as AVG(Salary) > 70000. HAVING can also appear without an explicit GROUP BY, treating the input as one overall group.

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


**Reasoning and interview follow-ups**

Selecting only DeptId with GROUP BY produces the same values as SELECT DISTINCT DeptId, including one NULL group if present. GROUP BY becomes necessary when you want a calculation per group, such as employee count or total payroll.

Neither form is universally faster; their plans can be similar. Do not use DISTINCT to hide unexplained join duplicates: a wrong many-to-many join can multiply rows, and deduplication can conceal that error while removing legitimate repeated values.

---

### 13. What is the difference between ORDER BY and GROUP BY?

| GROUP BY | ORDER BY |
|---|---|
| Combines rows into groups | Sorts final result |
| Often used with aggregates | Used for display order |
| Runs before `SELECT` logically | Runs near the end |


**Reasoning and interview follow-ups**

Grouping changes the result's level of detail; sorting changes presentation. Grouping 1,000 employees by department might produce 10 department rows, while sorting by salary still produces 1,000 employee rows.

Use both when a report needs payroll per department ordered from largest to smallest. An internal sort used to implement grouping does not guarantee the final display order.

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


**Reasoning and interview follow-ups**

Each matching pair produces one output row. A department with three employees produces three department-employee pairs, so summing a department-level budget after that join would count the same budget three times.

With the declared foreign key, each non-NULL employee department must exist. The employee-side inner join therefore removes employees with NULL DeptId. Equality does not match NULL to NULL.

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


**Reasoning and interview follow-ups**

Preserving a left row means it appears at least once, not exactly once. Two matching right rows produce two result rows; no match produces one row with NULL placeholders for the right side.

When left-joining Departments to Employees, COUNT(*) counts the placeholder row for an empty department. COUNT(e.EmpId) returns zero because the non-nullable employee key is NULL only for an unmatched placeholder. This is a common follow-up question about outer joins and aggregation.

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


**Reasoning and interview follow-ups**

The join tries to find a department, and the WHERE predicate keeps failed matches. Test a non-nullable right-side key: testing DeptName IS NULL could include an actual department with a missing name.

With the enforced foreign key, unmatched employees must have e.DeptId IS NULL, so that simpler test is sufficient. The anti-join also detects invalid non-NULL references in unconstrained staging data. NOT EXISTS expresses the same absence-of-match idea.

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


**Reasoning and interview follow-ups**

The aliases assign two roles to the same table: e is the employee and m is the manager. An employee with ManagerId = 1 joins to the employee row whose EmpId = 1 to retrieve that manager's name.

LEFT JOIN preserves top-level employees without managers. One self join retrieves one level; use recursion to traverse an arbitrary management chain. The sample schema does not constrain ManagerId, so a missing manager reference is also possible.

---

### 18. What is a CROSS JOIN?

`CROSS JOIN` returns the Cartesian product of two tables.

```sql
SELECT e.EmpName, d.DeptName
FROM Employees e
CROSS JOIN Departments d;
```

If `Employees` has 10 rows and `Departments` has 5 rows, result has 50 rows.


**Reasoning and interview follow-ups**

A cross join pairs every left row with every right row. It can generate every product-warehouse combination, including combinations with no inventory record yet.

Cardinality multiplies: 100,000 rows crossed with 1,000 rows creates 100 million combinations before later filtering. If either input is empty, the result is empty. State the intended combination explicitly so an accidentally omitted join condition does not masquerade as valid logic.

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


**Reasoning and interview follow-ups**

Suppose Ana belongs to IT, Ben to HR, and Cara has no department. Filtering in ON returns Ana with IT and both Ben and Cara with NULL department columns. Filtering in WHERE returns only Ana because HR fails the predicate and the NULL comparison is UNKNOWN.

ON decides which rows match; WHERE filters the completed join result. A right-side equality filter in WHERE is null-rejecting, but not every WHERE clause converts an outer join to an inner join: WHERE d.DeptId IS NULL intentionally keeps unmatched rows.

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


**Reasoning and interview follow-ups**

A salary of zero means the amount is known to be zero; a NULL salary means no amount is known. Replacing missing salaries with zero changes averages and can change the business meaning.

SQL Server distinguishes an empty string from NULL. Dialects can differ, so avoid assuming every database handles empty strings identically. Use NOT NULL when missing values are invalid, rather than trying to repair their meaning in every query.

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


**Reasoning and interview follow-ups**

Two unknown values might be equal or different, so equality cannot establish TRUE. NULL <> NULL is also UNKNOWN, and negating an unknown equality does not make it true.

IS NULL asks whether a value is missing. If the business wants two missing values to match, explicitly write `a = b OR (a IS NULL AND b IS NULL)`. Replacing NULL with a magic value is risky because that value might also occur in real data.

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


**Reasoning and interview follow-ups**

A decisive TRUE or FALSE can determine a compound expression even when another part is UNKNOWN:

| Expression | Result | Reason |
|---|---|---|
| TRUE AND UNKNOWN | UNKNOWN | The unknown side could fail |
| FALSE AND UNKNOWN | FALSE | One false side is sufficient |
| TRUE OR UNKNOWN | TRUE | One true side is sufficient |
| FALSE OR UNKNOWN | UNKNOWN | The unknown side decides |
| NOT UNKNOWN | UNKNOWN | Negation does not supply missing information |

WHERE, HAVING, and join matching require TRUE. SQL Server CHECK constraints reject FALSE but can accept UNKNOWN, so CHECK (Salary > 0) does not prohibit NULL by itself; add NOT NULL when salary is mandatory.

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


**Reasoning and interview follow-ups**

For emails a@example.com, NULL, and a@example.com, COUNT(*) is 3, COUNT(1) is 3, COUNT(Email) is 2, and COUNT(DISTINCT Email) is 1. The constant 1 is never NULL, so counting it counts every input row; it does not refer to the first column.

There is no general speed advantage to COUNT(1) over COUNT(*). Use COUNT(*) to express row count. Ordinary aggregates such as AVG ignore NULL inputs: the average of 100 and NULL is 100, not 50, because only one known value contributes.

---

### 24. What is the difference between COALESCE and ISNULL?

| COALESCE | ISNULL |
|---|---|
| ANSI SQL standard | SQL Server specific |
| Accepts multiple arguments | Accepts two arguments |
| Returns first non-NULL value | Replaces NULL with fallback |
| SQL Server uses data-type precedence | Usually uses the first argument type; untyped NULL is a special case |

```sql
SELECT COALESCE(Email, 'No Email')
FROM Employees;

SELECT ISNULL(Email, 'No Email')
FROM Employees;
```


**Reasoning and interview follow-ups**

SQL Server's result types matter as much as fallback values. `ISNULL(CAST(NULL AS VARCHAR(3)), 'abcdef')` returns abc because the first argument fixes the result length; the corresponding COALESCE can return all six characters.

COALESCE uses data-type precedence, so mixing text and numbers can cause conversion errors. Its expressions can be evaluated more than once, which matters with subqueries. Use explicit casts when the output type matters. See [COALESCE and ISNULL differences](https://learn.microsoft.com/en-us/sql/t-sql/language-elements/coalesce-transact-sql).

---

### 25. What does NULLIF do?

`NULLIF(a, b)` returns `NULL` if `a = b`, otherwise returns `a`.

Useful to avoid divide-by-zero:

```sql
SELECT TotalMarks / NULLIF(SubjectCount, 0) AS AverageMarks
FROM StudentMarks;
```


**Reasoning and interview follow-ups**

When SubjectCount is zero, NULLIF turns the denominator into NULL, making the division result NULL rather than raising a divide-by-zero error. This represents an undefined average; replacing it with zero is a separate business decision.

If both operands are integers, SQL Server uses integer division. `CAST(TotalMarks AS DECIMAL(12, 2)) / NULLIF(SubjectCount, 0)` preserves fractional results. This scenario assumes a separate StudentMarks table with those numeric columns.

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
    WHERE Salary IS NOT NULL
)
SELECT EmpName, Salary
FROM RankedEmployees
WHERE SalaryRank = 2;
```

This handles ties correctly.


**Reasoning and interview follow-ups**

First clarify whether second highest means the second distinct salary or the second employee after sorting. With salaries 100, 100, 90, DENSE_RANK identifies 90 as the second distinct salary; ROW_NUMBER identifies one of the employees earning 100 as the second row.

Exclude NULL salaries before ranking. Fewer than two distinct known salaries means no result rows. If only the amount is needed, this alternative returns one row, containing NULL when no second salary exists:

```sql
SELECT MAX(Salary) AS SecondHighestSalary
FROM Employees
WHERE Salary < (SELECT MAX(Salary) FROM Employees);
```

The inner maximum finds the highest value, and the outer maximum finds the largest value below it.

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
    ROW_NUMBER() OVER (ORDER BY Salary DESC, EmpId) AS RowNo,
    RANK() OVER (ORDER BY Salary DESC) AS RankNo,
    DENSE_RANK() OVER (ORDER BY Salary DESC) AS DenseRankNo
FROM Employees;
```


**Reasoning and interview follow-ups**

ROW_NUMBER numbers individual rows, RANK assigns competition positions with gaps, and DENSE_RANK numbers distinct ordering values without gaps. Choose based on whether the requirement concerns people, positions, or salary levels.

For deterministic ROW_NUMBER results, use Salary DESC, EmpId so the unique key breaks ties. Do not add EmpId to RANK or DENSE_RANK when equal salaries should tie: the functions compare the entire ordering tuple, so a unique ID would give every row its own rank.

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
    WHERE Salary IS NOT NULL
)
SELECT EmpName, DeptId, Salary
FROM RankedEmployees
WHERE SalaryRank = 1;
```

`PARTITION BY` restarts ranking for each department.


**Reasoning and interview follow-ups**

PARTITION BY separates departments, then descending salary order assigns rank 1 to each department's highest known salary. Tied employees are all returned. Without partitioning, the query would rank salaries across the entire company.

Use MAX(Salary) with GROUP BY if you need only amounts; ranking preserves the associated employee rows. NULL department IDs form their own partition. Exclude them if the requirement concerns assigned departments only. Departments with no known salary produce no ranked row after the NULL salary filter.

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


**Reasoning and interview follow-ups**

Three employees in one department become one row under GROUP BY but remain three rows under the windowed AVG, each carrying the same department average. This makes individual-versus-department comparisons possible without losing employee detail.

A window has a partition, optional ordering, and for relevant functions a frame. Adding ORDER BY to a windowed SUM can change a department total into a running total, so ordering inside OVER is not merely cosmetic.

---

### 30. How do you find duplicate emails?

```sql
SELECT Email, COUNT(*) AS DuplicateCount
FROM Employees
WHERE Email IS NOT NULL
GROUP BY Email
HAVING COUNT(*) > 1;
```


**Reasoning and interview follow-ups**

GROUP BY collects equal emails, COUNT(*) measures each group, and HAVING keeps groups with multiple rows. Filtering NULL avoids treating missing addresses as duplicate known addresses. The output lists duplicate values and counts, not every affected employee.

Equality depends on collation, so case sensitivity can change which values count as duplicates. Define the application's normalization rules before cleanup; lowercasing or trimming values without an agreed rule can merge records incorrectly.

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


**Reasoning and interview follow-ups**

PARTITION BY defines duplicates as repeated non-NULL emails. ORDER BY EmpId keeps the smallest employee ID and marks later rows for deletion. Choose a different deterministic survivor rule if a verified or newer record should win.

Preview the rows by replacing the final DELETE with SELECT * FROM DuplicateRows WHERE rn > 1. References to deleted IDs may need reassignment before deletion. Cleanup does not prevent future duplicates; add the appropriate unique constraint or filtered unique index afterward. This deletion-through-CTE syntax is SQL Server-specific.

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


**Reasoning and interview follow-ups**

The inner query produces one average; the outer query compares each employee salary with that scalar. Unknown salaries do not contribute to AVG and cannot pass the comparison. If no known salaries exist, the average is NULL and no employee passes.

A scalar subquery must return at most one row; more than one causes an error. An IN subquery can return many rows because it supplies candidate values. Explain the expected result shape before choosing a subquery form.

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


**Reasoning and interview follow-ups**

The outer department becomes an input to the inner average, so employees are compared with their own department rather than the company. Conceptually this can be understood per row, but the optimizer may transform the query into a join or another set-based plan; repeated physical execution is not guaranteed.

A NULL department does not equal another NULL department. Unassigned employees therefore find no peers through the correlated equality and are excluded. A window partition behaves differently unless you explicitly align those NULL rules.

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


**Reasoning and interview follow-ups**

A CTE names a query expression for the immediately following statement. It is not a persistent view or temporary table, and SQL Server does not guarantee materialized intermediate rows or one-time evaluation.

Use it to express stages such as calculating a rank and then filtering it. Use a temporary table when reusable stored intermediate rows or indexes across multiple statements are needed. Terminate the previous SQL Server statement with a semicolon before starting WITH.

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


**Reasoning and interview follow-ups**

The anchor selects roots at level 0. Each recursive iteration finds employees reporting to the previous level, adds 1, and continues until no new rows appear. UNION ALL combines those levels without duplicate elimination.

A root-based traversal misses orphans and disconnected cycles. Other traversal starting points can enter cycles and repeat indefinitely without protection. SQL Server defaults to a recursion limit of 100; OPTION (MAXRECURSION 200) on the final query can allow a known deeper tree, but increasing the limit does not fix cycles. Validate the hierarchy or track visited keys, and explicitly order the final output.

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


**Reasoning and interview follow-ups**

UNION removes duplicate whole result rows both within and across inputs. If one input contains a, a and the other a, b, UNION returns a, b; UNION ALL keeps all four rows. Eliminating duplicates generally requires additional sorting, hashing, or equivalent work.

Both inputs need the same number of columns in corresponding positions with compatible types. Neither operator guarantees order. Apply one final ORDER BY to the combined result, and use UNION ALL only when preserving duplicates matches the requirement.

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

`EXISTS` expresses an existence test without multiplying outer rows; the optimizer determines physical execution.


**Reasoning and interview follow-ups**

EXISTS asks whether any qualifying row exists, so the selected expression is irrelevant: SELECT NULL also satisfies EXISTS when a row is returned. Multiple inner matches do not multiply an outer employee, unlike an ordinary join.

These positive IN and EXISTS examples can have equivalent results and similar semi-join plans. Do not claim that EXISTS is always faster or guarantees a specific physical short-circuit order. Negation and NULL handling create the more important distinction in the next question.

---

### 38. Why can NOT IN be dangerous with NULL?

If the subquery returns `NULL`, `NOT IN` can return no rows unexpectedly.

Problem:

```sql
SELECT *
FROM Departments
WHERE DeptId NOT IN (
    SELECT DeptId
    FROM Employees -- Nullable: an unassigned employee exposes the trap.
);
```

If the subquery contains `NULL`, the comparison becomes `UNKNOWN`.

Safer:

```sql
SELECT d.*
FROM Departments d
WHERE NOT EXISTS (
    SELECT 1
    FROM Employees e
    WHERE e.DeptId = d.DeptId
);
```


**Reasoning and interview follow-ups**

`30 NOT IN (10, 20, NULL)` behaves like `30 <> 10 AND 30 <> 20 AND 30 <> NULL`. The final comparison is UNKNOWN, so the whole expression cannot pass WHERE. A value equal to 10 or 20 instead yields FALSE, meaning no candidate survives this list.

Departments.DeptId is a primary key and cannot contain NULL, so it would not demonstrate a NULL returned by the subquery. The corrected example reads nullable employee department IDs. NOT EXISTS asks directly whether a matching employee is absent and is unaffected by unrelated NULLs. If the outer expression itself is nullable, decide whether unmatched outer NULLs should qualify; equality-based NOT EXISTS normally includes them.

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


**Reasoning and interview follow-ups**

In SQL Server, DELETE removes rows and can fire DELETE triggers. TRUNCATE releases data pages, does not fire DELETE triggers, and resets an identity counter if present. It logs page deallocation, so describing it as unlogged is incorrect. Both can be rolled back in an explicit SQL Server transaction.

TRUNCATE has restrictions, including most tables referenced by foreign keys. DROP removes the definition as well as data. Do not assume another engine has SQL Server's transactional DDL behavior. See [TRUNCATE TABLE](https://learn.microsoft.com/en-us/sql/t-sql/statements/truncate-table-transact-sql).

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


**Reasoning and interview follow-ups**

CHAR is suitable when the encoded size is genuinely fixed, such as a two-letter code. A large CHAR column wastes space for short strings because of padding, while VARCHAR normally fits variable-length text better.

In SQL Server, CHAR(n) and VARCHAR(n) specify bytes, not a universal count of characters. Multibyte encodings can therefore store fewer than n characters. Trailing-space comparison rules also mean stored length and equality are different questions.

---

### 41. What is the difference between VARCHAR and NVARCHAR?

| VARCHAR | NVARCHAR |
|---|---|
| Code-page encoding, or Unicode under a UTF-8 collation | Unicode |
| Storage depends on encoding and text | Storage depends on Unicode characters used |
| Literal: `'text'` | Literal: `N'text'` |

Use `NVARCHAR` when storing names, addresses, or text in multiple languages.


**Reasoning and interview follow-ups**

VARCHAR is not always non-Unicode: SQL Server 2019 and later support Unicode VARCHAR under UTF-8 collations. Otherwise, the relevant code page determines supported characters. NVARCHAR provides Unicode storage without requiring a UTF-8 collation.

VARCHAR length is measured in bytes; NVARCHAR length uses two-byte units, and some characters require multiple units. Choose encoding and capacity for the real text, and bind compatible parameter types. See [SQL Server character types](https://learn.microsoft.com/en-us/sql/t-sql/data-types/char-and-varchar-transact-sql).

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


**Reasoning and interview follow-ups**

ON defines source-target identity. Matching IDs update names; absent IDs insert rows. This example never deletes target rows missing from the source because it has no deletion branch.

Ensure source keys are unique for the intended match: multiple source rows attempting to update one target can fail. One statement does not eliminate concurrent insertion races or the need for uniqueness and isolation. Separate INSERT and UPDATE statements need the same concurrency reasoning. TargetTable and SourceTable here are illustrative tables with Id and Name columns.

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


**Reasoning and interview follow-ups**

An ordinary rowstore B-tree index maintains ordered keys so the engine can navigate to a value or range instead of inspecting all table rows. This can make an email equality lookup efficient, but the index takes storage and needs maintenance as data changes.

An index does not affect which answer is logically correct. The optimizer can ignore it when scanning is cheaper, such as when most rows qualify. Explain which query pattern an index helps instead of promising that every query becomes faster.

---

### 44. What is the difference between clustered and non-clustered index?

| Clustered Index | Non-Clustered Index |
|---|---|
| Data rows stored in a structure logically ordered by key | Separate structure containing keys and row locators |
| Only one per table | Multiple allowed |
| Supports key lookups and range scans | Supports key lookups, ranges, and covered queries |
| Leaf level contains actual data rows | Leaf level contains key + row locator |

In SQL Server, a primary key defaults to clustered if no clustered index already exists and no alternative is specified.


**Reasoning and interview follow-ups**

For SQL Server rowstore indexes, clustered leaves contain data rows; nonclustered leaves contain keys and row locators, plus included columns where defined. A locator uses the clustering key for a clustered table or a row identifier for a heap.

Logical ordering does not guarantee contiguous disk placement or sorted query output. Both kinds can support ranges. See [clustered and nonclustered indexes](https://learn.microsoft.com/sql/relational-databases/indexes/clustered-and-nonclustered-indexes-described).

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


**Reasoning and interview follow-ups**

Design for query patterns. For WHERE DeptId = 10 AND Salary >= 70000, an index on (DeptId, Salary) can locate a department and then a salary range inside it. Reversing keys changes that access path; a later key is generally not as useful alone as a leading key.

Balance read benefit against write maintenance and storage, then check representative plans and reads. See the [index design guide](https://learn.microsoft.com/en-us/sql/relational-databases/sql-server-index-design-guide).

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


**Reasoning and interview follow-ups**

Coverage is relative to a query. Here DeptId filters, Salary supplies a key/output column, and included EmpName supplies the remaining output without becoming a search key.

Adding a noncovered output column can require base-row lookups. Including every column makes the index wider and more expensive to maintain. See [included columns and index design](https://learn.microsoft.com/en-us/sql/relational-databases/sql-server-index-design-guide).

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


**Reasoning and interview follow-ups**

A HireDate index orders dates, not the results of YEAR(HireDate). A direct range exposes a contiguous interval that the engine can seek and scan when that plan is worthwhile. The exclusive next-year boundary also handles timestamps without guessing December 31's final representable time.

Some expressions have special optimizer support or can use indexed computed columns, so say functions can prevent useful seeks rather than claiming every function disables every index.

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


**Reasoning and interview follow-ups**

A useful search predicate can become a boundary on a suitable index. Salary >= 49000 states the intended threshold directly instead of requiring arithmetic on every stored salary in Salary + 1000 >= 50000.

Algebraic rewrites must preserve type, overflow, and rounding behavior. SARGability also does not guarantee a seek: a useful index must exist and scanning may still be cheaper. Match parameter types so conversions on the column do not undermine the access path.

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


**Reasoning and interview follow-ups**

An estimated plan describes chosen operators and estimated row counts without executing the statement. An actual plan adds runtime information. Large estimated-versus-actual row differences can explain poor join choices, memory grants, or spills.

Nested loops can fit small inputs with efficient lookups; hash joins often suit larger inputs; merge joins exploit ordered inputs. A scan can be appropriate when many rows qualify. Compare CPU, elapsed time, and logical reads rather than assuming that every seek is good and every scan is bad.

---

## Transaction Questions

### 50. What are ACID properties?

| Property | Meaning |
|---|---|
| Atomicity | All operations succeed or all fail |
| Consistency | Data moves from one valid state to another |
| Isolation | Concurrent transactions do not interfere incorrectly |
| Durability | Committed data survives failures |


**Reasoning and interview follow-ups**

Use a transfer to explain the properties. Atomicity makes debit and credit commit or roll back together. Consistency means enforced invariants remain valid; the database cannot infer business rules that neither constraints nor application logic enforce.

Isolation defines what concurrent work can observe; weaker levels do not promise serial execution semantics. Durability means a fully durable committed transaction survives a crash through recovery mechanisms such as the transaction log. Atomicity concerns all-or-nothing work; durability concerns preserving committed work.

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


**Reasoning and interview follow-ups**

BEGIN TRANSACTION alone is not sufficient error handling. Updating a missing account affects zero rows without necessarily raising an error, so a naive transfer can debit without a corresponding credit. Check affected rows and roll back on failure.

This SQL Server pattern assumes Accounts(AccountId PRIMARY KEY, Balance DECIMAL(12,2) NOT NULL):

```sql
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;

    UPDATE Accounts
    SET Balance = Balance - 500
    WHERE AccountId = 1 AND Balance >= 500;
    IF @@ROWCOUNT <> 1
        THROW 50001, 'Source account missing or insufficient balance.', 1;

    UPDATE Accounts
    SET Balance = Balance + 500
    WHERE AccountId = 2;
    IF @@ROWCOUNT <> 1
        THROW 50002, 'Destination account missing.', 1;

    COMMIT;
END TRY
BEGIN CATCH
    IF XACT_STATE() <> 0 ROLLBACK;
    THROW;
END CATCH;
```

The debit checks sufficient funds as part of the write, rather than trusting an earlier unlocked read. Real transfer code also needs validated inputs, consistent lock ordering, and protection against applying the same request twice.

---

### 52. What are isolation levels?

| Isolation Level | Prevents | Allows |
|---|---|---|
| Read Uncommitted | Almost nothing | Dirty reads |
| Read Committed | Dirty reads | Non-repeatable reads, phantom reads |
| Repeatable Read | Dirty reads, non-repeatable reads | Phantom reads |
| Serializable | Dirty, non-repeatable, phantom reads | Can increase blocking or retries; implementation dependent |
| Snapshot | Reads consistent version | Depends on database/versioning |


**Reasoning and interview follow-ups**

A non-repeatable read means rereading a row observes a committed change; a phantom means repeating a predicate sees changed membership. SQL Server's lock-based repeatable read protects rows already read, while serializable also protects relevant key ranges.

READ COMMITTED can use locks or statement-level row versions, depending on configuration. SNAPSHOT gives a transaction-level view but can cause update conflicts and still allow cross-row anomalies such as write skew. Choose isolation for the business invariant being protected. See the [locking and row versioning guide](https://learn.microsoft.com/en-us/sql/relational-databases/sql-server-transaction-locking-and-row-versioning-guide).

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


**Reasoning and interview follow-ups**

The value existed as an uncommitted change but never became committed state. A report using it can publish totals that cannot be reconstructed from any committed outcome.

SQL Server NOLOCK permits read-uncommitted behavior; it is not a reliable nonblocking snapshot and can miss or duplicate rows during concurrent changes. Use suitable locking or versioning when committed consistency matters. See the [transaction guide](https://learn.microsoft.com/en-us/sql/relational-databases/sql-server-transaction-locking-and-row-versioning-guide).

---

### 54. What is a deadlock?

A **deadlock** is a cycle of transactions waiting on resources held by one another; the database normally detects it and aborts a victim.

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


**Reasoning and interview follow-ups**

Blocking is waiting; a deadlock requires a cycle of dependencies. SQL Server detects the cycle and rolls back a victim so other work can proceed, rather than leaving both transactions waiting forever.

Retry the entire rolled-back transaction with bounded attempts. External effects need idempotency because rollback cannot undo an email or remote API call. A deadlock graph identifies the resources involved; consistent access order reduces cycles. See the [transaction guide](https://learn.microsoft.com/en-us/sql/relational-databases/sql-server-transaction-locking-and-row-versioning-guide).

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
    WHERE DeptId IS NOT NULL
)
SELECT EmpName, DeptId, Salary
FROM EmployeeSalary
WHERE Salary > DeptAvgSalary;
```


**Reasoning and interview follow-ups**

With department salaries 40,000, 60,000, and 80,000, the average is 60,000 and only 80,000 passes the strict greater-than comparison. AVG ignores NULL salaries, and an unknown employee salary cannot pass the comparison.

The two formulations need consistent NULL rules. Correlated equality excludes employees with NULL departments. The window version explicitly excludes those departments too; otherwise it would form a NULL partition and could return unassigned employees above that partition's average.

---

### 56. Find departments with no employees.

```sql
SELECT d.*
FROM Departments d
LEFT JOIN Employees e
    ON d.DeptId = e.DeptId
WHERE e.EmpId IS NULL;
```


**Reasoning and interview follow-ups**

Start from Departments because those rows must survive when no employee exists. The missing employee primary key marks an unsuccessful match, so an empty department is returned once. An equivalent formulation is:

```sql
SELECT d.DeptId, d.DeptName
FROM Departments d
WHERE NOT EXISTS (
    SELECT 1 FROM Employees e WHERE e.DeptId = d.DeptId
);
```

Checking Employees.DeptId IS NULL instead would find unassigned employees, which answers a different question.

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
    WHERE Salary IS NOT NULL
)
SELECT EmpName, DeptId, Salary
FROM RankedEmployees
WHERE SalaryRank <= 3;
```


**Reasoning and interview follow-ups**

This returns employees in the top three distinct known salary levels, potentially more than three people. For salaries 100, 100, 90, 80, 70, the first four employees qualify.

If the request is at most three employees, use ROW_NUMBER with ORDER BY Salary DESC, EmpId and filter to 3. If competition ranks are intended, use RANK and accept that gaps affect the cutoff. State the tie policy before choosing the function.

---

### 58. Find employees hired in the last 30 days.

```sql
DECLARE @Today DATE = CAST(GETDATE() AS DATE);

SELECT *
FROM Employees
WHERE HireDate >= DATEADD(DAY, -29, @Today)
  AND HireDate < DATEADD(DAY, 1, @Today);
```


**Reasoning and interview follow-ups**

Define the interval first. With a DATE column, this version interprets the last 30 days as today and the previous 29 calendar dates, excluding future hires. A lower-bound-only predicate would incorrectly include future dates.

For a timestamp and a rolling 30-times-24-hour interval, capture one current timestamp, subtract 30 days, and apply both interval bounds. Agree on timezone and whether the current instant is included; a server-local date and a business-local date can differ.

---

### 59. Find running total of salaries by hire date.

```sql
SELECT
    EmpName,
    HireDate,
    Salary,
    SUM(Salary) OVER (
        ORDER BY HireDate, EmpId
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS RunningSalaryTotal
FROM Employees
ORDER BY HireDate, EmpId;
```


**Reasoning and interview follow-ups**

UNBOUNDED PRECEDING starts at the first ordered row and CURRENT ROW ends at the current row. Salaries 100, 90, and 80 produce running totals 100, 190, and 270 in that order.

HireDate, EmpId resolves same-date ties deterministically. Explicit ROWS accumulates row by row; a peer-based frame can include every row sharing an ordering value at once. Window ordering controls calculation, while the final ORDER BY controls display. See [window frames](https://learn.microsoft.com/en-us/sql/t-sql/queries/select-over-clause-transact-sql).

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
AND CAST(n2.Id AS BIGINT) - n1.Id > 1;
```


**Reasoning and interview follow-ups**

The original query pairs each ID with the smallest greater ID and reports a gap when they differ by more than one. For IDs 1, 2, 5, 8, the missing ranges are 3 through 4 and 6 through 7. LEAD expresses the adjacent-row relationship more directly:

```sql
WITH AdjacentIds AS (
    SELECT CAST(Id AS BIGINT) AS Id,
           LEAD(CAST(Id AS BIGINT)) OVER (ORDER BY Id) AS NextId
    FROM Numbers
)
SELECT Id + 1 AS GapStart, NextId - 1 AS GapEnd
FROM AdjacentIds
WHERE NextId - Id > 1
ORDER BY Id;
```

Casting before arithmetic avoids INT boundary overflow. The final row has no successor and does not qualify. This reports internal ranges, not every missing integer or gaps outside the observed minimum and maximum. Identity values can have legitimate gaps after rollbacks, so a gap does not by itself prove lost data.

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


**Reasoning and interview follow-ups**

Aliases belong to a query scope. AnnualSalary is created by SELECT after WHERE logically filters input. In the CTE version, it is already an input column of the outer query, so the outer WHERE can use it.

SQL Server permits a SELECT alias in ORDER BY because ordering occurs later logically. A CTE improves naming here but does not promise one-time computation or storage. Multiplication by 12 also assumes Salary is monthly, which should be stated before interpreting the result as annual pay.

---

### 62. What is wrong with SELECT *?

`SELECT *` is convenient but risky in production.

Problems:

- Reads unnecessary columns
- More network transfer
- Can break code if table schema changes
- Makes covering the query with a narrow index harder
- Makes query intent unclear

Prefer:

```sql
SELECT EmpId, EmpName, Salary
FROM Employees;
```


**Reasoning and interview follow-ups**

Explicit columns create a stable result contract and make narrower access paths possible. Adding a large text column should not silently increase every application query's payload. Positional result mapping and insert-select statements are especially sensitive to shape changes.

SELECT * is reasonable for exploration or when all columns are required. It does not categorically forbid a covering index; it makes coverage harder. COUNT(*) and EXISTS (SELECT *) have different semantics and do not mean return every column to the client.

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


**Reasoning and interview follow-ups**

BETWEEN includes both endpoints. A date-only upper literal converted to a timestamp represents the start of that date, so most of May 31 lies beyond the bound.

A half-open range includes the start and excludes the next period's start, working across timestamp precisions and preventing overlap between adjacent periods. Avoid invented end times such as 23:59:59.999 because precision and rounding vary. A DATE-only column does not have this time-of-day issue.

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


**Reasoning and interview follow-ups**

The original predicate is correct for known departments other than 10. It is incomplete when unassigned employees should also qualify: NULL <> 10 is UNKNOWN and WHERE discards it.

The OR branch states the missing-value policy explicitly. When adding another filter, write `(DeptId <> 10 OR DeptId IS NULL) AND Salary > 50000`; without parentheses, operator precedence can apply the salary condition only to one branch.

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

For suffix search at scale, consider an indexed domain column or indexed reversed representation, depending on the required semantics.


**Reasoning and interview follow-ups**

An ordered string index can often narrow a known prefix such as john% to a key range. A suffix supplies no starting key, so the engine generally examines more values. An index scan can still be possible; the lost benefit is a selective seek.

For exact email-domain matching, compute or store the domain and index it. LIKE '%gmail.com' also matches notgmail.com, so its semantics can be wrong. Full-text search uses tokens and linguistic matching; it is not a general substitute for arbitrary suffix matching.

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


**Reasoning and interview follow-ups**

SQL Server uses type precedence to reconcile a string column with a number. Converting stored phone strings to numbers can make the string index less useful and fail on nonnumeric values.

Phone numbers are identifiers: leading zeros, plus signs, and formatting can matter, making numeric comparison conceptually wrong too. Bind a compatible string parameter. Converting a parameter can be harmless; focus on conversions applied to the indexed column and their actual plan effects.

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


**Reasoning and interview follow-ups**

An index change, parallel execution, or a new plan can change observed order without changing the result rows. Only an outer ORDER BY requests presentation order.

ORDER BY EmpName still leaves equal names tied. Add EmpId for a total order, especially with TOP or pagination. Unique ordering resolves ties, although concurrent inserts or deletions can still shift offset-based pages between requests.

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


**Reasoning and interview follow-ups**

The optimizer may implement grouping with a hash operation or an ordered stream. Changing data or costs can change that choice, so an apparently sorted grouped result has no ordering contract.

GROUP BY DeptId ORDER BY DeptId explicitly asks for both behaviors. To show largest departments first, use ORDER BY COUNT(*) DESC, DeptId, with the department key resolving equal counts.

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


**Reasoning and interview follow-ups**

Without GROUP BY, an ordinary aggregate query produces one overall result even for empty input: COUNT is zero, while SUM, AVG, MIN, and MAX are NULL. With GROUP BY DeptId, empty input produces no groups and therefore no rows. HAVING can also remove an overall aggregate row.

COALESCE replaces a NULL in an existing result row; it does not create absent departments. To show every department with zero payroll when empty, start from Departments, left-join Employees, group, and then replace NULL sums if zero matches the requirement.

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


**Reasoning and interview follow-ups**

Concatenation lets input become SQL syntax. Binding a parameter preserves statement structure and passes the input as a value, even when it contains quotes. A stored procedure remains vulnerable if it builds dynamic SQL through unsafe concatenation internally.

In Java, use a bound parameter:

```java
String sql = "SELECT UserId, Email FROM Users WHERE Email = ?";
try (PreparedStatement statement = connection.prepareStatement(sql)) {
    statement.setString(1, email);
    try (ResultSet result = statement.executeQuery()) {
        while (result.next()) {
            long userId = result.getLong("UserId");
            String storedEmail = result.getString("Email");
            // Map the selected values to an application result object.
        }
    }
}
```

This assumes an existing JDBC connection, input email, and java.sql imports. Parameters bind values, not table names, column names, or ASC/DESC keywords. Choose dynamic identifiers from a fixed allowlist. Least privilege limits damage but does not replace parameterization.

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
| What is ACID? | Atomicity, Consistency, Isolation, Durability |
| Best way to compare NULL? | `IS NULL` or `IS NOT NULL` |
| Safest alternative to `NOT IN` with nullable subquery? | `NOT EXISTS` |
| Best way to get nth salary with ties? | `DENSE_RANK()` |
| Is result order guaranteed without `ORDER BY`? | No |

## Strong vs Weak Entities (Tables)

A strong entity has an identifier independent of another entity's identifier. A weak entity, in classical entity-relationship modeling, is identified using its owner's key plus a partial key unique only within that owner. This concerns identity and existence dependency, not merely whether a table contains a foreign key.

An order line's LineNumber is unique only inside its order. Orders 100 and 200 can each contain line 1, so the line is identified by (OrderId, LineNumber):

```sql
CREATE TABLE SalesOrders (
    OrderId INT PRIMARY KEY
);

CREATE TABLE OrderLines (
    OrderId INT NOT NULL,
    LineNumber INT NOT NULL,
    ProductId INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    PRIMARY KEY (OrderId, LineNumber),
    FOREIGN KEY (OrderId) REFERENCES SalesOrders(OrderId)
);
```

The composite key prevents duplicate line numbers within one order, while the foreign key prevents lines referencing nonexistent orders. ProductId is a plain identifier in this minimal example; a full product model would also reference Products.

**Does weak ownership require cascade deletion?** No. The database can reject deleting a parent that still has children. Cascading deletion is one policy; explicitly deleting children first in a transaction is another. The example uses the default rejection behavior, so no orphan can survive.

**Is every foreign-key table a weak entity?** No. An employee can have an independent EmpId and a department reference without being identified by that department. A surrogate ID on OrderLines can simplify references, but the business rule still needs a unique constraint on (OrderId, LineNumber) if repeated line numbers are forbidden.

## How to Practice These Questions

For each answer, explain the definition, the mechanism, and one case where a naive answer fails. Before writing a query, state whether you need one row per employee, department, or distinct salary. Then explain how joins, filters, grouping, windows, ties, NULLs, and ordering produce that result.

Practice with small counterexamples: tied salaries, an unassigned employee, an empty department, duplicate emails, and two hires on the same date. Predict the result before running the query; these cases show whether you understand its behavior or only remember its syntax.
