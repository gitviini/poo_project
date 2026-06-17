package com.arena.app.scheduling.infrastructure.persistence;

import com.arena.app.scheduling.domain.model.Event;

public class EventMapper {

    public static EventEntity toEntity(Event domain) {
        return new EventEntity(
            domain.getId(),
            domain.getTitle(),
            domain.getDate(),
            domain.getDescription(),
            domain.getCurrency(),
            domain.getPrice(),
            domain.getCapacity(),
            domain.getCategory(),
            domain.getImageBase64()
        );
    }

    public static Event toDomain(EventEntity entity) {
        Event domain = new Event();
        domain.setId(entity.getId());
        domain.setTitle(entity.getTitle());
        domain.setDate(entity.getDate());
        domain.setDescription(entity.getDescription());
        domain.setCurrency(entity.getCurrency());
        domain.setPrice(entity.getPrice());
        domain.setCapacity(entity.getCapacity());
        domain.setCategory(entity.getCategory());
        domain.setImageBase64(entity.getImageBase64());
        return domain;
    }
}
