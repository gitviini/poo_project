package com.arena.app.core.domain.event;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;
import com.arena.app.scheduling.domain.model.Visit;


public class VisitVacatedEvent {
    private final Visit visit;

    public VisitVacatedEvent(Visit visit) {
        this.visit = visit;
    }

    public Visit getVisit() {
        return visit;
    }
}
