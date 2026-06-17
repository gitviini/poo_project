package com.arena.app.notification.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface JpaWaitlistEntryRepository extends JpaRepository<WaitlistEntryEntity, UUID> {
    List<WaitlistEntryEntity> findByEventIdOrderByCreatedAtAsc(UUID eventId);
    List<WaitlistEntryEntity> findByVisitIdOrderByCreatedAtAsc(UUID visitId);
}
