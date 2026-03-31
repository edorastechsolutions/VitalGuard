package com.edorastech.vitalguard.audit;

import com.edorastech.vitalguard.monitoring.RequestContext;

import java.time.LocalDateTime;

public class AuditService {

    public void logSuccess(String userId, String endpoint) {
        System.out.println("[AUDIT] " +
                RequestContext.getRequestId() +
                " | USER=" + userId +
                " | ENDPOINT=" + endpoint +
                " | SUCCESS");
    }

    public void logFailure(String userId, String endpoint, String error) {
        System.out.println("[AUDIT] " +
                RequestContext.getRequestId() +
                " | USER=" + userId +
                " | ENDPOINT=" + endpoint +
                " | FAIL | ERROR=" + error);
    }
}