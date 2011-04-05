package ru.swimmasters.time;

import org.joda.time.DateTime;

/**
 * User: dedmajor
 * Date: 4/5/11
 */
public interface Clock {
    DateTime now();
    void skew(long offset);
}
