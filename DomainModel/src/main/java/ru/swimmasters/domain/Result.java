package ru.swimmasters.domain;

import org.joda.time.Duration;

/**
 * User: dedmajor
 * Date: 5/14/11
 */
public interface Result {
    MeetAthlete getAthlete();

    Event getEvent();

    Heat getHeat();
    Integer getLane();

    /**
     * @return zero if time is not available
     * @see {@link #isTimeAvailable()}
     */
    Duration getSwimTime();

    /**
     * Swim time available if is the status is {@link ResultStatus#QUALIFIED}.
     *
     * @return true if swim time is available (i. e. non-zero)
     */
    boolean isTimeAvailable();

    ResultStatus getStatus();
}
