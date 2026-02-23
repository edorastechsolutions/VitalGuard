package com.edorastech.vitalguard.audit;

import com.edorastech.vitalguard.model.OverallStatus;
import java.time.LocalDateTime;
import java.util.List;

public class AuditRecord {

    private final String patientId;
    private final LocalDateTime timestamp;
    private final OverallStatus overallStatus;
    private final List<String> abnormalParameters;

    public AuditRecord(String patientId,
                       LocalDateTime timestamp,
                       OverallStatus overallStatus,
                       List<String> abnormalParameters) {

        this.patientId = patientId;
        this.timestamp = timestamp;
        this.overallStatus = overallStatus;
        this.abnormalParameters = List.copyOf(abnormalParameters);
    }

    public String getPatientId() {
        return patientId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public OverallStatus getOverallStatus() {
        return overallStatus;
    }

    public List<String> getAbnormalParameters() {
        return abnormalParameters;
    }
}