package com.arena.app.model;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "scheduled_events")
public class ScheduledEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

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

    public ScheduledEvent() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.event != null && this.currency == null) {
            this.currency = this.event.getCurrency();
        }
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getArenaArea() {
        return arenaArea;
    }

    public void setArenaArea(String arenaArea) {
        this.arenaArea = arenaArea;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(Double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
