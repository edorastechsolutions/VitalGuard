package com.edorastech.vitalguard.notification;

import com.edorastech.vitalguard.config.EscalationMatrix;
import com.edorastech.vitalguard.risk.RiskEvaluationResult;
import com.edorastech.vitalguard.trend.TrendAnalysisResult;

import java.time.LocalDateTime;

public class AlertNotificationEngine {

    public NotificationResult generateNotification(
            RiskEvaluationResult risk,
            TrendAnalysisResult trend) {

        NotificationType type =
                EscalationMatrix.resolve(
                        risk.getRiskCategory(),
                        trend.getTrendClassification()
                );

        String message = buildMessage(type, risk, trend);

        return new NotificationResult(
                risk.getPatientId(),
                type,
                message,
                LocalDateTime.now()
        );
    }

    private String buildMessage(NotificationType type,
                                RiskEvaluationResult risk,
                                TrendAnalysisResult trend) {

        return switch (type) {

            case EMERGENCY_ALERT ->
                    "CRITICAL CONDITION: Immediate medical attention required.";

            case ESCALATED_ALERT ->
                    "Condition worsening. Escalation required.";

            case STANDARD_ALERT ->
                    "Vitals outside optimal range. Monitoring advised.";

            case NONE ->
                    "Patient condition recovering. No escalation needed.";
        };
    }
}