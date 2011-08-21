package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 5/15/11
 */
public interface RaceResult {
    int getLength();

    /**
     * Lane results MUST contain the same laps count, i. e. including only
     * finished results.
     *
     * @see FinalLaneResult#getLapsCount()
     */
    int getLapsCount();

    LaneResults getLaneResults();
}
