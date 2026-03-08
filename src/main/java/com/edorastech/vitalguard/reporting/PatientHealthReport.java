package com.edorastech.vitalguard.reporting;

import com.edorastech.vitalguard.notification.NotificationType;
import com.edorastech.vitalguard.risk.RiskCategory;
import com.edorastech.vitalguard.trend.TrendClassification;

import java.time.LocalDateTime;
import java.util.Objects;

public final class PatientHealthReport {

    private final String patientId;
    private final RiskCategory riskCategory;
    private final TrendClassification trendClassification;
    private final NotificationType notificationType;
    private final OverallStatus overallStatus;
    private final LocalDateTime generatedAt;

    public PatientHealthReport(
            String patientId,
            RiskCategory riskCategory,
            TrendClassification trendClassification,
            NotificationType notificationType,
            OverallStatus overallStatus,
            LocalDateTime generatedAt) {

        this.patientId = Objects.requireNonNull(patientId);
        this.riskCategory = Objects.requireNonNull(riskCategory);
        this.trendClassification = Objects.requireNonNull(trendClassification);
        this.notificationType = Objects.requireNonNull(notificationType);
        this.overallStatus = Objects.requireNonNull(overallStatus);
        this.generatedAt = Objects.requireNonNull(generatedAt);
    }

    public String getPatientId() { return patientId; }
    public RiskCategory getRiskCategory() { return riskCategory; }
    public TrendClassification getTrendClassification() { return trendClassification; }
    public NotificationType getNotificationType() { return notificationType; }
    public OverallStatus getOverallStatus() { return overallStatus; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }

    public enum OverallStatus {
        CRITICAL,
        HIGH_RISK,
        MONITOR,
        STABLE
    }
}