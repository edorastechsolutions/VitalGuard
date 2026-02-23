package com.edorastech.vitalguard.model;

import java.util.List;

public class EvaluatedVitals {

    private final OverallStatus overallStatus;
    private final double heartRate;
    private final double systolic;
    private final double diastolic;
    private final double temperature;
    private final double oxygenSaturation;
    private final List<VitalAbnormality> abnormalities;

    public EvaluatedVitals(OverallStatus overallStatus,
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

    public OverallStatus getOverallStatus() {
        return overallStatus;
    }

    public List<VitalAbnormality> getAbnormalities() {
        return abnormalities;
    }
}