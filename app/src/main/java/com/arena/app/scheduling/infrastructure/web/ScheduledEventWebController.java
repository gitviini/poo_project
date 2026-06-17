package com.arena.app.scheduling.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.iam.domain.repository.UserRepository;
import com.arena.app.notification.application.service.WaitlistService;
import com.arena.app.scheduling.domain.model.Event;
import com.arena.app.scheduling.domain.model.ScheduledEvent;
import com.arena.app.scheduling.domain.repository.EventRepository;
import com.arena.app.scheduling.domain.repository.ScheduledEventRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<String> seatsList = Stream.of(seatsArray)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        if (event.getCapacity() != null) {
            long bookedSeats = scheduledEventRepository.countBookedSeatsByEventId(event.getId());
            if (bookedSeats + seatsList.size() > event.getCapacity()) {
                redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Vagas insuficientes! Capacidade esgotada.",
                    "statusCode", 400
                ));
                return "redirect:/event/" + eventTitle;
            }
        }

        ScheduledEvent scheduledEvent = new ScheduledEvent();
        scheduledEvent.setUserId(user.getId());
        scheduledEvent.setEventId(event.getId());
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
        var eventOpt = eventRepository.findById(scheduledEvent.getEventId());
        var userOfEventOpt = userRepository.findById(scheduledEvent.getUserId());

        if (userOfEventOpt.isEmpty() || (!userOfEventOpt.get().getUserId().equals(user.getUserId()) && !"admin".equals(user.getRole()))) {
            redirectAttributes.addFlashAttribute("toast", Map.of("message", "Acesso negado", "statusCode", 403));
            return "redirect:/";
        }

        scheduledEventRepository.delete(scheduledEvent);
        
        if (eventOpt.isPresent()) {
            waitlistService.notifyWaitlistForEvent(eventOpt.get());
        }

        redirectAttributes.addFlashAttribute("toast", Map.of("message", "Agendamento cancelado com sucesso!", "statusCode", 200));
        return "redirect:/profile/" + user.getUserId();
    }
}
