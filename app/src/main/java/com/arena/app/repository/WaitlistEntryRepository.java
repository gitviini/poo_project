package com.arena.app.repository;

import com.arena.app.model.WaitlistEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import com.arena.app.model.Event;
import com.arena.app.model.Visit;

public interface WaitlistEntryRepository extends JpaRepository<WaitlistEntry, UUID> {
    List<WaitlistEntry> findByEventOrderByCreatedAtAsc(Event event);
    List<WaitlistEntry> findByVisitOrderByCreatedAtAsc(Visit visit);
}
