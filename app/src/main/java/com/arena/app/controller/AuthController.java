package com.arena.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getLogin() {
        return "auth/login.html";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute LoginUserDTO entity) {

        var userOpt = userRepository.findByEmail(entity.getEmail());

        if(userOpt.isEmpty()){
            return "/login";
        }

        var user = userOpt.get();

        if(!user.getPassword().equals(entity.getPassword())){
            return "/login";
        }

        return "redirect:/";
    }
    
    @GetMapping("/signup")
    public String getSignup() {
        return "auth/signup.html";
    }

    @PostMapping("/signup")
    public String postLogin(@ModelAttribute SignupUserDTO entity) {

        System.out.println(entity.toString());

        User newUser = new User(entity);

        User createUser = userRepository.save(newUser);

        if(createUser.getName().isEmpty()){
            return "/signup";
        }

        return "redirect:/";
    }
    
}
