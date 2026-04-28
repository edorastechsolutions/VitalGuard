package com.edorastech.vitalguard.testing;

import com.edorastech.vitalguard.security.RateLimiter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterTest {

    @Test
    void shouldAllowRequestsWithinLimit() {

        RateLimiter limiter = new RateLimiter();

        assertTrue(limiter.allowRequest("user1"));
    }

    @Test
    void shouldBlockAfterLimitExceeded() {

        RateLimiter limiter = new RateLimiter();

        for (int i = 0; i < 10; i++) {
            limiter.allowRequest("user1");
        }

        boolean blocked = !limiter.allowRequest("user1");

        assertTrue(blocked);
    }
}