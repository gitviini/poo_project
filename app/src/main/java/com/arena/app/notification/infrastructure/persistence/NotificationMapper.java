package com.arena.app.notification.infrastructure.persistence;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;

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
