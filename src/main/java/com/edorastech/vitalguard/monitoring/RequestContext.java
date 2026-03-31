package com.edorastech.vitalguard.monitoring;

public class RequestContext {

    private static final ThreadLocal<String> requestIdHolder = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        requestIdHolder.set(requestId);
    }

    public static String getRequestId() {
        return requestIdHolder.get();
    }

    public static void clear() {
        requestIdHolder.remove();
    }
}