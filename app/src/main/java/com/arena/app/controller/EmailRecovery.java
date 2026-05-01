package com.arena.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailRecovery {
    @GetMapping("/forgot-password")
    public String mostrarPaginaRecuperacaoSenha(Model model) {
        model.addAttribute("sucesso", false);
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processarFormulario(@RequestParam String email, Model model) {

        System.out.println("Email recebido: " + email);
        model.addAttribute("sucesso", true);
        return "forgot-password";
    }
}
