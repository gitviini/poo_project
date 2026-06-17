package com.arena.app.iam.application.dto;

public class LoginUserDTO {
    private String userId;
    private String password;

    public LoginUserDTO() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
