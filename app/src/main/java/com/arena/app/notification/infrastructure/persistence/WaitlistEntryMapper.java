package com.arena.app.notification.infrastructure.persistence;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;

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
