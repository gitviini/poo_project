package com.arena.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.arena.app.model.User;
import com.arena.app.repository.UserRepository;

@Controller
public class ProfileController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/profile/{email}")
    public String getProfile(@PathVariable("email") String email, Model model, RedirectAttributes redirectAttributes){
        var userOpt = userRepository.findByEmail(email);

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
        System.out.println(user);

        // Passa o objeto para o Thymeleaf poder renderizar os dados na tela
        model.addAttribute("user", user);

        return "profile";
    }

    @PutMapping("/profile/{email}")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@PathVariable("email") String email, @RequestBody User updatedUser) {
        var userOpt = userRepository.findByEmail(email);

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
}
