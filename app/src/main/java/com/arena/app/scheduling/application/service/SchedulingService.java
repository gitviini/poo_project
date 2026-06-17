package com.arena.app.scheduling.application.service;

import com.arena.app.scheduling.domain.model.ScheduledVisit;
import com.arena.app.scheduling.domain.model.Visit;
import com.arena.app.scheduling.domain.repository.ScheduledVisitRepository;
import com.arena.app.scheduling.domain.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SchedulingService {
    private final ScheduledVisitRepository repository;
    private final VisitRepository visitRepository;

    public SchedulingService(ScheduledVisitRepository repository, VisitRepository visitRepository) {
        this.repository = repository;
        this.visitRepository = visitRepository;
    }

    public ScheduledVisit scheduleVisit(ScheduledVisit scheduledVisit) {
        validateUniqueness(scheduledVisit);
        validateCapacity(scheduledVisit);
        scheduledVisit.setStatus("CONFIRMADO");
        return repository.save(scheduledVisit);
    }

    private void validateUniqueness(ScheduledVisit scheduledVisit) {
        Optional<ScheduledVisit> existing = repository.findByUserIdAndVisitId(scheduledVisit.getUserId(), scheduledVisit.getVisitId());
        if (existing.isPresent()) {
            throw new RuntimeException("Você já possui um agendamento para este horário.");
        }
    }

    private void validateCapacity(ScheduledVisit scheduledVisit) {
        Visit visit = visitRepository.findById(scheduledVisit.getVisitId())
            .orElseThrow(() -> new RuntimeException("Visita não encontrada"));
            
        Integer booked = repository.countBookedPeopleByVisitId(visit.getId());
        int currentTotal = (booked != null ? booked : 0);

        visit.validateCapacity(currentTotal, scheduledVisit.getNumberOfPeople());
    }
}
