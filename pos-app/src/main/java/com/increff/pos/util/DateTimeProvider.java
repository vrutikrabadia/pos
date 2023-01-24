package com.increff.pos.util;

import java.time.Clock;
import java.time.ZonedDateTime;

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
