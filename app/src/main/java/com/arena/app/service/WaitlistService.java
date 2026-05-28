package com.arena.app.service;

import com.arena.app.model.*;
import com.arena.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitlistService {

    @Autowired
    private WaitlistEntryRepository waitlistEntryRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public void notifyWaitlistForEvent(Event event) {
        List<WaitlistEntry> entries = waitlistEntryRepository.findByEventOrderByCreatedAtAsc(event);
        for (WaitlistEntry entry : entries) {
            Notification notification = new Notification();
            notification.setUser(entry.getUser());
            notification.setMessage("Novos ingressos estão disponíveis para o evento: " + event.getTitle() + "!");
            notificationRepository.save(notification);
            
            // We can keep them in the waitlist or remove them. 
            // The prompt says "be notified if new tickets become available".
            // To prevent spamming, we can delete the entry after notifying once.
            waitlistEntryRepository.delete(entry);
        }
    }

    public void notifyWaitlistForVisit(Visit visit) {
        List<WaitlistEntry> entries = waitlistEntryRepository.findByVisitOrderByCreatedAtAsc(visit);
        for (WaitlistEntry entry : entries) {
            Notification notification = new Notification();
            notification.setUser(entry.getUser());
            notification.setMessage("Novas vagas estão disponíveis para a visita em " + visit.getDate() + " às " + visit.getTime() + "!");
            notificationRepository.save(notification);
            
            waitlistEntryRepository.delete(entry);
        }
    }
}
