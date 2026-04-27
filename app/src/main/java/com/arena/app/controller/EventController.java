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
import com.arena.app.repository.EventRepository;
import com.arena.app.repository.SugestionRepository;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/event")
public class EventController {

    /* 
    --- PUBLIC EVENT HANDLERS ---
     */
    
    @Autowired
    EventRepository eventRepository;

    @Autowired
    SugestionRepository sugestionRepository;

    @GetMapping("{title}")
    public String getEventByTitle(@PathVariable("title") String eventTitle, Model model) {

        var eventOpt = eventRepository.findByTitle(eventTitle);

        if (eventOpt.isEmpty()) {
            return "redirect:/";
        }

        var event = eventOpt.get();

        model.addAttribute("event", event);

        return "event";
    }
    
    @GetMapping("sugestion/new")
    public String showSugestionForm(Model model) {
        // Envia uma nova instância vazia para o formulário HTML
        model.addAttribute("sugestion", new Sugestion());
        
        // Retorna a view do formulário (ajuste o caminho se estiver em outra pasta)
        return "event-sugestion-form";
    }

    @PostMapping("sugestion/save")
    public String saveSugestion(@ModelAttribute Sugestion sugestion, RedirectAttributes redirectAttributes) {
        System.out.println(sugestion);

        // O Hibernate cuidará automaticamente de gerar o UUID, createdAt e updatedAt
        sugestionRepository.save(sugestion);

        // Configura o Toast para feedback visual do usuário
        redirectAttributes.addFlashAttribute("toast", Map.of(
                "message", "Sugestão enviada com sucesso!",
                "statusCode", 201));

        // Redireciona para a Home ou para a página que preferir após o envio
        return "redirect:/"; 
    }
}
