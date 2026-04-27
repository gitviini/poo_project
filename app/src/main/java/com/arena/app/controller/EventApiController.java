/* package com.arena.app.controller;

import com.arena.app.dto.DashboardStatsDTO;
import com.arena.app.model.Event;
import com.arena.app.repository.EventRepository;
import com.arena.app.service.StatisticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

package com.arena.app.controller;

import com.arena.app.dto.DashboardStatsDTO;
import com.arena.app.model.Event;
import com.arena.app.repository.EventRepository;
import com.arena.app.service.StatisticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public DashboardResponse getStats(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(required = false) String title) { // Adicionado parâmetro title

        List<Event> filteredEvents = eventRepository.findAll().stream()
                .filter(e -> (category == null || category.isEmpty() || category.equalsIgnoreCase(e.getCategory())))
                .filter(e -> (date == null || date.equals(e.getDate())))
                // Adicionado filtro por título
                .filter(e -> (title == null || title.isEmpty()
                        || e.getTitle().toLowerCase().contains(title.toLowerCase())))
                .collect(Collectors.toList());

        DashboardStatsDTO stats = statisticsService.calculateStats(filteredEvents);

        return new DashboardResponse(filteredEvents, stats);
    }

    // Classe auxiliar para resposta combinada do Dashboard
    record DashboardResponse(List<Event> events, DashboardStatsDTO stats) {
    }
}