package com.arena.app.scheduling.domain.model;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

public class ScheduledEvent {

    private UUID id;
    private UUID userId;
    private UUID eventId;
    private String arenaArea;
    private List<String> seats;
    private Currency currency;
    private Double pricePerSeat;
    private Double totalDiscount;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private Event event;

    public ScheduledEvent() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public String getArenaArea() { return arenaArea; }
    public void setArenaArea(String arenaArea) { this.arenaArea = arenaArea; }
    public List<String> getSeats() { return seats; }
    public void setSeats(List<String> seats) { this.seats = seats; }
    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }
    public Double getPricePerSeat() { return pricePerSeat; }
    public void setPricePerSeat(Double pricePerSeat) { this.pricePerSeat = pricePerSeat; }
    public Double getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(Double totalDiscount) { this.totalDiscount = totalDiscount; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
}
