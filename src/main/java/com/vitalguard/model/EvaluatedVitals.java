package com.vitalguard.model;

import java.util.List;

public class EvaluatedVitals {

    private final String overallStatus;
    private final double heartRate;
    private final double systolic;
    private final double diastolic;
    private final double temperature;
    private final double oxygenSaturation;
    private final List<VitalAbnormality> abnormalities;

    public EvaluatedVitals(String overallStatus,
                           double heartRate,
                           double systolic,
                           double diastolic,
                           double temperature,
                           double oxygenSaturation,
                           List<VitalAbnormality> abnormalities) {
        this.overallStatus = overallStatus;
        this.heartRate = heartRate;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.temperature = temperature;
        this.oxygenSaturation = oxygenSaturation;
        this.abnormalities = abnormalities;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public double getSystolic() {
        return systolic;
    }

    public double getDiastolic() {
        return diastolic;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getOxygenSaturation() {
        return oxygenSaturation;
    }

    public List<VitalAbnormality> getAbnormalities() {
        return abnormalities;
    }
}
