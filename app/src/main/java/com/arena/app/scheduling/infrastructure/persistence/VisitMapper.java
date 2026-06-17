package com.arena.app.scheduling.infrastructure.persistence;

import com.arena.app.scheduling.domain.model.Visit;

public class VisitMapper {

    public static VisitEntity toEntity(Visit domain) {
        return new VisitEntity(
            domain.getId(),
            domain.getDate(),
            domain.getTime(),
            domain.getCapacity(),
            domain.getDescription()
        );
    }

    public static Visit toDomain(VisitEntity entity) {
        Visit domain = new Visit();
        domain.setId(entity.getId());
        domain.setDate(entity.getDate());
        domain.setTime(entity.getTime());
        domain.setCapacity(entity.getCapacity());
        domain.setDescription(entity.getDescription());
        return domain;
    }
}
