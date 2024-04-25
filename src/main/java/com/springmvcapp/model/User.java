package com.springmvcapp.model;
import javax.persistence.Table;
import javax.persistence.Column;
import jakarta.persistence.*;
@Entity
@Table(name="logincredential")
public class User {
    @Id  // This annotation marks 'username' as the primary key.
    @Column(name="username", nullable=false, unique=true)  // Ensures the username column is created.
    private String username;

    @Column(name="password", nullable=false)  // Ensures the password column is created.
    private String password;

    // Default constructor is required for JPA
    public User() {}

    // Constructor with parameters
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters for username and password
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
