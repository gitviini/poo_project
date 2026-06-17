package com.arena.app.scheduling.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.scheduling.domain.repository.ScheduledVisitRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ScheduledVisitRepository scheduledVisitRepository;

    @GetMapping("/")
    public String getHome(Model model, HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);
        
        model.addAttribute("scheduledVisits", scheduledVisitRepository.findByUserId(user.getId()));

        return "home";
    }
}
