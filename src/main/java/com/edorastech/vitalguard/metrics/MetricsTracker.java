package com.edorastech.vitalguard.metrics;

import java.util.concurrent.atomic.AtomicInteger;

public class MetricsTracker {

    private static final AtomicInteger totalRequests = new AtomicInteger();
    private static final AtomicInteger failedRequests = new AtomicInteger();
    private static final AtomicInteger rateLimited = new AtomicInteger();

    public static void incrementTotal() {
        totalRequests.incrementAndGet();
    }

    public static void incrementFailure() {
        failedRequests.incrementAndGet();
    }

    public static void incrementRateLimited() {
        rateLimited.incrementAndGet();
    }

    public static String snapshot() {
        return "{ " +
                "\"totalRequests\": " + totalRequests.get() + ", " +
                "\"failedRequests\": " + failedRequests.get() + ", " +
                "\"rateLimited\": " + rateLimited.get() +
                " }";
    }
}