package com.janani.contentrecommendation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity //Marks the class as JPA Entity
@Table(name = "users")//explicitly specifies the table name
public class User {

    @Id //makes the userID as Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)//It is used to mention auto-incrementation of the id
    private Long userId;

    @Column(nullable = false)//It mention that this column cannot be null in DB
    private String name;

    @Column(unique = true, nullable = false)//It mention that this column should be unique and not null
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)//saves enum values Admin,user and curator as strings in db
    @Column(nullable = false)
    private Role role = Role.USER; // default role

    @Column(columnDefinition = "JSON")//It stores the categories and tags as JSON in DB
    private String preferredCategories;

    @Column(columnDefinition = "JSON")
    private String preferredTags;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();// Stores When the user was created

    // Constructors
    public User() {}
    /*
    JPA (Java Persistence API) requires every entity to have a no‑argument constructor.
    Hibernate uses reflection to create entity objects when it loads rows from the database.
    Without a default constructor, Hibernate wouldn’t know how to instantiate your User class.
     */

    public User(String name, String email, String passwordHash, Role role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getPreferredCategories() { return preferredCategories; }
    public void setPreferredCategories(String preferredCategories) { this.preferredCategories = preferredCategories; }

    public String getPreferredTags() { return preferredTags; }
    public void setPreferredTags(String preferredTags) { this.preferredTags = preferredTags; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
