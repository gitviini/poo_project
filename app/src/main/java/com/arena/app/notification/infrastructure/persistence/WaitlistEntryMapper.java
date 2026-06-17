package com.arena.app.notification.infrastructure.persistence;

import com.arena.app.notification.domain.model.WaitlistEntry;

public class WaitlistEntryMapper {

    public static WaitlistEntryEntity toEntity(WaitlistEntry domain) {
        return new WaitlistEntryEntity(
            domain.getId(),
            domain.getUserId(),
            domain.getEventId(),
            domain.getVisitId(),
            domain.getCreatedAt()
        );
    }

    public static WaitlistEntry toDomain(WaitlistEntryEntity entity) {
        return new WaitlistEntry(
            entity.getId(),
            entity.getUserId(),
            entity.getEventId(),
            entity.getVisitId(),
            entity.getCreatedAt()
        );
    }
}
