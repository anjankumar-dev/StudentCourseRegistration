# Student Course Registration System

Console-based Java 17 + JDBC + MySQL project with HikariCP connection pooling.

## Features
- Student registration, listing, search, update, and delete
- Course creation, listing, search, update, and delete
- Student-course enrollment with transaction commit/rollback
- Students enrolled in a course; courses taken by a student
- Student/course report using SQL JOINs
- Stored procedure called with `CallableStatement`
- Batch enrollment with `addBatch()` / `executeBatch()`
- Prepared statements for user-supplied SQL values
- Try-with-resources for JDBC resources

## 1. Requirements
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- MySQL Workbench (optional)

## 2. Create database
Run `database/schema.sql` in MySQL Workbench. The script creates the schema,
tables, stored procedure, and optional sample records.

## 3. Configure credentials
Set environment variables before running:

Windows PowerShell:
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/student_course_db"
$env:DB_USER="your_mysql_user"
$env:DB_PASSWORD="your_mysql_password"
```

Windows CMD:
```cmd
set DB_URL=jdbc:mysql://localhost:3306/student_course_db
set DB_USER=your_mysql_user
set DB_PASSWORD=your_mysql_password
```

Linux/macOS:
```bash
export DB_URL="jdbc:mysql://localhost:3306/student_course_db"
export DB_USER="your_mysql_user"
export DB_PASSWORD="your_mysql_password"
```

If not set, the app defaults to `jdbc:mysql://localhost:3306/student_course_db`,
user `root`, and an empty password. Change these defaults in `DBConnection.java`
if you prefer not to use environment variables. Never commit real credentials.

## 4. Run
From the project directory:
```bash
mvn clean compile
mvn exec:java
```

## 5. Menu overview
1 Register student
2 List students
3 Find student by ID
4 Update student
5 Delete student
6 Add course
7 List courses
8 Update course
9 Delete course
10 Enroll student in course
11 Students in a course
12 Courses for a student
13 Student-course JOIN report
14 Stored procedure: courses for a student
15 Batch enroll one student in multiple courses
0 Exit

## 6. Transaction behavior
Enrollment turns auto-commit off, inserts the enrollment, commits on success,
and rolls back if insertion fails. Database foreign keys and the unique
(student_id, course_id) constraint are the final integrity safeguards.

## 7. Testing checklist
- Register a student with a new email; then try the same email again.
- Search/update/delete with an existing and nonexistent student ID.
- Add a course with positive duration and non-negative fee; try invalid values.
- Enroll a valid student in a valid course; then attempt duplicate enrollment.
- Try enrollment with an invalid student or course ID.
- Run both student/course listing queries and the JOIN report.
- Run the stored procedure option for a student with and without enrollments.
- Batch enroll with a mixture of valid and invalid course IDs; verify rollback.
- Stop MySQL and try an operation to see database connection error handling.

## Screenshots / captured output
Capture your own console screenshots after running the menu and testing each
scenario. No screenshots are fabricated or included in this starter project.
