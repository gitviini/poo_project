package com.arena.app.notification.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {
    
    private final UUID id;
    private final UUID userId;
    private final String message;
    private boolean isRead;
    private final LocalDateTime createdAt;

    public Notification(UUID id, UUID userId, String message, boolean isRead, LocalDateTime createdAt) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("A mensagem da notificação não pode ser vazia.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("O ID do usuário é obrigatório.");
        }
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public static Notification createNew(UUID userId, String message) {
        return new Notification(
            null, 
            userId, 
            message, 
            false,
            LocalDateTime.now()
        );
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
