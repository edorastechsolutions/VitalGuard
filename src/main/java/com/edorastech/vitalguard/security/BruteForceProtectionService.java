package com.edorastech.vitalguard.security;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BruteForceProtectionService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;

    // Track failed attempts
    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();

    // Track lock time
    private final Map<String, LocalDateTime> lockTime = new ConcurrentHashMap<>();

    /**
     * Check if account is locked
     */
    public boolean isLocked(String userId) {

        if (!lockTime.containsKey(userId)) {
            return false;
        }

        LocalDateTime lockedAt = lockTime.get(userId);

        // Check if lock expired
        if (lockedAt.plusMinutes(LOCK_DURATION_MINUTES).isBefore(LocalDateTime.now())) {
            // Unlock automatically
            lockTime.remove(userId);
            attempts.remove(userId);
            return false;
        }

        return true;
    }

    /**
     * Record failed attempt
     */
    public void recordFailure(String userId) {

        int currentAttempts = attempts.getOrDefault(userId, 0) + 1;
        attempts.put(userId, currentAttempts);

        if (currentAttempts >= MAX_ATTEMPTS) {
            lockTime.put(userId, LocalDateTime.now());
        }
    }

    /**
     * Reset attempts on success
     */
    public void recordSuccess(String userId) {
        attempts.remove(userId);
        lockTime.remove(userId);
    }

    /**
     * Get remaining attempts (for UI/logging)
     */
    public int getRemainingAttempts(String userId) {
        return MAX_ATTEMPTS - attempts.getOrDefault(userId, 0);
    }
}