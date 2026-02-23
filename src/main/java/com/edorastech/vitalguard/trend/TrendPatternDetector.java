package com.edorastech.vitalguard.trend;

import com.edorastech.vitalguard.audit.AuditRecord;
import java.util.List;

public interface TrendPatternDetector {

    boolean detect(List<AuditRecord> records);

    String getPatternName();
}