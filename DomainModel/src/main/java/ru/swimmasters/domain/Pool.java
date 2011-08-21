package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public interface Pool {
    Integer getId();
    String getName();

    int getLaneMin();
    int getLaneMax();

    /**
     * Total count of meet lanes, i. e. from 2 to 8 there are 6 lanes.
     */
    int getMeetLanesCount();

    /**
     * @return true if lane number between min and max lane inclusive, false otherwise
     */
    boolean isMeetLane(int laneNumber);
}
