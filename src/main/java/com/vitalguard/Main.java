package com.vitalguard;

import com.vitalguard.audit.VitalsAuditLogger;
import com.vitalguard.model.*;
import com.vitalguard.risk.*;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("====================================================");
        System.out.println("        VITALGUARD HEALTH MONITORING SYSTEM");
        System.out.println("====================================================");

        System.out.print("Patient ID: ");
        String patientId = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Age: ");
        int age = scanner.nextInt();

        System.out.print("Heart Rate (bpm): ");
        double heartRate = scanner.nextDouble();

        System.out.print("Systolic BP: ");
        double systolic = scanner.nextDouble();

        System.out.print("Diastolic BP: ");
        double diastolic = scanner.nextDouble();

        System.out.print("Temperature (°C): ");
        double temperature = scanner.nextDouble();

        System.out.print("Oxygen Saturation (%): ");
        double oxygen = scanner.nextDouble();

        List<VitalAbnormality> abnormalities = new ArrayList<>();

        if (heartRate < 60)
            abnormalities.add(new VitalAbnormality("Heart Rate", "Low", SeverityLevel.MODERATE));

        if (systolic > 140)
            abnormalities.add(new VitalAbnormality("Systolic BP", "High", SeverityLevel.MODERATE));

        if (temperature > 38)
            abnormalities.add(new VitalAbnormality("Temperature", "High", SeverityLevel.MILD));

        String overallStatus = abnormalities.isEmpty() ? "NORMAL" : "ALERT";

        EvaluatedVitals evaluatedVitals = new EvaluatedVitals(
                overallStatus, heartRate, systolic, diastolic,
                temperature, oxygen, abnormalities
        );

        RiskScoringEngine engine = new RiskScoringEngine();
        RiskEvaluationResult result = engine.calculateRisk(evaluatedVitals);

        VitalsAuditLogger logger = new VitalsAuditLogger();
        logger.log(patientId, overallStatus,
                abnormalities.stream().map(VitalAbnormality::getParameter).toList());

        printReport(patientId, name, age, evaluatedVitals, result);
    }

    private static void printReport(String patientId,
                                    String name,
                                    int age,
                                    EvaluatedVitals vitals,
                                    RiskEvaluationResult result) {

        System.out.println("\nPatient Information");
        System.out.println("----------------------------------------------------");
        System.out.printf("Patient ID      : %s%n", patientId);
        System.out.printf("Name            : %s%n", name);
        System.out.printf("Age             : %d%n", age);

        System.out.println("\nEvaluation Summary");
        System.out.println("----------------------------------------------------");
        System.out.printf("Overall Status  : %s%n", vitals.getOverallStatus());

        if (vitals.getAbnormalities().isEmpty()) {
            System.out.println("Abnormal Vitals : None");
        } else {
            System.out.println("Abnormal Vitals :");
            vitals.getAbnormalities()
                    .forEach(a -> System.out.println("  - " + a));
        }

        System.out.println("\nRisk Assessment");
        System.out.println("----------------------------------------------------");
        System.out.printf("Total Risk Score : %d%n", result.getTotalScore());
        System.out.printf("Risk Category    : %s%n", result.getRiskCategory());

        System.out.println("\n====================================================");
    }
}
