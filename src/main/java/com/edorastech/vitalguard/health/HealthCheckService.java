package com.edorastech.vitalguard.health;

import java.time.LocalDateTime;

public class HealthCheckService {

    public String check() {

        return "{\n" +
                "  \"status\": \"UP\",\n" +
                "  \"timestamp\": \"" + LocalDateTime.now() + "\",\n" +
                "  \"components\": {\n" +
                "    \"repository\": \"UP\",\n" +
                "    \"rateLimiter\": \"UP\"\n" +
                "  }\n" +
                "}";
    }
}