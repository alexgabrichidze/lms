-- Seed data 

-- Insert sample books
INSERT INTO books (title, author, isbn, published_date, status) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', '1925-04-10', 'AVAILABLE'),
('1984', 'George Orwell', '9780451524935', '1949-06-08', 'AVAILABLE'),
('To Kill a Mockingbird', 'Harper Lee', '9780060935467', '1960-07-11', 'BORROWED');

-- Insert sample users
INSERT INTO users (name, email, role) VALUES
('Alice Johnson', 'alice@example.com', 'USER'),
('Bob Smith', 'bob@example.com', 'ADMIN'),
('Charlie Brown', 'charlie@example.com', 'USER');

-- Insert sample loans
INSERT INTO loans (user_id, book_id, loan_date, return_date) VALUES
(1, 3, '2025-01-01', '2027-04-25'), -- Alice borrowed 'To Kill a Mockingbird'
(2, 1, '2024-12-15', '2026-12-22'); -- Bob borrowed 'The Great Gatsby' and returned it
