package com.arena.app.scheduling.domain.repository;

import com.arena.app.scheduling.domain.model.Visit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitRepository {
    List<Visit> findAll();
    Optional<Visit> findById(UUID id);
    Visit save(Visit visit);
    void deleteById(UUID id);
}
