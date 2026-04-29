/* package com.arena.app.controller;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
// Mudei o nome da classe para CustomErrorController para não confundir com a interface do Spring
public class CustomErrorController implements ErrorController { 

    // O RequestMapping pega TODOS os tipos de erros, seja de POST, GET, PUT...
    @RequestMapping("/error")
    public String handleError() {
        
        // Redireciona silenciosamente qualquer usuário perdido de volta para a Home
        return "redirect:/";
    }
} */