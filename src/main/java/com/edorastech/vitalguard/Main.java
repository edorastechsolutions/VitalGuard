package com.edorastech.vitalguard;

import com.edorastech.vitalguard.audit.VitalsAuditLogger;
import com.edorastech.vitalguard.model.*;
import com.edorastech.vitalguard.notification.*;
import com.edorastech.vitalguard.reporting.PatientHealthReport;
import com.edorastech.vitalguard.reporting.PatientHealthReportAggregator;
import com.edorastech.vitalguard.repository.InMemoryPatientHealthReportRepository;
import com.edorastech.vitalguard.repository.PatientHealthReportRepository;
import com.edorastech.vitalguard.risk.*;
import com.edorastech.vitalguard.trend.*;
import com.edorastech.vitalguard.validation.*;

import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final PatientHealthReportRepository reportRepository =
            new InMemoryPatientHealthReportRepository();

    public static void main(String[] args) {

        printSystemHeader();

        try {

            // Patient Input
            System.out.print("Patient ID: ");
            String patientId = scanner.nextLine().trim();

            double heartRate = readDouble("Heart Rate (bpm): ");
            double systolic = readDouble("Systolic BP: ");
            double diastolic = readDouble("Diastolic BP: ");
            double temperature = readDouble("Body Temperature (°C): ");
            double oxygen = readDouble("Oxygen Saturation (%): ");

            printVitalsPanel(patientId, heartRate, systolic, diastolic, temperature, oxygen);

            // Abnormality Detection
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

            // Risk Evaluation
            RiskScoringEngine riskEngine = new RiskScoringEngine();
            RiskEvaluationResult riskResult =
                    riskEngine.calculateRisk(patientId, evaluatedVitals);

            // Audit Logging
            VitalsAuditLogger auditLogger = new VitalsAuditLogger();

            List<String> abnormalParams = new ArrayList<>();
            for (VitalAbnormality abnormality : abnormalities) {
                abnormalParams.add(abnormality.getParameter());
            }

            auditLogger.log(patientId, status, abnormalParams);

            // Trend Analysis
            VitalsTrendAnalyzer trendAnalyzer = new VitalsTrendAnalyzer();
            TrendAnalysisResult trendResult =
                    trendAnalyzer.analyze(
                            patientId,
                            auditLogger.getHistory(patientId)
                    );

            // System Validation
            SystemIntegrityGuard guard = new SystemIntegrityGuard();
            guard.validate(riskResult, trendResult);

            // Notification
            AlertNotificationEngine notificationEngine =
                    new AlertNotificationEngine();

            NotificationResult notification =
                    notificationEngine.generateNotification(
                            riskResult,
                            trendResult
                    );

            guard.validateNotification(notification, patientId);

            // Aggregate Report
            PatientHealthReportAggregator aggregator =
                    new PatientHealthReportAggregator();

            PatientHealthReport report =
                    aggregator.aggregate(
                            riskResult,
                            trendResult,
                            notification
                    );

            reportRepository.save(report);

            // Print Final Report
            printFinalReport(evaluatedVitals, riskResult, trendResult, notification, report);

            // Print History
            printPatientHistory(patientId);

        } catch (DomainValidationException ex) {

            System.out.println("\n[DOMAIN VALIDATION ERROR]");
            System.out.println(ex.getMessage());

        } catch (InputMismatchException ex) {

            System.out.println("\n[INPUT ERROR]");
            System.out.println("Invalid numeric input provided.");

        } finally {
            scanner.close();
        }
    }

    private static void printSystemHeader() {

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       VITALGUARD - ADVANCED HEALTH MONITORING SYSTEM       ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        return scanner.nextDouble();
    }

    private static void printVitalsPanel(
            String patientId,
            double hr,
            double sys,
            double dia,
            double temp,
            double oxy) {

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
                "Patient ID",
                "Risk",
                "Trend",
                "Status",
                "Generated At");

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