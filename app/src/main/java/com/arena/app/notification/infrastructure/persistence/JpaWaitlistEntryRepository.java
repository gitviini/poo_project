package com.arena.app.notification.infrastructure.persistence;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
 
import java.util.List;

public interface JpaWaitlistEntryRepository extends JpaRepository<WaitlistEntryEntity, UUID> {
    List<WaitlistEntryEntity> findByEventIdOrderByCreatedAtAsc(UUID eventId);
    List<WaitlistEntryEntity> findByVisitIdOrderByCreatedAtAsc(UUID visitId);
}
