package com.arena.app.scheduling.infrastructure.persistence;

import com.arena.app.scheduling.domain.model.ScheduledEvent;
import com.arena.app.scheduling.domain.repository.ScheduledEventRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ScheduledEventPersistenceAdapter implements ScheduledEventRepository {

    private final JpaScheduledEventRepository jpaRepository;

    public ScheduledEventPersistenceAdapter(JpaScheduledEventRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<ScheduledEvent> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(ScheduledEventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countBookedSeatsByEventId(UUID eventId) {
        return jpaRepository.countBookedSeatsByEventId(eventId);
    }

    @Override
    public ScheduledEvent save(ScheduledEvent scheduledEvent) {
        ScheduledEventEntity entity = ScheduledEventMapper.toEntity(scheduledEvent);
        ScheduledEventEntity saved = jpaRepository.save(entity);
        return ScheduledEventMapper.toDomain(saved);
    }

    @Override
    public Optional<ScheduledEvent> findById(UUID id) {
        return jpaRepository.findById(id).map(ScheduledEventMapper::toDomain);
    }

    @Override
    public void delete(ScheduledEvent scheduledEvent) {
        jpaRepository.deleteById(scheduledEvent.getId());
    }
}
