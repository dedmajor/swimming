package ru.swimmasters.domain;

import org.joda.time.Duration;

/**
 * User: dedmajor
 * Date: 5/15/11
 */
public interface LapResult {
    LapSensorTimes getTimes();

    /**
     * All lap sensor times MUST have the same split length.
     *
     * @see LapSensorTime#getSplitLength()
     */
    int getSplitLength();

    Duration getReactionTime();
    Duration getLapTime();
    Duration getTotalTime();
}
