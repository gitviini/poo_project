package com.arena.app.scheduling.infrastructure.persistence;

import com.arena.app.scheduling.domain.model.Sugestion;

public class SugestionMapper {

    public static SugestionEntity toEntity(Sugestion domain) {
        return new SugestionEntity(
            domain.getId(),
            domain.getTitle(),
            domain.getDescription(),
            domain.getCreatedAt(),
            domain.getUpdatedAt()
        );
    }

    public static Sugestion toDomain(SugestionEntity entity) {
        Sugestion domain = new Sugestion();
        domain.setId(entity.getId());
        domain.setTitle(entity.getTitle());
        domain.setDescription(entity.getDescription());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }
}
