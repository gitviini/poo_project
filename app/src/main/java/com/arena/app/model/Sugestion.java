package com.arena.app.model;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sugestion")
public class Sugestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String description;

    // Anotação para preencher automaticamente na criação
    @CreationTimestamp
    @Column(nullable = false, name = "created_at", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date createdAt;

    // Anotação para atualizar automaticamente sempre que houver uma alteração
    @UpdateTimestamp
    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date updatedAt;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

}
