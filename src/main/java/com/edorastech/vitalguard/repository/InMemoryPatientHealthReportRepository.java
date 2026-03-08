package com.edorastech.vitalguard.repository;

import com.edorastech.vitalguard.reporting.PatientHealthReport;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InMemoryPatientHealthReportRepository
        implements PatientHealthReportRepository {

    private final CopyOnWriteArrayList<PatientHealthReport> storage =
            new CopyOnWriteArrayList<>();

    @Override
    public void save(PatientHealthReport report) {
        if (report == null) {
            throw new IllegalArgumentException("Report cannot be null");
        }
        storage.add(report);
    }

    @Override
    public List<PatientHealthReport> findByPatientId(String patientId) {
        return storage.stream()
                .filter(r -> r.getPatientId().equals(patientId))
                .sorted(Comparator.comparing(PatientHealthReport::getGeneratedAt))
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientHealthReport> findByPatientIdAndDateRange(
            String patientId,
            LocalDateTime from,
            LocalDateTime to) {

        return storage.stream()
                .filter(r -> r.getPatientId().equals(patientId))
                .filter(r -> !r.getGeneratedAt().isBefore(from)
                        && !r.getGeneratedAt().isAfter(to))
                .sorted(Comparator.comparing(PatientHealthReport::getGeneratedAt))
                .collect(Collectors.toList());
    }
}