package com.edorastech.vitalguard.audit;

import java.time.LocalDateTime;

public class AuditLog {

    private final String requestId;
    private final String userId;
    private final String action;
    private final LocalDateTime timestamp;
    private final AuditStatus status;
    private final String error;

    public AuditLog(String requestId,
                    String userId,
                    String action,
                    LocalDateTime timestamp,
                    AuditStatus status,
                    String error) {
        this.requestId = requestId;
        this.userId = userId;
        this.action = action;
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }

    @Override
    public String toString() {
        return String.format(
                "[%s] [%s] User=%s Action=%s Status=%s Error=%s",
                timestamp,
                requestId,
                userId,
                action,
                status,
                error == null ? "-" : error
        );
    }
}