package com.arena.app.controller;

import com.arena.app.model.ScheduledVisit;
import com.arena.app.model.User;
import com.arena.app.repository.ScheduledVisitRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scheduled-visits")
public class ScheduledVisitApiController {

    @Autowired
    private ScheduledVisitRepository scheduledVisitRepository;

    @GetMapping
    public ResponseEntity<?> getScheduledVisitsByUserId(@RequestParam("userId") String userId, HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");
        if (!authenticatedUser.getUserId().equals(userId) && !"admin".equals(authenticatedUser.getRole())) {
            return ResponseEntity.status(403).body(Map.of("message", "Acesso negado"));
        }

        List<ScheduledVisit> visits = scheduledVisitRepository.findByUserUserId(userId);
        return ResponseEntity.ok(visits);
    }
}
