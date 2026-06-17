package com.arena.app.scheduling.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ScheduledVisit {

    private UUID id;
    private UUID userId;
    private UUID visitId;
    private Visit visit;
    private Integer numberOfPeople;
    private String status = "CONFIRMADO";
    private LocalDateTime createdAt;

    public ScheduledVisit() {
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getVisitId() { return visitId; }
    public void setVisitId(UUID visitId) { this.visitId = visitId; }
    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }
    public Integer getNumberOfPeople() { return numberOfPeople; }
    public void setNumberOfPeople(Integer numberOfPeople) { this.numberOfPeople = numberOfPeople; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
