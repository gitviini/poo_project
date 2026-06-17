package com.arena.app.scheduling.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JpaSugestionRepository extends JpaRepository<SugestionEntity, UUID> {
}
