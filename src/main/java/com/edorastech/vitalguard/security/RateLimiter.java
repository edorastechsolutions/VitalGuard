package com.edorastech.vitalguard.security;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

    private final Map<String, RequestInfo> requestMap = new ConcurrentHashMap<>();
    private static final int LIMIT = 10;
    private static final long WINDOW = 60_000; // 1 minute

    public boolean allowRequest(String key) {

        long now = Instant.now().toEpochMilli();

        requestMap.putIfAbsent(key, new RequestInfo(0, now));

        RequestInfo info = requestMap.get(key);

        synchronized (info) {
            if (now - info.startTime > WINDOW) {
                info.count = 0;
                info.startTime = now;
            }

            if (info.count >= LIMIT) {
                return false;
            }

            info.count++;
            return true;
        }
    }

    private static class RequestInfo {
        int count;
        long startTime;

        RequestInfo(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }
}