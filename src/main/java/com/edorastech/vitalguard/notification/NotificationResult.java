package com.edorastech.vitalguard.notification;

import java.time.LocalDateTime;

public final class NotificationResult {

    private final String patientId;
    private final NotificationType notificationType;
    private final String message;
    private final LocalDateTime generatedAt;

    public NotificationResult(String patientId,
                              NotificationType notificationType,
                              String message,
                              LocalDateTime generatedAt) {
        this.patientId = patientId;
        this.notificationType = notificationType;
        this.message = message;
        this.generatedAt = generatedAt;
    }

    public String getPatientId() { return patientId; }

    public NotificationType getNotificationType() { return notificationType; }

    public String getMessage() { return message; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
}