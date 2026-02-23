package com.edorastech.vitalguard.audit;

import java.time.LocalDateTime;
import java.util.*;

import com.edorastech.vitalguard.model.OverallStatus;

public class VitalsAuditLogger {

    private final Map<String, List<AuditRecord>> auditStore = new HashMap<>();

    public void log(String patientId,
                    OverallStatus status,
                    List<String> abnormalParameters) {

        AuditRecord record = new AuditRecord(
                patientId,
                LocalDateTime.now(),
                status,
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