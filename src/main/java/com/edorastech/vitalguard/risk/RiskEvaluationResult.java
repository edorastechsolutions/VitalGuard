package com.edorastech.vitalguard.risk;

public class RiskEvaluationResult {

    private final String patientId;
    private final int totalScore;
    private final RiskCategory riskCategory;

    public RiskEvaluationResult(String patientId,
                                int totalScore,
                                RiskCategory riskCategory) {
        this.patientId = patientId;
        this.totalScore = totalScore;
        this.riskCategory = riskCategory;
    }

    public String getPatientId() {
        return patientId;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public RiskCategory getRiskCategory() {
        return riskCategory;
    }
}