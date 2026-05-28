package com.arena.app.model;

import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    Currency currency;

    @Column(nullable = false)
    Double price;

    @Column(nullable = true)
    private Integer capacity; // Novo campo para limite de ingressos

    @Column(nullable = true)
    String category; // Novo campo da História de Usuário 1

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = true)
    private String imageBase64;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledEvent> scheduledEvents;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WaitlistEntry> waitlistEntries;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public String getCategory() {
        return category;
    }
}
