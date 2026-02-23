package com.edorastech.vitalguard.risk;

import com.edorastech.vitalguard.config.RiskThresholds;
import com.edorastech.vitalguard.model.EvaluatedVitals;
import com.edorastech.vitalguard.model.VitalAbnormality;
import com.edorastech.vitalguard.model.SeverityLevel;

public class RiskScoringEngine {

    public RiskEvaluationResult calculateRisk(String patientId,
                                              EvaluatedVitals vitals) {

        int totalScore = 0;

        for (VitalAbnormality abnormality : vitals.getAbnormalities()) {
            totalScore += abnormality.getSeverity().getPoints();
        }

        RiskCategory category;

        if (totalScore <= RiskThresholds.LOW_RISK_MAX) {
            category = RiskCategory.LOW;
        } else if (totalScore <= RiskThresholds.MODERATE_RISK_MAX) {
            category = RiskCategory.MODERATE;
        } else {
            category = RiskCategory.HIGH;
        }

        return new RiskEvaluationResult(patientId, totalScore, category);
    }
}