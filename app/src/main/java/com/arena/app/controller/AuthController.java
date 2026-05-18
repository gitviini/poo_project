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

import com.arena.app.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @GetMapping("/login")
    public String getLogin(Model model) {
        return "auth/login";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute LoginUserDTO entity, RedirectAttributes redirectAttributes, HttpServletResponse response) {

        var userOpt = userRepository.findByUserId(entity.getUserId());

        if(userOpt.isEmpty() || !tokenService.checkPassword(entity.getPassword(), userOpt.get().getPassword())){
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Login ou senha incorretos.",
                    "statusCode", 403));
            return "redirect:/login";
        }

        var user = userOpt.get();

        // Generate Token and Set Cookie
        String token = tokenService.generateToken(user.getUserId(), user.getRole());
        Cookie cookie = new Cookie("auth_token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400); // 1 day
        response.addCookie(cookie);

        return "redirect:/";
    }
    
    @GetMapping("/signup")
    public String getSignup() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String postSignup(@ModelAttribute SignupUserDTO entity, RedirectAttributes redirectAttributes) {

        if(userRepository.existsByEmail(entity.getEmail()) || userRepository.existsByUserId(entity.getUserId())){
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "E-mail ou ID de usuário já cadastrado.",
                    "statusCode", 402));
            return "redirect:/signup";
        }

        User newUser = new User(entity);
        // Hash the password
        newUser.setPassword(tokenService.hashPassword(entity.getPassword()));

        User createUser = userRepository.save(newUser);

        if(createUser.getName().isEmpty()){
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Falha ao criar usuário. Tente novamente.",
                    "statusCode", 500));
            return "redirect:/signup";
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth_token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
