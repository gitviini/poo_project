package com.arena.app.controller;

import com.arena.app.model.Event;
import com.arena.app.model.User;
import com.arena.app.model.Visit;
import com.arena.app.model.WaitlistEntry;
import com.arena.app.repository.EventRepository;
import com.arena.app.repository.UserRepository;
import com.arena.app.repository.VisitRepository;
import com.arena.app.repository.WaitlistEntryRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/waitlist")
public class WaitlistController {

    @Autowired
    private WaitlistEntryRepository waitlistEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VisitRepository visitRepository;

    @PostMapping("/join-event")
    public String joinEventWaitlist(@RequestParam String eventTitle, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = (User) request.getAttribute("authenticatedUser");
        var eventOpt = eventRepository.findByTitle(eventTitle);

        if (eventOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Evento não encontrado", "statusCode", 404));
            return "redirect:/";
        }

        WaitlistEntry entry = new WaitlistEntry();
        entry.setUser(user);
        entry.setEvent(eventOpt.get());
        waitlistEntryRepository.save(entry);

        redirectAttributes.addFlashAttribute("toast", Map.of("message", "Você entrou na lista de espera!", "statusCode", 201));
        return "redirect:/event/" + eventTitle;
    }

    @PostMapping("/join-visit")
    public String joinVisitWaitlist(@RequestParam UUID visitId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = (User) request.getAttribute("authenticatedUser");
        var visitOpt = visitRepository.findById(visitId);

        if (visitOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Visita não encontrada", "statusCode", 404));
            return "redirect:/";
        }

        WaitlistEntry entry = new WaitlistEntry();
        entry.setUser(user);
        entry.setVisit(visitOpt.get());
        waitlistEntryRepository.save(entry);

        redirectAttributes.addFlashAttribute("toast", Map.of("message", "Você entrou na lista de espera!", "statusCode", 201));
        return "redirect:/scheduled-visit/new";
    }
}
