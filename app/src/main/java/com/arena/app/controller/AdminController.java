package com.arena.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.arena.app.model.Event;
import com.arena.app.model.User;
import com.arena.app.repository.EventRepository;
import com.arena.app.repository.UserRepository;
import com.arena.app.service.StatisticsService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Currency;
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
    private com.arena.app.repository.VisitRepository visitRepository;

    @Autowired
    private StatisticsService statsService;

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
        model.addAttribute("event", new Event());
        return "admin/event-form";
    }

    @PostMapping("event/save")
    public String saveEvent(@ModelAttribute Event event, RedirectAttributes redirectAttributes) {

        // Define valores padrão caso venham nulos do formulário
        if (event.getCurrency() == null) {
            event.setCurrency(Currency.getInstance("BRL"));
        }
        if (event.getPrice() == null) {
            event.setPrice(0.0);
        }

        eventRepository.save(event);

        // Configura o Toast para ser exibido após o redirecionamento
        redirectAttributes.addFlashAttribute("toast", Map.of(
                "message", "Evento cadastrado com sucesso!",
                "statusCode", 201));

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
}