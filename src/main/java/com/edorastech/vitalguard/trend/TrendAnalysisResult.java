package com.edorastech.vitalguard.trend;

import java.util.List;

public final class TrendAnalysisResult {

    private final String patientId;
    private final int totalRecords;
    private final int alertStreakMax;
    private final List<String> detectedPatterns;
    private final TrendClassification trendClassification;

    public TrendAnalysisResult(String patientId,
                               int totalRecords,
                               int alertStreakMax,
                               List<String> detectedPatterns,
                               TrendClassification trendClassification) {
        this.patientId = patientId;
        this.totalRecords = totalRecords;
        this.alertStreakMax = alertStreakMax;
        this.detectedPatterns = List.copyOf(detectedPatterns);
        this.trendClassification = trendClassification;
    }

    public String getPatientId() { return patientId; }

    public int getTotalRecords() { return totalRecords; }

    public int getAlertStreakMax() { return alertStreakMax; }

    public List<String> getDetectedPatterns() { return detectedPatterns; }

    public TrendClassification getTrendClassification() { return trendClassification; }
}