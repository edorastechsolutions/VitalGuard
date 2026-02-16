package com.vitalguard.risk;

import com.vitalguard.config.RiskThresholds;
import com.vitalguard.model.EvaluatedVitals;
import com.vitalguard.model.VitalAbnormality;

public class RiskScoringEngine {

    public RiskEvaluationResult calculateRisk(EvaluatedVitals vitals) {

        int totalScore = 0;

        for (VitalAbnormality abnormality : vitals.getAbnormalities()) {
            totalScore += abnormality.getSeverity().getPoints();
        }

        String category;

        if (totalScore <= RiskThresholds.LOW_RISK_MAX) {
            category = "LOW";
        } else if (totalScore <= RiskThresholds.MODERATE_RISK_MAX) {
            category = "MODERATE";
        } else {
            category = "HIGH";
        }

        return new RiskEvaluationResult(totalScore, category);
    }
}
