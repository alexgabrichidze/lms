# Library Management System (LMS)

## Project Description

The **Library Management System (LMS)** is a backend application designed to manage books, users, and loan transactions for a library. It provides REST API endpoints to perform CRUD operations for books, users, and loans while ensuring role-based access control for administrative tasks. The LMS is developed in **pure Java** using minimal libraries and a PostgreSQL database, with Maven as the build tool. This project focuses on creating a foundational system for managing library operations efficiently, serving as a platform for learning backend development.

---

## Minimum Viable Product (MVP)

### Core Functionalities:

1. **Books Management**

    - Add a new book (title, author, ISBN, availability).
    - Update book details.
    - Delete a book.
    - Retrieve a list of all books or search for books by title, author, or ISBN.

2. **User Management**

    - Register a user (name, email, role: `USER` or `ADMIN`).
    - Fetch a list of all users.
    - Delete a user (admin-only).

3. **Loan Management**

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

6. **Error Handling**
    - Gracefully handle errors such as invalid input, unavailable books, or unauthorized actions.
    - Return meaningful HTTP status codes (e.g., `404` for not found, `403` for unauthorized).

---

## Technologies and Libraries

### Core Technology

-   **Java** (JDK 21): Core programming language.

### Libraries and Tools

-   **Maven**: Build automation and dependency management.
-   **PostgreSQL**: Relational database for storing library data.
-   **JUnit 5**: Unit testing framework.
-   **SLF4J + Logback**: Logging framework.
-   **Jackson Databind**: JSON serialization and deserialization.
-   **Java HTTP Server (Built-in)**: To handle raw HTTP requests and responses.

### Database

-   PostgreSQL schema includes the following tables:
    -   `books`: Stores book details (title, author, ISBN, availability).
    -   `users`: Stores user information (name, email, role).
    -   `loans`: Tracks book loans (user, book, due date).

### Build and Packaging

-   Maven plugins for compilation, packaging, and testing:
    -   **Maven Compiler Plugin**
    -   **Maven Surefire Plugin**
    -   **Maven JAR Plugin**