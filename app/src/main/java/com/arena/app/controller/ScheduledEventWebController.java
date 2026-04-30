package com.arena.app.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.arena.app.model.Event;
import com.arena.app.model.ScheduledEvent;
import com.arena.app.model.User;
import com.arena.app.repository.EventRepository;
import com.arena.app.repository.ScheduledEventRepository;
import com.arena.app.repository.UserRepository;

@Controller
@RequestMapping("/scheduled-event")
public class ScheduledEventWebController {

    @Autowired
    private ScheduledEventRepository scheduledEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/new")
    public String showScheduledEventForm(@RequestParam("eventTitle") String eventTitle, Model model) {
        var eventOpt = eventRepository.findByTitle(eventTitle);
        if (eventOpt.isEmpty()) {
            return "redirect:/";
        }

        // Placeholder user
        var userOpt = userRepository.findByEmail("gvinicius105@gmail.com");
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        model.addAttribute("event", eventOpt.get());
        model.addAttribute("user", userOpt.get());
        model.addAttribute("scheduledEvent", new ScheduledEvent());

        return "scheduled-event-form";
    }

    @PostMapping("/save")
    public String saveScheduledEvent(@RequestParam("email") String email,
                                     @RequestParam("eventTitle") String eventTitle,
                                     @RequestParam("arenaArea") String arenaArea,
                                     @RequestParam("seatsString") String seatsString,
                                     RedirectAttributes redirectAttributes) {
        
        var userOpt = userRepository.findByEmail(email);
        var eventOpt = eventRepository.findByTitle(eventTitle);

        if (userOpt.isEmpty() || eventOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("toast", Map.of(
                "message", "Usuário ou Evento não encontrado",
                "statusCode", 404
            ));
            return "redirect:/";
        }

        Event event = eventOpt.get();
        User user = userOpt.get();

        ScheduledEvent scheduledEvent = new ScheduledEvent();
        scheduledEvent.setUser(user);
        scheduledEvent.setEvent(event);
        scheduledEvent.setArenaArea(arenaArea);
        
        String[] seatsArray = seatsString.split(",");
        java.util.List<String> seatsList = java.util.stream.Stream.of(seatsArray)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toList());
        scheduledEvent.setSeats(seatsList);
        
        scheduledEvent.setPricePerSeat(event.getPrice());
        scheduledEvent.setCurrency(event.getCurrency());
        scheduledEvent.setTotalPrice(event.getPrice() * seatsList.size());

        scheduledEventRepository.save(scheduledEvent);

        redirectAttributes.addFlashAttribute("toast", Map.of(
            "message", "Ingresso comprado com sucesso!",
            "statusCode", 201
        ));

        return "redirect:/";
    }
}
