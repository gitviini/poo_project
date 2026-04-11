package com.arena.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.arena.app.repository.EventRepository;

@Controller("/")
public class Home {
    @Autowired
    EventRepository eventRepository;

    @GetMapping("")
    public String getHome(Model model) {

        model.addAttribute("cards", eventRepository.findAll());

        return "home";
    }

    @GetMapping("/event/{title}")
    public String getMethodName(@PathVariable("title") String eventTitle, Model model) {

        var eventOpt = eventRepository.findByTitle(eventTitle);

        if(eventOpt.isEmpty()){
            return "redirect:/";
        }

        var event = eventOpt.get();

        model.addAttribute("event", event);

        return "event";
    }
}
