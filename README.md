VitalGuard – Health Monitoring System

Overview:

VitalGuard is a modular Java-based health monitoring system that evaluates patient vitals, calculates risk scores, detects health trends, and determines alert escalation using clean architectural principles.

The system simulates real-world clinical monitoring with structured decision logic and pattern-based trend analysis.

Architecture Overview:

com.edorastech.vitalguard
│
├── model          → Core domain models (Vitals, Severity, Status)
├── risk           → Risk scoring engine
├── trend          → Trend analysis & classification
│   └── patterns   → Pattern detection strategies
├── audit          → Historical record storage
├── notification   → Escalation & alert generation
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
 → Output Report

Pattern Detection Logic:

Trend analysis is implemented using a strategy-based detector interface:

AlertStreakDetector
Detects 3 consecutive ALERT records → CRITICAL pattern.

RecoveryDetector
Detects ALERT → ALERT → NORMAL → RECOVERING pattern.

If no recovery and alerts increase → DETERIORATING.

Trend classifications:

STABLE
RECOVERING
DETERIORATING
CRITICAL

Escalation Logic:

Escalation is determined using a centralized rule matrix:

Risk Level	          Trend	        Result
HIGH	             Any	          EMERGENCY_ALERT
MODERATE	         DETERIORATING	ESCALATED_ALERT
LOW	               CRITICAL	      ESCALATED_ALERT
Any	               RECOVERING	    NONE
Otherwise	             —          STANDARD_ALERT

Rules are defined in a configuration class to avoid scattered conditional logic.

Assumptions:

Audit logs are stored in memory.
Severity thresholds are simplified for simulation.
Console-based execution (no UI).
Single-threaded environment.
Timestamps use system clock.
No external database.

Design Decisions:

Enums used for type safety (no string comparisons).
Immutable result objects.
Strategy pattern for trend detection.
Centralized escalation configuration.
Chronological audit processing.
No magic numbers (constants defined).

Future Scalability:

Add database persistence (JPA / PostgreSQL).
Convert to Spring Boot REST API.
Externalize escalation rules (YAML / DB).
Add unit and integration tests.
Implement real-time streaming support.
Split into microservices (Risk, Trend, Notification).

Tech Stack:

Java 21
Maven
Clean Architecture
Enum-driven domain modeling

To Run the Project
mvn clean compile
mvn exec:java
exec-maven-plugin points to → "com.edorastech.vitalguard.Main"

VitalGuard demonstrates clean design, scalable alert logic, and professional-grade domain modeling suitable for enterprise healthcare monitoring systems.