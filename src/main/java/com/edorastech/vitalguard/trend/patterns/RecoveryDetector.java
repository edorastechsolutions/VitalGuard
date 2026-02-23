package com.edorastech.vitalguard.trend.patterns;

import com.edorastech.vitalguard.audit.AuditRecord;
import com.edorastech.vitalguard.model.OverallStatus;
import com.edorastech.vitalguard.trend.TrendPatternDetector;

import java.util.List;

public class RecoveryDetector implements TrendPatternDetector {

    @Override
    public boolean detect(List<AuditRecord> records) {

        if (records == null || records.size() < 3) {
            return false;
        }

        for (int i = 0; i < records.size() - 2; i++) {

            if (records.get(i).getOverallStatus() == OverallStatus.ALERT &&
                records.get(i + 1).getOverallStatus() == OverallStatus.ALERT &&
                records.get(i + 2).getOverallStatus() == OverallStatus.NORMAL) {

                return true;
            }
        }

        return false;
    }

    @Override
    public String getPatternName() {
        return "RECOVERY_PATTERN";
    }
}