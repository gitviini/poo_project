package com.arena.app.iam.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.iam.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class ProfileController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile/{userId}")
    public String getProfile(@PathVariable("userId") String userId, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request){
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");
        
        // Authorization check: User can only see their own profile, unless they are admin
        if (!authenticatedUser.getUserId().equals(userId) && !"admin".equals(authenticatedUser.getRole())) {
            return "redirect:/profile/" + authenticatedUser.getUserId();
        }

        var userOpt = userRepository.findByUserId(userId);

        if(userOpt.isEmpty()){
            // Configura o Toast para feedback visual do usuário
            redirectAttributes.addFlashAttribute("toast", Map.of(
                    "message", "Perfil não encontrado",
                    "statusCode", 500));
    
            // Redireciona para a Home ou para a página que preferir após o envio
            return "redirect:/";
        }

        // Extrai o objeto User do Optional
        var user = userOpt.get();

        // Passa o objeto para o Thymeleaf poder renderizar os dados na tela
        model.addAttribute("user", user);

        return "profile";
    }

    @PutMapping("/profile/{userId}")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@PathVariable("userId") String userId, @RequestBody User updatedUser, HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        // Authorization check: User can only edit their own profile, unless they are admin
        if (!authenticatedUser.getUserId().equals(userId) && !"admin".equals(authenticatedUser.getRole())) {
            return ResponseEntity.status(403).body(Map.of("message", "Acesso negado"));
        }

        var userOpt = userRepository.findByUserId(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Perfil não encontrado"));
        }

        var user = userOpt.get();

        // Atualiza apenas os campos permitidos
        if (updatedUser.getName() != null) user.setName(updatedUser.getName());
        if (updatedUser.getPhone() != null) user.setPhone(updatedUser.getPhone());
        if (updatedUser.getBio() != null) user.setBio(updatedUser.getBio());
        if (updatedUser.getImageBase64() != null) user.setImageBase64(updatedUser.getImageBase64());

        // A senha NUNCA é alterada por este endpoint
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Perfil atualizado com sucesso!"));
    }

    @DeleteMapping("/profile/{userId}")
    @ResponseBody
    public ResponseEntity<?> deleteProfile(@PathVariable("userId") String userId, HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        // Authorization check: User can only delete their own profile, unless they are admin
        if (!authenticatedUser.getUserId().equals(userId) && !"admin".equals(authenticatedUser.getRole())) {
            return ResponseEntity.status(403).body(Map.of("message", "Acesso negado"));
        }

        var userOpt = userRepository.findByUserId(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Perfil não encontrado"));
        }

        userRepository.delete(userOpt.get());

        return ResponseEntity.ok(Map.of("message", "Conta excluída com sucesso!"));
    }
}
