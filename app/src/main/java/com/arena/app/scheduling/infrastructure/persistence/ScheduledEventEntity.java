package com.arena.app.scheduling.infrastructure.persistence;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "scheduled_events")
public class ScheduledEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(nullable = true)
    private String arenaArea;

    @ElementCollection
    @CollectionTable(name = "scheduled_event_seats", joinColumns = @JoinColumn(name = "scheduled_event_id"))
    @Column(name = "seat")
    private List<String> seats;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = true)
    private Double pricePerSeat;

    @Column(nullable = true)
    private Double totalDiscount;

    @Column(nullable = true)
    private Double totalPrice;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected ScheduledEventEntity() {}

    public ScheduledEventEntity(UUID id, UUID userId, UUID eventId, String arenaArea, List<String> seats, Currency currency, Double pricePerSeat, Double totalDiscount, Double totalPrice, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.arenaArea = arenaArea;
        this.seats = seats;
        this.currency = currency;
        this.pricePerSeat = pricePerSeat;
        this.totalDiscount = totalDiscount;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getEventId() { return eventId; }
    public String getArenaArea() { return arenaArea; }
    public List<String> getSeats() { return seats; }
    public Currency getCurrency() { return currency; }
    public Double getPricePerSeat() { return pricePerSeat; }
    public Double getTotalDiscount() { return totalDiscount; }
    public Double getTotalPrice() { return totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
