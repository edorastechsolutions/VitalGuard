# VitalGuard Health Monitoring System

## Overview

VitalGuard is a modular Java-based health risk assessment system designed to demonstrate clean architecture, separation of concerns, and production-quality engineering practices.

The system evaluates patient vitals, computes a structured risk score, and maintains an in-memory audit trail of evaluations.

---

## Architecture

The project follows a modular design:

- **config/** → Risk thresholds and configuration constants  
- **model/** → Domain models and structured data objects  
- **risk/** → Risk Scoring Engine module  
- **audit/** → Vitals Audit Logger module  
- **Main.java** → Application entry point (user interaction)

Each module is strictly separated and independently maintainable.

---

## Module Responsibilities

### 1️⃣ Risk Scoring Engine
- Accepts evaluated vitals data
- Assigns severity points:
  - Mild → +1  
  - Moderate → +2  
  - Severe → +3  
- Computes total risk score
- Determines risk category: LOW / MODERATE / HIGH
- Returns a structured result object

### 2️⃣ Vitals Audit Logger
- Records:
  - patientId
  - timestamp
  - overall status (NORMAL / ALERT)
  - abnormal parameters
- Stores logs in memory
- Provides audit history retrieval by patient ID
- Maintains structured, readable records

---

## Scoring Logic

Each abnormal vital is assigned a severity level with defined point values.  
The total score determines the overall risk category based on configurable thresholds defined in `RiskThresholds.java`.

No hardcoded magic numbers are used in the scoring logic.

---

## Assumptions

- Console-based backend simulation
- Vitals provided manually by user input
- In-memory audit storage (no database integration)
- Positive numeric values assumed for vitals
- Single-user execution environment

---

## How to Run

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="com.vitalguard.Main"