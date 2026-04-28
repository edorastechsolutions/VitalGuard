package com.edorastech.vitalguard.testing;

import com.edorastech.vitalguard.reporting.*;
import com.edorastech.vitalguard.repository.*;
import com.edorastech.vitalguard.risk.*;
import com.edorastech.vitalguard.trend.*;
import com.edorastech.vitalguard.notification.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    @Test
    void shouldThrowOnNullInputs() {
    assertThrows(NullPointerException.class, () ->
    new PatientHealthReport(null, null, null, null, null, null));
}

    @Test
    void shouldAggregateAndStoreReport() {

        PatientHealthReportAggregator aggregator = new PatientHealthReportAggregator();
        PatientHealthReportRepository repo = new InMemoryPatientHealthReportRepository();

        RiskEvaluationResult risk =
                new RiskEvaluationResult("P1", 90, RiskCategory.HIGH);

        TrendAnalysisResult trend =
                new TrendAnalysisResult("P1", 5, 3, List.of("SPIKE"), TrendClassification.CRITICAL);

        NotificationResult notification =
                new NotificationResult("P1", NotificationType.EMERGENCY_ALERT, "Critical", LocalDateTime.now());

        PatientHealthReport report = aggregator.aggregate(risk, trend, notification);

        repo.save(report);

        List<PatientHealthReport> results = repo.findByPatientId("P1");

        assertEquals(1, results.size());
        assertEquals("P1", results.get(0).getPatientId());
        
    }
}