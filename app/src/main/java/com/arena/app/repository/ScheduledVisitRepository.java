package com.arena.app.repository;

import com.arena.app.model.ScheduledVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface ScheduledVisitRepository extends JpaRepository<ScheduledVisit, UUID> {
    List<ScheduledVisit> findByUserUserId(String userId);
}
