package com.vitalguard.model;

import com.vitalguard.risk.SeverityLevel;

public class VitalAbnormality {

    private final String parameter;
    private final String description;
    private final SeverityLevel severity;

    public VitalAbnormality(String parameter, String description, SeverityLevel severity) {
        this.parameter = parameter;
        this.description = description;
        this.severity = severity;
    }

    public String getParameter() {
        return parameter;
    }

    public String getDescription() {
        return description;
    }

    public SeverityLevel getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return String.format("%-15s (%s) → Severity: %s",
                parameter,
                description,
                severity);
    }
}
