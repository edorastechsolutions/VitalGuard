package com.vitalguard.risk;

public class RiskEvaluationResult {

    private final int totalScore;
    private final String riskCategory;

    public RiskEvaluationResult(int totalScore, String riskCategory) {
        this.totalScore = totalScore;
        this.riskCategory = riskCategory;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getRiskCategory() {
        return riskCategory;
    }
}
