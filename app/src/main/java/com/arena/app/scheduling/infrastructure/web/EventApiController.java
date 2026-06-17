package com.arena.app.scheduling.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.scheduling.application.service.StatisticsService;
import com.arena.app.scheduling.domain.model.Event;
import com.arena.app.scheduling.domain.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/* 
 
import com.arena.app.scheduling.domain.model.*;
import com.arena.app.scheduling.domain.repository.*;
import com.arena.app.scheduling.application.service.*;
import com.arena.app.scheduling.application.dto.DashboardStatsDTO;
import com.arena.app.iam.domain.model.User;
import com.arena.app.iam.domain.repository.UserRepository;
import com.arena.app.core.domain.event.VisitVacatedEvent;
import com.arena.app.notification.application.service.WaitlistService;
import com.arena.app.core.application.service.BackupService;

import com.arena.app.scheduling.application.dto.DashboardStatsDTO;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.List;
 
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/events")
public class EventApiController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StatisticsService statisticsService;

    // Endpoint para a Home (Retorna apenas a lista de eventos)
    @GetMapping
    public List<Event> getEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        
        return eventRepository.findAll().stream()
            .filter(e -> (category == null || category.isEmpty() || e.getCategory().equalsIgnoreCase(category)))
            .filter(e -> (date == null || e.getDate().equals(date)))
            .collect(Collectors.toList());
    }

    // Endpoint para o Dashboard (Retorna a lista + estatísticas calculadas)
    @GetMapping("/admin/stats")
    public DashboardResponse getStats(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        
        List<Event> filteredEvents = eventRepository.findAll().stream()
            .filter(e -> (category == null || category.isEmpty() || e.getCategory().equalsIgnoreCase(category)))
            .filter(e -> (date == null || e.getDate().equals(date)))
            .collect(Collectors.toList());

        DashboardStatsDTO stats = statisticsService.calculateStats(filteredEvents);
        
        return new DashboardResponse(filteredEvents, stats);
    }

    // Classe auxiliar para resposta combinada do Dashboard
    record DashboardResponse(List<Event> events, DashboardStatsDTO stats) {}
} */


 
import com.arena.app.scheduling.domain.model.*;
import com.arena.app.scheduling.domain.repository.*;
import com.arena.app.scheduling.application.service.*;
import com.arena.app.scheduling.application.dto.DashboardStatsDTO;
import com.arena.app.iam.domain.model.User;
import com.arena.app.iam.domain.repository.UserRepository;
import com.arena.app.core.domain.event.VisitVacatedEvent;
import com.arena.app.notification.application.service.WaitlistService;
import com.arena.app.core.application.service.BackupService;

import com.arena.app.scheduling.application.dto.DashboardStatsDTO;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/events")
public class EventApiController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StatisticsService statisticsService;

    // Endpoint para a Home (Retorna apenas a lista de eventos)
    @GetMapping
    public List<Event> getEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(required = false) String title) { // Adicionado parâmetro title
                

        return eventRepository.findAll().stream()
                .filter(e -> (category == null || category.isEmpty() || category.equalsIgnoreCase(e.getCategory())))
                .filter(e -> (date == null || date.equals(e.getDate())))
                // Adicionado filtro por título (ignorando maiúsculas/minúsculas e buscando
                // partes do texto)
                .filter(e -> (title == null || title.isEmpty()
                        || e.getTitle().toLowerCase().contains(title.toLowerCase())))
                .collect(Collectors.toList());
    }

    // Endpoint para o Dashboard (Retorna a lista + estatísticas calculadas)
    @GetMapping("/admin/stats")
    public ResponseEntity<?> getStats(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(required = false) String title,
            HttpServletRequest request) { // Adicionado parâmetro title

        User authenticatedUser = (User) request.getAttribute("authenticatedUser");
        if (!"admin".equals(authenticatedUser.getRole())) {
            return ResponseEntity.status(403).body(Map.of("message", "Acesso negado"));
        }

        List<Event> filteredEvents = eventRepository.findAll().stream()
                .filter(e -> (category == null || category.isEmpty() || category.equalsIgnoreCase(e.getCategory())))
                .filter(e -> (date == null || date.equals(e.getDate())))
                // Adicionado filtro por título
                .filter(e -> (title == null || title.isEmpty()
                        || e.getTitle().toLowerCase().contains(title.toLowerCase())))
                .collect(Collectors.toList());

        DashboardStatsDTO stats = statisticsService.calculateStats(filteredEvents);

        return ResponseEntity.ok(new DashboardResponse(filteredEvents, stats));
    }

    // Classe auxiliar para resposta combinada do Dashboard
    record DashboardResponse(List<Event> events, DashboardStatsDTO stats) {
    }
}