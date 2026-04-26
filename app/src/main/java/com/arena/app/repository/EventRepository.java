package com.arena.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arena.app.model.Event;
import java.util.Optional;
import java.util.List;
import java.util.Date;




@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> findByTitle(String title);

    List<Event> findByDate(Date date);
}