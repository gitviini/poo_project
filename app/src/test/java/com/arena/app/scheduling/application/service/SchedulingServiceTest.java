package com.arena.app.scheduling.application.service;

import com.arena.app.scheduling.domain.model.ScheduledVisit;
import com.arena.app.iam.domain.model.User;
import com.arena.app.scheduling.domain.model.Visit;
import com.arena.app.scheduling.domain.repository.ScheduledVisitRepository;
import com.arena.app.scheduling.domain.repository.VisitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceTest {

    @Mock
    private ScheduledVisitRepository repository;

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private SchedulingService service;

    @Test
    @DisplayName("Deve agendar com sucesso quando houver capacidade")
    void shouldScheduleSuccessfullyWhenCapacityAvailable() {
        // Arrange
        UUID visitId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Visit visit = new Visit();
        visit.setId(visitId);
        visit.setCapacity(10);

        ScheduledVisit newBooking = new ScheduledVisit();
        newBooking.setVisitId(visitId);
        newBooking.setUserId(userId);
        newBooking.setNumberOfPeople(2);

        when(repository.findByUserIdAndVisitId(userId, visitId)).thenReturn(Optional.empty());
        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(repository.countBookedPeopleByVisitId(visitId)).thenReturn(5);
        when(repository.save(any())).thenReturn(newBooking);

        // Act
        ScheduledVisit result = service.scheduleVisit(newBooking);

        // Assert
        assertEquals("CONFIRMADO", newBooking.getStatus());
        verify(repository, times(1)).save(newBooking);
    }

    @Test
    @DisplayName("Deve falhar ao agendar quando a capacidade for atingida")
    void shouldFailWhenCapacityExceeded() {
        // Arrange
        UUID visitId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Visit visit = new Visit();
        visit.setId(visitId);
        visit.setCapacity(10);
        
        ScheduledVisit newBooking = new ScheduledVisit();
        newBooking.setVisitId(visitId);
        newBooking.setUserId(userId);
        newBooking.setNumberOfPeople(1);

        when(repository.findByUserIdAndVisitId(userId, visitId)).thenReturn(Optional.empty());
        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(repository.countBookedPeopleByVisitId(visitId)).thenReturn(10);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.scheduleVisit(newBooking));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar ao agendar duplicado para o mesmo usuário e visita")
    void shouldFailWhenDuplicateBooking() {
        // Arrange
        UUID visitId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        ScheduledVisit newBooking = new ScheduledVisit();
        newBooking.setVisitId(visitId);
        newBooking.setUserId(userId);

        when(repository.findByUserIdAndVisitId(userId, visitId)).thenReturn(Optional.of(new ScheduledVisit()));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.scheduleVisit(newBooking));
        verify(repository, never()).save(any());
    }
}
