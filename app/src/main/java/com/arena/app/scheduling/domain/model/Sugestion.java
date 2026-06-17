package com.arena.app.scheduling.domain.model;

import java.util.Date;
import java.util.UUID;

public class Sugestion {
    private UUID id;
    private String title;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    public Sugestion() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
