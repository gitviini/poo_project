package com.arena.app.service;

import com.arena.app.model.Event;
import com.arena.app.dto.DashboardStatsDTO;
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