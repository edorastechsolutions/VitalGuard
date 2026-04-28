package com.edorastech.vitalguard;

import com.edorastech.vitalguard.audit.*;
import com.edorastech.vitalguard.health.HealthCheckService;
import com.edorastech.vitalguard.metrics.MetricsTracker;
import com.edorastech.vitalguard.model.*;
import com.edorastech.vitalguard.monitoring.*;
import com.edorastech.vitalguard.notification.*;
import com.edorastech.vitalguard.reporting.*;
import com.edorastech.vitalguard.repository.*;
import com.edorastech.vitalguard.risk.*;
import com.edorastech.vitalguard.security.*;
import com.edorastech.vitalguard.trend.*;
import com.edorastech.vitalguard.validation.*;

import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final PatientHealthReportRepository reportRepository =
            new InMemoryPatientHealthReportRepository();

    private static final RateLimiter rateLimiter = new RateLimiter();
    private static final BruteForceProtectionService bruteForceService =
            new BruteForceProtectionService();
    private static final CentralAuditLogger auditLogger =
            new CentralAuditLogger();
    private static final MetricsTracker metrics = new MetricsTracker();

    public static void main(String[] args) {

        printSystemHeader();

        String requestId = new RequestIdGenerator().generate();
        RequestContext.setRequestId(requestId);

        metrics.incrementTotal();

        String patientId = null;

        try {
            // ================= AUTH =================
            System.out.print("Patient ID: ");
            patientId = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            if (!bruteForceService.authenticate(patientId, password)) {

                metrics.incrementFailure();

                auditLogger.log(
                        requestId,
                        patientId,
                        "AUTH",
                        AuditStatus.FAIL,
                        "INVALID_CREDENTIALS_OR_LOCKED"
                );

                System.out.println("❌ Authentication failed / Account locked.");
                return;
            }

            // ================= RATE LIMIT =================
            if (!rateLimiter.allowRequest(patientId)) {

                metrics.incrementRateLimited();

                auditLogger.log(
                        requestId,
                        patientId,
                        "RATE_LIMIT",
                        AuditStatus.FAIL,
                        "TOO_MANY_REQUESTS"
                );

                System.out.println("❌ Too many requests (HTTP 429)");
                return;
            }

            // ================= INPUT =================
            double heartRate = readDouble("Heart Rate (bpm): ");
            double systolic = readDouble("Systolic BP: ");
            double diastolic = readDouble("Diastolic BP: ");
            double temperature = readDouble("Body Temperature (°C): ");
            double oxygen = readDouble("Oxygen Saturation (%): ");

            printVitalsPanel(patientId, heartRate, systolic, diastolic, temperature, oxygen);

            // ================= DOMAIN =================
            List<VitalAbnormality> abnormalities = detectAbnormalities(
                    heartRate, systolic, temperature, oxygen
            );

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

            RiskEvaluationResult riskResult =
                    new RiskScoringEngine().calculateRisk(patientId, evaluatedVitals);

            auditLogger.log(requestId, patientId, "RISK_ENGINE", AuditStatus.SUCCESS, null);

            // ================= TREND =================
            VitalsAuditLogger vitalsAuditLogger = new VitalsAuditLogger();
            vitalsAuditLogger.log(patientId, status, extractParams(abnormalities));

            TrendAnalysisResult trendResult =
                    new VitalsTrendAnalyzer().analyze(
                            patientId,
                            vitalsAuditLogger.getHistory(patientId)
                    );

            // ================= VALIDATION =================
            SystemIntegrityGuard guard = new SystemIntegrityGuard();
            guard.validate(riskResult, trendResult);

            NotificationResult notification =
                    new AlertNotificationEngine()
                            .generateNotification(riskResult, trendResult);

            guard.validateNotification(notification, patientId);

            // ================= REPORT =================
            PatientHealthReport report =
                    new PatientHealthReportAggregator()
                            .aggregate(riskResult, trendResult, notification);

            reportRepository.save(report);

            auditLogger.log(requestId, patientId, "PROCESS_COMPLETE", AuditStatus.SUCCESS, null);

            printFinalReport(evaluatedVitals, riskResult, trendResult, notification, report);
            printPatientHistory(patientId);

        }
        catch (DomainValidationException ex) {

            metrics.incrementFailure();

            auditLogger.log(requestId, patientId, "VALIDATION", AuditStatus.FAIL, ex.getMessage());
            System.out.println("❌ DOMAIN ERROR: " + ex.getMessage());

        }
        catch (InputMismatchException ex) {

            metrics.incrementFailure();

            auditLogger.log(requestId, patientId, "INPUT", AuditStatus.FAIL, "INVALID_INPUT");
            System.out.println("❌ Invalid numeric input.");

        }
        catch (Exception ex) {

            metrics.incrementFailure();

            auditLogger.log(requestId, patientId, "SYSTEM", AuditStatus.FAIL, ex.getMessage());
            GlobalExceptionHandler.handle(ex);

        }
        finally {
            RequestContext.clear();
            scanner.close();

            System.out.println("\n📊 SYSTEM METRICS:");
            System.out.println(metrics.snapshot());
        }
    }

    // ================= HELPERS =================

    private static List<VitalAbnormality> detectAbnormalities(
            double heartRate,
            double systolic,
            double temperature,
            double oxygen) {

        List<VitalAbnormality> abnormalities = new ArrayList<>();

        if (heartRate < 60 || heartRate > 100)
            abnormalities.add(new VitalAbnormality("Heart Rate", "Out of Range", SeverityLevel.MODERATE));

        if (systolic > 140)
            abnormalities.add(new VitalAbnormality("Systolic BP", "High", SeverityLevel.MODERATE));

        if (temperature > 38)
            abnormalities.add(new VitalAbnormality("Temperature", "Fever", SeverityLevel.MILD));

        if (oxygen < 95)
            abnormalities.add(new VitalAbnormality("Oxygen Saturation", "Low", SeverityLevel.SEVERE));

        return abnormalities;
    }

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

        List<PatientHealthReport> history =
                reportRepository.findByPatientId(patientId);

        if (history.isEmpty()) {
            System.out.println("No previous reports found.");
            return;
        }

        System.out.println("\n--- PATIENT HISTORY ---");

        for (PatientHealthReport r : history) {
            System.out.printf("%s | %s | %s | %s%n",
                    r.getPatientId(),
                    r.getRiskCategory(),
                    r.getTrendClassification(),
                    r.getGeneratedAt());
        }
        System.out.println("\n🏥 HEALTH CHECK:");
        System.out.println(new HealthCheckService().check());
    }
    
}
