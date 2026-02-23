package com.edorastech.vitalguard.validation;

import com.edorastech.vitalguard.notification.NotificationResult;
import com.edorastech.vitalguard.risk.RiskEvaluationResult;
import com.edorastech.vitalguard.trend.TrendAnalysisResult;

public class SystemIntegrityGuard {

    public void validate(RiskEvaluationResult risk,
                         TrendAnalysisResult trend) {

        if (!risk.getPatientId().equals(trend.getPatientId())) {
            throw new DomainValidationException(
                    "Risk and Trend results belong to different patients."
            );
        }
    }

    public void validateNotification(NotificationResult notification,
                                     String expectedPatientId) {

        if (!notification.getPatientId().equals(expectedPatientId)) {
            throw new DomainValidationException(
                    "Notification generated for incorrect patient."
            );
        }
    }
}