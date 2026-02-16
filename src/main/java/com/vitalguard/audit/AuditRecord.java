package com.vitalguard.audit;

import java.time.LocalDateTime;
import java.util.List;

public class AuditRecord {

    private final String patientId;
    private final LocalDateTime timestamp;
    private final String overallStatus;
    private final List<String> abnormalParameters;

    public AuditRecord(String patientId,
                       LocalDateTime timestamp,
                       String overallStatus,
                       List<String> abnormalParameters) {
        this.patientId = patientId;
        this.timestamp = timestamp;
        this.overallStatus = overallStatus;
        this.abnormalParameters = abnormalParameters;
    }

    @Override
    public String toString() {
        return String.format("""
Patient ID  : %s
Timestamp   : %s
Status      : %s
Abnormalities: %s
----------------------------------------
""", patientId, timestamp, overallStatus,
                abnormalParameters.isEmpty() ? "None" : abnormalParameters);
    }
}
