package org.example.academic.system.logging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TUS-2395 - Verify logging infrastructure behavior")
class LoggingInfrastructureTest {

    @Test
    @DisplayName("Logger instance can be created successfully")
    void testLoggerCanBeCreated() {
        Logger logger = LoggerFactory.getLogger(LoggingInfrastructureTest.class);
        assertNotNull(logger);
    }

    @Test
    @DisplayName("Logger name matches the class name")
    void testLoggerNameMatchesClass() {
        Logger logger = LoggerFactory.getLogger(LoggingInfrastructureTest.class);
        assertEquals(LoggingInfrastructureTest.class.getName(), logger.getName());
    }

    @Test
    @DisplayName("Log messages at INFO level do not throw exceptions")
    void testInfoLogDoesNotThrow() {
        Logger logger = LoggerFactory.getLogger(LoggingInfrastructureTest.class);
        assertDoesNotThrow(() -> logger.info("Test info message"));
    }

    @Test
    @DisplayName("Log messages at WARN level do not throw exceptions")
    void testWarnLogDoesNotThrow() {
        Logger logger = LoggerFactory.getLogger(LoggingInfrastructureTest.class);
        assertDoesNotThrow(() -> logger.warn("Test warn message"));
    }

    @Test
    @DisplayName("Log messages at ERROR level do not throw exceptions")
    void testErrorLogDoesNotThrow() {
        Logger logger = LoggerFactory.getLogger(LoggingInfrastructureTest.class);
        assertDoesNotThrow(() -> logger.error("Test error message"));
    }

    @Test
    @DisplayName("Log messages at DEBUG level do not throw exceptions")
    void testDebugLogDoesNotThrow() {
        Logger logger = LoggerFactory.getLogger(LoggingInfrastructureTest.class);
        assertDoesNotThrow(() -> logger.debug("Test debug message"));
    }

    @Test
    @DisplayName("Multiple log messages in sequence do not throw exceptions")
    void testMultipleLogMessagesDoNotThrow() {
        Logger logger = LoggerFactory.getLogger(LoggingInfrastructureTest.class);
        assertDoesNotThrow(() -> {
            logger.info("First message");
            logger.warn("Second message");
            logger.error("Third message");
            logger.debug("Fourth message");
        });
    }

    @Test
    @DisplayName("Logger can be created for any class")
    void testLoggerCanBeCreatedForAnyClass() {
        Logger logger1 = LoggerFactory.getLogger(String.class);
        Logger logger2 = LoggerFactory.getLogger(Integer.class);
        assertNotNull(logger1);
        assertNotNull(logger2);
        assertNotEquals(logger1.getName(), logger2.getName());
    }
}
