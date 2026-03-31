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
├── audit          → Audit logging & traceability
├── notification   → Alert generation & escalation
├── reporting      → Health report aggregation
├── repository     → Patient report storage
├── monitoring     → Request tracing (Request ID)
├── security       → Rate limiting & brute force protection
├── validation     → Domain integrity & fail-safe checks
├── config         → Centralized rules & security configs
└── Main           → Console entry point

Execution Flow:
Request Start (Request ID)
 → Authentication Protection
 → Rate Limiting
 → Vital Evaluation
 → Risk Scoring
 → Audit Logging
 → Trend Analysis
 → Validation (Fail-Closed)
 → Notification Decision
 → Report Aggregation
 → Report Storage
 → Response Output

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

Module 8 – Request Trace & Correlation System:
Generates unique Request ID (UUID) per request.
Attached to logs and responses
Enables full request traceability

Module 9 – Rate Limiting Engine:
Prevents excessive usage.
Thread-safe implementation
Per-user request limits
Rejects with:
429 TOO MANY REQUESTS

Module 10 – Brute Force Protection System:
Protects authentication flow.
Tracks failed login attempts
Locks account after threshold
Auto-reset on success

Module 11 – Centralized Audit Logging:
Captures system activity:
requestId
userId
endpoint
timestamp
status
Ensures observability and traceability.

Module 12 – Fail-Closed Resilience Handling:
Ensures system safety.
Any failure → request denied
No unsafe execution allowed
Global exception handling

Module 13 – Security Headers & CORS Hardening:
Enhances system security.
Headers:
X-Content-Type-Options
X-Frame-Options
X-XSS-Protection
Strict-Transport-Security
CORS:
Only trusted origins allowed
No wildcard usage

Escalation Logic:
Risk	       Trend	        Result
HIGH	        Any	       EMERGENCY_ALERT
MODERATE	DETERIORATING  ESCALATED_ALERT
LOW	          CRITICAL	   ESCALATED_ALERT
Any	         RECOVERING	        NONE
Default	         —	        STANDARD_ALERT

Assumptions:
In-memory storage (no database)
Console-based execution
Simplified vital thresholds
Single-node system
System clock used for timestamps

Design Principles:
Clean modular architecture
Enum-based domain modeling
Immutable objects
Strategy pattern (trend detection)
Centralized configuration
Thread-safe implementations
Defensive programming

Tech Stack:
Java 21
Apache Maven

To Run the Project:
mvn clean compile
mvn exec:java

Main Class:
com.edorastech.vitalguard.Main

Conclusion:
VitalGuard demonstrates a production-ready health monitoring system combining:
Risk analysis
Trend intelligence
Alert escalation
Historical tracking
Security & abuse prevention
Observability & traceability