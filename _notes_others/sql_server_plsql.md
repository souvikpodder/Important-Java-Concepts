# SQL Server — Complete Reference (Functions, PL/SQL & Complex Queries)

> A comprehensive guide covering **SQL Server built-in functions**, **PL/SQL (Procedural SQL)** constructs, and **complex query examples** with real-world scenarios.

---

## Table of Contents

1. [String Functions](#1-string-functions)
2. [Numeric / Math Functions](#2-numeric--math-functions)
3. [Date & Time Functions](#3-date--time-functions)
4. [Aggregate Functions](#4-aggregate-functions)
5. [Conversion Functions](#5-conversion-functions)
6. [Logical / Conditional Functions](#6-logical--conditional-functions)
7. [Window / Analytic Functions](#7-window--analytic-functions)
8. [JSON Functions](#8-json-functions)
9. [System & Metadata Functions](#9-system--metadata-functions)
10. [PL/SQL — Procedural SQL in SQL Server (T-SQL)](#10-plsql--procedural-sql-in-sql-server-t-sql)
11. [Complex Query Examples](#11-complex-query-examples)

---

## 1. String Functions

| Function | Description | Example |
|---|---|---|
| `LEN(str)` | Length of string (excluding trailing spaces) | `SELECT LEN('Hello');` → `5` |
| `DATALENGTH(str)` | Number of bytes used | `SELECT DATALENGTH(N'Hello');` → `10` |
| `LEFT(str, n)` | First *n* characters | `SELECT LEFT('Database', 4);` → `Data` |
| `RIGHT(str, n)` | Last *n* characters | `SELECT RIGHT('Database', 4);` → `base` |
| `SUBSTRING(str, start, len)` | Extract substring | `SELECT SUBSTRING('Hello World', 7, 5);` → `World` |
| `CHARINDEX(find, str)` | Position of substring | `SELECT CHARINDEX('lo', 'Hello');` → `4` |
| `PATINDEX(pattern, str)` | Position using pattern | `SELECT PATINDEX('%lo%', 'Hello');` → `4` |
| `REPLACE(str, old, new)` | Replace occurrences | `SELECT REPLACE('abc', 'b', 'x');` → `axc` |
| `STUFF(str, start, len, new)` | Delete & insert | `SELECT STUFF('Hello', 2, 3, 'XY');` → `HXYo` |
| `UPPER(str)` | Uppercase | `SELECT UPPER('hello');` → `HELLO` |
| `LOWER(str)` | Lowercase | `SELECT LOWER('HELLO');` → `hello` |
| `LTRIM(str)` | Remove leading spaces | `SELECT LTRIM('  Hi');` → `Hi` |
| `RTRIM(str)` | Remove trailing spaces | `SELECT RTRIM('Hi  ');` → `Hi` |
| `TRIM(str)` | Remove both (SQL Server 2017+) | `SELECT TRIM('  Hi  ');` → `Hi` |
| `CONCAT(a, b, ...)` | Concatenate strings | `SELECT CONCAT('A', '-', 'B');` → `A-B` |
| `CONCAT_WS(sep, a, b, ...)` | Concatenate with separator | `SELECT CONCAT_WS('-', 'A', 'B', 'C');` → `A-B-C` |
| `STRING_AGG(col, sep)` | Aggregate strings (2017+) | `SELECT STRING_AGG(Name, ', ') FROM Emp;` |
| `REVERSE(str)` | Reverse a string | `SELECT REVERSE('Hello');` → `olleH` |
| `REPLICATE(str, n)` | Repeat string *n* times | `SELECT REPLICATE('Ab', 3);` → `AbAbAb` |
| `SPACE(n)` | *n* space characters | `SELECT 'A' + SPACE(5) + 'B';` |
| `FORMAT(value, fmt)` | Format to string | `SELECT FORMAT(1234.5, 'N2');` → `1,234.50` |
| `CHAR(n)` | ASCII code → character | `SELECT CHAR(65);` → `A` |
| `ASCII(ch)` | Character → ASCII code | `SELECT ASCII('A');` → `65` |
| `UNICODE(ch)` | Character → Unicode code | `SELECT UNICODE(N'€');` → `8364` |
| `NCHAR(n)` | Unicode code → character | `SELECT NCHAR(8364);` → `€` |
| `QUOTENAME(str)` | Wrap with brackets | `SELECT QUOTENAME('Table');` → `[Table]` |
| `STRING_SPLIT(str, sep)` | Split into rows (2016+) | `SELECT * FROM STRING_SPLIT('a,b,c', ',');` |
| `TRANSLATE(str, from, to)` | Translate characters (2017+) | `SELECT TRANSLATE('2*[3+4]', '[]', '()');` → `2*(3+4)` |
| `STRING_ESCAPE(str, type)` | Escape special chars | `SELECT STRING_ESCAPE('"Hi"', 'json');` |
| `SOUNDEX(str)` | Phonetic code | `SELECT SOUNDEX('Smith');` → `S530` |
| `DIFFERENCE(a, b)` | Compare SOUNDEX (0-4) | `SELECT DIFFERENCE('Smith', 'Smyth');` → `4` |

---

## 2. Numeric / Math Functions

| Function | Description | Example |
|---|---|---|
| `ABS(n)` | Absolute value | `SELECT ABS(-15);` → `15` |
| `CEILING(n)` | Round up to integer | `SELECT CEILING(4.2);` → `5` |
| `FLOOR(n)` | Round down to integer | `SELECT FLOOR(4.8);` → `4` |
| `ROUND(n, d)` | Round to *d* decimals | `SELECT ROUND(123.456, 2);` → `123.460` |
| `POWER(base, exp)` | Exponentiation | `SELECT POWER(2, 10);` → `1024` |
| `SQRT(n)` | Square root | `SELECT SQRT(144);` → `12` |
| `SQUARE(n)` | Square of number | `SELECT SQUARE(9);` → `81` |
| `LOG(n)` | Natural logarithm | `SELECT LOG(2.718);` → `~1` |
| `LOG10(n)` | Base-10 logarithm | `SELECT LOG10(1000);` → `3` |
| `EXP(n)` | e raised to power | `SELECT EXP(1);` → `2.718…` |
| `SIGN(n)` | Sign (-1, 0, 1) | `SELECT SIGN(-42);` → `-1` |
| `PI()` | Value of π | `SELECT PI();` → `3.14159…` |
| `RAND([seed])` | Random float 0–1 | `SELECT RAND();` |
| `SIN / COS / TAN(n)` | Trigonometric | `SELECT SIN(PI()/2);` → `1` |
| `ASIN / ACOS / ATAN(n)` | Inverse trig | `SELECT ACOS(0);` → `1.5707…` |
| `ATN2(y, x)` | Angle in radians | `SELECT ATN2(1, 1);` → `0.785…` |
| `DEGREES(rad)` | Radians → degrees | `SELECT DEGREES(PI());` → `180` |
| `RADIANS(deg)` | Degrees → radians | `SELECT RADIANS(180);` → `3.1415…` |

---

## 3. Date & Time Functions

| Function | Description | Example |
|---|---|---|
| `GETDATE()` | Current date/time | `SELECT GETDATE();` |
| `GETUTCDATE()` | Current UTC date/time | `SELECT GETUTCDATE();` |
| `SYSDATETIME()` | Higher precision datetime | `SELECT SYSDATETIME();` |
| `SYSUTCDATETIME()` | Higher precision UTC | `SELECT SYSUTCDATETIME();` |
| `CURRENT_TIMESTAMP` | ANSI equivalent of GETDATE | `SELECT CURRENT_TIMESTAMP;` |
| `DATEADD(part, n, date)` | Add interval | `SELECT DATEADD(DAY, 7, GETDATE());` |
| `DATEDIFF(part, start, end)` | Difference between dates | `SELECT DATEDIFF(YEAR, '2000-01-01', GETDATE());` |
| `DATEDIFF_BIG(part, s, e)` | Big int result (2016+) | `SELECT DATEDIFF_BIG(SECOND, '1970-01-01', GETDATE());` |
| `DATEPART(part, date)` | Extract date part as int | `SELECT DATEPART(MONTH, GETDATE());` |
| `DATENAME(part, date)` | Extract part as string | `SELECT DATENAME(MONTH, GETDATE());` → `February` |
| `YEAR(date)` | Extract year | `SELECT YEAR('2026-02-28');` → `2026` |
| `MONTH(date)` | Extract month | `SELECT MONTH('2026-02-28');` → `2` |
| `DAY(date)` | Extract day | `SELECT DAY('2026-02-28');` → `28` |
| `EOMONTH(date [,offset])` | End of month | `SELECT EOMONTH('2026-02-15');` → `2026-02-28` |
| `ISDATE(expr)` | 1 if valid date, else 0 | `SELECT ISDATE('2026-02-30');` → `0` |
| `DATEFROMPARTS(y,m,d)` | Construct date | `SELECT DATEFROMPARTS(2026, 2, 28);` |
| `DATETIMEFROMPARTS(...)` | Construct datetime | `SELECT DATETIMEFROMPARTS(2026,2,28,10,30,0,0);` |
| `TIMEFROMPARTS(h,m,s,f,p)` | Construct time | `SELECT TIMEFROMPARTS(14, 30, 45, 0, 0);` |
| `SWITCHOFFSET(dto, tz)` | Change timezone offset | `SELECT SWITCHOFFSET(SYSDATETIMEOFFSET(), '+05:30');` |
| `TODATETIMEOFFSET(dt, tz)` | Add timezone to datetime | `SELECT TODATETIMEOFFSET(GETDATE(), '+05:30');` |
| `AT TIME ZONE` | Convert to timezone (2016+) | `SELECT GETDATE() AT TIME ZONE 'India Standard Time';` |
| `FORMAT(date, fmt)` | Custom date formatting | `SELECT FORMAT(GETDATE(), 'dd-MMM-yyyy');` |

**Common datepart values:** `YEAR`, `QUARTER`, `MONTH`, `WEEK`, `DAY`, `HOUR`, `MINUTE`, `SECOND`, `MILLISECOND`, `DAYOFYEAR`, `WEEKDAY`

---

## 4. Aggregate Functions

| Function | Description | Example |
|---|---|---|
| `COUNT(*)` | Count all rows | `SELECT COUNT(*) FROM Employees;` |
| `COUNT(col)` | Count non-NULL values | `SELECT COUNT(Email) FROM Employees;` |
| `COUNT(DISTINCT col)` | Count distinct non-NULL | `SELECT COUNT(DISTINCT DeptId) FROM Employees;` |
| `SUM(col)` | Sum of values | `SELECT SUM(Salary) FROM Employees;` |
| `AVG(col)` | Average of values | `SELECT AVG(Salary) FROM Employees;` |
| `MIN(col)` | Minimum value | `SELECT MIN(HireDate) FROM Employees;` |
| `MAX(col)` | Maximum value | `SELECT MAX(Salary) FROM Employees;` |
| `STDEV(col)` | Standard deviation (sample) | `SELECT STDEV(Salary) FROM Employees;` |
| `STDEVP(col)` | Std deviation (population) | `SELECT STDEVP(Salary) FROM Employees;` |
| `VAR(col)` | Variance (sample) | `SELECT VAR(Salary) FROM Employees;` |
| `VARP(col)` | Variance (population) | `SELECT VARP(Salary) FROM Employees;` |
| `CHECKSUM_AGG(col)` | Checksum of group | `SELECT CHECKSUM_AGG(BINARY_CHECKSUM(*)) FROM Employees;` |
| `GROUPING(col)` | 1 if rolled up, else 0 | Used with `ROLLUP` / `CUBE` |
| `GROUPING_ID(cols)` | Bitmask of grouping | Used with `ROLLUP` / `CUBE` |
| `STRING_AGG(col, sep)` | Concatenate with sep (2017+) | `SELECT STRING_AGG(Name, ', ') FROM Employees;` |
| `APPROX_COUNT_DISTINCT(col)` | Approx distinct count (2019+) | `SELECT APPROX_COUNT_DISTINCT(UserId) FROM Logs;` |

---

## 5. Conversion Functions

| Function | Description | Example |
|---|---|---|
| `CAST(expr AS type)` | Convert data type (ANSI) | `SELECT CAST('123' AS INT);` |
| `CONVERT(type, expr [,style])` | Convert with style code | `SELECT CONVERT(VARCHAR, GETDATE(), 103);` → `28/02/2026` |
| `TRY_CAST(expr AS type)` | Safe CAST (returns NULL) | `SELECT TRY_CAST('abc' AS INT);` → `NULL` |
| `TRY_CONVERT(type, expr)` | Safe CONVERT (returns NULL) | `SELECT TRY_CONVERT(INT, 'abc');` → `NULL` |
| `PARSE(str AS type [USING culture])` | Culture-aware parse | `SELECT PARSE('28/02/2026' AS DATE USING 'en-GB');` |
| `TRY_PARSE(str AS type)` | Safe PARSE | `SELECT TRY_PARSE('abc' AS INT);` → `NULL` |
| `STR(float [,len [,dec]])` | Float → string | `SELECT STR(123.456, 7, 2);` → ` 123.46` |
| `FORMAT(value, fmt [,culture])` | Locale-aware formatting | `SELECT FORMAT(1234.5, 'C', 'en-IN');` → `₹1,234.50` |

**Common CONVERT Style Codes:**

| Style | Format | Example Output |
|---|---|---|
| 101 | `mm/dd/yyyy` | `02/28/2026` |
| 103 | `dd/mm/yyyy` | `28/02/2026` |
| 104 | `dd.mm.yyyy` | `28.02.2026` |
| 110 | `mm-dd-yyyy` | `02-28-2026` |
| 120 | `yyyy-mm-dd hh:mi:ss` | `2026-02-28 16:43:09` |
| 126 | ISO 8601 | `2026-02-28T16:43:09` |

---

## 6. Logical / Conditional Functions

| Function | Description | Example |
|---|---|---|
| `ISNULL(expr, replacement)` | Replace NULL | `SELECT ISNULL(Phone, 'N/A') FROM Emp;` |
| `COALESCE(a, b, c, ...)` | First non-NULL (ANSI) | `SELECT COALESCE(Phone, Mobile, 'N/A');` |
| `NULLIF(a, b)` | NULL if a = b | `SELECT NULLIF(Bonus, 0);` |
| `IIF(cond, true, false)` | Inline IF (2012+) | `SELECT IIF(Salary > 50000, 'High', 'Low');` |
| `CHOOSE(idx, v1, v2, ...)` | Pick by index | `SELECT CHOOSE(MONTH(GETDATE()), 'Jan', 'Feb', ...);` |
| `CASE WHEN ... THEN ... END` | Case expression | See examples below |

### CASE Expression

**Simple CASE** — Works like a `switch` statement. Compares **one expression** (`DeptId`) against fixed values using **equality only** (`=`). Cannot use `>`, `<`, `LIKE`, etc.

```sql
-- Simple CASE: maps DeptId (1,2,3) to department names; anything else → 'Other'
SELECT Name,
    CASE DeptId
        WHEN 1 THEN 'Engineering'
        WHEN 2 THEN 'Marketing'
        WHEN 3 THEN 'Finance'
        ELSE 'Other'
    END AS Department
FROM Employees;
```

**Searched CASE** — Works like `if / else if / else`. Each `WHEN` has its **own independent boolean condition**, so you can use ranges (`>=`), multiple columns, `LIKE`, `IS NULL`, `AND`/`OR`, etc.

```sql
-- Searched CASE: classifies employees into salary bands using range comparisons
-- Conditions are evaluated top-to-bottom; first TRUE match wins
SELECT Name, Salary,
    CASE
        WHEN Salary >= 100000 THEN 'Executive'
        WHEN Salary >= 60000  THEN 'Senior'
        WHEN Salary >= 30000  THEN 'Mid-Level'
        ELSE 'Junior'
    END AS Band
FROM Employees;
```

---

## 7. Window / Analytic Functions

Window functions operate over a set of rows **without collapsing** them.

### Syntax

Every window function uses the `OVER()` clause. **PARTITION BY** divides rows into groups (like GROUP BY but without collapsing). **ORDER BY** defines the order within each partition. **ROWS/RANGE** defines the window frame (which rows to include in the calculation).

```sql
FUNCTION_NAME(args) OVER (
    [PARTITION BY col1, col2, ...]       -- Split into groups
    [ORDER BY col1 [ASC|DESC], ...]      -- Order within each group
    [ROWS | RANGE BETWEEN ... AND ...]   -- Frame: which rows to include
)
```

### Ranking Functions

Assigns a rank/position number to each row. The key difference is how they handle **ties** (rows with equal values).

```sql
-- Compares all 4 ranking functions side-by-side on the same data
SELECT
    Name, DeptId, Salary,
    ROW_NUMBER() OVER (ORDER BY Salary DESC)                     AS RowNum,      -- Always unique: 1,2,3,4,5 (ties broken arbitrarily)
    RANK()       OVER (ORDER BY Salary DESC)                     AS Rnk,         -- Ties share rank, gaps after: 1,2,2,4,5
    DENSE_RANK() OVER (ORDER BY Salary DESC)                     AS DenseRnk,    -- Ties share rank, no gaps:    1,2,2,3,4
    NTILE(4)     OVER (ORDER BY Salary DESC)                     AS Quartile,    -- Splits all rows into 4 equal buckets (1-4)
    ROW_NUMBER() OVER (PARTITION BY DeptId ORDER BY Salary DESC) AS DeptRowNum   -- Restarts numbering within each department
FROM Employees;
```

| Function | Behavior |
|---|---|
| `ROW_NUMBER()` | Unique sequential number (no ties) |
| `RANK()` | Ties get same rank, next rank is skipped |
| `DENSE_RANK()` | Ties get same rank, no gap |
| `NTILE(n)` | Divide rows into *n* roughly equal groups |

### Value Functions

Access values from **other rows** without a self-join. Useful for comparing a row to its neighbors or to the first/last row in a partition.

```sql
-- For each employee (ordered by HireDate), peek at the previous and next employee's salary,
-- and find the first-ever and last-ever hired employee names
SELECT
    Name, Salary, HireDate,
    LAG(Salary, 1, 0)    OVER (ORDER BY HireDate)  AS PrevSalary,    -- Salary of the person hired just before (default 0 if none)
    LEAD(Salary, 1, 0)   OVER (ORDER BY HireDate)  AS NextSalary,   -- Salary of the person hired just after (default 0 if none)
    FIRST_VALUE(Name)     OVER (ORDER BY HireDate
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS FirstHired,  -- Name of the very first hired employee
    LAST_VALUE(Name)      OVER (ORDER BY HireDate
        ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING)  AS LastHired   -- Name of the very last hired employee
FROM Employees;
-- NOTE: LAST_VALUE needs explicit frame (CURRENT ROW to UNBOUNDED FOLLOWING),
-- otherwise default frame ends at CURRENT ROW and you'd always get the current row's name
```

| Function | Description |
|---|---|
| `LAG(col, offset, default)` | Value from *offset* rows before |
| `LEAD(col, offset, default)` | Value from *offset* rows after |
| `FIRST_VALUE(col)` | First value in window frame |
| `LAST_VALUE(col)` | Last value in window frame |
| `NTH_VALUE(col, n)` | *n*-th value (SQL Server 2022+) |

### Aggregate Window Functions

Same aggregates as GROUP BY (`SUM`, `AVG`, `COUNT`) but **each row keeps its own identity** — the aggregate value is added as an extra column rather than collapsing rows.

```sql
-- Each row shows the employee's own data PLUS department-level aggregates alongside
SELECT
    Name, DeptId, Salary,
    SUM(Salary)   OVER (PARTITION BY DeptId)                       AS DeptTotal,    -- Total salary of the entire department
    AVG(Salary)   OVER (PARTITION BY DeptId)                       AS DeptAvg,      -- Avg salary in the department
    COUNT(*)      OVER (PARTITION BY DeptId)                       AS DeptCount,    -- Number of people in the department
    SUM(Salary)   OVER (ORDER BY HireDate
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)         AS RunningTotal, -- Cumulative salary sum (grows with each row)
    AVG(Salary)   OVER (ORDER BY HireDate
        ROWS BETWEEN 2 PRECEDING AND CURRENT ROW)                 AS MovingAvg3   -- Average of current + 2 previous rows (3-row sliding window)
FROM Employees;
```

### Statistical Functions

| Function | Description |
|---|---|
| `PERCENT_RANK()` | Relative rank (0 to 1) |
| `CUME_DIST()` | Cumulative distribution |
| `PERCENTILE_CONT(p) WITHIN GROUP (ORDER BY col) OVER (...)` | Continuous percentile |
| `PERCENTILE_DISC(p) WITHIN GROUP (ORDER BY col) OVER (...)` | Discrete percentile |

---

## 8. JSON Functions

*(SQL Server 2016+)*

| Function | Description | Example |
|---|---|---|
| `ISJSON(str)` | 1 if valid JSON | `SELECT ISJSON('{"a":1}');` → `1` |
| `JSON_VALUE(json, path)` | Extract scalar value | `SELECT JSON_VALUE('{"name":"John"}', '$.name');` → `John` |
| `JSON_QUERY(json, path)` | Extract object/array | `SELECT JSON_QUERY('{"a":[1,2]}', '$.a');` → `[1,2]` |
| `JSON_MODIFY(json, path, val)` | Modify JSON value | `SELECT JSON_MODIFY('{"a":1}', '$.a', 2);` → `{"a":2}` |
| `OPENJSON(json)` | Parse JSON to rows | See below |
| `FOR JSON PATH` | Result set → JSON | `SELECT * FROM Emp FOR JSON PATH;` |
| `FOR JSON AUTO` | Auto-structured JSON | `SELECT * FROM Emp FOR JSON AUTO;` |
| `JSON_OBJECT(...)` | Build JSON object (2022+) | `SELECT JSON_OBJECT('name':'John', 'age':30);` |
| `JSON_ARRAY(...)` | Build JSON array (2022+) | `SELECT JSON_ARRAY(1, 2, 'three');` |

**OPENJSON example** — Converts a JSON array into a relational table. The `WITH` clause defines the output schema: each `'$.path'` maps a JSON property to a column. `AS JSON` keeps nested arrays/objects as raw JSON strings instead of extracting a scalar.

```sql
-- Parse a JSON array of employees into a proper SQL result set with typed columns
DECLARE @json NVARCHAR(MAX) = N'[
    {"id":1, "name":"Alice", "skills":["SQL","Python"]},
    {"id":2, "name":"Bob",   "skills":["Java","C#"]}
]';

SELECT *
FROM OPENJSON(@json)         -- Expands the JSON array: each array element becomes one row
WITH (
    Id    INT          '$.id',           -- Extract 'id' as INT
    Name  NVARCHAR(50) '$.name',         -- Extract 'name' as NVARCHAR
    Skills NVARCHAR(MAX) '$.skills' AS JSON  -- Keep 'skills' array as raw JSON (not scalar)
);
-- Result: 2 rows with columns Id, Name, Skills
-- Row 1: 1, Alice, ["SQL","Python"]
-- Row 2: 2, Bob,   ["Java","C#"]
```

---

## 9. System & Metadata Functions

| Function | Description |
|---|---|
| `@@IDENTITY` | Last inserted identity value (any scope) |
| `SCOPE_IDENTITY()` | Last identity in current scope (**preferred**) |
| `IDENT_CURRENT('table')` | Last identity for a specific table |
| `@@ROWCOUNT` | Rows affected by last statement |
| `@@ERROR` | Error number of last statement |
| `@@TRANCOUNT` | Current transaction nesting level |
| `@@VERSION` | SQL Server version string |
| `@@SERVERNAME` | Server name |
| `@@SPID` | Current session ID |
| `DB_ID([name])` | Database ID |
| `DB_NAME([id])` | Database name |
| `OBJECT_ID('name')` | Object ID |
| `OBJECT_NAME(id)` | Object name |
| `SCHEMA_NAME([id])` | Schema name |
| `USER_NAME([id])` | User name |
| `SUSER_SNAME()` | Login name |
| `HOST_NAME()` | Client machine name |
| `APP_NAME()` | Application name |
| `NEWID()` | Generate new GUID | 
| `NEWSEQUENTIALID()` | Sequential GUID (default constraint only) |
| `CHECKSUM(*)` | Hash value for row |
| `BINARY_CHECKSUM(*)` | Binary hash value |
| `HASHBYTES(algo, data)` | Cryptographic hash (SHA2_256, etc.) |
| `COMPRESS(data)` | GZIP compress |
| `DECOMPRESS(data)` | GZIP decompress |
| `COLUMNPROPERTY(...)` | Column metadata |
| `SERVERPROPERTY(prop)` | Server property |
| `DATABASEPROPERTYEX(db, prop)` | Database property |

---

## 10. PL/SQL — Procedural SQL in SQL Server (T-SQL)

> **Note:** SQL Server uses **T-SQL (Transact-SQL)** as its procedural extension. The concepts below map to PL/SQL equivalents in Oracle.

### 10.1 Variables & Data Types

Variables are declared with `DECLARE @name TYPE [= value]` and assigned with `SET` or `SELECT`. T-SQL supports **table variables** (`DECLARE @t TABLE(...)`) which act like in-memory temp tables scoped to the current batch.

```sql
DECLARE @name      NVARCHAR(100) = N'John Doe';
DECLARE @salary    DECIMAL(10,2);
DECLARE @hireDate  DATE          = GETDATE();
DECLARE @isActive  BIT           = 1;

SET @salary = 75000.00;

-- Table variable
DECLARE @TempResults TABLE (
    Id      INT,
    Name    NVARCHAR(100),
    Amount  DECIMAL(10,2)
);

PRINT 'Employee: ' + @name + ' | Salary: ' + CAST(@salary AS VARCHAR);
```

### 10.2 Control Flow — IF / ELSE

T-SQL uses `IF...ELSE IF...ELSE` for branching. For **single statements**, no `BEGIN/END` is needed. For **multiple statements** in a branch, wrap them in `BEGIN...END`.

```sql
-- Assigns a letter grade based on score ranges; evaluated top-to-bottom
DECLARE @score INT = 85;

IF @score >= 90
    PRINT 'Grade: A';
ELSE IF @score >= 80
    PRINT 'Grade: B';
ELSE IF @score >= 70
    PRINT 'Grade: C';
ELSE
    PRINT 'Grade: F';
```

### 10.3 WHILE Loop

T-SQL has only `WHILE` loops (no `FOR` or `DO...WHILE`). Use `CONTINUE` to skip to the next iteration and `BREAK` to exit the loop early. The loop body must be wrapped in `BEGIN...END`.

```sql
-- Loops 1–10, but skips iteration 5 (CONTINUE) and exits at 8 (BREAK)
-- So it prints: 1, 2, 3, 4, 6, 7, 8
DECLARE @i INT = 1;

WHILE @i <= 10
BEGIN
    IF @i = 5
    BEGIN
        SET @i = @i + 1;
        CONTINUE;  -- Skip iteration
    END

    PRINT 'Iteration: ' + CAST(@i AS VARCHAR);

    IF @i = 8
        BREAK;  -- Exit loop

    SET @i = @i + 1;
END
```

### 10.4 Cursors

Cursors let you process rows **one at a time** in a loop. The lifecycle is: `DECLARE` → `OPEN` → `FETCH` in a loop → `CLOSE` → `DEALLOCATE`. Use `FAST_FORWARD` (read-only, forward-only) for best performance. `@@FETCH_STATUS = 0` means the last fetch succeeded.

> **Note:** Cursors are slow compared to set-based operations. Use them only when row-by-row processing is truly needed.

```sql
-- Iterates through all employees in DeptId=1, printing each one's details
DECLARE @empId INT, @empName NVARCHAR(100), @empSalary DECIMAL(10,2);

-- Declare cursor
DECLARE emp_cursor CURSOR FAST_FORWARD FOR
    SELECT EmployeeId, Name, Salary
    FROM Employees
    WHERE DeptId = 1
    ORDER BY Salary DESC;

-- Open and fetch
OPEN emp_cursor;
FETCH NEXT FROM emp_cursor INTO @empId, @empName, @empSalary;

WHILE @@FETCH_STATUS = 0
BEGIN
    PRINT CONCAT('ID: ', @empId, ' | Name: ', @empName, ' | Salary: $', @empSalary);
    FETCH NEXT FROM emp_cursor INTO @empId, @empName, @empSalary;
END

-- Cleanup
CLOSE emp_cursor;
DEALLOCATE emp_cursor;
```

### 10.5 TRY...CATCH (Error Handling)

Wraps risky operations in `BEGIN TRY...END TRY`. If any error occurs, control jumps to `BEGIN CATCH...END CATCH`. Inside CATCH, use `ERROR_NUMBER()`, `ERROR_MESSAGE()`, etc. to inspect the error. `THROW` re-raises the original error or raises custom errors (error numbers ≥ 50000).

```sql
-- Simulates a bank transfer: debit one account, credit another
-- If balance goes negative, THROW raises a custom error, which jumps to CATCH and rolls back
BEGIN TRY
    BEGIN TRANSACTION;

    UPDATE Accounts SET Balance = Balance - 500 WHERE AccountId = 101;
    UPDATE Accounts SET Balance = Balance + 500 WHERE AccountId = 202;

    -- Simulate error
    IF (SELECT Balance FROM Accounts WHERE AccountId = 101) < 0
        THROW 50001, 'Insufficient funds.', 1;

    COMMIT TRANSACTION;
    PRINT 'Transfer successful.';
END TRY
BEGIN CATCH
    ROLLBACK TRANSACTION;

    PRINT 'Error Number : ' + CAST(ERROR_NUMBER()   AS VARCHAR);
    PRINT 'Error Message: ' + ERROR_MESSAGE();
    PRINT 'Error Line   : ' + CAST(ERROR_LINE()     AS VARCHAR);
    PRINT 'Error Proc   : ' + ISNULL(ERROR_PROCEDURE(), 'N/A');
    PRINT 'Error State  : ' + CAST(ERROR_STATE()     AS VARCHAR);
    PRINT 'Error Severity: ' + CAST(ERROR_SEVERITY() AS VARCHAR);
END CATCH
```

### 10.6 Stored Procedures

Reusable, named blocks of SQL saved in the database. They accept **input parameters** (with optional defaults), **output parameters** (return values to the caller), and are executed with `EXEC`. `SET NOCOUNT ON` suppresses the "N rows affected" messages for cleaner output.

```sql
-- Creates a procedure that filters employees by department and min salary
-- Returns results + sets an OUTPUT parameter with the count of rows found
CREATE OR ALTER PROCEDURE usp_GetEmployeesByDept
    @DeptId       INT,
    @MinSalary    DECIMAL(10,2)  = 0,          -- Default parameter
    @TotalCount   INT            OUTPUT         -- Output parameter
AS
BEGIN
    SET NOCOUNT ON;

    SELECT EmployeeId, Name, Salary, HireDate
    FROM Employees
    WHERE DeptId = @DeptId
      AND Salary >= @MinSalary
    ORDER BY Salary DESC;

    SET @TotalCount = @@ROWCOUNT;
END;
GO

-- ═══ EXECUTE ═══
DECLARE @count INT;
EXEC usp_GetEmployeesByDept
    @DeptId    = 1,
    @MinSalary = 50000,
    @TotalCount = @count OUTPUT;

PRINT 'Total employees found: ' + CAST(@count AS VARCHAR);
```

### 10.7 User-Defined Functions

SQL Server supports **3 types** of UDFs:
- **Scalar Function** — Returns a single value. Called with `dbo.fn_Name()`.
- **Inline Table-Valued Function (iTVF)** — Returns a table from a single SELECT. Best performance.
- **Multi-Statement TVF** — Returns a table built with multiple statements. More flexible but slower.

```sql
-- SCALAR FUNCTION: Takes a salary and returns the tax amount based on bracket
-- Usage: SELECT dbo.fn_CalculateTax(75000); → returns 15000.00
CREATE OR ALTER FUNCTION dbo.fn_CalculateTax(@salary DECIMAL(10,2))
RETURNS DECIMAL(10,2)
AS
BEGIN
    RETURN CASE
        WHEN @salary > 100000 THEN @salary * 0.30
        WHEN @salary > 50000  THEN @salary * 0.20
        WHEN @salary > 25000  THEN @salary * 0.10
        ELSE 0
    END;
END;
GO

-- Usage
SELECT Name, Salary, dbo.fn_CalculateTax(Salary) AS Tax FROM Employees;

-- INLINE TABLE-VALUED FUNCTION: Returns the top N highest-paid employees as a table
-- Can be used in FROM clause: SELECT * FROM dbo.fn_GetTopEarners(10);
CREATE OR ALTER FUNCTION dbo.fn_GetTopEarners(@topN INT)
RETURNS TABLE
AS
RETURN (
    SELECT TOP (@topN) EmployeeId, Name, Salary, DeptId
    FROM Employees
    ORDER BY Salary DESC
);
GO

-- Usage
SELECT * FROM dbo.fn_GetTopEarners(10);

-- MULTI-STATEMENT TVF: Groups employees into salary bands and returns count + avg per band
-- Declares a return table variable, populates it with INSERT, then returns it
CREATE OR ALTER FUNCTION dbo.fn_SalaryBands()
RETURNS @Result TABLE (
    Band       NVARCHAR(20),
    EmpCount   INT,
    AvgSalary  DECIMAL(10,2)
)
AS
BEGIN
    INSERT INTO @Result
    SELECT
        CASE
            WHEN Salary >= 100000 THEN 'Executive'
            WHEN Salary >= 60000  THEN 'Senior'
            WHEN Salary >= 30000  THEN 'Mid-Level'
            ELSE 'Junior'
        END,
        COUNT(*),
        AVG(Salary)
    FROM Employees
    GROUP BY
        CASE
            WHEN Salary >= 100000 THEN 'Executive'
            WHEN Salary >= 60000  THEN 'Senior'
            WHEN Salary >= 30000  THEN 'Mid-Level'
            ELSE 'Junior'
        END;

    RETURN;
END;
GO
```

### 10.8 Triggers

Triggers are special procedures that **fire automatically** on INSERT, UPDATE, or DELETE. Two types:
- **AFTER** trigger — fires after the operation completes. Has access to `inserted` (new rows) and `deleted` (old rows) virtual tables.
- **INSTEAD OF** trigger — **replaces** the original operation entirely (the actual DELETE/INSERT/UPDATE never happens unless you code it).

```sql
-- AFTER UPDATE Trigger: Whenever someone changes an employee's salary,
-- this trigger automatically logs the old and new values into an AuditLog table
CREATE OR ALTER TRIGGER trg_AuditSalaryChange
ON Employees
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF UPDATE(Salary)
    BEGIN
        INSERT INTO AuditLog (TableName, Action, OldValue, NewValue, ChangedBy, ChangedAt)
        SELECT
            'Employees',
            'SALARY_UPDATE',
            CONCAT('EmpId=', d.EmployeeId, ', OldSalary=', d.Salary),
            CONCAT('EmpId=', i.EmployeeId, ', NewSalary=', i.Salary),
            SUSER_SNAME(),
            GETDATE()
        FROM inserted i
        INNER JOIN deleted d ON i.EmployeeId = d.EmployeeId
        WHERE i.Salary <> d.Salary;
    END
END;
GO

-- INSTEAD OF DELETE Trigger: Intercepts a DELETE and marks the employee as soft-deleted
-- instead of actually removing the row. The original DELETE never executes.
CREATE OR ALTER TRIGGER trg_SoftDeleteEmployee
ON Employees
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE e
    SET IsDeleted = 1,
        DeletedAt = GETDATE(),
        DeletedBy = SUSER_SNAME()
    FROM Employees e
    INNER JOIN deleted d ON e.EmployeeId = d.EmployeeId;

    PRINT CONCAT(@@ROWCOUNT, ' employee(s) soft-deleted.');
END;
GO
```

### 10.9 Transactions & Savepoints

Transactions ensure **all-or-nothing** execution. `SAVE TRANSACTION name` creates a **savepoint** — you can rollback to just that point without losing earlier work. Nested TRY/CATCH allows parts of a transaction to fail gracefully while the rest commits.

```sql
-- 3-phase bank transfer: Debit → Savepoint → Credit → Log
-- If logging (Phase 3) fails, only the log INSERT is rolled back; the transfer still commits
BEGIN TRY
    BEGIN TRANSACTION;

    -- Phase 1: Debit
    UPDATE Accounts SET Balance = Balance - 1000 WHERE AccountId = 1;
    SAVE TRANSACTION SaveAfterDebit;

    -- Phase 2: Credit
    UPDATE Accounts SET Balance = Balance + 1000 WHERE AccountId = 2;

    -- Phase 3: Log (can fail independently)
    BEGIN TRY
        INSERT INTO TransactionLog (FromAcc, ToAcc, Amount, TxDate)
        VALUES (1, 2, 1000, GETDATE());
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION SaveAfterDebit;  -- Only rollback logging
        PRINT 'Logging failed, but transfer committed.';
    END CATCH

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRANSACTION;

    THROW;
END CATCH
```

### 10.10 Dynamic SQL

Builds SQL strings at runtime and executes them. **Always use `sp_executesql` with parameters** instead of string concatenation to prevent SQL injection. `QUOTENAME()` wraps identifiers in brackets to safely handle special characters in table/column names.

```sql
-- Builds a SELECT query dynamically where table name, column name, and filter are variables
-- QUOTENAME() makes identifiers safe; @minVal is parameterized (not concatenated) to prevent injection
DECLARE @tableName  NVARCHAR(128) = N'Employees';
DECLARE @colName    NVARCHAR(128) = N'Salary';
DECLARE @minValue   DECIMAL(10,2) = 50000;
DECLARE @sql        NVARCHAR(MAX);
DECLARE @params     NVARCHAR(MAX);

-- Safe: parameterized dynamic SQL with sp_executesql
SET @sql = N'SELECT * FROM ' + QUOTENAME(@tableName)
         + N' WHERE ' + QUOTENAME(@colName) + N' >= @minVal'
         + N' ORDER BY ' + QUOTENAME(@colName) + N' DESC;';

SET @params = N'@minVal DECIMAL(10,2)';

EXEC sp_executesql @sql, @params, @minVal = @minValue;
```

### 10.11 Temporary Tables & CTEs

- **`#TempTable`** — Local temp table, visible only in the current session. Dropped automatically when session ends.
- **`##GlobalTemp`** — Global temp table, visible to all sessions. Dropped when the creating session ends.
- **CTE (`WITH ... AS`)** — A named, temporary result set that exists only for the duration of the following `SELECT`. Can be chained (multiple CTEs separated by commas).

```sql
-- LOCAL TEMP TABLE: Aggregates sales by product into a session-scoped temp table
CREATE TABLE #TempSales (
    ProductId   INT,
    TotalQty    INT,
    TotalAmount DECIMAL(12,2)
);

INSERT INTO #TempSales
SELECT ProductId, SUM(Quantity), SUM(Amount)
FROM Sales
GROUP BY ProductId;

-- GLOBAL TEMP TABLE: Visible across all connections; useful for cross-session data sharing
CREATE TABLE ##GlobalCache (Key_ NVARCHAR(100), Value_ NVARCHAR(MAX));

-- CTE CHAIN: First CTE aggregates sales per product, second CTE ranks them, final SELECT gets top 5
WITH SalesCTE AS (
    SELECT ProductId, SUM(Amount) AS Total
    FROM Sales
    GROUP BY ProductId
),
RankedCTE AS (
    SELECT *, RANK() OVER (ORDER BY Total DESC) AS Rnk
    FROM SalesCTE
)
SELECT * FROM RankedCTE WHERE Rnk <= 5;
```

### 10.12 MERGE Statement (Upsert)

`MERGE` combines INSERT, UPDATE, and DELETE into a **single atomic statement**. It compares a `target` table against a `source` and handles 3 scenarios:
- **MATCHED** — Row exists in both → UPDATE
- **NOT MATCHED BY TARGET** — Row in source but not target → INSERT
- **NOT MATCHED BY SOURCE** — Row in target but not source → DELETE

The `OUTPUT` clause returns what action was taken for each row.

```sql
-- Syncs Products table with a staging table:
-- Updates prices that changed, inserts new products, deletes discontinued ones
MERGE INTO Products AS target
USING StagingProducts AS source
ON target.ProductId = source.ProductId
WHEN MATCHED AND target.Price <> source.Price THEN
    UPDATE SET
        target.Price       = source.Price,
        target.UpdatedAt   = GETDATE()
WHEN NOT MATCHED BY TARGET THEN
    INSERT (ProductId, Name, Price, CreatedAt)
    VALUES (source.ProductId, source.Name, source.Price, GETDATE())
WHEN NOT MATCHED BY SOURCE THEN
    DELETE
OUTPUT $action, inserted.*, deleted.*;
```

---

## 11. Complex Query Examples

### 11.1 Employee Hierarchy — Recursive CTE

Builds an **org chart** by recursively traversing the employee–manager relationship. The **anchor** query selects top-level managers (no ManagerId). The **recursive** part joins each employee to their manager’s row, building the chain one level at a time. `OPTION (MAXRECURSION 100)` prevents infinite loops if there’s a circular reference.

```sql
WITH OrgChart AS (
    -- Anchor: top-level managers (no manager)
    SELECT
        EmployeeId, Name, ManagerId,
        CAST(Name AS NVARCHAR(MAX)) AS ReportingChain,
        0                           AS Level
    FROM Employees
    WHERE ManagerId IS NULL

    UNION ALL

    -- Recursive: each employee under their manager
    SELECT
        e.EmployeeId, e.Name, e.ManagerId,
        CONCAT(oc.ReportingChain, ' → ', e.Name),
        oc.Level + 1
    FROM Employees e
    INNER JOIN OrgChart oc ON e.ManagerId = oc.EmployeeId
)
SELECT
    EmployeeId,
    Name,
    Level,
    ReportingChain,
    REPLICATE('  ', Level) + Name AS Indented
FROM OrgChart
ORDER BY ReportingChain
OPTION (MAXRECURSION 100);
```

### 11.2 Year-over-Year Revenue Growth with Running Totals

**CTE 1 (`MonthlyRevenue`)**: Aggregates orders by year+month into revenue, customer count, and order count for the last 3 years.

**CTE 2 (`WithGrowth`)**: Adds analytical columns using window functions:
- `LAG(Revenue, 12)` — looks back **12 rows** (= same month last year) to calculate YoY growth
- `SUM(...) PARTITION BY OrderYear` — running Year-To-Date total that resets each year
- `AVG(...) ROWS BETWEEN 2 PRECEDING` — 3-month moving average to smooth out volatility

The final SELECT formats everything as currency and calculates YoY growth percentage.

```sql
WITH MonthlyRevenue AS (
    SELECT
        YEAR(OrderDate)                          AS OrderYear,
        MONTH(OrderDate)                         AS OrderMonth,
        SUM(TotalAmount)                         AS Revenue,
        COUNT(DISTINCT CustomerId)               AS UniqueCustomers,
        COUNT(*)                                 AS OrderCount
    FROM Orders
    WHERE OrderDate >= DATEADD(YEAR, -3, GETDATE())
    GROUP BY YEAR(OrderDate), MONTH(OrderDate)
),
WithGrowth AS (
    SELECT
        *,
        LAG(Revenue, 12) OVER (ORDER BY OrderYear, OrderMonth) AS RevenueSameMonthLastYear,
        SUM(Revenue) OVER (
            PARTITION BY OrderYear
            ORDER BY OrderMonth
            ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
        ) AS YTDRevenue,
        AVG(Revenue) OVER (
            ORDER BY OrderYear, OrderMonth
            ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
        ) AS MovingAvg3Month
    FROM MonthlyRevenue
)
SELECT
    OrderYear,
    OrderMonth,
    FORMAT(Revenue, 'C')                                                     AS Revenue,
    FORMAT(YTDRevenue, 'C')                                                  AS YTD,
    FORMAT(MovingAvg3Month, 'C')                                             AS AvgLast3Mo,
    UniqueCustomers,
    OrderCount,
    CASE
        WHEN RevenueSameMonthLastYear IS NOT NULL
        THEN FORMAT((Revenue - RevenueSameMonthLastYear)
              / RevenueSameMonthLastYear, 'P1')
        ELSE 'N/A'
    END                                                                      AS YoYGrowth
FROM WithGrowth
ORDER BY OrderYear, OrderMonth;
```

### 11.3 Top-N per Group (Top 3 Earners per Department)

A classic pattern: use `DENSE_RANK() OVER (PARTITION BY DeptId ORDER BY Salary DESC)` to rank employees **within each department**. Filter `WHERE DeptRank <= 3` in the outer query to keep only the top 3. Also calculates how each person’s salary compares to their department average and their percentile rank.

```sql
WITH RankedEmployees AS (
    SELECT
        e.EmployeeId,
        e.Name,
        d.DeptName,
        e.Salary,
        DENSE_RANK() OVER (PARTITION BY e.DeptId ORDER BY e.Salary DESC) AS DeptRank,
        e.Salary - AVG(e.Salary) OVER (PARTITION BY e.DeptId)            AS SalaryVsAvg,
        PERCENT_RANK() OVER (PARTITION BY e.DeptId ORDER BY e.Salary)    AS PctRank
    FROM Employees e
    INNER JOIN Departments d ON e.DeptId = d.DeptId
)
SELECT
    DeptName,
    Name,
    FORMAT(Salary, 'C')         AS Salary,
    DeptRank,
    FORMAT(SalaryVsAvg, '+#,##0;-#,##0') + ' vs avg' AS VsAverage,
    FORMAT(PctRank, 'P0')       AS Percentile
FROM RankedEmployees
WHERE DeptRank <= 3
ORDER BY DeptName, DeptRank;
```

### 11.4 Gaps & Islands — Find Consecutive Date Ranges

The **Gaps & Islands** problem: finding consecutive sequences in data. The trick: subtracting `ROW_NUMBER()` from the date. Consecutive dates (Jan 1, Jan 2, Jan 3) minus row numbers (1, 2, 3) all produce the **same GroupId**. Non-consecutive dates produce different GroupIds. Then GROUP BY GroupId to find each streak’s start, end, and duration.

```sql
WITH NumberedLogins AS (
    SELECT
        UserId,
        LoginDate,
        LoginDate - CAST(
            ROW_NUMBER() OVER (PARTITION BY UserId ORDER BY LoginDate)
            AS INT
        ) AS GroupId   -- Same GroupId = consecutive
    -- Note: date minus int arithmetic; alternatively use DATEADD
    FROM (SELECT DISTINCT UserId, CAST(LoginDate AS DATE) AS LoginDate
          FROM UserLogins) AS DistinctLogins
),
Streaks AS (
    SELECT
        UserId,
        MIN(LoginDate)                             AS StreakStart,
        MAX(LoginDate)                             AS StreakEnd,
        DATEDIFF(DAY, MIN(LoginDate), MAX(LoginDate)) + 1 AS StreakDays
    FROM NumberedLogins
    GROUP BY UserId, GroupId
)
SELECT
    UserId,
    StreakStart,
    StreakEnd,
    StreakDays,
    RANK() OVER (PARTITION BY UserId ORDER BY StreakDays DESC) AS StreakRank
FROM Streaks
ORDER BY UserId, StreakDays DESC;
```

### 11.5 PIVOT — Monthly Sales Crosstab

`PIVOT` rotates rows into columns. The **subquery** produces raw data with columns (ProductName, MonthName, SaleAmount). `PIVOT` then groups by ProductName and creates one column per month, filling each cell with `SUM(SaleAmount)`. The month names must be known at compile time and listed in the `IN (...)` clause.

```sql
SELECT *
FROM (
    SELECT
        p.ProductName,
        DATENAME(MONTH, o.OrderDate) AS MonthName,
        od.Quantity * od.UnitPrice   AS SaleAmount
    FROM OrderDetails od
    INNER JOIN Orders o   ON od.OrderId   = o.OrderId
    INNER JOIN Products p ON od.ProductId = p.ProductId
    WHERE YEAR(o.OrderDate) = 2026
) AS SourceData
PIVOT (
    SUM(SaleAmount)
    FOR MonthName IN (
        [January], [February], [March], [April], [May], [June],
        [July], [August], [September], [October], [November], [December]
    )
) AS PivotTable
ORDER BY ProductName;
```

### 11.6 Dynamic Pivot (Unknown Column Values)

When pivot column values aren’t known at compile time, build the query dynamically. `STRING_AGG(QUOTENAME(DeptName), ', ')` constructs the comma-separated column list from the actual data. The full PIVOT query is built as a string and executed with `sp_executesql`. This pattern handles any number of departments without code changes.

```sql
DECLARE @cols   NVARCHAR(MAX);
DECLARE @query  NVARCHAR(MAX);

-- Build column list dynamically
SELECT @cols = STRING_AGG(QUOTENAME(DeptName), ', ')
    WITHIN GROUP (ORDER BY DeptName)
FROM Departments;

-- Build and execute dynamic pivot
SET @query = N'
SELECT *
FROM (
    SELECT d.DeptName, e.Name, e.Salary
    FROM Employees e
    INNER JOIN Departments d ON e.DeptId = d.DeptId
) AS Src
PIVOT (
    SUM(Salary) FOR DeptName IN (' + @cols + N')
) AS Pvt;';

EXEC sp_executesql @query;
```

### 11.7 Recursive Bill of Materials (BOM) with Cost Roll-up

Models a **product assembly tree** recursively. The anchor selects direct children of product 1000. The recursive part drills into sub-components, multiplying quantities at each level (e.g., if product A needs 3 of B, and B needs 2 of C, then A needs 3×2=6 of C). The `Path` column tracks the full component hierarchy for ordering. `REPLICATE` creates visual indentation.

```sql
WITH BOM AS (
    -- Anchor: top-level product
    SELECT
        ParentId     AS RootProduct,
        ChildId,
        Quantity,
        0            AS Level,
        CAST(CONCAT('/', ParentId, '/', ChildId) AS NVARCHAR(MAX)) AS Path
    FROM BillOfMaterials
    WHERE ParentId = 1000  -- Top-level product

    UNION ALL

    -- Recursive: sub-components
    SELECT
        b.RootProduct,
        bm.ChildId,
        b.Quantity * bm.Quantity,  -- Cumulative quantity
        b.Level + 1,
        CONCAT(b.Path, '/', bm.ChildId)
    FROM BOM b
    INNER JOIN BillOfMaterials bm ON b.ChildId = bm.ParentId
)
SELECT
    b.Level,
    REPLICATE('  │  ', b.Level) + p.ProductName  AS Component,
    b.Quantity                                    AS TotalQtyNeeded,
    p.UnitCost,
    b.Quantity * p.UnitCost                       AS TotalCost,
    b.Path
FROM BOM b
INNER JOIN Products p ON b.ChildId = p.ProductId
ORDER BY b.Path
OPTION (MAXRECURSION 50);
```

### 11.8 Median Salary per Department (Without PERCENTILE_CONT)

Calculates the **median** manually. First, number all salaries in ascending order within each department and get the total count. For odd counts (e.g., 5 rows), the median is row `(5+1)/2 = 3`. For even counts (e.g., 4 rows), the median is the average of rows `(4+1)/2 = 2` and `(4+2)/2 = 3`. The `WHERE RowAsc IN (...)` picks exactly the middle row(s), and `AVG` handles both cases.

```sql
WITH Ranked AS (
    SELECT
        DeptId,
        Salary,
        ROW_NUMBER() OVER (PARTITION BY DeptId ORDER BY Salary) AS RowAsc,
        COUNT(*)      OVER (PARTITION BY DeptId)                AS Total
    FROM Employees
)
SELECT
    d.DeptName,
    AVG(r.Salary) AS MedianSalary   -- AVG handles both odd and even counts
FROM Ranked r
INNER JOIN Departments d ON r.DeptId = d.DeptId
WHERE r.RowAsc IN (
    (r.Total + 1) / 2,
    (r.Total + 2) / 2
)
GROUP BY d.DeptName
ORDER BY MedianSalary DESC;
```

### 11.9 Finding Overlapping Date Ranges (Booking Conflicts)

Detects double-bookings for the same room. Two bookings overlap when `A.CheckIn < B.CheckOut AND B.CheckIn < A.CheckOut`. The condition `a.BookingId < b.BookingId` prevents duplicate pairs (A–B and B–A) and self-joins. The CASE expressions calculate the actual overlap period (max of the two start dates to min of the two end dates).

```sql
SELECT
    a.BookingId    AS BookingA,
    b.BookingId    AS BookingB,
    a.RoomId,
    a.CheckIn      AS A_CheckIn,
    a.CheckOut     AS A_CheckOut,
    b.CheckIn      AS B_CheckIn,
    b.CheckOut     AS B_CheckOut,
    -- Overlap period
    CASE WHEN a.CheckIn > b.CheckIn THEN a.CheckIn ELSE b.CheckIn END AS OverlapStart,
    CASE WHEN a.CheckOut < b.CheckOut THEN a.CheckOut ELSE b.CheckOut END AS OverlapEnd
FROM Bookings a
INNER JOIN Bookings b
    ON a.RoomId = b.RoomId
    AND a.BookingId < b.BookingId          -- Avoid duplicates + self-join
    AND a.CheckIn < b.CheckOut             -- Overlap condition
    AND b.CheckIn < a.CheckOut
ORDER BY a.RoomId, OverlapStart;
```

### 11.10 Advanced: Customer Retention & Cohort Analysis

Groups customers into **cohorts** by their first purchase month, then tracks what percentage of each cohort is still active in subsequent months.

- **`FirstPurchase` CTE** — Finds each customer’s first-ever order month (their cohort).
- **`MonthlyActivity` CTE** — For each cohort, counts distinct active customers at each month offset (0 = first month, 1 = second month, etc.).
- **`CohortSize` CTE** — Total customers per cohort for percentage calculation.
- **Final SELECT** — Divides active customers by cohort size to get the retention rate. A healthy business shows high retention rates at later offsets.

```sql
WITH FirstPurchase AS (
    SELECT
        CustomerId,
        MIN(DATEFROMPARTS(YEAR(OrderDate), MONTH(OrderDate), 1)) AS CohortMonth
    FROM Orders
    GROUP BY CustomerId
),
MonthlyActivity AS (
    SELECT
        fp.CohortMonth,
        DATEDIFF(MONTH, fp.CohortMonth,
            DATEFROMPARTS(YEAR(o.OrderDate), MONTH(o.OrderDate), 1)
        ) AS MonthOffset,
        COUNT(DISTINCT o.CustomerId) AS ActiveCustomers
    FROM Orders o
    INNER JOIN FirstPurchase fp ON o.CustomerId = fp.CustomerId
    GROUP BY
        fp.CohortMonth,
        DATEDIFF(MONTH, fp.CohortMonth,
            DATEFROMPARTS(YEAR(o.OrderDate), MONTH(o.OrderDate), 1)
        )
),
CohortSize AS (
    SELECT CohortMonth, COUNT(*) AS TotalCustomers
    FROM FirstPurchase
    GROUP BY CohortMonth
)
SELECT
    FORMAT(ma.CohortMonth, 'yyyy-MM')                                  AS Cohort,
    cs.TotalCustomers                                                  AS CohortSize,
    ma.MonthOffset,
    ma.ActiveCustomers,
    FORMAT(
        CAST(ma.ActiveCustomers AS FLOAT) / cs.TotalCustomers, 'P1'
    )                                                                  AS RetentionRate
FROM MonthlyActivity ma
INNER JOIN CohortSize cs ON ma.CohortMonth = cs.CohortMonth
WHERE ma.MonthOffset <= 12
ORDER BY ma.CohortMonth, ma.MonthOffset;
```

### 11.11 Window Frame: Accumulative Comparison with ROWS vs RANGE

Demonstrates the critical difference between `ROWS` and `RANGE` window frames:
- **`ROWS`** — Treats each row as an individual unit. Running total processes rows one by one, even if dates are identical.
- **`RANGE`** — Groups rows with the **same ORDER BY value** together. If two rows share the same OrderDate, they both get the same running total (including both amounts).

Also shows a **7-day moving average** (current row + 6 preceding) and **day-over-day change** using `LAG`.

```sql
SELECT
    OrderDate,
    Amount,
    -- Running total (all rows from start to current)
    SUM(Amount) OVER (ORDER BY OrderDate
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)   AS RunningTotal_Rows,

    -- RANGE treats ties (same OrderDate) as a group
    SUM(Amount) OVER (ORDER BY OrderDate
        RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)  AS RunningTotal_Range,

    -- 7-day moving average
    AVG(Amount) OVER (ORDER BY OrderDate
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW)           AS MovingAvg7Day,

    -- Difference from previous day
    Amount - LAG(Amount, 1) OVER (ORDER BY OrderDate)       AS DayOverDay
FROM DailySales
ORDER BY OrderDate;
```

---

## Quick Reference Cheatsheet

```
┌─────────────────────────────────────────────────────────────────┐
│  SQL SERVER — QUERY EXECUTION ORDER                             │
├─────────────────────────────────────────────────────────────────┤
│  1. FROM / JOIN          → Sources & joins                      │
│  2. WHERE                → Row-level filter                     │
│  3. GROUP BY             → Grouping                             │
│  4. HAVING               → Group-level filter                   │
│  5. SELECT               → Columns & expressions                │
│  6. DISTINCT             → Remove duplicates                    │
│  7. ORDER BY             → Sorting                              │
│  8. TOP / OFFSET-FETCH   → Limiting rows                        │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  JOIN TYPES                                                     │
├─────────────────────────────────────────────────────────────────┤
│  INNER JOIN        → Matching rows from both tables             │
│  LEFT JOIN         → All from left + matching from right        │
│  RIGHT JOIN        → All from right + matching from left        │
│  FULL OUTER JOIN   → All from both tables                       │
│  CROSS JOIN        → Cartesian product                          │
│  CROSS APPLY       → Like LEFT JOIN with TVF (inner)            │
│  OUTER APPLY       → Like LEFT JOIN with TVF (outer)            │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  SET OPERATIONS                                                 │
├─────────────────────────────────────────────────────────────────┤
│  UNION         → Combine results, remove duplicates             │
│  UNION ALL     → Combine results, keep duplicates               │
│  INTERSECT     → Only rows in both results                      │
│  EXCEPT        → Rows in first but not in second                │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│  COMMON TABLE PATTERNS                                          │
├─────────────────────────────────────────────────────────────────┤
│  CTE               → WITH name AS (SELECT ...)                  │
│  Recursive CTE      → WITH name AS (anchor UNION ALL recursive) │
│  Derived Table      → SELECT ... FROM (SELECT ...) AS alias     │
│  Temp Table         → CREATE TABLE #name (...)                  │
│  Table Variable     → DECLARE @name TABLE (...)                 │
│  CROSS APPLY        → Apply TVF per row                         │
└─────────────────────────────────────────────────────────────────┘
```

---

*Created for [Important-Java-Concepts](https://github.com/Suryakant-Bharti/Important-Java-Concepts) repository.*
