package com.edorastech.vitalguard.trend.patterns;

import com.edorastech.vitalguard.audit.AuditRecord;
import com.edorastech.vitalguard.model.OverallStatus;
import com.edorastech.vitalguard.trend.TrendPatternDetector;

import java.util.List;

public class AlertStreakDetector implements TrendPatternDetector {

    private static final int CRITICAL_STREAK_THRESHOLD = 3;

    private int maxStreak = 0;

    @Override
    public boolean detect(List<AuditRecord> records) {

        int current = 0;

        for (AuditRecord record : records) {

            if (record.getOverallStatus() == OverallStatus.ALERT) {
                current++;
                maxStreak = Math.max(maxStreak, current);
            } else {
                current = 0;
            }
        }

        return maxStreak >= CRITICAL_STREAK_THRESHOLD;
    }

    @Override
    public String getPatternName() {
        return "CRITICAL_ALERT_STREAK";
    }

    public int getMaxStreak() {
        return maxStreak;
    }
}