package com.arena.app.notification.application.service;

import com.arena.app.core.domain.event.VisitVacatedEvent;
import com.arena.app.notification.domain.model.Notification;
import com.arena.app.notification.domain.model.WaitlistEntry;
import com.arena.app.notification.domain.repository.NotificationRepository;
import com.arena.app.notification.domain.repository.WaitlistEntryRepository;
import com.arena.app.scheduling.domain.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitlistService {

    @Autowired
    private WaitlistEntryRepository waitlistEntryRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @EventListener
    public void handleVisitVacatedEvent(VisitVacatedEvent event) {
        notifyWaitlistForVisit(event.getVisit());
    }

    public void notifyWaitlistForVisit(Visit visit) {
        List<WaitlistEntry> entries = waitlistEntryRepository.findByVisitId(visit.getId());
        for (WaitlistEntry entry : entries) {
            String message = String.format("Novas vagas estão disponíveis para a visita em %s às %s! Reserve agora: /scheduled-visit/new?visitId=%s", 
                visit.getDate(), visit.getTime(), visit.getId());
            
            Notification notification = Notification.createNew(entry.getUserId(), message);
            notificationRepository.save(notification);
            
            waitlistEntryRepository.delete(entry);
        }
    }
}
