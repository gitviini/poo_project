package com.arena.app.scheduling.application.service;

import com.arena.app.scheduling.application.dto.EventRequestDTO;
import com.arena.app.scheduling.domain.model.Event;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class EventMapper {

    public Event toEntity(EventRequestDTO dto) {
        Event event = new Event();
        return updateEntity(event, dto);
    }

    public Event updateEntity(Event event, EventRequestDTO dto) {
        if (dto.getId() != null) {
            event.setId(dto.getId());
        }
        event.setTitle(dto.getTitle());
        event.setDate(dto.getDate());
        event.setDescription(dto.getDescription());
        
        if (dto.getCurrency() != null) {
            event.setCurrency(dto.getCurrency());
        } else if (event.getCurrency() == null) {
            event.setCurrency(Currency.getInstance("BRL"));
        }
        
        if (dto.getPrice() != null) {
            event.setPrice(dto.getPrice());
        } else if (event.getPrice() == null) {
            event.setPrice(0.0);
        }
        
        event.setCapacity(dto.getCapacity());
        event.setCategory(dto.getCategory());
        event.setImageBase64(dto.getImageBase64());
        
        return event;
    }
    
    public EventRequestDTO toDTO(Event event) {
        EventRequestDTO dto = new EventRequestDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDate(event.getDate());
        dto.setDescription(event.getDescription());
        dto.setCurrency(event.getCurrency());
        dto.setPrice(event.getPrice());
        dto.setCapacity(event.getCapacity());
        dto.setCategory(event.getCategory());
        dto.setImageBase64(event.getImageBase64());
        return dto;
    }
}
