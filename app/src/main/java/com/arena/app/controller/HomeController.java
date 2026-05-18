package com.arena.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.arena.app.model.User;
import com.arena.app.repository.UserRepository;
import com.arena.app.repository.ScheduledVisitRepository;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduledVisitRepository scheduledVisitRepository;

    @GetMapping("/")
    public String getHome(Model model, HttpServletRequest request) {

        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);
        
        model.addAttribute("scheduledVisits", scheduledVisitRepository.findByUserUserId(user.getUserId()));

        return "home";
    }
}
