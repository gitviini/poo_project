package com.arena.app.scheduling.infrastructure.persistence;

import jakarta.persistence.*;
import java.util.Currency;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = true)
    private Integer capacity;

    @Column(nullable = true)
    private String category;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = true)
    private String imageBase64;

    protected EventEntity() {}

    public EventEntity(UUID id, String title, Date date, String description, Currency currency, Double price, Integer capacity, String category, String imageBase64) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
        this.currency = currency;
        this.price = price;
        this.capacity = capacity;
        this.category = category;
        this.imageBase64 = imageBase64;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public Date getDate() { return date; }
    public String getDescription() { return description; }
    public Currency getCurrency() { return currency; }
    public Double getPrice() { return price; }
    public Integer getCapacity() { return capacity; }
    public String getCategory() { return category; }
    public String getImageBase64() { return imageBase64; }
}
