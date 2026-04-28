VitalGuard – Advanced Health Monitoring System
📌 Overview :
VitalGuard is a modular, production-oriented Java-based health monitoring system designed to simulate real-world clinical workflows.

The system:
Evaluates patient vitals
Calculates risk scores
Detects health trends
Generates alert notifications
Maintains historical records
Ensures security, observability, and reliability

It evolves from a simple backend into a failure-resistant, testable, and observable system.

🏗️ Architecture Overview:

com.edorastech.vitalguard
│
├── model          → Core domain models
├── risk           → Risk scoring engine
├── trend          → Trend analysis & pattern detection
│   └── patterns   → Detection strategies
├── audit          → Audit logging & traceability
├── notification   → Alert generation & escalation
├── reporting      → Health report aggregation
├── repository     → Report storage (in-memory)
├── monitoring     → Request tracing (Request ID)
├── security       → Rate limiting & brute force protection
├── validation     → Fail-safe integrity checks
├── config         → Centralized rules & security configs
├── testing        → Unit & integration tests
├── metrics        → System metrics tracking
├── health         → Health check endpoint
└── Main           → Console entry point

🔄 Execution Flow:

Request Start (Request ID)
 → Authentication Protection
 → Rate Limiting
 → Vital Input Processing
 → Risk Scoring
 → Audit Logging
 → Trend Analysis
 → Validation (Fail-Closed)
 → Notification Decision
 → Report Aggregation
 → Report Storage
 → Response Output
 → Metrics Update

📦 Module-Wise Breakdown:

🔷 Module 1 – Model Layer

Defines core domain entities.

EvaluatedVitals
VitalAbnormality
SeverityLevel
OverallStatus
👉 Represents structured patient health data.

🔷 Module 2 – Risk Scoring Engine

Analyzes abnormalities and computes risk score.

Risk Categories:
LOW
MODERATE
HIGH

🔷 Module 3 – Audit Logging (Vitals)

Stores patient evaluation history for trend analysis.

🔷 Module 4 – Trend Analysis

Detects patterns from historical data.

Strategies:
AlertStreakDetector → 3 alerts → CRITICAL
RecoveryDetector → ALERT → ALERT → NORMAL

Trend Types:
STABLE
RECOVERING
DETERIORATING
CRITICAL

🔷 Module 5 – Notification Engine

Generates alerts based on:

Risk level
Trend classification

Uses centralized escalation rules.

🔷 Module 6 – Report Aggregation

Combines:
Risk result
Trend result
Notification

➡ Produces PatientHealthReport

🔷 Module 7 – Repository

InMemoryPatientHealthReportRepository

Stores reports
Retrieves by patient ID
Enables history tracking

🔷 Module 8 – Request Trace & Correlation

Generates unique Request ID (UUID)
Propagates across logs & flow
Enables full traceability

🔷 Module 9 – Rate Limiting Engine

Prevents abuse
Thread-safe (ConcurrentHashMap)

Example Rules:
10 requests/min per user
Response:
HTTP 429 – Too Many Requests

🔷 Module 10 – Brute Force Protection

Tracks failed login attempts
Locks account after threshold

Rules:
5 failures → lock for 15 minutes
Reset on success

🔷 Module 11 – Centralized Audit Logging

Logs system activity:
requestId
userId
endpoint
timestamp
status (SUCCESS / FAIL)

👉 Ensures observability & traceability

🔷 Module 12 – Fail-Closed Resilience

Any failure → DENY request
No unsafe execution allowed

Handled via:
SystemIntegrityGuard
GlobalExceptionHandler

🔷 Module 13 – Security Hardening

Implements security headers:
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
Strict-Transport-Security

CORS:
Only trusted origins allowed
No wildcard usage

🔷 Module 14 – Test Coverage

Includes:
Unit Tests
Integration Tests

Tested Components:
Aggregator
Repository
Rate Limiter
Audit Logger

✔ Edge cases covered:
null inputs
empty data
invalid states

🔷 Module 15 – Failure Simulation Engine

Simulates:
Null input
Repository failure
Rate limit breach
Unauthorized access

👉 Ensures fail-closed behavior

🔷 Module 16 – Health Check System

Endpoint Simulation:
GET /health
Response:
{
  "status": "UP",
  "timestamp": "...",
  "components": {
    "repository": "UP",
    "rateLimiter": "UP"
  }
}

🔷 Module 17 – Data Consistency Validator

Ensures:
Chronological order
No duplicate reports
Valid report structure

🔷 Module 18 – Metrics Tracker

Tracks:
Total requests
Failed requests
Rate-limited requests

✔ Thread-safe using AtomicInteger

📊 Escalation Logic
Risk	              Trend	               Result
HIGH	               Any	           EMERGENCY_ALERT
MODERATE	      DETERIORATING	       ESCALATED_ALERT
LOW	                 CRITICAL	       ESCALATED_ALERT
Any	                RECOVERING	            NONE
Default	                —	           STANDARD_ALERT

Assumptions:

In-memory storage (no DB)
Console-based execution
Simplified thresholds
Single-node system
Local system clock used

Design Principles:

Clean modular architecture
Immutable domain models
Strategy pattern (trend detection)
Centralized configuration
Thread-safe components
Fail-closed security design
Separation of concerns

🛠️ Tech Stack:

Java 21
Apache Maven
JUnit (Testing)

▶️ Running the Project:

mvn clean install
mvn exec:java

Main Class:
com.edorastech.vitalguard.Main

Sample Output:

Request ID : a1b2c3

PATIENT HEALTH REPORT
Patient ID           : P101
Overall Status       : ALERT
Risk Score           : 75
Risk Category        : HIGH
Trend Classification : CRITICAL
Notification Type    : EMERGENCY_ALERT
Final System Status  : CRITICAL
Generated At         : 2026-04-28T22:30

Conclusion:

VitalGuard successfully demonstrates a production-grade health monitoring system by combining:

✅ Risk Analysis
✅ Trend Intelligence
✅ Alert Escalation
✅ Historical Tracking
✅ Security & Abuse Prevention
✅ Observability & Traceability
✅ Reliability & Testing