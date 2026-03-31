package com.edorastech.vitalguard.audit;

import java.time.LocalDateTime;

public class CentralAuditLogger {

    public void log(String requestId,
                    String userId,
                    String action,
                    AuditStatus status,
                    String error) {

        AuditLog log = new AuditLog(
                requestId,
                userId,
                action,
                LocalDateTime.now(),
                status,
                error
        );

        // Non-blocking simulation (console)
        System.out.println(log);
    }
}