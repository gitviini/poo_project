package com.arena.app.scheduling.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Visit {

    private UUID id;
    private LocalDate date;
    private LocalTime time;
    private Integer capacity;
    private String description;

    public Visit() {
    }

    public void validateCapacity(int currentBookedCount, int requestedSpots) {
        if (currentBookedCount + requestedSpots > this.capacity) {
            throw new RuntimeException("Desculpe, a capacidade máxima para este horário foi atingida.");
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
