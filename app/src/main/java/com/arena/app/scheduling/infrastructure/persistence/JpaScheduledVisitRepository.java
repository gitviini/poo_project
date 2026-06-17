package com.arena.app.scheduling.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaScheduledVisitRepository extends JpaRepository<ScheduledVisitEntity, UUID> {
    
    List<ScheduledVisitEntity> findByUserId(UUID userId);

    Optional<ScheduledVisitEntity> findByUserIdAndVisitId(UUID userId, UUID visitId);

    @Query("SELECT SUM(sv.numberOfPeople) FROM ScheduledVisitEntity sv WHERE sv.visitId = :visitId")
    Integer countBookedPeopleByVisitId(@Param("visitId") UUID visitId);
}
