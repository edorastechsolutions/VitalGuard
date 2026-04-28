package com.edorastech.vitalguard.testing;

import com.edorastech.vitalguard.notification.*;
import com.edorastech.vitalguard.reporting.*;
import com.edorastech.vitalguard.risk.*;
import com.edorastech.vitalguard.trend.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientHealthReportAggregatorTest {

    @Test
    void shouldAggregateReport() {

        PatientHealthReportAggregator aggregator =
                new PatientHealthReportAggregator();

        RiskEvaluationResult risk =
                new RiskEvaluationResult("P1", 80, RiskCategory.HIGH);

        TrendAnalysisResult trend =
                new TrendAnalysisResult(
                        "P1",
                        5,
                        3,
                        List.of("ALERT_STREAK"),
                        TrendClassification.CRITICAL
                );

        NotificationResult notification =
                new NotificationResult(
                        "P1",
                        NotificationType.EMERGENCY_ALERT,
                        "Critical condition detected",
                        LocalDateTime.now()
                );

        PatientHealthReport report =
                aggregator.aggregate(risk, trend, notification);

        assertNotNull(report);
        assertEquals("P1", report.getPatientId());
    }
}