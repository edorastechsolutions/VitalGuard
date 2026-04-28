package com.edorastech.vitalguard.resilience;

public class FailureSimulationEngine {

    public static void simulateNullInput() {
        throw new RuntimeException("Simulated NULL INPUT failure");
    }

    public static void simulateRepositoryFailure() {
        throw new RuntimeException("Simulated REPOSITORY failure");
    }

    public static void simulateUnauthorizedAccess() {
        throw new SecurityException("Simulated UNAUTHORIZED access");
    }

    public static void simulateRateLimit() {
        throw new RuntimeException("Simulated RATE LIMIT breach");
    }
}