package com.edorastech.vitalguard.validation;

import com.edorastech.vitalguard.reporting.PatientHealthReport;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataConsistencyValidator {

    public void validate(List<PatientHealthReport> reports) {

        if (reports == null)
            throw new IllegalArgumentException("Reports cannot be null");

        Set<String> unique = new HashSet<>();

        for (PatientHealthReport r : reports) {

            String key = r.getPatientId() + r.getGeneratedAt();

            if (!unique.add(key)) {
                throw new IllegalStateException("Duplicate report detected");
            }
        }
    }
}