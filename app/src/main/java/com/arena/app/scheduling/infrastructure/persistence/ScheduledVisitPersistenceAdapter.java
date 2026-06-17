package com.arena.app.scheduling.infrastructure.persistence;

import com.arena.app.scheduling.domain.model.ScheduledVisit;
import com.arena.app.scheduling.domain.repository.ScheduledVisitRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ScheduledVisitPersistenceAdapter implements ScheduledVisitRepository {

    private final JpaScheduledVisitRepository jpaRepository;

    public ScheduledVisitPersistenceAdapter(JpaScheduledVisitRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<ScheduledVisit> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(ScheduledVisitMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ScheduledVisit> findByUserIdAndVisitId(UUID userId, UUID visitId) {
        return jpaRepository.findByUserIdAndVisitId(userId, visitId).map(ScheduledVisitMapper::toDomain);
    }

    @Override
    public Integer countBookedPeopleByVisitId(UUID visitId) {
        return jpaRepository.countBookedPeopleByVisitId(visitId);
    }

    @Override
    public ScheduledVisit save(ScheduledVisit scheduledVisit) {
        ScheduledVisitEntity entity = ScheduledVisitMapper.toEntity(scheduledVisit);
        ScheduledVisitEntity saved = jpaRepository.save(entity);
        return ScheduledVisitMapper.toDomain(saved);
    }

    @Override
    public Optional<ScheduledVisit> findById(UUID id) {
        return jpaRepository.findById(id).map(ScheduledVisitMapper::toDomain);
    }

    @Override
    public void delete(ScheduledVisit scheduledVisit) {
        jpaRepository.deleteById(scheduledVisit.getId());
    }
}
