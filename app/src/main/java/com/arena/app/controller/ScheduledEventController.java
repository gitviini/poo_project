package com.arena.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arena.app.model.ScheduledEvent;
import com.arena.app.model.User;
import com.arena.app.model.Event;
import com.arena.app.repository.ScheduledEventRepository;
import com.arena.app.repository.UserRepository;
import com.arena.app.repository.EventRepository;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/scheduled-events")
public class ScheduledEventController {

    @Autowired
    private ScheduledEventRepository scheduledEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    // 1. Endpoint GET: Retorna todos os eventos agendados de um usuário pelo userId com filtros
    @GetMapping
    public ResponseEntity<?> getScheduledEventsByUserId(
            @RequestParam("userId") String userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date date,
            HttpServletRequest request) {
        
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");
        if (!authenticatedUser.getUserId().equals(userId) && !"admin".equals(authenticatedUser.getRole())) {
            return ResponseEntity.status(403).body(Map.of("message", "Acesso negado"));
        }

        List<ScheduledEvent> events = scheduledEventRepository.findByUserUserId(userId);

        List<ScheduledEvent> filteredEvents = events.stream()
                .filter(se -> (title == null || title.isEmpty() 
                        || se.getEvent().getTitle().toLowerCase().contains(title.toLowerCase())))
                .filter(se -> {
                    if (date == null) return true;
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(se.getEvent().getDate()).equals(sdf.format(date));
                })
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(filteredEvents);
    }

    // 2. Endpoint POST: Criação de um novo evento agendado
    @PostMapping
    public ResponseEntity<?> createScheduledEvent(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        try {
            String userId = (String) payload.get("userId");
            User authenticatedUser = (User) request.getAttribute("authenticatedUser");

            if (!authenticatedUser.getUserId().equals(userId) && !"admin".equals(authenticatedUser.getRole())) {
                return ResponseEntity.status(403).body(Map.of("message", "Acesso negado"));
            }

            String eventTitle = (String) payload.get("eventTitle");
            
            var userOpt = userRepository.findByUserId(userId);
            var eventOpt = eventRepository.findByTitle(eventTitle);

            if (userOpt.isEmpty() || eventOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("message", "Usuário ou Evento não encontrado"));
            }

            ScheduledEvent scheduledEvent = new ScheduledEvent();
            scheduledEvent.setUser(userOpt.get());
            scheduledEvent.setEvent(eventOpt.get());
            
            // Campos opcionais
            if (payload.containsKey("arenaArea")) {
                scheduledEvent.setArenaArea((String) payload.get("arenaArea"));
            }
            if (payload.containsKey("seats")) {
                scheduledEvent.setSeats((List<String>) payload.get("seats"));
            }
            if (payload.containsKey("pricePerSeat")) {
                scheduledEvent.setPricePerSeat(Double.valueOf(payload.get("pricePerSeat").toString()));
            }
            if (payload.containsKey("totalDiscount")) {
                scheduledEvent.setTotalDiscount(Double.valueOf(payload.get("totalDiscount").toString()));
            }
            if (payload.containsKey("totalPrice")) {
                scheduledEvent.setTotalPrice(Double.valueOf(payload.get("totalPrice").toString()));
            }

            scheduledEventRepository.save(scheduledEvent);

            return ResponseEntity.status(201).body(Map.of(
                "message", "Evento agendado com sucesso!",
                "id", scheduledEvent.getId()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "message", "Erro ao agendar evento",
                "error", e.getMessage()
            ));
        }
    }
}
