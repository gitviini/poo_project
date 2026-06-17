package com.arena.app.scheduling.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.iam.domain.repository.UserRepository;
import com.arena.app.scheduling.domain.model.Event;
import com.arena.app.scheduling.domain.model.ScheduledEvent;
import com.arena.app.scheduling.domain.repository.EventRepository;
import com.arena.app.scheduling.domain.repository.ScheduledEventRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@RestController
@RequestMapping("/api/scheduled-events")
public class ScheduledEventController {

    @Autowired
    private ScheduledEventRepository scheduledEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

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

        var userOpt = userRepository.findByUserId(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Usuário não encontrado"));
        }

        List<ScheduledEvent> events = scheduledEventRepository.findByUserId(userOpt.get().getId());

        List<ScheduledEvent> filteredEvents = events.stream()
                .peek(se -> {
                    eventRepository.findById(se.getEventId()).ifPresent(se::setEvent);
                })
                .filter(se -> {
                    if (se.getEvent() == null) return false;
                    Event ev = se.getEvent();
                    
                    if (title != null && !title.isEmpty() && !ev.getTitle().toLowerCase().contains(title.toLowerCase())) return false;
                    if (date != null) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        if (!sdf.format(ev.getDate()).equals(sdf.format(date))) return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredEvents);
    }

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
            scheduledEvent.setUserId(userOpt.get().getId());
            scheduledEvent.setEventId(eventOpt.get().getId());
            
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
            
            // Set currency from event
            scheduledEvent.setCurrency(eventOpt.get().getCurrency());

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
