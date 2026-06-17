package com.arena.app.scheduling.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Date;

public interface JpaEventRepository extends JpaRepository<EventEntity, UUID> {
    Optional<EventEntity> findByTitle(String title);
    List<EventEntity> findByDate(Date date);
}
