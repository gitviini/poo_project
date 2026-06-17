package com.arena.app.scheduling.domain.repository;

import com.arena.app.scheduling.domain.model.Event;
import java.util.Optional;
import java.util.List;
import java.util.Date;
import java.util.UUID;

public interface EventRepository {
    Optional<Event> findByTitle(String title);
    List<Event> findByDate(Date date);
    List<Event> findAll();
    Optional<Event> findById(UUID id);
    Event save(Event event);
    void deleteById(UUID id);
}
