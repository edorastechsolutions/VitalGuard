package com.edorastech.vitalguard.reporting;

import com.edorastech.vitalguard.notification.NotificationResult;
import com.edorastech.vitalguard.notification.NotificationType;
import com.edorastech.vitalguard.risk.RiskEvaluationResult;
import com.edorastech.vitalguard.trend.TrendAnalysisResult;

import java.time.LocalDateTime;
import java.util.Objects;

public class PatientHealthReportAggregator {

    public PatientHealthReport aggregate(
            RiskEvaluationResult riskResult,
            TrendAnalysisResult trendResult,
            NotificationResult notificationResult) {

        Objects.requireNonNull(riskResult);
        Objects.requireNonNull(trendResult);
        Objects.requireNonNull(notificationResult);

        PatientHealthReport.OverallStatus overallStatus =
                deriveOverallStatus(notificationResult.getNotificationType());

        return new PatientHealthReport(
                riskResult.getPatientId(),
                riskResult.getRiskCategory(),
                trendResult.getTrendClassification(),
                notificationResult.getNotificationType(),
                overallStatus,
                LocalDateTime.now()
        );
    }

    private PatientHealthReport.OverallStatus deriveOverallStatus(
            NotificationType notificationType) {

        return switch (notificationType) {
            case EMERGENCY_ALERT -> PatientHealthReport.OverallStatus.CRITICAL;
            case ESCALATED_ALERT -> PatientHealthReport.OverallStatus.HIGH_RISK;
            case STANDARD_ALERT -> PatientHealthReport.OverallStatus.MONITOR;
            case NONE -> PatientHealthReport.OverallStatus.STABLE;
        };
    }
}