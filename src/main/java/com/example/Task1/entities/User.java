package com.example.Task1.entities;
import jakarta.persistence.*;
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String roles;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRoles() { return roles; }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
