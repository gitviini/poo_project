package com.arena.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

/**
 * Dispara o backup automatico do banco em horario agendado.
 *
 * O horario vem da propriedade app.backup.cron (por padrao, todo dia as 03:00).
 */
@Component
public class BackupScheduler {

    private static final Logger log = LoggerFactory.getLogger(BackupScheduler.class);

    private final BackupService backupService;

    public BackupScheduler(BackupService backupService) {
        this.backupService = backupService;
    }

    // Cron do Spring (6 campos): segundo minuto hora dia mes dia-da-semana.
    @Scheduled(cron = "${app.backup.cron}")
    public void scheduledBackup() {
        try {
            Path path = backupService.createBackup();
            log.info("Backup automatico criado: {}", path);
        } catch (Exception e) {
            log.error("Falha ao criar backup automatico", e);
        }
    }
}
