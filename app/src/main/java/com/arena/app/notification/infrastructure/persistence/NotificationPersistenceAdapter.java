package com.arena.app.notification.infrastructure.persistence;

import com.arena.app.notification.domain.model.Notification;
import com.arena.app.notification.domain.repository.NotificationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class NotificationPersistenceAdapter implements NotificationRepository {

    private final JpaNotificationRepository jpaRepository;

    public NotificationPersistenceAdapter(JpaNotificationRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Notification notification) {
        jpaRepository.save(NotificationMapper.toEntity(notification));
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return jpaRepository.findById(id).map(NotificationMapper::toDomain);
    }

    @Override
    public List<Notification> findByUserId(UUID userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countUnreadByUserId(UUID userId) {
        return jpaRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    public void delete(Notification notification) {
        jpaRepository.deleteById(notification.getId());
    }
}
