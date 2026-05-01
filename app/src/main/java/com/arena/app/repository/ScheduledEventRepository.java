package com.arena.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arena.app.model.ScheduledEvent;

@Repository
public interface ScheduledEventRepository extends JpaRepository<ScheduledEvent, UUID> {
    List<ScheduledEvent> findByUserUserId(String userId);
}
