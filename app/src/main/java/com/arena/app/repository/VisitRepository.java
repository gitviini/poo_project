package com.arena.app.repository;

import com.arena.app.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID> {
}
