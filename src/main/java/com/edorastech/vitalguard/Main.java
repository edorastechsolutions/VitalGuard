package com.edorastech.vitalguard;

import com.edorastech.vitalguard.audit.VitalsAuditLogger;
import com.edorastech.vitalguard.model.*;
import com.edorastech.vitalguard.notification.*;
import com.edorastech.vitalguard.risk.*;
import com.edorastech.vitalguard.trend.*;
import com.edorastech.vitalguard.validation.*;

import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=======================================================");
        System.out.println("        VITALGUARD - ADVANCED HEALTH MONITORING SYSTEM");
        System.out.println("=======================================================\n");

        try {
            //Input from User

            System.out.print("Enter Patient ID: ");
            String patientId = scanner.nextLine().trim();

            double heartRate = readDouble("Heart Rate (bpm): ");
            double systolic = readDouble("Systolic BP: ");
            double diastolic = readDouble("Diastolic BP: ");
            double temperature = readDouble("Body Temperature (°C): ");
            double oxygen = readDouble("Oxygen Saturation (%): ");

            //Abnormality Detection

            List<VitalAbnormality> abnormalities = new ArrayList<>();

            if (heartRate < 60 || heartRate > 100)
                abnormalities.add(new VitalAbnormality(
                        "Heart Rate",
                        "Out of Range",
                        SeverityLevel.MODERATE));

            if (systolic > 140)
                abnormalities.add(new VitalAbnormality(
                        "Systolic BP",
                        "High",
                        SeverityLevel.MODERATE));

            if (temperature > 38)
                abnormalities.add(new VitalAbnormality(
                        "Temperature",
                        "Fever",
                        SeverityLevel.MILD));

            if (oxygen < 95)
                abnormalities.add(new VitalAbnormality(
                        "Oxygen Saturation",
                        "Low",
                        SeverityLevel.SEVERE));

            OverallStatus status =
                    abnormalities.isEmpty()
                            ? OverallStatus.NORMAL
                            : OverallStatus.ALERT;

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

            //Risk Evaluation
           
            RiskScoringEngine riskEngine = new RiskScoringEngine();
            RiskEvaluationResult riskResult =
                    riskEngine.calculateRisk(patientId, evaluatedVitals);

            //Audit Logging

            VitalsAuditLogger auditLogger = new VitalsAuditLogger();

            List<String> abnormalParams = new ArrayList<>();
            for (VitalAbnormality abnormality : abnormalities) {
                abnormalParams.add(abnormality.getParameter());
            }

            auditLogger.log(patientId, status, abnormalParams);

            //Trend Analysis

            VitalsTrendAnalyzer trendAnalyzer = new VitalsTrendAnalyzer();
            TrendAnalysisResult trendResult =
                    trendAnalyzer.analyze(
                            patientId,
                            auditLogger.getHistory(patientId)
                    );

            //System Integrity Validation
  
            SystemIntegrityGuard guard = new SystemIntegrityGuard();
            guard.validate(riskResult, trendResult);

           
            //Notification Engine

            AlertNotificationEngine notificationEngine =
                    new AlertNotificationEngine();

            NotificationResult notification =
                    notificationEngine.generateNotification(
                            riskResult,
                            trendResult
                    );

            guard.validateNotification(notification, patientId);

            //Output

            printFinalReport(
                    evaluatedVitals,
                    riskResult,
                    trendResult,
                    notification
            );

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

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        return scanner.nextDouble();
    }

    private static void printFinalReport(
            EvaluatedVitals vitals,
            RiskEvaluationResult risk,
            TrendAnalysisResult trend,
            NotificationResult notification) {

        System.out.println("\n=======================================================");
        System.out.println("                  PATIENT HEALTH REPORT");
        System.out.println("=======================================================");

        System.out.printf("Patient ID              : %s%n", risk.getPatientId());
        System.out.printf("Overall Status          : %s%n", vitals.getOverallStatus());
        System.out.printf("Risk Score              : %d%n", risk.getTotalScore());
        System.out.printf("Risk Category           : %s%n", risk.getRiskCategory());

        System.out.printf("Total Records           : %d%n", trend.getTotalRecords());
        System.out.printf("Max Alert Streak        : %d%n", trend.getAlertStreakMax());
        System.out.printf("Trend Classification    : %s%n",
                trend.getTrendClassification());

        System.out.println("\nDetected Patterns:");
        if (trend.getDetectedPatterns().isEmpty()) {
            System.out.println(" - None");
        } else {
            trend.getDetectedPatterns()
                    .forEach(p -> System.out.println(" - " + p));
        }

        System.out.println("\nNotification Summary:");
        System.out.printf("Type                    : %s%n",
                notification.getNotificationType());
        System.out.printf("Message                 : %s%n",
                notification.getMessage());
        System.out.printf("Generated At            : %s%n",
                notification.getGeneratedAt());

        System.out.println("=======================================================");
        System.out.println("System Status: OPERATION COMPLETE");
        System.out.println("=======================================================");
    }
}