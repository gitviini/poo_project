package com.arena.app.notification.domain.repository;

import com.arena.app.notification.domain.model.Notification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    void save(Notification notification);
    Optional<Notification> findById(UUID id);
    List<Notification> findByUserId(UUID userId);
    long countUnreadByUserId(UUID userId);
    void delete(Notification notification);
}
