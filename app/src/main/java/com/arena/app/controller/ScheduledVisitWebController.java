package com.arena.app.controller;

import com.arena.app.model.ScheduledVisit;
import com.arena.app.model.User;
import com.arena.app.model.Visit;
import com.arena.app.repository.ScheduledVisitRepository;
import com.arena.app.repository.VisitRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.arena.app.service.WaitlistService;

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
    private WaitlistService waitlistService;

    @GetMapping("/new")
    public String showForm(Model model, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);
        
        var visits = visitRepository.findAll();
        java.util.Map<UUID, Integer> bookedSpotsMap = new java.util.HashMap<>();
        for (Visit visit : visits) {
            Integer booked = scheduledVisitRepository.countBookedPeopleByVisit(visit);
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
        
        // Correct capacity check
        Integer bookedPeople = scheduledVisitRepository.countBookedPeopleByVisit(visit);
        if (bookedPeople == null) bookedPeople = 0;
        
        if (bookedPeople + numberOfPeople > visit.getCapacity()) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Vagas insuficientes", "statusCode", 400));
            return "redirect:/scheduled-visit/new";
        }

        ScheduledVisit scheduledVisit = new ScheduledVisit();
        scheduledVisit.setUser(user);
        scheduledVisit.setVisit(visit);
        scheduledVisit.setNumberOfPeople(numberOfPeople);

        scheduledVisitRepository.save(scheduledVisit);

        redirectAttributes.addFlashAttribute("toast", Map.of(
            "message", "Visita agendada com sucesso!",
            "statusCode", 201
        ));

        return "redirect:/";
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
        if (!scheduledVisit.getUser().getUserId().equals(user.getUserId()) && !"admin".equals(user.getRole())) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Acesso negado", "statusCode", 403));
            return "redirect:/";
        }

        Visit visit = scheduledVisit.getVisit();
        scheduledVisitRepository.delete(scheduledVisit);
        
        // Notify waitlist
        waitlistService.notifyWaitlistForVisit(visit);

        redirectAttributes.addFlashAttribute("toast", Map.of("message", "Agendamento cancelado com sucesso!", "statusCode", 200));
        return "redirect:/profile/" + user.getUserId();
    }
}
