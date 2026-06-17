package com.arena.app.notification.infrastructure.persistence;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;

import jakarta.persistence.*;
import java.time.LocalDateTime;
 

@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private boolean isRead;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected NotificationEntity() {}

    public NotificationEntity(UUID id, UUID userId, String message, boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
