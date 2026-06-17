package com.arena.app.scheduling.domain.repository;

import com.arena.app.scheduling.domain.model.ScheduledVisit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduledVisitRepository {
    List<ScheduledVisit> findByUserId(UUID userId);
    Optional<ScheduledVisit> findByUserIdAndVisitId(UUID userId, UUID visitId);
    Integer countBookedPeopleByVisitId(UUID visitId);
    ScheduledVisit save(ScheduledVisit scheduledVisit);
    Optional<ScheduledVisit> findById(UUID id);
    void delete(ScheduledVisit scheduledVisit);
}
