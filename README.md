VitalGuard – Health Monitoring System

Overview:
VitalGuard is a modular Java-based health monitoring system that evaluates patient vitals, calculates risk scores, detects health trends, and determines alert escalation using structured domain logic.
The system simulates real-world clinical monitoring workflows, producing structured health reports while maintaining historical patient records.

Architecture Overview:
com.edorastech.vitalguard
│
├── model          → Core domain models (Vitals, Severity, Status)
├── risk           → Risk scoring engine
├── trend          → Trend analysis & classification
│   └── patterns   → Pattern detection strategies
├── audit          → Historical record storage
├── notification   → Alert generation & escalation
├── reporting      → Health report aggregation
├── repository     → Patient report storage
├── validation     → Domain integrity checks
├── config         → Centralized escalation rules
└── Main           → Console entry point

Execution Flow:

User Input
 → Vital Evaluation
 → Risk Scoring
 → Audit Logging
 → Trend Analysis
 → Integrity Validation
 → Notification Decision
 → Report Aggregation
 → Report Storage
 → Output Report

Module Responsibilities:

Module 1 – Model Layer:
Defines the core domain entities used across the system.
Examples:
EvaluatedVitals
VitalAbnormality
SeverityLevel
OverallStatus
These classes represent structured patient health data.

Module 2 – Risk Scoring Engine:
The RiskScoringEngine analyzes abnormal vital parameters and calculates a risk score for the patient.
Risk categories include:
LOW
MODERATE
HIGH

Module 3 – Audit Logging:
The VitalsAuditLogger records patient evaluation results and stores historical monitoring data.
These records are used later for trend analysis.

Module 4 – Trend Analysis:
The VitalsTrendAnalyzer processes historical audit records to detect patterns in patient health.

Pattern detection strategies include:
AlertStreakDetector - Detects three consecutive ALERT states and classifies the trend as CRITICAL.
RecoveryDetector - Detects recovery pattern:
ALERT → ALERT → NORMAL

Trend classifications include:
STABLE
RECOVERING
DETERIORATING
CRITICAL

Module 5 – Notification Engine:
The AlertNotificationEngine determines the appropriate alert response based on:
Risk category
Trend classification
Escalation rules are maintained in a centralized configuration matrix to simplify decision logic.

Module 6 – Patient Health Report Aggregation:
The PatientHealthReportAggregator consolidates the outputs from:
Risk evaluation
Trend analysis
Notification engine
It generates a structured PatientHealthReport representing the patient's overall health status at a given time.

Module 7 – Patient Health Report Repository:
This module manages the storage and retrieval of patient health reports.
The InMemoryPatientHealthReportRepository:
Stores generated reports
Retrieves reports by patient ID
Displays historical patient monitoring data
This enables tracking patient health progression over time.

Escalation Logic:
Escalation is determined using a centralized rule matrix:

Risk Level	        Trend	        Result
HIGH	             Any	    EMERGENCY_ALERT
MODERATE	     DETERIORATING	ESCALATED_ALERT
LOW	               CRITICAL	    ESCALATED_ALERT
Any	              RECOVERING	NONE
Otherwise          	  —	        STANDARD_ALERT

Assumptions:
Audit logs are stored in memory
Simplified vital thresholds are used
Application runs via console interface
Single-threaded execution
No external database
System timestamps use the local system clock

Design Principles:
Clean modular architecture
Enum-based domain modeling
Immutable result objects
Strategy pattern for trend detection
Centralized rule configuration
Chronological audit processing

Tech Stack:
Java 21
Apache Maven
Clean Architecture Design

Running the Project:
Compile and run the project using Maven:
mvn clean compile
mvn exec:java

The execution plugin is configured to run:
com.edorastech.vitalguard.Main

Possible enhancements:
Add database persistence (PostgreSQL / JPA)
Convert to Spring Boot REST API
Externalize escalation rules (YAML / DB)
Add unit and integration testing
Implement real-time monitoring
Split into microservices architecture

VitalGuard demonstrates a clean and scalable architecture for health monitoring systems, combining risk analysis, trend detection, and alert management in a structured Java application.