package com.edorastech.vitalguard.audit;

import java.time.LocalDateTime;

public class AuditLog {

    private final String requestId;
    private final String patientId;
    private final String endpoint;
    private final LocalDateTime timestamp;
    private final AuditStatus status;
    private final String error;

    public AuditLog(String requestId,
                    String patientId,
                    String endpoint,
                    LocalDateTime timestamp,
                    AuditStatus status,
                    String error) {

        this.requestId = requestId;
        this.patientId = patientId;
        this.endpoint = endpoint;
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }

    public String getRequestId() { return requestId; }
    public String getPatientId() { return patientId; }
    public String getEndpoint() { return endpoint; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public AuditStatus getStatus() { return status; }
    public String getError() { return error; }
}