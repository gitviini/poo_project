package com.arena.app.core.application.service;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;
import com.arena.app.iam.domain.repository.UserRepository;
import com.arena.app.scheduling.domain.repository.EventRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Faz o backup do banco de dados H2.
 Usa o comando nativo do H2 "BACKUP TO '<arquivo>.zip'", que gera uma copia
  binaria e consistente de todo o banco em um unico arquivo .zip. Os backups
  ficam na pasta configurada em app.backup.dir e a quantidade e limitada por
  app.backup.retention (mantem apenas os mais recentes).
 */
@Service
public class BackupService {

    private final JdbcTemplate jdbcTemplate;

    @Value("${app.backup.dir:./data/backups}")
    private String backupDir;

    @Value("${app.backup.retention:5}")
    private int retention;

    public BackupService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Cria um backup com data/hora no nome e apaga os mais antigos.
     *
     * @return o caminho do arquivo de backup gerado.
     */
    public Path createBackup() throws IOException {
        Path dir = Paths.get(backupDir);
        Files.createDirectories(dir);

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        Path target = dir.resolve("backup-" + timestamp + ".zip").toAbsolutePath();

        // BACKUP TO gera um .zip com os arquivos do banco. As aspas simples sao
        // escapadas para evitar problemas no comando SQL.
        jdbcTemplate.execute("BACKUP TO '" + target.toString().replace("'", "''") + "'");

        pruneOldBackups(dir);
        return target;
    }

    /** Mantem apenas os {@code retention} backups mais recentes. */
    private void pruneOldBackups(Path dir) throws IOException {
        try (Stream<Path> files = Files.list(dir)) {
            List<Path> backups = files
                    .filter(p -> {
                        String name = p.getFileName().toString();
                        return name.startsWith("backup-") && name.endsWith(".zip");
                    })
                    // Nome contem a data/hora, entao ordem reversa = mais novo primeiro.
                    .sorted(Comparator.comparing(Path::getFileName).reversed())
                    .toList();

            for (int i = retention; i < backups.size(); i++) {
                Files.deleteIfExists(backups.get(i));
            }
        }
    }
}
