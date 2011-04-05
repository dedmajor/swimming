package ru.swimmasters.time;

import org.joda.time.DateTime;

/**
 * User: dedmajor
 * Date: 4/5/11
 */
public class RealTimeClock implements Clock {
    @Override
    public DateTime now() {
        return new DateTime();
    }

    @Override
    public void skew(long offset) {
        throw new UnsupportedOperationException("skew is not supported for the real time clock");
    }
}
