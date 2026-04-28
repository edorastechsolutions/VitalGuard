package com.edorastech.vitalguard.testing;

import com.edorastech.vitalguard.audit.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuditLoggerTest {

    @Test
    void shouldLogEntries() {

        CentralAuditLogger logger = new CentralAuditLogger();

        logger.log(
                "req1",
                "P1",
                "TEST",
                AuditStatus.SUCCESS,
                null
        );

        assertFalse(logger.getLogs().isEmpty());
    }
}