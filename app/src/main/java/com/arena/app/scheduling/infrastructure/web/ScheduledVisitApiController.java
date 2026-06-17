package com.arena.app.scheduling.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.iam.domain.repository.UserRepository;
import com.arena.app.scheduling.domain.model.ScheduledVisit;
import com.arena.app.scheduling.domain.repository.ScheduledVisitRepository;
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

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getScheduledVisitsByUserId(@RequestParam("userId") String userId, HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");
        if (!authenticatedUser.getUserId().equals(userId) && !"admin".equals(authenticatedUser.getRole())) {
            return ResponseEntity.status(403).body(Map.of("message", "Acesso negado"));
        }

        var userOpt = userRepository.findByUserId(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Usuário não encontrado"));
        }

        List<ScheduledVisit> visits = scheduledVisitRepository.findByUserId(userOpt.get().getId());
        return ResponseEntity.ok(visits);
    }
}
