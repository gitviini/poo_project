package com.arena.app.notification.infrastructure.persistence;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;

import com.arena.app.notification.domain.model.WaitlistEntry;
import com.arena.app.notification.domain.repository.WaitlistEntryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
 
import java.util.stream.Collectors;

@Repository
public class WaitlistEntryPersistenceAdapter implements WaitlistEntryRepository {

    private final JpaWaitlistEntryRepository jpaRepository;

    public WaitlistEntryPersistenceAdapter(JpaWaitlistEntryRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(WaitlistEntry entry) {
        jpaRepository.save(WaitlistEntryMapper.toEntity(entry));
    }

    @Override
    public void delete(WaitlistEntry entry) {
        jpaRepository.deleteById(entry.getId());
    }

    @Override
    public List<WaitlistEntry> findByEventId(UUID eventId) {
        return jpaRepository.findByEventIdOrderByCreatedAtAsc(eventId).stream()
                .map(WaitlistEntryMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<WaitlistEntry> findByVisitId(UUID visitId) {
        return jpaRepository.findByVisitIdOrderByCreatedAtAsc(visitId).stream()
                .map(WaitlistEntryMapper::toDomain)
                .collect(Collectors.toList());
    }
}
