package com.arena.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.arena.app.repository.UserRepository;


@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String getHome(Model model, RedirectAttributes redirectAttributes) {

        // placeholder when session system is not implemented
        var userOpt = userRepository.findByUserId("gvinicius105");

        if(userOpt.isEmpty()){
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Perfil não encontrado",
                    "statusCode", 500));
            return "redirect:/login";
        }

        var user = userOpt.get();

        /*
         * --- EXAMPLE: CREATION TOAST ---
         * 
         * model.addAttribute("toast", Map.of(
         * "message", "Operação realizada com sucesso!",
         * "statusCode", 200));
         */

        model.addAttribute("user", user);

        return "home";
    }
}
