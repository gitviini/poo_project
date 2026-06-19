package com.arena.app.iam.infrastructure.web;

import com.arena.app.iam.application.dto.LoginUserDTO;
import com.arena.app.iam.application.dto.SignupUserDTO;
import com.arena.app.iam.application.service.LoginAttemptService;
import com.arena.app.iam.application.service.TokenService;
import com.arena.app.iam.domain.model.User;
import com.arena.app.iam.domain.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @GetMapping("/login")
    public String getLogin(Model model) {
        return "auth/login";
    }

    @PostMapping("/login")
    public String postLogin(@Valid @ModelAttribute LoginUserDTO entity, BindingResult result, RedirectAttributes redirectAttributes, HttpServletResponse response) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", result.getAllErrors().get(0).getDefaultMessage(),
                    "statusCode", 400));
            return "redirect:/login";
        }

        String login = entity.getUserId();

        // Protecao contra forca bruta: bloqueio temporario apos muitas falhas.
        if (loginAttemptService.isBlocked(login)) {
            long mins = loginAttemptService.minutesRemaining(login);
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Muitas tentativas de login. Tente novamente em " + mins + " min.",
                    "statusCode", 429));
            return "redirect:/login";
        }

        var userOpt = userRepository.findByUserId(login);

        if(userOpt.isEmpty() || !tokenService.checkPassword(entity.getPassword(), userOpt.get().getPassword())){
            loginAttemptService.loginFailed(login);
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Login ou senha incorretos.",
                    "statusCode", 403));
            return "redirect:/login";
        }

        loginAttemptService.loginSucceeded(login);
        var user = userOpt.get();

        // Generate Token and Set Cookie
        String token = tokenService.generateToken(user.getUserId(), user.getRole());
        Cookie cookie = new Cookie("auth_token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        // Validade do cookie alinhada a expiracao do token JWT (2h), para nao
        // deixar um cookie vivo depois que o token ja expirou.
        cookie.setMaxAge(7200); // 2 horas
        response.addCookie(cookie);

        return "redirect:/";
    }
    
    @GetMapping("/signup")
    public String getSignup() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String postSignup(@Valid @ModelAttribute SignupUserDTO entity, BindingResult result, RedirectAttributes redirectAttributes) {

        // Validacao de entrada: campos obrigatorios, e-mail valido, forca da senha
        // e consentimento LGPD (ver SignupUserDTO).
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", result.getAllErrors().get(0).getDefaultMessage(),
                    "statusCode", 400));
            return "redirect:/signup";
        }

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
