package com.arena.app.dto;

import jakarta.persistence.Column;

public class LoginUserDTO {
    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    String password;

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
