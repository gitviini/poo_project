package com.arena.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arena.app.model.ScheduledEvent;
import com.arena.app.model.Event;

@Repository
public interface ScheduledEventRepository extends JpaRepository<ScheduledEvent, UUID> {
    List<ScheduledEvent> findByUserUserId(String userId);

    @Query("SELECT COUNT(s) FROM ScheduledEvent se JOIN se.seats s WHERE se.event = :event")
    long countBookedSeatsByEvent(@Param("event") Event event);
}
