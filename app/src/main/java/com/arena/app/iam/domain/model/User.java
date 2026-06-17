package com.arena.app.iam.domain.model;

import com.arena.app.iam.application.dto.SignupUserDTO;
import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String userId;
    private String email;
    private String phone;
    private String password;
    private String role; // "user" or "admin"
    private String bio;
    private String imageBase64;

    public User() {
    }

    public User(SignupUserDTO newUser) {
        this.userId = newUser.getUserId();
        this.name = newUser.getName();
        this.email = newUser.getEmail();
        this.phone = newUser.getPhone();
        this.password = newUser.getPassword();
        this.role = "user"; // Default role
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
