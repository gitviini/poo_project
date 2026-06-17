package com.arena.app.scheduling.infrastructure.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scheduled_visits")
public class ScheduledVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "visit_id", nullable = false)
    private UUID visitId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "visit_id", insertable = false, updatable = false)
    private VisitEntity visit;

    @Column(nullable = false)
    private Integer numberOfPeople;

    @Column(nullable = false)
    private String status = "CONFIRMADO";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected ScheduledVisitEntity() {}

    public ScheduledVisitEntity(UUID id, UUID userId, UUID visitId, Integer numberOfPeople, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.visitId = visitId;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getVisitId() { return visitId; }
    public VisitEntity getVisit() { return visit; }
    public Integer getNumberOfPeople() { return numberOfPeople; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
