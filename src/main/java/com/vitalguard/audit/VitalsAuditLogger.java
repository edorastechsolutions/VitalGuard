package com.vitalguard.audit;

import java.time.LocalDateTime;
import java.util.*;

public class VitalsAuditLogger {

    private final Map<String, List<AuditRecord>> auditStore = new HashMap<>();

    public void log(String patientId,
                    String overallStatus,
                    List<String> abnormalParameters) {

        AuditRecord record = new AuditRecord(
                patientId,
                LocalDateTime.now(),
                overallStatus,
                abnormalParameters
        );

        auditStore
                .computeIfAbsent(patientId, k -> new ArrayList<>())
                .add(record);
    }

    public List<AuditRecord> getHistory(String patientId) {
        return auditStore.getOrDefault(patientId, Collections.emptyList());
    }
}
