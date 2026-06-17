package com.arena.app.notification.infrastructure.persistence;

import com.arena.app.notification.domain.model.Notification;

public class NotificationMapper {

    public static NotificationEntity toEntity(Notification domain) {
        return new NotificationEntity(
            domain.getId(),
            domain.getUserId(),
            domain.getMessage(),
            domain.isRead(),
            domain.getCreatedAt()
        );
    }

    public static Notification toDomain(NotificationEntity entity) {
        return new Notification(
            entity.getId(),
            entity.getUserId(),
            entity.getMessage(),
            entity.isRead(),
            entity.getCreatedAt()
        );
    }
}
