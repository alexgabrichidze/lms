## API Endpoint Mapping

### Books

| **HTTP Method** | **Endpoint**             | **Description**                           |
| --------------- | ------------------------ | ----------------------------------------- |
| `GET`           | `/books`                 | Get all books.                            |
| `GET`           | `/books/{id}`            | Get a book by ID.                         |
| `POST`          | `/books`                 | Add a new book.                           |
| `PATCH`         | `/books/{id}`            | Update a book by ID.                      |
| `DELETE`        | `/books/{id}`            | Delete a book by ID.                      |
| `GET`           | `/books?title={title}`   | Search books by title.                    |
| `GET`           | `/books?author={author}` | Search books by author.                   |
| `GET`           | `/books?isbn={isbn}`     | Get a book by ISBN.                       |
| `PATCH`         | `/books/{id}/status`     | Update a book's status (e.g., AVAILABLE). |

---

### Users

| **HTTP Method** | **Endpoint**           | **Description**      |
| --------------- | ---------------------- | -------------------- |
| `GET`           | `/users`               | Get all users.       |
| `GET`           | `/users/{id}`          | Get a user by ID.    |
| `POST`          | `/users`               | Create a new user.   |
| `PATCH`         | `/users/{id}`          | Update a user by ID. |
| `DELETE`        | `/users/{id}`          | Delete a user by ID. |
| `GET`           | `/users?email={email}` | Get a user by email. |

---

### Loans

| **HTTP Method** | **Endpoint**             | **Description**       |
| --------------- | ------------------------ | --------------------- |
| `GET`           | `/loans`                 | Get all loans.        |
| `GET`           | `/loans/{id}`            | Get a loan by ID.     |
| `POST`          | `/loans`                 | Create a new loan.    |
| `PATCH`         | `/loans/{id}`            | Update a loan by ID.  |
| `DELETE`        | `/loans/{id}`            | Delete a loan by ID.  |
| `GET`           | `/loans?userId={userId}` | Get loans by user ID. |
| `GET`           | `/loans/active`          | Get all active loans. |

---

### Example Requests

#### Books

-   `GET /books` → Get all books.
-   `GET /books/1` → Get book with ID 1.
-   `POST /books` → Add a new book.
-   `PUT /books/1` → Update book with ID 1.
-   `DELETE /books/1` → Delete book with ID 1.
-   `GET /books?title=1984` → Search books by title.
-   `GET /books?author=George+Orwell` → Search books by author.
-   `GET /books?isbn=1234567890123` → Get book by ISBN.
-   `PUT /books/1/status` → Update book status (e.g., AVAILABLE, BORROWED).

#### Users

-   `GET /users` → Get all users.
-   `GET /users/1` → Get user with ID 1.
-   `POST /users` → Add a new user.
-   `PUT /users/1` → Update user with ID 1.
-   `DELETE /users/1` → Delete user with ID 1.
-   `GET /users?email=john@example.com` → Get user by email.

#### Loans

-   `GET /loans` → Get all loans.
-   `GET /loans/1` → Get loan with ID 1.
-   `POST /loans` → Add a new loan.
-   `PUT /loans/1` → Update loan with ID 1.
-   `DELETE /loans/1` → Delete loan with ID 1.
-   `GET /loans?userId=1` → Get loans by user ID.
-   `GET /loans?bookId=1` → Get loans by book ID.
-   `GET /loans/active` → Get all active loans.
