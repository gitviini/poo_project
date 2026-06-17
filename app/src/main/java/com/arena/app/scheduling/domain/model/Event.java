package com.arena.app.scheduling.domain.model;

import java.util.Currency;
import java.util.Date;
import java.util.UUID;

public class Event {
    private UUID id;
    private String title;
    private Date date;
    private String description;
    private Currency currency;
    private Double price;
    private Integer capacity;
    private String category;
    private String imageBase64;

    public Event() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
