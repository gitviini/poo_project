package com.arena.app.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class Home {

    @GetMapping("/")
    public String getHome() {

        /*
         * --- EXAMPLE: CREATION TOAST ---
         * 
         * model.addAttribute("toast", Map.of(
         * "message", "Operação realizada com sucesso!",
         * "statusCode", 200));
         */

        return "home";
    }
}
