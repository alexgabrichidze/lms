package com.library.model;

/**
 * Represents a user in the library management system.
 * Includes attributes such as name, email, and role.
 */
public class User {
    private int id; // Unique identifier for the user
    private String name; // Name of the user
    private String email; // Email address of the user
    private UserRole role; // Role of the user (e.g., USER, ADMIN)

    /**
     * Default constructor for creating an empty User object.
     */
    public User() {
    }

    /**
     * Parameterized constructor for creating a User with specified attributes.
     *
     * @param name  the name of the user
     * @param email the email address of the user
     * @param role  the role of the user (default is USER if null)
     */
    public User(String name, String email, UserRole role) {
        this.name = name;
        this.email = email;
        this.role = role; // Default role is USER
    }

    /**
     * Parameterized constructor for creating a User with an ID.
     *
     * @param id    the unique ID of the user
     * @param name  the name of the user
     * @param email the email address of the user
     * @param role  the role of the user (default is USER if null)
     */
    public User(int id, String name, String email, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets the unique ID of the user.
     *
     * @return the user's ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the unique ID of the user.
     *
     * @param id the user's ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the user.
     *
     * @return the user's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the user's email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the role of the user.
     *
     * @return the user's role
     */
    public UserRole getRole() {
        return this.role;
    }

    /**
     * Sets the role of the user. If the role is null, it defaults to USER.
     *
     * @param role the user's role
     */
    public void setRole(UserRole role) {
        this.role = role;
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
                ", role=" + role +
                '}';
    }
}
