package com.arena.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arena.app.model.Sugestion;

@Repository
public interface SugestionRepository extends JpaRepository<Sugestion, UUID> {
}