package com.arena.app.scheduling.application.service;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;
import com.arena.app.scheduling.domain.model.*;
import com.arena.app.scheduling.domain.repository.*;
import com.arena.app.iam.domain.model.User;

import com.arena.app.scheduling.application.dto.DashboardStatsDTO;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    public DashboardStatsDTO calculateStats(List<Event> events) {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        if (events.isEmpty()) return stats;

        List<Integer> attendance = events.stream()
                .map(e -> (int)(e.getPrice() * 10)) // Simulação de público baseada no preço
                .collect(Collectors.toList());

        // Média
        double mean = attendance.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        
        // Mediana
        Collections.sort(attendance);
        double median = (attendance.size() % 2 == 0) ? 
            (attendance.get(attendance.size()/2) + attendance.get(attendance.size()/2 - 1)) / 2.0 :
            attendance.get(attendance.size()/2);

        // Desvio Padrão (Dispersão)
        double variance = attendance.stream()
                .mapToDouble(a -> Math.pow(a - mean, 2))
                .sum() / attendance.size();
        double stdDev = Math.sqrt(variance);

        stats.setMean(mean);
        stats.setMedian(median);
        stats.setStandardDeviation(stdDev);
        stats.setAttendanceFlow(attendance);
        
        return stats;
    }
}