package com.arena.app.notification.domain.repository;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;

import com.arena.app.notification.domain.model.Notification;
 
import java.util.List;

public interface NotificationRepository {
    void save(Notification notification);
    java.util.Optional<Notification> findById(UUID id);
    List<Notification> findByUserId(UUID userId);
    long countUnreadByUserId(UUID userId);
    void delete(Notification notification);
}
