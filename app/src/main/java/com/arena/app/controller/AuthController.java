package com.arena.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.arena.app.dto.LoginUserDTO;
import com.arena.app.dto.SignupUserDTO;
import com.arena.app.model.User;
import com.arena.app.repository.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller("")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String getLogin(Model model) {
        return "auth/login";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute LoginUserDTO entity, RedirectAttributes redirectAttributes) {

        var userOpt = userRepository.findByEmail(entity.getEmail());

        if(userOpt.isEmpty()){
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Usuário não encontrado.",
                    "statusCode", 500));
            return "redirect:/login";
        }

        var user = userOpt.get();

        if(!user.getPassword().equals(entity.getPassword())){
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Login ou senha incorretos.",
                    "statusCode", 403));
            return "redirect:/login";
        }

        return "redirect:/";
    }
    
    @GetMapping("/signup")
    public String getSignup() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String postLogin(@ModelAttribute SignupUserDTO entity, RedirectAttributes redirectAttributes) {

        if(userRepository.existsByEmail(entity.getEmail())){
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "E-mail já cadastrado.",
                    "statusCode", 402));
            return "redirect:/signup";
        }

        User newUser = new User(entity);

        User createUser = userRepository.save(newUser);

        if(createUser.getName().isEmpty()){
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Falha ao criar usuário. Tente novamente.",
                    "statusCode", 500));
            return "redirect:/signup";
        }

        return "redirect:/";
    }
    
}
