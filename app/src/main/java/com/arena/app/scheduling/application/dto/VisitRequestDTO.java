package com.arena.app.scheduling.application.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class VisitRequestDTO {
    private UUID id;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime time;
    
    private Integer capacity;
    private String description;

    public VisitRequestDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
