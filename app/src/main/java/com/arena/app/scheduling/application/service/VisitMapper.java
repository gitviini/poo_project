package com.arena.app.scheduling.application.service;

import com.arena.app.scheduling.application.dto.VisitRequestDTO;
import com.arena.app.scheduling.domain.model.Visit;
import org.springframework.stereotype.Component;

@Component
public class VisitMapper {

    public Visit toEntity(VisitRequestDTO dto) {
        Visit visit = new Visit();
        return updateEntity(visit, dto);
    }

    public Visit updateEntity(Visit visit, VisitRequestDTO dto) {
        if (dto.getId() != null) {
            visit.setId(dto.getId());
        }
        visit.setDate(dto.getDate());
        visit.setTime(dto.getTime());
        visit.setCapacity(dto.getCapacity());
        visit.setDescription(dto.getDescription());
        return visit;
    }

    public VisitRequestDTO toDTO(Visit visit) {
        VisitRequestDTO dto = new VisitRequestDTO();
        dto.setId(visit.getId());
        dto.setDate(visit.getDate());
        dto.setTime(visit.getTime());
        dto.setCapacity(visit.getCapacity());
        dto.setDescription(visit.getDescription());
        return dto;
    }
}
