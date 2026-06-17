package com.arena.app.scheduling.infrastructure.persistence;

import com.arena.app.scheduling.domain.model.Event;
import com.arena.app.scheduling.domain.repository.EventRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class EventPersistenceAdapter implements EventRepository {

    private final JpaEventRepository jpaRepository;

    public EventPersistenceAdapter(JpaEventRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Event> findByTitle(String title) {
        return jpaRepository.findByTitle(title).map(EventMapper::toDomain);
    }

    @Override
    public List<Event> findByDate(Date date) {
        return jpaRepository.findByDate(date).stream()
                .map(EventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findAll() {
        return jpaRepository.findAll().stream()
                .map(EventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return jpaRepository.findById(id).map(EventMapper::toDomain);
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = EventMapper.toEntity(event);
        EventEntity saved = jpaRepository.save(entity);
        return EventMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
