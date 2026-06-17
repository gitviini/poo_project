package com.arena.app.scheduling.infrastructure.persistence;

import com.arena.app.scheduling.domain.model.Visit;
import com.arena.app.scheduling.domain.repository.VisitRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class VisitPersistenceAdapter implements VisitRepository {

    private final JpaVisitRepository jpaRepository;

    public VisitPersistenceAdapter(JpaVisitRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Visit> findAll() {
        return jpaRepository.findAll().stream()
                .map(VisitMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Visit> findById(UUID id) {
        return jpaRepository.findById(id).map(VisitMapper::toDomain);
    }

    @Override
    public Visit save(Visit visit) {
        VisitEntity entity = VisitMapper.toEntity(visit);
        VisitEntity saved = jpaRepository.save(entity);
        return VisitMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
