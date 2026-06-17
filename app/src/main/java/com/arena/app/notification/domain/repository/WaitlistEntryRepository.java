package com.arena.app.notification.domain.repository;

import com.arena.app.notification.domain.model.WaitlistEntry;
import java.util.List;
import java.util.UUID;

public interface WaitlistEntryRepository {
    void save(WaitlistEntry entry);
    void delete(WaitlistEntry entry);
    List<WaitlistEntry> findByEventId(UUID eventId);
    List<WaitlistEntry> findByVisitId(UUID visitId);
}
