package com.arena.app.iam.application.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignupUserDTO {

    @NotBlank(message = "O nome é obrigatório.")
    private String name;

    @NotBlank(message = "O ID de usuário é obrigatório.")
    @Size(max = 25, message = "O ID de usuário deve ter no máximo 25 caracteres.")
    private String userId;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    private String phone;

    // Força mínima de senha: 8+ caracteres, com pelo menos uma letra e um número.
    @NotBlank(message = "A senha é obrigatória.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
        message = "A senha deve ter no mínimo 8 caracteres, incluindo letras e números."
    )
    private String password;

    // Consentimento LGPD: o cadastro só é aceito se o usuário marcar a caixa.
    @AssertTrue(message = "É necessário aceitar a Política de Privacidade para se cadastrar.")
    private boolean aceitePrivacidade;

    public SignupUserDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isAceitePrivacidade() {
        return aceitePrivacidade;
    }

    public void setAceitePrivacidade(boolean aceitePrivacidade) {
        this.aceitePrivacidade = aceitePrivacidade;
    }
}
