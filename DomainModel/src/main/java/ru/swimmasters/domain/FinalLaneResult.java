package ru.swimmasters.domain;

import org.joda.time.Duration;

/**
 * User: dedmajor
 * Date: 5/15/11
 */
public interface FinalLaneResult {
    int getLaneNumber();
    Duration getFinalTime();

    /**
     * The count of splits in the laps results (can be 0 if no splits
     * information available).
     */
    int getLapsCount();
    LapResults getLapResults();
}
