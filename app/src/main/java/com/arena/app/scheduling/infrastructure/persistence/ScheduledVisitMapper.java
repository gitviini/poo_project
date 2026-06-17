package com.arena.app.scheduling.infrastructure.persistence;

import com.arena.app.scheduling.domain.model.ScheduledVisit;

public class ScheduledVisitMapper {

    public static ScheduledVisitEntity toEntity(ScheduledVisit domain) {
        return new ScheduledVisitEntity(
            domain.getId(),
            domain.getUserId(),
            domain.getVisitId(),
            domain.getNumberOfPeople(),
            domain.getStatus(),
            domain.getCreatedAt()
        );
    }

    public static ScheduledVisit toDomain(ScheduledVisitEntity entity) {
        ScheduledVisit domain = new ScheduledVisit();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setVisitId(entity.getVisitId());
        if (entity.getVisit() != null) {
            domain.setVisit(VisitMapper.toDomain(entity.getVisit()));
        }
        domain.setNumberOfPeople(entity.getNumberOfPeople());
        domain.setStatus(entity.getStatus());
        domain.setCreatedAt(entity.getCreatedAt());
        return domain;
    }
}
