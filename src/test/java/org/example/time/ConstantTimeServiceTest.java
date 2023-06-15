package org.example.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ConstantTimeServiceTest {

    private ConstantTimeService timeService;

    @BeforeEach
    void setup() {
        timeService = new ConstantTimeService();
    }

    @Test
    void nonNullByDefault() {
        assertNotNull(timeService.getNow());
    }

    @Test
    void isConstant() {
        Instant a = timeService.getNow();
        Instant b = timeService.getNow();
        assertEquals(a, b);
    }

    @Test
    void equalsToGivenValue() {
        Instant instant = Instant.now();
        timeService.setNow(instant);
        assertEquals(instant, timeService.getNow());
    }

    @Test
    void changesToNowIfNullGiven() {
        Instant instant = Instant.now().minusSeconds(1);
        timeService.setNow(instant);
        timeService.setNow(null);
        assertNotEquals(instant, timeService.getNow());
    }
}