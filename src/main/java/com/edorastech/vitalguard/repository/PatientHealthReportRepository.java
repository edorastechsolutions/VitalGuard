package com.edorastech.vitalguard.repository;

import com.edorastech.vitalguard.reporting.PatientHealthReport;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientHealthReportRepository {

    void save(PatientHealthReport report);

    List<PatientHealthReport> findByPatientId(String patientId);

    List<PatientHealthReport> findByPatientIdAndDateRange(
            String patientId,
            LocalDateTime from,
            LocalDateTime to
    );
}