package com.edorastech.vitalguard;

import com.edorastech.vitalguard.audit.*;
import com.edorastech.vitalguard.model.*;
import com.edorastech.vitalguard.monitoring.*;
import com.edorastech.vitalguard.notification.*;
import com.edorastech.vitalguard.reporting.*;
import com.edorastech.vitalguard.repository.*;
import com.edorastech.vitalguard.risk.*;
import com.edorastech.vitalguard.security.*;
import com.edorastech.vitalguard.trend.*;
import com.edorastech.vitalguard.validation.*;

import java.time.LocalDateTime;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final PatientHealthReportRepository reportRepository =
            new InMemoryPatientHealthReportRepository();

    // Module 9 - Rate Limiter (shared instance)
    private static final RateLimiter rateLimiter = new RateLimiter();

    // Module 10 - Brute Force Protection
    private static final BruteForceProtectionService bruteForceService =
            new BruteForceProtectionService();

    // Module 11 - Central Audit Logger
    private static final CentralAuditLogger auditLogger =
            new CentralAuditLogger();

    public static void main(String[] args) {

        printSystemHeader();

        // Module 8 - Request Trace
        String requestId = new RequestIdGenerator().generate();
        RequestContext.setRequestId(requestId);

        System.out.println("Request ID : " + requestId);

        String patientId = null;

        try {
             // ================= INPUT =================
            System.out.print("Patient ID: ");
            patientId = scanner.nextLine().trim();

            // Module 9 - Rate Limiting
            if (!rateLimiter.allowRequest(patientId)) {
                auditLogger.log(requestId, patientId, "INPUT", AuditStatus.FAIL, "RATE_LIMIT_EXCEEDED");
                System.out.println("❌ ERROR: Too many requests (HTTP 429)");
                return;
            }

            // Module 10 - Brute Force Lock Check
System.out.print("Enter Patient ID: ");
String patientId1 = scanner.nextLine().trim();
System.out.print("Enter Password: ");
String password = scanner.nextLine().trim();

// Check lock
if (bruteForceService.isLocked(patientId1)) {
    System.out.println("❌ Account locked due to multiple failed attempts. Try later.");
    return;
}

// Simulated password (for demo)
String correctPassword = "admin123";

if (!password.equals(correctPassword)) {

    bruteForceService.recordFailure(patientId1);

    System.out.println("❌ Invalid credentials.");
    System.out.println("Remaining Attempts: " +
            bruteForceService.getRemainingAttempts(patientId1));

    return;
}

// Success
bruteForceService.recordSuccess(patientId1);
System.out.println("✅ Login successful.\n");


            double heartRate = readDouble("Heart Rate (bpm): ");
            double systolic = readDouble("Systolic BP: ");
            double diastolic = readDouble("Diastolic BP: ");
            double temperature = readDouble("Body Temperature (°C): ");
            double oxygen = readDouble("Oxygen Saturation (%): ");

            printVitalsPanel(patientId1, heartRate, systolic, diastolic, temperature, oxygen);

            // ================= DOMAIN PROCESS =================

            List<VitalAbnormality> abnormalities = new ArrayList<>();

            if (heartRate < 60 || heartRate > 100)
                abnormalities.add(new VitalAbnormality("Heart Rate", "Out of Range", SeverityLevel.MODERATE));

            if (systolic > 140)
                abnormalities.add(new VitalAbnormality("Systolic BP", "High", SeverityLevel.MODERATE));

            if (temperature > 38)
                abnormalities.add(new VitalAbnormality("Temperature", "Fever", SeverityLevel.MILD));

            if (oxygen < 95)
                abnormalities.add(new VitalAbnormality("Oxygen Saturation", "Low", SeverityLevel.SEVERE));

            OverallStatus status =
                    abnormalities.isEmpty() ? OverallStatus.NORMAL : OverallStatus.ALERT;

            EvaluatedVitals evaluatedVitals =
                    new EvaluatedVitals(
                            status,
                            heartRate,
                            systolic,
                            diastolic,
                            temperature,
                            oxygen,
                            abnormalities
                    );

            // Risk
            RiskEvaluationResult riskResult =
                    new RiskScoringEngine().calculateRisk(patientId1, evaluatedVitals);

            // Audit Trail (Module 11)
            auditLogger.log(requestId, patientId1, "RISK_ENGINE", AuditStatus.SUCCESS, null);

            // Trend
            VitalsAuditLogger vitalsAuditLogger = new VitalsAuditLogger();
            vitalsAuditLogger.log(patientId1, status, extractParams(abnormalities));

            TrendAnalysisResult trendResult =
                    new VitalsTrendAnalyzer().analyze(
                            patientId1,
                            vitalsAuditLogger.getHistory(patientId1)
                    );

            // Validation (Module 12 Fail-Closed)
            SystemIntegrityGuard guard = new SystemIntegrityGuard();
            guard.validate(riskResult, trendResult);

            // Notification
            NotificationResult notification =
                    new AlertNotificationEngine().generateNotification(riskResult, trendResult);

            guard.validateNotification(notification, patientId1);

            // Aggregation (Module 6)
            PatientHealthReport report =
                    new PatientHealthReportAggregator()
                            .aggregate(riskResult, trendResult, notification);

            // Repository (Module 7)
            reportRepository.save(report);

            // ================= SUCCESS =================

            bruteForceService.recordSuccess(patientId1);

            auditLogger.log(requestId, patientId1, "PROCESS_COMPLETE", AuditStatus.SUCCESS, null);

            printFinalReport(evaluatedVitals, riskResult, trendResult, notification, report);
            printPatientHistory(patientId1);

        }
        catch (DomainValidationException ex) {

            bruteForceService.recordFailure(patientId);
            auditLogger.log(requestId, patientId, "VALIDATION", AuditStatus.FAIL, ex.getMessage());

            System.out.println("❌ DOMAIN ERROR: " + ex.getMessage());

        }
        catch (InputMismatchException ex) {

            bruteForceService.recordFailure(patientId);
            auditLogger.log(requestId, patientId, "INPUT", AuditStatus.FAIL, "INVALID_INPUT");

            System.out.println("❌ Invalid numeric input.");

        }
        catch (Exception ex) {

            bruteForceService.recordFailure(patientId);
            auditLogger.log(requestId, patientId, "SYSTEM", AuditStatus.FAIL, ex.getMessage());

            GlobalExceptionHandler.handle(ex);

        }
        finally {
            RequestContext.clear();
            scanner.close();
        }
    }

    // ================= UTIL METHODS =================

    private static List<String> extractParams(List<VitalAbnormality> abnormalities) {
        List<String> list = new ArrayList<>();
        for (VitalAbnormality a : abnormalities) {
            list.add(a.getParameter());
        }
        return list;
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        return scanner.nextDouble();
    }

    private static void printSystemHeader() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       VITALGUARD - HEALTH MONITORING SYSTEM                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }

    private static void printVitalsPanel(
            String patientId,
            double hr, double sys, double dia, double temp, double oxy) {

        System.out.println("\n---------------- PATIENT VITAL DATA ----------------");

        System.out.printf("%-20s : %s%n", "Patient ID", patientId);
        System.out.printf("%-20s : %.1f bpm%n", "Heart Rate", hr);
        System.out.printf("%-20s : %.1f mmHg%n", "Systolic BP", sys);
        System.out.printf("%-20s : %.1f mmHg%n", "Diastolic BP", dia);
        System.out.printf("%-20s : %.1f °C%n", "Temperature", temp);
        System.out.printf("%-20s : %.1f %% %n", "Oxygen Saturation", oxy);

        System.out.println("-----------------------------------------------------");
    }

    private static void printFinalReport(
            EvaluatedVitals vitals,
            RiskEvaluationResult risk,
            TrendAnalysisResult trend,
            NotificationResult notification,
            PatientHealthReport report) {

        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║               PATIENT HEALTH REPORT                ║");
        System.out.println("╚════════════════════════════════════════════════════╝");

        System.out.printf("%-25s : %s%n", "Patient ID", risk.getPatientId());
        System.out.printf("%-25s : %s%n", "Overall Status", vitals.getOverallStatus());
        System.out.printf("%-25s : %d%n", "Risk Score", risk.getTotalScore());
        System.out.printf("%-25s : %s%n", "Risk Category", risk.getRiskCategory());
        System.out.printf("%-25s : %s%n", "Trend Classification", trend.getTrendClassification());
        System.out.printf("%-25s : %s%n", "Notification Type", notification.getNotificationType());
        System.out.printf("%-25s : %s%n", "Final System Status", report.getOverallStatus());
        System.out.printf("%-25s : %s%n", "Generated At", report.getGeneratedAt());

        System.out.println("────────────────────────────────────────────────────");
        System.out.println("SYSTEM STATUS : OPERATION COMPLETE");
    }

    private static void printPatientHistory(String patientId) {

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                PATIENT HISTORICAL REPORTS                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        List<PatientHealthReport> history =
                reportRepository.findByPatientId(patientId);

        if (history.isEmpty()) {
            System.out.println("No previous reports found.");
            return;
        }

        System.out.printf("%-12s %-15s %-20s %-15s %-25s%n",
                "Patient ID", "Risk", "Trend", "Status", "Generated At");

        System.out.println("--------------------------------------------------------------------------");

        for (PatientHealthReport r : history) {
            System.out.printf("%-12s %-15s %-20s %-15s %-25s%n",
                    r.getPatientId(),
                    r.getRiskCategory(),
                    r.getTrendClassification(),
                    r.getOverallStatus(),
                    r.getGeneratedAt());
        }

        System.out.println("--------------------------------------------------------------------------");
    }
}