package com.arena.app.repository;

import com.arena.app.model.ScheduledVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.arena.app.model.Visit;

import java.util.UUID;
import java.util.List;

@Repository
public interface ScheduledVisitRepository extends JpaRepository<ScheduledVisit, UUID> {
    List<ScheduledVisit> findByUserUserId(String userId);

    @Query("SELECT SUM(sv.numberOfPeople) FROM ScheduledVisit sv WHERE sv.visit = :visit")
    Integer countBookedPeopleByVisit(@Param("visit") Visit visit);
}
