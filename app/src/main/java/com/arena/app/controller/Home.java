package com.arena.app.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.arena.app.model.Event;
import com.arena.app.repository.EventRepository;

@Controller("/")
public class Home {
    @Autowired
    EventRepository eventRepository;

    @GetMapping("")
    public String getHome(Model model) {

        var cards = eventRepository.findAll();

        model.addAttribute("cards", cards);

        return "home";
    }

    @GetMapping("/event/{title}")
    public String getMethodName(@PathVariable("title") String eventTitle, Model model) {

        var eventOpt = eventRepository.findByTitle(eventTitle);

        if (eventOpt.isEmpty()) {
            return "redirect:/";
        }

        var event = eventOpt.get();

        model.addAttribute("event", event);

        return "event";
    }

    @GetMapping("/events/filter")
    public String filterEvents(
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(value = "category", required = false) String category,
            Model model) {

        System.out.println(date);

        List<Event> filteredEvents;

        if (date != null) {
            filteredEvents = eventRepository.findByDate(date);
        }
        // Exemplo de lógica para categoria se o campo existir:
        // else if (category != null && !category.isEmpty()) {
        // filteredEvents = eventRepository.findByCategory(category);
        // }
        else {
            filteredEvents = eventRepository.findAll();
        }

        model.addAttribute("cards", filteredEvents);
        return "home";
    }

}
