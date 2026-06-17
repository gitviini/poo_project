package com.arena.app.scheduling.domain.repository;

import com.arena.app.scheduling.domain.model.ScheduledEvent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduledEventRepository {
    List<ScheduledEvent> findByUserId(UUID userId);
    long countBookedSeatsByEventId(UUID eventId);
    ScheduledEvent save(ScheduledEvent scheduledEvent);
    Optional<ScheduledEvent> findById(UUID id);
    void delete(ScheduledEvent scheduledEvent);
}
