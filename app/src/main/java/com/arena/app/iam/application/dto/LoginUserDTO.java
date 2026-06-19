package com.arena.app.iam.application.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginUserDTO {

    @NotBlank(message = "Informe o ID de usuário.")
    private String userId;

    @NotBlank(message = "Informe a senha.")
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
