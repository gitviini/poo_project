package com.arena.app.config;

import com.arena.app.model.User;
import com.arena.app.repository.UserRepository;
import com.arena.app.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public void run(String... args) throws Exception {
        // Define default admin credentials
        String adminUserId = "admin";
        String adminPassword = "admin123";
        String adminEmail = "admin@arena.com";

        // Check if admin already exists
        if (userRepository.findByUserId(adminUserId).isEmpty()) {
            User admin = new User();
            admin.setUserId(adminUserId);
            admin.setName("Administrator");
            admin.setEmail(adminEmail);
            admin.setPassword(tokenService.hashPassword(adminPassword));
            admin.setRole("admin");
            admin.setPhone("000000000");
            admin.setBio("System Administrator");

            userRepository.save(admin);
            System.out.println(">>> Default admin user created (ID: admin, Pass: admin123)");
        } else {
            System.out.println(">>> Admin user already exists. Skipping initialization.");
        }
    }
}
