package com.arena.app.scheduling.infrastructure.persistence;

import com.arena.app.scheduling.domain.model.Sugestion;
import com.arena.app.scheduling.domain.repository.SugestionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SugestionPersistenceAdapter implements SugestionRepository {

    private final JpaSugestionRepository jpaRepository;

    public SugestionPersistenceAdapter(JpaSugestionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Sugestion save(Sugestion sugestion) {
        SugestionEntity entity = SugestionMapper.toEntity(sugestion);
        SugestionEntity saved = jpaRepository.save(entity);
        return SugestionMapper.toDomain(saved);
    }
}
