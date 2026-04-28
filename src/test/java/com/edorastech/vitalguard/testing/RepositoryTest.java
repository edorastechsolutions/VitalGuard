package com.edorastech.vitalguard.testing;

import com.edorastech.vitalguard.notification.NotificationType;
import com.edorastech.vitalguard.reporting.*;
import com.edorastech.vitalguard.repository.*;
import com.edorastech.vitalguard.risk.RiskCategory;
import com.edorastech.vitalguard.trend.TrendClassification;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {

    @Test
    void shouldSaveAndRetrieveReports() {

        PatientHealthReportRepository repo =
                new InMemoryPatientHealthReportRepository();

        PatientHealthReport report =
                new PatientHealthReport(
                        "P1",
                        RiskCategory.HIGH,
                        TrendClassification.CRITICAL,
                        NotificationType.EMERGENCY_ALERT,
                        PatientHealthReport.OverallStatus.CRITICAL,
                        LocalDateTime.now()
                );

        repo.save(report);

        List<PatientHealthReport> result =
                repo.findByPatientId("P1");

        assertEquals(1, result.size());
    }
}