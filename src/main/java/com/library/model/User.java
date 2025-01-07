package com.library.model;

/**
 * Represents a user in the library management system.
 * Includes attributes such as name, email, and role.
 */
public class User {
    private int id; // Unique identifier for the user (auto-generated by the database)
    private String name; // Name of the user
    private String email; // Email address of the user
    private String role; // Role of the user (e.g., USER, ADMIN)

    /**
     * Default constructor for creating an empty User object.
     */
    public User() {
    }

    /**
     * Parameterized constructor for creating a User with specified attributes.
     *
     * @param name  the name of the user (must not be null or empty)
     * @param email the email address of the user (must be valid and not null)
     * @param role  the role of the user (must be either USER or ADMIN)
     */
    public User(String name, String email, String role) {
        this.setName(name); // Validates and sets the name
        this.setEmail(email); // Validates and sets the email
        this.setRole(role); // Validates and sets the role
    }

    /**
     * Parameterized constructor for creating a User with all attributes.
     *
     * @param id    the unique identifier of the user
     * @param name  the name of the user (must not be null or empty)
     * @param email the email address of the user (must be valid and not null)
     * @param role  the role of the user (must be either USER or ADMIN)
     */
    public User(int id, String name, String email, String role) {
        this.setId(id); // Validates and sets the ID
        this.setName(name); // Validates and sets the name
        this.setEmail(email); // Validates and sets the email
        this.setRole(role); // Validates and sets the role
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return the user ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the user ID (must be non-negative)
     * @throws IllegalArgumentException if the ID is negative
     */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative.");
        }
        this.id = id;
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name of the user (must not be null or empty)
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address of the user (must be valid and not null)
     * @throws IllegalArgumentException if the email is null or invalid
     */
    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        this.email = email;
    }

    /**
     * Gets the role of the user.
     *
     * @return the role of the user
     */
    public String getRole() {
        return this.role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the role of the user (must be either USER or ADMIN)
     * @throws IllegalArgumentException if the role is null or invalid
     */
    public void setRole(String role) {
        if (role == null) {
            this.role = "USER";
        } else if (!role.equalsIgnoreCase("USER") && !role.equalsIgnoreCase("ADMIN")) {
            throw new IllegalArgumentException("Role must be either 'USER' or 'ADMIN'.");
        } else {
            this.role = role.toUpperCase();
        }
    }

    /**
     * Provides a string representation of the user.
     *
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
