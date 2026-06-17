package com.arena.app.scheduling.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.iam.domain.repository.UserRepository;
import com.arena.app.scheduling.application.dto.EventRequestDTO;
import com.arena.app.scheduling.application.service.EventMapper;
import com.arena.app.scheduling.application.service.StatisticsService;
import com.arena.app.scheduling.domain.model.Event;
import com.arena.app.scheduling.domain.repository.EventRepository;
import com.arena.app.scheduling.domain.repository.VisitRepository;
import com.arena.app.core.application.service.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private StatisticsService statsService;

    @Autowired
    private BackupService backupService;

    @Autowired
    private EventMapper eventMapper;

    @GetMapping("dashboard")
    public String showDashboard(@RequestParam(required = false) String category, Model model,
            HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);

        model.addAttribute("visits", visitRepository.findAll());

        List<Event> events = null;

        // Reaproveitando a lógica de filtragem existente
        if (category != null && !category.isEmpty()) {
            events = eventRepository.findAll().stream()
                    .filter(e -> category.equalsIgnoreCase(e.getCategory()))
                    .collect(Collectors.toList());
        }
        if (events == null || events.isEmpty()) {
            events = eventRepository.findAll();
        }

        model.addAttribute("stats", statsService.calculateStats(events));
        model.addAttribute("events", events);
        return "admin/dashboard";
    }

    /*
     * --- PRIVATE EVENT HANDLERS ---
     */

    @GetMapping("event/new")
    public String showRegistrationForm(Model model) {
        model.addAttribute("event", new EventRequestDTO());
        return "admin/event-form";
    }

    @GetMapping("event/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + id));
        model.addAttribute("event", eventMapper.toDTO(event));
        return "admin/event-form";
    }

    @PostMapping("event/save")
    public String saveEvent(@ModelAttribute("event") EventRequestDTO eventDTO, RedirectAttributes redirectAttributes) {
        boolean isNew = eventDTO.getId() == null;
        
        Event event;
        if (isNew) {
            event = eventMapper.toEntity(eventDTO);
        } else {
            event = eventRepository.findById(eventDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + eventDTO.getId()));
            eventMapper.updateEntity(event, eventDTO);
        }

        eventRepository.save(event);

        // Configura o Toast para ser exibido após o redirecionamento
        redirectAttributes.addFlashAttribute("toast", Map.of(
                "message", isNew ? "Evento cadastrado com sucesso!" : "Evento atualizado com sucesso!",
                "statusCode", isNew ? 201 : 200));

        return "redirect:/admin/dashboard";
    }

    @PostMapping("event/delete/{id}")
    public String deleteEvent(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        eventRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("toast", Map.of(
                "message", "Evento deletado com sucesso!",
                "statusCode", 200));

        return "redirect:/admin/dashboard";
    }

    /*
     * --- BACKUP ---
     * Gera um backup manual do banco. Toda rota sob /admin/** ja passa pela
     * checagem de role no AuthInterceptor, entao isto e acessivel so ao admin.
     */

    @PostMapping("backup/run")
    public String runBackup(RedirectAttributes redirectAttributes) {
        try {
            Path arquivo = backupService.createBackup();
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Backup criado: " + arquivo.getFileName().toString(),
                    "statusCode", 200));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Falha ao criar backup: " + e.getMessage(),
                    "statusCode", 500));
        }

        return "redirect:/admin/dashboard";
    }
}
