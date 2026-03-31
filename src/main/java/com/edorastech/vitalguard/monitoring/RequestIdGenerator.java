package com.edorastech.vitalguard.monitoring;

import java.util.UUID;

public class RequestIdGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }
}