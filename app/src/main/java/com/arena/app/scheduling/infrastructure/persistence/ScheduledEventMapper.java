package com.arena.app.scheduling.infrastructure.persistence;

import com.arena.app.scheduling.domain.model.ScheduledEvent;

public class ScheduledEventMapper {

    public static ScheduledEventEntity toEntity(ScheduledEvent domain) {
        return new ScheduledEventEntity(
            domain.getId(),
            domain.getUserId(),
            domain.getEventId(),
            domain.getArenaArea(),
            domain.getSeats(),
            domain.getCurrency(),
            domain.getPricePerSeat(),
            domain.getTotalDiscount(),
            domain.getTotalPrice(),
            domain.getCreatedAt()
        );
    }

    public static ScheduledEvent toDomain(ScheduledEventEntity entity) {
        ScheduledEvent domain = new ScheduledEvent();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setEventId(entity.getEventId());
        domain.setArenaArea(entity.getArenaArea());
        domain.setSeats(entity.getSeats());
        domain.setCurrency(entity.getCurrency());
        domain.setPricePerSeat(entity.getPricePerSeat());
        domain.setTotalDiscount(entity.getTotalDiscount());
        domain.setTotalPrice(entity.getTotalPrice());
        domain.setCreatedAt(entity.getCreatedAt());
        return domain;
    }
}
