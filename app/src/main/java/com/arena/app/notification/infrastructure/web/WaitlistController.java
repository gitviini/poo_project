package com.arena.app.notification.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.notification.domain.model.WaitlistEntry;
import com.arena.app.notification.domain.repository.WaitlistEntryRepository;
import com.arena.app.scheduling.domain.repository.VisitRepository;
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
    private VisitRepository visitRepository;

    @PostMapping("/join-visit")
    public String joinVisitWaitlist(@RequestParam UUID visitId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = (User) request.getAttribute("authenticatedUser");
        var visitOpt = visitRepository.findById(visitId);

        if (visitOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Visita não encontrada", "statusCode", 404));
            return "redirect:/";
        }

        WaitlistEntry entry = WaitlistEntry.forVisit(user.getId(), visitOpt.get().getId());
        waitlistEntryRepository.save(entry);

        redirectAttributes.addFlashAttribute("toast", Map.of("message", "Você entrou na lista de espera!", "statusCode", 201));
        return "redirect:/scheduled-visit/new";
    }
}
