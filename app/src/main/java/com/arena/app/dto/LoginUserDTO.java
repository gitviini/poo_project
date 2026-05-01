package com.arena.app.dto;

import jakarta.persistence.Column;

public class LoginUserDTO {
    @Column(nullable = false)
    String userId;
    @Column(nullable = false)
    String password;

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
}
