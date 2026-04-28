package com.edorastech.vitalguard.audit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CentralAuditLogger {

    private final List<AuditLog> logs = new ArrayList<>();

    public void log(String requestId,
                    String patientId,
                    String endpoint,
                    AuditStatus status,
                    String error) {

        AuditLog log = new AuditLog(
                requestId,
                patientId,
                endpoint,
                LocalDateTime.now(),
                status,
                error
        );

        logs.add(log);
    }

    public List<AuditLog> getLogs() {
        return Collections.unmodifiableList(logs);
    }
}