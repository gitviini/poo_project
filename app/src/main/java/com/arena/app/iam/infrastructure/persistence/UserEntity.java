package com.arena.app.iam.infrastructure.persistence;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = true, length = 25)
    private String userId;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = true)
    private String bio;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = true)
    private String imageBase64;

    protected UserEntity() {}

    public UserEntity(UUID id, String name, String userId, String email, String phone, String password, String role, String bio, String imageBase64) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.bio = bio;
        this.imageBase64 = imageBase64;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getBio() { return bio; }
    public String getImageBase64() { return imageBase64; }

    public void setName(String name) { this.name = name; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setBio(String bio) { this.bio = bio; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
