package com.increff.pos.util;

import java.time.Clock;
import java.time.ZonedDateTime;

/**
 * Used to modify the current date time for testing purpose.
 * A user can set custom clock to timeTravel during the tests.
 * If no custom clock is set explicitly the timeNow() method returns the current ZonedDateTime.
*/
public enum DateTimeProvider {
    INSTANCE;

    public static DateTimeProvider getInstance() {
        return INSTANCE;
    }

    private final Clock defaultClock = Clock.systemDefaultZone();
    private Clock clock = defaultClock;

    public ZonedDateTime timeNow() {
        return ZonedDateTime.now(clock);
    }

    public void setTime(ZonedDateTime zonedDateTime) {
        this.setTime( Clock.fixed(zonedDateTime.toInstant(), zonedDateTime.getZone()));
    }

    public void setTime(Clock clock) {
        this.clock = clock;
    }

    public void resetTime() {
        this.clock = this.defaultClock;
    }

}
