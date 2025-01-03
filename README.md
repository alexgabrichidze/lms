# Library management system

## Project description

The **library management system** is a backend application designed to manage books, users, and loan transactions for a library. It provides REST API endpoints to perform CRUD operations for books, users, and loans while ensuring role-based access control for administrative tasks. This system is developed in **pure Java** using minimal libraries and a PostgreSQL database, with Maven as the build tool. This project focuses on creating a foundational system for managing library operations efficiently, serving as a platform for learning backend development.

---

## Minimum viable product 

### Core functionalities:

1. **Books management**

    - Add a new book (title, author, ISBN, availability).
    - Update book details.
    - Delete a book.
    - Retrieve a list of all books or search for books by title, author, or ISBN.

2. **User management**

    - Register a user (name, email, role: `USER` or `ADMIN`).
    - Fetch a list of all users.
    - Delete a user (admin-only).

3. **Loan management**

    - Issue a book to a user (ensure the book is available).
    - Mark a book as returned.
    - View all active loans (admin-only).

4. **Authorization**

    - Role-based access control:
        - Admin can manage books, users, and loans.
        - Regular users can only borrow and return books.

5. **Logging**

    - Log all operations (e.g., book added, user registered, loan issued).
    - Include timestamps and request details.

6. **Error handling**
    - Gracefully handle errors such as invalid input, unavailable books, or unauthorized actions.
    - Return meaningful HTTP status codes (e.g., `404` for not found, `403` for unauthorized).

---

## Technologies and libraries

### Core technology

-   **Java** (JDK 21): core programming language.

### Libraries and tools

-   **Maven**: build automation and dependency management.
-   **PostgreSQL**: relational database for storing library data.
-   **JUnit 5**: unit testing framework.
-   **SLF4J + Logback**: logging framework.
-   **Jackson Databind**: JSON serialization and deserialization.
-   **Java HTTP Server**: to handle raw HTTP requests and responses.

### Database

-   **PostgreSQL** schema includes the following tables:
    -   `books`: stores book details (title, author, ISBN, availability).
    -   `users`: stores user information (name, email, role).
    -   `loans`: tracks book loans (user, book, due date).

### Build and packaging

-   **Maven** plugins for compilation, packaging, and testing:
    -   **Maven Compiler Plugin**
    -   **Maven Surefire Plugin**
    -   **Maven JAR Plugin**