package com.arena.app.scheduling.application.dto;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.List;
 
import java.util.UUID;

import java.util.List;

public class DashboardStatsDTO {
    private double mean;
    private double median;
    private double standardDeviation;
    private List<Integer> attendanceFlow; // Dados para o gráfico de linha

    // Getters e Setters
    public double getMean() { return mean; }
    public void setMean(double mean) { this.mean = mean; }
    public double getMedian() { return median; }
    public void setMedian(double median) { this.median = median; }
    public double getStandardDeviation() { return standardDeviation; }
    public void setStandardDeviation(double standardDeviation) { this.standardDeviation = standardDeviation; }
    public List<Integer> getAttendanceFlow() { return attendanceFlow; }
    public void setAttendanceFlow(List<Integer> attendanceFlow) { this.attendanceFlow = attendanceFlow; }
}