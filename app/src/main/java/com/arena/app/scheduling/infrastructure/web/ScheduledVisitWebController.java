package com.arena.app.scheduling.infrastructure.web;

import com.arena.app.core.domain.event.VisitVacatedEvent;
import com.arena.app.iam.domain.model.User;
import com.arena.app.scheduling.application.service.SchedulingService;
import com.arena.app.scheduling.domain.model.ScheduledVisit;
import com.arena.app.scheduling.domain.model.Visit;
import com.arena.app.scheduling.domain.repository.ScheduledVisitRepository;
import com.arena.app.scheduling.domain.repository.VisitRepository;
import com.arena.app.iam.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/scheduled-visit")
public class ScheduledVisitWebController {

    @Autowired
    private ScheduledVisitRepository scheduledVisitRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private SchedulingService schedulingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/new")
    public String showForm(Model model, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);
        
        var visits = visitRepository.findAll();
        Map<UUID, Integer> bookedSpotsMap = new HashMap<>();
        for (Visit visit : visits) {
            Integer booked = scheduledVisitRepository.countBookedPeopleByVisitId(visit.getId());
            bookedSpotsMap.put(visit.getId(), booked != null ? booked : 0);
        }
        
        model.addAttribute("visits", visits);
        model.addAttribute("bookedSpotsMap", bookedSpotsMap);
        
        return "scheduled-visit-form";
    }

    @PostMapping("/save")
    public String save(@RequestParam UUID visitId, @RequestParam Integer numberOfPeople, 
                       HttpServletRequest request, RedirectAttributes redirectAttributes) {
        
        User user = (User) request.getAttribute("authenticatedUser");
        var visitOpt = visitRepository.findById(visitId);

        if (visitOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Visita não encontrada", "statusCode", 404));
            return "redirect:/";
        }

        Visit visit = visitOpt.get();

        ScheduledVisit scheduledVisit = new ScheduledVisit();
        scheduledVisit.setUserId(user.getId());
        scheduledVisit.setVisitId(visit.getId());
        scheduledVisit.setNumberOfPeople(numberOfPeople);

        try {
            scheduledVisit = schedulingService.scheduleVisit(scheduledVisit);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", e.getMessage(), "statusCode", 400));
            return "redirect:/scheduled-visit/new";
        }

        redirectAttributes.addFlashAttribute("toast", Map.of(
            "message", "Visita agendada com sucesso!",
            "statusCode", 201
        ));

        return "redirect:/scheduled-visit/ticket/" + scheduledVisit.getId();
    }

    @GetMapping("/ticket/{id}")
    public String viewTicket(@PathVariable UUID id, Model model, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        var ticketOpt = scheduledVisitRepository.findById(id);

        if (ticketOpt.isEmpty()) {
            return "redirect:/";
        }

        ScheduledVisit ticket = ticketOpt.get();
        var userOfTicketOpt = userRepository.findById(ticket.getUserId());

        if (userOfTicketOpt.isEmpty() || (!userOfTicketOpt.get().getUserId().equals(user.getUserId()) && !"admin".equals(user.getRole()))) {
            return "redirect:/";
        }

        model.addAttribute("ticket", ticket);
        var visitOpt = visitRepository.findById(ticket.getVisitId());
        model.addAttribute("visit", visitOpt.orElse(null));
        model.addAttribute("ticketUser", userOfTicketOpt.get());

        return "scheduled-visit-ticket";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable UUID id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = (User) request.getAttribute("authenticatedUser");
        var scheduledVisitOpt = scheduledVisitRepository.findById(id);

        if (scheduledVisitOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Agendamento não encontrado", "statusCode", 404));
            return "redirect:/";
        }

        ScheduledVisit scheduledVisit = scheduledVisitOpt.get();
        var userOfTicketOpt = userRepository.findById(scheduledVisit.getUserId());

        if (userOfTicketOpt.isEmpty() || (!userOfTicketOpt.get().getUserId().equals(user.getUserId()) && !"admin".equals(user.getRole()))) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Acesso negado", "statusCode", 403));
            return "redirect:/";
        }

        var visitOpt = visitRepository.findById(scheduledVisit.getVisitId());
        scheduledVisitRepository.delete(scheduledVisit);
        
        if (visitOpt.isPresent()) {
            eventPublisher.publishEvent(new VisitVacatedEvent(visitOpt.get()));
        }

        redirectAttributes.addFlashAttribute("toast", Map.of("message", "Agendamento cancelado com sucesso!", "statusCode", 200));
        return "redirect:/profile/" + user.getUserId();
    }
}
