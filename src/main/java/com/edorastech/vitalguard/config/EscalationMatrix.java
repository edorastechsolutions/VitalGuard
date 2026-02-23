package com.edorastech.vitalguard.config;

import com.edorastech.vitalguard.notification.NotificationType;
import com.edorastech.vitalguard.risk.RiskCategory;
import com.edorastech.vitalguard.trend.TrendClassification;

public final class EscalationMatrix {

    private EscalationMatrix() {}

    public static NotificationType resolve(RiskCategory risk,
                                           TrendClassification trend) {

        if (trend == TrendClassification.RECOVERING) {
            return NotificationType.NONE;
        }

        if (risk == RiskCategory.HIGH) {
            return NotificationType.EMERGENCY_ALERT;
        }

        if (risk == RiskCategory.MODERATE &&
            trend == TrendClassification.DETERIORATING) {
            return NotificationType.ESCALATED_ALERT;
        }

        if (risk == RiskCategory.LOW &&
            trend == TrendClassification.CRITICAL) {
            return NotificationType.ESCALATED_ALERT;
        }

        return NotificationType.STANDARD_ALERT;
    }
}