package com.arena.app.model;

import java.util.Currency;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    Date date;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    Currency currency;

    @Column(nullable = false)
    Double price;

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
}
