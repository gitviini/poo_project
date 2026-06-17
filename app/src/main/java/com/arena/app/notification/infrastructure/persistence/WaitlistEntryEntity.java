package com.arena.app.notification.infrastructure.persistence;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;

import jakarta.persistence.*;
import java.time.LocalDateTime;
 

@Entity
@Table(name = "waitlist_entries")
public class WaitlistEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "visit_id")
    private UUID visitId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected WaitlistEntryEntity() {}

    public WaitlistEntryEntity(UUID id, UUID userId, UUID eventId, UUID visitId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.visitId = visitId;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getEventId() { return eventId; }
    public UUID getVisitId() { return visitId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
