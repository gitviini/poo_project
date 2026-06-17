package com.arena.app.scheduling.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "visits")
public class VisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false, length = 1000)
    private String description;

    protected VisitEntity() {}

    public VisitEntity(UUID id, LocalDate date, LocalTime time, Integer capacity, String description) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.capacity = capacity;
        this.description = description;
    }

    public UUID getId() { return id; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public Integer getCapacity() { return capacity; }
    public String getDescription() { return description; }
}
