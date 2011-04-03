package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public class SwimMastersPool implements Pool {
    private int laneMin;
    private int laneMax;

    public void setLaneMin(int laneMin) {
        this.laneMin = laneMin;
    }

    public void setLaneMax(int laneMax) {
        this.laneMax = laneMax;
    }

    @Override
    public int getLaneMin() {
        return laneMin;
    }

    @Override
    public int getLaneMax() {
        return laneMax;
    }

    @Override
    public int getMeetLanesCount() {
        return laneMax - laneMin + 1;
    }

    @Override
    public boolean isMeetLane(int laneNumber) {
        return laneNumber <= laneMax && laneNumber >= laneMin;
    }
}
