package com.arena.app.notification.infrastructure.web;

import com.arena.app.iam.domain.model.User;
import com.arena.app.notification.domain.model.Notification;
import com.arena.app.notification.domain.repository.NotificationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        return ResponseEntity.ok(notificationRepository.findByUserId(user.getId()));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        long count = notificationRepository.countUnreadByUserId(user.getId());
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable UUID id, HttpServletRequest request) {
        User user = (User) request.getAttribute("authenticatedUser");
        var notificationOpt = notificationRepository.findById(id);

        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            if (notification.getUserId().equals(user.getId())) {
                notification.markAsRead();
                notificationRepository.save(notification);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(403).build();
    }
}
