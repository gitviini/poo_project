package com.arena.app.scheduling.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.scheduling.domain.model.Event;
import com.arena.app.scheduling.domain.model.Sugestion;
import com.arena.app.scheduling.domain.repository.EventRepository;
import com.arena.app.scheduling.domain.repository.ScheduledEventRepository;
import com.arena.app.scheduling.domain.repository.SugestionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SugestionRepository sugestionRepository;

    @Autowired
    private ScheduledEventRepository scheduledEventRepository;

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
        long bookedSeats = scheduledEventRepository.countBookedSeatsByEventId(event.getId());
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
