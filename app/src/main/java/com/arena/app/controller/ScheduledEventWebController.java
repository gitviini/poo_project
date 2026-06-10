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
import com.arena.app.service.WaitlistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@Controller
@RequestMapping("/scheduled-event")
public class ScheduledEventWebController {

    @Autowired
    private ScheduledEventRepository scheduledEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private WaitlistService waitlistService;

    @GetMapping("/new")
    public String showScheduledEventForm(@RequestParam("eventTitle") String eventTitle, Model model, HttpServletRequest request) {
        var eventOpt = eventRepository.findByTitle(eventTitle);
        if (eventOpt.isEmpty()) {
            return "redirect:/";
        }

        User user = (User) request.getAttribute("authenticatedUser");

        model.addAttribute("event", eventOpt.get());
        model.addAttribute("user", user);
        model.addAttribute("scheduledEvent", new ScheduledEvent());

        return "scheduled-event-form";
    }

    @PostMapping("/save")
    public String saveScheduledEvent(@RequestParam("userId") String userId,
                                     @RequestParam("eventTitle") String eventTitle,
                                     @RequestParam("arenaArea") String arenaArea,
                                     @RequestParam("seatsString") String seatsString,
                                     RedirectAttributes redirectAttributes) {

        System.out.println(seatsString);
        
        var userOpt = userRepository.findByUserId(userId);
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

        String[] seatsArray = seatsString.split(",");
        java.util.List<String> seatsList = java.util.stream.Stream.of(seatsArray)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toList());

        // Capacity check
        if (event.getCapacity() != null) {
            long bookedSeats = scheduledEventRepository.countBookedSeatsByEvent(event);
            if (bookedSeats + seatsList.size() > event.getCapacity()) {
                redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Vagas insuficientes! Capacidade esgotada.",
                    "statusCode", 400
                ));
                return "redirect:/event/" + eventTitle;
            }
        }

        ScheduledEvent scheduledEvent = new ScheduledEvent();
        scheduledEvent.setUser(user);
        scheduledEvent.setEvent(event);
        scheduledEvent.setArenaArea(arenaArea);
        
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

    @PostMapping("/cancel/{id}")
    public String cancelScheduledEvent(@PathVariable UUID id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = (User) request.getAttribute("authenticatedUser");
        var scheduledEventOpt = scheduledEventRepository.findById(id);

        if (scheduledEventOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Agendamento não encontrado", "statusCode", 404));
            return "redirect:/";
        }

        ScheduledEvent scheduledEvent = scheduledEventOpt.get();
        if (!scheduledEvent.getUser().getUserId().equals(user.getUserId()) && !"admin".equals(user.getRole())) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Acesso negado", "statusCode", 403));
            return "redirect:/";
        }

        Event event = scheduledEvent.getEvent();
        scheduledEventRepository.delete(scheduledEvent);
        
        // Notify waitlist
        waitlistService.notifyWaitlistForEvent(event);

        redirectAttributes.addFlashAttribute("toast", Map.of("message", "Agendamento cancelado com sucesso!", "statusCode", 200));
        return "redirect:/profile/" + user.getUserId();
    }
}
