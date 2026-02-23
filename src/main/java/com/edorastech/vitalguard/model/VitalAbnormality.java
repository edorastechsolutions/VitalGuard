package com.edorastech.vitalguard.model;

import java.util.Objects;

import com.edorastech.vitalguard.model.SeverityLevel;

public final class VitalAbnormality {

    private final String parameter;
    private final String description;
    private final SeverityLevel severity;

    public VitalAbnormality(String parameter,
                            String description,
                            SeverityLevel severity) {

        this.parameter = Objects.requireNonNull(parameter, "Parameter cannot be null");
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.severity = Objects.requireNonNull(severity, "Severity cannot be null");
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
        return String.format(
                "%-15s (%s) → Severity: %s",
                parameter,
                description,
                severity
        );
    }
}