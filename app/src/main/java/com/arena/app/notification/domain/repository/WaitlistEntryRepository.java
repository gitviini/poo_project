package com.arena.app.notification.domain.repository;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;

import com.arena.app.notification.domain.model.WaitlistEntry;
 
import java.util.List;

public interface WaitlistEntryRepository {
    void save(WaitlistEntry entry);
    void delete(WaitlistEntry entry);
    List<WaitlistEntry> findByEventId(UUID eventId);
    List<WaitlistEntry> findByVisitId(UUID visitId);
}
