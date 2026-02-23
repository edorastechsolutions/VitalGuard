package com.edorastech.vitalguard.trend;

import com.edorastech.vitalguard.audit.AuditRecord;
import com.edorastech.vitalguard.trend.patterns.AlertStreakDetector;
import com.edorastech.vitalguard.trend.patterns.RecoveryDetector;

import java.util.*;

public class VitalsTrendAnalyzer {

    public TrendAnalysisResult analyze(String patientId,
                                       List<AuditRecord> records) {

        if (records == null || records.isEmpty()) {
            return new TrendAnalysisResult(
                    patientId,
                    0,
                    0,
                    List.of(),
                    TrendClassification.STABLE
            );
        }

        // Ensure chronological order
        records.sort(Comparator.comparing(AuditRecord::getTimestamp));

        AlertStreakDetector streakDetector = new AlertStreakDetector();
        RecoveryDetector recoveryDetector = new RecoveryDetector();

        List<TrendPatternDetector> detectors = List.of(
                streakDetector,
                recoveryDetector
        );

        List<String> detectedPatterns = new ArrayList<>();

        boolean criticalDetected = false;
        boolean recoveryDetected = false;

        for (TrendPatternDetector detector : detectors) {

            boolean detected = detector.detect(records);

            if (detected) {
                detectedPatterns.add(detector.getPatternName());

                if (detector instanceof AlertStreakDetector) {
                    criticalDetected = true;
                }

                if (detector instanceof RecoveryDetector) {
                    recoveryDetected = true;
                }
            }
        }

        TrendClassification classification =
                determineClassification(criticalDetected, recoveryDetected);

        return new TrendAnalysisResult(
                patientId,
                records.size(),
                streakDetector.getMaxStreak(),
                detectedPatterns,
                classification
        );
    }

    private TrendClassification determineClassification(
            boolean critical,
            boolean recovering) {

        if (critical) return TrendClassification.CRITICAL;
        if (recovering) return TrendClassification.RECOVERING;

        return TrendClassification.DETERIORATING;
    }
}