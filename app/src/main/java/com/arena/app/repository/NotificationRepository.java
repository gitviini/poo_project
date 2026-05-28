package com.arena.app.repository;

import com.arena.app.model.Notification;
import com.arena.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    long countByUserAndIsReadFalse(User user);
}
