# Database Normalization

## What is Normalization?
Normalization is the process of organizing data in a database to reduce data redundancy and improve data integrity. It involves dividing large tables into smaller, related tables and defining relationships between them to eliminate redundant data.

## Why do we need Normalization?
Without normalization, a database can suffer from data anomalies, which make it difficult to maintain and update data correctly. There are three main types of anomalies:
1. **Update Anomaly:** When a piece of data is stored in multiple places, updating it in one place but not the others leads to inconsistent data.
2. **Insertion Anomaly:** When certain attributes cannot be inserted into the database without the presence of other attributes.
3. **Deletion Anomaly:** When deleting a record unintentionally causes the loss of other important data.

## Normal Forms
Normalization rules are categorized into several "Normal Forms." The most commonly used are 1NF, 2NF, 3NF, and BCNF.

### First Normal Form (1NF)
A table is in 1NF if:
- It contains only atomic (indivisible) values. There are no repeating groups or arrays.
- Each column must contain values of the same data type.
- Each column must have a unique name.
- The order in which data is stored does not matter.

**Example:**
*Unnormalized:*
| Student ID | Name | Subjects |
| :--- | :--- | :--- |
| 1 | John | Math, Science |
| 2 | Alice | English |

*1NF:*
| Student ID | Name | Subject |
| :--- | :--- | :--- |
| 1 | John | Math |
| 1 | John | Science |
| 2 | Alice | English |

### Second Normal Form (2NF)
A table is in 2NF if:
- It is in 1NF.
- It has no **partial dependencies**. Every non-prime attribute (an attribute that is not part of any candidate key) must be fully functionally dependent on the entire primary key.
- *Note: If a table has a single-column primary key and is in 1NF, it is automatically in 2NF.*

**Example:**
Consider a table with a composite primary key (Student ID, Course ID) and a non-prime attribute `Course Name`. The `Course Name` depends only on `Course ID`, not on the full primary key (Student ID). To convert to 2NF, you would separate the course details into a new table.

### Third Normal Form (3NF)
A table is in 3NF if:
- It is in 2NF.
- It has no **transitive dependencies**. A non-prime attribute cannot depend on another non-prime attribute. All non-prime attributes must depend ONLY on the primary key. (Often summarized as: "The key, the whole key, and nothing but the key, so help me Codd.")

**Example:**
A table `Employee` has columns `Employee ID`, `Name`, `Department ID`, and `Department Name`. The `Department Name` depends on `Department ID`, which in turn depends on `Employee ID`. This transitive dependency is resolved by splitting it into an `Employee` table and a `Department` table.

### Boyce-Codd Normal Form (BCNF)
A table is in BCNF (also known as 3.5NF) if:
- It is in 3NF.
- For every non-trivial functional dependency $X \rightarrow Y$, $X$ must be a superkey.
- It handles anomalies that can still occur in 3NF if there are overlapping candidate keys.

## Denormalization
While normalization reduces redundancy and prevents anomalies, it often requires joining multiple tables during queries, which can slow down read performance. **Denormalization** is the process of intentionally adding redundant data back into a normalized database to improve the performance of read-heavy workloads (like in Data Warehouses or OLAP systems).
