package com.arena.app.scheduling.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaScheduledEventRepository extends JpaRepository<ScheduledEventEntity, UUID> {
    
    // In the old code it was findByUserUserId. We mapped it to String in domain, but the DB is UUID. Let's assume it's UUID.
    // The previous implementation was: List<ScheduledEvent> findByUserUserId(String userId); (which mapped to User's String userId).
    // Now that we only have UUID userId, we have a problem. The old controller was passing a String userId.
    // The previous controller did: User authenticatedUser = ...; scheduledEventRepository.findByUserUserId(userId);
    // So it was passing the String userId. Now we are using UUID user_id as foreign key. This needs attention.
    // Wait, if I changed the column to user_id, it refers to the UUID pk of User. So we need findByUserId(UUID userId).
    
    List<ScheduledEventEntity> findByUserId(UUID userId);

    @Query("SELECT COUNT(s) FROM ScheduledEventEntity se JOIN se.seats s WHERE se.eventId = :eventId")
    long countBookedSeatsByEventId(@Param("eventId") UUID eventId);
}
