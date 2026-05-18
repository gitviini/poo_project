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

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/scheduled-visit")
public class ScheduledVisitWebController {

    @Autowired
    private ScheduledVisitRepository scheduledVisitRepository;

    @Autowired
    private VisitRepository visitRepository;

    @GetMapping("/new")
    public String showForm(Model model, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);
        model.addAttribute("visits", visitRepository.findAll());
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
        
        // Basic capacity check (could be improved by checking existing bookings)
        if (numberOfPeople > visit.getCapacity()) {
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
}
