package com.edorastech.vitalguard.security;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class BruteForceProtectionService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;

    private final Map<String, Integer> attempts = new HashMap<>();
    private final Map<String, LocalDateTime> lockTime = new HashMap<>();

    public boolean authenticate(String userId, String password) {

        if (isLocked(userId)) {
            return false;
        }

        // Demo password logic (replace later if needed)
        String correctPassword = "admin123";

        if (!correctPassword.equals(password)) {
            recordFailure(userId);
            return false;
        }

        recordSuccess(userId);
        return true;
    }

    public void recordFailure(String userId) {
        attempts.put(userId, attempts.getOrDefault(userId, 0) + 1);

        if (attempts.get(userId) >= MAX_ATTEMPTS) {
            lockTime.put(userId, LocalDateTime.now());
        }
    }

    public void recordSuccess(String userId) {
        attempts.remove(userId);
        lockTime.remove(userId);
    }

    public boolean isLocked(String userId) {
        if (!lockTime.containsKey(userId)) return false;

        LocalDateTime lockedAt = lockTime.get(userId);

        if (lockedAt.plusMinutes(LOCK_DURATION_MINUTES).isBefore(LocalDateTime.now())) {
            lockTime.remove(userId);
            attempts.remove(userId);
            return false;
        }

        return true;
    }

    public int getRemainingAttempts(String userId) {
        return MAX_ATTEMPTS - attempts.getOrDefault(userId, 0);
    }
}