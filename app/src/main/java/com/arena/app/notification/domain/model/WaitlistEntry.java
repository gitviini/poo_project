package com.arena.app.notification.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class WaitlistEntry {

    private final UUID id;
    private final UUID userId;
    private final UUID eventId;
    private final UUID visitId;
    private final LocalDateTime createdAt;

    public WaitlistEntry(UUID id, UUID userId, UUID eventId, UUID visitId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.visitId = visitId;
        this.createdAt = createdAt;
    }

    public static WaitlistEntry forEvent(UUID userId, UUID eventId) {
        return new WaitlistEntry(null, userId, eventId, null, LocalDateTime.now());
    }

    public static WaitlistEntry forVisit(UUID userId, UUID visitId) {
        return new WaitlistEntry(null, userId, null, visitId, LocalDateTime.now());
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getEventId() { return eventId; }
    public UUID getVisitId() { return visitId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
