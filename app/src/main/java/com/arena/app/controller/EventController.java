package com.arena.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arena.app.model.Sugestion;
import com.arena.app.model.User;
import com.arena.app.repository.EventRepository;
import com.arena.app.repository.SugestionRepository;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.arena.app.repository.ScheduledEventRepository;

@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SugestionRepository sugestionRepository;

    @Autowired
    ScheduledEventRepository scheduledEventRepository;

    @GetMapping("{title}")
    public String getEventByTitle(@PathVariable("title") String eventTitle, Model model, HttpServletRequest request) {

        var eventOpt = eventRepository.findByTitle(eventTitle);

        if (eventOpt.isEmpty()) {
            return "redirect:/";
        }

        var event = eventOpt.get();

        User user = (User) request.getAttribute("authenticatedUser");
        model.addAttribute("user", user);
        model.addAttribute("event", event);

        // Capacity calculation
        long bookedSeats = scheduledEventRepository.countBookedSeatsByEvent(event);
        model.addAttribute("bookedSeats", bookedSeats);
        model.addAttribute("isSoldOut", event.getCapacity() != null && bookedSeats >= event.getCapacity());

        return "event";
    }
    
    @GetMapping("sugestion/new")
    public String showSugestionForm(Model model) {
        model.addAttribute("sugestion", new Sugestion());
        return "event-sugestion-form";
    }

    @PostMapping("sugestion/save")
    public String saveSugestion(@ModelAttribute Sugestion sugestion, RedirectAttributes redirectAttributes) {
        sugestionRepository.save(sugestion);

        redirectAttributes.addFlashAttribute("toast", Map.of(
                "message", "Sugestão enviada com sucesso!",
                "statusCode", 201));

        return "redirect:/"; 
    }
}
